package database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long insert(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM tasks WHERE id = :id")
    void deleteById(long id);

    @Query("SELECT * FROM TASKS WHERE id = :id")
    LiveData<Task> selectById(int id);

    @Query("UPDATE TASKS SET task_description = :description, priority = :priority, finished_time = :finishedTime" +
            " WHERE id = :id")
    void update(long id, String description, int priority, String finishedTime);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM tasks ORDER BY priority")
    LiveData<List<Task>> getAllTasksOrderedByPriority();

    @Query("SELECT * FROM tasks ORDER BY created_time")
    LiveData<List<Task>> getAllTasksOrderedByCreatedTime();

    @Query("SELECT * FROM tasks ORDER BY finished_time")
    LiveData<List<Task>> getAllTasksOrderedByFinishedTime();
}
