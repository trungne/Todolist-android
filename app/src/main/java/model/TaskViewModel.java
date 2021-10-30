package model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import database.Task;
import database.TaskRepository;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository taskRepository;
    private final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        this.allTasks = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return this.allTasks;
    }

    public void update(int id, String description, int priority, String finishedTime){
        this.taskRepository.update(id, description, priority, finishedTime);
    }

    public void insert(Task task){
        this.taskRepository.insert(task);
    }

    public void delete(Task task){
        this.taskRepository.delete(task);
    }

    public void deleteById(int id){
        this.taskRepository.deleteById(id);
    }
}
