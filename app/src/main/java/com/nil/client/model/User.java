package com.nil.client.model;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String username;
    private String gender;
    private String status;
    private String imageURL;

    public User(String id, String username, String status, String imageURL, String gender) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.imageURL = imageURL;
        this.gender = gender;
    }

    public User() {
        this.imageURL = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
