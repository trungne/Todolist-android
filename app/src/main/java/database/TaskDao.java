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
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM tasks WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();
}
