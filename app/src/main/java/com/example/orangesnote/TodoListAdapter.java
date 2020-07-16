package com.example.orangesnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    class TodoViewHolder extends RecyclerView.ViewHolder {
        private TextView todoItemText;
        private CheckBox todoItemCheck;
        private com.google.android.material.card.MaterialCardView todoItemCard;

        private TodoViewHolder(View itemView) {
            super(itemView);
            todoItemCard = itemView.findViewById(R.id.org_todo_item);
            todoItemText = itemView.findViewById(R.id.org_item_text);
            todoItemCheck = itemView.findViewById(R.id.org_checkbox);
        }
    }

    private final LayoutInflater mInflater;
    private List<Todo> mTodos;

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
            if(current.isDone()){
                holder.todoItemCheck.setChecked(true);
            }else{
                holder.todoItemCheck.setChecked(false);
            }
        } else {
            holder.todoItemText.setText("No Content");
            holder.todoItemCheck.setChecked(false);
        }
    }

    void setTodos(List<Todo> todos) {
        mTodos = todos;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (mTodos != null)
            return mTodos.size();
        else return 0;
    }
}