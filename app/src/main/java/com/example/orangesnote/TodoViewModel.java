package com.example.orangesnote;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private TodoRepository mRepository; // hold reference to Repository
    private LiveData<List<Todo>> mAllTodos;

    public TodoViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mAllTodos = mRepository.getAllTodos();
    }

    LiveData<List<Todo>> getAllTodos() {
        return mAllTodos;
    }

    public void insert(Todo todo) {
        mRepository.insert(todo);
    }

    public void delete(String todoItem) { mRepository.delete(todoItem);}

    public void deleteAll(){
        mRepository.deleteAll();
    }
}