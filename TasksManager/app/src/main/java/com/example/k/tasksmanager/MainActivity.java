package com.example.k.tasksmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG="kamil";
    private List<Task> tasks;
    private ImageButton confimOrEditButton;
    private ImageButton deleteButton;
    private int  selectedTasks=0;
    DBHandler dbHandler;
    TaskAdapter adapter;
    private enum compareChoose{TITLE, END_DATE, CREATED_DATE}
    private  SharedPreferences saveSortPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler=new DBHandler(this,null,null,1);
        tasks= dbHandler.loadFromDB();
    }

    private void createViews(){
        setContentView(R.layout.activity_main);
        confimOrEditButton =(ImageButton)findViewById(R.id.fab);
        deleteButton=(ImageButton)findViewById(R.id.fab1);
        deleteButton.setVisibility(View.INVISIBLE);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    @Override
    protected void onStart() {
        createViews();
        createList();
        selectedTasks=0;
        super.onStart();
    }

    private void createList(){
         RecyclerView rv = (RecyclerView)findViewById(R.id.rv_list);
        saveSortPreferences=getSharedPreferences("saveSort",Context.MODE_PRIVATE);

        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        rv.setLayoutManager(llm);



//loadFromJSON();

        adapter = new TaskAdapter(tasks,this);
        rv.setAdapter(adapter);
        getNewTask();
        checkTaskStatus();
        confimOrEditButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent in = new Intent(v.getContext(),TaskView.class);
                if(selectedTasks==1)
                {
                    for(int i=0;i<tasks.size();++i){
                        if (tasks.get(i).isSelected())
                        {in.putExtra("editTask",tasks.get(i));

                            adapter.setSelected(i);selectedTasks=0;
                            break;}
                        }
                }
                    else
                    in.putExtra("newTask","NewTask");
                startActivity(in);}
        });
        //setting delete fab listener
        deleteButton.setOnClickListener(new View.OnClickListener(
        ) {
             @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext()).setMessage("Are you sure?").setCancelable(false).setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteButton.setVisibility(View.INVISIBLE);
                        selectedTasks=0;
                        for(int i=0;i<tasks.size();++i)
                        { if(tasks.get(i).isSelected())
                           dbHandler.deleteTask(tasks.get(i).get_id());
                            }
                        adapter.deleteSelectedItems();
                        confimOrEditButton.setImageResource(R.drawable.ic_add_white_48dp);
                        Toast.makeText(getApplicationContext(),"Task Removed",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No",null).show();
            }
        });
        //setting on a list choose listener.
    rv.addOnItemTouchListener(new RecyclerItemClickListener(this,rv, new RecyclerItemClickListener.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {

        if(selectedTasks==0){
        Intent intent = new Intent(view.getContext(),TaskView.class);
        intent.putExtra("ViewTask",tasks.get(position));
        startActivity(intent);
        }
        else{
            if(tasks.get(position).isSelected()){
            adapter.setSelected(position);
              selectedTasks--;
                       if(selectedTasks==1)
                    confimOrEditButton.setImageResource(R.drawable.ic_build_white_48dp);
                else
                    confimOrEditButton.setImageResource(R.drawable.ic_add_white_48dp);
                 if(selectedTasks==0)
                     deleteButton.setVisibility(View.INVISIBLE);
            }
            else{
                adapter.setSelected(position);
                selectedTasks++;
                  confimOrEditButton.setImageResource(R.drawable.ic_add_white_48dp);
                deleteButton.setVisibility(View.VISIBLE);
            }
        }
    }
        @Override
        public void onItemLongClick(View view, int position) {
            try {
               adapter.setSelected(position);
                selectedTasks++;
                  if(selectedTasks==1)
                    confimOrEditButton.setImageResource(R.drawable.ic_build_white_48dp);
                else
                confimOrEditButton.setImageResource(R.drawable.ic_add_white_48dp);
                deleteButton.setVisibility(View.VISIBLE);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }));

        rv.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                animFadeOut.setDuration(200);
                animFadeOut.setStartTime(50);
                if (dy > 0) {
                   if (deleteButton.isShown())
                   deleteButton.setAnimation(animFadeOut);
                    confimOrEditButton.setAnimation(animFadeOut);
                    confimOrEditButton.setVisibility(recyclerView.GONE);
                }

                else if (dy < 0)
                {
                    if (deleteButton.isShown())
                deleteButton.setAnimation(animFadeOut);
                    confimOrEditButton.setAnimation(animFadeOut);
                    confimOrEditButton.setVisibility(recyclerView.GONE);}
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                animFadeIn.setDuration(200);
                if (deleteButton.isShown())
                {deleteButton.setAnimation(animFadeIn);
                    deleteButton.setVisibility(recyclerView.VISIBLE);}
                confimOrEditButton.setAnimation(animFadeIn);
                confimOrEditButton.setVisibility(recyclerView.VISIBLE);
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
    public void getNewTask()
    {    Task task;
        Intent i = getIntent();

            if(i.hasExtra("addTask") ){
            task= i.getParcelableExtra("addTask");
                i.removeExtra("addTask");
                dbHandler.addTask(task);
                tasks= dbHandler.loadFromDB();
            }
        else if(i.hasExtra("editTask")){
                task=i.getParcelableExtra("editTask");
                i.removeExtra("editTask");
                dbHandler.addTask(task);
                tasks= dbHandler.loadFromDB();
            }
        else if (i.hasExtra("Return")){
               i.removeExtra("Return");
            }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
       int id =  saveSortPreferences.getInt("saveSort",-1);

        switch (id){
            case R.id.sortByTitle:
                if(menu.getItem(0).isChecked())
                    menu.getItem(0).setChecked(false);
                else
                    menu.getItem(0).setChecked(true);
                compareDate(compareChoose.TITLE);
                adapter.notifyDataSetChanged();
               return true;
            case R.id.sortByEndTime:
                if(menu.getItem(1).isChecked())
                    menu.getItem(1).setChecked(false);
                else
                    menu.getItem(1).setChecked(true);
                compareDate(compareChoose.END_DATE);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.sortByCreated:
                if(menu.getItem(2).isChecked())
                    menu.getItem(2).setChecked(false);
                else
                    menu.getItem(2).setChecked(true);
                compareDate(compareChoose.CREATED_DATE);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return true;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
SharedPreferences.Editor editor = saveSortPreferences.edit();
       switch (item.getItemId()){
            case R.id.sortByTitle:
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                compareDate(compareChoose.TITLE);
                adapter.notifyDataSetChanged();
                editor.putInt("saveSort",R.id.sortByTitle);
                editor.apply();
                return true;
            case R.id.sortByEndTime:
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                compareDate(compareChoose.END_DATE);
                editor.putInt("saveSort",R.id.sortByEndTime);
                editor.apply();
                adapter.notifyDataSetChanged();
               return true;

            case R.id.sortByCreated:
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                compareDate(compareChoose.CREATED_DATE);
                editor.putInt("saveSort",R.id.sortByCreated);
                editor.apply();
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkTaskStatus(){
        SimpleDateFormat format =new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = Calendar.getInstance().getTime();
        if(tasks.size()>0){
        for(Task task: tasks)
        {
            if(!task.is_done()){
        try{
            if((currentDate).after(format.parse(task.get_date_to_end())))
            { task.setIs_done(true);
        }
        else
        task.setIs_done(false);
                      }
        catch (ParseException e){e.printStackTrace();}}}}
        adapter.notifyDataSetChanged();}


    public void compareDate(final compareChoose c){

        if(tasks.size()>1){
            Collections.sort(tasks, new Comparator<Task>() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            @Override
            public int compare(Task t1, Task t2) {

                switch (c){
                    case TITLE:
                      return t1.get_title().toLowerCase().compareTo(t2.get_title().toLowerCase());
                    case END_DATE:
                        try {
                            return (sdf.parse(t1.get_date_to_end()).compareTo(sdf.parse(t2.get_date_to_end())));

                        } catch (ParseException e) {
                            if (t1.get_date_to_end().equals(""))
                                return 1;
                            else if (t2.get_date_to_end().equals(""))
                                return -1;
                            else
                                return 0;
                        }
                    case CREATED_DATE:
                        try {
                            return (sdf.parse(t1.get_created()).compareTo(sdf.parse(t2.get_created())));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                }
              return 0;
               }
        });
    if(c==c.CREATED_DATE)
        Collections.reverse(tasks);
        }}

    private void loadFromJSON(){
        //reading JSON from a res file
        String jsonValues="";
        String data;
        Resources res=getResources();
        InputStream is =res.openRawResource(R.raw.tasks);
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((data= br.readLine()) != null)
            {
                jsonValues+=data;
            }
        }
        catch(IOException e)
        {e.printStackTrace();}
        //adding jsons to a list.

        try{
            JSONObject jsonRootObject = new JSONObject(jsonValues);
            JSONArray jsonArray= jsonRootObject.optJSONArray("task");

            for(int i=0;i<jsonArray.length();++i){
                JSONObject jsonObject=jsonArray.getJSONObject(i);

                dbHandler.addTask(new Task(jsonObject.optString("_title"), jsonObject.optString("_description"),
                        jsonObject.optString("_url_to_icon"),  jsonObject.optString("_date_to_end"),Integer.parseInt(jsonObject.optString("_id")), jsonObject.optString("_createdTime")));
            }

        }catch(JSONException e){e.printStackTrace();}
    }

    private void writeToJSON(){

        String data="";
        JSONArray jsonArray = new JSONArray();
        try {

            for (int i = 0; i < tasks.size(); ++i) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("_title", tasks.get(i).get_title());
                jsonObject.put("_description",tasks.get(i).get_description());
                jsonObject.put("_url_to_icon",tasks.get(i).get_url_to_icon());
                jsonObject.put("_date_to_end",tasks.get(i).get_date_to_end());
                jsonObject.put("_id",tasks.get(i).get_id());
                jsonObject.put("_createdTime",tasks.get(i).get_created());
                jsonArray.put(jsonObject);
            }
            JSONObject taskJSON = new JSONObject();
            taskJSON.put("task", jsonArray);
            data = taskJSON.toString();
        }catch(JSONException e){e.printStackTrace();}
            //Writing to the InternalStorage
        try{
            FileOutputStream fileOutputStream = openFileOutput("tasks.json",MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
           }catch(FileNotFoundException e){e.printStackTrace();} catch (IOException e) {
            e.printStackTrace();
        }
    }

}

