package com.stuffer.stuffers.activity.lunex_card;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.MobileRechargeActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.activity.wallet.WalletActivity;
import com.stuffer.stuffers.adapter.recyclerview.GiftAmountAdapter;
import com.stuffer.stuffers.adapter.recyclerview.GiftCarrierAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.CustomCountryListener;
import com.stuffer.stuffers.communicator.GiftRequestListener;
import com.stuffer.stuffers.communicator.PhoneNoListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemClickListener2;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomPhoneFragments;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.fragments.dialog.CustomCountryDialogFragment;
import com.stuffer.stuffers.models.lunex_giftcard.GiftProductList;
import com.stuffer.stuffers.models.output.CustomArea;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class LunexGiftActivity extends AppCompatActivity implements View.OnClickListener, TransactionPinListener, CustomCountryListener, RecyclerViewRowItemCLickListener, RecyclerViewRowItemClickListener2, GiftRequestListener, PhoneNoListener {
    private static final String TAG = "LunexGiftActivity";
    private RecyclerView rvGiftProduct, rvGiftAmount;
    private MyTextView tvAreaCode;
    private MyEditText edPhoneNumber;
    private ImageView ivContactList;
    ArrayList<CustomArea> mListArea;
    private String mAraaCode;
    private ProgressDialog mProgress;
    MainAPIInterface mainAPIInterfaceNode;
    List<GiftProductList.Product> mListGift;
    private GiftCarrierAdapter giftCarrierAdapter;
    List<GiftProductList.Amount> mListAmout;
    private GiftAmountAdapter giftAmountAdapter;
    private String senderAccountnumber, senderAccountstatus, senderCurrencyid, senderCurrentbalance, senderId, senderReserveamount, senderCurrencyCode, currentbalance;
    private BottotmPinFragment fragmentBottomSheet;
    private AlertDialog dialogPayment;
    private String userTransactionPin;
    private ProgressDialog dialog;
    private JSONObject jsonCommission;
    private float bankfees, newamount, processingfees, finalamount, finalAmount1 = 0, chargesAmount = 0;
    private int mGiftCardPos;
    private int mGiftAmtPos;
    private Float mGiftAmount;
    private MainAPIInterface mainAPIInterface;
    private String mDesCurrency;
    private JSONArray resultConversion;
    private String currencyId;
    private BottomPhoneFragments mBottomPhoneFragments;
    private String mCountryCode, mRecPhNumber;
    private PhoneNumberUtil phoneUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunex_gift);
        mainAPIInterfaceNode = ApiUtils.getAPIServiceNode();
        mainAPIInterface = ApiUtils.getAPIService();
        rvGiftProduct = (RecyclerView) findViewById(R.id.rvGiftProduct);
        rvGiftAmount = (RecyclerView) findViewById(R.id.rvGiftAmount);
        tvAreaCode = (MyTextView) findViewById(R.id.tvAreaCode);
        edPhoneNumber = (MyEditText) findViewById(R.id.edPhoneNumber);
        ivContactList = (ImageView) findViewById(R.id.ivContactList);
        tvAreaCode.setOnClickListener(this);
        //rvGiftProduct.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
        rvGiftProduct.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvGiftAmount.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        setupActionBar();
        //setListOfAreas();
        mAraaCode = "1";
        getAvailableGiftCards();

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
    public void onClick(View view) {
        if (view.getId() == R.id.tvAreaCode) {
            CustomCountryDialogFragment customCountryDialogFragment = new CustomCountryDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(AppoConstants.TITLE, "Please Select Carrier");
            bundle.putParcelableArrayList(AppoConstants.INFO, mListArea);
            customCountryDialogFragment.setArguments(bundle);
            customCountryDialogFragment.setCancelable(false);
            customCountryDialogFragment.show(getSupportFragmentManager(), customCountryDialogFragment.getTag());
        }
    }

    private void setListOfAreas() {
        mListArea = new ArrayList<>();
        String data = Helper.AREA_CODE_GIFT_JSON;
        try {
            JSONArray rowAreas = new JSONArray(data);
            for (int i = 0; i < rowAreas.length(); i++) {
                JSONObject index = rowAreas.getJSONObject(i);
                CustomArea customArea = new CustomArea(index.getString(AppoConstants.AREACODE), index.getString(AppoConstants.AREACODE_WITH_NAME));
                mListArea.add(customArea);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCustomCountryCodeSelect(String code) {
        tvAreaCode.setText("+" + code);
        mAraaCode = code;
        getAvailableGiftCards();
    }

    private void getAvailableGiftCards() {
        showLoading();
        JsonObject index = new JsonObject();
        index.addProperty("countryCode", "1");
        mainAPIInterfaceNode.getAllGiftCardList(index).enqueue(new Callback<GiftProductList>() {
            @Override
            public void onResponse(@NotNull Call<GiftProductList> call, @NotNull Response<GiftProductList> response) {
                //Log.e(TAG, "onResponse: called");
                hideLoading();
                if (response.isSuccessful()) {
                    try {
                        mListGift = response.body().getProducts();
                        boolean status = false;
                        if (mListGift.size() > 0) {
                            for (int i = 0; i < mListGift.size(); i++) {
                                if (mListGift.get(i).getProductType().equalsIgnoreCase("GIFT_CARD")) {
                                    status = true;
                                } else {
                                    status = false;
                                }
                            }
                            if (status) {
                                getCarrierNames();
                            } else {
                                Toast.makeText(LunexGiftActivity.this, "No Gift Card Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(Call<GiftProductList> call, Throwable t) {
                hideLoading();
                Log.e(TAG, "onFailure: called" + t.getMessage());

            }
        });
    }

    private void getCarrierNames() {
        giftCarrierAdapter = new GiftCarrierAdapter(LunexGiftActivity.this, mListGift, 1);
        rvGiftProduct.setAdapter(giftCarrierAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(rvGiftProduct.findViewHolderForAdapterPosition(0)).itemView.performClick();
            }
        }, 500);
    }


    private void showLoading() {
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
        }
        mProgress.setMessage(getString(R.string.info_please_wait_dots));
        mProgress.show();
    }

    private void hideLoading() {
        mProgress.dismiss();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(getString(R.string.info_brand_vouchers));

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }

    @Override
    public void onRowItemClick(int pos) {

        giftCarrierAdapter.selected(pos);
        mGiftCardPos = pos;
        showGiftAmout(pos);
    }

    private void showGiftAmout(int pos) {
        mListAmout = mListGift.get(pos).getAmounts();
        giftAmountAdapter = new GiftAmountAdapter(this, mListAmout, 1);
        rvGiftAmount.setAdapter(giftAmountAdapter);
    }

    @Override
    public void onRowItemClick2(int pos) {
        giftAmountAdapter.selected(pos);
    }

    @Override
    public void onGiftRequest(int pos) {
        Log.e(TAG, "onGiftRequest: gift amount pos : " + pos);
        mGiftAmtPos = pos;
        mDesCurrency = mListAmout.get(mGiftAmtPos).getDestCurr();
        mGiftAmount = mListAmout.get(mGiftAmtPos).getAmt();
        showContanctDialog();

    }

    private void showContanctDialog() {
        mBottomPhoneFragments = new BottomPhoneFragments();
        mBottomPhoneFragments.show(getSupportFragmentManager(), mBottomPhoneFragments.getTag());
        mBottomPhoneFragments.setCancelable(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppoConstants.TOPUP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                senderAccountnumber = data.getStringExtra(AppoConstants.ACCOUNTNUMBER);
                senderAccountstatus = data.getStringExtra(AppoConstants.ACCOUNTSTATUS);
                senderCurrencyid = data.getStringExtra(AppoConstants.CURRENCYID);
                senderCurrentbalance = data.getStringExtra(AppoConstants.CURRENTBALANCE);
                senderId = data.getStringExtra(AppoConstants.ID);
                senderReserveamount = data.getStringExtra(AppoConstants.RESERVEAMOUNT);
                senderCurrencyCode = data.getStringExtra(AppoConstants.CURRENCYCODE);
                currentbalance = data.getStringExtra(AppoConstants.CURRENTBALANCE);
                getCurrencyConversion();

            }
        } else if (resultCode == Activity.RESULT_OK) {
            //  Log.e(TAG, "onActivityResult: Pick Contact NUmber :: " + data.getStringExtra(AppoConstants.INFO));
            String mMobileNumber = data.getStringExtra(AppoConstants.INFO);

            try {
                // phone must begin with '+'
                if (phoneUtil == null) {
                    phoneUtil = PhoneNumberUtil.createInstance(LunexGiftActivity.this);
                }
                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(mMobileNumber, "");
                int countryCode = numberProto.getCountryCode();
                long nationalNumber = numberProto.getNationalNumber();
                mMobileNumber=""+nationalNumber;
                mBottomPhoneFragments.setReceiverContact(mMobileNumber);
                //  Log.e("code", "code " + countryCode);
                //  Log.e("code", "national number " + nationalNumber);

            } catch (NumberParseException e) {
                System.err.println("NumberParseException was thrown: " + e.toString());
            }

        }
    }

    private void getCurrencyConversion() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.info_conversion_rate));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        //String mtype = mListProduct.get(mCarrierPosition).getAmounts().get(mRechargePosition).getDestCurr();
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getCurrencyConversions(mDesCurrency, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ////Log.e(TAG, "onResponse: conversion : " + new Gson().toJson(response.body()));
                    String src = new Gson().toJson(response.body());
                    try {
                        JSONObject jsonConversion = new JSONObject(src);
                        if (jsonConversion.getString(AppoConstants.MESSAGE).equals(AppoConstants.SUCCESS)) {
                            resultConversion = jsonConversion.getJSONArray(AppoConstants.RESULT);
                            if (resultConversion.length() > 0) {
                                performConversion();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(LunexGiftActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(LunexGiftActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(LunexGiftActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                ////Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private void performConversion() {

        try {
            JSONObject indexZero = resultConversion.getJSONObject(0);
            float conversionrate = Float.parseFloat(indexZero.getString(AppoConstants.CONVERSIONRATE));
            float newAmount = (float) (conversionrate * mGiftAmount);
            float currentbalance = Float.parseFloat(senderCurrentbalance);
            currencyId = indexZero.getString(AppoConstants.CURRENCYID);
            if (currentbalance < newAmount) {
                showBalanceError();
            } else {

                showBottomDialog(newAmount);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void checkAvailabeBalance() {
        if (Float.parseFloat(currentbalance) < mGiftAmount) {
            showBalanceError();
        } else {
            //processPayment(newAmount);
            showBottomDialog(mGiftAmount);
        }
    }

    private void showBalanceError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_balance_error, null);

        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPayment.dismiss();
            }
        });


        builder.setView(dialogLayout);

        dialogPayment = builder.create();

        dialogPayment.setCanceledOnTouchOutside(false);

        dialogPayment.show();
    }

    private void showBottomDialog(float newAmount) {
        fragmentBottomSheet = new BottotmPinFragment();
        fragmentBottomSheet.show(getSupportFragmentManager(), fragmentBottomSheet.getTag());
        fragmentBottomSheet.setCancelable(false);
    }

    @Override
    public void onPinConfirm(String pin) {
        if (fragmentBottomSheet != null)
            fragmentBottomSheet.dismiss();
        getCommissions(pin.trim(), mGiftAmount);
    }

    private void getCommissions(String transaction, final float newAmount) {
        userTransactionPin = transaction;
        ////Log.e(TAG, "getCommissions: pin : " + transaction);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.info_conversion_rate));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getAllTypeCommissions(bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ////Log.e(TAG, "onResponse: commissions : " + new Gson().toJson(response.body()));
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    String res = new Gson().toJson(response.body());
                    try {
                        jsonCommission = new JSONObject(res);
                        calculateCommission(newAmount);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(LunexGiftActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(LunexGiftActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(LunexGiftActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog != null) {
                    dialogPayment.dismiss();
                }
                dialog.dismiss();
                ////Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });


    }

    private void calculateCommission(float newAmountParam) {
        try {
            JSONObject result = jsonCommission.getJSONObject(AppoConstants.RESULT);
            float bankcomission = Float.parseFloat(result.getString(AppoConstants.BANKCOMMISSION));
            float processingcomission = Float.parseFloat(result.getString(AppoConstants.PROCESSINGFEES));
            float flatbankcomission = Float.parseFloat(result.getString(AppoConstants.FLATBANKCOMMISSION));
            float flatprocessingcomission = Float.parseFloat(result.getString(AppoConstants.FLATPROCESSINGFEES));
            float fundamount = newAmountParam;
            float taxpercentage = Float.parseFloat(result.getString(AppoConstants.TAXPERCENTAGE));
            bankfees = getTwoDecimal(bankcomission * fundamount);
            newamount = fundamount + bankfees;
            processingfees = getTwoDecimal(fundamount * processingcomission);
            finalamount = newamount + processingfees;
            bankfees = getTwoDecimal(bankfees + flatbankcomission);
            processingfees = getTwoDecimal(processingfees + flatprocessingcomission);
            float flatfees = getTwoDecimal(flatbankcomission + flatprocessingcomission);
            finalamount = (finalamount + flatfees);
            chargesAmount = getTwoDecimal(fundamount * (taxpercentage / 100.0f));
            finalAmount1 = 0;
            finalAmount1 = fundamount + chargesAmount + bankfees;
            finalAmount1 = getTwoDecimal(finalAmount1);
            ////Log.e(TAG, "calculateCommission: recharge amount : " + fundamount);
            ////Log.e(TAG, "calculateCommission: bankfees : " + bankfees);
            ////Log.e(TAG, "calculateCommission: processingfees : " + processingfees);
            ////Log.e(TAG, "calculateCommission: flatfees : " + flatfees);
            ////Log.e(TAG, "calculateCommission: amount before 7 per " + finalamount);
            float per7 = getTwoDecimal(fundamount * (07.f / 100.0f));
            ////Log.e(TAG, "calculateCommission:  7 per of " + fundamount + " is : " + per7);
            ////Log.e(TAG, "calculateCommission: final amount : " + (finalAmount1));
            showYouAboutToPay(newAmountParam);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public float getTwoDecimal(float params) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        return Float.parseFloat(df.format(params));
    }

    private void showYouAboutToPay(final float newAmountParam) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_about_to_pay, null);

        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvTitle = dialogLayout.findViewById(R.id.tvTitle);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnNo = dialogLayout.findViewById(R.id.btnNo);
        tvTitle.setText("Gift Card Payment");
        String boldText = "<font color=''><b>" + finalAmount1 + "</b></font>" + " " + "<font color=''><b>" + senderCurrencyCode + "</b></font>";
        String paymentAmount = getString(R.string.recharge_partial_pay1) + " " + boldText + " " + getString(R.string.gift_card__pay);

        tvInfo.setText(Html.fromHtml(paymentAmount));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment(newAmountParam);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPayment.dismiss();
            }
        });

        builder.setView(dialogLayout);
        dialogPayment = builder.create();
        dialogPayment.setCanceledOnTouchOutside(false);
        dialogPayment.show();
    }

    private void makePayment(float newAmountParam) {
        if (dialogPayment != null) {
            dialogPayment.dismiss();
            dialogPayment = null;
        }
        JsonObject sentParams = new JsonObject();
        sentParams.addProperty(AppoConstants.AMOUNT, String.valueOf(mGiftAmount));
        sentParams.addProperty(AppoConstants.CARRIER, mListGift.get(mGiftCardPos).getCarrier());
        sentParams.addProperty(AppoConstants.CCEXP, (String) null);
        sentParams.addProperty(AppoConstants.CCNUMBER, (String) null);
        sentParams.addProperty(AppoConstants.CHARGES, 0.0);
        sentParams.addProperty(AppoConstants.CVV, (String) null);
        sentParams.addProperty(AppoConstants.FEES, 0.0);
        sentParams.addProperty(AppoConstants.TAXES, chargesAmount);

        sentParams.addProperty(AppoConstants.FROMCURRENCY, currencyId);
        sentParams.addProperty(AppoConstants.FROMCURRENCYCODE, senderCurrencyCode);
        sentParams.addProperty(AppoConstants.FULLNAME, (String) null);
        sentParams.addProperty(AppoConstants.ORIGINALAMOUNT, finalAmount1);
        sentParams.addProperty(AppoConstants.PAYAMOUNT, finalAmount1);
        sentParams.addProperty(AppoConstants.PRODUCTCODE, mListGift.get(mGiftCardPos).getProductCode());
        sentParams.addProperty(AppoConstants.RECEIVERAREACODE, mCountryCode);
        sentParams.addProperty(AppoConstants.RECEIVERMOBILENUMBER, mRecPhNumber);
        sentParams.addProperty(AppoConstants.SENDERACCOUNTNUMBER, senderAccountnumber);
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        try {
            JSONObject jsonUser = new JSONObject(vaultValue);
            JSONObject objResult = jsonUser.getJSONObject(AppoConstants.RESULT);
            sentParams.addProperty(AppoConstants.SENDERAREACODE, objResult.getString(AppoConstants.PHONECODE));
            sentParams.addProperty(AppoConstants.SENDERMOBILENUMBER, objResult.getString(AppoConstants.MOBILENUMBER));
            String senderName = objResult.getString(AppoConstants.FIRSTNAME) + " " + objResult.getString(AppoConstants.LASTNAME);
            sentParams.addProperty(AppoConstants.SENDERNAME, senderName);
            sentParams.addProperty(AppoConstants.TRANSACTIONPIN, userTransactionPin);
            sentParams.addProperty(AppoConstants.USERID, objResult.getString(AppoConstants.ID));
            Log.e(TAG, "makePayment: " + sentParams.toString());
            makeRechargePayment(sentParams);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void makeRechargePayment(JsonObject sentParams) {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.info_sending_request));
        dialog.show();
        String accessToken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        mainAPIInterface.postSentLunexGiftCard(sentParams, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    try {
                        JSONObject json = new JSONObject(res);
                        if (json.getString(AppoConstants.RESULT).equalsIgnoreCase(AppoConstants.SUCCESS)) {
                            showSuccessDialog();
                        } else {
                            showErrorDialog(json.getString(AppoConstants.RESULT));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(LunexGiftActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(LunexGiftActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(LunexGiftActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                ////Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }


    private void showErrorDialog(String params) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_coomon_error, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvTitle = dialogLayout.findViewById(R.id.tvTitle);
        tvTitle.setText("Gift Card Payment");
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        tvInfo.setText(params);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPayment.dismiss();
            }
        });

        builder.setView(dialogLayout);

        dialogPayment = builder.create();

        dialogPayment.setCanceledOnTouchOutside(false);

        dialogPayment.show();
    }


    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_success_topup, null);

        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvTitleCommon = dialogLayout.findViewById(R.id.tvTitleCommon);
        tvTitleCommon.setText("Gift Card Payment");
        MyTextView tvSuccess = dialogLayout.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Thank You!" + "\n" + "You Gift Card Has Been Successfully Sent.");
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //invalidateDetails();
                updateWallet();
            }
        });

        builder.setView(dialogLayout);

        dialogPayment = builder.create();

        dialogPayment.setCanceledOnTouchOutside(false);

        dialogPayment.show();
    }

    public void updateWallet() {
        if (dialogPayment != null) {
            dialogPayment.dismiss();
        }
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void getPhoneNoWithCountryCode(String countryCode, String phone_number) {
        mCountryCode = countryCode;
        mRecPhNumber = phone_number;
        if (mBottomPhoneFragments != null)
            mBottomPhoneFragments.dismiss();
        Intent intentWallet = new Intent(this, WalletActivity.class);
        startActivityForResult(intentWallet, AppoConstants.TOPUP_REQUEST_CODE);
    }
}