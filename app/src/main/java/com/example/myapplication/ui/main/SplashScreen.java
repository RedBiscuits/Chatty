package com.example.myapplication.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.datastructures.chatty.databinding.ActivitySplashScreenBinding;
import com.example.myapplication.screens.authentication.LoginActivity;
import com.example.myapplication.screens.security.Pattern;
import com.example.myapplication.utils.SharedPreferenceClass;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {
    public static final String SHARED_PREFERENCES_NAME = "mypref";
    public static final String KEY_PHONE_NUMBER = "phone";
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
                    SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(this);
                    SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                    String phoneNumber = sharedPreferences.getString(KEY_PHONE_NUMBER, null);
                    if(phoneNumber != null) {

                        Intent intent = new Intent(this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }else {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    }

                }
            }, 3000);
    }
}

