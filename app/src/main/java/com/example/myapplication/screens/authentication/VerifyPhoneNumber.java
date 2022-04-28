package com.example.myapplication.screens.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityVerifyPhoneNumberBinding;
import com.example.myapplication.ui.main.home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumber extends AppCompatActivity {
    String verificationCodesBySystem;
    Button verify_Btn;
    EditText phoneNoEnteredByTheUser;
    ProgressBar progressBar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ActivityVerifyPhoneNumberBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        verify_Btn = findViewById(R.id.verify_Btn);
        phoneNoEnteredByTheUser = findViewById(R.id.editTextNumberSigned);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //To receive the phone no, we will use getIntent()
        String phoneNo = getIntent().getStringExtra("phoneNo");
        sendVerificationCodeToUser(phoneNo);

        binding.verifyBtn.setOnClickListener(v -> {

            String code = phoneNoEnteredByTheUser.getText().toString();

            if (code.isEmpty() || code.length() < 6) {
                phoneNoEnteredByTheUser.setError("Wrong verification code. ");
                phoneNoEnteredByTheUser.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }

        });

    }

    private void sendVerificationCodeToUser(String phoneNo) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+20" + phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    //In this function, we will handle the possible events occurred while verifying the SMS code.
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //Get the code in global variable
            verificationCodesBySystem = s;
        }

        //This is the function performed the automatic verification
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }

        }

        //It will be executed when the verification will be failed. So to handle that we will simply show the error message to the user.
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneNumber.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    //It will be used to verify the credentials entered by the user
    // furthermore match them with the system generated verification ID.
    private void verifyCode(String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodesBySystem, verificationCode);
        signInTheUserByCredential(credential);

    }


    private void signInTheUserByCredential(PhoneAuthCredential credential) {


        mAuth.signInWithCredential(credential).addOnCompleteListener(VerifyPhoneNumber.this, new OnCompleteListener<AuthResult>() {

            private void showInfoToUser(Task<AuthResult> task) {
                //here manage the exceptions and show relevant information to user
            }

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String NewMessage="......";
                if (task.isSuccessful()) {
                    Toast.makeText(VerifyPhoneNumber.this, "Success", Toast.LENGTH_SHORT).show();

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        NewMessage = "This account is already Exist";
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.progressBar), NewMessage, Snackbar.LENGTH_LONG);
                        snackbar.setAction("Dismiss", v -> {

                        });
                        snackbar.show();

                    }
                    Intent intent = new Intent(VerifyPhoneNumber.this , home.class);
                    startActivity(intent);

                }
            }

        });
    }
}