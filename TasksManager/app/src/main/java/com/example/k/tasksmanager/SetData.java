package com.example.k.tasksmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;


import java.util.Calendar;

/**
 * Created by k on 28.05.2016.
 */
public class SetData extends DialogFragment implements DatePickerDialog.OnDateSetListener{


Log log;
private static final String TAG="kamil";


    public SetData(){
      }

    SetDataListener activityCommander;
    public interface SetDataListener{

        public void setData(String data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            activityCommander =(SetDataListener) activity;
        }
        catch (ClassCastException e)
        {throw new ClassCastException(activity.toString());}
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        return new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
          activityCommander.setData(date);
    }
}
