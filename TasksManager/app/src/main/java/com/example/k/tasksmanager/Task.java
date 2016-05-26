package com.example.k.tasksmanager;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by k on 25.05.2016.
 */
public class Task implements Parcelable{

    private int _id;
    private String _title;
    private String _description;
    private String _url_to_icon;
    private String _date_to_end;
    private String _token;

    private boolean selected;

    public Task(String title, String description, String url_to_icon,String _date_to_end, String token) {
        this._title = title;
        this._description = description;
        this._url_to_icon = url_to_icon;
        this._date_to_end = _date_to_end;
        this._token=token;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_url_to_icon() {
        return _url_to_icon;
    }

    public void set_url_to_icon(String _url_to_icon) {
        this._url_to_icon = _url_to_icon;
    }

    public String get_date_to_end() {
        return _date_to_end;
    }

    public void set_date_to_end(String _date_to_end) {
        this._date_to_end = _date_to_end;
    }

    public String get_token() {
        return _token;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static Creator<Task> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_title);
        dest.writeString(_description);
        dest.writeString(_url_to_icon);
        dest.writeString(_date_to_end);

    }


    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Task(Parcel in) {
        _title=in.readString();
        _description=in.readString();
        _url_to_icon=in.readString();
        _date_to_end =in.readString();
    }
}

