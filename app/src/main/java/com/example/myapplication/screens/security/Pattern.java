package com.example.myapplication.screens.security;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.datastructures.chatty.R;
import com.example.myapplication.screens.authentication.LoginFormActivity;

import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class Pattern extends AppCompatActivity {
    String Save_Pattern_Key = "Pattern Code";
    String final_Pattern = "";
    PatternLockView mpatternLockView;
    Button btnCancel, btnFinger, btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        String SavePattern = Paper.book().read(Save_Pattern_Key);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar

        if (SavePattern != null && !SavePattern.equals("null")) {
            setContentView(R.layout.mainpattern);
            mpatternLockView = (PatternLockView) findViewById(R.id.patternLockView);
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
                    if (final_Pattern.equals(SavePattern)) {
                        Toast.makeText(Pattern.this, "Correct!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Pattern.this,
                                LoginFormActivity.class));
                    } else
                        Toast.makeText(Pattern.this, "Incorrect!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCleared() {

                }

            });
            btnCancel = findViewById(R.id.cancel_button);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();

                }
            });
            btnFinger = findViewById(R.id.Use_finger_print);
            btnFinger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Pattern.this,
                            com.example.myapplication.screens.security.Fingerprint.class));
                }
            });


        } else
            startActivity(new Intent(Pattern.this,
                    com.example.myapplication.ui.main.SplashScreen.class));


    }

}