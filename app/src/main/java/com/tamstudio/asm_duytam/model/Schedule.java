package com.tamstudio.asm_duytam.model;

import java.util.Date;

public class Schedule {
    private int id;
    private Date date;
    private Date time;
    private String  address;
    private String meet;
    private boolean isTestSchedule;

    public Schedule(int id, Date date, Date time, String address, String meet, boolean isTestSchedule) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.address = address;
        this.meet = meet;
        this.isTestSchedule = isTestSchedule;
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
