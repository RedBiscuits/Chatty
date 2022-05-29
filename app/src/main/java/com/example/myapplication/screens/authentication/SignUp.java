package com.example.myapplication.screens.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.datastructures.chatty.R;
import com.datastructures.chatty.databinding.ActivitySignUpBinding;
import com.example.myapplication.utils.SharedPreferenceClass;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    private EditText descriptionEditText, registerUserNameEditText;
    private CircleImageView ProfilePhoto;
    private Uri profileImageUri = Uri.parse("https://10play.com.au/ip/s3/2022/01/28/a9333564010931a07b777e8c32f2ce8c-1123582.png?image-profile=image_max&io=landscape");
    SharedPreferenceClass sharedPreferenceClass;
    private final String DEFAULT_IMAGE = "https://www.pixsy.com/wp-content/uploads/2021/04/ben-sweet-2LowviVHZ-E-unsplash-1.jpeg";

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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //to hide title bar
        getSupportActionBar().hide(); //to hide action bar


        ActivitySignUpBinding binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        descriptionEditText = findViewById(R.id.description);
        registerUserNameEditText = findViewById(R.id.registering_user_name);
        ProfilePhoto=findViewById(R.id.current_user_profile_image);
        Button regbtn = findViewById(R.id.rigester_button);

        regbtn.setOnClickListener(view -> {
            // Performing Validation by calling validation functions
            if(!validateUsername() || !validateName()){
                Toast.makeText(this, "Make sure everything is ok and try again :)", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = registerUserNameEditText.getText().toString().trim();
            String descriptionStr = descriptionEditText.getText().toString().trim();
            String phoneNumber = getIntent().getStringExtra("phone");

            try {
                DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(phoneNumber);

                docRef.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()){
                            Toast.makeText(this, "phone number is already registered login instead", Toast.LENGTH_LONG).show();
                        }else {
                            ArrayList<String> arr = new ArrayList<String>();
                            Map<String,Object> user= new HashMap<>();
                            user.put("name", name);
                            user.put("description", descriptionStr);
                            user.put("profileImageUrl", profileImageUri.toString() != null ?
                                    profileImageUri.toString() : DEFAULT_IMAGE );
                            user.put("hasStory" , false);
                            user.put("friends" , arr);
                            user.put("lastStory" , "");
                            user.put("storyUrl" , "");
                            docRef.set(user);
                            Toast.makeText(this,"successfully registered",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.putExtra("done", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        });
        binding.currentUserProfileImage.setOnClickListener(view -> pickImg());
    }
    private void pickImg() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        this.startActivityForResult(intent, 3);
    }
    //Chek if The username is Valid or not
    private boolean validateUsername() {
        String val = registerUserNameEditText.getText().toString();


        if (val.isEmpty()) {
            registerUserNameEditText.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 20) {
            registerUserNameEditText.setError("Username too long");
            return false;

        } else {
            registerUserNameEditText.setError(null);
            return true;
        }
    }
    //Chek if The name is Valid or not
    private Boolean validateName() {
        String val = descriptionEditText.getText().toString();

        if (val.isEmpty()) {
            descriptionEditText.setError("Field cannot be empty");
            return false;
        }
        else {
            descriptionEditText.setError(null);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            profileImageUri = data.getData();
            ProfilePhoto.setImageURI(profileImageUri);
        }else{
            profileImageUri = Uri.parse("https://10play.com.au/ip/s3/2022/01/28/a9333564010931a07b777e8c32f2ce8c-1123582.png?image-profile=image_max&io=landscape");
        }

    }

}
