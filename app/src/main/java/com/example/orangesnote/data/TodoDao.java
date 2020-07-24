package com.example.orangesnote.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.orangesnote.data.Todo;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM todo_table")
    LiveData<List<Todo>>  getAllTodos();

    @Query("SELECT * FROM todo_table WHERE todo_item LIKE :todoItem" )
    Todo find(String todoItem);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Todo todo);

    @Insert
    void insert(Todo... todo);

    @Query("DELETE FROM todo_table WHERE todo_item = :todoItem ")
    void delete(String todoItem);

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Query("DELETE FROM todo_table WHERE is_done = 1")
    void deleteAllDones();

    @Update
    void update(Todo todo);
}

