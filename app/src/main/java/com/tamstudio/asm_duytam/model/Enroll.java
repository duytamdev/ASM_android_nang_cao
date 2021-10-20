package com.tamstudio.asm_duytam.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Enroll implements Parcelable {
    private int id;
    private String userEmailID;
    private int courseID;


    public Enroll(String userEmailID, int courseID) {
        this.userEmailID = userEmailID;
        this.courseID = courseID;
    }

    protected Enroll(Parcel in) {
        id = in.readInt();
        userEmailID = in.readString();
        courseID = in.readInt();
    }

    public static final Creator<Enroll> CREATOR = new Creator<Enroll>() {
        @Override
        public Enroll createFromParcel(Parcel in) {
            return new Enroll(in);
        }

        @Override
        public Enroll[] newArray(int size) {
            return new Enroll[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmailID() {
        return userEmailID;
    }

    public void setUserEmailID(String userEmailID) {
        this.userEmailID = userEmailID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(userEmailID);
        parcel.writeInt(courseID);
    }
}
