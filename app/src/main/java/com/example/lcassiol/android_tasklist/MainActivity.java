package com.example.lcassiol.android_tasklist;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText txtTask;
    private ListView taskList;
    private Button btnCreate;

    private SQLiteDatabase storage;

    private ArrayAdapter<String> itemAdapter;
    private ArrayList<Integer> ids;
    private ArrayList<String> itens;

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

        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                alertConfirmAction(position);
                return false;
            }
        });
    }

    private void loadtasks(){
        try {
            storage = openOrCreateDatabase("TakeANote", MODE_PRIVATE, null);
            storage.execSQL("CREATE TABLE IF NOT EXISTS Task(id INTEGER PRIMARY KEY AUTOINCREMENT,taskItem varchar)");

            Cursor cursor = storage.rawQuery("SELECT * FROM Task ORDER BY id DESC", null);
            int indexColumnId = cursor.getColumnIndex("id");
            int indexColumnTaskItem = cursor.getColumnIndex("taskItem");

            itens = new ArrayList<>();
            ids = new ArrayList<>();

            itemAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, itens);
            taskList.setAdapter(itemAdapter);

            cursor.moveToFirst();
            while(cursor!=null){
                Log.i("Logx", "ID: " + cursor.getString(indexColumnId) + " Task: " +  cursor.getString(indexColumnTaskItem));
                ids.add(Integer.parseInt(cursor.getString(indexColumnId)));
                itens.add(cursor.getString(indexColumnTaskItem));
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

    private void deletetask(Integer id){
            try {
                storage.execSQL("DELETE FROM Task where id = " + id);
                Toast.makeText(MainActivity.this, "Tarefa removida!", Toast.LENGTH_SHORT).show();
                loadtasks();
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void alertConfirmAction(Integer id){
        String selectTask = itens.get(id);
        final Integer numberId = id;

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Aviso!")
                .setMessage("Deseja remover '" + selectTask + "' ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletetask(ids.get(numberId));
                    }
                }).setNegativeButton("Não", null).show();
    }

}
