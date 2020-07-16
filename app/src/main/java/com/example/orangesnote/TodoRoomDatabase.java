package com.example.orangesnote;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Todo.class}, version=1 , exportSchema = false)
public abstract class TodoRoomDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();
    private static volatile TodoRoomDatabase INSTANCE;//volatile保证从内存读写，各线程中获取的database实例相同
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);//？

    static TodoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoRoomDatabase.class, "todo_database")
                            .addCallback(mRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback mRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            TodoDao dao = INSTANCE.todoDao();
            Todo todo = new Todo("侧滑删除", false);
            Todo todo1 = new Todo("拖动排序",false);
            Todo todo2 = new Todo("点小加号增添任务项",false);
            Todo todo3 = new Todo("祝你好运",false);
            dao.insert(todo);
            dao.insert(todo2);
            dao.insert(todo3);
            dao.insert(todo3);
        }


    };

}
