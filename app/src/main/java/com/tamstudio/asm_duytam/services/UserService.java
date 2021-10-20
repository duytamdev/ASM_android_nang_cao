package com.tamstudio.asm_duytam.services;

import static com.tamstudio.asm_duytam.utilities.MyStructure.KEY_RESULT;

import android.app.IntentService;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tamstudio.asm_duytam.dao.UserDAO;
import com.tamstudio.asm_duytam.model.User;

public class UserService extends IntentService {
    public static final String ACTION_REGISTER_USER ="REGISTER_USER";
    public static final String ACTION_CHECK_LOGIN ="CHECK_LOGIN_USER";
    public static final String ACTION_LOGIN_GOOGLE ="LOGIN_GOOGLE";
    public static final String ACTION_UPDATE_USER ="UPDATE_USER";
    public static final String ACTION_CHECK_EXITS = "CHECK_EXITS";



    public UserService() {
        super("UserService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Intent intentBroadcast = new Intent();
            boolean result = false;
            User user = intent.getParcelableExtra("user");
           switch (intent.getAction()){
               case  ACTION_REGISTER_USER:{
                   boolean isExits = UserDAO.getInstance(this).checkRegister(user.getEmail());
                   intentBroadcast = new Intent(ACTION_REGISTER_USER);
                   if(!isExits){
                       result = UserDAO.getInstance(this).register(user);
                   }else{
                       result = false;
                   }
                   break;
               }
               case ACTION_CHECK_LOGIN:{
                   intentBroadcast = new Intent(ACTION_CHECK_LOGIN);
                   String email = intent.getStringExtra("email");
                   String pass = intent.getStringExtra("pass");
                   result = UserDAO.getInstance(this).checkLogin(email,pass);
                   break;
               }
               case ACTION_UPDATE_USER:{
                   intentBroadcast = new Intent(ACTION_UPDATE_USER);
                   result = UserDAO.getInstance(this).update(user);
                   break;
               }
               case ACTION_CHECK_EXITS:{
                   intentBroadcast = new Intent(ACTION_CHECK_EXITS);
                   String email = intent.getStringExtra("emailRes");
                   result = UserDAO.getInstance(this).checkRegister(email);
                   break;
               }
               case ACTION_LOGIN_GOOGLE:{
                   intentBroadcast = new Intent(ACTION_LOGIN_GOOGLE);
                   result = UserDAO.getInstance(this).loginGoogle(user);
                   break;
               }
           }
           intentBroadcast.putExtra(KEY_RESULT,result);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);
        }
    }

}