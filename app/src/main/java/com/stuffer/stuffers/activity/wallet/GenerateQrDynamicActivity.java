package com.stuffer.stuffers.activity.wallet;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_CCODE;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.earthling.atminput.ATMEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
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
    //private MyEditText edQrAmount;
    private ATMEditText edQrAmount;
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
        edQrAmount = (ATMEditText) findViewById(R.id.edQrAmount);
        btnQrCreate = (MyButton) findViewById(R.id.btnQrCreate);
        user_qr_image = (ImageView) findViewById(R.id.user_qr_image);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvInfo = (MyTextView) findViewById(R.id.tvInfo);
        frameCountry=(FrameLayout)findViewById(R.id.frameCountry);
        countryCodePicker=(CountryCodePicker)findViewById(R.id.countryCodePicker);

        btnQrCreate.setOnClickListener(this);
        try {

            String vaultValue1 = DataVaultManager.getInstance(GenerateQrDynamicActivity.this).getVaultValue(KEY_CCODE);

            if (!StringUtils.isEmpty(vaultValue1)) {
                countryCodePicker.setCountryForNameCode(vaultValue1);
            } else {
                countryCodePicker.setCountryForPhoneCode(countryCodePicker.getDefaultCountryCodeAsInt());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        countryCodePicker.setDialogEventsListener(mLis);
        edQrAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
        MyTextViewBold common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        common_toolbar_title.setText("Customer QR-CODE");
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

        if (mBottomPinFragment != null) {
            mBottomPinFragment.dismiss();
        }
        showProgress();
        String customerAccountId = Helper.getCustomerAccountId();
        getDynamicQrCode(customerAccountId, edQrAmount.getText().toString().trim(), "true");


        /*if (mTransactionPin.equalsIgnoreCase(transactionPin)) {
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
        }*/
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
                    }else {
                        if (mResult.getString("result").equalsIgnoreCase("failed")){
                            Toast.makeText(GenerateQrDynamicActivity.this, ""+mResult.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                hideProgress();
            }
        });

    }

}