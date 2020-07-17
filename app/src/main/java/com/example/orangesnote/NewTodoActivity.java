package com.example.orangesnote;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewTodoActivity extends AppCompatActivity {
    private EditText editNewTodo;
    private Button saveNewTodo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_new_todo);
        editNewTodo = (EditText)findViewById(R.id.edit_new_todo);
        saveNewTodo = (Button)findViewById(R.id.save_todo_button);
        /**
        saveNewTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (TextUtils.isEmpty(editNewTodo.getText())) {
                    setResult(RESULT_CANCELED , intent);
                } else {
                    String todoItem = editNewTodo.getText().toString();
                    intent.putExtra("new_todo", todoItem);
                    setResult(RESULT_OK , intent);
                }
                finish();
            }
        });
         **/
    }
}
