package com.tamstudio.asm_duytam.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.dao.CourseDAO;
import com.tamstudio.asm_duytam.dao.UserDAO;
import com.tamstudio.asm_duytam.model.Course;
import com.tamstudio.asm_duytam.model.Schedule;
import com.tamstudio.asm_duytam.model.User;
import com.tamstudio.asm_duytam.utilities.MySharePreferences;
import com.tamstudio.asm_duytam.utilities.Utilities;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>{

    public interface IMyClickScheduleItem{
        void clickDeleteSchedule(int id);
        void clickEditSchedule(Schedule schedule);
    }
    String emailIDUser ;
    User user ;
    IMyClickScheduleItem iMyClickScheduleItem;
    List<Schedule> list;
    Context context;

    public ScheduleAdapter( Context context,IMyClickScheduleItem iMyClickScheduleItem) {
        this.iMyClickScheduleItem = iMyClickScheduleItem;
        this.context = context;
        emailIDUser=  MySharePreferences.getInstance(context).getStringValue("email");
        user = UserDAO.getInstance(context).getById(emailIDUser);
    }

    public void setData(List<Schedule> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item,parent,false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = list.get(position);
        if(schedule==null) return;
        boolean isAdmin = user.isAdmin();
        holder.swipeRevealLayout.setLockDrag(!isAdmin);
        Course course = CourseDAO.getInstance(context).getById(schedule.getCourseId());
        holder.tvNameCourse.setText(course.getNameCourse());
        //holder.tvDate.setText("Date: "+ Utilities.DateToString(schedule.getDate(),Utilities.formatDate));
        holder.tvDate.setText(Utilities.DateToString(schedule.getDate(),Utilities.formatMonth));

        holder.tvTime.setText(Utilities.DateToString(schedule.getTime(),Utilities.formatTime2));
        holder.tvRoom.setText("Room: "+schedule.getAddress());
        holder.tvMeet.setText("Meet: "+schedule.getMeet());
        boolean isTest = schedule.isTestSchedule();
        if(isTest){
            holder.tvIsTest.setText("Thi đi");
            holder.tvIsTest.setTextColor(Color.parseColor("#FF5D59"));
            holder.tvDate.setBackgroundResource(R.color.red);
            holder.tvTime.setBackgroundResource(R.color.red);
        }else{
            holder.tvDate.setBackgroundResource(R.color.blue);
            holder.tvTime.setBackgroundResource(R.color.blue);
            holder.tvIsTest.setText("Học đi");
            holder.tvIsTest.setTextColor(Color.parseColor("#3EB2FF"));
        }
        holder.tvUpdate.setOnClickListener(view -> iMyClickScheduleItem.clickEditSchedule(schedule));
        holder.tvDelete.setOnClickListener(view -> iMyClickScheduleItem.clickDeleteSchedule(schedule.getId()));
    }


    @Override
    public int getItemCount() {
        if(list!=null) return list.size();
        return 0;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameCourse,tvDate,tvTime,tvRoom,tvMeet,tvIsTest,tvUpdate,tvDelete;
        SwipeRevealLayout swipeRevealLayout;
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUpdate = itemView.findViewById(R.id.tv_update_schedule_item);
            tvDelete = itemView.findViewById(R.id.tv_tv_delete_schedule_item);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_layout_schedule);
            tvNameCourse = itemView.findViewById(R.id.tv_name_course_schedule_item);
            tvDate = itemView.findViewById(R.id.tv_date_schedule_item);
            tvRoom = itemView.findViewById(R.id.tv_room_schedule_item);
            tvTime = itemView.findViewById(R.id.tv_time_schedule_item);
            tvMeet = itemView.findViewById(R.id.tv_meet_schedule_item);
            tvIsTest = itemView.findViewById(R.id.tv_is_test_schedule_item);

        }
    }
}
