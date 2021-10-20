package com.tamstudio.asm_duytam.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharePreferences {

    public static final String MY_SHARE_PRE = "my_pre";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static MySharePreferences instance;
    public static MySharePreferences getInstance(Context context){
        if(instance==null){
            instance = new MySharePreferences(context);
        }
        return instance;
    }
    private MySharePreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(MY_SHARE_PRE,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void putStringValue(String key,String value){
        editor.putString(key,value);
        editor.apply();
    }
    public void putBooleanValue(String key,Boolean value){
        editor.putBoolean(key,value);
        editor.apply();
    }
    public String getStringValue(String key){
        return sharedPreferences.getString(key,null);
    }
    public boolean getBooleanValue(String key){
        return sharedPreferences.getBoolean(key,false);
    }
}
