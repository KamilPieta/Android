package com.example.k.tasksmanager;

import android.content.Context;

import android.graphics.Color;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by k on 25.05.2016.
 */
public class TaskAdapter extends RecyclerView.Adapter<MainViewHolder> {


    List<Task> tasks;
    protected Context context;
    Log log;
    private static final String TAG="kamil";


    public TaskAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);

        MainViewHolder tvh = new MainViewHolder(v);
        return tvh;
    }
    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {


        holder.task_title.setText(tasks.get(position).get_title());
        holder.task_date_to_end.setText(tasks.get(position).get_date_to_end());
        holder.task_url_to_icon.setImageBitmap(tasks.get(position).get_image());



        if (tasks.get(position).isSelected() && !tasks.get(position).is_done()) {
            holder.task_layout.setBackgroundColor(Color.parseColor("#d5d5d5"));
        } else if(!tasks.get(position).isSelected() && !tasks.get(position).is_done()){
            holder.task_layout.setBackgroundColor(Color.TRANSPARENT);
        }
        else if(tasks.get(position).isSelected() && tasks.get(position).is_done()){
            holder.task_layout.setBackgroundColor(Color.parseColor("#B5F5B3"));
        }
        else if(!tasks.get(position).isSelected() && tasks.get(position).is_done()){
            holder.task_layout.setBackgroundColor(Color.parseColor("#6cec67"));

        }

    }

    public void setSelected(int pos) {
        try {
            tasks.get(pos).setSelected(!tasks.get(pos).isSelected());
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(int position, Task task) {
        tasks.add(task);
        notifyItemInserted(position);
    }

    public void deleteSelectedItems() {
        for(int i=getItemCount()-1;i>-1;--i){
            { if(tasks.get(i).isSelected()){
                tasks.remove(i);
            notifyItemRemoved(i);}}
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}