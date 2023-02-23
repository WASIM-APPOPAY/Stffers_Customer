
package com.stuffer.stuffers.activity.wallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.api.MainUAPIInterface;
import com.stuffer.stuffers.communicator.ConfirmSelectListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.emv.qrcode.core.model.mpm.TagLengthString;
import com.emv.qrcode.decoder.mpm.DecoderMpm;
import com.emv.qrcode.model.mpm.AdditionalDataField;
import com.emv.qrcode.model.mpm.AdditionalDataFieldTemplate;
import com.emv.qrcode.model.mpm.MerchantPresentedMode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayUserActivity extends AppCompatActivity implements ConfirmSelectListener, RecyclerViewRowItemCLickListener, TransactionPinListener {
    private static final String TAG = "PayUserActivity";
    MainAPIInterface mainAPIInterface;
    ProgressDialog dialog;
    String resultScan;
    private MyTextView tvHeader, tvCodeMobile, tvEmialId, tvIndex5, tvAccountNos, tvTerminalId;
    private MyTextView tvRequiredFilled, tvFromAccount, tvConversionRates, tvAmountCredit;
    private CardView tvCardMerchant;

    private Dialog dialogMerchant;
    private MyEditText edAmount;
    private float conversionRates = 0;
    MyButton btnPayNow;
    private float finaamount, bankfees, processingfees = 0, amountaftertax_fees, taxes;
    private int mFromPosition;
    private BottotmPinFragment bottotmPinFragment;
    private ProgressDialog mProgress;
    private String mCountryCode;
    private String mName;
    private String mCity;
    private String mId;
    private String mAIIN;
    private String mFIIN;
    private String mBillNumber;
    private String mReferenceLabel;
    private String mTerminalLabel;
    private String mMcc = "";
    private JSONObject mRoot;
    private MainUAPIInterface apiServiceUNIONPay;
    private String token;
    private String mDiscountAmt;
    private boolean mAllowDiscount = false;
    private String mOriginalAmount;
    private String mVoucherNumber;
    private String mTimeStamp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_user_activity);
        AndroidNetworking.initialize(getApplicationContext());
        apiServiceUNIONPay = ApiUtils.getApiServiceUNIONPay();

        setupActionBar();

        mainAPIInterface = ApiUtils.getAPIService();
        tvCardMerchant = (CardView) findViewById(R.id.tvCardMerchant);
        tvHeader = (MyTextView) findViewById(R.id.tvHeader);
        tvCodeMobile = (MyTextView) findViewById(R.id.tvCodeMobile);
        tvEmialId = (MyTextView) findViewById(R.id.tvEmialId);
        tvIndex5 = (MyTextView) findViewById(R.id.tvIndex5);
        tvTerminalId = (MyTextView) findViewById(R.id.tvTerminalId);

        tvAccountNos = (MyTextView) findViewById(R.id.tvAccountNos);

        tvRequiredFilled = (MyTextView) findViewById(R.id.tvRequiredFilled);
        String required = getString(R.string.required_filled) + "<font color='#00baf2'>" + "*" + "</font>";
        tvRequiredFilled.setText(Html.fromHtml(required));
        tvFromAccount = (MyTextView) findViewById(R.id.tvFromAccount);

        edAmount = (MyEditText) findViewById(R.id.edAmount);
        tvAmountCredit = (MyTextView) findViewById(R.id.tvAmountCredit);

        tvConversionRates = (MyTextView) findViewById(R.id.tvConversionRates);
        btnPayNow = (MyButton) findViewById(R.id.btnPayNow);
        resultScan = getIntent().getStringExtra(AppoConstants.MERCHANTSCANCODE);
        ////Log.e(TAG, "onCreate: resultScan :: " + resultScan);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyDetails();
            }
        });

        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (conversionRates == 0) {
                    Toast.makeText(PayUserActivity.this, getString(R.string.info_selecr_from_account_first), Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    float twoDecimal = (float) Helper.getTwoDecimal(Float.parseFloat(edAmount.getText().toString().trim()) * conversionRates);
                    tvAmountCredit.setText(String.valueOf(twoDecimal));

                } catch (Exception e) {
                    if (edAmount.getText().toString().trim().isEmpty()) {
                        ////Log.e(TAG, "onTextChanged: no toast");
                    } else {
                        Toast.makeText(PayUserActivity.this, getString(R.string.info_invalid_format), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        try {
            showMerchantDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSavedCard();

    }

    private void getSavedCard() {
        String walletAccountNumber = Helper.getWalletAccountNumber();
        apiServiceUNIONPay.getSavedCardUnion(walletAccountNumber).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    String s1 = new Gson().toJson(response.body());
                    try {
                        JSONObject mRoot = new JSONObject(s1);
                        if (mRoot.getInt("status") == 200 && mRoot.getString("message").equalsIgnoreCase("success")) {
                            if (mRoot.has("result")) {
                                String result = mRoot.getString("result");
                                //Log.e(TAG, "onResponse: saved caed " + result);
                                if (result != null && !result.isEmpty()) {
                                    String result1 = mRoot.getString("result");
                                    JSONObject mParent1 = new JSONObject(result1);
                                    JSONObject mParent = mParent1.getJSONObject("card_details");
                                    JSONObject trxInfo = mParent.getJSONObject("trxInfo");
                                    token = trxInfo.getString("token");
                                    DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveCardToken(token);
                                }
                            } else {
                                Toast.makeText(PayUserActivity.this, getString(R.string.info_request_for_card), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(PayUserActivity.this, getString(R.string.info_bad_request), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(PayUserActivity.this, getString(R.string.info_503), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(PayUserActivity.this, getString(R.string.info_not_found), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }


    private void showMerchantErrorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_merchant_error, null);

        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectHome();
            }
        });


        builder.setView(dialogLayout);

        dialogMerchant = builder.create();

        dialogMerchant.setCanceledOnTouchOutside(false);

        dialogMerchant.show();
    }

    private void redirectHome() {
        if (dialogMerchant != null) {
            dialogMerchant.dismiss();
        }
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void showMerchantDetails() {
        tvCardMerchant.setVisibility(View.VISIBLE);
        try {
            //getMerchantProfile(mobileNumber, merchantAreaCode);
            //tvHead //merchant name   //tvCodeMobile //country code   //tvEmialId // country name  //tvAccountNos //mid  //tvTerminalId //Terminal id
            String walletAccountNumber = Helper.getWalletAccountNumber();
            tvFromAccount.setText(walletAccountNumber);
            conversionRates = 1;

            MerchantPresentedMode merchantPresentedMode = DecoderMpm.decode(resultScan, MerchantPresentedMode.class);
            TagLengthString countryCode = merchantPresentedMode.getCountryCode();
            String value = countryCode.getValue();
            if (value != null) {
                tvCodeMobile.setText(getString(R.string.info_country_code) + value);
                mCountryCode = value;
            } else {
                tvCodeMobile.setText(getString(R.string.info_country_code) + "NA");
                mCountryCode = "";
            }

            TagLengthString merchantName = merchantPresentedMode.getMerchantName();
            String value1 = merchantName.getValue();
            if (value1 != null) {
                tvHeader.setText(value1);
                mName = value1;
            } else {
                tvHeader.setText(getString(R.string.info_merchant_name) + "NA");
                mName = "";
            }
            TagLengthString merchantCity = merchantPresentedMode.getMerchantCity();
            String value2 = merchantCity.getValue();
            if (value2 != null) {
                tvEmialId.setText(getString(R.string.info_city) + value2);
                mCity = value2;
            } else {
                tvEmialId.setText(getString(R.string.info_city) + "NA");
                mCity = value2;
            }
            TagLengthString merchantCategoryCode = merchantPresentedMode.getMerchantCategoryCode();
            mMcc = merchantCategoryCode.getValue();
            //Map<String, MerchantAccountInformationTemplate> merchantAccountInformation = merchantPresentedMode.getMerchantAccountInformation();
            String s = new Gson().toJson(merchantPresentedMode);
            JSONObject mParam = new JSONObject(s);
            JSONObject merchantAccountInformation = mParam.getJSONObject("merchantAccountInformation");
            JSONObject jsonObject15 = merchantAccountInformation.getJSONObject("15");
            JSONObject value3 = jsonObject15.getJSONObject("value");
            String value4 = value3.getString("value");
            if (value4 != null) {
                mAIIN = value4.substring(0, 8);
                mFIIN = value4.substring(8, 16);
                String substring = value4.substring(16);
                tvAccountNos.setText(" " + substring);
                mId = substring;

            } else {
                mId = "";
                mAIIN = "";
                mFIIN = "";
            }

            AdditionalDataFieldTemplate additionalDataField = merchantPresentedMode.getAdditionalDataField();
            AdditionalDataField value5 = additionalDataField.getValue();
            TagLengthString billNumber = value5.getBillNumber();
            mBillNumber = billNumber.getValue();
            TagLengthString referenceLabel = value5.getReferenceLabel();
            mReferenceLabel = referenceLabel.getValue();
            TagLengthString terminalLabel = value5.getTerminalLabel();
            mTerminalLabel = terminalLabel.getValue();
            if (mTerminalLabel != null) {
                tvTerminalId.setText(getString(R.string.info_tid) + mTerminalLabel);
            } else {
                tvTerminalId.setText(getString(R.string.info_tid) + " NA");
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "invalid format", Toast.LENGTH_SHORT).show();
            btnPayNow.setEnabled(false);
            btnPayNow.setClickable(false);

        }


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.toolbar_title_merchant_details);
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

    @Override
    public void onConfirmSelect() {


    }

    @Override
    public void onRowItemClick(int pos) {
        //fromAccountPosition = pos;
    }


    public void verifyDetails() {
        Helper.hideKeyboard(edAmount, PayUserActivity.this);
        if (tvFromAccount.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.info_selecr_from_account_first));
            return;
        }

        if (edAmount.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.info_enter_transer_amount));
            return;
        }

        try {
            Helper.getTwoDecimal(Float.parseFloat(edAmount.getText().toString().trim()) * 1);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.info_invalid_format), Toast.LENGTH_SHORT).show();
            return;
        }

        String currantBalance = Helper.getCurrantBalance();
        boolean status = Float.parseFloat(currantBalance) >= Float.parseFloat(tvAmountCredit.getText().toString().trim());
        if (status) {
            showBottomPinDialog();
        } else {
            showBalanceErrorDailog();
        }

    }

    public void showBottomPinDialog() {
        bottotmPinFragment = new BottotmPinFragment();
        bottotmPinFragment.show(getSupportFragmentManager(), bottotmPinFragment.getTag());
        bottotmPinFragment.setCancelable(false);
    }


    private void showYouAboutToPay() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_about_topay_common, null);

        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnNo = dialogLayout.findViewById(R.id.btnNo);
        String boldText = "<font color=''><b>" + edAmount.getText().toString().trim() + "</b></font>" + " " + "<font color=''><b>" + "USD" + "</b></font>";

        String paymentAmount = getString(R.string.merchant_partial_pay1) + " " + boldText + " " + getString(R.string.merchant_partial_pay2);

        tvInfo.setText(Html.fromHtml(paymentAmount));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makePayment();
                getJWSToken();


            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMerchant.dismiss();
            }
        });

        builder.setView(dialogLayout);
        dialogMerchant = builder.create();
        dialogMerchant.setCanceledOnTouchOutside(false);
        dialogMerchant.show();
    }

    public void showLoading() {
        mProgress = new ProgressDialog(PayUserActivity.this);
        mProgress.setMessage("Please wait.....");
        mProgress.show();
    }

    public void hide() {
        mProgress.dismiss();
    }

    private void getJWSToken() {
        if (dialogMerchant != null) {
            dialogMerchant.dismiss();
        }


        mRoot = new JSONObject();
        JSONObject mMsgInfo = new JSONObject();
        JSONObject mTrxInfo = new JSONObject();
        JSONObject mMerchantInfo = new JSONObject();
        JSONObject mRiskInfo = new JSONObject();
        JSONObject mAdditionalData = new JSONObject();

        try {
            //message info
            mMsgInfo.put("versionNo", TimeUtils.getVersion());
            String uniqueTimeStamp = TimeUtils.getUniqueTimeStamp();
            mMsgInfo.put("timeStamp", uniqueTimeStamp);
            mMsgInfo.put("msgID", TimeUtils.getUniqueMsgId(uniqueTimeStamp));
            mMsgInfo.put("msgType", "MPQRC_PAYMENT_EMV");
            mMsgInfo.put("insID", TimeUtils.getInsId());
            //transaction info
            mTrxInfo.put("deviceID", TimeUtils.getDeviceId());
            Long senderMobileNumber = Helper.getSenderMobileNumber();
            String senderAreaCode = Helper.getSenderAreaCode();
            String mPhWithCode = senderAreaCode + senderMobileNumber;
            mTrxInfo.put("userID", mPhWithCode);
            //mTrxInfo.put("token", "6292603420956679");
            //mTrxInfo.put("token", "6292603410292481");
            mTrxInfo.put("token", token);
            //mTrxInfo.put("trxAmt", edAmount.getText().toString().trim());
            //float twoDecimal = Helper.getTwoDecimal(amountaftertax_fees);

            mTrxInfo.put("trxAmt", edAmount.getText().toString().trim());
            mTrxInfo.put("trxCurrency", "840");
            mTrxInfo.put("mpqrcPayload", resultScan);
            //merchant info
            mMerchantInfo.put("acquirerIIN", mAIIN);
            mMerchantInfo.put("fwdIIN", mFIIN);
            mMerchantInfo.put("mid", mId);
            mMerchantInfo.put("merchantName", mName);
            mMerchantInfo.put("mcc", mMcc);
            //risk info
            mRiskInfo.put("captureMethod", "MANUAL");
            mRiskInfo.put("deviceType", "MOBILE");
            //additional data field
            mAdditionalData.put("billNo", mBillNumber);
            mAdditionalData.put("referenceLabel", mReferenceLabel);
            mAdditionalData.put("terminalLabel", mTerminalLabel);

            mTrxInfo.put("merchantInfo", mMerchantInfo);
            mTrxInfo.put("riskInfo", mRiskInfo);
            mTrxInfo.put("additionalData", mAdditionalData);

            mRoot.put("msgInfo", mMsgInfo);
            mRoot.put("trxInfo", mTrxInfo);
            // Log.e(TAG, "getJWSToken: " + mRoot.toString());

            showLoading();
            AndroidNetworking.post("https://labapi-union.appopay.com/scis/switch/getJWSToken")
                    .addHeaders("requestPath", "/scis/switch/qremvpayment")
                    .addHeaders("Content-Type", "application/json")
                    .addJSONObjectBody(mRoot)
                    .setPriority(Priority.IMMEDIATE)
                    .setTag("token")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hide();
                            try {
                                if (response.getInt("status") == 200) {
                                    //Log.e(TAG, "onResponse: called");
                                    if (response.getString("message").equalsIgnoreCase("success")) {
                                        String mResult = response.getString("result");
                                        requestForPayment(mResult);
                                    }
                                } else {
                                    showToast(response.getString("status"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            hide();
                            /*Log.e(TAG, "onError: " + anError.getMessage());
                            Log.e(TAG, "onError: " + anError.getErrorBody());
                            Log.e(TAG, "onError: " + anError.getErrorCode());
                            Log.e(TAG, "onError: " + anError.getResponse());
                            Log.e(TAG, "onError: " + anError.getErrorDetail());*/
                            showToast("Error Code : " + anError.getErrorBody());
                        }
                    });

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }


    }

    private void requestForPayment(String authToken) {
        showLoading();

        AndroidNetworking.post("https://labapi-union.appopay.com/scis/switch/qremvpayment")

                .addHeaders("requestPath", "/scis/switch/qremvpayment")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("authToken", authToken)
                .addJSONObjectBody(mRoot)
                .setPriority(Priority.IMMEDIATE)
                .setTag("token")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e(TAG, "onResponse: payment " + response);
                        hide();

                        try {
                            /*JSONObject mParam = new JSONObject(param);
                            response = mParam;*/
                            if (response.getInt("status") == 200) {
                                if (response.getString("message").equalsIgnoreCase("OK")) {
                                    String result = response.getString("result");
                                    Log.e(TAG, "onResponse: " + result);
                                    JSONObject mResult = new JSONObject(result);
                                    JSONObject mMsgInfo = mResult.getJSONObject("msgInfo");
                                    JSONObject mTrxInfo = mResult.getJSONObject("trxInfo");
                                    JSONObject msgResponse = mResult.getJSONObject("msgResponse");
                                    String responseCode = msgResponse.getString("responseCode");
                                    String responseMsg = msgResponse.getString("responseMsg");
                                    if (responseCode.equals("00")) {
                                        //if ()
                                        //mOriginalAmount = mTrxInfo.getString("trxAmt");
                                        if (mTrxInfo.has("trxAmt")) {
                                            mOriginalAmount = mTrxInfo.getString("trxAmt");
                                        }
                                        if (mTrxInfo.has("originalAmount")) {
                                            mOriginalAmount = mTrxInfo.getString("originalAmount");
                                        }
                                        mVoucherNumber = mTrxInfo.getString("qrcVoucherNo");
                                        mTimeStamp = mMsgInfo.getString("timeStamp");
                                        if (mTrxInfo.has("discountDetails")) {
                                            //Log.e(TAG, "onResponse: yes");
                                            JSONArray discountDetails = mTrxInfo.getJSONArray("discountDetails");
                                            JSONObject jsonObject = discountDetails.getJSONObject(0);
                                            mDiscountAmt = jsonObject.getString("discountAmt");
                                            Log.e(TAG, "onResponse: discount " + mDiscountAmt);
                                            mAllowDiscount = true;
                                        } else {
                                            mAllowDiscount = false;
                                            //Log.e(TAG, "onResponse: no ");
                                        }
                                        //showCommonError("Your transaction has been " + responseMsg, 124);
                                        showCommonError("SUCCESS", 124);
                                    } else {
                                        mAllowDiscount = false;
                                        showCommonError("Response Code : " + responseCode + "\nResponse Message : " + responseMsg);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            mAllowDiscount = false;
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        mAllowDiscount = false;
                        hide();
                        showToast(anError.getErrorBody());
                    }
                });


    }


    private void showSuccessDialog(String param) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_success_merchant, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvTransactionId = dialogLayout.findViewById(R.id.tvTransactionId);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        tvTransactionId.setText("Please save this " + param + " Transaction ID for reference");
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectHome();
            }
        });

        builder.setView(dialogLayout);

        dialogMerchant = builder.create();

        dialogMerchant.setCanceledOnTouchOutside(false);
        dialogMerchant.setCancelable(false);

        dialogMerchant.show();
    }


    private void showBalanceErrorDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        tvInfo.setText(getString(R.string.merchant_balance_error));
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMerchant.dismiss();
            }
        });

        builder.setView(dialogLayout);

        dialogMerchant = builder.create();

        dialogMerchant.setCanceledOnTouchOutside(false);

        dialogMerchant.show();
    }

    private void showSameAccountErrors() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        tvInfo.setText(getString(R.string.merchant_same_account));
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMerchant.dismiss();
            }
        });

        builder.setView(dialogLayout);

        dialogMerchant = builder.create();

        dialogMerchant.setCanceledOnTouchOutside(false);

        dialogMerchant.show();


    }

    public void showToast(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    public void showCommonError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        ImageView ivCancel = dialogLayout.findViewById(R.id.ivCancel);
        ivCancel.setVisibility(View.VISIBLE);

        tvInfo.setText(message);
        //tvInfo.setVisibility(View.GONE);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMerchant.dismiss();
            }
        });

        builder.setView(dialogLayout);

        dialogMerchant = builder.create();

        dialogMerchant.setCanceledOnTouchOutside(false);

        dialogMerchant.show();
    }

    public void showCommonError(String message, int param) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        ImageView ivSuccess = dialogLayout.findViewById(R.id.ivSuccess);
        ivSuccess.setVisibility(View.VISIBLE);
        if (mAllowDiscount) {
            MyTextView tvPaymentAmount = dialogLayout.findViewById(R.id.tvPaymentAmount);

            MyTextView tvDiscAmount = dialogLayout.findViewById(R.id.tvDiscAmount);
            MyTextView tvFinalAmount = dialogLayout.findViewById(R.id.tvFinalAmount);
            MyTextViewBold tvAmountPay = dialogLayout.findViewById(R.id.tvAmountPay);
            tvAmountPay.setText(tvAmountPay.getText().toString() + mOriginalAmount);
            tvAmountPay.setVisibility(View.VISIBLE);

            tvPaymentAmount.setText(tvPaymentAmount.getText().toString().trim() + " " + edAmount.getText().toString().trim());
            tvPaymentAmount.setVisibility(View.VISIBLE);
            tvDiscAmount.setText(tvDiscAmount.getText().toString().trim() + " " + mDiscountAmt);
            tvDiscAmount.setVisibility(View.VISIBLE);

            MyTextView tvCurrencyPay = dialogLayout.findViewById(R.id.tvCurrencyPay);
            tvCurrencyPay.setVisibility(View.VISIBLE);

            String currencyId = Helper.getCurrencyId();
            String mCurrencyId = "";
            if (Helper.getCurrencyId().equalsIgnoreCase("1")) {
                mCurrencyId = "USD";
            } else if (Helper.getCurrencyId().equalsIgnoreCase("2")) {

                mCurrencyId = "INR";
            } else if (Helper.getCurrencyId().equalsIgnoreCase("3")) {

                mCurrencyId = "CAD";
            } else if (Helper.getCurrencyId().equalsIgnoreCase("4")) {

                mCurrencyId = "ERU";
            } else if (Helper.getCurrencyId().equalsIgnoreCase("5")) {

                mCurrencyId = "DOP";
            }

            tvCurrencyPay.setText(tvCurrencyPay.getText().toString() + " " + mCurrencyId);

            MyTextView tvTransactionTime = dialogLayout.findViewById(R.id.tvTransactionTime);
            /*Date mDate=new Date(Long.parseLong(mTimeStamp));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = dateFormat.format(mDate);*/
            Date value = null;
            String ourDate = null;
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                value = formatter.parse(mTimeStamp);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //this format changeable
                ourDate = dateFormatter.format(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvTransactionTime.setText(tvTransactionTime.getText().toString() + "" + ourDate);
            tvTransactionTime.setVisibility(View.VISIBLE);

            MyTextView tvVoucherPay = dialogLayout.findViewById(R.id.tvVoucherPay);
            tvVoucherPay.setText(tvVoucherPay.getText().toString() + "" + mVoucherNumber);
            tvVoucherPay.setVisibility(View.VISIBLE);
            double mDiscTemp = Double.parseDouble(mDiscountAmt);
            double mAmountTemp = Double.parseDouble(edAmount.getText().toString().trim());
            double mFinalPay = mAmountTemp - mDiscTemp;
            tvFinalAmount.setText(tvFinalAmount.getText().toString().trim() + " " + mFinalPay);
            tvFinalAmount.setVisibility(View.VISIBLE);
        } else {
            MyTextView tvCurrencyPay = dialogLayout.findViewById(R.id.tvCurrencyPay);
            tvCurrencyPay.setVisibility(View.VISIBLE);
            String currencyId = Helper.getCurrencyId();
            String mCurrencyId = "";
            if (Helper.getCurrencyId().equalsIgnoreCase("1")) {
                mCurrencyId = "USD";
            } else if (Helper.getCurrencyId().equalsIgnoreCase("2")) {

                mCurrencyId = "INR";
            } else if (Helper.getCurrencyId().equalsIgnoreCase("3")) {

                mCurrencyId = "CAD";
            } else if (Helper.getCurrencyId().equalsIgnoreCase("4")) {

                mCurrencyId = "ERU";
            } else if (Helper.getCurrencyId().equalsIgnoreCase("5")) {

                mCurrencyId = "DOP";
            }
            tvCurrencyPay.setText(tvCurrencyPay.getText().toString() + " " + mCurrencyId);

            MyTextViewBold tvAmountPay = dialogLayout.findViewById(R.id.tvAmountPay);
            tvAmountPay.setText("Amount : " + mOriginalAmount);
            tvAmountPay.setVisibility(View.VISIBLE);

            MyTextView tvTransactionTime = dialogLayout.findViewById(R.id.tvTransactionTime);
            /*Date mDate=new Date(Long.parseLong(mTimeStamp));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = dateFormat.format(mDate);*/
            Date value = null;
            String ourDate = null;
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                value = formatter.parse(mTimeStamp);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //this format changeable
                ourDate = dateFormatter.format(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvTransactionTime.setText(tvTransactionTime.getText().toString() + "" + ourDate);
            tvTransactionTime.setVisibility(View.VISIBLE);
            MyTextView tvVoucherPay = dialogLayout.findViewById(R.id.tvVoucherPay);
            tvVoucherPay.setText(tvVoucherPay.getText().toString() + "" + mVoucherNumber);
            tvVoucherPay.setVisibility(View.VISIBLE);
        }


        tvInfo.setText(message);
        //tvInfo.setVisibility(View.GONE);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPrevious();
            }
        });

        builder.setView(dialogLayout);

        dialogMerchant = builder.create();

        dialogMerchant.setCanceledOnTouchOutside(false);

        dialogMerchant.show();
    }

    private void goPrevious() {
        dialogMerchant.dismiss();
        setResult(RESULT_OK);
        finish();
    }


    @Override
    public void onPinConfirm(String pin) {
        if (bottotmPinFragment != null)
            bottotmPinFragment.dismiss();

        String transactionPin = Helper.getTransactionPin();
        if (transactionPin != null) {
            if (transactionPin.equalsIgnoreCase(pin)) {
                showYouAboutToPay();
            } else {
                showToast(getString(R.string.info_invalid_transaction_pin));
            }
        }

    }
}
