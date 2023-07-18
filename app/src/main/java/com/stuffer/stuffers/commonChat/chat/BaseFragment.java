package com.stuffer.stuffers.commonChat.chat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatModel.Contact;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;

import java.util.HashMap;

public class BaseFragment extends Fragment {
    protected String[] permissionsRecord = {Manifest.permission.VIBRATE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    protected String[] permissionsContact = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    protected String[] permissionsStorage = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    protected String[] permissionsStorage30 = {Manifest.permission.MANAGE_EXTERNAL_STORAGE};

    protected String[] permissionsCamera = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    protected String[] permissionsSinch = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.READ_PHONE_STATE};

    protected User userMe;
    protected ChatHelper chatHelper;
    protected Context mContext;
    private HashMap<String, Contact> savedContacts;

    protected DatabaseReference usersRef, groupRef, chatRef, inboxRef;
    //private SinchService.SinchServiceInterface mSinchServiceInterface;

    //abstract void onSinchConnected();

    //abstract void onSinchDisconnected();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        chatHelper = new ChatHelper(mContext);
        userMe = chatHelper.getLoggedInUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();//get firebase instance
        usersRef = firebaseDatabase.getReference(ChatHelper.REF_USERS);//instantiate user's firebase reference
        groupRef = firebaseDatabase.getReference(ChatHelper.REF_GROUP);//instantiate group's firebase reference
        chatRef = firebaseDatabase.getReference(ChatHelper.REF_CHAT);//instantiate chat's firebase reference
        inboxRef = firebaseDatabase.getReference(ChatHelper.REF_INBOX);//instantiate inbox's firebase reference

        //startService(new Intent(this, FirebaseChatService.class));
        //getApplicationContext().bindService(new Intent(mContext, SinchService.class), this, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();
    }

    /*@Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onSinchConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onSinchDisconnected();
        }
    }

    protected SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }*/

    protected boolean permissionsAvailable(String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        return granted;
    }

    protected void refreshMyContactsCache(HashMap<String, Contact> contactsToSet) {
        savedContacts = (contactsToSet != null) ? contactsToSet : chatHelper.getMyContacts();
    }

    protected HashMap<String, Contact> getSavedContacts() {
        if (savedContacts == null) refreshMyContactsCache(null);
        return savedContacts;
    }

    protected String getNameById(String senderId) {
        if (savedContacts == null) refreshMyContactsCache(null);
        String senderIDEndTrim = ChatHelper.getEndTrim(senderId);
        if (savedContacts.containsKey(senderIDEndTrim))
            return savedContacts.get(senderIDEndTrim).getName();
        else
            return senderId;
    }

    protected void setNotificationMessageNames(Message newMessage) {
        String[] bodySplit = newMessage.getBody().split(" ");
        if (bodySplit.length > 1) {
            String userOneIDEndTrim = ChatHelper.getEndTrim(bodySplit[0]);
            if (TextUtils.isDigitsOnly(userOneIDEndTrim)) {
                String userNameInPhone;
                if (bodySplit[0].equals(userMe.getId()))
                    userNameInPhone = getString(R.string.you);
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
                    userNameInPhone = getString(R.string.you);
                else
                    userNameInPhone = getNameById(userTwoIDEndTrim);
                if (userNameInPhone.equals(userTwoIDEndTrim))
                    userNameInPhone = bodySplit[bodySplit.length - 1];
                newMessage.setBody(newMessage.getBody().substring(0, newMessage.getBody().indexOf(bodySplit[bodySplit.length - 1])) + userNameInPhone);
            }
        }
    }
}
