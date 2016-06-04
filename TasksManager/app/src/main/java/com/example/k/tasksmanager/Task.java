package com.example.k.tasksmanager;

import android.graphics.Bitmap;
import android.media.Image;
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
    private String _created;
    private boolean selected;

    public Bitmap get_image() {
        return _image;
    }

    public void set_image(Bitmap _image) {
        this._image = _image;
    }

    private boolean is_done;
    private Bitmap _image;

    public Task(String title, String description, String url_to_icon, String _date_to_end, int _id, String _created) {
        this._title = title;
        this._description = description;
        this._url_to_icon = url_to_icon;
        this._date_to_end = _date_to_end;
        this._id=_id;
        this._created = _created;

    }


    public boolean is_done() {return is_done;}

    public void setIs_done(boolean is_done) {this.is_done = is_done;}


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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String get_date_to_end() {
        return _date_to_end;
    }

    public void set_date_to_end(String _date_to_end) {
        this._date_to_end = _date_to_end;
    }

    public String get_created() {
        return _created;
    }

    public void set_created(String _created) {
        this._created = _created;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_title);
        dest.writeString(_description);
        dest.writeString(_url_to_icon);
        dest.writeString(_date_to_end);
        dest.writeInt(_id);
        dest.writeString(_created);
    }


    // this is used to regenerate object.
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    // constructor that takes a Parcel and gives you an object populated with it's values
    private Task(Parcel in) {
        _title=in.readString();
        _description=in.readString();
        _url_to_icon=in.readString();
        _date_to_end=in.readString();
         _id=in.readInt();
        _created =in.readString();

    }
}

