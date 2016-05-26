package com.example.k.tasksmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by k on 25.05.2016.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    List<Task> tasks;
    protected Context context;

    public TaskAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                v.getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);

        TaskViewHolder tvh = new TaskViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        holder.task_title.setText(tasks.get(position).get_title());
        holder.task_date_to_end.setText(tasks.get(position).get_description());

           try {
            Picasso.with(context).load(tasks.get(position).get_url_to_icon()).resize(200, 200).into(holder.task_url_to_icon);
        } catch (Exception e) {
        }

        if (tasks.get(position).isSelected()) {
            holder.task_layout.setBackgroundColor(Color.parseColor("#d5d5d5"));
        } else {
            holder.task_layout.setBackgroundColor(Color.TRANSPARENT);
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
        for(int i=0;i<getItemCount();++i){
            if(tasks.get(i).isSelected()==true)
            {   tasks.remove(i);
                 notifyItemRemoved(i);i=0;
               }
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
