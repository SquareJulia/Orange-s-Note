package com.example.orangesnote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TodoListAdapter adapter;
    private TodoViewModel todoViewModel;
    private FloatingActionButton fab;

    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        todoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                adapter.setTodos(todos);
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        //init recyclerview
        recyclerView = findViewById(R.id.org_recycler_view);
        adapter = new TodoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewTodoActivity.class);
                startActivityForResult(intent, NEW_TODO_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_TODO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "内容为空，未保存", Toast.LENGTH_SHORT).show();
            } else{
                Todo todo = new Todo(data.getStringExtra("new_todo"),false);
                todoViewModel.insert(todo);
            }
        }
    }
}