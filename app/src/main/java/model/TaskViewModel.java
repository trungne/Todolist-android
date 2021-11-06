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

    public TaskViewModel(@NonNull Application application) {
        super(application);

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

    public void update(long id, String description, int priority, String finishedTime){
        this.taskRepository.update(id, description, priority, finishedTime);
    }

    public void insert(Task task){
        // create notification for task
        this.taskRepository.insert(task);
    }


    public void deleteById(long id){
        this.taskRepository.deleteById(id);
    }


}
