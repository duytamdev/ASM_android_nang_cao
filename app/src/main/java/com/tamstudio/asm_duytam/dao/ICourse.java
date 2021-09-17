package com.tamstudio.asm_duytam.dao;

import com.tamstudio.asm_duytam.model.Course;

import java.util.List;

public interface ICourse {

    List<Course> getAllCourse();
    List<Course> getAllCourseByStudent(int idStudent);// select * from course inner join enroll on courseId = enroll.courseId, where enroll.idStudent = id
    Course getCourse(int idCourse);
    boolean insert(Course course);
    boolean update(Course course);
    boolean delete(int idCourse);

}
