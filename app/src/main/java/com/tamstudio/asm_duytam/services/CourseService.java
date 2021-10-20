package com.tamstudio.asm_duytam.services;

import static com.tamstudio.asm_duytam.utilities.MyStructure.KEY_RESULT;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tamstudio.asm_duytam.dao.CourseDAO;
import com.tamstudio.asm_duytam.model.Course;
import com.tamstudio.asm_duytam.utilities.MySharePreferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseService extends IntentService {

    public static final String ACTION_INSERT_COURSE ="INSERT_COURSE";
    public static final String ACTION_DELETE_COURSE = "DELETE_COURSE";
    public static final String ACTION_UPDATE_COURSE = "UPDATE_COURSE";
    public static final String ACTION_GET_ALL_COURSE = "GET_ALL_COURSE";
    public static final String ACTION_GET_MY_COURSE = "GET_MY_COURSE";


    public CourseService() {
        super("CourseService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            Intent intentBroadcast = new Intent();
            boolean result = false;
            switch (intent.getAction()){
                case ACTION_INSERT_COURSE:{
                    intentBroadcast = new Intent(ACTION_INSERT_COURSE);
                    Course course = intent.getParcelableExtra("course");
                    result = CourseDAO.getInstance(this).insert(course);
                    break;
                }
                case ACTION_UPDATE_COURSE:{
                    intentBroadcast = new Intent(ACTION_UPDATE_COURSE);
                    Course course = intent.getParcelableExtra("course");
                    result = CourseDAO.getInstance(this).update(course);
                    break;
                }
                case ACTION_DELETE_COURSE:{
                    int idCourse = intent.getIntExtra("idCourse",-1);
                    intentBroadcast = new Intent(ACTION_DELETE_COURSE);
                    result = CourseDAO.getInstance(this).delete(idCourse);
                    break;
                }
                case ACTION_GET_ALL_COURSE:{
                    intentBroadcast = new Intent(ACTION_GET_ALL_COURSE);
                    List<Course> list = CourseDAO.getInstance(this).getAllByUserIDUnregister(MySharePreferences.getInstance(this).getStringValue("email"));
                    intentBroadcast.putParcelableArrayListExtra("listCourse", (ArrayList<? extends Parcelable>) list);
                    break;
                }
                case ACTION_GET_MY_COURSE:{
                    intentBroadcast = new Intent(ACTION_GET_MY_COURSE);
                    List<Course> list = CourseDAO.getInstance(this).getAllByUserID(MySharePreferences.getInstance(this).getStringValue("email"));
                    intentBroadcast.putParcelableArrayListExtra("listMyCourse", (ArrayList<? extends Parcelable>) list);
                    break;
                }
                default:break;
            }
            intentBroadcast.putExtra(KEY_RESULT,result);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
