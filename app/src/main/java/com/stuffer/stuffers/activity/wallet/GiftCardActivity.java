package com.stuffer.stuffers.activity.wallet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.contact.ContactDemoActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyEditText;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class GiftCardActivity extends AppCompatActivity implements View.OnClickListener {

    private PhoneNumberUtil phoneUtil;
    private MyEditText edtphone_number;
    private ImageView ivContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card);
        edtphone_number = (MyEditText) findViewById(R.id.edtphone_number);
        ivContactList = (ImageView) findViewById(R.id.ivContactList);
        setupActionBar();

        ivContactList.setOnClickListener(this);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("UnionPay Gift Card");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppoConstants.PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                //  Log.e(TAG, "onActivityResult: Pick Contact NUmber :: " + data.getStringExtra(AppoConstants.INFO));
                String mMobileNumber = data.getStringExtra(AppoConstants.INFO);
                edtphone_number.setText(mMobileNumber);
                try {
                    // phone must begin with '+'
                    if (phoneUtil == null) {
                        phoneUtil = PhoneNumberUtil.createInstance(GiftCardActivity.this);
                    }
                    Phonenumber.PhoneNumber numberProto = phoneUtil.parse(mMobileNumber, "");
                    int countryCode = numberProto.getCountryCode();
                    long nationalNumber = numberProto.getNationalNumber();
                    edtphone_number.setText(String.valueOf(nationalNumber));
                    //  Log.e("code", "code " + countryCode);
                    //  Log.e("code", "national number " + nationalNumber);
                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
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
    public void onClick(View v) {
        if (v.getId() == R.id.ivContactList) {
            Intent intentContact = new Intent(GiftCardActivity.this, ContactDemoActivity.class);
            startActivityForResult(intentContact, AppoConstants.PICK_CONTACT);

        }
    }
}