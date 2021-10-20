package com.tamstudio.asm_duytam.activities.courseaction;

import static com.tamstudio.asm_duytam.services.ScheduleService.ACTION_DELETE_SCHEDULE;
import static com.tamstudio.asm_duytam.services.ScheduleService.ACTION_INSERT_SCHEDULE;
import static com.tamstudio.asm_duytam.services.ScheduleService.ACTION_UPDATE_SCHEDULE;
import static com.tamstudio.asm_duytam.utilities.MyStructure.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.adapters.ScheduleAdapter;
import com.tamstudio.asm_duytam.dao.CourseDAO;
import com.tamstudio.asm_duytam.dao.ScheduleDAO;
import com.tamstudio.asm_duytam.dao.UserDAO;
import com.tamstudio.asm_duytam.model.Course;
import com.tamstudio.asm_duytam.model.Schedule;
import com.tamstudio.asm_duytam.services.ScheduleService;
import com.tamstudio.asm_duytam.utilities.MySharePreferences;
import com.tamstudio.asm_duytam.utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    RecyclerView rvSchedule;
    FloatingActionButton fabAddSchedule;
    ScheduleAdapter scheduleAdapter;
    List<Schedule> list;
    List<Course> courseList;
    String emailUser;
    boolean isAdmin;
    BroadcastReceiver scheduleBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mess ;
            boolean result = intent.getBooleanExtra(KEY_RESULT,false);
            switch (intent.getAction()){
                case ACTION_INSERT_SCHEDULE:{

                    mess = result? TOAST_ADD_SUCCESS: TOAST_ADD_NOT_SUCCESS;
                    break;
                }
                case ACTION_DELETE_SCHEDULE:{
                    mess = result?TOAST_DELETE_SUCCESS:TOAST_DELETE_NOT_SUCCESS;
                    break;
                }
                case ACTION_UPDATE_SCHEDULE:{
                    mess = result?TOAST_UPDATE_SUCCESS:TOAST_UPDATE_NOT_SUCCESS;
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + intent.getAction());
            }
                Utilities.showToast(ScheduleActivity.this,mess,
                        result?R.drawable.ic_ok_toast:R.drawable.ic_delete_toast,
                        result?R.drawable.bg_ok_toast:R.drawable.bg_delete_toast);
            reloadList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        emailUser = MySharePreferences.getInstance(getApplicationContext()).getStringValue(SHARE_PREFERENCE_EMAIL_ID);
        isAdmin = UserDAO.getInstance(getApplicationContext()).getById(emailUser).isAdmin();
        initUI();
        initRecycler();
        actionFabAdd();
    }

    private void actionFabAdd() {
        fabAddSchedule.setOnClickListener(view -> {
            openDialogAddSchedule();
        });
    }

    private void openDialogAddSchedule() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ScheduleActivity.this);
        bottomSheetDialog.setContentView(R.layout.add_schedule_dialog);
        Spinner spinnerNameCourse = bottomSheetDialog.findViewById(R.id.sp_name_course);
        EditText edtRoom = bottomSheetDialog.findViewById(R.id.edt_room);
        EditText edtMeet = bottomSheetDialog.findViewById(R.id.edt_meet);
        CheckBox chkIsTest = bottomSheetDialog.findViewById(R.id.chk_isTest);
        ImageView ivDateTime = bottomSheetDialog.findViewById(R.id.iv_date_time_schedule);
        TextView tvDate = bottomSheetDialog.findViewById(R.id.tv_date_schedule_add);
        TextView tvTime = bottomSheetDialog.findViewById(R.id.tv_time_schedule_add);
        Button btnAdd = bottomSheetDialog.findViewById(R.id.btn_add_schedule);
        btnAdd.setText("Thêm lịch học");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,getNameCourses());
        spinnerNameCourse.setAdapter(arrayAdapter);
        tvDate.setText(Utilities.DateToString(Calendar.getInstance().getTime(), Utilities.formatDate));
        tvTime.setText(Utilities.DateToString(Calendar.getInstance().getTime(), Utilities.formatTime));

        ivDateTime.setOnClickListener(view -> openDatePicker(tvDate,tvTime));
        tvDate.setOnClickListener(view ->   openDatePicker(tvDate,tvTime));
        tvTime.setOnClickListener(view -> openDatePicker(tvDate,tvTime));
        btnAdd.setOnClickListener(view -> {
            try {
                int indexSpinnerChoice = (int) spinnerNameCourse.getSelectedItemId();
                Date date = Utilities.StringToDate(tvDate.getText().toString(),Utilities.formatDate);
                Date time = Utilities.StringToDate(tvTime.getText().toString(),Utilities.formatTime);
                int idCourse = courseList.get(indexSpinnerChoice).getId();
                String room = edtRoom.getText().toString();
                String meet = edtMeet.getText().toString();
                if(!isValid(room,meet)) return;
                boolean isTest = chkIsTest.isChecked();
                Schedule schedule = new Schedule(date,time,room,meet,isTest,idCourse);
                Intent intent = new Intent(ACTION_INSERT_SCHEDULE,null,ScheduleActivity.this, ScheduleService.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("schedule",schedule);
                intent.putExtras(bundle);
                startService(intent);
            }catch (Exception e){
                Toast.makeText(ScheduleActivity.this,TOAST_ADD_NOT_SUCCESS,Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            bottomSheetDialog.dismiss();

        });
        bottomSheetDialog.show();

    }
    private void openDatePicker(TextView tvDate,TextView tvTime){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                // year , month , day
                calendar.set(i,i1,i2);
                tvDate.setText(Utilities.DateToString(calendar.getTime(), Utilities.formatDate));

            }
        }, Utilities.YEAR,Utilities.MONTH,Utilities.DAY);
        datePickerDialog.show();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                // hour, minus
                calendar.set(0,0,0,i,i1);
                tvTime.setText(Utilities.DateToString(calendar.getTime(), Utilities.formatTime));
            }
        },Utilities.HOUR,Utilities.MINUS,true);
        timePickerDialog.show();
    }

    private List<String> getNameCourses(){
        List<String> list = new ArrayList<>();
        courseList = CourseDAO.getInstance(this).getAll();
        for (Course c: courseList) {
            list.add(c.getCodeCourse()+" - "+c.getNameCourse());
        }
        return list;
    }

    private void initRecycler() {
        scheduleAdapter = new ScheduleAdapter(this, new ScheduleAdapter.IMyClickScheduleItem() {
            @Override
            public void clickDeleteSchedule(int id) {
                confirmDeleteSchedule(id);
            }

            @Override
            public void clickEditSchedule(Schedule schedule) {
                    openDialogUpdateSchedule(schedule);
            }
        });
        scheduleAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        linearLayoutManager.setStackFromEnd(true);
        rvSchedule.setAdapter(scheduleAdapter);
        rvSchedule.setLayoutManager(linearLayoutManager);
    }

    private void openDialogUpdateSchedule(Schedule schedule) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ScheduleActivity.this);
        bottomSheetDialog.setContentView(R.layout.add_schedule_dialog);
        Spinner spinnerNameCourse = bottomSheetDialog.findViewById(R.id.sp_name_course);
        EditText edtRoom = bottomSheetDialog.findViewById(R.id.edt_room);
        EditText edtMeet = bottomSheetDialog.findViewById(R.id.edt_meet);
        CheckBox chkIsTest = bottomSheetDialog.findViewById(R.id.chk_isTest);
        ImageView ivDateTime = bottomSheetDialog.findViewById(R.id.iv_date_time_schedule);
        TextView tvDate = bottomSheetDialog.findViewById(R.id.tv_date_schedule_add);
        TextView tvTime = bottomSheetDialog.findViewById(R.id.tv_time_schedule_add);
        Button btnAdd = bottomSheetDialog.findViewById(R.id.btn_add_schedule);
        btnAdd.setText("Cập Nhật Lịch Học");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,getNameCourses());
        spinnerNameCourse.setAdapter(arrayAdapter);
        tvDate.setText(Utilities.DateToString(schedule.getDate(), Utilities.formatDate));
        tvTime.setText(Utilities.DateToString(schedule.getTime(), Utilities.formatTime));
        edtRoom.setText(schedule.getAddress());
        edtMeet.setText(schedule.getMeet());
        chkIsTest.setChecked(schedule.isTestSchedule());
        ivDateTime.setOnClickListener(view -> openDatePicker(tvDate,tvTime));
        tvDate.setOnClickListener(view ->   openDatePicker(tvDate,tvTime));
        tvTime.setOnClickListener(view -> openDatePicker(tvDate,tvTime));
        btnAdd.setOnClickListener(view -> {
            try {
                int indexSpinnerChoice = (int) spinnerNameCourse.getSelectedItemId();
                Date date = Utilities.StringToDate(tvDate.getText().toString(),Utilities.formatDate);
                Date time = Utilities.StringToDate(tvTime.getText().toString(),Utilities.formatTime);
                int idCourse = courseList.get(indexSpinnerChoice).getId();
                String room = edtRoom.getText().toString();
                String meet = edtMeet.getText().toString();
                if(!isValid(room,meet)) return;
                boolean isTest = chkIsTest.isChecked();
                // update
                schedule.setCourseId(idCourse);
                schedule.setDate(date);
                schedule.setTime(time);
                schedule.setTestSchedule(isTest);
                schedule.setAddress(room);
                schedule.setMeet(meet);

                Intent intent = new Intent(ACTION_UPDATE_SCHEDULE,null,ScheduleActivity.this, ScheduleService.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("schedule",schedule);
                intent.putExtras(bundle);
                startService(intent);
            }catch (Exception e){
                Toast.makeText(ScheduleActivity.this,TOAST_ADD_NOT_SUCCESS,Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            bottomSheetDialog.dismiss();
        });




        bottomSheetDialog.show();

    }

    private void confirmDeleteSchedule(int id) {
        new AlertDialog.Builder(this)
                .setMessage(DIALOG_MESS_CONFIRM_DELETE)
                .setPositiveButton(BUTTON_DIALOG_DELETE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(ACTION_DELETE_SCHEDULE,null,ScheduleActivity.this,ScheduleService.class);
                        intent.putExtra("idSchedule",id);
                        startService(intent);
                    }
                })
                .setNegativeButton(BUTTON_DIALOG_CANCEL,null)
                .show();

    }

    private boolean isValid(String ...agm){
        for (String s:agm
             ) {
            if(s.trim().isEmpty()){
                Toast.makeText(ScheduleActivity.this,"Vui vòng điền đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    private void initUI() {
        rvSchedule = findViewById(R.id.rv_schedule);
        fabAddSchedule = findViewById(R.id.fab_add_schedule);
        if(Utilities.checkAdmin(this)){
            fabAddSchedule.setVisibility(View.VISIBLE);
        }else{
            fabAddSchedule.setVisibility(View.GONE);
        }
    }

    private void reloadList() {
        list = new ArrayList<>();
        if(isAdmin){
            list = ScheduleDAO.getInstance(this).getAll();
        }else{
            list = ScheduleDAO.getInstance(this).getScheduleOfUser(emailUser);
        }
        scheduleAdapter.setData(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_INSERT_SCHEDULE);
        intentFilter.addAction(ACTION_UPDATE_SCHEDULE);
        intentFilter.addAction(ACTION_DELETE_SCHEDULE);
//        intentFilter.addAction(ACTION_GET_SCHEDULES);
        LocalBroadcastManager.getInstance(this).registerReceiver(scheduleBroadcast,intentFilter);
        reloadList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(scheduleBroadcast);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}