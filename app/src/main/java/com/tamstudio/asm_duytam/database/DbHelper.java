package com.tamstudio.asm_duytam.database;

import static com.tamstudio.asm_duytam.utilities.MyStructure.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {


    public static final String USER_TABLE_NAME = "USER";
    public static final String USER_EMAIL = "EMAIL_USER"; // PRIMARY KEY
    public static final String USER_PASSWORD = "PASSWORD_USER";
    public static final String USER_NAME = "NAME_USER";
    public static final String USER_ROLE = "ROLE_USER"; // role = 0: user, role = 1: admin

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
    public static final String ENROLL_EMAIL_USER = "USER_ID_ENROLL"; // foreign key


    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "+ USER_TABLE_NAME+" ( "
            +USER_EMAIL+" TEXT PRIMARY KEY, "
            +USER_PASSWORD+" TEXT , "
            +USER_NAME+" TEXT , "
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
            +ENROLL_EMAIL_USER+" TEXT , "
            +"FOREIGN KEY ( "+ENROLL_ID_COURSE+" ) REFERENCES "+ COURSE_TABLE_NAME+" ( "+COURSE_ID+" ) , "
            +"FOREIGN KEY ( "+ENROLL_EMAIL_USER+" ) REFERENCES "+ USER_TABLE_NAME+" ( "+USER_EMAIL+" ) )";

    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS "+USER_TABLE_NAME;
    private static final String DROP_TABLE_COURSE = "DROP TABLE IF EXISTS "+COURSE_TABLE_NAME;
    private static final String DROP_TABLE_SCHEDULE = "DROP TABLE IF EXISTS "+SCHEDULE_TABLE_NAME;
    private static final String DROP_TABLE_ENROLL = "DROP TABLE IF EXISTS "+ENROLL_TABLE_NAME;

    private void dataDefault(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("INSERT INTO "+USER_TABLE_NAME+" VALUES ('admin@app.com','admin','Admin',1 )");
        sqLiteDatabase.execSQL("INSERT INTO "+USER_TABLE_NAME+" VALUES ('user@app.com','123123','Duy Tam',0 )");
        sqLiteDatabase.execSQL("INSERT INTO COURSE(CODE_COURSE,NAME_COURSE,NAME_TEACHER_COURSE) VALUES ('MOB201','Android nâng cao','channn3')");
        sqLiteDatabase.execSQL("INSERT INTO COURSE(CODE_COURSE,NAME_COURSE,NAME_TEACHER_COURSE) VALUES ('MOB202','Dự án mẫu Android','khoand')");
        sqLiteDatabase.execSQL("INSERT INTO COURSE(CODE_COURSE,NAME_COURSE,NAME_TEACHER_COURSE) VALUES ('MOB101','Android cơ bản','khoand')");
        sqLiteDatabase.execSQL("INSERT INTO COURSE(CODE_COURSE,NAME_COURSE,NAME_TEACHER_COURSE) VALUES ('WEB201','Javascript cơ bản','channn3')");
        sqLiteDatabase.execSQL("INSERT INTO COURSE(CODE_COURSE,NAME_COURSE,NAME_TEACHER_COURSE) VALUES ('WEB202','HTML & CSS cơ bản','anna33')");
        sqLiteDatabase.execSQL("INSERT INTO COURSE(CODE_COURSE,NAME_COURSE,NAME_TEACHER_COURSE) VALUES ('MOB105','React Native cơ bản','huongtt')");
        sqLiteDatabase.execSQL("INSERT INTO COURSE(CODE_COURSE,NAME_COURSE,NAME_TEACHER_COURSE) VALUES ('MOB501','Android Networking','huongtt')");
        sqLiteDatabase.execSQL("INSERT INTO COURSE(CODE_COURSE,NAME_COURSE,NAME_TEACHER_COURSE) VALUES ('MOB401','Game 2D cơ bản','channn3')");
        sqLiteDatabase.execSQL("INSERT INTO COURSE(CODE_COURSE,NAME_COURSE,NAME_TEACHER_COURSE) VALUES ('MOB402','Game 2D nâng cao','channn3')");
        sqLiteDatabase.execSQL("INSERT INTO COURSE(CODE_COURSE,NAME_COURSE,NAME_TEACHER_COURSE) VALUES ('TES201','Kiểm thủ cơ bản','anna33')");




        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE(DATE_SCHEDULE, TIME_SCHEDULE,ADDRESS_SCHEDULE,MEET_SCHEDULE,TYPE_SCHEDULE,COURSE_ID_SCHEDULE)" +
                " VALUES ('20/10/2021','06:00:33 AM','501','grspim',0,1) ");
        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE(DATE_SCHEDULE, TIME_SCHEDULE,ADDRESS_SCHEDULE,MEET_SCHEDULE,TYPE_SCHEDULE,COURSE_ID_SCHEDULE)" +
                " VALUES ('18/11/2021','08:15:33 PM','502','pfytei',0,2) ");
        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE(DATE_SCHEDULE, TIME_SCHEDULE,ADDRESS_SCHEDULE,MEET_SCHEDULE,TYPE_SCHEDULE,COURSE_ID_SCHEDULE)" +
                " VALUES ('14/12/2021','07:30:33 AM','503','plpwxj',1,3) ");
        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE(DATE_SCHEDULE, TIME_SCHEDULE,ADDRESS_SCHEDULE,MEET_SCHEDULE,TYPE_SCHEDULE,COURSE_ID_SCHEDULE)" +
                " VALUES ('10/09/2021','01:15:33 PM','501','visscb',0,4) ");
        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE(DATE_SCHEDULE, TIME_SCHEDULE,ADDRESS_SCHEDULE,MEET_SCHEDULE,TYPE_SCHEDULE,COURSE_ID_SCHEDULE)" +
                " VALUES ('05/08/2021','07:30:33 AM','504','kewqyi',0,5) ");
        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE(DATE_SCHEDULE, TIME_SCHEDULE,ADDRESS_SCHEDULE,MEET_SCHEDULE,TYPE_SCHEDULE,COURSE_ID_SCHEDULE)" +
                " VALUES ('11/11/2021','07:30:33 AM','501','kiqanf',0,6) ");
        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE(DATE_SCHEDULE, TIME_SCHEDULE,ADDRESS_SCHEDULE,MEET_SCHEDULE,TYPE_SCHEDULE,COURSE_ID_SCHEDULE)" +
                " VALUES ('25/12/2021','06:15:33 AM','505','bumrni',1,7) ");
        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE(DATE_SCHEDULE, TIME_SCHEDULE,ADDRESS_SCHEDULE,MEET_SCHEDULE,TYPE_SCHEDULE,COURSE_ID_SCHEDULE)" +
                " VALUES ('18/11/2021','01:15:33 PM','507','orejds',0,8) ");
        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE(DATE_SCHEDULE, TIME_SCHEDULE,ADDRESS_SCHEDULE,MEET_SCHEDULE,TYPE_SCHEDULE,COURSE_ID_SCHEDULE)" +
                " VALUES ('28/11/2021','08:15:33 AM','508','cyfcuw',0,9) ");
        sqLiteDatabase.execSQL("INSERT INTO SCHEDULE(DATE_SCHEDULE, TIME_SCHEDULE,ADDRESS_SCHEDULE,MEET_SCHEDULE,TYPE_SCHEDULE,COURSE_ID_SCHEDULE)" +
                " VALUES ('27/11/2021','09:15:33 AM','502','xdwxir',0,10) ");
    }
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
        dataDefault(sqLiteDatabase);
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
