package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "tasks")
public class Task {
    @Ignore
    public static int TASK_PRIORITY_LOW = 0;
    @Ignore
    public static int TASK_PRIORITY_MEDIUM = 1;
    @Ignore
    public static int TASK_PRIORITY_HIGH = 2;

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "task_description")
    public String taskDescription;

    @ColumnInfo
    public int priority;

    @ColumnInfo(name = "created_time")
    public LocalDateTime createdTime;

    @ColumnInfo(name = "finished_time")
    public LocalDateTime finishedTime;

    public Task(String description, int priority, LocalDateTime createdTime, LocalDateTime finishedTime){
        this.taskDescription = description;
        this.priority = priority;
        this.createdTime = createdTime;
        this.finishedTime = finishedTime;
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
}
