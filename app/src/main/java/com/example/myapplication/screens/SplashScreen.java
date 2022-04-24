package com.example.myapplication.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySplashScreenBinding;
import com.example.myapplication.ui.main.home;

public class SplashScreen extends AppCompatActivity {
    private ActivitySplashScreenBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bot);


        binding.logo.startAnimation(topAnim);
        binding.name.startAnimation(bottomAnim);
        binding.designImage.startAnimation(topAnim);

        Handler handler = new Handler();

        handler.postDelayed(() -> {
            //Do something after delay
            finish();
            startActivity(new Intent(SplashScreen.this, home.class));
        }, 3000);
    }

}

