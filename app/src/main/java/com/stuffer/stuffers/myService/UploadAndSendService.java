package com.stuffer.stuffers.myService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatModel.Attachment;
import com.stuffer.stuffers.commonChat.chatModel.AttachmentTypes;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.chatUtils.FirebaseUploader;


import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class UploadAndSendService extends Service {
    public static final String CHANNEL_ID = "UploadAndSendChannelAppOpay";
    private ArrayList<String> userPlayerIds;
    private Message message;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)

                .setSmallIcon(R.drawable.appopay_new_logo2)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.uploading))
                .setSound(null)
                //.setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }
//57708791-f9a1-4d04-bbca-cafe1b6588ef
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Attachment attachment = intent.getParcelableExtra("attachment");
        int type = intent.getIntExtra("attachment_type", -1);
        String attachmentFilePath = intent.getStringExtra("attachment_file_path");
        Message message = intent.getParcelableExtra("attachment_message");
        ArrayList<String> groupIds = intent.getParcelableExtra("attachment_group_ids");
        userPlayerIds = intent.getStringArrayListExtra("attachment_player_ids");
        sendPrepareMessage(type, message);
        upload(new File(attachmentFilePath), attachment, type, groupIds);
        return START_NOT_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
//https://stackoverflow.com/questions/68344001/firebase-storage-error-404-access-bucket
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void upload(final File fileToUpload,
                        final Attachment attachment, final int attachmentType,
                        final ArrayList<String> groupIds) {
        if (!fileToUpload.exists())
            return;
        final String fileName = Uri.fromFile(fileToUpload).getLastPathSegment();
        /*StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        Log.d(TAG, "UploadToFireBaseStorage: StorageRef "+mStorageReference);
        storageReference = mStorageReference.child("Photos").child(fileUri.getLastPathSegment());*/
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = mStorageReference.child(getString(R.string.app_name)).child(AttachmentTypes.getTypeName(attachmentType)).child(fileName);
        String path = storageReference.getPath();
        //Log.e("TAG", "upload: "+path );
        //final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(getString(R.string.app_name)).child(AttachmentTypes.getTypeName(attachmentType)).child(fileName);
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            //If file is already uploaded
            Attachment attachment1 = attachment;
            if (attachment1 == null) attachment1 = new Attachment();
            attachment1.setName(fileName);
            attachment1.setUrl(uri.toString());
            attachment1.setBytesCount(fileToUpload.length());
            sendMessage(attachment1, groupIds);
        }).addOnFailureListener(exception -> {
            //Elase upload and then send message
            FirebaseUploader firebaseUploader = new FirebaseUploader(new FirebaseUploader.UploadListener() {
                @Override
                public void onUploadFail(String message1) {
                    //Log.e("DatabaseException", message1);
                    Toast.makeText(UploadAndSendService.this, R.string.upload_fail, Toast.LENGTH_SHORT).show();
                    removePreparedMessage();
                    stopSelf();
                }

                @Override
                public void onUploadSuccess(String downloadUrl) {
                    Attachment attachment1 = attachment;
                    if (attachment1 == null) attachment1 = new Attachment();
                    attachment1.setName(fileToUpload.getName());
                    attachment1.setUrl(downloadUrl);
                    attachment1.setBytesCount(fileToUpload.length());
                    sendMessage(attachment1, groupIds);
                }

                @Override
                public void onUploadProgress(int progress) {

                }

                @Override
                public void onUploadCancelled() {
                    removePreparedMessage();
                    stopSelf();
                }
            }, storageReference);
            firebaseUploader.uploadOthers(getApplicationContext(), fileToUpload);
        });
    }

    private void sendMessage(Attachment attachment, ArrayList<String> groupIds) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference chatRef = firebaseDatabase.getReference(ChatHelper.REF_CHAT);
        DatabaseReference inboxRef = firebaseDatabase.getReference(ChatHelper.REF_INBOX);
        message.setAttachment(attachment);
        //message.setBody();
        //message.setDateTimeStamp(String.valueOf(System.currentTimeMillis()));
        message.setSent(true);
        //Add message in chat child
        chatRef.child(message.getChatId()).child(message.getId()).setValue(message);
        //Add message for inbox updates
        if (message.getChatId().startsWith(ChatHelper.GROUP_PREFIX) && groupIds != null) {
            for (String memberId : groupIds) {
                inboxRef.child(memberId).child(message.getChatId()).setValue(message);
            }
        } else {
            inboxRef.child(message.getSenderId()).child(message.getRecipientId()).setValue(message);
            inboxRef.child(message.getRecipientId()).child(message.getSenderId()).setValue(message);
        }

        notifyMessage(message.getSenderId(), message);

        stopSelf();
    }

    private void removePreparedMessage() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference chatRef = firebaseDatabase.getReference(ChatHelper.REF_CHAT);

        chatRef.child(message.getChatId()).child(message.getId()).setValue(null);
    }

    private void sendPrepareMessage(int type, Message message) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference chatRef = firebaseDatabase.getReference(ChatHelper.REF_CHAT);
        message.setAttachmentType(type);
        Attachment attachment = new Attachment();
        attachment.setUrl("loading");
        message.setAttachment(attachment);
        message.setDateTimeStamp(String.valueOf(System.currentTimeMillis()));
        message.setSent(false);
        //message.setBody(getStringResourceByName(AttachmentTypes.getTypeName(type).toLowerCase()));
        message.setBody(AttachmentTypes.getTypeNameDisplay(this, type));
        message.setId(chatRef.child(message.getChatId()).push().getKey());
        //Add message in chat child
        chatRef.child(message.getChatId()).child(message.getId()).setValue(message);
        this.message = message;
    }

    private void notifyMessage(String userMeId, Message message) {
        //Log.e("TAG", "notifyMessage: called in service" );
        //Log.e("TAG", "notifyMessage: player me id"+userMeId );
        //Log.e("TAG", "notifyMessage: user player id"+userPlayerIds );
        if (userPlayerIds != null && !userPlayerIds.isEmpty()) {
            try {
//                String headings = userMeId + " " + getString(R.string.new_message_sent);
                //String headings = userMeId;
                Chat chat = new Chat(message, message.getSenderId().equals(userMeId));
                String headings = chat.isGroup() ? chat.getChatName() : userMeId;
              //  String s = new Gson().toJson(new JSONObject("{'headings': {'en':'" + headings + "'}, 'contents': {'en':'" + message.getBody() + "'}, 'include_player_ids': " + userPlayerIds.toString() + ",'data': " + new Gson().toJson(message) + ",'android_group':" + message.getChatId() + " }"));
                //Log.e("TAG", "notifyMessage: "+s );

                OneSignal.postNotification(new JSONObject("{'headings': {'en':'" + headings + "'}, 'contents': {'en':'" + message.getBody() + "'}, 'include_player_ids': " + userPlayerIds.toString() + ",'data': " + new Gson().toJson(message) + ",'android_group':" + message.getChatId() + " }"),
                        new OneSignal.PostNotificationResponseHandler() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                //Log.e("OneSignalExample", "postNotification Success: " + response.toString());
                            }

                            @Override
                            public void onFailure(JSONObject response) {
                                //Log.e("OneSignalExample", "postNotification Failure: " + response.toString());
                            }
                        });

            } catch (Exception e) {
            //    e.printStackTrace();
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.up_and_send),
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

//    private String getStringResourceByName(String aString) {
//        String packageName = getPackageName();
//        int resId = getResources().getIdentifier(aString, "string", packageName);
//        return getString(resId);
//    }

}
