package com.tamstudio.asm_duytam.database;

import static com.tamstudio.asm_duytam.utilities.MyStructure.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static final String USER_TABLE_NAME = "USER";
    public static final String USER_ID = "ID_USER";
    public static final String USER_NAME = "NAME_USER";
    public static final String USER_EMAIL = "EMAIL_USER";
    public static final String USER_PHONE_NUMBER = "PHONE_NUMBER_USER";
    public static final String USER_DOB = "DOB_USER";
    public static final String USER_ROLE = "ROLE_USER"; // role = 0: user, role = 1: user

    public static final String COURSE_TABLE_NAME = "COURSE";
    public static final String COURSE_ID = "ID_COURSE";
    public static final String COURSE_CODE = "CODE_COURSE";
    public static final String COURSE_NAME = "NAME_COURSE";
    public static final String COURSE_NAME_TEACHER = "NAME_TEACHER_COURSE";

    public static final String SCHEDULE_TABLE_NAME = "SCHEDULE";
    public static final String SCHEDULE_ID = "ID_SCHEDULE";
    public static final String SCHEDULE_DATE = "DATE_SCHEDULE";
    public static final String SCHEDULE_TIME = "TIME_SCHEDULE";
    public static final String SCHEDULE_ADDRESS = "ADDRESS_SCHEDULE";
    public static final String SCHEDULE_MEET = "MEET_SCHEDULE";
    public static final String SCHEDULE_TYPE = "TYPE_SCHEDULE";// type = 0 : học, type =1: thi
    public static final String SCHEDULE_COURSE_ID = "COURSE_ID_SCHEDULE"; // foreign key

    public static final String ENROLL_TABLE_NAME = "ENROLL";
    public static final String ENROLL_ID = "ID_ENROLL";
    public static final String ENROLL_ID_COURSE= "COURSE_ID_ENROLL"; // foreign key
    public static final String ENROLL_ID_USER = "USER_ID_ENROLL"; // foreign key


    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "+ USER_TABLE_NAME+" ( "
            +USER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "
            +USER_NAME+" TEXT , "
            +USER_EMAIL+" TEXT , "
            +USER_PHONE_NUMBER+" TEXT , "
            +USER_DOB+" TEXT , "
            +USER_ROLE+" INTEGER )";

    private static final String CREATE_TABLE_COURSE = "CREATE TABLE IF NOT EXISTS "+ COURSE_TABLE_NAME+" ( "
            +COURSE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "
            +COURSE_CODE+" TEXT , "
            +COURSE_NAME+" TEXT , "
            +COURSE_NAME_TEACHER+" TEXT )";

    private static final String CREATE_TABLE_SCHEDULE = "CREATE TABLE IF NOT EXISTS "+ SCHEDULE_TABLE_NAME+" ( "
            +SCHEDULE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "
            +SCHEDULE_DATE+" TEXT , "
            +SCHEDULE_TIME+" TEXT , "
            +SCHEDULE_ADDRESS+" TEXT ,"
            +SCHEDULE_MEET+" TEXT , "
            +SCHEDULE_TYPE+" INTEGER , "
            +SCHEDULE_COURSE_ID+" INTEGER ,"
            +"FOREIGN KEY ( "+SCHEDULE_COURSE_ID+" ) REFERENCES "+ COURSE_TABLE_NAME+" ( "+COURSE_ID+" ) )";

    private static final String CREATE_TABLE_ENROLL = "CREATE TABLE IF NOT EXISTS "+ ENROLL_TABLE_NAME+" ( "
            +ENROLL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "
            +ENROLL_ID_COURSE+" INTEGER , "
            +ENROLL_ID_USER+" INTEGER , "
            +"FOREIGN KEY ( "+ENROLL_ID_COURSE+" ) REFERENCES "+ COURSE_TABLE_NAME+" ( "+COURSE_ID+" ) , "
            +"FOREIGN KEY ( "+ENROLL_ID_USER+" ) REFERENCES "+ USER_TABLE_NAME+" ( "+USER_ID+" ) )";

    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS "+USER_TABLE_NAME;
    private static final String DROP_TABLE_COURSE = "DROP TABLE IF EXISTS "+COURSE_TABLE_NAME;
    private static final String DROP_TABLE_SCHEDULE = "DROP TABLE IF EXISTS "+SCHEDULE_TABLE_NAME;
    private static final String DROP_TABLE_ENROLL = "DROP TABLE IF EXISTS "+ENROLL_TABLE_NAME;

//    private static final String INSERT_USES_AMIN = "INSERT INTO "+USER_TABLE_NAME+" ( "
//            +USER_NAME+" , "
//            +USER_EMAIL+" , "
//            +USER_PHONE_NUMBER+" , "
//            +USER_DOB+" , "
//            +USER_ROLE+" ) "+"VALUE ( 'NGUYEN DUY TAM ','ADMIN@GMAIL.COM,'"
    private static DbHelper instance;
    public static synchronized DbHelper getInstance(Context context){
        if(instance==null){
            instance = new DbHelper(context);
        }
        return  instance;
    }

    private DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_COURSE);
        sqLiteDatabase.execSQL(CREATE_TABLE_SCHEDULE);
        sqLiteDatabase.execSQL(CREATE_TABLE_ENROLL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_SCHEDULE);
        db.execSQL(DROP_TABLE_ENROLL);
        db.execSQL(DROP_TABLE_USER);
        db.execSQL(DROP_TABLE_COURSE);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
