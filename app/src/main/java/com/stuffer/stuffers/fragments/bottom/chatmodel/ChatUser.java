package com.stuffer.stuffers.fragments.bottom.chatmodel;

import androidx.annotation.Keep;

@Keep
public class ChatUser {
    private String id, username,imageURL,verification,status,search,phone_number,area_code;


    public ChatUser(String id, String username, String imageURL, String verification, String status, String search,String phone_number,String area_code) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.verification = verification;
        this.status = status;
        this.search = search;
        this.phone_number=phone_number;
        this.area_code=area_code;

    }
    public ChatUser()
    {

    }

    public String getArea_code() {
        return area_code;
    }

    public String getPhone_number() {
        return phone_number;
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

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
