package com.example.k.tasksmanager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import java.util.Date;

public class TaskView extends AppCompatActivity implements SetData.SetDataListener {
    private Task task;
    Log log;
    boolean newTask=false;
    boolean editTask=false;
    private boolean dateEditable=false;
    EditText description;
    EditText title;
    EditText url;
    EditText datePicker;
    private static final String TAG="kamil";
    protected ImageButton confim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        setUpViews();
        }

    @Override
    protected void onStart() {
        super.onStart();
        getTask();
        datePicker();
    }

    public void setUpViews(){
        title=(EditText)findViewById(R.id.title);
        description=(EditText)findViewById(R.id.description);
        url=(EditText)findViewById(R.id.url);
        datePicker=(EditText)findViewById(R.id.datePicker);
        confim =(ImageButton)findViewById(R.id.fab);


        confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleField = title.getText().toString();
                boolean isValidate;
                if(titleField.isEmpty()){
                    title.setError("That field cannot be empty!");
                    isValidate=false;
                }
                else if(titleField.length()<3){
                    title.setError("The length of the title must contains at last 3 characters");
                    isValidate=false;
                }
                else
                isValidate=true;

               if(isValidate){

                if(newTask ){
                    Intent i = new Intent(v.getContext(),MainActivity.class);
                    Date d= new Date();
                    task = new Task(title.getText().toString(),description.getText().toString(),url.getText().toString(),datePicker.getText().toString(),0,d.toString());
                    i.putExtra("addTask",task);
                    startActivity(i);
                }
               else if(editTask){
                    Intent i = new Intent(v.getContext(),MainActivity.class);
                    task.set_title(title.getText().toString());
                    task.set_description(title.getText().toString());
                    task.set_date_to_end(datePicker.getText().toString());
                    task.set_url_to_icon(url.getText().toString());
                    i.putExtra("editTask",task);
                    startActivity(i);
                }

               }

            }
        });
    }
    public void getTask(){


        Bundle b =getIntent().getExtras();
        if(b.containsKey("ViewTask")){
            task = b.getParcelable("ViewTask");
            title.setText(task.get_title());
            description.setText(task.get_description());
            url.setText(task.get_url_to_icon());
           confim.setVisibility(View.GONE);
            setEditable(false);
        }
        else if(b.containsKey("newTask")) {
            newTask = true;
            setEditable(true);
        }
        else if(b.containsKey("editTask")){
            task =b.getParcelable("editTask");
            title.setText(task.get_title());
            description.setText(task.get_description());
            url.setText(task.get_url_to_icon());
            setEditable(true);
            editTask=true;
        }
    }


public void setEditable(boolean editable){
EditText[] textFields= {title,description,url};

    if(editable){
        dateEditable=true;
        for(EditText textField : textFields){
            textField.setFocusable(true);
            textField.setFocusableInTouchMode(true);
            textField.setClickable(true);
        }
    }
    else{
        dateEditable=false;
        for(EditText textField : textFields){
            textField.setFocusable(false);
            textField.setFocusableInTouchMode(false);
            textField.setClickable(false);
        }
    }
}

     public void datePicker(){
         if(dateEditable){
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetData data = new SetData();
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                data.show(ft,"dataPicker");
            }
        });
         }
    }

    @Override
    public void setData(String data) {
        log.i(TAG,data);
        datePicker.setText(data);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("Return","return");
        log.i(TAG,"return");
        startActivity(myIntent);
           }
}

