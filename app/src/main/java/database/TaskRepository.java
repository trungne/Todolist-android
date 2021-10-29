package database;

import android.app.Application;
import android.os.AsyncTask;

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

    public void insert(Task task){
        new insertAsyncTask(this.mTaskDao).execute(task);
    }

    public void delete(Task task){
        new deleteAsyncTask(this.mTaskDao).execute(task);
    }

    public void deleteById(int id){
        new deleteByIdAsyncTask(this.mTaskDao).execute(id);
    }

    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {
        private final TaskDao mAsyncTaskDao;

        insertAsyncTask(TaskDao dao){
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            this.mAsyncTaskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends  AsyncTask<Task, Void, Void> {
        private final TaskDao mAsyncTaskDao;

        deleteAsyncTask(TaskDao dao){
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            this.mAsyncTaskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class deleteByIdAsyncTask extends AsyncTask<Integer, Void, Void> {
        private final TaskDao mAsyncTaskDao;

        deleteByIdAsyncTask(TaskDao dao){
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            this.mAsyncTaskDao.deleteById(integers[0]);
            return null;
        }
    }

}
