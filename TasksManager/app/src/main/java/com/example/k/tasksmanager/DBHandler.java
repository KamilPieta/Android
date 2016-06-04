package com.example.k.tasksmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k on 26.05.2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="task.db";
    private static final String TABLE_TASKS="tasks";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_TITLE="title";
    private static final String COLUMN_DESC="description";
    private static final String COLUMN_URL="url_to_icon";
    private static final String COLUMN_DATE="date_to_end";
    private static final String COLUMN_START_DATE="_createdTime";
    private RequestQueue queue ;
Log log;

    private final String TAG="kamil";
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        queue = Volley.newRequestQueue(context);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+ TABLE_TASKS + "("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_TITLE+
                " TEXT, "+COLUMN_DESC+" TEXT, "+COLUMN_URL+" TEXT, "+COLUMN_DATE+ " TEXT, "+COLUMN_START_DATE+" TEXT );";


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
        contentValues.put(COLUMN_START_DATE,task.get_created());
        SQLiteDatabase db = getWritableDatabase();

        log.i(TAG,"DODANO do DANYCH" );
        String query="SELECT * FROM " + TABLE_TASKS ;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        boolean isEdited=false;
        while(!c.isAfterLast()){
          if(c.getString(c.getColumnIndex("_id")).equals(Integer.toString(task.get_id()))){
             isEdited=true;
                break;
            }
            c.moveToNext();
        }
    if(isEdited)
    {
        db.update(TABLE_TASKS,contentValues,COLUMN_ID+"="+c.getString(c.getColumnIndex("_id")),null);
    }else
    db.insert(TABLE_TASKS,null,contentValues);
        db.close();
    }

    public void deleteTask(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_TASKS + " WHERE " + COLUMN_ID + "=\""
                + Integer.toString(id) + "\";");

    }

    public List<Task> loadFromDB(){
        List<Task> tasks=new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query="SELECT * FROM " + TABLE_TASKS ;


        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()){

            if(c.getString(c.getColumnIndex("_id"))!=null) {
                final  Task task=new Task(c.getString(c.getColumnIndex("title")), c.getString(c.getColumnIndex("description"))
                        , c.getString(c.getColumnIndex("url_to_icon")), c.getString(c.getColumnIndex("date_to_end")), Integer.parseInt(c.getString(c.getColumnIndex("_id")))
                        ,c.getString(c.getColumnIndex("_createdTime")));
                tasks.add(task);


        ImageRequest postRequest = new ImageRequest(task.get_url_to_icon(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
               task.set_image(response);
            }
        }, 200,200, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){};
                queue.add(postRequest);
            }
            c.moveToNext();
        }
        db.close();
        return tasks;
    }
}
