package com.tamstudio.asm_duytam.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.dao.UserDAO;
import com.tamstudio.asm_duytam.model.Course;
import com.tamstudio.asm_duytam.model.User;
import com.tamstudio.asm_duytam.utilities.MySharePreferences;

import java.util.List;
import java.util.Random;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>{

    public interface IMyClickCourseItem{
        void clickRegisterCourse(Course course);
        void clickUnRegisterCourse(int idCourse);
        void clickDeleteCourse(int idCourse);
        void clickEditCourse(Course course);
    }
    int[] resImgsAvt =
            new int[]{R.drawable.course1,R.drawable.course2,R.drawable.course3,R.drawable.course4,R.drawable.course5};
    IMyClickCourseItem iMyClickCourseItem;
    Context context;
    List<Course> list;
    String emailIDUser ;
    boolean flagRegistered;
    User user ;

    public CourseAdapter( Context context,IMyClickCourseItem iMyClickCourseItem,boolean flagRegistered) {
        this.context = context;
        this.flagRegistered = flagRegistered;
        this.iMyClickCourseItem = iMyClickCourseItem;
        emailIDUser=  MySharePreferences.getInstance(context).getStringValue("email");
        user = UserDAO.getInstance(context).getById(emailIDUser);
    }

    public void setData(List<Course> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_item,parent,false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = list.get(position);
        if(course==null) return;

        int numRan = resImgsAvt.length-1;
        holder.imgResCourse.setImageResource(resImgsAvt[new Random().nextInt(numRan)]);


        boolean isAdmin = user.isAdmin();
        if(!isAdmin){
            holder.layoutActionAdmin.setVisibility(View.GONE);
        }else{
            holder.layoutActionAdmin.setVisibility(View.VISIBLE);
            holder.tvDelete.setOnClickListener((View view) -> iMyClickCourseItem.clickDeleteCourse(course.getId()));
            holder.tvUpdate.setOnClickListener(view -> iMyClickCourseItem.clickEditCourse(course));
        }
        holder.tvCode.setText("Mã môn hoc: "+course.getCodeCourse());
        holder.tvName.setText(course.getNameCourse());
        holder.tvTeacher.setText(course.getNameTeacher());
        if(!flagRegistered){
            holder.tvRegister.setOnClickListener(view -> iMyClickCourseItem.clickRegisterCourse(course));
            holder.tvRegister.setText("Đăng Kí");
            holder.tvRegister.setBackgroundColor(Color.parseColor("#8bc34a"));
        }else{
            holder.tvRegister.setOnClickListener(view -> iMyClickCourseItem.clickUnRegisterCourse(course.getId()));
            holder.tvRegister.setText("Huỷ Đăng Kí");
            holder.tvRegister.setBackgroundColor(Color.parseColor("#F51720"));
        }

    }

    @Override
    public int getItemCount() {
        if(list!=null) return list.size();
        return 0;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode,tvName,tvTeacher,tvRegister,tvUpdate,tvDelete;
        SwipeRevealLayout swipeRevealLayout;
        LinearLayout layoutActionAdmin;
        ImageView imgResCourse;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            imgResCourse = itemView.findViewById(R.id.iv_resImgCourse);
            tvUpdate = itemView.findViewById(R.id.tv_update_course_item);
            tvDelete = itemView.findViewById(R.id.tv_delete_course_item);
            layoutActionAdmin = itemView.findViewById(R.id.layout_action_admin);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_layout);
            tvCode = itemView.findViewById(R.id.tv_code_course_item);
            tvName = itemView.findViewById(R.id.tv_name_course_item);
            tvTeacher = itemView.findViewById(R.id.tv_name_teacher_course_item);
            tvRegister = itemView.findViewById(R.id.tv_regis_course_item);
        }
    }
}
