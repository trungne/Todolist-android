package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "tasks")
public class Task {
    @Ignore
    public static final int TASK_PRIORITY_LOW = 0;
    @Ignore
    public static final int TASK_PRIORITY_MEDIUM = 1;
    @Ignore
    public static final int TASK_PRIORITY_HIGH = 2;

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "task_description")
    public String taskDescription;

    public int priority;

    @ColumnInfo(name = "created_time")
    public LocalDateTime createdTime;

    @ColumnInfo(name = "finished_time")
    public LocalDateTime finishedTime;

    @ColumnInfo(name = "is_finished")
    public boolean isFinished;

    public Task(String taskDescription, int priority, LocalDateTime createdTime, LocalDateTime finishedTime){
        this.taskDescription = taskDescription;
        this.priority = priority;
        this.createdTime = createdTime;
        this.finishedTime = finishedTime;
        this.isFinished = false;
    }

    // getters
    public int getId() {
        return id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getPriority() {
        return priority;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getFinishedTime() {
        return finishedTime;
    }

    public boolean isFinished() {
        return isFinished;
    }

    // setters
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public void setFinishedTime(LocalDateTime finishedTime) {
        this.finishedTime = finishedTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFinished(){
        this.isFinished = true;
    }
}
