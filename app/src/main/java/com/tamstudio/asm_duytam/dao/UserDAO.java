package com.tamstudio.asm_duytam.dao;

import com.tamstudio.asm_duytam.database.DbHelper;
import com.tamstudio.asm_duytam.model.User;

import static com.tamstudio.asm_duytam.database.DbHelper.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserDAO  {


    SQLiteDatabase database;
    DbHelper db;
    private static UserDAO instance;
    public static UserDAO getInstance(Context context){
        if(instance==null){
            instance = new UserDAO(context);
        }
        return instance;
    }

    private UserDAO(Context context) {
        db = DbHelper.getInstance(context);
    }

    public User getById(String emailId) {
        List<User> list = getData("SELECT "
                +USER_EMAIL+" , "
                +USER_PASSWORD+" , "
                +USER_NAME+" , "
                +USER_ROLE
                +" FROM "+USER_TABLE_NAME+" WHERE "+USER_EMAIL+" = ? ",emailId);
        assert list != null;
        return list.get(0);
    }

    public boolean register(User entity) {
        database = db.getWritableDatabase();
        database.beginTransaction();
        long result=0;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(USER_EMAIL,entity.getEmail());
            contentValues.put(USER_PASSWORD,entity.getPassword());
            contentValues.put(USER_NAME,entity.getName());
            int role ;
            if(entity.isAdmin()){
                role = 1;
            }else role = 0;

            contentValues.put(USER_ROLE,role);
            result =  database.insert(USER_TABLE_NAME,null,contentValues);
            database.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            database.endTransaction();
            database.close();
        }
        return result>0;
    }

    public boolean update(User entity) {
        database = db.getWritableDatabase();
        database.beginTransaction();
        int result=0;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(USER_PASSWORD,entity.getPassword());
            contentValues.put(USER_NAME,entity.getName());
            int role ;
            if(entity.isAdmin()){
                role = 1;
            }else role = 0;

            contentValues.put(USER_ROLE,role);
            result = database.update(USER_TABLE_NAME,contentValues,USER_EMAIL+" = ? ",new String[]{entity.getEmail()});
            database.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            database.endTransaction();
            database.close();
        }
        return result>0;
    }

    @SuppressLint("Range")
    public boolean checkLogin(String email,String pass){
        database = db.getReadableDatabase();
        database.beginTransaction();
        try {
            Cursor cursor = database.rawQuery("SELECT "+USER_EMAIL+" , "+USER_PASSWORD+" FROM "+USER_TABLE_NAME+" WHERE "+USER_EMAIL+" = ? " ,new String[]{email});
            if(cursor.getCount()<=0){
                return false;
            }
            cursor.moveToFirst();
           String password = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
           database.setTransactionSuccessful();
            return pass.equals(password);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            database.endTransaction();
            database.close();
        }
    }
    public boolean loginGoogle(User user){
        database = db.getReadableDatabase();
        boolean result;
        try {
            boolean isExist = checkRegister(user.getEmail());
            if (!isExist) {
                register(user);
            }
           result =  checkLogin(user.getEmail(),user.getPassword());
        }catch (Exception e){
            database.close();
            e.printStackTrace();
            return false;
        }
        return result;
    }
    public boolean checkRegister(String email){
        database = db.getReadableDatabase();
        try {
            Cursor cursor = database.rawQuery("SELECT "+USER_EMAIL+" FROM "+USER_TABLE_NAME+" WHERE "+USER_EMAIL+" = ? ",new String[]{email});
            return cursor.getCount()>0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            database.close();
        }
    }
    @SuppressLint("Range")
    private List<User>getData(String sql, String ...augments){
        database = db.getReadableDatabase();
        List<User> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql,augments);
        try {
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    String email = cursor.getString(cursor.getColumnIndex(USER_EMAIL));
                    String password = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
                    String name = cursor.getString(cursor.getColumnIndex(USER_NAME));
                    int role = cursor.getInt(cursor.getColumnIndex(USER_ROLE));
                    boolean isAdmin = role==1;
                    User user = new User(email,password,name,isAdmin);
                    list.add(user);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(cursor!=null&& !cursor.isClosed()){
                cursor.close();
            }
            database.close();
        }
        return list;
    }
}