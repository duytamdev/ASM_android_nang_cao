package com.tamstudio.asm_duytam.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import static com.tamstudio.asm_duytam.database.DbHelper.*;
import com.tamstudio.asm_duytam.database.DbHelper;
import com.tamstudio.asm_duytam.model.Enroll;
import com.tamstudio.asm_duytam.utilities.Utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EnrollDAO {

    DbHelper db;
    SQLiteDatabase database;
    private static EnrollDAO instance;
    public static EnrollDAO getInstance(Context context){
        if(instance==null){
            instance = new EnrollDAO(context);
        }
        return instance;
    }

    private EnrollDAO(Context context) {
        db = DbHelper.getInstance(context);
    }

    public boolean register(Enroll entity) {
        database = db.getWritableDatabase();
        database.beginTransaction();
        long result ;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ENROLL_EMAIL_USER,entity.getUserEmailID());
            contentValues.put(ENROLL_ID_COURSE,entity.getCourseID());
            result = database.insert(ENROLL_TABLE_NAME,null,contentValues);
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

    public boolean unRegister(String emailUser,Integer idCourse) {
        SQLiteDatabase database = db.getWritableDatabase();
        int result = 0;
        try {
            result = database.delete(ENROLL_TABLE_NAME,"ENROLL.USER_ID_ENROLL = ? AND ENROLL.COURSE_ID_ENROLL = ? ",new String[]{emailUser,String.valueOf(idCourse)});
        }catch (Exception e){
            e.printStackTrace();
            database.close();
        }
        return result>0;
    }
}