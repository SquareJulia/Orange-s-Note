package com.example.orangesnote;


import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.orangesnote.data.Todo;
import com.example.orangesnote.data.TodoDao;

import java.util.ArrayList;
import java.util.List;

class TodoRepository {

    private TodoDao mTodoDao;
    private LiveData<List<Todo>> mAllTodos;

    TodoRepository(Application application) {
        TodoRoomDatabase db = TodoRoomDatabase.getDatabase(application);
        mTodoDao = db.todoDao();
        mAllTodos = mTodoDao.getAllTodos();
    }


    LiveData<List<Todo>> getAllTodos() {
        return mAllTodos;
    }

    void insert(Todo todo) {
        TodoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTodoDao.insert(todo);
        });
    }

    void update(Todo todo){
        TodoRoomDatabase.databaseWriteExecutor.execute(()->{
            mTodoDao.update(todo);
        });
    }


    void delete(String todoItem) {
        TodoRoomDatabase.databaseWriteExecutor.execute(()->{
            mTodoDao.delete(todoItem);
        });
    }

    void deleteAll(){
        TodoRoomDatabase.databaseWriteExecutor.execute(()->{
            mTodoDao.deleteAll();
        });
    }


    void deleteAllDones(){
        TodoRoomDatabase.databaseWriteExecutor.execute(()->{
            mTodoDao.deleteAllDones();
        });
    }

    Todo find(String todoItem){
        return mTodoDao.find(todoItem);
    }



}