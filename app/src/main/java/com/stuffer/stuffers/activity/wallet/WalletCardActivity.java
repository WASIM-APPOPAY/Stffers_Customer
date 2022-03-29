package com.stuffer.stuffers.activity.wallet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.ConfirmSelectListener;
import com.stuffer.stuffers.communicator.CurrencySelectListener;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.fragments.dialog.CurrencyDialogFragment;
import com.stuffer.stuffers.models.Country.CountryCodeResponse;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.CardNumberValidation;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.CardDateEditText;
import com.stuffer.stuffers.views.CardNumberEditText;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import io.card.payment.CardIOActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class WalletCardActivity extends AppCompatActivity implements CurrencySelectListener, ConfirmSelectListener, TransactionPinListener {
    MyTextView tvFromAccount;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private String areacode;
    private String mobileNumber;
    ArrayList<AccountModel> mListAccount;
    //LinearLayout layoutCurrency;
    private String mCardType;
    //Gopal Gung address
    private List<CurrencyResult> result;
    private static final String TAG = "WalletCardActivity";
    private CurrencyDialogFragment dialogFragment;
    private int mCurrencyId;
    private String mCurrencyCode;
    private MyEditText edAmount;
    //private MyTextView tvConversionRates;
    //private MyTextView tvAmountCredit;
    private TextInputEditText cardholder_field_text;
    private CardNumberEditText card_number_field_text;
    private CardDateEditText expiry_date_field;
    private TextInputEditText card_filed_cvv;
    private MyButton btnTransfer;
    //private MyTextView tvSelectBankCurrency;
    private float conversionRates;
    private Dialog dialogFund;
    private JSONObject jsonCommission;
    private float bankfees;
    private float processingfees;

    private float amountaftertax_fees;
    private float taxes;
    private String userTransactionPin;
    private BottotmPinFragment bottotmPinFragment;
    private MyEditText edZipCode;
    List<Result> mListCountry;
    private String mCountryName;
    private String mStateName;
    int MY_SCAN_CARD_REQUEST_CODE = 987;
    private static final int MY_SCAN_REQUEST_CODE_TOP = 1001;
    private String stringExtraPath;
    private MainAPIInterface apiServiceOCR;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_card);

        mainAPIInterface = ApiUtils.getAPIService();
        apiServiceOCR = ApiUtils.getApiServiceOCR();
        mListAccount = new ArrayList<>();
        setupActionBar();
        findViews();
        mUserName = Helper.getSenderName();
        cardholder_field_text.setText(mUserName);
        showScanActivity();
        getUserDetailsForProfile();


        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    float twoDecimal = (float) Helper.getTwoDecimal(Float.parseFloat(edAmount.getText().toString().trim()) * 1);
                    btnTransfer.setEnabled(true);
                    btnTransfer.setClickable(true);
                } catch (Exception e) {
                    if (edAmount.getText().toString().trim().isEmpty()) {
                        //do nothing user is using back press for inserting new digits
                    } else {
                        btnTransfer.setEnabled(false);
                        btnTransfer.setClickable(false);
                        Toast.makeText(WalletCardActivity.this, getString(R.string.info_invalid_format), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void showToast(String param) {
        Toast.makeText(this, "" + param, Toast.LENGTH_SHORT).show();

    }

    private void findViews() {
        tvFromAccount = findViewById(R.id.tvFromAccount);
        edZipCode = (MyEditText) findViewById(R.id.edZipCode);
        cardholder_field_text = (TextInputEditText) findViewById(R.id.cardholder_field_text);
        expiry_date_field = (CardDateEditText) findViewById(R.id.expiry_date_field_text);
        card_filed_cvv = (TextInputEditText) findViewById(R.id.card_filed_cvv);
        card_number_field_text = (CardNumberEditText) findViewById(R.id.card_number_field_text);
        edAmount = (MyEditText) findViewById(R.id.edAmount);
        btnTransfer = (MyButton) findViewById(R.id.btnTransfer);

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edAmount.getText().toString().trim().isEmpty()) {
                    //showToast(getString(R.string.info_enter_fund_ammount));
                    edAmount.setError(getString(R.string.info_enter_fund_ammount));
                    edAmount.requestFocus();
                    edAmount.setFocusable(true);
                    return;
                }

              /*  if (edZipCode.getText().toString().trim().isEmpty()) {
                    edZipCode.setError("please enter your zip code");
                    edZipCode.requestFocus();
                    edZipCode.setFocusable(true);
                    return;
                }*/


                if (card_number_field_text.getText().toString().trim().isEmpty()) {
                    card_number_field_text.setError(getString(R.string.info_enter_your_card_number));
                    card_number_field_text.requestFocus();
                    return;
                }

                long cardNumberLong = Long.parseLong(card_number_field_text.getCardNumber().toString().trim());
                //Log.e(TAG, "verifyCardDetails: " + cardNumberLong);
                if (!CardNumberValidation.isValid(cardNumberLong)) {
                    card_number_field_text.setError(getString(R.string.info_enter_valid_card));
                    card_number_field_text.requestFocus();
                    return;
                }
                if (cardholder_field_text.getText().toString().trim().isEmpty()) {
                    cardholder_field_text.setError(getString(R.string.info_enter_card_holder_name));
                    cardholder_field_text.requestFocus();
                    return;
                }

                if (expiry_date_field.getText().toString().trim().isEmpty()) {
                    expiry_date_field.setError(getString(R.string.info_enter_expirary_date));
                    expiry_date_field.requestFocus();
                    return;
                }

                if (expiry_date_field.getText().toString().trim().length() < 3) {
                    expiry_date_field.setError(getString(R.string.info_invalid_format));
                    expiry_date_field.requestFocus();
                    return;
                }

                if (card_filed_cvv.getText().toString().trim().isEmpty()) {
                    card_filed_cvv.setError("enter cvv ");
                    card_filed_cvv.requestFocus();
                    return;
                }

                if (card_filed_cvv.getText().toString().trim().length() < 3) {
                    card_filed_cvv.setError("cvv must be 3 digits numeric");
                    card_filed_cvv.requestFocus();
                    return;
                }

                String cardNumber = card_number_field_text.getCardNumber();
                int cardType = card_number_field_text.getCardType(cardNumber);
                mCardType = null;
                switch (cardType) {
                    case 0:
                        mCardType = "Visa";
                        break;
                    case 1:
                        mCardType = "MasterCard";
                        break;
                    case 2:
                        mCardType = "American Express";
                        break;
                    case 3:
                        mCardType = "Diners Club";
                        break;
                    case 4:
                        mCardType = "Discover";
                        break;
                    case 5:
                        mCardType = "JCB";
                        break;
                    default:
                        mCardType = "unknown";
                        break;
                }


                showBottomPinDialog();
            }
        });
    }

    public void showBottomPinDialog() {
        bottotmPinFragment = new BottotmPinFragment();
        bottotmPinFragment.show(getSupportFragmentManager(), bottotmPinFragment.getTag());
        bottotmPinFragment.setCancelable(false);
    }


    private void getFundCommissions(String transaction) {
        userTransactionPin = transaction;
        //Log.e(TAG, "getCommissions: pin : " + transaction);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.info_conversion_rate));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getCardFundCommission(bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //Log.e(TAG, "onResponse: commissions : " + new Gson().toJson(response.body()));
                    if (dialogFund != null) {
                        dialogFund.dismiss();
                    }
                    String res = new Gson().toJson(response.body());
                    try {
                        jsonCommission = new JSONObject(res);
                        calculateCommission();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (dialogFund != null) {
                        dialogFund.dismiss();
                    }
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(WalletCardActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(WalletCardActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(WalletCardActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialogFund != null) {
                    dialogFund.dismiss();
                }
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });


    }

    private void calculateCommission() {
        try {
            JSONObject jsonResult = jsonCommission.getJSONObject(AppoConstants.RESULT);
            float bankcommission = Float.parseFloat(jsonResult.getString(AppoConstants.BANKCOMMISSION));
            float processingCommission = Float.parseFloat(jsonResult.getString(AppoConstants.PROCESSINGFEES));
            float flatbankcomission = Float.parseFloat(jsonResult.getString(AppoConstants.FLATBANKCOMMISSION));
            float flatprocessingcomission = Float.parseFloat(jsonResult.getString(AppoConstants.FLATPROCESSINGFEES));
            float taxPercentage = Float.parseFloat(jsonResult.getString(AppoConstants.TAXPERCENTAGE));
            String taxon = jsonResult.getString(AppoConstants.TAXON);
            float fundamount = Float.parseFloat(edAmount.getText().toString().trim());
            bankfees = 0;
            processingfees = 0;
            bankfees = (float) Helper.getTwoDecimal(bankcommission * fundamount);
            processingfees = (float) Helper.getTwoDecimal(fundamount * processingCommission);
            bankfees = (float) Helper.getTwoDecimal(bankfees + flatbankcomission);
            processingfees = (float) Helper.getTwoDecimal(processingfees + flatprocessingcomission);
            amountaftertax_fees = 0;
            taxes = 0;
            if (taxon.equalsIgnoreCase(AppoConstants.FEES)) {
                taxes = (float) Helper.getTwoDecimal(((processingfees * taxPercentage) / 100));
                amountaftertax_fees = (float) Helper.getTwoDecimal(fundamount + ((processingfees * taxPercentage) / 100) + processingfees);
            } else {
                taxes = (float) Helper.getTwoDecimal((fundamount * taxPercentage) / 100);
                amountaftertax_fees = (float) Helper.getTwoDecimal(fundamount + ((fundamount * taxPercentage) / 100));
            }
            showYouAboutToPay();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showYouAboutToPay() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogLayout = inflater.inflate(R.layout.dialog_about_topay_common, null);

        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnNo = dialogLayout.findViewById(R.id.btnNo);
        tvInfo.setText(getString(R.string.info_fund_wallet));
        tvHeader.setText("Add Fund's by Card");
        String boldText = "<font color=''><b>" + amountaftertax_fees + "</b></font>" + " " + "<font color=''><b>" + mListAccount.get(0).getCurrencyCode() + "</b></font>";

        String paymentAmount = getString(R.string.merchant_partial_pay1) + " " + boldText + " " + getString(R.string.merchant_partial_pay2);

        tvInfo.setText(Html.fromHtml(paymentAmount));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFund.dismiss();
                makePayment();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFund.dismiss();
            }
        });

        builder.setView(dialogLayout);
        dialogFund = builder.create();
        dialogFund.setCanceledOnTouchOutside(false);
        dialogFund.show();
    }

    /**
     * accountnumber: "6367820001585611774"
     * amount: 10.15
     * areacode: 507
     * cardtpe: "VISA"
     * ccexp: "1025"
     * ccnumber: 4111111111111111
     * charges: "0.10"
     * currencycode: "USD"
     * currencyid: 1
     * cvv: 999
     * fees: "0.05"
     * fullName: "MOE ALHARAZI"
     * fundamount: "10"
     * mobilenumber: 63516303
     * senddername: "Moe Alharazi"
     * transactionpin: "777777"
     * userid: 108
     */

    private void makePayment() {
        JsonObject paramSent = new JsonObject();
        paramSent.addProperty(AppoConstants.ACCOUNTNUMBER, mListAccount.get(0).getAccountnumber());
        paramSent.addProperty(AppoConstants.AREACODE, Integer.parseInt(Objects.requireNonNull(Helper.getSenderAreaCode())));
        paramSent.addProperty(AppoConstants.AMOUNT, amountaftertax_fees);
        paramSent.addProperty(AppoConstants.CARDTYPE, mCardType.toUpperCase());
        String expDate = expiry_date_field.getText().toString().trim();
        String replaceExpDate = expDate.replace("/", "");
        paramSent.addProperty(AppoConstants.CCEXP, replaceExpDate);
        paramSent.addProperty(AppoConstants.CCNUMBER, card_number_field_text.getCardNumber());
        paramSent.addProperty(AppoConstants.CHARGES, String.valueOf(bankfees));
        paramSent.addProperty(AppoConstants.CURRENCYCODE, mListAccount.get(0).getCurrencyCode());
        paramSent.addProperty(AppoConstants.CURRENCYID, mListAccount.get(0).getCurrencyid());
        paramSent.addProperty(AppoConstants.CVV, card_filed_cvv.getText().toString().trim());
        paramSent.addProperty(AppoConstants.FEES, String.valueOf(processingfees));
        paramSent.addProperty(AppoConstants.TAXES, taxes);
        paramSent.addProperty(AppoConstants.FULLNAME, cardholder_field_text.getText().toString().trim());
        paramSent.addProperty(AppoConstants.FUNDAMOUNTS, edAmount.getText().toString().trim());
        paramSent.addProperty(AppoConstants.MOBILENUMBER, Helper.getSenderMobileNumber());
        paramSent.addProperty(AppoConstants.SENDERNAME, Helper.getSenderName());
        paramSent.addProperty(AppoConstants.TRANSACTIONPIN, userTransactionPin);
        paramSent.addProperty(AppoConstants.USERID, Helper.getUserId());
        paramSent.addProperty(AppoConstants.ADDRESS1, Helper.getAddress());
        paramSent.addProperty(AppoConstants.sFIRSTNAME, Helper.getFirstName());
        paramSent.addProperty(AppoConstants.sLASTNAME, Helper.getLastName());

        paramSent.addProperty(AppoConstants.CITY, Helper.getCityName());
        int stateId = Integer.parseInt(Helper.getStateId());
        int countryId = Integer.parseInt(Helper.getCountryId());

        for (int i = 0; i < mListCountry.size(); i++) {
            if (mListCountry.get(i).getId().equals(countryId)) {
                mCountryName = mListCountry.get(i).getCountryname();
                for (int k = 0; k < mListCountry.get(i).getStates().size(); k++) {
                    if (mListCountry.get(i).getStates().get(k).getId().equals(stateId)) {
                        mStateName = mListCountry.get(i).getStates().get(k).getStatename();
                        break;
                    }
                }
            }

        }
        paramSent.addProperty(AppoConstants.STATE, mStateName);
        paramSent.addProperty(AppoConstants.COUNTRY, mCountryName);
        paramSent.addProperty(AppoConstants.ZIPCODE, "");
        // Log.e(TAG, "makePayment: " + paramSent.toString());
        creditFundToWallet(paramSent);
    }

    private void creditFundToWallet(JsonObject paramSent) {
        String accessToken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.info_processing_your_request));
        dialog.show();
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        mainAPIInterface.postAddFundToWallet(paramSent, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    //Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                    String res = new Gson().toJson(response.body());
                    try {
                        JSONObject responsePayment = new JSONObject(res);
                        if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("-1")) {
                            showCommonError(getString(R.string.info_invalid_transactipon_pin));
                        } else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("-2")) {
                            showCommonError(getString(R.string.info_sufficient_fund));
                        } else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("0")) {
                            showCommonError(getString(R.string.info_payment_failed_try_after));
                        } else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("1"))
                            showSuccessDialog();
                        else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("2")) {
                            showCommonError("Your transaction has been decline");
                        } else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("3")) {
                            showCommonError("Error in transaction data or system error");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(WalletCardActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(WalletCardActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(WalletCardActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });


    }

    public void showCommonError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        tvInfo.setText(message);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.info_fund_wallet));
        ImageView ivCancel = dialogLayout.findViewById(R.id.ivCancel);
        ivCancel.setVisibility(View.VISIBLE);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFund.dismiss();
            }
        });

        builder.setView(dialogLayout);

        dialogFund = builder.create();

        dialogFund.setCanceledOnTouchOutside(false);

        dialogFund.show();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_success_merchant, null);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.info_fund_wallet));
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvSuccess = dialogLayout.findViewById(R.id.tvSuccess);
        tvSuccess.setText(R.string.info_wallet_credit_successfully);
        MyTextView tvTransactionId = dialogLayout.findViewById(R.id.tvTransactionId);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectHome();
            }
        });

        builder.setView(dialogLayout);

        dialogFund = builder.create();

        dialogFund.setCanceledOnTouchOutside(false);

        dialogFund.show();
    }

    private void redirectHome() {
        if (dialogFund != null) {
            dialogFund.dismiss();
        }

        getCurrentWalletBalance();

    }

    private void getCurrentWalletBalance() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.info_please_wait));
        dialog.show();

        String accessToken = DataVaultManager.getInstance(this).getVaultValue(KEY_ACCESSTOKEN);
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        JSONObject mIndex = null;
        String ph1 = null;
        String area1 = null;
        try {
            mIndex = new JSONObject(vaultValue);
            JSONObject mResult = mIndex.getJSONObject(AppoConstants.RESULT);
            ph1 = mResult.getString(AppoConstants.MOBILENUMBER);
            area1 = mResult.getString(AppoConstants.PHONECODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        mainAPIInterface.getProfileDetails(Long.parseLong(ph1), Integer.parseInt(area1), bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    JsonObject body = response.body();
                    String res = body.toString();

                    try {
                        JSONObject indexUser = new JSONObject(res);
                        if (indexUser.isNull("result")) {
                            //Log.e(TAG, "onResponse: " + true);
                            Toast.makeText(WalletCardActivity.this, getString(R.string.error_user_detail_not_found), Toast.LENGTH_SHORT).show();
                        } else {
                            DataVaultManager.getInstance(WalletCardActivity.this).saveUserDetails(res);
                            //getCurrencyCode();
                            Intent intent = new Intent(WalletCardActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(WalletCardActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(WalletCardActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(WalletCardActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 400) {
                        Toast.makeText(WalletCardActivity.this, getString(R.string.error_bad_request), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }


    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);
        ImageView ivScanCard = (ImageView) toolbar.findViewById(R.id.ivScanCard);
        ivScanCard.setVisibility(View.VISIBLE);
        ivScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, "onClick: scan called");
                showScanActivity();
            }
        });

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(R.string.info_add_fund_by_card);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(R.string.info_add_money);
        toolbar.setTitleTextColor(Color.WHITE);


    }

    private void showScanActivity() {
        /*Intent intentCamera = new Intent(WalletCardActivity.this, CameraActivity.class);
        startActivityForResult(intentCamera, 777);*/
        //KYCCamera.create(this).openCamera(KYCCamera.TYPE_PANCARD_FRONT);
        Intent scanIntent = new Intent(WalletCardActivity.this, CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_SCAN, true); // supmit cuando termine de reconocer el documento
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true); // esconder teclado
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, false); // cambiar logo de paypal por el de card.io
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true); // capture img
        scanIntent.putExtra(CardIOActivity.EXTRA_CAPTURED_CARD_IMAGE, true); // capturar img
        //scanIntent.putExtra(CardIOActivity.EXTRA_UNBLUR_DIGITS, true);
        // laszar activity
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE_TOP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            // Log.e(TAG, "onActivityResult: elase called");
            if (requestCode == MY_SCAN_REQUEST_CODE_TOP ) {//|| requestCode == RESULT_SCAN_SUPPRESSED) {               // RESULT_SCAN_SUPPRESSED
                Log.e(TAG, "onActivityResult: called signature");
                Bitmap myBitmap = CardIOActivity.getCapturedCardImage(data);
                if (myBitmap!=null){
                    //pcf.setImageBitmap(myBitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();
                    String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    //uploadFile(stringExtraPath);
                    JsonObject mSent = new JsonObject();
                    mSent.addProperty("image", imageString);
                    showLoading();
                    getDataFromImage(mSent);
                }else {
                    //Log.e(TAG, "onActivityResult: " );
                }


            }
        }



    private void getDataFromImage(JsonObject mRoot) {


        apiServiceOCR.getExtractedDataOfBank(mRoot).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                closeLoadig();
                //Log.e(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    String mRes = new Gson().toJson(response.body());
                    try {
                        JSONObject mRoot = new JSONObject(mRes);
                        JSONObject jsonData = mRoot.getJSONObject(AppoConstants.DATA);

                        if (jsonData.has("cardNumber")) {
                            card_number_field_text.setText(jsonData.getString("cardNumber"));
                        }
                        if (jsonData.has("validThru")) {
                            expiry_date_field.setText(jsonData.getString("validThru"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /*String responseExtract = new Gson().toJson(response.body());
                    try {
                        JSONObject mExtractROOT = new JSONObject(responseExtract);
                        JSONObject mExtractJSON = mExtractROOT.getJSONObject(AppoConstants.DATA);
                        if (mExtractJSON.getString(Constants.ERRORCODE).equalsIgnoreCase("0")) {
                            if (mExtractJSON.has("documentNumber")) {
                                if (mExtractJSON.has("documentNumber")) {
                                    tvIdNo.setText(mExtractJSON.getString("documentNumber"));
                                }
                                if (mExtractJSON.has("dateOfExpiry")) {
                                    //mExpiry = mExtractJSON.getString("dateOfExpiry");
                                    edtExpirayDate.setText("Expiry Date: " + mExpiry);
                                }

                                if (mExtractJSON.has("dateOfBirth")) {
                                    mDob = mExtractJSON.getString("dateOfBirth");
                                    edtDob.setText("Dob : " + mDob);
                                }

                                if (mExtractJSON.has("name")) {
                                    txtUserName.setText(mExtractJSON.getString("name"));
                                } else if (mExtractJSON.has("surname")) {
                                    txtUserName.setText(mExtractJSON.getString("surname"));
                                }

                            }

                        } else {
                            showErrorExtract();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
*/
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                closeLoadig();
                //Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(WalletCardActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        /*AndroidNetworking.post("http://3.141.54.113:8889/ocr/idcard_base64")
                .addJSONObjectBody(mRoot)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        close();
                        Log.e(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        close();
                        Log.e(TAG, "onError: " + anError.getErrorBody());
                        Log.e(TAG, "onError: " + anError.getErrorDetail());

                    }
                });*/

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

    private void getUserDetailsForProfile() {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject result = index.getJSONObject(AppoConstants.RESULT);
            String userId = result.getString(AppoConstants.ID);
            areacode = result.getString(AppoConstants.PHONECODE);
            mobileNumber = result.getString(AppoConstants.MOBILENUMBER);
            getProfileDetails();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void showLoading() {
        dialog = new ProgressDialog(WalletCardActivity.this);
        dialog.setMessage(getString(R.string.info_please_wait_dots));
        dialog.show();
    }

    public void closeLoadig() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    private void getProfileDetails() {
        dialog = new ProgressDialog(WalletCardActivity.this);
        dialog.setMessage(getString(R.string.info_read_user_details));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getProfileDetails(Long.parseLong(mobileNumber), Integer.parseInt(areacode), bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    JsonObject body = response.body();
                    String res = body.toString();

                    //String res = new Gson().toJson(response.body());
                    //Log.e(TAG, "onResponse: getprofile :" + res);
                    DataVaultManager.getInstance(WalletCardActivity.this).saveUserDetails(res);
                    getCurrencyCode();
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(WalletCardActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(WalletCardActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(WalletCardActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }

    private void getCurrencyCode() {
        dialog = new ProgressDialog(WalletCardActivity.this);
        dialog.setMessage(getString(R.string.info_get_curreny_code));
        dialog.show();

        mainAPIInterface.getCurrencyResponse().enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    result = response.body().getResult();
                    readUserAccounts();
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private void readUserAccounts() {
        mListAccount = new ArrayList<>();
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);

        ////Log.e(TAG, "readUserAccounts: " + vaultValue);

        try {
            JSONObject root = new JSONObject(vaultValue);
            JSONObject objResult = root.getJSONObject(AppoConstants.RESULT);
            JSONObject objCustomerDetails = objResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            JSONArray arrCustomerAccount = objCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            for (int i = 0; i < arrCustomerAccount.length(); i++) {
                JSONObject index = arrCustomerAccount.getJSONObject(i);
                AccountModel model = new AccountModel();
                model.setAccountnumber(index.getString(AppoConstants.ACCOUNTNUMBER));
                String mIncryptAccount = getAccountNumber(index.getString(AppoConstants.ACCOUNTNUMBER));
                model.setAccountEncrypt(mIncryptAccount);
                //Log.e(TAG, "readUserAccounts: encrypt ::  " + mIncryptAccount);
                if (index.has(AppoConstants.ACCOUNTSTATUS)) {
                    //Log.e(TAG, "readUserAccounts: AccountStatus : " + index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                } else {
                    //Log.e(TAG, "readUserAccounts: AccountStatus : " + "null");
                    model.setAccountstatus("");
                }
                model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                model.setCurrentbalance(index.getString(AppoConstants.CURRENTBALANCE));
                mListAccount.add(model);
            }
            if (mListAccount.size() > 0) {
                tvFromAccount.setText(mListAccount.get(0).getAccountnumber() + "-" + mListAccount.get(0).getCurrencyCode());
                getCountryCode();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void getCountryCode() {

        dialog = new ProgressDialog(WalletCardActivity.this);
        dialog.setMessage(getString(R.string.info_please_wait));
        dialog.show();
        mainAPIInterface.getCountryCode().enqueue(new Callback<CountryCodeResponse>() {
            @Override
            public void onResponse(Call<CountryCodeResponse> call, Response<CountryCodeResponse> response) {
                dialog.dismiss();
                //Log.e(TAG, "onResponse: " + new Gson().toJson(response));
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        //mListMain = new ArrayList<>();
                        //mListMain=response.body().getResult();
                        mListCountry = new ArrayList<>();
                        mListCountry = response.body().getResult();
                        //Result result = new Result();


                        //initCountryCode();
                    }

                }
            }

            @Override
            public void onFailure(Call<CountryCodeResponse> call, Throwable t) {
                dialog.dismiss();
                //Log.e("tag", t.getMessage().toString());
            }
        });


    }

    private String getCurrency(String param) {
        String res = null;
        for (int i = 0; i < result.size(); i++) {
            String sid = result.get(i).getId().toString();
            if (sid.equals(param)) {
                res = result.get(i).getCurrencyCode();
                break;
            }
        }
        return res;
    }

    private String getAccountNumber(String string) {
        char[] chars = string.toCharArray();
        String strTemp = "";
        int cout = 0;
        for (int i = chars.length - 1; i >= 0; i--) {
            cout = cout + 1;
            if (cout >= 4 && cout < 9) {
                strTemp = strTemp + "X";
            } else {
                String temp = String.valueOf(chars[i]);
                strTemp = strTemp + temp;
            }
        }
        ////Log.e(TAG, "getAccountNumber: atLast :: " + strTemp);
        StringBuilder builder = new StringBuilder();
        builder.append(strTemp);
        builder = builder.reverse();
        ////Log.e(TAG, "getAccountNumber: after reverse ::  " + builder.toString());
        return String.valueOf(builder);
    }

    @Override
    public void onCurrencySelected(int id, String currencyCode) {
        mCurrencyId = id;
        mCurrencyCode = currencyCode;
    }

    @Override
    public void onConfirmSelect() {
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
        //tvSelectBankCurrency.setText(mCurrencyCode);
        //   getBaseRate();
    }


    @Override
    public void onPinConfirm(String pin) {
        if (bottotmPinFragment != null)
            bottotmPinFragment.dismiss();
        getFundCommissions(pin);
    }

    /**
     * {"status":200,"message":"success","result":-1}
     */


}
