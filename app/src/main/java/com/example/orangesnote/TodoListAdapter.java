package com.example.orangesnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orangesnote.data.Todo;
import com.example.orangesnote.helper.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> implements ItemTouchHelperAdapter{

    private final LayoutInflater mInflater;
    private List<Todo> mTodos;

    class TodoViewHolder extends RecyclerView.ViewHolder {
        private TextView todoItemText;
        private CheckBox todoItemCheck;
        private TextView todoItemPriority;


        private TodoViewHolder(View itemView) {
            super(itemView);
            todoItemText = itemView.findViewById(R.id.org_item_text);
            todoItemCheck = itemView.findViewById(R.id.org_checkbox);
            todoItemPriority = itemView.findViewById(R.id.org_item_priority);
        }
    }


    TodoListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.from(parent.getContext()).inflate(R.layout.org_todo_item, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        if (mTodos != null) {
            Todo current = mTodos.get(position);
            holder.todoItemText.setText(current.getTodoItem());
            holder.todoItemCheck.setTag(position);//标记checkbox的位置
            holder.todoItemCheck.setChecked(current.isDone());
            holder.todoItemPriority.setText(Integer.toString(current.getPriority()));
            holder.todoItemCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos = (Integer)holder.getAdapterPosition();
                    //Integer pos = (Integer)holder.todoItemCheck.getTag();
                    Todo current = mTodos.get(pos);
                    current.changeDone();
                    updateFromVM(current);
                }
            });

        }
    }

    //把排序方式保存到数据库，用adapter里面的list替换数据库，通过detele和insert实现
    void saveOrder(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Todo> cache = mTodos;//保存一份缓存，一会viewmodel全删了以后recyclerview观察着也删掉了
                MainActivity.getTodoViewModel().deleteAll();

                for(Todo todo:cache)
                    MainActivity.getTodoViewModel().insert(todo);
            }
        }).start();
    }



    void setTodos(List<Todo> todos) {
        mTodos = todos;
        notifyDataSetChanged();
    }

    private void updateFromVM(Todo todo){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.getTodoViewModel().update(todo);
            }
        }).start();
    }
    private void deleteFromVM(String todoItem){
        new Thread(new Runnable(){
            @Override
            public void run() {
                MainActivity.getTodoViewModel().delete(todoItem);
            }
        }).start();
    }

    @Override
    public void onItemDismiss(int position) {
        deleteFromVM(mTodos.get(position).getTodoItem());//从ViewModel里删除
        mTodos.remove(position);
        notifyItemRemoved(position);
    }



    @Override
    public  void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mTodos, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mTodos, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }


    @Override
    public int getItemCount() {
        if (mTodos != null)
            return mTodos.size();
        else return 0;
    }
}