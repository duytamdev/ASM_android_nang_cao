package com.tamstudio.asm_duytam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.activities.courseaction.MyCourseActivity;
import com.tamstudio.asm_duytam.activities.courseaction.RegisterCourseActivity;
import com.tamstudio.asm_duytam.activities.courseaction.ScheduleActivity;
import com.tamstudio.asm_duytam.adapters.SliderAdapter;

public class CourseFragment extends Fragment {

    View mView;
    Intent intent;
    SliderView sliderView;
    SliderAdapter sliderAdapter;
    ImageView ivCourseRegister,ivMyCourse,ivSchedule;
    int[] imagesSlide = {R.drawable.girl1,R.drawable.girl2,R.drawable.girl3,R.drawable.girl4,R.drawable.girl5};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_course,container,false);
        initUI();
        initSlider();
        eventsCourse();
        return mView;
    }

    private void initUI() {
        sliderView = mView.findViewById(R.id.sliderView);
        ivCourseRegister = mView.findViewById(R.id.iv_action_course_register_course);
        ivMyCourse = mView.findViewById(R.id.iv_action_course_registered);
        ivSchedule = mView.findViewById(R.id.iv_action_schedule_course);
    }

    private void initSlider() {
        sliderAdapter = new SliderAdapter(getContext(),imagesSlide);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }
    private void eventsCourse() {
        ivCourseRegister.setOnClickListener(view -> {
            intent = new Intent(getContext(), RegisterCourseActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        });
        ivMyCourse.setOnClickListener(view -> {
            intent = new Intent(getContext(), MyCourseActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        });
        ivSchedule.setOnClickListener(view -> {
            intent = new Intent(getContext(), ScheduleActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        });
    }
}
