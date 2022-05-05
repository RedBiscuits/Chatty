package com.example.myapplication.screens.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                userName= "NotPassedProbably :/";
                userPhone = "NotPassedProbably :/";
            } else {
                userPhone= extras.getString("phone");
                userName = extras.getString("name");
            }
        } else {
            userName= (String) savedInstanceState.getSerializable("name");
            userPhone= (String) savedInstanceState.getSerializable("phone");

        }
        userChatImage = findViewById(R.id.user_in_chat_image);
        userChatName = findViewById(R.id.user_in_chat_text);
        userChatPhone = findViewById(R.id.user_in_chat_phone);
        userChatName.setText(userName);
        userChatPhone.setText(userPhone);

        userChatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, VideoActivity.class);
                startActivity(intent);
            }
        });
    }
}