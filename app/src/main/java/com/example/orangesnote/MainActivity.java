package com.example.orangesnote;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Worker;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.orangesnote.data.Todo;
import com.example.orangesnote.helper.OnStartDragListener;
import com.example.orangesnote.helper.mItemTouchHelperCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, AddItemDialogFragment.NoticeDialogListener, OnStartDragListener{

    private Todo temp;
    private RecyclerView recyclerView;
    private static TodoListAdapter adapter;
    private static TodoViewModel todoViewModel;
    //UI
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private Button button_search;
    private Button button_cancel_search;
    private EditText searchItem;
    private LinearLayout searchFrame;
    //Fragment
    private DialogFragment mDialog;

    private ItemTouchHelper itemTouchHelper;
    private static final String TAG = "MainActivity";

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                Todo todo = new Todo("侧滑删除", true, 4);
                Todo todo1 = new Todo("拖动排序", false, 7);
                Todo todo2 = new Todo("点小加号增添任务项", true, 2);
                Todo todo3 = new Todo("任务名称不可以重复哦", false, 9);
                Todo todo5 = new Todo("温馨提示：请先保存排序方式再点确认是否完成", true, 1);
                todoViewModel.insert(todo);
                todoViewModel.insert(todo1);
                todoViewModel.insert(todo2);
                todoViewModel.insert(todo3);
                todoViewModel.insert(todo5);
            }
        }).start();


    }


    public static TodoViewModel getTodoViewModel() {
        return todoViewModel;
    }

    private void initView() {
        setContentView(R.layout.activity_main);

        //init toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        //init search
        button_search = findViewById(R.id.search_save);
        button_cancel_search = findViewById(R.id.search_cancel);
        searchItem = findViewById(R.id.search_edit);
        searchFrame = findViewById(R.id.searchFrame);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //用于让ViewModel得到adapter，操作界面
    public static TodoListAdapter getAdapter() {
        return adapter;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Log.d(TAG, "onOptionsItemSelected: search");
                searchFrame.setVisibility(View.VISIBLE);
                button_cancel_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchFrame.setVisibility(View.INVISIBLE);
                    }
                });
                button_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String todoItem = searchItem.getText().toString();
                        if (TextUtils.isEmpty(todoItem)) {
                            Toast.makeText(getApplicationContext(), "内容为空，已取消", Toast.LENGTH_SHORT).show();
                        } else {
                            temp = null;
                            temp=todoViewModel.find(todoItem);
                            if (temp == null) {
                                Toast.makeText(getApplicationContext(), "未找到", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog = new AddItemDialogFragment();
                                mDialog.show(getSupportFragmentManager(), "AddItemDialogFragment");
                            }
                        }
                        searchFrame.setVisibility(View.INVISIBLE);
                    }
                });
                break;

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
                                todoViewModel.deleteAll();
                            }
                        }).start();
                        Snackbar.make(recyclerView, "已清空", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
                dialog.setNegativeButton(R.string.org_cancel_todo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                break;
            case R.id.delete_all_dones:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        todoViewModel.deleteAllDones();
                    }
                }).start();
                Snackbar.make(recyclerView, "已删除所有完成项", Snackbar.LENGTH_SHORT)
                        .show();
                break;
            case R.id.order_by_priority:
                todoViewModel.order(TodoViewModel.OrderBy.priority);
                break;
            case R.id.order_by_time:
                Toast.makeText(getApplicationContext(), "功能还没开发出来", Toast.LENGTH_SHORT).show();
                break;
            case R.id.order_by_dictionary:
                todoViewModel.order(TodoViewModel.OrderBy.dict);
                break;
            case R.id.order_by_done:
                todoViewModel.order(TodoViewModel.OrderBy.done);
                break;
            case R.id.save_order:
                adapter.saveOrder();
                break;

        }
        return true;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    public Todo getTemp(){
        return temp;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                mDialog = new AddItemDialogFragment();
                mDialog.show(getSupportFragmentManager(), "AddItemDialogFragment");
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "未保存", Toast.LENGTH_SHORT).show();
    }


}