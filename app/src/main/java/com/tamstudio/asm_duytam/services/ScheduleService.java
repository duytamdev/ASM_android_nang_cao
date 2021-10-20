package com.tamstudio.asm_duytam.services;

import static com.tamstudio.asm_duytam.utilities.MyStructure.KEY_RESULT;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tamstudio.asm_duytam.dao.ScheduleDAO;
import com.tamstudio.asm_duytam.dao.UserDAO;
import com.tamstudio.asm_duytam.model.Schedule;
import com.tamstudio.asm_duytam.utilities.MySharePreferences;
import com.tamstudio.asm_duytam.utilities.MyStructure;
import com.tamstudio.asm_duytam.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ScheduleService extends IntentService {

    public static final String ACTION_INSERT_SCHEDULE ="INSERT_SCHEDULE ";
    public static final String ACTION_DELETE_SCHEDULE = "DELETE_SCHEDULE ";
    public static final String ACTION_UPDATE_SCHEDULE  = "UPDATE_SCHEDULE ";
    public static final String ACTION_GET_SCHEDULES  = "GET_SCHEDULES ";

    public ScheduleService() {
        super("ScheduleService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){

            Intent intentBroadcast = new Intent();
            Bundle bundle = intent.getExtras();
            Schedule schedule  = (Schedule) bundle.getSerializable("schedule");
            boolean result = false;
            switch (intent.getAction()){
                case ACTION_INSERT_SCHEDULE:{
                    intentBroadcast = new Intent(ACTION_INSERT_SCHEDULE);
                    result = ScheduleDAO.getInstance(this).insert(schedule);
                    break;
                }
                case ACTION_UPDATE_SCHEDULE:{
                    intentBroadcast = new Intent(ACTION_UPDATE_SCHEDULE);
                    result = ScheduleDAO.getInstance(this).update(schedule);
                    break;
                }
                case ACTION_DELETE_SCHEDULE:{
                    int idSchedule = intent.getIntExtra("idSchedule",-1);
                    intentBroadcast = new Intent(ACTION_DELETE_SCHEDULE);
                    result = ScheduleDAO.getInstance(this).delete(idSchedule);
                    break;
                }
//                case ACTION_GET_SCHEDULES:{
//                    intentBroadcast = new Intent(ACTION_GET_SCHEDULES);
//                    String emailCurrent = MySharePreferences.getInstance(this).getStringValue(MyStructure.SHARE_PREFERENCE_EMAIL_ID);
//                    boolean isAdmin = UserDAO.getInstance(this).getById(emailCurrent).isAdmin();
//                    List<Schedule> list ;
//                    if(isAdmin){
//                        list = ScheduleDAO.getInstance(this).getAll();
//                    }else{
//                        list = ScheduleDAO.getInstance(this).getScheduleOfUser(emailCurrent);
//                    }
//                    intentBroadcast.putParcelableArrayListExtra("listSchedule", (ArrayList<? extends Parcelable>) list);
//                    break;
//                }
                default: break;
            }
            intentBroadcast.putExtra(KEY_RESULT, result);
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
