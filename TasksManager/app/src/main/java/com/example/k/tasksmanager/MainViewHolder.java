package com.example.k.tasksmanager;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by k on 25.05.2016.
 */
public class MainViewHolder extends RecyclerView.ViewHolder{

    CardView taskList;
    TextView task_title;
    TextView task_date_to_end;
    ImageView task_url_to_icon;
    RelativeLayout task_layout;


    public MainViewHolder(View itemView) {
        super(itemView);
        taskList=(CardView)itemView.findViewById(R.id.taskList);
        task_title=(TextView)itemView.findViewById(R.id.task_title);
        task_date_to_end=(TextView)itemView.findViewById(R.id.task_date_to_end);
        task_layout=(RelativeLayout)itemView.findViewById(R.id.task_layout);
        task_url_to_icon=(ImageView)itemView.findViewById(R.id.task_url_to_icon);
    }
}
