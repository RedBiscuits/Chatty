package com.example.myapplication.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.datastructures.chatty.R;
import com.datastructures.chatty.databinding.ActivityMainBinding;
import com.example.myapplication.screens.authentication.SharedPreferenceClass;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class home extends AppCompatActivity {

    private ActivityMainBinding binding ;
    private static SharedPreferenceClass sharedPreferenceClass;
    private Button settingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        //Binding and drawing layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Tab layout setup
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings(view);
            }
        });
    }


    public void goToSettings(View view) {
        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
    }

}