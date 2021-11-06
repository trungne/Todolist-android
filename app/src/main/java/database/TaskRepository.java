package database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {
    private final TaskDao mTaskDao;
    private final LiveData<List<Task>> mAllTasks;

    public TaskRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
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
        executor.execute(() -> mTaskDao.insert(task));
    }

    public void delete(Task task){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> mTaskDao.delete(task));
    }

    public void deleteById(int id){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> mTaskDao.deleteById(id));
    }

    public void update(int id, String description, int priority, String finishedTime){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> mTaskDao.update(id, description, priority,finishedTime));
    }
}
