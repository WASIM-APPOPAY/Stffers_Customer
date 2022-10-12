package com.stuffer.stuffers.commonChat.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatAdapters.MenuUsersRecyclerAdapter;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.Contact;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.chatUtils.Constants;
import com.stuffer.stuffers.commonChat.interfaces.ChatItemClickListener;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;


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
@Keep
public class InviteActivity extends AppCompatActivity implements ChatItemClickListener {
    private static String EXTRA_PARAM1 = "my_id";
    private static final int REQUEST_CODE_CHAT_FORWARD = 99;
    private static String EXTRA_PARAM2 = "token";
    private HashMap<String, Contact> myContacts;
    private ArrayList<User> myUsers;
    private String myId, idToken;
    private MenuUsersRecyclerAdapter menuUsersRecyclerAdapter;
    private Context mContext;
    private RecyclerView menuRecyclerView;
    private ArrayList<Message> messageForwardList = new ArrayList<>();
    private MyEditText searchContact;
    private ImageView ivBackInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        searchContact = findViewById(R.id.searchContact);
        ivBackInvite = findViewById(R.id.ivBackInvite);
        mContext = this;
        myId = getIntent().getStringExtra(EXTRA_PARAM1);
        idToken = getIntent().getStringExtra(EXTRA_PARAM2);
        menuRecyclerView = findViewById(R.id.menu_recycler_view);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        searchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                menuUsersRecyclerAdapter.getFilter().filter(editable.toString());
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Helper.isPhonePermissionGranted(InviteActivity.this)) {
                requestPermission();
            } else {
                fetchMyContacts();
            }
        } else {
            fetchMyContacts();
        }
        ivBackInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public static void startInvite(Context context, String myId, String idToken) {
        Intent intent = new Intent(context, InviteActivity.class);
        intent.putExtra(EXTRA_PARAM1, myId);
        intent.putExtra(EXTRA_PARAM2, idToken);
        context.startActivity(intent);
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
                    String idTrim = Helper.getEndTrim(user.getId());
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
                    tempContactData.remove(Helper.getEndTrim(user.getId()));
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

                menuUsersRecyclerAdapter = new MenuUsersRecyclerAdapter(mContext, myUsers);
                menuRecyclerView.setAdapter(menuUsersRecyclerAdapter);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, Constants.PHONE_REQUEST_CODE);
        } else {
            fetchMyContacts();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PHONE_REQUEST_CODE) {
            if (Helper.isPhonePermissionGranted(InviteActivity.this)) {
                fetchMyContacts();
            } else {
                boolean setting = false;
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRational = shouldShowRequestPermissionRationale(permission);
                        if (!showRational) {
///                            Log.e(TAG, "onRequestPermissionsResult: true : " + permission);
                            setting = true;
                            break;
                        }
                    }
                }
            }
        }
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
                        String endTrim = Helper.getEndTrim(contact.getPhoneNumber());
                        if (!myContacts.containsKey(endTrim))
                            myContacts.put(endTrim, contact);
                    }
                }
            }
            cursor.close();
            broadcastMyContacts();
            fetchMyUsers();
        }

    }

    private void broadcastMyContacts() {
        new ChatHelper(this).setMyContacts(myContacts);
    }

    @Override
    public void onChatItemClick(Chat chat, int position, View userImage) {

        openChat(ChatActivity.newIntent(mContext, messageForwardList, chat), userImage);

    }

    private void openChat(Intent intent, View userImage) {


        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, userImage, "userImage");
        //startActivityForResult(intent, REQUEST_CODE_CHAT_FORWARD, activityOptionsCompat.toBundle());
        startActivity(intent, activityOptionsCompat.toBundle());

//        if (Build.VERSION.SDK_INT > 21) {
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, userImage, "userImage");
//            startActivityForResult(intent, REQUEST_CODE_CHAT_FORWARD, options.toBundle());
//        } else {
//            startActivityForResult(intent, REQUEST_CODE_CHAT_FORWARD);
//            overridePendingTransition(0, 0);
//        }

        /*if (userSelectDialogFragment != null)
            userSelectDialogFragment.dismiss();*/
    }

    @Override
    public void placeCall(boolean callIsVideo, User user) {

    }
}