package com.stuffer.stuffers.myService;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.HomeActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.commonChat.chat.ChatActivity;
import com.stuffer.stuffers.commonChat.chatModel.AttachmentTypes;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.Contact;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOnesignalNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {
    private final Context context;
    private HashMap<String, Contact> savedContacts;

    public MyOnesignalNotificationOpenedHandler(Context context) {
        this.context = context;
    }

    /*@Override
    public void notificationOpened(OSNotificationOpenResult result) {
        Helper.DISABLE_SPLASH_HANDLER = true;
        JSONObject data = result.notification.payload.additionalData;
        Log.d("notificationOpened", data.toString());
        Helper helper = new Helper(context);
        if (helper.isLoggedIn()) {
            Chat newChat = null;
            savedContacts = helper.getMyContacts();
            User userMe = helper.getLoggedInUser();
            Gson gson = new Gson();
            Message message = gson.fromJson(data.toString(), new TypeToken<Message>() {
            }.getType());
            if (message != null && message.getId() != null && message.getChatId() != null) {
                if (message.getAttachmentType() == AttachmentTypes.NONE_NOTIFICATION)
                    setNotificationMessageNames(message, userMe);
                newChat = new Chat(message, message.getSenderId().equals(userMe.getId()));
                if (!newChat.isGroup()) newChat.setChatName(getNameById(newChat.getUserId()));
            }

            if (newChat != null) {
                Intent intent = ChatActivity.newIntent(context, new ArrayList<>(), newChat);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                context.startActivity(new Intent(context, HomeActivity.class));
            }
        } else {
            context.startActivity(new Intent(context, SignInActivity.class));
        }
    }*/

    private void setNotificationMessageNames(Message newMessage, User userMe) {
        String[] bodySplit = newMessage.getBody().split(" ");
        if (bodySplit.length > 1) {
            String userOneIDEndTrim = ChatHelper.getEndTrim(bodySplit[0]);
            if (TextUtils.isDigitsOnly(userOneIDEndTrim)) {
                String userNameInPhone;
                if (bodySplit[0].equals(userMe.getId()))
                    userNameInPhone = context.getString(R.string.you);
                else
                    userNameInPhone = getNameById(userOneIDEndTrim);
                if (userNameInPhone.equals(userOneIDEndTrim))
                    userNameInPhone = bodySplit[0];
                newMessage.setBody(userNameInPhone + newMessage.getBody().substring(bodySplit[0].length()));
            }
            String userTwoIDEndTrim = ChatHelper.getEndTrim(bodySplit[bodySplit.length - 1]);
            if (TextUtils.isDigitsOnly(userTwoIDEndTrim)) {
                String userNameInPhone;
                if (bodySplit[bodySplit.length - 1].equals(userMe.getId()))
                    userNameInPhone = context.getString(R.string.you);
                else
                    userNameInPhone = getNameById(userTwoIDEndTrim);
                if (userNameInPhone.equals(userTwoIDEndTrim))
                    userNameInPhone = bodySplit[bodySplit.length - 1];
                newMessage.setBody(newMessage.getBody().substring(0, newMessage.getBody().indexOf(bodySplit[bodySplit.length - 1])) + userNameInPhone);
            }
        }
    }

    private String getNameById(String senderId) {
        String senderIDEndTrim = ChatHelper.getEndTrim(senderId);
        if (savedContacts.containsKey(senderIDEndTrim))
            return savedContacts.get(senderIDEndTrim).getName();
        else
            return senderId;
    }

    @Override
    public void notificationOpened(OSNotificationOpenedResult result) {
        ChatHelper.DISABLE_SPLASH_HANDLER = true;
        //JSONObject data = result.notification.payload.additionalData;
        OSNotification notification = result.getNotification();
        JSONObject data = notification.getAdditionalData();

        //JSONObject data = result.getNotification().getRawPayload(
        Log.d("notificationOpened", data.toString());
        ChatHelper helper = new ChatHelper(context);
        if (helper.isLoggedIn()) {
            Chat newChat = null;
            savedContacts = helper.getMyContacts();
            User userMe = helper.getLoggedInUser();
            Gson gson = new Gson();
            Message message = gson.fromJson(data.toString(), new TypeToken<Message>() {
            }.getType());
            if (message != null && message.getId() != null && message.getChatId() != null) {
                if (message.getAttachmentType() == AttachmentTypes.NONE_NOTIFICATION)
                    setNotificationMessageNames(message, userMe);
                newChat = new Chat(message, message.getSenderId().equals(userMe.getId()));
                if (!newChat.isGroup()) newChat.setChatName(getNameById(newChat.getUserId()));
            }

            if (newChat != null) {
                Intent intent = ChatActivity.newIntent(context, new ArrayList<>(), newChat,"");
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                context.startActivity(new Intent(context, HomeActivity.class));
            }
        } else {
            context.startActivity(new Intent(context, SignInActivity.class));
        }
    }
}
