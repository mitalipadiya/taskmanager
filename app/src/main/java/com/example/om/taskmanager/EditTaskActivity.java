package com.example.om.taskmanager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by OM on 20/04/2017.
 */
public class EditTaskActivity extends AppCompatActivity {
    EditText edit_task,edit_date;
    DBAdapter db;
    static String id;
    static String taskname,taskdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("EDIT TASK");
        edit_task = (EditText) findViewById(R.id.edit_task_name);
        edit_date = (EditText) findViewById(R.id.edit_task_date);
        db=new DBAdapter(EditTaskActivity.this);
        Bundle bundle =  getIntent().getExtras();
        id =bundle.getString("id");
        //get row by id from database
        db.open();
        Cursor c= db.getTaskById(id);
            if (c != null)
              c.moveToFirst();
        edit_task.setText(c.getString(1));
        edit_date.setText(c.getString(2));
        taskname=edit_task.getText().toString();
        taskdate=edit_date.getText().toString();

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edit_date.setText(sdf.format(calendar.getTime()));
            }

        };
        edit_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //open datepicker after clicking on ediText
                new DatePickerDialog(EditTaskActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_delete_update, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            case R.id.action_done_btn:
                //edit task
                String task = edit_task.getText().toString();
                String date = edit_date.getText().toString();
                if(taskname.equals(task)&& taskdate.equals(date)) {
                    this.finish();
                }else{
                    if(task.equals("")){
                        Toast.makeText(EditTaskActivity.this,"Task name needed!",Toast.LENGTH_LONG).show();
                    }else {
                        db.open();
                        boolean flag = db.updateTask(id, task, date, getDateTime());
                        if (flag == true) {
                            Toast.makeText(EditTaskActivity.this, "Task Updated!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditTaskActivity.this, "Task not Updated!", Toast.LENGTH_LONG).show();
                        }
                        db.close();
                        this.finish();
                    }
                }
                return true;
            case R.id.action_delete:
                //delete task
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        db.open();
                        boolean delFlag = db.deleteTask(id);
                        if(delFlag==true){
                            Toast.makeText(EditTaskActivity.this,"Task Deleted!",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(EditTaskActivity.this,"Task not Deleted!",Toast.LENGTH_LONG).show();
                        }
                        db.close();
                        EditTaskActivity.this.finish();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
        }
        return true;
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
