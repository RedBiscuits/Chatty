package com.example.myapplication.utils;

import android.os.Bundle;

import androidx.recyclerview.widget.DiffUtil;

import com.example.myapplication.screens.chatroom.Message;

import java.util.ArrayList;

public class DiffUtillCall extends DiffUtil.Callback {

    ArrayList<Message> newList;

    public DiffUtillCall(ArrayList<Message> newList, ArrayList<Message> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    ArrayList<Message> oldList;

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).getTime().equals(oldList.get(oldItemPosition).getTime());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
         return newList.get(newItemPosition).getText()
                .equals(oldList.get(oldItemPosition).getText()) &&
                 newList.get(newItemPosition).getUser()
                         .equals(oldList.get(oldItemPosition).getUser());
    }
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        Message newMessage = newList.get(newItemPosition);
        Message oldMessage = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (!newMessage.getTime().equals(oldMessage.getTime())) {
            diff.putString("text", newMessage.getText());
            diff.putString("name", newMessage.getUser());
            diff.putString("time", newMessage.getTime());
        }
        if (diff.size() == 0) {
            return null;
        }
        return diff;
        //return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}


