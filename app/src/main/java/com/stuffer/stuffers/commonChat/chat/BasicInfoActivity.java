package com.stuffer.stuffers.commonChat.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.HomeActivity;
import com.stuffer.stuffers.activity.wallet.HomeActivity2;
import com.stuffer.stuffers.activity.wallet.Registration;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;


import org.apache.commons.lang3.StringUtils;

public class BasicInfoActivity extends AppCompatActivity {

    private ChatHelper chatHelper;
    private DatabaseReference usersRef;
    private MyEditText userPhone;
    private User userMe;
    private MyEditText userNameEditFirst;
    private MyEditText userNameEditLast;
    private MyTextView tvSave;
    private String mFirstName, mLastName, mFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatHelper = new ChatHelper(this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference(ChatHelper.REF_USERS);
        setContentView(R.layout.activity_basic_info);
        userPhone = (MyEditText) findViewById(R.id.userPhone);
        userNameEditFirst = (MyEditText) findViewById(R.id.userNameEditFirst);
        userNameEditLast = (MyEditText) findViewById(R.id.userNameEditLast);
        tvSave = (MyTextView) findViewById(R.id.tvSave);
        userPhone.setEnabled(false);
        userMe = chatHelper.getLoggedInUser();
        setUserDetails();
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });

    }

    private void verify() {
        mFirstName = userNameEditFirst.getText().toString().trim();
        mLastName = userNameEditLast.getText().toString().trim();
        if (TextUtils.isEmpty(mFirstName)) {
            userNameEditFirst.setError("enter first name");
            userNameEditFirst.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mLastName)) {
            userNameEditLast.setError("enter last name");
            userNameEditLast.requestFocus();
            return;
        }

        mFullName = mFirstName + " " + mLastName;
        userMe.setName(mFullName);
        usersRef.child(userMe.getId()).setValue(userMe);
        chatHelper.setLoggedInUser(userMe);

        /*String keyUserDetails = DataVaultManager.getInstance(this).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(keyUserDetails)) {
            Intent intent = new Intent(this, HomeActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            BasicInfoActivity.this.finish();
        } else {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            BasicInfoActivity.this.finish();
        }*/


        Intent intent = new Intent(this, HomeActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        BasicInfoActivity.this.finish();
        /*Intent intent = new Intent(this, Registration.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        BasicInfoActivity.this.finish();*/








    }


    private void setUserDetails() {
        userPhone.setText(userMe.getId());
    }


}