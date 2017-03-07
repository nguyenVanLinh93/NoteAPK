package com.note.model;

import java.io.Serializable;

/**
 * Created by nguyenlinh on 21/02/2017.
 */
public class Notes implements Serializable {

    private int Id;
    private String Title;
    private String Content;
    private String CreatedDate;
    private String Alarm;
    private String Background;
    // TODO create varilable phoito bitmap
    // TODO private String images // String image : path+path+path+..... after crack = String.split -> array images[]

    public Notes(int id, String title, String content, String createdDate, String alarm, String background) {
        Id = id;
        Title = title;
        Content = content;
        CreatedDate = createdDate;
        Alarm = alarm;
        Background = background;
    }
    public Notes() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getAlarm() {
        return Alarm;
    }

    public void setAlarm(String alarm) {
        Alarm = alarm;
    }

    public String getBackground() {
        return Background;
    }

    public void setBackground(String background) {
        Background = background;
    }

    @Override
    public String toString() {
        return this.Title;
    }
}
