package com.tamstudio.asm_duytam.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.adapters.PagerAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    PagerAdapter pagerAdapter;
    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        actionPager();
        actionBottomNav();
    }

    private void actionBottomNav() {
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_news: viewPager2.setCurrentItem(0); break;
                    case R.id.action_course: viewPager2.setCurrentItem(1); break;
                    case R.id.action_map: viewPager2.setCurrentItem(2); break;
                    default:viewPager2.setCurrentItem(0); break;
                }
                return true;
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
                    case 0: bottomNav.getMenu().findItem(R.id.action_news).setChecked(true); break;
                    case 1: bottomNav.getMenu().findItem(R.id.action_course).setChecked(true); break;
                    case 2: bottomNav.getMenu().findItem(R.id.action_map).setChecked(true); break;
                    default: bottomNav.getMenu().findItem(R.id.action_news).setChecked(true); break;
                }
            }
        });
    }

    private void initUI() {
        viewPager2 = findViewById(R.id.view_pager);
        bottomNav = findViewById(R.id.bottom_nav);
    }
}