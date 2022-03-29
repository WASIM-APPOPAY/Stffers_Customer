package com.stuffer.stuffers.activity.contact;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.fragments.bottom.chat.MessageActivity;
import com.stuffer.stuffers.fragments.bottom.chatmodel.ChatUser;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InviteContactActivity extends AppCompatActivity {
    RecyclerView rvContacts;
    private List<ChatUser> mUser;
    private static final String TAG = "InviteContactActivity";
    private HashMap<String, Contact> myContacts;
    private ProgressDialog dialog;
    private ArrayList<InviteChat> myUsers;
    private String myUID;
    private MyEditText edSearch;
    private MyInvitationAdapter mAdapter;
    ImageView ivBackInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_contact);
        rvContacts = findViewById(R.id.rvContacts);
        edSearch = findViewById(R.id.edSearch);
        ivBackInvite = findViewById(R.id.ivBackInvite);
        mUser = new ArrayList<>();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            getAllChatUser();
        } else {
            processReadContact();
        }

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable charSequence) {
                mAdapter.getFilter().filter(charSequence);
            }
        });
        ivBackInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Helper.hideKeyboard(edSearch, InviteContactActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });
    }

    private void getAllChatUser() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myUID = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatUser chatitUser = snapshot.getValue(ChatUser.class);
                        if (chatitUser != null && firebaseUser != null && !chatitUser.getId().equals(firebaseUser.getUid())) {
                            mUser.add(chatitUser);
                        }
                    }
                    processReadContact();
                }catch (Exception e){
                    e.printStackTrace();
                }

///                Log.e(TAG, "onDataChange: length :: " + mUser.size());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void processReadContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Helper.isPhonePermissionGranted(InviteContactActivity.this)) {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppoConstants.PHONE_REQUEST_CODE) {
            if (Helper.isPhonePermissionGranted(InviteContactActivity.this)) {
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
        dialog = new ProgressDialog(this);
        dialog.setMessage("please wait..");
        dialog.show();

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
                        Contact contact = new Contact(number, name);

                        String endTrim = Helper.getEndTrim(contact.getPhoneNumber());
                        ///// Log.e(TAG, "fetchMyContacts: Phone No :: " + number + "  Name :: " + name + "  EndTrim :: " + endTrim);
                        if (!myContacts.containsKey(endTrim))
                            myContacts.put(endTrim, contact);
                    }
                }
            }
            cursor.close();
        }


        refreshMyContactsCache(myContacts);
        dialog.dismiss();
    }

    protected void refreshMyContactsCache(HashMap<String, Contact> contactsToSet) {


        /**
         * Todo find the your own user in firebase DataBase
         * Based on your phone contact
         */
        myUsers = new ArrayList<>();
        for (ChatUser chatUser : mUser) {
            String idTrim = Helper.getEndTrim(chatUser.getPhone_number());
            if (myContacts.containsKey(idTrim) && !myUID.equals(chatUser.getId())) {
                InviteChat inviteChat = new InviteChat(
                        chatUser.getId(),
                        chatUser.getUsername(), "default", "verified",
                        chatUser.getStatus(), chatUser.getUsername().toLowerCase(), false, chatUser.getPhone_number());
                myUsers.add(inviteChat);
            }
            if (myUsers.size() == myContacts.size()) {
                break;
            }
        }

        Collections.sort(myUsers, new Comparator<InviteChat>() {
            @Override
            public int compare(InviteChat contact1, InviteChat contact2) {
                String name1 = contact1.getUsername().toLowerCase();
                String name2 = contact2.getUsername().toLowerCase();
                return name1.compareTo(name2);
            }
        });



        if (!myContacts.isEmpty()) {
            HashMap<String, Contact> tempContactData = new HashMap<>(myContacts);
            for (InviteChat user : this.myUsers) {
                tempContactData.remove(Helper.getEndTrim(user.getPhone_number()));
            }
            ArrayList<InviteChat> inviteAble = new ArrayList<>();
            for (Map.Entry<String, Contact> contactEntry : tempContactData.entrySet()) {
                inviteAble.add(new InviteChat("123456",
                        contactEntry.getValue().getName(), "default", "verified",
                        "null", contactEntry.getValue().getName().toLowerCase(), true, contactEntry.getValue().getPhoneNumber()));
            }
            if (!inviteAble.isEmpty()) {
                Collections.sort(inviteAble, new Comparator<InviteChat>() {
                    @Override
                    public int compare(InviteChat contact1, InviteChat contact2) {
                        String name1 = contact1.getUsername().toLowerCase();
                        String name2 = contact2.getUsername().toLowerCase();
                        return name1.compareTo(name2);
                    }
                });
                //inviteAble.add(0, new InviteChat("-1", "-1", "-1", "-1", "-1", "-1", false, "-1"));
                this.myUsers.addAll(inviteAble);
            }
        }

      /*  for (int i = 0; i < myUsers.size(); i++) {
///            Log.e(TAG, "refreshMyContactsCache: At Position ::  " + i);
///            Log.e(TAG, "refreshMyContactsCache: User Name :: " + myUsers.get(i).getUsername());
///            Log.e(TAG, "refreshMyContactsCache: Phone Number :: " + myUsers.get(i).getPhone_number());
        }*/

        mAdapter = new MyInvitationAdapter(InviteContactActivity.this, myUsers);
        rvContacts.setAdapter(mAdapter);

    }


    public class MyInvitationAdapter extends RecyclerView.Adapter<MyInvitationAdapter.MyInvitationHolder> implements Filterable {
        private ArrayList<InviteChat> mListInvite;
        private ArrayList<InviteChat> mListInviteFilter;
        private Context mCtx;

        public MyInvitationAdapter(Context ctx, ArrayList<InviteChat> listInvite) {
            this.mListInvite = listInvite;
            this.mListInviteFilter = listInvite;
            this.mCtx = ctx;
        }

        @NonNull
        @Override
        public MyInvitationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.row_items_invites, parent, false);
            return new MyInvitationHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyInvitationHolder holder, int position) {
            holder.bindProcess();
        }

        @Override
        public int getItemCount() {
            return mListInviteFilter.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mListInviteFilter = mListInvite;
                    } else {
                        ArrayList<InviteChat> tempFilterList = new ArrayList<>();
                        for (InviteChat mContact : mListInvite) {
                            if (mContact.getUsername().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                tempFilterList.add(mContact);
                            }

                        }
                        mListInviteFilter = tempFilterList;

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mListInviteFilter;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mListInviteFilter = (ArrayList<InviteChat>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }


        public class MyInvitationHolder extends RecyclerView.ViewHolder {
            MyTextView tvFirstLetter, tvMobileNumber;
            MyTextViewBold tvFullName;
            MyButton btnInvite;
            ImageButton iBtnForwardChat;

            public MyInvitationHolder(@NonNull View itemView) {
                super(itemView);
                tvFirstLetter = itemView.findViewById(R.id.tvFirstLetter);
                tvFullName = itemView.findViewById(R.id.tvFullName);
                tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber);
                btnInvite = itemView.findViewById(R.id.btnInvite);
                iBtnForwardChat = itemView.findViewById(R.id.iBtnForwardChat);
                btnInvite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Helper.openShareIntent(mCtx, (mCtx.getString(R.string.hey_there) + " " + mCtx.getString(R.string.app_name) + "\n" + mCtx.getString(R.string.download_now) + ": " + ("https://play.google.com/store/apps/details?id=" + mCtx.getPackageName()) + "\n" + getString(R.string.download_now_ios) + "https://apps.apple.com/us/app/appopay/id1587340828"));
                        /*Hey there i am using Appoapy
                        Download Now https://play.google.com/store/apps/details?id=com.stuffer.stuffers*/
                    }
                });

                iBtnForwardChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (AppoPayApplication.isNetworkAvailable(mCtx)) {
                            Intent intent = new Intent(mCtx, MessageActivity.class);
                            intent.putExtra(AppoConstants.USERID, mListInviteFilter.get(getAdapterPosition()).getId());
                            intent.putExtra(AppoConstants.USERNAME, mListInvite.get(getAdapterPosition()).getUsername());
                            mCtx.startActivity(intent);
                        } else {
                            Toast.makeText(InviteContactActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

            public void bindProcess() {
                if (!mListInviteFilter.get(getAdapterPosition()).isInvite()) {
                    tvFirstLetter.setText(String.valueOf(mListInviteFilter.get(getAdapterPosition()).getUsername().charAt(0)));
                    tvFullName.setText(mListInviteFilter.get(getAdapterPosition()).getUsername());
                    tvMobileNumber.setText("Hey I am using Appopay");
                    btnInvite.setVisibility(View.GONE);
                    iBtnForwardChat.setVisibility(View.VISIBLE);
                } else {
                    tvFirstLetter.setText(String.valueOf(mListInviteFilter.get(getAdapterPosition()).getUsername().charAt(0)));
                    tvFullName.setText(mListInviteFilter.get(getAdapterPosition()).getUsername());
                    tvMobileNumber.setText(mListInviteFilter.get(getAdapterPosition()).getPhone_number());
                    btnInvite.setVisibility(View.VISIBLE);
                    iBtnForwardChat.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}