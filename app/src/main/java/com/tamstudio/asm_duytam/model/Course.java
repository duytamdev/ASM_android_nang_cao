package com.tamstudio.asm_duytam.model;

public class Course {
    private int id;
    private String idCourse;
    private String  nameCourse;
    private String nameTeacher;

    public Course(int id, String idCourse, String nameCourse, String nameTeacher) {
        this.id = id;
        this.idCourse = idCourse;
        this.nameCourse = nameCourse;
        this.nameTeacher = nameTeacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
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
}
