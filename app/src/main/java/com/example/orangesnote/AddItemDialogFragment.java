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
import android.view.ViewGroup;
import android.widget.EditText;

public class AddItemDialogFragment extends DialogFragment {

    private EditText editText;

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

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.org_save_todo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String todoItem = editText.getText().toString();
                        if(TextUtils.isEmpty(todoItem)){
                            listener.onDialogNegativeClick(AddItemDialogFragment.this);
                            AddItemDialogFragment.this.getDialog().cancel();
                        }
                        else{
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    TodoDao dao = TodoRoomDatabase.getDatabase(getActivity()).todoDao();
                                    Todo todo=new Todo(editText.getText().toString(),false);
                                    dao.insert(todo);
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

}


