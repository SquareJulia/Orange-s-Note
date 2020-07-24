package com.example.orangesnote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orangesnote.data.Todo;

public class AddItemDialogFragment extends DialogFragment {

    private EditText editText;
    private EditText editPriority;
    private int priority = 5;
    private Todo todo=null;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NoticeDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_item_dialog, null);
        editText = view.findViewById(R.id.edit_new_todo_dialog);
        editPriority = view.findViewById(R.id.edit_priority);

        MainActivity activity = (MainActivity)getActivity();
        todo = activity.getTemp();
        if(todo!=null){
            editText.setText(todo.getTodoItem());
            editPriority.setText(Integer.toString(todo.getPriority()));
        }
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.org_save_todo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String todoItem = editText.getText().toString();
                        try {
                            priority = Integer.parseInt(editPriority.getText().toString());
                        } catch (NumberFormatException exception) {
                            AddItemDialogFragment.this.getDialog().cancel();
                        }
                        if (TextUtils.isEmpty(todoItem)) {//todoItem为空，取消保存
                            listener.onDialogNegativeClick(AddItemDialogFragment.this);
                            AddItemDialogFragment.this.getDialog().cancel();
                        } else {
                            if(priority<1 || priority>10) {//priority超出范围，提示并设置为默认值5
                                priority = 5;
                                Toast.makeText(getContext(), R.string.org_priority_warning, Toast.LENGTH_SHORT).show();
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    TodoViewModel todoViewModel = MainActivity.getTodoViewModel();
                                    Todo todo = new Todo(editText.getText().toString(), false, priority);
                                    todoViewModel.insert(todo);
                                }
                            }).start();
                            listener.onDialogPositiveClick(AddItemDialogFragment.this);
                        }
                    }
                })
                .setNegativeButton(R.string.org_cancel_todo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(AddItemDialogFragment.this);
                        AddItemDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


    //empty constructor
    public AddItemDialogFragment() {
    }

    public AddItemDialogFragment(Todo todo){
        this.todo = todo;
    }

}


