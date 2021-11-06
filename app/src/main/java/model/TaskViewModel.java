package model;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.main.todolist.MainActivity;
import com.main.todolist.ReminderBroadcast;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import database.Task;
import database.TaskRepository;

public class TaskViewModel extends AndroidViewModel {
    public static final int SORTED_BY_PRIORITY = 1;
    public static final int SORTED_BY_FINISH_TIME = 2;
    public static final int SORTED_BY_CREATED_TIME = 3;

    private final TaskRepository taskRepository;
    private final LiveData<List<Task>> allTasks;
    private final MutableLiveData<Integer> sortProperty = new MutableLiveData<>(SORTED_BY_PRIORITY);
    private final Application application;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        taskRepository = new TaskRepository(application);
        this.allTasks = Transformations.switchMap(sortProperty, property -> {
            switch (property){
                case SORTED_BY_PRIORITY:
                    return taskRepository.getAllTasksOrderedByPriority();
                case SORTED_BY_FINISH_TIME:
                    return taskRepository.getAllTasksOrderedByFinishedTime();
                case SORTED_BY_CREATED_TIME:
                    return taskRepository.getAllTasksOrderedByCreatedTime();
                default:
                    return taskRepository.getAllTasks();
            }
        });
    }

    public void setTaskSortingProperty(int property){
        sortProperty.setValue(property);
    }

    public LiveData<List<Task>> getAllTasks(){
        return this.allTasks;
    }

    public void update(int id, String description, int priority, String finishedTime){
        createPendingNotification(id, description, LocalDateTime.parse(finishedTime));
        this.taskRepository.update(id, description, priority, finishedTime);
    }

    public void insert(Task task){
        // create notification for task
        createPendingNotification(task.getId(), task.getTaskDescription(), task.getFinishedTime());
        this.taskRepository.insert(task);
    }

    public void delete(Task task){
        this.taskRepository.delete(task);
    }

    public void deleteById(int id){
        this.taskRepository.deleteById(id);
    }

    private void createPendingNotification(int id, String description, LocalDateTime finishedTime){
        Intent intent = new Intent(application.getApplicationContext(), ReminderBroadcast.class);
        intent.putExtra(ReminderBroadcast.TASK_DESCRIPTION_KEY, description);
        intent.putExtra(ReminderBroadcast.TASK_ID_KEY, id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                application.getApplicationContext(),
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) application.getSystemService(ALARM_SERVICE);

        long timeAtCreatingTask = System.currentTimeMillis();

        long taskFinishedTime = finishedTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // don't create notification pending for tasks in the past
        if (taskFinishedTime < timeAtCreatingTask) {
            return;
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, taskFinishedTime, pendingIntent);
    }
}
