package com.example.orangesnote;

import java.text.Collator;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.orangesnote.data.Todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private TodoRepository mRepository; // hold reference to Repository
    private LiveData<List<Todo>> mAllTodos;
    private List<Todo> cache = new ArrayList<>();//把它和MutableLiveData同步，用来修改再setValue

    enum OrderBy {
        priority, time, dict, done
    }

    public TodoViewModel(Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mAllTodos = mRepository.getAllTodos();
        //cache = mRepository.getAllTodos().getValue();
        // mAllTodos.setValue(cache);
    }

    LiveData<List<Todo>> getAllTodos() {
        return mAllTodos;
    }

    //用于每一次修改数据库以后从数据库拉来数据更新cache和mAllTodos
    //public void refresh2List(){
    //    cache = mRepository.getAllTodos().getValue();
    //    mAllTodos.setValue(cache);
    //}

    public void insert(Todo todo) {
        mRepository.insert(todo);
    }

    public void update(Todo todo) {
        mRepository.update(todo);
    }

    public void delete(String todoItem) {
        mRepository.delete(todoItem);
    }

    public void deleteAll() {
        mRepository.deleteAll();

    }

    public void deleteAllDones() {
        mRepository.deleteAllDones();
    }


    public Todo find(String todoItem) {
        for (Todo todo : mAllTodos.getValue())
            if (TextUtils.equals(todoItem, todo.getTodoItem()))
                return todo;
        return null;
    }
    //return mRepository.find(todoItem);



    //暂时排序，只对cache和mAllTodos进行操作
    public void order(OrderBy orderBy) {
        cache = mAllTodos.getValue();
        //对cache进行排序
        if (orderBy == OrderBy.dict) {//中文,有现成的比较器
            final Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);
            Collections.sort(cache, CHINA_COMPARE);
        } else {
            Collections.sort(cache, new Comparator<Todo>() {
                @Override
                public int compare(Todo todo1, Todo todo2) {
                    switch (orderBy) {
                        case priority:
                            return todo2.getPriority() - todo1.getPriority();
                        case done:
                            return todo1.isDone().compareTo(todo2.isDone());
                        default:
                            return 0;
                    }
                }
            });
        }

        //这里好像不太好，在viewmodel得到了activity的索引，去更新recyclerview的list
        MainActivity.getAdapter().setTodos(cache);
    }

}