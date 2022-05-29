package com.example.myapplication.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.screens.authentication.LoginTapFragment;
import com.example.myapplication.screens.authentication.SignupTapFragment;

public class LoginAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public LoginAdapter(FragmentManager fragmentManager, Context context, int totalTabs) {
        super(fragmentManager);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new LoginTapFragment();
            case 1:
                return new SignupTapFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
