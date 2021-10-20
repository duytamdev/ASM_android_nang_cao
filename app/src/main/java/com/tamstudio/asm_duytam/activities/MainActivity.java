package com.tamstudio.asm_duytam.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import android.annotation.SuppressLint;
import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.adapters.PagerAdapter;


public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    PagerAdapter pagerAdapter;
    ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        actionPager();
        actionBottomNav();
        viewPager2.setCurrentItem(0);// course fragment  = home
    }

    @SuppressLint("NonConstantResourceId")
    private void actionBottomNav() {
        chipNavigationBar.setOnItemSelectedListener(i -> {
            switch (i){
                case R.id.action_news: viewPager2.setCurrentItem(1); break;
                case R.id.action_map: viewPager2.setCurrentItem(2); break;
                case R.id.action_account: viewPager2.setCurrentItem(3); break;
                default:viewPager2.setCurrentItem(0); break;
            }
        });
    }

    private void actionPager() {
        pagerAdapter = new PagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 1: chipNavigationBar.setItemSelected(R.id.action_news,true); break;
                    case 2: chipNavigationBar.setItemSelected(R.id.action_map,true); break;
                    case 3: chipNavigationBar.setItemSelected(R.id.action_account,true); break;
                    default: chipNavigationBar.setItemSelected(R.id.action_course,true); break;
                }
            }
        });
        viewPager2.setUserInputEnabled(false);
    }

    private void initUI() {
        viewPager2 = findViewById(R.id.view_pager);
        chipNavigationBar = findViewById(R.id.bottom_nav_chip);
    }

}