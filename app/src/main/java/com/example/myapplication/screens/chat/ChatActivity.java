package com.example.myapplication.screens.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    TextView userChatName;
    TextView userChatPhone;
    CircleImageView userChatImage;
    String userName;
    String userPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = getIntent().getExtras();
//
//        userName = getIntent().getStringExtra("name");
//        userPhone = getIntent().getStringExtra("phone");;
        userName = extras.getString("name");
        userPhone = extras.getString("phone");

        userChatImage = findViewById(R.id.user_in_chat_image);
        userChatName = findViewById(R.id.user_in_chat_text);
        userChatPhone = findViewById(R.id.user_in_chat_phone);
        userChatName.setText(userName);
        userChatPhone.setText(userPhone);
    }
}