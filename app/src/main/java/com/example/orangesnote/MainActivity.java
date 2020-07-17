package com.example.orangesnote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.orangesnote.helper.OnStartDragListener;
import com.example.orangesnote.helper.mItemTouchHelperCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, AddItemDialogFragment.NoticeDialogListener, OnStartDragListener {

    private RecyclerView recyclerView;
    private TodoListAdapter adapter;
    private TodoViewModel todoViewModel;
    private TodoDao dao;
    private FloatingActionButton fab;
    private DialogFragment mDialog;
    private ItemTouchHelper itemTouchHelper;

    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao = TodoRoomDatabase.getDatabase(getApplicationContext()).todoDao();
                Todo todo = new Todo("侧滑删除", false);
                Todo todo1 = new Todo("拖动排序", false);
                Todo todo2 = new Todo("点小加号增添任务项", false);
                Todo todo3 = new Todo("任务名称不可以重复哦", false);
                dao.insert(todo);
                dao.insert(todo1);
                dao.insert(todo2);
                dao.insert(todo3);
            }
        }).start();
        todoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                adapter.setTodos(todos);
            }

        });
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init recyclerview
        recyclerView = findViewById(R.id.org_recycler_view);
        adapter = new TodoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper.Callback callback = new mItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //init fab
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    /**失败了
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_TODO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "内容为空，未保存", Toast.LENGTH_SHORT).show();
            } else {
                Todo todo = new Todo(data.getStringExtra("new_todo"), false);
                todoViewModel.insert(todo);
            }
        }
    }
     **/



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                showAddItemDialog();
                break;
        }
    }


    /**
     * Create an instance of the dialog fragment and show it
     */
    public void showAddItemDialog() {
        mDialog = new AddItemDialogFragment();
        mDialog.show(getSupportFragmentManager(), "AddItemDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(),"未保存",Toast.LENGTH_SHORT).show();
    }
}