package com.example.om.taskmanager;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
public class NewTaskActivity extends AppCompatActivity {
EditText edit_task,edit_date;
    DBAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("NEW TASK");
        edit_task = (EditText) findViewById(R.id.edit_task_name);
        edit_date = (EditText) findViewById(R.id.edit_task_date);
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
                // TODO Auto-generated method stub
                new DatePickerDialog(NewTaskActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if ((edit_task.getText().toString().equals("")) && (edit_date.getText().toString().equals(""))) {
                    this.finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Are You Sure?");
                    builder.setMessage("Quit Without saving?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            NewTaskActivity.this.finish();
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
                }
                return true;
            case R.id.action_done_btn:
                //insert new task
                if ((edit_task.getText().toString().isEmpty()) && (edit_date.getText().toString().isEmpty())) {
                    Toast.makeText(NewTaskActivity.this, "Enter The details", Toast.LENGTH_LONG).show();
                } else {
                    db = new DBAdapter(NewTaskActivity.this);
                    db.open();
                    long id = db.insertTask(edit_task.getText().toString(), edit_date.getText().toString(), getDateTime());
                    if(id>0){
                        Toast.makeText(NewTaskActivity.this,"Task Added!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(NewTaskActivity.this,"Task not Added!",Toast.LENGTH_LONG).show();
                    }
                    db.close();
                    this.finish();
                }
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
