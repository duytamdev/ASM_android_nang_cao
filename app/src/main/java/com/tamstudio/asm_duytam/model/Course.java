package com.tamstudio.asm_duytam.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Course implements  Parcelable {
    private int id;
    private String codeCourse;
    private String  nameCourse;
    private String nameTeacher;

    public Course(int id, String codeCourse, String nameCourse, String nameTeacher) {
        this.id = id;
        this.codeCourse = codeCourse;
        this.nameCourse = nameCourse;
        this.nameTeacher = nameTeacher;
    }

    public Course(String codeCourse, String nameCourse, String nameTeacher) {
        this.codeCourse = codeCourse;
        this.nameCourse = nameCourse;
        this.nameTeacher = nameTeacher;
    }

    protected Course(Parcel in) {
        id = in.readInt();
        codeCourse = in.readString();
        nameCourse = in.readString();
        nameTeacher = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeCourse() {
        return codeCourse;
    }

    public void setCodeCourse(String codeCourse) {
        this.codeCourse = codeCourse;
    }

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

    public String getNameTeacher() {
        return nameTeacher;
    }

    public void setNameTeacher(String nameTeacher) {
        this.nameTeacher = nameTeacher;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(codeCourse);
        parcel.writeString(nameCourse);
        parcel.writeString(nameTeacher);
    }
}
