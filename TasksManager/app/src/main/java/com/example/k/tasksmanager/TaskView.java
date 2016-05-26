package com.example.k.tasksmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import java.util.logging.Logger;

public class TaskView extends AppCompatActivity {
    private Task task;
    Logger log;
    boolean newTask=false;
    EditText description;
    EditText title;
    EditText url;
    EditText date;
    protected ImageButton comfim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        final String TAG="kamil";
        title=(EditText)findViewById(R.id.title);
        description=(EditText)findViewById(R.id.description);
        url=(EditText)findViewById(R.id.url);
        date=(EditText)findViewById(R.id.date);
        comfim=(ImageButton)findViewById(R.id.fab);
        getTask();


        comfim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleField = title.getText().toString();
                if(titleField.isEmpty()){
                    title.setError("That field cannot be empty!");
                }
                else if(titleField.length()<3){
                   title.setError("The length of the title must contains at last 3 characters");
                }
                if(newTask){
                    task = new Task(title.getText().toString(),description.getText().toString(),url.getText().toString(),date.getText().toString(),"");
                    Intent i = new Intent(v.getContext(),MainActivity.class);
                    i.putExtra("addTask",task);
                    startActivity(i);
                }
            }
        });
        }

    public void getTask(){
        Intent i = getIntent();
        if(i.hasExtra("Task")){
            task = i.getParcelableExtra("Task");
            title.setText(task.get_title());
            description.setText(task.get_description());
            url.setText(task.get_url_to_icon());
            date.setText(task.get_date_to_end());
            setEditable(false);
        }
        else if(i.hasExtra("newTask")) {
            newTask = true;
            setEditable(true);
        }

    }
public void setEditable(boolean editable){
EditText[] textFields= {title,description,url,date};

    if(editable){
        for(EditText textField : textFields){
            textField.setFocusable(true);
            textField.setFocusableInTouchMode(true);
            textField.setClickable(true);
        }
    }
    else{
        for(EditText textField : textFields){
            textField.setFocusable(false);
            textField.setFocusableInTouchMode(false);
            textField.setClickable(false);
        }
    }


}

    }

