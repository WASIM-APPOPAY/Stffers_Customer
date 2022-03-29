package com.stuffer.stuffers.fragments.bottom.chatnotification;

public class NotificationData {

    private String type;
    private int icon;
    private String title;
    private String body;
    private String receiverIds;

    public NotificationData() {

    }

    public NotificationData(String type, int icon, String title, String body, String receiverIds) {
        this.type = type;
        this.icon = icon;
        this.title = title;
        this.body = body;
        this.receiverIds = receiverIds;
    }


}
