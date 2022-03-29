package com.stuffer.stuffers.activity.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class FindFriendActivity extends AppCompatActivity {
    private static final String TAG = "FindFriendActivity";
    ArrayList<UserObject> userList, contactList;
    private ProgressDialog dialog;
    private HashMap<String, UserObject> myContacts;
    RecyclerView rvContacts;
    private boolean allow = false;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        rvContacts = findViewById(R.id.rvContacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(FindFriendActivity.this));
        contactList = new ArrayList<>();
        userList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Helper.isPhonePermissionGranted(FindFriendActivity.this)) {
                requestPermission();
            } else {
                fetchMyContacts();
            }
        } else {
            fetchMyContacts();
        }

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, AppoConstants.PHONE_REQUEST_CODE);
        } else {
            fetchMyContacts();
        }
    }

    private void fetchMyContacts() {


        myContacts = new HashMap<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null && !cursor.isClosed()) {
            cursor.getCount();
            while (cursor.moveToNext()) {
                int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasPhoneNumber == 1) {
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s+", "");
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                    if (Patterns.PHONE.matcher(number).matches()) {
                        boolean hasPlus = String.valueOf(number.charAt(0)).equals("+");
                        number = number.replaceAll("[\\D]", "");
                        if (hasPlus) {
                            number = "+" + number;
                        }
                        UserObject contact = new UserObject(name, number, "");
                        String endTrim = Helper.getEndTrim(contact.getPhone());
                        if (!myContacts.containsKey(endTrim))
                            myContacts.put(endTrim, contact);


                    }
                }
            }
            cursor.close();
        }


        refreshMyContactsCache(myContacts);
        // dialog.dismiss();
    }


    protected void refreshMyContactsCache(HashMap<String, UserObject> contactsToSet) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("please wait..");
        dialog.show();
    ////Log.e(TAG, "refreshMyContactsCache: Name : " + contact.getName() + "  Ph : " + contact.getPhoneNumber());
        contactList.addAll(contactsToSet.values());
        Collections.sort(contactList, new Comparator<UserObject>() {
            @Override
            public int compare(UserObject contact1, UserObject contact2) {
                String name1 = contact1.getName().toLowerCase();
                String name2 = contact2.getName().toLowerCase();
                return name1.compareTo(name2);
            }
        });



        for (int i = 0; i < contactList.size(); i++) {
           //counter = counter + 1;
            if (i + 1 >= contactList.size())
                allow = true;

            getUserDetails(contactList.get(i));
        }
        //Log.e(TAG, "refreshMyContactsCache: ==================================================");

        dialog.dismiss();



        /*mAdapter = new ContactDemoActivity.MyContactAdapter(ContactDemoActivity.this, mListContact);
        rvContacts.setAdapter(mAdapter);*/


    }

    private void getUserDetails(final UserObject mContact) {
        String param = null;
        if (String.valueOf(mContact.getPhone().charAt(0)).equalsIgnoreCase("+")) {
            param = mContact.getPhone().substring(1);
        } else {
            param = mContact.getPhone();
        }
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = mUserDB.orderByChild("phone_number").equalTo(param);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Log.e(TAG, "onDataChange: called");
                    String phone = "", name = "";
                    for (DataSnapshot childSnapshots : dataSnapshot.getChildren()) {
                        if (childSnapshots.child("phone_number").getValue() != null) {
                            phone = childSnapshots.child("phone_number").getValue().toString();
                        }
                        if (childSnapshots.child("username").getValue() != null) {
                            name = childSnapshots.child("username").getValue().toString() + "\n Hey there i am using Appopay";
                        }

                        UserObject mUser = new UserObject(name, phone, childSnapshots.getKey());
                        userList.add(mUser);
                        //Log.e(TAG, "onDataChange: Name " + name);
                        if (allow) {
                            updateRecyclerView2();

                        }
                    }

                } else {
                    /*if (counter == contactList.size()) {
                        //Log.e(TAG, "onDataChange: else called");
                        updateRecyclerView();
                    }*/
                    if (allow){
                        updateRecyclerView2();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.e(TAG, "onCancelled: error occured");
            }
        });

    }

    private void updateRecyclerView2() {
        ArrayList<UserObject> tempList;

        tempList = contactList;
        String param;
        for (int i = 0; i < userList.size(); i++) {
            UserObject mContact = userList.get(i);
            if (String.valueOf(mContact.getPhone().charAt(0)).equalsIgnoreCase("+")) {
                param = mContact.getPhone().substring(1);
            } else {
                param = mContact.getPhone();
            }
            for (int j = 0; j < contactList.size(); j++) {
                if (contactList.get(j).getPhone().equals(param)) {
                    tempList.remove(j);
                }
            }
        }
        contactList = tempList;
        //contactList.addAll(userList);
        for (int i = 0; i < userList.size(); i++) {
            //Log.e(TAG, "updateRecyclerView2: output :: " + contactList.get(i).getName());
            contactList.add(i, userList.get(i));

        }


        MyContactAdapter adapter = new MyContactAdapter(FindFriendActivity.this, contactList);
        rvContacts.setAdapter(adapter);


    }

    private void updateRecyclerView() {
        MyContactAdapter adapter = new MyContactAdapter(FindFriendActivity.this, contactList);
        rvContacts.setAdapter(adapter);

    }

    public class MyContactAdapter extends RecyclerView.Adapter<MyContactAdapter.MyContactHolder> {
        private Context mContext;
        private ArrayList<UserObject> mListContact;

        public MyContactAdapter(Context mContext, ArrayList<UserObject> mListContact) {
            this.mContext = mContext;
            this.mListContact = mListContact;
        }

        @NonNull
        @Override
        public MyContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_items_invites, parent, false);
            return new MyContactHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyContactHolder holder, int position) {
            holder.makeData();
        }

        @Override
        public int getItemCount() {
            return mListContact.size();
        }

        public class MyContactHolder extends RecyclerView.ViewHolder {
            MyTextView tvFirstLetter, tvFullName, tvMobileNumber,tvInvite;

            public MyContactHolder(@NonNull View itemView) {
                super(itemView);
                tvFirstLetter = itemView.findViewById(R.id.tvFirstLetter);
                tvFullName = itemView.findViewById(R.id.tvFullName);
                tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber);
                tvInvite = itemView.findViewById(R.id.btnInvite);
            }

            void makeData() {
                tvFirstLetter.setText(String.valueOf(mListContact.get(getAdapterPosition()).getName().charAt(0)));
                tvFullName.setText(mListContact.get(getAdapterPosition()).getName());
                if (mListContact.get(getAdapterPosition()).getName().contains("Hey there i am")) {
                    tvInvite.setVisibility(View.GONE);
                    tvMobileNumber.setVisibility(View.GONE);
                } else {
                    tvInvite.setVisibility(View.VISIBLE);
                    tvMobileNumber.setVisibility(View.VISIBLE);
                }
                tvMobileNumber.setText(mListContact.get(getAdapterPosition()).getPhone());
            }
        }
    }

    /*private void updateRecyclerView2() {
        ArrayList<UserObject> tempList = contactList;
        String param;
        for (int i = 0; i < userList.size(); i++) {
            UserObject mContact = userList.get(i);
            if (String.valueOf(mContact.getPhone().charAt(0)).equalsIgnoreCase("+")) {
                param = mContact.getPhone().substring(1);
            } else {
                param = mContact.getPhone();
            }
            for (int j = 0; j < contactList.size(); j++) {
                if (contactList.get(j).getPhone().equals(param)) {
                    tempList.remove(j);
                }
            }
        }
        contactList = tempList;

        contactList.addAll(userList);

        MyContactAdapter adapter = new MyContactAdapter(FindFriendActivity.this,contactList);
        rvContacts.setAdapter(adapter);


    }*/

}
