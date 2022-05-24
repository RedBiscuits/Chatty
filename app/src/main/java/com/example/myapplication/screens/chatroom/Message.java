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

class User {
    private String name;
    private String profile;

    public User(String name, String profile) {
        this.name = name;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}