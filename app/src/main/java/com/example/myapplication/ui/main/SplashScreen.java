package com.example.myapplication.ui.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.datastructures.chatty.R;
import com.datastructures.chatty.databinding.ActivitySplashScreenBinding;
import com.example.myapplication.screens.authentication.LoginFormActivity;
import com.example.myapplication.utils.Amar;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActivitySplashScreenBinding binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler handler = new Handler();

        handler.postDelayed(() -> {
            //Do something after delay
            finish();
            startActivity(new Intent(SplashScreen.this, LoginFormActivity.class));
        }, 3000);
    }
}

