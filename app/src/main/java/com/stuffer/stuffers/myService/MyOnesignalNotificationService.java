package com.stuffer.stuffers.myService;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.onesignal.OSMutableNotification;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;
import com.stuffer.stuffers.commonChat.chatModel.Contact;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;


import org.json.JSONObject;

import java.math.BigInteger;
import java.util.HashMap;

/*public class MyOnesignalNotificationService extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        // Read properties from result.
        Log.d("OSNotificationReceived1", notification.payload.title);
        Log.d("OSNotificationReceived2", notification.payload.body);
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = builder -> {
            if (notification.payload.title != null) {
                String notificationSenderIDEndTrim = Helper.getEndTrim(notification.payload.title);
                HashMap<String, Contact> savedContacts = new Helper(this).getMyContacts();
                if (savedContacts.containsKey(notificationSenderIDEndTrim)) {
                    return builder.setContentTitle(savedContacts.get(notificationSenderIDEndTrim).getName());
                }
            }

            return builder;
        };

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);
        // Return true to stop the notification from displaying.
        return false;
    }
}*/

public class MyOnesignalNotificationService implements OneSignal.OSRemoteNotificationReceivedHandler {

    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent notificationReceivedEvent) {
        OSNotification notification = notificationReceivedEvent.getNotification();
        JSONObject additionalData = notification.getAdditionalData();
        Log.e("TAG", "remoteNotificationReceived: "+additionalData );

        // Example of modifying the notification's accent color
        OSMutableNotification mutableNotification = notification.mutableCopy();
        mutableNotification.setExtender(builder -> {
            // Sets the accent color to Green on Android 5+ devices.
            // Accent color controls icon and action buttons on Android 5+. Accent color does not change app title on Android 10+
            builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            // Sets the notification Title to Red
            Spannable spannableTitle = new SpannableString(notification.getTitle());
            spannableTitle.setSpan(new ForegroundColorSpan(Color.RED),0,notification.getTitle().length(),0);
            String notificationSenderIDEndTrim = ChatHelper.getEndTrim(notification.getTitle());
            HashMap<String, Contact> savedContacts = new ChatHelper(context).getMyContacts();
            if (savedContacts.containsKey(notificationSenderIDEndTrim)) {
                builder.setContentTitle(savedContacts.get(notificationSenderIDEndTrim).getName());
            }else {
                builder.setContentTitle(spannableTitle);
            }

            // Sets the notification Body to Blue
            Spannable spannableBody = new SpannableString(notification.getBody());
            spannableBody.setSpan(new ForegroundColorSpan(Color.BLUE),0,notification.getBody().length(),0);
            builder.setContentText(spannableBody);
            //Force remove push from Notification Center after 30 seconds
            builder.setTimeoutAfter(30000);
            return builder;
        });
        JSONObject data = notification.getAdditionalData();
        Log.i("OneSignalExample", "Received Notification Data: " + data);

        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // To omit displaying a notification, pass `null` to complete()
        notificationReceivedEvent.complete(mutableNotification);
    }
}

