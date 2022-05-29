package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datastructures.chatty.R;
import com.example.myapplication.screens.chatroom.Message;
import com.example.myapplication.screens.security.Pattern;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter {

    @NonNull
    private Context mContext;
    private ArrayList<Message> mMessageList;
    private String currentUser;
    public MessageAdapter(Context context, ArrayList<Message> messageList , String currentUser) {
        mContext = context;
        mMessageList = messageList;
        this.currentUser = currentUser;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_msg, parent, false);
            return new SentMessageHolder(view);
        }
        else if(viewType == 2){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.other_msg, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case 1:
                ((SentMessageHolder) holder).bind(message);
                break;
            case 2:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList != null ? mMessageList.size() : 0;
    }
    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.TVuser_msg);
            timeText = itemView.findViewById(R.id.TVuser_ts);
        }

        void bind(Message message) {
            messageText.setText(message.getText());
            timeText.setText(formateTime(message));
        }
    }
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);
        if (message.getUser().equals(currentUser)) {
            // If the current user is the sender of the message
            return 1;
        } else {
            // If another user sent the message
            return 2;
        }
    }
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.TVrec_txt);
            timeText =  itemView.findViewById(R.id.TVrec_timeStamp);
            nameText =  itemView.findViewById(R.id.TVsender);
        }
        void bind(@NonNull Message message) {
            messageText.setText(message.getText());
            timeText.setText(formateTime(message));
            nameText.setText(message.getUser());
        }
    }
    private String formateTime(Message message){
        return message.getTime().substring(0 ,
                message.getTime().length()-7 >0 ?
                        message.getTime().length()-7
                        : message.getTime().length());
    }
}

