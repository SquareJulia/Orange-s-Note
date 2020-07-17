package com.example.orangesnote;


import android.app.Application;

import androidx.lifecycle.LiveData;

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

    void delete(Todo todo) {
        TodoRoomDatabase.databaseWriteExecutor.execute(()->{
            mTodoDao.delete(todo);
        });
    }
}