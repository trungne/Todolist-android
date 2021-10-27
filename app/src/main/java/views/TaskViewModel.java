package views;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import database.Task;
import database.TaskRepository;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        this.allTasks = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return this.allTasks;
    }

    public void insert(Task task){
        this.taskRepository.insert(task);
    }

    // TODO: delete task by id
    public void delete(Task task){
        this.taskRepository.delete(task);
    }
}
