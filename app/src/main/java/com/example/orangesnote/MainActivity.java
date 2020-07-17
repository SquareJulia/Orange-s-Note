package com.example.orangesnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private static TodoDao dao;
    private FloatingActionButton fab;
    private DialogFragment mDialog;
    private ItemTouchHelper itemTouchHelper;
    private static final String TAG = "MainActivity";

    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        dao = TodoRoomDatabase.getDatabase(getApplicationContext()).todoDao();
        int count = adapter.getItemCount();


        todoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                adapter.setTodos(todos);
            }

        });

        if( adapter.getItemCount() == 0){//TODO 为什么永远执行？
            new Thread(new Runnable() {
                @Override
                public void run() {
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
        }
    }
    //用来让recyclerview里面能对livedata进行修改
    public static TodoDao getDao(){
        return dao;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:
                //弹出警告框
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("确定清空所有项目？");
                dialog.setCancelable(true);
                dialog.setPositiveButton(R.string.org_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                dao.deleteAll();
                            }
                        }).start();
                    }
                });
                dialog.setNegativeButton(R.string.org_cancel_todo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                    }
                });
                dialog.show();
                break;
        }
        return true;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }





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
}