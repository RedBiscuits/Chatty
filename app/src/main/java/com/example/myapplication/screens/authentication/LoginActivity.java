package com.example.myapplication.screens.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.datastructures.chatty.R;
import com.example.myapplication.adapters.LoginAdapter;
import com.example.myapplication.ui.main.Home;
import com.example.myapplication.utils.SharedPreferenceClass;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    SharedPreferenceClass sharedPreferenceClass;
    SharedPreferences sharedPreferences;
    float v = 0;
    public static final String SHARED_PREFERENCES_NAME = "mypref";
    public static final String KEY_PHONE_NUMBER = "phone";
    private static final String KEY_CURRENT_USER = "name";
    private static final String KEY_PROFILE_IMAGE = "image";
    private static final String KEY_BIO = "bio";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //to hide action bar
        sharedPreferenceClass = new SharedPreferenceClass(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(KEY_PHONE_NUMBER, null);
        if(phoneNumber != null) {
            Intent intent = new Intent(LoginActivity.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        setContentView(R.layout.new_activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager_login);

        tabLayout.setupWithViewPager(viewPager);

        LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        loginAdapter.addFragment(new LoginTapFragment(), "Login");
        loginAdapter.addFragment(new SignupTapFragment(), "Register");
        viewPager.setAdapter(loginAdapter);


        tabLayout.setTranslationY(300);
        tabLayout.setAlpha(v);
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

    }
}