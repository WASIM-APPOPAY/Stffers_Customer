package com.stuffer.stuffers.commonChat.chatModel;

public class UserChat {
    String name,time,count;

    public UserChat(String name, String time, String count) {
        this.name = name;
        this.time = time;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getCount() {
        return count;
    }
}
