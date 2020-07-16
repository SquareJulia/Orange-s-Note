package com.example.orangesnote;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM todo_table")
    LiveData<List<Todo>> getAllTodos();

    @Query("SELECT * FROM todo_table WHERE todo_item LIKE :todoItem" )
    Todo findByName(String todoItem);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Query("SELECT * from todo_table ORDER BY todo_item ASC")
    LiveData<List<Todo>> getAlphabetizedTodos();

    @Query("DELETE FROM todo_table WHERE is_done LIKE :isDone")
    void deleteAllDones(Boolean isDone);

}

