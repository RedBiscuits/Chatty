package com.example.myapplication.models;

public class UserModel {
    private String name;
    private String imageUri;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String uri) {
        this.imageUri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private String msg;

    public String getMsg() {
        return msg;
    }

    public UserModel(String name, String msg, String uri) {
        this.name = name;
        this.msg = msg;
        this.imageUri=uri;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
