package com.example.k.tasksmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    private final String TAG="kamil";
    private List<Task> tasks;
    private ImageButton comfimOrEditButton;
    private ImageButton deleteButton;
    private int selectedTasks=0;
    DBHandler dbHandler;
    TaskAdapter adapter;
    Log log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler=new DBHandler(this,null,null,1);

        tasks= dbHandler.loadFromDB();
        adapter = new TaskAdapter(tasks,this);
        comfimOrEditButton=(ImageButton)findViewById(R.id.fab);
        deleteButton=(ImageButton)findViewById(R.id.fab1);
        deleteButton.setVisibility(View.INVISIBLE);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createList();
        getNewTask();
    }

    private void createList(){
         RecyclerView rv = (RecyclerView)findViewById(R.id.rv_list);

        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        rv.setLayoutManager(llm);



       /* tasks.add(new Task("Task 1", "Desc1", "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRTwPN0Be0cQxbdrTimqv0PToxQpRHI_6RO-K0blM50Z0vn6ODNDw","data"));
        tasks.add(new Task("Task 2", "Desc2", "https://j7w7h8q2.ssl.hwcdn.net/achievements/ach_ipad/9.10.png","data"));
        tasks.add(new Task("Task 3", "Desc3", "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRPavvtpEYQ4J6373MBqmtmpm-ReqVpuuAgchucxmi9PQGUIFa4","data"));
        tasks.add(new Task("Task 4", "Desc4", "http://andycroll.com/images/2013/pivotal-tracker-fluid-icon-2013.png","data"));
        tasks.add(new Task("Task 5", "Desc5", "","data"));
        tasks.add(new Task("Task 6", "Desc6", "http://www.todoapk.net/wp-content/uploads/2015/01/iride-ui-icon-pack-300x300.png","data"));
        tasks.add(new Task("Task 7", "Desc7", "http://img10.deviantart.net/ed1e/i/2009/028/9/9/space_1_by_sadmonkeydesign_res.jpg","data"));
        tasks.add(new Task("Task 8", "Desc8", "http://icons.iconarchive.com/icons/graphicloads/colorful-long-shadow/256/Home-icon.png","data"));
*/

        rv.setAdapter(adapter);
        comfimOrEditButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
               Intent intent = new Intent(v.getContext(),TaskView.class);
                intent.putExtra("newTask","NewTask");
                startActivity(intent);
            }
        });
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
                        {   dbHandler.deleteTask(tasks.get(i).get_token());
                            log.i(TAG,"TITLE TASK WICH MUST BE DELETED IS EQUAL"+tasks.get(i).get_token());}}
                        adapter.deleteSelectedItems();
                    }
                }).setNegativeButton("No",null).show();
            }
        });
    rv.addOnItemTouchListener(new RecyclerItemClickListener(this,rv, new RecyclerItemClickListener.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {
      String taskTittle=tasks.get(position).get_title();
        Toast.makeText(view.getContext(),taskTittle,Toast.LENGTH_LONG).show();
        if(selectedTasks==0){
        Intent intent = new Intent(view.getContext(),TaskView.class);
        intent.putExtra("Task",tasks.get(position));
        startActivity(intent);}
        else{
            if(tasks.get(position).isSelected()==true){
            adapter.setSelected(position);
            selectedTasks--;
                if(selectedTasks==0)deleteButton.setVisibility(View.INVISIBLE);
            }
            else{
                adapter.setSelected(position);
                selectedTasks++;
                deleteButton.setVisibility(View.VISIBLE);
            }
        }

    }
        @Override
        public void onItemLongClick(View view, int position) {
            try {
               adapter.setSelected(position);
                selectedTasks++;
                deleteButton.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }));
    }

    public void getNewTask()
    {
        Intent i = getIntent();
            if(i.hasExtra("addTask")){
            Task task;

            task= i.getParcelableExtra("addTask");
                Random randToken = new Random();
                boolean isAvailble=false;
                int randT=0;
                while(isAvailble==false){
                    randT=randToken.nextInt()%999999;
                    isAvailble=true;
                    for(Task t: tasks)
                        if(t.get_token()==Integer.toString(randT))
                            isAvailble=false;
                }
                task.set_token(Integer.toString(randT));
                dbHandler.addTask(task);
                adapter.add(tasks.size(),task);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
   return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings)
            return true;


        return super.onOptionsItemSelected(item);
    }



}
