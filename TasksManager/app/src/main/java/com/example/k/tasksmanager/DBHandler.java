package com.example.k.tasksmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k on 26.05.2016.
 */
public class DBHandler extends SQLiteOpenHelper {
    private final String TAG="kamil";
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="taskDB.db";
    private static final String TABLE_TASKS="tasks";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_TITLE="title";
    private static final String COLUMN_DESC="description";
    private static final String COLUMN_URL="url_to_icon";
    private static final String COLUMN_DATE="date_to_end";
    private static final String COLUMN_TOKEN="token";
Log log;

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+ TABLE_TASKS + "("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_TITLE+
                " TEXT, "+COLUMN_DESC+" TEXT, "+COLUMN_URL+" TEXT, "+COLUMN_DATE+ " TEXT, "+COLUMN_TOKEN + " TEXT "+");";
      log.i(TAG,query);

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXIST "+ TABLE_TASKS);
        onCreate(db);
    }


    public void addTask(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE,task.get_title());
        contentValues.put(COLUMN_DESC,task.get_description());
        contentValues.put(COLUMN_URL,task.get_url_to_icon());
        contentValues.put(COLUMN_DATE,task.get_date_to_end());
        contentValues.put(COLUMN_TOKEN,task.get_token());
       SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TASKS,null,contentValues);
        db.close();
    }

    public void deleteTask(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_TASKS + " WHERE " + COLUMN_TOKEN + "=\""
                + id + "\";");

    }


    public List<Task> loadFromDB(){
        List<Task> tasks=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query="SELECT * FROM " + TABLE_TASKS +" WHERE 1";

        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("title"))!=null)
        tasks.add(new Task(c.getString(c.getColumnIndex("title")),c.getString(c.getColumnIndex("description"))
                ,c.getString(c.getColumnIndex("url_to_icon")),c.getString(c.getColumnIndex("date_to_end")),c.getString(c.getColumnIndex("token"))));

            c.moveToNext();
        }

        db.close();
        return tasks;
    }
}
