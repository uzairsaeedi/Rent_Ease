package com.example.rentease.admin.model;

public class UserModel {
    public String profile_image;
    public String name,email,username,user_id;

    public UserModel() {
        // Default constructor
    }

    public UserModel(String profile_image, String name, String email, String username, String user_id) {
        this.profile_image = profile_image;
        this.name = name;
        this.email = email;
        this.username = username;
        this.user_id = user_id;
    }

    public String getId() {
        return user_id;
    }

    public void setId(String id) {
        this.user_id = id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
