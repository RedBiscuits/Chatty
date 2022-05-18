package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.screens.authentication.LoginFormActivity;

public class DataProvider extends AppCompatActivity {

    SharedPreferences sharedPreferences = getSharedPreferences(LoginFormActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    public String phoneNumber = sharedPreferences.getString(LoginFormActivity.KEY_PHONE_NUMBER, null);
}
