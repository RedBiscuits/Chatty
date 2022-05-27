package com.example.myapplication.screens.chatroom;

import androidx.annotation.NonNull;

public class Message implements  Cloneable {

    private String text;
    private String time;
    private String user;


    public Message(){};

    public Message(String text, String time, String user) {
        this.text = text;
        this.time = time;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @NonNull
    @Override
    public Message clone() {

        Message clone;
        try {
            clone = (Message) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); //should not happen
        }

        return clone;
    }


}

