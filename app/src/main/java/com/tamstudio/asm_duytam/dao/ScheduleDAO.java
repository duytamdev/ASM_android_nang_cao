package com.tamstudio.asm_duytam.dao;
import static com.tamstudio.asm_duytam.database.DbHelper.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tamstudio.asm_duytam.database.DbHelper;
import com.tamstudio.asm_duytam.model.Schedule;
import com.tamstudio.asm_duytam.utilities.Utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleDAO implements ICruidDAO<Schedule,Integer> {

    DbHelper db;
    SQLiteDatabase database;
    private static ScheduleDAO instance;
    public static ScheduleDAO getInstance(Context context){
        if(instance==null){
            instance = new ScheduleDAO(context);
        }
        return instance;
    }

    private ScheduleDAO(Context context) {
        db = DbHelper.getInstance(context);
    }
    @Override
    public List<Schedule> getAll() {
        return getData("SELECT "+
                SCHEDULE_ID+" , "
                +SCHEDULE_DATE+" , "
                +SCHEDULE_TIME+" , "
                +SCHEDULE_ADDRESS+" , "
                +SCHEDULE_MEET+" , "
                +SCHEDULE_TYPE+" , "
                +SCHEDULE_COURSE_ID
                +" FROM "+SCHEDULE_TABLE_NAME);
    }

    @Override
    public Schedule getById(Integer id) {
        List<Schedule> list = getData("SELECT "+
                SCHEDULE_ID+" , "
                +SCHEDULE_DATE+" , "
                +SCHEDULE_TIME+" , "
                +SCHEDULE_ADDRESS+" , "
                +SCHEDULE_MEET+" , "
                +SCHEDULE_TYPE+" , "
                +SCHEDULE_COURSE_ID
                +" FROM "+SCHEDULE_TABLE_NAME+" WHERE "+SCHEDULE_ID+" = ? ",String.valueOf(id));
        if(list==null) return null;
        return list.get(0);
    }
    public List<Schedule> getScheduleOfUser(String emailID){
        String sql ="SELECT "+
                SCHEDULE_ID+" , "
                +SCHEDULE_DATE+" , "
                +SCHEDULE_TIME+" , "
                +SCHEDULE_ADDRESS+" , "
                +SCHEDULE_MEET+" , "
                +SCHEDULE_TYPE+" , "
                +SCHEDULE_COURSE_ID
                +" FROM "+SCHEDULE_TABLE_NAME+" WHERE "+SCHEDULE_COURSE_ID+" IN "
                +"( SELECT COURSE.ID_COURSE FROM COURSE INNER JOIN ENROLL "
                + "ON COURSE.ID_COURSE = ENROLL.COURSE_ID_ENROLL "
                + "WHERE ENROLL.USER_ID_ENROLL = ? )";
        return getData(sql,emailID);
    }

    @Override
    public boolean insert(Schedule entity) {
        database = db.getWritableDatabase();
        database.beginTransaction();
        long result ;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SCHEDULE_DATE,Utilities.DateToString(entity.getDate(),Utilities.formatDate));
            contentValues.put(SCHEDULE_TIME,Utilities.DateToString(entity.getTime(),Utilities.formatTime));
            contentValues.put(SCHEDULE_ADDRESS,entity.getAddress());
            contentValues.put(SCHEDULE_MEET,entity.getMeet());
            boolean isTestSchedule = entity.isTestSchedule();
            int testSchedule;
            if(isTestSchedule){
                testSchedule = 1;
            }else testSchedule =0;
            contentValues.put(SCHEDULE_TYPE,testSchedule);
            contentValues.put(SCHEDULE_COURSE_ID,entity.getCourseId());
            result = database.insert(SCHEDULE_TABLE_NAME,null,contentValues);
            database.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            database.endTransaction();
            database.close();
        }
        return result>0;
    }

    @Override
    public boolean update(Schedule entity) {
        database = db.getWritableDatabase();
        database.beginTransaction();
        int result ;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SCHEDULE_DATE,Utilities.DateToString(entity.getDate(),Utilities.formatDate));
            contentValues.put(SCHEDULE_TIME,Utilities.DateToString(entity.getTime(),Utilities.formatTime));
            contentValues.put(SCHEDULE_ADDRESS,entity.getAddress());
            contentValues.put(SCHEDULE_MEET,entity.getMeet());
            boolean isTestSchedule = entity.isTestSchedule();
            int testSchedule;
            if(isTestSchedule){
                testSchedule = 1;
            }else testSchedule =0;
            contentValues.put(SCHEDULE_TYPE,testSchedule);
            contentValues.put(SCHEDULE_COURSE_ID,entity.getCourseId());
            result = database.update(SCHEDULE_TABLE_NAME,contentValues,SCHEDULE_ID+" = ? ",new String[]{String.valueOf(entity.getId())});
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
        int result;
        try {
            result = database.delete(SCHEDULE_TABLE_NAME,SCHEDULE_ID+" = ? ",new String[]{String.valueOf(id)});
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            database.close();
        }
        return result>0;
    }
    @SuppressLint("Range")
    private List<Schedule>getData(String sql, String ...augments){
        database = db.getReadableDatabase();
        List<Schedule> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql,augments);
        try {
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    int id = cursor.getInt(cursor.getColumnIndex(SCHEDULE_ID));
                    Date date = Utilities.StringToDate(cursor.getString(cursor.getColumnIndex(SCHEDULE_DATE)),Utilities.formatDate);
                    Date time = Utilities.StringToDate(cursor.getString(cursor.getColumnIndex(SCHEDULE_TIME)),Utilities.formatTime);
                    String address = cursor.getString(cursor.getColumnIndex(SCHEDULE_ADDRESS));
                    String meet = cursor.getString(cursor.getColumnIndex(SCHEDULE_MEET));
                    int testSchedule = cursor.getInt(cursor.getColumnIndex(SCHEDULE_TYPE));
                    boolean isTestSchedule = testSchedule==1;
                    int idCourse = cursor.getInt(cursor.getColumnIndex(SCHEDULE_COURSE_ID));
                    Schedule schedule = new Schedule(id,date,time,address,meet,isTestSchedule,idCourse);
                    list.add(schedule);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if(cursor!=null&& !cursor.isClosed()){
                cursor.close();
            }
            database.close();
        }

        return list;
    }
}