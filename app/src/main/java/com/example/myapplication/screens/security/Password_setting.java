package com.example.myapplication.screens.security;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.datastructures.chatty.R;

import java.util.List;

import io.paperdb.Paper;

public class Password_setting extends AppCompatActivity {
    PatternLockView mpatternLockView;
    String Save_Pattern_Key = "Pattern Code";
    String final_Pattern = "";
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        String SavePattern = Paper.book().read(Save_Pattern_Key);
        setContentView(R.layout.activity_pattern);
        mpatternLockView = (PatternLockView) findViewById(R.id.pattern_LockView);
        mpatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                final_Pattern = PatternLockUtils.patternToString(mpatternLockView, pattern);
            }

            @Override
            public void onCleared() {

            }
        });
        Button btnSetUp = findViewById(R.id.btnSetPattern);
        btnSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write(Save_Pattern_Key, final_Pattern);
                Toast.makeText(Password_setting.this, "Pattern saved Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
        btnDelete = findViewById(R.id.Delete_lock_App);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert Save_Pattern_Key != null;
                Paper.book().delete(Save_Pattern_Key);
                Paper.book().destroy();
                startActivity(new Intent(Password_setting.this,
                        com.example.myapplication.ui.main.SplashScreen.class));
                finish();
            }
        });


    }
}


