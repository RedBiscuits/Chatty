package com.example.myapplication.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.datastructures.chatty.databinding.ActivitySplashScreenBinding;
import com.example.myapplication.screens.authentication.LoginFormActivity;
import com.example.myapplication.screens.security.Pattern;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActivitySplashScreenBinding binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String Save_Pattern_Key = "Pattern Code";
        Handler handler = new Handler();
        Paper.init(this);
        String SavePattern = Paper.book().read(Save_Pattern_Key);
        handler.postDelayed(() -> {
                if (SavePattern != null && !SavePattern.equals("null")) {
                    startActivity(new Intent(SplashScreen.this, Pattern.class));
                }
                else{
                //Do something after delay
                    finish();
                    startActivity(new Intent(SplashScreen.this, LoginFormActivity.class));
                }
            }, 3000);
    }
}

