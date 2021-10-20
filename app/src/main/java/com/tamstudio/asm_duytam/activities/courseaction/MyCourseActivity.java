package com.tamstudio.asm_duytam.activities.courseaction;

import static com.tamstudio.asm_duytam.services.CourseService.ACTION_GET_MY_COURSE;
import static com.tamstudio.asm_duytam.services.EnrollService.ACTION_UN_REGISTER_COURSE;
import static com.tamstudio.asm_duytam.utilities.MyStructure.BUTTON_DIALOG_CANCEL;
import static com.tamstudio.asm_duytam.utilities.MyStructure.BUTTON_DIALOG_DELETE;
import static com.tamstudio.asm_duytam.utilities.MyStructure.DIALOG_MESS_CONFIRM_DELETE;
import static com.tamstudio.asm_duytam.utilities.MyStructure.KEY_RESULT;
import static com.tamstudio.asm_duytam.utilities.MyStructure.SHARE_PREFERENCE_EMAIL_ID;
import static com.tamstudio.asm_duytam.utilities.MyStructure.TOAST_UN_REGISTER_NOT_SUCCESS;
import static com.tamstudio.asm_duytam.utilities.MyStructure.TOAST_UN_REGISTER_SUCCESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.adapters.CourseAdapter;
import com.tamstudio.asm_duytam.dao.CourseDAO;
import com.tamstudio.asm_duytam.dao.EnrollDAO;
import com.tamstudio.asm_duytam.model.Course;
import com.tamstudio.asm_duytam.services.CourseService;
import com.tamstudio.asm_duytam.services.EnrollService;
import com.tamstudio.asm_duytam.utilities.MySharePreferences;
import com.tamstudio.asm_duytam.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class MyCourseActivity extends AppCompatActivity {

    RecyclerView rvMyCourse;
    CourseAdapter courseAdapter;
    List<Course> list;
     String emailUser;
     BroadcastReceiver myCourseBroadcast = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_GET_MY_COURSE)){
                list = new ArrayList<>();
                list = intent.getParcelableArrayListExtra("listMyCourse");
                courseAdapter.setData(list);
            }
            if(intent.getAction().equals(ACTION_UN_REGISTER_COURSE)){
                boolean result = intent.getBooleanExtra(KEY_RESULT,false);
                Utilities.showToast(MyCourseActivity.this
                        ,result?TOAST_UN_REGISTER_SUCCESS: TOAST_UN_REGISTER_NOT_SUCCESS
                        ,result?R.drawable.ic_ok_toast: R.drawable.ic_delete_toast
                        ,result?R.drawable.bg_ok_toast:R.drawable.bg_delete_toast);
                reloadList();
            }
         }
     };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);
        emailUser = MySharePreferences.getInstance(this).getStringValue(SHARE_PREFERENCE_EMAIL_ID);
        initUI();
    }

    private void initUI() {
        rvMyCourse = findViewById(R.id.rv_my_course);
        courseAdapter = new CourseAdapter(this, new CourseAdapter.IMyClickCourseItem() {
            @Override
            public void clickRegisterCourse(Course course) {

            }

            @Override
            public void clickUnRegisterCourse(int idCourse) {
                unRegisterCourse(idCourse);
            }

            @Override
            public void clickDeleteCourse(int idCourse) {

            }

            @Override
            public void clickEditCourse(Course course) {

            }
        },true);
        rvMyCourse.setAdapter(courseAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        linearLayoutManager.setStackFromEnd(true);
        rvMyCourse.setLayoutManager(linearLayoutManager);
    }

    private void unRegisterCourse(int idCourse) {
        new AlertDialog.Builder(this)
                .setMessage(DIALOG_MESS_CONFIRM_DELETE)
                .setPositiveButton(BUTTON_DIALOG_DELETE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ACTION_UN_REGISTER_COURSE,null,MyCourseActivity.this, EnrollService.class);
                        intent.putExtra("email",emailUser);
                        intent.putExtra("idCourse",idCourse);
                        startService(intent);
                    }
                })
                .setNegativeButton(BUTTON_DIALOG_CANCEL,null)
                .show();
    }

    private void reloadList(){
          Intent intent = new Intent(ACTION_GET_MY_COURSE,null,this, CourseService.class);
          startService(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GET_MY_COURSE);
        intentFilter.addAction(ACTION_UN_REGISTER_COURSE);
        LocalBroadcastManager.getInstance(this).registerReceiver(myCourseBroadcast,intentFilter);
        reloadList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myCourseBroadcast);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}