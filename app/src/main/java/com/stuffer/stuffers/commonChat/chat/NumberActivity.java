package com.stuffer.stuffers.commonChat.chat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

public class NumberActivity extends AppCompatActivity {
    private static final String TAG = "NumberActivity";
    private AlertDialog dialogEdit;


    private MyEditText edtMobile;
    private MyTextView tvSent;


    private CountryCodePicker edtCustomerCountryCode;
    private String selectedCountryCode;
    private MyTextViewBold tvCountryCode;
    private String selectedCountryNameCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_number);

        tvCountryCode = findViewById(R.id.tvCountryCode);
        edtCustomerCountryCode = findViewById(R.id.edtCustomerCountryCode);
        edtMobile = (MyEditText) findViewById(R.id.edtMobile);
        tvSent = (MyTextView) findViewById(R.id.tvSent);
        selectedCountryCode = edtCustomerCountryCode.getSelectedCountryCode();
        edtCustomerCountryCode.setDialogEventsListener(mLis);
        edtCustomerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selectedCountryCode = edtCustomerCountryCode.getSelectedCountryCode();

                selectedCountryNameCode = edtCustomerCountryCode.getSelectedCountryNameCode();
                tvCountryCode.setText(selectedCountryCode + " (" + selectedCountryNameCode + ")");
                edtCustomerCountryCode.setVisibility(View.GONE);
            }
        });
        tvSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(selectedCountryCode)) {
                    Toast.makeText(NumberActivity.this, getString(R.string.select_country_code), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtMobile.getText().toString().trim())) {
                    Toast.makeText(NumberActivity.this, getString(R.string.info_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    return;
                }
                showEdit();


            }
        });

        tvCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: called");
                //edtCustomerCountryCode.setVisibility(View.VISIBLE);
                showCountry();

            }
        });


    }

    private void showCountry() {

        /*new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selectedCountryCode = edtCustomerCountryCode.getSelectedCountryCode();
                String selectedCountryName = edtCustomerCountryCode.getSelectedCountryName();
                tvCountryCode.setText(selectedCountryName +" "+selectedCountryCode);
                edtCustomerCountryCode.setVisibility(View.GONE);
            }
        };*/

        //edtCustomerCountryCode.setDialogEventsListener(mLis);

        edtCustomerCountryCode.launchCountrySelectionDialog();


    }

    CountryCodePicker.DialogEventsListener mLis = new CountryCodePicker.DialogEventsListener() {
        @Override
        public void onCcpDialogOpen(Dialog dialog) {
            Log.e(TAG, "onCcpDialogOpen: called");
            edtCustomerCountryCode.setVisibility(View.VISIBLE);

        }

        @Override
        public void onCcpDialogDismiss(DialogInterface dialogInterface) {
            edtCustomerCountryCode.setVisibility(View.GONE);

        }

        @Override
        public void onCcpDialogCancel(DialogInterface dialogInterface) {
            edtCustomerCountryCode.setVisibility(View.GONE);
        }
    };

    public void showEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.verify_layout, null);
        MyTextViewBold btnEdit = dialogLayout.findViewById(R.id.btnEdit);
        MyTextViewBold btnOk = dialogLayout.findViewById(R.id.btnOk);
        MyTextView tvMobileNumber = dialogLayout.findViewById(R.id.tvMobileNumber);
        tvMobileNumber.setText("+" + selectedCountryCode + " " + edtMobile.getText().toString().trim());
        builder.setView(dialogLayout);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtMobile.requestFocus();
                dialogEdit.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextScreen();
            }
        });
        dialogEdit = builder.create();
        dialogEdit.setCanceledOnTouchOutside(false);
        dialogEdit.show();

    }

    private void nextScreen() {
        dialogEdit.dismiss();

        String selectedCountryNameCode = edtCustomerCountryCode.getSelectedCountryNameCode();
        DataVaultManager.getInstance(NumberActivity.this).saveCCODE(selectedCountryNameCode);
        Intent intent = new Intent(NumberActivity.this, VerificationActivity.class);
        intent.putExtra(AppoConstants.AREACODE, selectedCountryCode);
        intent.putExtra(AppoConstants.MOBILENUMBER, edtMobile.getText().toString().trim());
        startActivity(intent);
        finish();

    }
}