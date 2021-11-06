package database;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.lifecycle.LiveData;

import com.main.todolist.ReminderBroadcast;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {
    private final TaskDao mTaskDao;
    private final LiveData<List<Task>> mAllTasks;
    private final Application application;

    public TaskRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        this.application = application;
        this.mTaskDao = db.taskDao();
        this.mAllTasks = mTaskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return this.mAllTasks;
    }

    public LiveData<List<Task>> getAllTasksOrderedByPriority() {
        return mTaskDao.getAllTasksOrderedByPriority();
    }

    public LiveData<List<Task>> getAllTasksOrderedByCreatedTime() {
        return mTaskDao.getAllTasksOrderedByCreatedTime();
    }

    public LiveData<List<Task>> getAllTasksOrderedByFinishedTime() {
        return mTaskDao.getAllTasksOrderedByFinishedTime();
    }

    public void insert(Task task){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            long id = mTaskDao.insert(task);
            createPendingNotification(id, task.getTaskDescription(), task.getFinishedTime());
        });
    }

    public void deleteById(long id){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mTaskDao.deleteById(id);
            deletePendingNotification(id);
        });
    }

    public void update(long id, String description, int priority, String finishedTime){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mTaskDao.update(id, description, priority,finishedTime);
            createPendingNotification(id, description, LocalDateTime.parse(finishedTime));
        });
    }

    private void deletePendingNotification(long id) {
        Intent intent = new Intent(application.getApplicationContext(), ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                application.getApplicationContext(),
                (int) id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) application.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void createPendingNotification(long id, String description, LocalDateTime finishedTime){
        Intent intent = new Intent(application.getApplicationContext(), ReminderBroadcast.class);
        intent.putExtra(ReminderBroadcast.TASK_DESCRIPTION_KEY, description);
        intent.putExtra(ReminderBroadcast.TASK_ID_KEY, String.valueOf(id));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                application.getApplicationContext(),
                (int) id,
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
