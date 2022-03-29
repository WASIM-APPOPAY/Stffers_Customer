package com.stuffer.stuffers.activity.contact;

import android.Manifest;
import android.app.Activity;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.fragments.bottom.chatadapter.UserAdapter;
import com.stuffer.stuffers.fragments.bottom.chatmodel.ChatUser;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyRadioButton;
import com.stuffer.stuffers.views.MyTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ContactDemoActivity extends AppCompatActivity {
    private static final String TAG = "ContactDemoActivity";

    private RecyclerView rvContacts;
    ImageView ivContactList;
    Gson gson;
    private HashMap<String, Contact> myContacts;
    private ProgressDialog dialog;
    private MyContactAdapter mAdapter;
    private MyEditText edSearch;
    private MyButton btnConfirm;

    private String mContactNumber;
    private List<ChatUser> mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_demo);
        ivContactList = findViewById(R.id.ivContactList);
        edSearch = findViewById(R.id.edSearch);
        rvContacts = findViewById(R.id.rvContacts);
        btnConfirm = findViewById(R.id.btnConfirm);
        rvContacts.setLayoutManager(new LinearLayoutManager(ContactDemoActivity.this));

        ivContactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Helper.hideKeyboard(edSearch, ContactDemoActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isEmpty(mContactNumber)) {
                    Toast.makeText(ContactDemoActivity.this, "Please select contact number", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(AppoConstants.INFO, mContactNumber);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Helper.isPhonePermissionGranted(ContactDemoActivity.this)) {
                requestPermission();
            } else {
                fetchMyContacts();
            }
        } else {
            fetchMyContacts();
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
            if (Helper.isPhonePermissionGranted(ContactDemoActivity.this)) {
                fetchMyContacts();
            } else {
                boolean setting = false;
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRational = shouldShowRequestPermissionRationale(permission);
                        if (!showRational) {
                            //Log.e(TAG, "onRequestPermissionsResult: true : " + permission);
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
                       // Log.e(TAG, "fetchMyContacts: Phone No :: " + number + "  Name :: " + name + "  EndTrim :: " + endTrim);
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

        ArrayList<Contact> mListContact = new ArrayList<>();
        for (Contact contact : contactsToSet.values()) {
            mListContact.add(contact);
        }
        Collections.sort(mListContact, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact1, Contact contact2) {
                String name1 = contact1.getName().toLowerCase();
                String name2 = contact2.getName().toLowerCase();
                return name1.compareTo(name2);
            }
        });

        mAdapter = new MyContactAdapter(ContactDemoActivity.this, mListContact);
        rvContacts.setAdapter(mAdapter);

    }


    public class MyContactAdapter extends RecyclerView.Adapter<MyContactAdapter.MyContactHolder> implements Filterable {
        Context mContext;
        ArrayList<Contact> mListContact;
        ArrayList<Contact> mListContactFilter;
        public int lastSelectedPosition = -1;

        MyContactAdapter(Context mContext, ArrayList<Contact> mListContact) {
            this.mContext = mContext;
            this.mListContact = mListContact;
            this.mListContactFilter = mListContact;
        }

        @NonNull
        @Override
        public MyContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_contact_item, parent, false);
            return new MyContactHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyContactHolder holder, int position) {
            holder.makeData();
        }

        @Override
        public int getItemCount() {
            return mListContactFilter.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mListContactFilter = mListContact;
                    } else {
                        ArrayList<Contact> tempFilterList = new ArrayList<>();
                        for (Contact mContact : mListContact) {
                            if (mContact.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                tempFilterList.add(mContact);
                            }

                        }
                        mListContactFilter = tempFilterList;

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mListContactFilter;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mListContactFilter = (ArrayList<Contact>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        class MyContactHolder extends RecyclerView.ViewHolder {
            MyTextView tvFirstLetter, tvFullName, tvMobileNumber;
            MyRadioButton rbtnSelect;

            MyContactHolder(@NonNull View itemView) {
                super(itemView);
                tvFirstLetter = itemView.findViewById(R.id.tvFirstLetter);
                tvFullName = itemView.findViewById(R.id.tvFullName);
                tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber);
                rbtnSelect = itemView.findViewById(R.id.rbtnSelect);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lastSelectedPosition = getAdapterPosition();
                        mContactNumber = mListContactFilter.get(lastSelectedPosition).getPhoneNumber();
                        notifyDataSetChanged();
                    }
                });

                rbtnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lastSelectedPosition = getAdapterPosition();
                        mContactNumber = mListContactFilter.get(lastSelectedPosition).getPhoneNumber();
                        notifyDataSetChanged();
                    }
                });
            }


            void makeData() {
                tvFirstLetter.setText(String.valueOf(mListContactFilter.get(getAdapterPosition()).getName().charAt(0)));
                tvFullName.setText(mListContactFilter.get(getAdapterPosition()).getName());
                tvMobileNumber.setText(mListContactFilter.get(getAdapterPosition()).getPhoneNumber());
                rbtnSelect.setChecked(lastSelectedPosition == getAdapterPosition());
            }
        }
    }


}
