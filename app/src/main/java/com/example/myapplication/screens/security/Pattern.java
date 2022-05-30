package com.example.myapplication.screens.security;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.datastructures.chatty.R;
import com.example.myapplication.screens.authentication.LoginActivity;
import com.sarnava.textwriter.TextWriter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import io.paperdb.Paper;
import timber.log.Timber;

public class Pattern extends AppCompatActivity {
    String Save_Pattern_Key = "Pattern Code";
    String final_Pattern = "";
    PatternLockView mpatternLockView;
    Button btnCancel;
    private TextWriter textWriter;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private static final int REQUESTCODE = 101010;
    LottieAnimationView FingerprintLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        String SavePattern = Paper.book().read(Save_Pattern_Key);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        setContentView(R.layout.mainpattern);
        FingerprintLogin = findViewById(R.id.Use_finger_print);


        if (SavePattern != null && !SavePattern.equals("null")) {

            if(Integer.parseInt(android.os.Build.VERSION.SDK) >= 28) {
                BiometricManager biometricManager = BiometricManager.from(this);
                switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {//We will Switch some constant to check different possibility
                    case BiometricManager.BIOMETRIC_SUCCESS://this is  mean that  the user can use the biometric sensor
                        Timber.tag("MY_APP_TAG").d("App can authenticate using biometrics.");
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE://this is mean that the device don't have fingerprint sensor
                        Timber.tag("MY_APP_TAG").e("No biometric features available on this device.");
                        Toast.makeText(this, "Fingerprint sensor Not exist", Toast.LENGTH_SHORT).show();
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Toast.makeText(this, "Fingerprint Not recognize", Toast.LENGTH_SHORT).show();

                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        // Prompts the user to create credentials that your app accepts.
                        try {
                            final Intent enrollIntent = new Intent(Settings.ACTION_FINGERPRINT_ENROLL);
                            startActivityForResult(enrollIntent, REQUESTCODE);
                        } catch (Exception e) {
                            Toast.makeText(this, "No registered fingerprint",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                //start create biometric dialog box
                Executor executor = ContextCompat.getMainExecutor(this);
                biometricPrompt = new BiometricPrompt(Pattern.this,
                        executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode,
                                                      @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(getApplicationContext(),
                                        " Error: " + errString, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override

                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        startActivity(new Intent(Pattern.this, LoginActivity.class));
                        Toast.makeText(getApplicationContext(), " succeeded!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getApplicationContext(), "Authentication failed",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });
                //Biometric Dialog
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(" login for Chatty")
                        .setSubtitle("Use Your Fingerprint to login Your App")
                        .setNegativeButtonText("Cancel")
                        .build();

                //call the Dialog when the user press the  Fingerprint Button
                FingerprintLogin.setOnClickListener(view -> {
                    biometricPrompt.authenticate(promptInfo);
                });
            }
            else {
                Toast.makeText(this, "Fingerprint not supported",
                        Toast.LENGTH_LONG).show();
            }

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
                                LoginActivity.class));
                    } else
                        Toast.makeText(Pattern.this, "Incorrect!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCleared() {

                }

            });
            btnCancel = findViewById(R.id.cancel_button);
            btnCancel.setOnClickListener(v -> {
                finishAndRemoveTask();
                finishAffinity();
                System.exit(0);
            });
            textWriter=findViewById(R.id.textwriter);
            textWriter.setWidth(8)
                    .setDelay(30)
                    .setColor(R.color.md_theme_dark_onSecondaryContainer)
                    .setConfig(TextWriter.Configuration.INTERMEDIATE)
                    .setSizeFactor(50f)
                    .setLetterSpacing(20f)
                    .setText("CARBON")
                    .startAnimation();
        }
        else startActivity(new Intent(Pattern.this,
                com.example.myapplication.ui.main.SplashScreen.class));
    }

}