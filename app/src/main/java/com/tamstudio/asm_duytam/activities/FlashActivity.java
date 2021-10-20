package com.tamstudio.asm_duytam.activities;

import static com.tamstudio.asm_duytam.utilities.MyStructure.SHARE_PREFERENCE_IS_LOGGED;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.utilities.MySharePreferences;

public class FlashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        new Handler().postDelayed(() -> {
            Intent intent;
            boolean isLogged = MySharePreferences.getInstance(FlashActivity.this).getBooleanValue(SHARE_PREFERENCE_IS_LOGGED);
            if(isLogged){
                 intent = new Intent(FlashActivity.this,MainActivity.class);
            }else{
                 intent = new Intent(FlashActivity.this,LoginActivity.class);
            }
            startActivity(intent);
            finish();
        },2500);
    }
}