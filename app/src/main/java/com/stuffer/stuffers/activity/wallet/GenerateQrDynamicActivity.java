package com.stuffer.stuffers.activity.wallet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateQrDynamicActivity extends AppCompatActivity implements View.OnClickListener, TransactionPinListener {
    private static final String TAG = "GenerateQrDynamicActivi";
    private ImageView user_qr_image;
    private MyButton btnQrCreate;
    private FrameLayout frameLayout;
    private TextView tvUserName;
    private MyEditText edQrAmount;
    private ProgressDialog mDialog;
    private MainAPIInterface apiService;
    private MyTextView tvInfo;
    private BottotmPinFragment mBottomPinFragment;
    private FrameLayout frameCountry;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_dynamic);
        apiService = ApiUtils.getAPIService();
        setupActionBar();
        edQrAmount = (MyEditText) findViewById(R.id.edQrAmount);
        btnQrCreate = (MyButton) findViewById(R.id.btnQrCreate);
        user_qr_image = (ImageView) findViewById(R.id.user_qr_image);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvInfo = (MyTextView) findViewById(R.id.tvInfo);
        frameCountry=(FrameLayout)findViewById(R.id.frameCountry);
        countryCodePicker=(CountryCodePicker)findViewById(R.id.countryCodePicker);

        btnQrCreate.setOnClickListener(this);
        try {
            String phoneCode = Helper.getPhoneCode();
            //String phoneCode = "1";
            String senderMobileNumber = String.valueOf(Helper.getSenderMobileNumber());
            //String senderMobileNumber = "8092345454";
            //Log.e(TAG, "onCreate: senderMobileNumber : " + senderMobileNumber);
            if (phoneCode.equalsIgnoreCase("1")) {
                if (senderMobileNumber.startsWith("809") || senderMobileNumber.startsWith("829") || senderMobileNumber.startsWith("849")) {
                    countryCodePicker.setCountryForNameCode("DO");
                } else {
                    countryCodePicker.setCountryForPhoneCode(!Helper.getPhoneCode().equals("") ? Integer.parseInt(Helper.getPhoneCode()) : countryCodePicker.getDefaultCountryCodeAsInt());
                }
            } else {
                countryCodePicker.setCountryForPhoneCode(!Helper.getPhoneCode().equals("") ? Integer.parseInt(Helper.getPhoneCode()) : countryCodePicker.getDefaultCountryCodeAsInt());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        countryCodePicker.setDialogEventsListener(mLis);

    }

    CountryCodePicker.DialogEventsListener mLis = new CountryCodePicker.DialogEventsListener() {
        @Override
        public void onCcpDialogOpen(Dialog dialog) {
            dialog.dismiss();
        }

        @Override
        public void onCcpDialogDismiss(DialogInterface dialogInterface) {

        }

        @Override
        public void onCcpDialogCancel(DialogInterface dialogInterface) {

        }
    };


    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Customer QR-CODE");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

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

    private void showProgress() {
        mDialog = new ProgressDialog(GenerateQrDynamicActivity.this);
        mDialog.setMessage(getString(R.string.info_please_wait_dots));
        mDialog.show();
    }

    private void hideProgress() {
        if (mDialog != null)
            mDialog.dismiss();
    }

    private void showBottomPin() {
        mBottomPinFragment = new BottotmPinFragment();
        mBottomPinFragment.show(getSupportFragmentManager(), mBottomPinFragment.getTag());
        mBottomPinFragment.setCancelable(false);
    }

    @Override
    public void onPinConfirm(String transactionPin) {
        String mTransactionPin = Helper.getTransactionPin();
        if (mTransactionPin.equalsIgnoreCase(transactionPin)) {
            if (mBottomPinFragment != null) {
                mBottomPinFragment.dismiss();
            }
            showProgress();
            String customerAccountId = Helper.getCustomerAccountId();
            getDynamicQrCode(customerAccountId, edQrAmount.getText().toString().trim(), "true");
        } else {
            if (mBottomPinFragment != null) {
                mBottomPinFragment.dismiss();
            }
            Toast.makeText(this, "Invalid Transaction Pin", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnQrCreate) {

            if (edQrAmount.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "please enter amount", Toast.LENGTH_SHORT).show();
                return;
            }
            Helper.hideKeyboard(edQrAmount, GenerateQrDynamicActivity.this);
            showBottomPin();


        }
    }

    public void getDynamicQrCode(String customerId, String amount, String isImage) {
        //showProgress();
        apiService.getCustomerDynamicQrCode(customerId, amount, isImage).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //Log.e(TAG, "onResponse: "+response.body() );
                //Log.e(TAG, "onResponse: "+response );
                hideProgress();
                String s = new Gson().toJson(response.body());
                try {
                    JSONObject mResult = new JSONObject(s);
                    if (mResult.getString(AppoConstants.MESSAGE).equalsIgnoreCase(AppoConstants.SUCCESS)) {
                        String resultQRCODE = mResult.getString(AppoConstants.RESULT);
                        String mQrCode1 = resultQRCODE.substring(resultQRCODE.indexOf(",") + 1);
                        final byte[] decodedBytes = Base64.decode(mQrCode1, Base64.DEFAULT);
                        Glide.with(GenerateQrDynamicActivity.this).load(decodedBytes).into(user_qr_image);
                        frameLayout.setVisibility(View.VISIBLE);
                        String senderName = Helper.getSenderName();
                        tvUserName.setText(senderName);
                        tvUserName.setVisibility(View.VISIBLE);
                        tvInfo.setVisibility(View.VISIBLE);
                        frameCountry.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                hideProgress();
            }
        });

    }

}