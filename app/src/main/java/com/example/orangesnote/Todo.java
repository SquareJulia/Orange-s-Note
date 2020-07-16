package com.example.orangesnote;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="todo_table")
public class Todo {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "todo_item")
    private String todoItem;

    @ColumnInfo(name= "is_done")
    private Boolean isDone;


    public Todo(String todoItem, Boolean isDone){
        this.todoItem=todoItem;
        this.isDone=isDone;
    }


    public String getTodoItem(){
        return this.todoItem;
    }

    public Boolean isDone(){
        return this.isDone;
    }

}