package com.example.k.tasksmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by k on 25.05.2016.
 */
public class RecyclerItemClickListener  implements RecyclerView.OnItemTouchListener{

    private OnItemClickListener Listener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    GestureDetector GestureDetector;




    public RecyclerItemClickListener(Context context,final RecyclerView recyclerView, OnItemClickListener listener){
        this.Listener =listener;
        this.GestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && Listener != null) {
                    Listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && this.Listener != null && this.GestureDetector.onTouchEvent(e)) {
            this.Listener.onItemClick(childView, view.getChildPosition(childView));
            return true;
        }

        return false;
    }
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
