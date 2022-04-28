package com.example.myapplication.screens.authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {


    EditText RegName, RegUsername, RegPhoneNo, RegPassword; //
    Button RegBtn, RegToLoginBtn;
    CircleImageView ProfilePhoto;
    FloatingActionButton changePhoto;
    DatabaseReference databaseReference ;
    ActivitySignUpBinding binding;
    //Chek if The username is Valid or not

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RegName = findViewById(R.id.editTextTextPersonName2);
        RegUsername = findViewById(R.id.editTextTextPersonName);
        RegPassword = findViewById(R.id.editTextTextPassword3);
        RegPhoneNo = findViewById(R.id.editTextPhone2);
        RegBtn = findViewById(R.id.button);
        changePhoto=findViewById(R.id.ChangePhoto);
        ProfilePhoto=findViewById(R.id.profile_image);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapp-001-7d61f-default-rtdb.firebaseio.com");
        binding.button.setOnClickListener(view -> {
            // Performing Validation by calling validation functions
            if(!validateUsername() || !validateName() || ! validatePhoneNo() || !validatePassword()){
                Toast.makeText(this, "Some shit happened bro ", Toast.LENGTH_SHORT).show();
                return;
            }
            String Name = RegName.getText().toString().trim();
            String Username = RegUsername.getText().toString().trim();

            //Get the Phone No from phone no field in String
            String Phone = RegPhoneNo.getText().toString().trim();
            String Password = RegPassword.getText().toString().trim();

            //Call the next activity and pass phone no with it
            Intent intent = new Intent(SignUp.this, VerifyPhoneNumber.class);
            //To receive the phone no
            intent.putExtra("phoneNo", Phone);
            startActivity(intent);


        });
        binding.ChangePhoto.setOnClickListener(v -> com.github.drjacky.imagepicker.ImagePicker.Companion.with
                (SignUp.this)
                .maxResultSize(1080,1080)
                .start(20));

    }


    private boolean validateUsername() {
        String val = RegUsername.getText().toString();


        if (val.isEmpty()) {
            RegUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            RegUsername.setError("Username too long");
            return false;

        } else {
            RegUsername.setError(null);
            return true;
        }
    }
    //Chek if The name is Valid or not
    private Boolean validateName() {
        String val = RegName.getText().toString();

        if (val.isEmpty()) {
            RegName.setError("Field cannot be empty");
            return false;
        }
        else {
            RegName.setError(null);
            return true;
        }
    }
    //Chek if The phoneNumber is Valid or not
    private Boolean validatePhoneNo() {
        String val = RegPhoneNo.getText().toString();

        if (val.isEmpty()) {
            RegPhoneNo.setError("Field cannot be empty");
            return false;
        } else {
            RegPhoneNo.setError(null);
            return true;
        }
    }
    //Chek if The Password is Valid or not

    private Boolean validatePassword() {
        String val = RegPassword.getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";

        if (val.isEmpty()) {
            RegPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            RegPassword.setError("Password is too weak");
            return false;
        } else {
            RegPassword.setError(null);

            return true;
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = null;
        if (data != null) {
            uri = data.getData();
        }else{
            Toast.makeText(this, "meow meow meow", Toast.LENGTH_SHORT).show();
        }
        ProfilePhoto.setImageURI(uri);


    }
}
