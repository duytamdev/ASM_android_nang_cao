package com.tamstudio.asm_duytam.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import static com.tamstudio.asm_duytam.database.DbHelper.*;

import com.tamstudio.asm_duytam.database.DbHelper;
import com.tamstudio.asm_duytam.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDAO implements ICruidDAO<Course,Integer> {

    SQLiteDatabase database;
    DbHelper db;
    private static CourseDAO instance;
    public static CourseDAO getInstance(Context context){
        if(instance==null){
            instance = new CourseDAO(context);
        }
        return instance;
    }

    private CourseDAO(Context context) {
        db = DbHelper.getInstance(context);
    }

    @Override
    public List<Course> getAll() {
        return getData("SELECT "
                +COURSE_ID+", "
                +COURSE_CODE+", "
                +COURSE_NAME+", "
                +COURSE_NAME_TEACHER
                +" FROM "+COURSE_TABLE_NAME);
    }

    @Override
    public Course getById(Integer id) {
        List<Course> list = getData("SELECT "
                +COURSE_ID+", "
                +COURSE_CODE+", "
                +COURSE_NAME+", "
                +COURSE_NAME_TEACHER
                +" FROM "+COURSE_TABLE_NAME+" WHERE "+COURSE_ID+" = ? ",String.valueOf(id));
        assert list != null;
        return list.get(0);
    }
    // get danh sách coure của 1 người dùng
    public List<Course> getAllByUserID(String emailUser){
        String sql = "SELECT "
                +COURSE_ID+", "
                +COURSE_CODE+", "
                +COURSE_NAME+", "
                +COURSE_NAME_TEACHER
                +" FROM "+COURSE_TABLE_NAME+" INNER JOIN "+ENROLL_TABLE_NAME+" ON COURSE.ID_COURSE = ENROLL.COURSE_ID_ENROLL WHERE ENROLL.USER_ID_ENROLL = ? ";
        return getData(sql,emailUser);
    }
    public List<Course> getAllByUserIDUnregister(String emailUser){
        String sql =  "SELECT "
                +COURSE_ID+", "
                +COURSE_CODE+", "
                +COURSE_NAME+", "
                +COURSE_NAME_TEACHER
                +" FROM "+COURSE_TABLE_NAME+" WHERE "+COURSE_ID +" NOT IN ( "
                +"SELECT "+COURSE_ID+" FROM "+COURSE_TABLE_NAME+" INNER JOIN "+ENROLL_TABLE_NAME+" ON COURSE.ID_COURSE = ENROLL.COURSE_ID_ENROLL WHERE ENROLL.USER_ID_ENROLL = ? )";
        return getData(sql,emailUser);
    }

    @Override
    public boolean insert(Course entity) {
        database = db.getWritableDatabase();
        database.beginTransaction();
        long result;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COURSE_CODE,entity.getCodeCourse());
            contentValues.put(COURSE_NAME,entity.getNameCourse());
            contentValues.put(COURSE_NAME_TEACHER,entity.getNameTeacher());
            result =  database.insert(COURSE_TABLE_NAME,null,contentValues);
            database.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            database.endTransaction();
            database.close();
        }
        return result>0;
    }

    @Override
    public boolean update(Course entity) {
        database = db.getWritableDatabase();
        database.beginTransaction();
        long result;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COURSE_CODE,entity.getCodeCourse());
            contentValues.put(COURSE_NAME,entity.getNameCourse());
            contentValues.put(COURSE_NAME_TEACHER,entity.getNameTeacher());
            result =  database.update(COURSE_TABLE_NAME,contentValues,COURSE_ID+" = ? ",new String[]{String.valueOf(entity.getId())});
            database.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            database.endTransaction();
            database.close();
        }
        return result>0;
    }

    @Override
    public boolean delete(Integer id) {
        database = db.getWritableDatabase();
        int result =0;
        try {
             result = database.delete(COURSE_TABLE_NAME,COURSE_ID+" = ?",new String[]{id+""});
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            database.close();
        }
        return result>0;
    }
    @SuppressLint("Range")
    private List<Course>getData(String sql,String ...augments){
        database = db.getReadableDatabase();
        List<Course> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql,augments);
        try {
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    int id = cursor.getInt(cursor.getColumnIndex(COURSE_ID));
                    String codeCourse = cursor.getString(cursor.getColumnIndex(COURSE_CODE));
                    String nameCourse = cursor.getString(cursor.getColumnIndex(COURSE_NAME));
                    String nameTeacher = cursor.getString(cursor.getColumnIndex(COURSE_NAME_TEACHER));
                    Course course = new Course(id,codeCourse,nameCourse,nameTeacher);
                    list.add(course);
                    cursor.moveToNext();
                }
                cursor.close();
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(cursor!=null&& !cursor.isClosed())
                cursor.close();
            database.close();
        }
        return list;
    }
}