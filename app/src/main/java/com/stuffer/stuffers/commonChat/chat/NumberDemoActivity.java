package com.stuffer.stuffers.commonChat.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyCountryText;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

public class NumberDemoActivity extends AppCompatActivity {
    private static final String TAG = "NumberDemoActivity";
    private AlertDialog dialogEdit;


    private MyEditText edtMobile;
    private MyTextView tvSent;


    private CountryCodePicker edtCustomerCountryCode;
    private String selectedCountryCode;
    private MyCountryText tvCountryCode;
    private String selectedCountryNameCode;
    private ImageView ivFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_demo);
        setupActionBar();

        tvCountryCode = findViewById(R.id.tvCountryCode);
        ivFlag = findViewById(R.id.ivFlag);
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
                //tvCountryCode.setText("+"+selectedCountryCode + " (" + selectedCountryNameCode + ")");
                tvCountryCode.setText("+" + selectedCountryCode);
                edtCustomerCountryCode.setVisibility(View.GONE);
                ImageView imageViewFlag = edtCustomerCountryCode.getImageViewFlag();
                Bitmap bitmap = ((BitmapDrawable) imageViewFlag.getDrawable()).getBitmap();
                ivFlag.setImageBitmap(bitmap);
                ivFlag.setVisibility(View.VISIBLE);

            }
        });
        tvSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String term = tvCountryCode.getText().toString().trim();
                if (term.equalsIgnoreCase("select\ncountry")) {
                    edtMobile.setError("please select country code first");
                    edtMobile.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(selectedCountryCode)) {
                    Toast.makeText(NumberDemoActivity.this, getString(R.string.select_country_code), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtMobile.getText().toString().trim())) {
                    Toast.makeText(NumberDemoActivity.this, getString(R.string.info_enter_mobile_number), Toast.LENGTH_SHORT).show();
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

        edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String trim = tvCountryCode.getText().toString().trim();

                String term = tvCountryCode.getText().toString().trim();
                if (term.equalsIgnoreCase("select\ncountry")) {

                    edtMobile.setError("Select country code first");
                    edtMobile.requestFocus();
                    //edtMobile.setText("");

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this, R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
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
        DataVaultManager.getInstance(NumberDemoActivity.this).saveCCODE(selectedCountryNameCode);
        DataVaultManager.getInstance(NumberDemoActivity.this).saveUserMobile(edtMobile.getText().toString().trim());
        //Intent intent = new Intent(NumberDemoActivity.this, VerificationActivity.class);
        Intent intent = new Intent(NumberDemoActivity.this, VerifyDemoActivity.class);
        intent.putExtra(AppoConstants.AREACODE, selectedCountryCode);
        intent.putExtra(AppoConstants.MOBILENUMBER, edtMobile.getText().toString().trim());
        startActivity(intent);
        finish();
    }

    private void setupActionBar() {
        //MyTextViewBold common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        //common_toolbar_title.setText(mTitle);
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}