package com.tamstudio.asm_duytam.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tamstudio.asm_duytam.fragments.AccountFragment;
import com.tamstudio.asm_duytam.fragments.CourseFragment;
import com.tamstudio.asm_duytam.fragments.MapsFragment;
import com.tamstudio.asm_duytam.fragments.NewsFragment;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1: return new NewsFragment();
            case 2: return new MapsFragment();
            case 3: return new AccountFragment();
            default: return new CourseFragment(); //   case 0
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
