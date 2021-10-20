package com.tamstudio.asm_duytam.activities;

import static com.tamstudio.asm_duytam.services.UserService.ACTION_REGISTER_USER;
import static com.tamstudio.asm_duytam.utilities.MyStructure.KEY_RESULT;
import static com.tamstudio.asm_duytam.utilities.MyStructure.TOAST_INVALID_EMAIL;
import static com.tamstudio.asm_duytam.utilities.MyStructure.TOAST_WARING_NOT_EMPTY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.model.User;
import com.tamstudio.asm_duytam.services.UserService;
import com.tamstudio.asm_duytam.utilities.MyStructure;
import com.tamstudio.asm_duytam.utilities.Utilities;

public class RegisterActivity extends AppCompatActivity {
    EditText edtName,edtEmail,edtPass;
    Button btnRegister;
    BroadcastReceiver userBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                boolean result = intent.getBooleanExtra(KEY_RESULT,false);
                if (ACTION_REGISTER_USER.equals(intent.getAction())) {
                    if (result) {
                        new Handler().postDelayed(() -> {
                            Utilities.showToast(RegisterActivity.this, MyStructure.TOAST_REGISTER_SUCCESS, R.drawable.ic_ok_toast, R.drawable.bg_ok_toast);
                            Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent1);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                        }, 2800);
                    } else {
                        Utilities.showToast(RegisterActivity.this, MyStructure.TOAST_USER_EXIST, R.drawable.ic_delete_toast, R.drawable.bg_delete_toast);
                    }
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        eventRegister();
    }

    private void initUI() {
        edtName = findViewById(R.id.edt_full_name_register);
        edtEmail = findViewById(R.id.edt_email_register);
        edtPass = findViewById(R.id.edt_password_register);
        btnRegister = findViewById(R.id.btn_register);
    }
    private void eventRegister() {
        btnRegister.setOnClickListener(view -> {
            String name = edtName.getText().toString();
            String email = edtEmail.getText().toString();
            String pass = edtPass.getText().toString();
            if(!isValidation(name,email,pass)){
                return;
            }
            User user = new User(email,pass,name,false); // mặt định đk người dùng
            Intent intent = new Intent(ACTION_REGISTER_USER,null,RegisterActivity.this,UserService.class);
            intent.putExtra("user",user);
            startService(intent);
        });
    }
    private boolean isValidation(String name,String email,String pass) {
        if(name.trim().isEmpty()||email.trim().isEmpty()||pass.trim().isEmpty()){
            Toast.makeText(RegisterActivity.this,TOAST_WARING_NOT_EMPTY,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
            Toast.makeText(RegisterActivity.this,TOAST_INVALID_EMAIL,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_REGISTER_USER);
        LocalBroadcastManager.getInstance(this).registerReceiver(userBroadcast,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userBroadcast);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}