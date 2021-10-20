package com.tamstudio.asm_duytam.services;

import static com.tamstudio.asm_duytam.utilities.MyStructure.KEY_RESULT;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tamstudio.asm_duytam.dao.EnrollDAO;
import com.tamstudio.asm_duytam.model.Enroll;


public class EnrollService extends IntentService {

    public static final String ACTION_REGISTER_COURSE ="REGISTER_COURSE";
    public static final String ACTION_UN_REGISTER_COURSE = "UN_REGISTER_COURSE";
    public EnrollService() {
        super("EnrollService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Intent intentBroadcast = new Intent();
            boolean result = false;

            switch (intent.getAction()){
                case ACTION_REGISTER_COURSE:{
                    intentBroadcast = new Intent(ACTION_REGISTER_COURSE);
                    Enroll enroll = intent.getExtras().getParcelable("enroll");
                    result = EnrollDAO.getInstance(this).register(enroll);
                    break;
                }
                case ACTION_UN_REGISTER_COURSE:{
                    intentBroadcast = new Intent(ACTION_UN_REGISTER_COURSE);
                    String emailID = intent.getStringExtra("email");
                    int idCourse = intent.getIntExtra("idCourse",-1);
                    result = EnrollDAO.getInstance(this).unRegister(emailID,idCourse);
                    break;
                }
                default: break;
            }
            intentBroadcast.putExtra(KEY_RESULT,result);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);


        }
    }

}