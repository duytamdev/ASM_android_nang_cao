package com.tamstudio.asm_duytam.activities;

import static com.tamstudio.asm_duytam.services.UserService.ACTION_CHECK_LOGIN;
import static com.tamstudio.asm_duytam.services.UserService.ACTION_LOGIN_GOOGLE;
import static com.tamstudio.asm_duytam.utilities.MyStructure.KEY_RESULT;
import static com.tamstudio.asm_duytam.utilities.MyStructure.SHARE_PREFERENCE_EMAIL_ID;
import static com.tamstudio.asm_duytam.utilities.MyStructure.SHARE_PREFERENCE_IS_LOGGED;
import static com.tamstudio.asm_duytam.utilities.MyStructure.SHARE_PREFERENCE_LOGIN_SOCIAL;
import static com.tamstudio.asm_duytam.utilities.MyStructure.SHARE_PREFERENCE_PASS;
import static com.tamstudio.asm_duytam.utilities.MyStructure.SHARE_PREFERENCE_REMEMBER;
import static com.tamstudio.asm_duytam.utilities.MyStructure.TOAST_INVALID_EMAIL;
import static com.tamstudio.asm_duytam.utilities.MyStructure.TOAST_INVALID_EMAIL_OR_PASS;
import static com.tamstudio.asm_duytam.utilities.MyStructure.TOAST_LOGIN_SUCCESS;
import static com.tamstudio.asm_duytam.utilities.MyStructure.TOAST_WARING_NOT_EMPTY_EMAIL_PASS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.model.User;
import com.tamstudio.asm_duytam.services.UserService;
import com.tamstudio.asm_duytam.utilities.MySharePreferences;
import com.tamstudio.asm_duytam.utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    EditText edtEmail,edtPass;
    Button btnLogin;
    TextView tvRegister;
    CheckBox chkRemember;
    ImageView btnLoginGoogle;
    GoogleSignInClient googleSignInClient;
    private static final String EMAIL = "email";

    LoginButton loginButton;
    CallbackManager callbackManager;
    BroadcastReceiver userBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                String action = intent.getAction();
                if(action.equals(ACTION_CHECK_LOGIN)|| action.equals(ACTION_LOGIN_GOOGLE)){
                    boolean result = intent.getBooleanExtra(KEY_RESULT,false);
                    if(result){


                        Utilities.showToast(LoginActivity.this,TOAST_LOGIN_SUCCESS,R.drawable.ic_ok_toast,R.drawable.bg_ok_toast);
                        Intent intent1 = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                        finish();
                        MySharePreferences.getInstance(LoginActivity.this).putBooleanValue(SHARE_PREFERENCE_IS_LOGGED,true);
                    }else{
                        Utilities.showToast(LoginActivity.this,
                                TOAST_INVALID_EMAIL_OR_PASS,R.drawable.ic_delete_toast,R.drawable.bg_delete_toast);
                    }
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        rememberUser();
        events();
        loginGoogle();
        loginFacebook();
//        try {
//            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.tamstudio.asm_duytam", PackageManager.GET_SIGNATURES);
//            for(Signature signature: packageInfo.signatures){
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//                messageDigest.update(signature.toByteArray());
//                Log.d("KeyHash:>>>>>>>>>>>>>>>>>", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
//            }
//        }catch (Exception e){}

    }

    private void loginFacebook() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.btn_login_fbb);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
//                String userId = loginResult.getAccessToken().getUserId();
//                Log.d("userID --------->", "onSuccess: "+userId);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String name = object.getString("name");
                                    Intent userIntent = new Intent(ACTION_LOGIN_GOOGLE,null,LoginActivity.this, UserService.class);
                                    User user =  new User(email,"",name,false);
                                    userIntent.putExtra("user",user);
                                    ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                                    progressDialog.setTitle("Vui lòng chờ");
                                    progressDialog.setMessage("Đang tải ...");
                                    progressDialog.show();
                                    new Handler().postDelayed(progressDialog::dismiss,2000);
                                    startService(userIntent);
                                    MySharePreferences.getInstance(LoginActivity.this).putStringValue(SHARE_PREFERENCE_EMAIL_ID,email);
                                    MySharePreferences.getInstance(LoginActivity.this).putBooleanValue(SHARE_PREFERENCE_LOGIN_SOCIAL,true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("onCancel ------->", "onCancel: ");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void loginGoogle() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient   = GoogleSignIn.getClient(this,options);
        btnLoginGoogle.setOnClickListener(view -> {
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent,1000);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String email = account.getEmail();
                String name = account.getDisplayName();
                Intent userIntent = new Intent(ACTION_LOGIN_GOOGLE,null,LoginActivity.this, UserService.class);
                User user =  new User(email,"",name,false);
                userIntent.putExtra("user",user);
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Vui lòng chờ");
                progressDialog.setMessage("Đang tải ...");
                progressDialog.show();
                new Handler().postDelayed(progressDialog::dismiss,2000);
                startService(userIntent);
                MySharePreferences.getInstance(this).putStringValue(SHARE_PREFERENCE_EMAIL_ID,email);
                MySharePreferences.getInstance(this).putBooleanValue(SHARE_PREFERENCE_LOGIN_SOCIAL,true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initUI() {
        edtEmail = findViewById(R.id.edt_email_login);
        edtPass = findViewById(R.id.edt_password_login);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_sign_up);
        chkRemember = findViewById(R.id.chk_remember);
        btnLoginGoogle = findViewById(R.id.btn_login_gg);
    }

    private void events() {
        btnLogin.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            String pass = edtPass.getText().toString();
            if(!isValidation(email,pass)){
                return;
            }
            Intent userIntent = new Intent(ACTION_CHECK_LOGIN,null,LoginActivity.this, UserService.class);
            userIntent.putExtra("email",email);
            userIntent.putExtra("pass",pass);
            startService(userIntent);
            MySharePreferences.getInstance(LoginActivity.this).putBooleanValue(SHARE_PREFERENCE_LOGIN_SOCIAL,false);


        });
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        });
    }

    private boolean isValidation(String email,String pass) {
        if(email.trim().isEmpty()||pass.trim().isEmpty()){
            Toast.makeText(LoginActivity.this,TOAST_WARING_NOT_EMPTY_EMAIL_PASS,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
            Toast.makeText(LoginActivity.this,TOAST_INVALID_EMAIL,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void rememberUser(){
        String email = MySharePreferences.getInstance(this).getStringValue(SHARE_PREFERENCE_EMAIL_ID);
        String pass = MySharePreferences.getInstance(this).getStringValue(SHARE_PREFERENCE_PASS);
        boolean rememberMe = MySharePreferences.getInstance(this).getBooleanValue(SHARE_PREFERENCE_REMEMBER);
        edtEmail.setText(email);
        edtPass.setText(pass);
        chkRemember.setChecked(rememberMe);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // neu nguoi dung login bang google or facebook thi ko luu cac value tren form dang nhap
        if(MySharePreferences.getInstance(this).getBooleanValue(SHARE_PREFERENCE_LOGIN_SOCIAL)){
            return;
        }
        MySharePreferences.getInstance(this).putStringValue(SHARE_PREFERENCE_EMAIL_ID,edtEmail.getText().toString());
        if(chkRemember.isChecked()){
            MySharePreferences.getInstance(this).putStringValue(SHARE_PREFERENCE_PASS,edtPass.getText().toString());
            MySharePreferences.getInstance(this).putBooleanValue(SHARE_PREFERENCE_REMEMBER,true);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userBroadcast);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ACTION_CHECK_LOGIN);
        intentFilter.addAction(ACTION_LOGIN_GOOGLE);

        LocalBroadcastManager.getInstance(this).registerReceiver(userBroadcast,intentFilter);
    }
}