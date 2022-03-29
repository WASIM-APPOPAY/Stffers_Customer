package com.stuffer.stuffers.activity.contact;

public class UserObject {
    private String name,phone,uid;

    public UserObject(String name, String phone, String uid) {
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }
}
