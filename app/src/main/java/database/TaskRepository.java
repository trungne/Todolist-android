package database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {
    private TaskDao mTaskDao;
    private LiveData<List<Task>> mAllTasks;

    public TaskRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        this.mTaskDao = db.taskDao();
        this.mAllTasks = mTaskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return this.mAllTasks;
    }
    // add async operations

    public void insert(Task task){
        this.mTaskDao.insert(task);
    }

    // TODO: delete by ID
    public void delete(Task task){
        this.mTaskDao.delete(task);
    }
}
