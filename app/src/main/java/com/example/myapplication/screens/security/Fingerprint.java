package com.example.myapplication.screens.security;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.datastructures.chatty.R;
import com.example.myapplication.ui.main.SplashScreen;

import java.util.concurrent.Executor;

import timber.log.Timber;

public class Fingerprint extends AppCompatActivity {

    private static final int REQUESTCODE = 101010;
    ImageView FingerprintLogin;
    Button btnPattern;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        FingerprintLogin = findViewById(R.id.Fingerprint);
        btnPattern = findViewById(R.id.Pattern_action);

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
        }
        else {
            Toast.makeText(this, "Fingerprint not supported",
                    Toast.LENGTH_LONG).show();
            finish();
        }


        //start create biometric dialog box
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(Fingerprint.this,
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
                startActivity(new Intent(Fingerprint.this, SplashScreen.class));
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
        btnPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Fingerprint.this, Pattern.class));
            }
        });

    }
}
