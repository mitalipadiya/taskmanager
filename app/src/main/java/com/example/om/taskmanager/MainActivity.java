package com.example.om.taskmanager;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by OM on 20/04/2017.
 */
public class MainActivity extends AppCompatActivity {
    String[] valId, valTaskName, valTaskDate;
    CustomListAdapter adapter;
    DBAdapter db;
    ListView taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=new DBAdapter(this);
        taskList = (ListView) findViewById(R.id.list_task);
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String id = valId[position];
                Intent i = new Intent(MainActivity.this,EditTaskActivity.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add new Task
                Intent i = new Intent(MainActivity.this,NewTaskActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //find all task intact upon closing the application or device restart
        db.open();
        Cursor c = db.getAllTaks();
        valId = new String[c.getCount()];
        valTaskDate = new String[c.getCount()];
        valTaskName = new String[c.getCount()];
        int cnt=0;
        if(c.moveToFirst()){
            do{
                valId[cnt]= c.getString(0);
                valTaskName[cnt]=c.getString(1);
                valTaskDate[cnt]=c.getString(2);
                cnt++;
            }while(c.moveToNext());
            db.close();
        }else{
            //No task exist
            Toast.makeText(MainActivity.this,"No task exists",Toast.LENGTH_LONG).show();
        }
        adapter = new CustomListAdapter(MainActivity.this,valTaskName,valTaskDate);
        taskList.setAdapter(adapter);
        //update listview
        adapter.notifyDataSetChanged();
    }

}
