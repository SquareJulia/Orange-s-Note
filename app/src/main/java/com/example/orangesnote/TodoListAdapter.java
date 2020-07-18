package com.example.orangesnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orangesnote.helper.ItemTouchHelperAdapter;
import com.google.android.material.card.MaterialCardView;

import java.util.Collections;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>
        implements ItemTouchHelperAdapter {

    private final LayoutInflater mInflater;
    private List<Todo> mTodos;

    class TodoViewHolder extends RecyclerView.ViewHolder {
        private TextView todoItemText;
        private CheckBox todoItemCheck;

        private TodoViewHolder(View itemView) {
            super(itemView);
            todoItemText = itemView.findViewById(R.id.org_item_text);
            todoItemCheck = itemView.findViewById(R.id.org_checkbox);
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
            holder.todoItemCheck.setChecked(current.isDone());
        } else {
            holder.todoItemText.setText("No Content");
            holder.todoItemCheck.setChecked(false);
        }
    }

    void setTodos(List<Todo> todos) {
        mTodos = todos;
        notifyDataSetChanged();
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