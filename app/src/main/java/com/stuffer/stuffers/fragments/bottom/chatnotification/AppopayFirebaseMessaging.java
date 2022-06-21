package com.stuffer.stuffers.fragments.bottom.chatnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.AccountActivity;
import com.stuffer.stuffers.activity.wallet.CustomerProfileActivity;
import com.stuffer.stuffers.fragments.bottom.chat.MessageActivity;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

/*import static com.paypal.android.sdk.cy.j;*/


public class AppopayFirebaseMessaging extends FirebaseMessagingService {
    private static final String TAG = "AppopayFirebaseMessagin";

    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Log.e(TAG, "onNewToken: Param Token :: " + newToken);
        DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveFirebaseToken(newToken);
        if (firebaseUser != null) {
            updateToken(newToken);
        }
    }

    public void onTokenRefresh() {

    }


    // [END refresh_token]


    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshToken);
        reference.child(firebaseUser.getUid()).setValue(token);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Log.e(TAG, "onMessageReceived: " + new Gson().toJson(remoteMessage));
        String sented = remoteMessage.getData().get("sented");
        showNotification(remoteMessage);
       /*FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && sented.equals(firebaseUser.getUid())) {
            showNotification(remoteMessage);
        }*/
    }

    public void showNotification(RemoteMessage remoteMessage) {
        //Log.e(TAG, "showNotification: called" );
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
            }
        });
        String user = remoteMessage.getData().get("user");
        String mBody = remoteMessage.getData().get("body");

        if ("Gift Card".equalsIgnoreCase(user)) {
            int icon = Integer.parseInt(remoteMessage.getData().get("icon"));
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
            Intent intent = new Intent(this, AccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putString("userid", user);
            intent.putExtras(bundle);
            int j = 0;
            if (icon == 0) {
                mNotificationManager.showSmallNotification1(title, body, intent, j);
            } else {
                mNotificationManager.showBigNotification2(title, body, intent);
            }

            /***************************************************/
        } else if ("Wallet_Transfer".equals(user)) {
            int icon = Integer.parseInt(remoteMessage.getData().get("icon"));
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
            Intent intent = new Intent(this, AccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putString("userid", user);
            intent.putExtras(bundle);
            int j = 0;
            if (icon == 0) {
                mNotificationManager.showSmallNotification1(title, body, intent, j);
            } else {
                mNotificationManager.showBigNotificationE_Wallet(title, body, intent);
            }
        } else if ("topup".equals(user)) {
            int icon = Integer.parseInt(remoteMessage.getData().get("icon"));
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
            Intent intent = new Intent(this, AccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putString("userid", user);
            intent.putExtras(bundle);
            int j = 0;
            if (icon == 0) {
                mNotificationManager.showSmallNotification1(title, body, intent, j);
            } else {
                mNotificationManager.showBigNotificationE_Recharge(title, body, intent);
            }
        } else if ("New Message".equals(mBody)) {
            int icon = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("icon")));
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
            Intent intent = new Intent(this, MessageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putString("userid", user);
            intent.putExtras(bundle);
            assert user != null;
            int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
            if (icon == 0) {
                mNotificationManager.showSmallNotification(title, body, intent, j);
            } else {
                String[] params = body.split(" ");
                mNotificationManager.showBigNotification(title, params[0], params[1], intent);
            }

        } else {
            int icon;
            if (remoteMessage.getData().get("icon") == null) {
                icon = 0;
            } else {
                icon = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("icon")));
            }

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
            //Intent intent = new Intent(this, NotificationReceiver.class);
            Intent intent = new Intent(this, CustomerProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putString("discount", body);
            intent.putExtras(bundle);
            int j = 0;
            if (user != null) {
                j = Integer.parseInt(user.replaceAll("[\\D]", ""));
            } else {
                j = j + 2;
            }
            if (icon == 0) {
                mNotificationManager.showSmallNotificationDiscount(title, body, intent, j);
            } else {
                String[] params = body.split(" ");
                mNotificationManager.showBigNotification(title, params[0], params[1], intent);
            }
        }


    }





    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel(NotificationManager notificationManager) {
        String name = "notification";
        String description = "Notifications from appopay";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel mChannel = new NotificationChannel("notification", name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);
    }


}
