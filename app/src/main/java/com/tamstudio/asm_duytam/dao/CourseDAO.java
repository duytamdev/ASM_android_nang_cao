package com.tamstudio.asm_duytam.dao;

import com.tamstudio.asm_duytam.model.Course;

import java.util.List;

public class CourseDAO implements ICourse{


    @Override
    public List<Course> getAllCourse() {
        return null;
    }

    @Override
    public List<Course> getAllCourseByStudent(int idStudent) {
        return null;
    }

    @Override
    public Course getCourse(int idCourse) {
        return null;
    }

    @Override
    public boolean insert(Course course) {
        return false;
    }

    @Override
    public boolean update(Course course) {
        return false;
    }

    @Override
    public boolean delete(int idCourse) {
        return false;
    }
}
