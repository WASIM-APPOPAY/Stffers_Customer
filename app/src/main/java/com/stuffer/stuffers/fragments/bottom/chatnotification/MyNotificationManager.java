package com.stuffer.stuffers.fragments.bottom.chatnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.utils.AppoConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyNotificationManager {
    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;
    private Context mCtx;

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void showSmallNotification(String title, String message, Intent intent, int j) {
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx, j, intent, PendingIntent.FLAG_ONE_SHOT);
        //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, "notification");
        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.ic_appopay_notification2).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(Html.fromHtml(title).toString())
                .setContentText(Html.fromHtml(message).toString())
                /*.setSmallIcon(R.mipmap.ic_launcher)*/
                .setColor(Color.parseColor("#15D4ED"))
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(message).toString()))
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);
        }
        int i = 0;
        if (j > 0) {
            i = j;
        }
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

    public void showSmallNotificationDiscount(String title, String message, Intent intent, int j) {
        //PendingIntent resultPendingIntent = PendingIntent.getBroadcast(mCtx, j, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx, j, intent, PendingIntent.FLAG_ONE_SHOT);
        //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, "notification");
        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.ic_appopay_notification2).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(Html.fromHtml(title).toString())
                .setContentText(Html.fromHtml(message).toString())
                /*.setSmallIcon(R.mipmap.ic_launcher)*/
                .setColor(Color.parseColor("#15D4ED"))
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(message).toString()))
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);
        }
        int i = 0;
        if (j > 0) {
            i = j;
        }
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

    public void showBigNotification(String title, String message, String url, Intent intent) {
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx, ID_BIG_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(Html.fromHtml(title).toString());
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, "notification");
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(Html.fromHtml(title).toString())
                .setContentText(Html.fromHtml(message).toString())
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                /*.setDefaults(Notification.DEFAULT_SOUND)*/
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);
        }
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel(NotificationManager notificationManager) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String name = "notification";
        String description = "Notifications for appopay message";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel("notification", name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        //mChannel.setSound(soundUri, audioAttributes);

        notificationManager.createNotificationChannel(mChannel);
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showSmallNotification1(String title, String message, Intent intent, int j) {
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx, j, intent, PendingIntent.FLAG_ONE_SHOT);
        //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, "notification");
        Notification notification;

        notification = mBuilder.setSmallIcon(R.drawable.ic_appopay_notification2).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                //.setContentIntent(resultPendingIntent)
                .setContentTitle(Html.fromHtml(title).toString())
                .setContentText(Html.fromHtml(message))
                /*.setSmallIcon(R.mipmap.ic_launcher)*/
                .setColor(Color.parseColor("#15D4ED"))
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(message).toString()))
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);
        }
        int i = 0;
        if (j > 0) {
            i = j;
        }
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

    /*public void create(){
        Intent notificationIntent;

        long when = System.currentTimeMillis();
        int id = (int) System.currentTimeMillis();

        Bitmap bitmap = getBitmapFromURL(image_url);
        NotificationCompat.BigPictureStyle notifystyle = new NotificationCompat.BigPictureStyle();
        notifystyle.bigPicture(bitmap);
        RemoteViews contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.layout_custom_notification);
        //contentView.setImageViewBitmap(R.id.image, bitmap);
        contentView.setTextViewText(R.id.title, body);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setStyle(notifystyle)
                .setCustomBigContentView(contentView)
                .setContentText(body);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationIntent = new Intent(context, SinglePost.class);//Sritapana189
        notificationIntent.putExtra("single_id",single_id);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, id, notificationIntent, 0); //Modified

        Notification notification = mBuilder.build();
        notification.contentIntent = contentIntent;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        mNotificationManager.notify(notificationsId, notification);
    }*/

    public void showBigNotification1(String title, String message, Intent intent) {
        //Log.e("TAG", "showBigNotification1: called");
        //Log.e("TAG", "showBigNotification1: json message :: " + message);
        String mAmount = "";
        String mMessage = "";
        String mFromCurrency = "";
        String mSenderName = "";
        String mGiftWalletNumber = "";
        try {
            JSONObject paramReceived = new JSONObject(message);
            mAmount = paramReceived.getString(AppoConstants.AMOUNT);
            mMessage = paramReceived.getString(AppoConstants.MESSAGE);
            mFromCurrency = paramReceived.getString(AppoConstants.FROMCURRENCY);
            mSenderName = paramReceived.getString(AppoConstants.SENDERNAME);
            mGiftWalletNumber = paramReceived.getString(AppoConstants.GIFTCARD_WALLET_NUMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx, ID_BIG_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setSummaryText(mMessage);

        RemoteViews contentView = new RemoteViews(AppoPayApplication.getInstance().getPackageName(), R.layout.layout_custom_notification);
        contentView.setImageViewResource(R.id.imageviewj, R.drawable.user_card);
        contentView.setTextViewText(R.id.title, "Appopay Gift Card");
        contentView.setTextViewText(R.id.tvAccountNos, mGiftWalletNumber);
        contentView.setTextViewText(R.id.tvFullName, mSenderName);
        contentView.setTextViewText(R.id.tvFromCurrency, mFromCurrency);
        contentView.setTextViewText(R.id.tvGiftAmount, mAmount);
        // contentView.setTextViewText(R.id.tvGiftMessage, mMessage);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, "notification");
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                //.setContentIntent(resultPendingIntent)
                //.setContentTitle(Html.fromHtml(title).toString())
                .setContentText(Html.fromHtml(message).toString())
                .setStyle(bigPictureStyle)
                //.setSmallIcon(R.mipmap.ic_launcher)
                .setCustomBigContentView(contentView)
                //.setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);
        }
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    public void showBigNotification2(String title, String message, Intent intent) {
        String mAmount = "";
        String mMessage = "";
        String mFromCurrency = "";
        String mSenderName = "";
        String mGiftWalletNumber = "";
        try {
            JSONObject paramReceived = new JSONObject(message);
            mAmount = paramReceived.getString(AppoConstants.AMOUNT);
            mMessage = paramReceived.getString(AppoConstants.MESSAGE);
            mFromCurrency = paramReceived.getString(AppoConstants.FROMCURRENCY);
            mSenderName = paramReceived.getString(AppoConstants.SENDERNAME);
            mGiftWalletNumber = paramReceived.getString(AppoConstants.GIFTCARD_WALLET_NUMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx, ID_BIG_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews bigRemoteView = new RemoteViews(AppoPayApplication.getInstance().getPackageName(), R.layout.layout_custom_notification);
        bigRemoteView.setImageViewResource(R.id.imageviewj, R.drawable.user_card);
        bigRemoteView.setTextViewText(R.id.title, "Appopay Gift Card");
        bigRemoteView.setTextViewText(R.id.tvAccountNos, mGiftWalletNumber);
        bigRemoteView.setTextViewText(R.id.tvFullName, mSenderName);
        bigRemoteView.setTextViewText(R.id.tvFromCurrency, mFromCurrency);
        bigRemoteView.setTextViewText(R.id.tvGiftAmount, mAmount);

        RemoteViews remoteViews = createRemoteViews1(mCtx, R.layout.notification_custom_content,
                R.drawable.appopay_gift_card, "Appopay Gift Card",
                mMessage, 0);

        NotificationCompat.Builder builder = createCustomNotificationBuilder(mCtx);
        builder.setCustomContentView(remoteViews)
                .setAutoCancel(true)
                .setCustomBigContentView(bigRemoteView)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        showNotification(mCtx, builder.build(), 0);


    }

    private RemoteViews createRemoteViews1(Context context, int layout, int iconResource,
                                           String title, String message, int imageResource) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);
        remoteViews.setTextViewText(R.id.text_title, title);
        remoteViews.setTextViewText(R.id.text_message, message);

        return remoteViews;
    }

    public NotificationCompat.Builder createCustomNotificationBuilder(Context context) {
        return new NotificationCompat.Builder(context, "notification")
                .setSmallIcon(R.drawable.ic_notification_bell)
                .setAutoCancel(true);
    }

    private void showNotification(Context context, Notification notification, int id) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(mNotificationManager);
        }
        mNotificationManager.notify(id, notification);
    }

    public void showBigNotificationE_Wallet(String title, String message, Intent intent) {
        String mMessage = "";
        try {
            JSONObject paramReceived = new JSONObject(message);
            mMessage = paramReceived.getString(AppoConstants.MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RemoteViews remoteViews = createRemoteViews1(mCtx, R.layout.notification_custom_content,
                R.drawable.appopay_gift_card, "E Wallet Transfer",
                mMessage, 0);

        NotificationCompat.Builder builder = createCustomNotificationBuilder(mCtx);
        builder.setCustomContentView(remoteViews)
                .setAutoCancel(true)
                //.setCustomBigContentView(bigRemoteView)
                //.setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        showNotification(mCtx, builder.build(), 0);
    }

    public void showBigNotificationE_Recharge(String title, String message, Intent intent) {
        String mMessage = "";
        try {
            JSONObject paramReceived = new JSONObject(message);
            mMessage = paramReceived.getString(AppoConstants.MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RemoteViews remoteViews = createRemoteViews1(mCtx, R.layout.notification_custom_content,
                R.drawable.appopay_gift_card, "Topup Airtime",
                mMessage, 0);

        NotificationCompat.Builder builder = createCustomNotificationBuilder(mCtx);
        builder.setCustomContentView(remoteViews)
                .setAutoCancel(true)
                //.setCustomBigContentView(bigRemoteView)
                //.setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        showNotification(mCtx, builder.build(), 0);
    }

}
