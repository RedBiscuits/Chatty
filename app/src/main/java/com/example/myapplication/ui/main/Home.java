package com.example.myapplication.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.datastructures.chatty.R;
import com.datastructures.chatty.databinding.ActivityMainBinding;
import com.example.myapplication.screens.chatroom.Message;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    private CircleImageView currentUserProfileImage;
    public static boolean hasRetrieved = false;
    public static ArrayList<Message> oldData = new ArrayList<Message>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar


        //Binding and drawing layout
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Tab layout setup
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        //Views
        Button settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this::goToSettings);
        currentUserProfileImage = findViewById(R.id.current_user_profile_image);

        //Vars
        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone" , null);


        binding.currentUserProfileImage.setOnClickListener(view -> goToProfile(phone));

        try {
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(phone);
            docRef.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        String imageUrl = doc.getString("profileImageUrl");
                        Glide.with(getApplicationContext()).load(imageUrl).
                                diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(currentUserProfileImage);
                    }else {
                        Toast.makeText(this,
                                "User doesn't exist !",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


    public void goToSettings(View view) {
        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
    }

    public void goToProfile( String phone) {
        Intent intent =new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra("phone" , phone);
        startActivity(intent);
    }

}