package com.example.lcassiol.android_tasklist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText txtTask;
    private ListView taskList;
    private Button btnCreate;

    private SQLiteDatabase storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtTask = (EditText) findViewById(R.id.txtTask);
        taskList = (ListView) findViewById(R.id.taskList);
        btnCreate = (Button) findViewById(R.id.btnCreate);

        loadtasks();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTask(txtTask.getText().toString());

            }
        });
    }

    private void loadtasks(){
        try {
            storage = openOrCreateDatabase("TakeANote", MODE_PRIVATE, null);
            storage.execSQL("CREATE TABLE IF NOT EXISTS Task(id INTEGER PRIMARY KEY AUTOINCREMENT,taskItem varchar)");

            //String newTask = txtTask.getText().toString();
            //storage.execSQL("INSERT INTO Task(taskItem) VALUES ('" + newTask + "')");

            Cursor cursor = storage.rawQuery("SELECT * FROM Task", null);
            int indexColumnId = cursor.getColumnIndex("id");
            int indexColumnTaskItem = cursor.getColumnIndex("taskItem");

            cursor.moveToFirst();
            while(cursor!=null){
                Log.i("Logx", "ID: " + cursor.getString(indexColumnId) + " Task: " +  cursor.getString(indexColumnTaskItem));
                cursor.moveToNext();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addTask(String newTask){
            try {
                if(newTask.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Insira uma Tarefa!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Tarefa " + newTask + " inserida!", Toast.LENGTH_SHORT).show();
                    txtTask.setText("");
                    storage.execSQL("INSERT INTO Task(taskItem) VALUES ('" + newTask + "')");
                    loadtasks();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void Deletetask(){

    }

}
