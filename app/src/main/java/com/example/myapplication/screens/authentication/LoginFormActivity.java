package com.example.myapplication.screens.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.datastructures.chatty.R;
import com.example.myapplication.ui.main.Home;
import com.example.myapplication.utils.SharedPreferenceClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LoginFormActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_NAME = "mypref";
    public static final String KEY_PHONE_NUMBER = "phone";
    private static final String KEY_CURRENT_USER = "name";
    private static final String KEY_PROFILE_IMAGE = "image";
    private static final String KEY_BIO = "bio";


    SharedPreferenceClass sharedPreferenceClass;
    EditText phone, OTP;
    Button verifyOTPBtn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    String mVerificationId;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    private DocumentReference documentReference;
    private FirebaseFirestore firestore;
    String phoneNumber, image, username, bio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceClass = new SharedPreferenceClass(this);
        if(sharedPreferenceClass.loadNightModeState()) {
            setTheme(R.style.dark_theme);
        }
        else {
            setTheme(R.style.app_theme);
        }

        super.onCreate(savedInstanceState);

        getSupportActionBar().hide(); //to hide action bar

        setContentView(R.layout.activity_login);

        initWidgets();

        String phoneNumber = sharedPreferences.getString(KEY_PHONE_NUMBER, null);
        if(phoneNumber != null) {
            Intent intent = new Intent(LoginFormActivity.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void initWidgets() {
        phone =  findViewById(R.id.editTextPhoneNumber);
        verifyOTPBtn =  findViewById(R.id.buttonVerifyOTP);
        OTP =  findViewById(R.id.editTextOTP);
        progressBar =  findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

    }

    public void generateOTP(View view) { //on click in generate OTP button to generate OTP code to sent it to user

        phoneNumber = phone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) { //check if user doesn't entered his phone number
            phone.setError("Please Enter Your Phone Number.");
            phone.requestFocus();
            return;
        }
        checkUser(phoneNumber); //check if is a user or not by phone number
    }

    public void checkUser(String phoneNumber) {
        documentReference = firestore.collection("users").document(phoneNumber);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        image = doc.getString("profileImageUrl");
                        username = doc.getString("name");
                        bio = doc.getString("bio");
                        sendVerificationCode(phoneNumber); //to sent verification code to user
                    }else{
                        Intent signUp = new Intent(LoginFormActivity.this,SignUp.class);
                        signUp.putExtra("phone" , phoneNumber);
                        startActivityForResult(signUp ,1);
                    }
                }
            }
        });
    }


    public void verifyOTP(View view) { //on click in verify OTP button to check if OTP code entered by user is true or not
        String OTPCode = OTP.getText().toString();
        boolean[] found = new boolean[1];
        String phoneNumber = phone.getText().toString();
        documentReference = firestore.collection("users").document(phoneNumber);
        documentReference.get().addOnCompleteListener(
            new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                       found[0] =doc.exists();
                    }
                }
            }
        );
        if (!found[0]){
            Toast.makeText(this , "aaaaaaaaaaaaaa" , Toast.LENGTH_LONG);
        }

        if(TextUtils.isEmpty(OTPCode)) { //to check if user entered OTP code or not
            OTP.setError("Please Enter Your OTP Code.");
            OTP.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        verifyCode(OTPCode); //to check if OTP code entered by user is true or not
        SharedPreferences.Editor editor = sharedPreferences.edit(); //store data in shared preferences
        editor.putString(KEY_PHONE_NUMBER, bio);
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.putString(KEY_CURRENT_USER, username);
        editor.putString(KEY_PROFILE_IMAGE, image);
        editor.apply();
    } //on click in verify OTP button

    private void sendVerificationCode(String phoneNumber) { //to sent verification message to user by his phone number
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+20"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks (what to do when code sent)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if(code != null) {
                progressBar.setVisibility(View.VISIBLE); //to display loading icon
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) { //if OTP code is wrong
            Toast.makeText(getApplicationContext(), "Verification Failed!",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId ,token);
            mVerificationId = verificationId;
            Toast.makeText(getApplicationContext(), "Code Sent...", Toast.LENGTH_SHORT).show();
            OTP.requestFocus();
            verifyOTPBtn.setEnabled(true);
            verifyOTPBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_purple)));
            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    private void verifyCode(String code) { //to verify the code that user sent by the code in sms
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
        signInByCredentials(credential);
    }

    private void signInByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        firebaseauth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                Intent intent =new Intent(getApplicationContext(), Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                OTP.setError("InValid OTP Code");
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                sendVerificationCode(phoneNumber);
            }
            else Toast.makeText(this , "User not reggistered" , Toast.LENGTH_LONG).show();
        }
    } //onActivityResult

    public void toSignup(View view) {
        startActivity(new Intent(LoginFormActivity.this , SignUp.class));
    }
}