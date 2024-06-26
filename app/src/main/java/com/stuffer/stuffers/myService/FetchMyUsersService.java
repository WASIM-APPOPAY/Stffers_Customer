package com.stuffer.stuffers.myService;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Patterns;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stuffer.stuffers.commonChat.chatModel.Contact;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.utils.Helper;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FetchMyUsersService extends IntentService {
    private static String EXTRA_PARAM1 = "my_id";
    private static String EXTRA_PARAM2 = "token";
    private HashMap<String, Contact> myContacts;
    private ArrayList<User> myUsers;
    private String myId, idToken;

    public static boolean STARTED = false;

    public FetchMyUsersService() {
        super("FetchMyUsersService");
    }

    public static void startMyUsersService(Context context, String myId, String idToken) {
        Intent intent = new Intent(context, FetchMyUsersService.class);
        intent.putExtra(EXTRA_PARAM1, myId);
        intent.putExtra(EXTRA_PARAM2, idToken);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        STARTED = true;
        myId = intent.getStringExtra(EXTRA_PARAM1);
        idToken = intent.getStringExtra(EXTRA_PARAM2);
        fetchMyContacts();
        broadcastMyContacts();
        fetchMyUsers();
        broadcastMyUsers();
        STARTED = false;
    }

    private void broadcastMyUsers() {
        if (this.myUsers != null) {
            Intent intent = new Intent(ChatHelper.BROADCAST_MY_USERS);
            intent.putParcelableArrayListExtra("data", this.myUsers);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(intent);
        }
    }

    private void fetchMyUsers() {
        myUsers = new ArrayList<>();
        try {
            StringBuilder response = new StringBuilder();
            URL url = new URL(FirebaseDatabase.getInstance().getReference().toString() + "/" + ChatHelper.REF_USERS + ".json?auth=" + idToken);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line).append(" ");
            }
            rd.close();

            JSONObject responseObject = new JSONObject(response.toString());
            Gson gson = new GsonBuilder().create();
            Iterator<String> keys = responseObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject innerJObject = responseObject.getJSONObject(key);
                User user = gson.fromJson(innerJObject.toString(), User.class);
                if (User.validate(user) && !user.getId().equals(myId)) {
                    String idTrim = ChatHelper.getEndTrim(user.getId());
                    if (myContacts.containsKey(idTrim)) {
                        user.setName(myContacts.get(idTrim).getName());
                        myUsers.add(user);
                    }
                }
                if (myUsers.size() == myContacts.size()) {
                    break;
                }
            }

            Collections.sort(myUsers, (user1, user2) -> user1.getName().compareToIgnoreCase(user2.getName()));

            if (!myContacts.isEmpty()) {
                HashMap<String, Contact> tempContactData = new HashMap<>(myContacts);
                for (User user : this.myUsers) {
                    tempContactData.remove(ChatHelper.getEndTrim(user.getId()));
                }
                ArrayList<User> inviteAble = new ArrayList<>();
                for (Map.Entry<String, Contact> contactEntry : tempContactData.entrySet()) {
                    inviteAble.add(new User(contactEntry.getValue().getPhoneNumber(), contactEntry.getValue().getName()));
                }
                if (!inviteAble.isEmpty()) {
                    Collections.sort(inviteAble, (user1, user2) -> user1.getName().compareToIgnoreCase(user2.getName()));
                    inviteAble.add(0, new User("-1", "-1"));
                    this.myUsers.addAll(inviteAble);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void broadcastMyContacts() {
        new ChatHelper(this).setMyContacts(myContacts);
        Intent intent = new Intent(ChatHelper.BROADCAST_MY_CONTACTS);
        intent.putExtra("data", myContacts);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void fetchMyContacts() {
        myContacts = new HashMap<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null && !cursor.isClosed()) {
            cursor.getCount();
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasPhoneNumber == 1) {
                    @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s+", "");
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                    if (Patterns.PHONE.matcher(number).matches()) {
                        boolean hasPlus = String.valueOf(number.charAt(0)).equals("+");
                        number = number.replaceAll("[\\D]", "");
                        if (hasPlus) {
                            number = "+" + number;
                        }
                        Contact contact = new Contact(number, name);
                        String endTrim = ChatHelper.getEndTrim(contact.getPhoneNumber());
                        if (!myContacts.containsKey(endTrim))
                            myContacts.put(endTrim, contact);
                    }
                }
            }
            cursor.close();
        }
    }
}
