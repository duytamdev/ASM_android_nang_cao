package com.tamstudio.asm_duytam.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Schedule  implements Serializable{
    private int id;
    private Date date;
    private Date time;
    private String  address;
    private String meet;
    private boolean isTestSchedule;
    private int courseId;

    public Schedule(int id, Date date, Date time, String address, String meet, boolean isTestSchedule,int courseID) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.address = address;
        this.meet = meet;
        this.isTestSchedule = isTestSchedule;
        this.courseId = courseID;
    }

    public Schedule(Date date, Date time, String address, String meet, boolean isTestSchedule, int courseId) {
        this.date = date;
        this.time = time;
        this.address = address;
        this.meet = meet;
        this.isTestSchedule = isTestSchedule;
        this.courseId = courseId;
    }



    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeet() {
        return meet;
    }

    public void setMeet(String meet) {
        this.meet = meet;
    }

    public boolean isTestSchedule() {
        return isTestSchedule;
    }

    public void setTestSchedule(boolean testSchedule) {
        isTestSchedule = testSchedule;
    }

}
