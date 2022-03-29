package com.stuffer.stuffers.activity.contact;

public class InviteChat {
    private String id, username, imageURL, verification, status, search, phone_number;
    boolean invite;

    public InviteChat(String id, String username, String imageURL, String verification, String status, String search, boolean invite, String phone_number) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.verification = verification;
        this.status = status;
        this.search = search;
        this.invite = invite;
        this.phone_number = phone_number;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getVerification() {
        return verification;
    }

    public String getStatus() {
        return status;
    }

    public String getSearch() {
        return search;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public boolean isInvite() {
        return invite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        InviteChat contact = (InviteChat) o;
        return getPhone_number().equals(contact.getPhone_number());

    }
    @Override
    public int hashCode() {
        return getPhone_number().hashCode();
    }
}
