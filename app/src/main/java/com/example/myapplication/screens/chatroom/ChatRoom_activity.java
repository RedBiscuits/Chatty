package com.example.myapplication.screens.chatroom;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datastructures.chatty.R;
import com.example.myapplication.adapters.MessageAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class ChatRoom_activity extends AppCompatActivity {

    //references to activity view items
    private RecyclerView mMessageRecycler;
    private MessageAdapter mMessageAdapter;
    private EditText msgTxt;

    //local state and current user handle
    private User calleeUser ;
    private String currentMsg;

    //required data structures
    private ArrayList<Message> messageList = new ArrayList<>();
    private Map<String,String> lastMessage = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_chat_room);

        //initializing data
        SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone",null);
        String name = sharedPreferences.getString("name",null);
        User currentUser = new User(name , phone);

        //get UI references
        CircleImageView img = findViewById(R.id.recImg);
        TextView recName = findViewById(R.id.TVname);
        Button sendBtn = findViewById(R.id.sendBtn);
        msgTxt = findViewById(R.id.msgTxt);
        ImageButton vidCall = findViewById(R.id.vidCall);

        //Getting intent or saved instance data
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
               finish();
            } else {
               calleeUser = new User(extras.getString("name"), extras.getString("RecPhone"));
               recName.setText(calleeUser.getName());
               if (extras.getString("image") != null){
                   Glide.with(this).load(extras.getString("image")).placeholder(org.jitsi.meet.sdk.R.drawable.images_avatar).into(img);
               }
            }
        }
        else {
            calleeUser = new User((String) savedInstanceState.getSerializable("name"),
                    (String) savedInstanceState.getSerializable("RecPhone"));
        }
        Timber.tag(TAG).d("Users : >> "+calleeUser.getProfile()+" "+ calleeUser.getName());

        //database references
        String currentDoc = getDoc(currentUser.getProfile(), calleeUser.getProfile());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("chatRooms").document(currentDoc);
        CollectionReference collRef = docRef.collection("Messages");

        //get old messages
        collRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Message msg = document.toObject(Message.class);
                    messageList.add(msg);
                }
            } else {
                Timber.tag(TAG).d(task.getException(),"Error getting Messages documents: ");
            }
        });

        //resolving messages list and passing it to the view adapter
        mMessageRecycler = findViewById(R.id.recycler_chat);
        mMessageAdapter = new MessageAdapter(this, messageList ,phone);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
        RecyclerView rv_messages = this.mMessageRecycler;
        rv_messages.setAdapter(this.mMessageAdapter);

        //listen to chat
        collRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Timber.tag(TAG).w(e, "listen:error");
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Message msg = dc.getDocument().toObject(Message.class);
                            try {
                                if (!msg.getTime()
                                        .equals(messageList.get(messageList.size()-1).getTime())){
                                    messageList.add(msg);
                                    updateState();
                                }
                            }
                            catch (Exception x){
                                Timber.tag(TAG).d("Empty messages");
                            }
                            Timber.tag(TAG).d("New Message: %s", dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            Timber.tag(TAG).d("Modified Message: %s", dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Timber.tag(TAG).d("Removed Message: %s", dc.getDocument().getData());
                            break;
                    }
                }

            }
        });

       //Enable video calls
        vidCall.setOnClickListener(view -> {
            if(currentDoc.length() > 0){
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions
                        .Builder()
                        .setRoom("CarbonVideoCall" + currentDoc)
                        .build();
                JitsiMeetActivity.launch(this,options);
            }
            else Snackbar.make(view , "Invalid Room ID" , BaseTransientBottomBar.LENGTH_SHORT).show();
        });

        //Send button action
        sendBtn.setOnClickListener(view -> {
            //Remove extra spaces and break lines
            currentMsg = msgTxt.getText().toString().trim();
            if (!currentMsg.equals(""))
            {
                // Add a new message object to the end of messages arraylist
                Message tmp = new Message(currentMsg ,
                        new Timestamp(System.currentTimeMillis()).toString()
                        , currentUser.getProfile() );
                messageList.add(mMessageAdapter.getItemCount(), tmp);
                updateState();

                //Send message to firebase and handle success / failure
                lastMessage.put("text" , tmp.getText());
                lastMessage.put("time" , tmp.getTime());
                lastMessage.put("user" , tmp.getUser());
                collRef.document()
                        .set(lastMessage, SetOptions.merge())
                        .addOnSuccessListener(aVoid -> Timber.tag(TAG).d("DocumentSnapshot successfully written!"))
                        .addOnFailureListener(e -> Timber.tag(TAG).w(e, "Error writing document"));
                //empty the message holder to receive the next message
                msgTxt.setText("");
            }
        });
    }

    //to get document reference for current chat
    @NonNull
    private String getDoc(String num1 , String num2){
            if(num1.compareTo(num2) > 0)
                    return num1+num2;
            else return num2+num1;
    }
    private void updateState(){
        mMessageAdapter.notifyItemInserted(mMessageAdapter.getItemCount());
        mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount());
    }
}