package com.example.myapplication.screens.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.utils.Amar;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class VideoActivity extends  AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Amar.setMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
//        try {
//            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                    .setServerURL(new URL(""))
//                    .setWelcomePageEnabled(true)
//                    .build();
//            JitsiMeetActivity.launch(this , options);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }

    public void onButtonClick(View view) {

        EditText roomIdInput = findViewById(R.id.room_id);
        String roomId = roomIdInput.getText().toString();

        if(roomId.length() > 0){
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions
                    .Builder()
                    .setRoom(roomId)
                    .build();
            JitsiMeetActivity.launch(this,options);


        }

    }
}