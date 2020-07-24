package com.example.orangesnote.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName="todo_table")
public class Todo {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "todo_item")
    private String todoItem;

    @ColumnInfo(name= "is_done")
    private Boolean isDone;

    @NonNull
    @ColumnInfo()
    private int priority;

    public Todo(String todoItem, Boolean isDone,int priority){
        this.todoItem=todoItem;
        this.isDone=isDone;
        this.priority = priority;
    }

    public int getPriority(){return this.priority;}

    public void changeDone(){
        if(this.isDone)
            this.isDone=false;
        else
            this.isDone=true;
    }

    public String getTodoItem(){
        return this.todoItem;
    }

    public Boolean isDone(){
        return this.isDone;
    }

}