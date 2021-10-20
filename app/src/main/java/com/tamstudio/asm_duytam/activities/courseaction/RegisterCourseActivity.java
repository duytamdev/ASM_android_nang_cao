package com.tamstudio.asm_duytam.activities.courseaction;

import static com.tamstudio.asm_duytam.services.CourseService.ACTION_DELETE_COURSE;
import static com.tamstudio.asm_duytam.services.CourseService.ACTION_GET_ALL_COURSE;
import static com.tamstudio.asm_duytam.services.CourseService.ACTION_INSERT_COURSE;
import static com.tamstudio.asm_duytam.services.CourseService.ACTION_UPDATE_COURSE;
import static com.tamstudio.asm_duytam.services.EnrollService.ACTION_REGISTER_COURSE;
import static com.tamstudio.asm_duytam.utilities.MyStructure.*;

import androidx.annotation.NonNull;
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
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.adapters.CourseAdapter;
import com.tamstudio.asm_duytam.dao.CourseDAO;
import com.tamstudio.asm_duytam.dao.EnrollDAO;
import com.tamstudio.asm_duytam.model.Course;
import com.tamstudio.asm_duytam.model.Enroll;
import com.tamstudio.asm_duytam.services.CourseService;
import com.tamstudio.asm_duytam.services.EnrollService;
import com.tamstudio.asm_duytam.utilities.MySharePreferences;
import com.tamstudio.asm_duytam.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class RegisterCourseActivity extends AppCompatActivity {

    RecyclerView rvCourse;
    CourseAdapter courseAdapter;
    FloatingActionButton fabAddCourse;
    List<Course> list;
    String emailCurrentUser;
    BroadcastReceiver courseBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mess = "";
            boolean result = intent.getBooleanExtra(KEY_RESULT,false);
            switch (intent.getAction()){
                case ACTION_INSERT_COURSE:{
                    reloadList();
                    mess = result?TOAST_ADD_SUCCESS:TOAST_ADD_NOT_SUCCESS;
                    break;
                }
                case ACTION_DELETE_COURSE:{
                    reloadList();
                    mess = result?TOAST_DELETE_SUCCESS:TOAST_DELETE_NOT_SUCCESS;
                    break;
                }
                case ACTION_UPDATE_COURSE:{
                    reloadList();
                    mess = result?TOAST_UPDATE_SUCCESS:TOAST_UPDATE_NOT_SUCCESS;
                    break;
                }
                case ACTION_GET_ALL_COURSE:{
                    list = new ArrayList<>();
                    list = intent.getParcelableArrayListExtra("listCourse");
                    courseAdapter.setData(list);
                    break;
                }
                case ACTION_REGISTER_COURSE:{
                    reloadList();
                    mess = result? TOAST_REGISTER_SUCCESS: TOAST_REGISTER_NOT_SUCCESS;
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + intent.getAction());
            }
            if(mess.length()>0){
                Utilities.showToast(RegisterCourseActivity.this,mess,
                        result?R.drawable.ic_ok_toast:R.drawable.ic_delete_toast,
                        result?R.drawable.bg_ok_toast:R.drawable.bg_delete_toast);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_coure);
        emailCurrentUser = MySharePreferences.getInstance(this).getStringValue(SHARE_PREFERENCE_EMAIL_ID);
        initUI();
        initRecycler();
        actionAddCourse();
    }

    private void actionAddCourse() {
        fabAddCourse.setOnClickListener(view -> showDialogAddCourse());
    }

    private void showDialogAddCourse() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.register_course_dialog);
        EditText edtCode = bottomSheetDialog.findViewById(R.id.edt_code_course_add);
        EditText edtName = bottomSheetDialog.findViewById(R.id.edt_name_course_add);
        EditText edtNameTeacher = bottomSheetDialog.findViewById(R.id.edt_name_teacher_course_add);
        Button btnAdd = bottomSheetDialog.findViewById(R.id.btn_add_course);
        btnAdd.setText("Thêm Lớp Học");
        btnAdd.setOnClickListener(view -> {
            String code = edtCode.getText().toString();
            String name = edtName.getText().toString();
            String nameTeacher = edtNameTeacher.getText().toString();
            if(!checkValid(code,name,nameTeacher)){
                Toast.makeText(this,"Vui lòng điền đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                return;
            }
            Course course = new Course(code,name,nameTeacher);
            Intent intent = new Intent(ACTION_INSERT_COURSE,null,this, CourseService.class);
            intent.putExtra("course", (Parcelable) course);
            startService(intent);
            bottomSheetDialog.dismiss();
        });



        bottomSheetDialog.show();
    }
    private boolean checkValid(@NonNull String ...edt){
        for (String s: edt
             ) {
            if(s.trim().isEmpty()){
                return false;
            }
        }
        return true;
    }

    private void initRecycler() {
        courseAdapter = new CourseAdapter(this, new CourseAdapter.IMyClickCourseItem() {
            @Override
            public void clickRegisterCourse(Course course) {
                new AlertDialog.Builder(RegisterCourseActivity.this)
                        .setMessage("Bạn thật sự muốn đăng ký lớp học nay chứ ? ")
                        .setPositiveButton("Đăng kí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                actionRegisterCourse(course);
                            }
                        })
                        .setNegativeButton(BUTTON_DIALOG_CANCEL,null)
                        .show();
            }

            @Override
            public void clickUnRegisterCourse(int idCourse) {

            }

            @Override
            public void clickDeleteCourse(int idCourse) {
               confirmDeleteCourse(idCourse);
            }

            @Override
            public void clickEditCourse(Course course) {
                showDialogEditCourse(course);
            }
        },false);
        courseAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        linearLayoutManager.setStackFromEnd(true);
        rvCourse.setAdapter(courseAdapter);
        rvCourse.setLayoutManager(linearLayoutManager);

    }

    private void actionRegisterCourse(Course course) {
        String emailId = MySharePreferences.getInstance(this).getStringValue("email");
        Enroll enroll = new Enroll(emailId,course.getId());
        Intent intent = new Intent(ACTION_REGISTER_COURSE,null,this, EnrollService.class);
        intent.putExtra("enroll",enroll);
        startService(intent);
    }

    private void showDialogEditCourse(Course course) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.register_course_dialog);
        EditText edtCode = bottomSheetDialog.findViewById(R.id.edt_code_course_add);
        EditText edtName = bottomSheetDialog.findViewById(R.id.edt_name_course_add);
        EditText edtNameTeacher = bottomSheetDialog.findViewById(R.id.edt_name_teacher_course_add);
        Button btnUpdate = bottomSheetDialog.findViewById(R.id.btn_add_course);
        btnUpdate.setText("Cập Nhật Lớp Học");
        edtCode.setText(course.getCodeCourse());
        edtName.setText(course.getNameCourse());
        edtNameTeacher.setText(course.getNameTeacher());
        btnUpdate.setOnClickListener(view -> {
            String newCode = edtCode.getText().toString();
            String newName = edtName.getText().toString();
            String newNameTeacher = edtNameTeacher.getText().toString();
            if(!checkValid(newName,newCode,newNameTeacher)){
                return;
            }
            course.setCodeCourse(newCode);
            course.setNameCourse(newName);
            course.setNameTeacher(newNameTeacher);
            Intent intent = new Intent(ACTION_UPDATE_COURSE,null,this, CourseService.class);
            intent.putExtra("course", (Parcelable) course);
            startService(intent);
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    private void confirmDeleteCourse(int idCourse) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage(DIALOG_MESS_CONFIRM_DELETE)
                .setPositiveButton(BUTTON_DIALOG_DELETE, (dialogInterface, i) -> {
                    Intent intent = new Intent(ACTION_DELETE_COURSE,null,RegisterCourseActivity.this,CourseService.class);
                    intent.putExtra("idCourse",idCourse);
                    startService(intent);
                })
                .setNegativeButton(BUTTON_DIALOG_CANCEL,null)
                .show();
    }

    private void initUI() {
        rvCourse = findViewById(R.id.rv_course_list);
        fabAddCourse = findViewById(R.id.fab_add_course_list);

        if(Utilities.checkAdmin(this)){
            fabAddCourse.setVisibility(View.VISIBLE);
        }else{
            fabAddCourse.setVisibility(View.GONE);
        }
    }
    private void reloadList() {
        Intent intent = new Intent(ACTION_GET_ALL_COURSE,null,RegisterCourseActivity.this,CourseService.class);
        startService(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_INSERT_COURSE);
        intentFilter.addAction(ACTION_DELETE_COURSE);
        intentFilter.addAction(ACTION_UPDATE_COURSE);
        intentFilter.addAction(ACTION_GET_ALL_COURSE);
        intentFilter.addAction(ACTION_REGISTER_COURSE);
        LocalBroadcastManager.getInstance(this).registerReceiver(courseBroadcast,intentFilter);
        reloadList();

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(courseBroadcast);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}