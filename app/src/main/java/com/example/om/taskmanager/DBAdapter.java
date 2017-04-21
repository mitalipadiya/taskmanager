package com.example.om.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by OM on 20/04/2017.
 */
public class DBAdapter {
    public static final String KEY_ROWID="_id";
    public static final String KEY_WORK ="work";
    public static final String KEY_DUE_DATE="duedate";
    public static final String KEY_TIMESTAMP="timestamp";

    private static final String DATABASE_NAME="TaskDB";
    private static final String TABLE_NAME="TblTask";
    private static final int DATABASE_VERSION=1;

    /*create table TblTask (_id integer primary key autoincrement, work text not null, duedate date not null, timestamp datetime default current_timestamp);*/
    private static final String CREATE_TABLE="create table "+TABLE_NAME+" ("+KEY_ROWID+" integer primary key autoincrement, "
            + KEY_WORK +" text not null, "+KEY_DUE_DATE+" date not null, "+KEY_TIMESTAMP+" datetime default CURRENT_TIMESTAMP);";
    private  final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    public DBAdapter(Context context){
        this.context=context;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table if exists TblTask");
            onCreate(db);
        }
    }
    public DBAdapter open(){
        db= DBHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        db.close();
    }

    //inserting data
    public long insertTask(String work, String date,String timestamp){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_WORK, work);
        initialValues.put(KEY_DUE_DATE, date);
        initialValues.put(KEY_TIMESTAMP, timestamp);
        return db.insert(TABLE_NAME, null, initialValues);
    }

    //deleting data
    public boolean deleteTask(String rowId){
        return db.delete(TABLE_NAME, KEY_ROWID + "=" +rowId, null)>0;
    }

    //select all tasks
    public Cursor getAllTaks() {
        return  db.query(TABLE_NAME,new String[]{KEY_ROWID,KEY_WORK,KEY_DUE_DATE,KEY_TIMESTAMP},null,null,null,null,KEY_TIMESTAMP+ " DESC");

    }

    //select task by id
    public Cursor getTaskById(String id){
        return db.query(TABLE_NAME,new String[]{KEY_ROWID,KEY_WORK,KEY_DUE_DATE,KEY_TIMESTAMP},KEY_ROWID+"=?", new String[]{id},null,null,null,null);
    }

    //update task
    public boolean updateTask(String rowId, String task, String date, String timestamp) {
        ContentValues args = new ContentValues();
        args.put(KEY_WORK, task);
        args.put(KEY_DUE_DATE, date);
        args.put(KEY_TIMESTAMP,timestamp);
        return db.update(TABLE_NAME, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
