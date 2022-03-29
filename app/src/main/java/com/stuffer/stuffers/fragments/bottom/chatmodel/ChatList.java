package com.stuffer.stuffers.fragments.bottom.chatmodel;

import androidx.annotation.Keep;

@Keep
public class ChatList {
    public String id;

    public ChatList(String id) {
        this.id = id;
    }

    public ChatList(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
