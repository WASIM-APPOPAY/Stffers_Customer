package com.stuffer.stuffers.activity.lunex_card;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.contact.ContactDemoActivity;
import com.stuffer.stuffers.activity.lunex_card.LunexItemsActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.activity.wallet.WalletActivity;
import com.stuffer.stuffers.adapter.recyclerview.LunexAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.CustomCountryListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.communicator.TransactionPinListener;
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
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class LunexItemsActivity extends AppCompatActivity implements CustomCountryListener, RecyclerViewRowItemCLickListener, TransactionPinListener {

    private MyTextViewBold tvItemTitle;
    private ImageView ivContactList;
    private MyEditText edtphone_number;
    private static final String TAG = "LunexItemsActivity";
    private PhoneNumberUtil phoneUtil;
    private ArrayList<CustomArea> mListArea;
    private MyTextView tvAreaCode;
    private String mAraaCode = "";
    private RecyclerView rvGiftsAmount;
    private AlertDialog dialogPayment;
    private String senderAccountnumber, senderAccountstatus, senderCurrencyid, senderCurrentbalance, senderId, senderReserveamount, senderCurrencyCode;
    private ProgressDialog dialog;
    private List<GiftProductList.Amount> mListAmount;
    private List<GiftProductList.Product> mListProduct;
    private float mRechargeAmount;
    private String mDesCurrency;
    private MainAPIInterface mainAPIInterface;
    private JSONArray resultConversion;
    private float newAmount;
    private String currencyId;
    private BottotmPinFragment fragmentBottomSheet;
    private JSONObject jsonCommission;
    private String userTransactionPin;
    private float processingfees;
    private float bankfees;
    private float newamount;
    private float finalamount;
    private float chargesAmount;
    private float finalAmount1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunex_items);
        mainAPIInterface = ApiUtils.getAPIService();
        tvAreaCode = (MyTextView) findViewById(R.id.tvAreaCode);
        tvItemTitle = findViewById(R.id.tvItemTitle);
        ivContactList = findViewById(R.id.ivContactList);
        edtphone_number = findViewById(R.id.edtphone_number);
        rvGiftsAmount = (RecyclerView) findViewById(R.id.rvGiftsAmount);
        rvGiftsAmount.setLayoutManager(new LinearLayoutManager(LunexItemsActivity.this));
        setupActionBar();
        String stringExtra = getIntent().getStringExtra(AppoConstants.PRODUCT);
        ////Log.e(TAG, "onCreate: "+stringExtra );
        Gson gson = new Gson();
        GiftProductList.Product product = gson.fromJson(stringExtra, new TypeToken<GiftProductList.Product>() {
        }.getType());

        mListProduct = new ArrayList<>();
        mListProduct.add(0, product);
        tvItemTitle.setText(mListProduct.get(0).getCarrier());
        ivContactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentContact = new Intent(LunexItemsActivity.this, ContactDemoActivity.class);
                startActivityForResult(intentContact, AppoConstants.PICK_CONTACT);
            }
        });

        tvAreaCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomCountryDialogFragment customCountryDialogFragment = new CustomCountryDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(AppoConstants.TITLE, "Please Select Carrier");
                bundle.putParcelableArrayList(AppoConstants.INFO, mListArea);
                customCountryDialogFragment.setArguments(bundle);
                customCountryDialogFragment.setCancelable(false);
                customCountryDialogFragment.show(getSupportFragmentManager(), customCountryDialogFragment.getTag());
            }
        });
        setListOfAreas();

        mListAmount = mListProduct.get(0).getAmounts();
        LunexAdapter adapter = new LunexAdapter(mListAmount, LunexItemsActivity.this);
        rvGiftsAmount.setAdapter(adapter);


    }

    private void setListOfAreas() {
        mListArea = new ArrayList<>();
        String data = Helper.AREA_CODE_JSON2;
        try {
            JSONArray rowAreas = new JSONArray(data);
            for (int i = 0; i < rowAreas.length(); i++) {
                JSONObject index = rowAreas.getJSONObject(i);
                CustomArea customArea = new CustomArea(index.getString(AppoConstants.AREACODE), index.getString(AppoConstants.AREACODE_WITH_NAME),"");
                mListArea.add(customArea);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                ////Log.e(TAG, "onActivityResult: code will add here");
                /*if (senderCurrencyCode.equals("USD")) {
                    getCurrencyConversion();
                }*/
                getCurrencyConversion();
                //getCurrencyConversion();
            }
        } else if (requestCode == AppoConstants.PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
              //  Log.e(TAG, "onActivityResult: Pick Contact NUmber :: " + data.getStringExtra(AppoConstants.INFO));
                String mMobileNumber = data.getStringExtra(AppoConstants.INFO);
                edtphone_number.setText(mMobileNumber);
                try {
                    // phone must begin with '+'
                    if (phoneUtil == null) {
                        phoneUtil = PhoneNumberUtil.createInstance(LunexItemsActivity.this);
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
    public void onCustomCountryCodeSelect(String code) {
        tvAreaCode.setText("+" + code);
        mAraaCode = code;

    }

    @Override
    public void onRowItemClick(int pos) {
        if (AppoPayApplication.isNetworkAvailable(LunexItemsActivity.this)) {
            if (mAraaCode.isEmpty()) {
                Toast.makeText(this, "Please select receiver area code first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edtphone_number.getText().toString().trim().isEmpty()) {
                //Toast.makeText(this, "please enter receiver mobile number", Toast.LENGTH_SHORT).show();
                edtphone_number.setError("please enter receiver mobile number");
                edtphone_number.requestFocus();
                return;
            }
            //mRechargeAmount = (float) desAmount;
            mRechargeAmount = (float) mListAmount.get(pos).getAmt();
            mDesCurrency = mListAmount.get(pos).getDestCurr();

          //  Log.e(TAG, "onReceiverClick: mRechargeAmount :: " + mRechargeAmount);
          //  Log.e(TAG, "onReceiverClick: mDesCurrency :: " + mDesCurrency);
            closeKeyboard();
            showPaymentTypeDialog();


        } else {
            Toast.makeText(this, "" + getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void closeKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void showPaymentTypeDialog() {
        closeKeyboard();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dilaog_payment_type, null);


        MyButton btnCard = dialogLayout.findViewById(R.id.btnCard);
        MyButton btnWallet = dialogLayout.findViewById(R.id.btnWallet);
        MyTextView tvTitle = dialogLayout.findViewById(R.id.tvTitle);
        tvTitle.setText("Gift Card Payment");
        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCardDetails();
            }
        });

        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWalletScreen();
            }
        });
        builder.setView(dialogLayout);

        dialogPayment = builder.create();

        dialogPayment.show();
    }

    private void getCardDetails() {
        if (dialogPayment != null) {
            dialogPayment.dismiss();
        }
    /*    Intent intentCard = new Intent(LunexItemsActivity.this, TopupCardActivity.class);
        startActivity(intentCard);*/
    }

    private void showWalletScreen() {
        if (dialogPayment != null) {
            dialogPayment.dismiss();
            dialogPayment = null;
        }
        Intent intentWallet = new Intent(this, WalletActivity.class);
        startActivityForResult(intentWallet, AppoConstants.TOPUP_REQUEST_CODE);
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
                        DataVaultManager.getInstance(LunexItemsActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(LunexItemsActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(LunexItemsActivity.this, SignInActivity.class);
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
            //float newAmount = (float) (conversionrate * Float.parseFloat(edtAmount.getText().toString().trim()));
            newAmount = (float) (conversionrate * mRechargeAmount);
            float currentbalance = Float.parseFloat(senderCurrentbalance);
            currencyId = indexZero.getString(AppoConstants.CURRENCYID);


            if (currentbalance < newAmount) {
                showBalanceError();
            } else {
                //processPayment(newAmount);
                showBottomDialog(newAmount);
            }

            //processPayment(newAmount);

        } catch (JSONException e) {
            e.printStackTrace();
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
        getCommissions(pin.trim(), newAmount);
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
                    if (dialogPayment != null) {
                        dialogPayment.dismiss();
                    }
                    String res = new Gson().toJson(response.body());
                    try {
                        jsonCommission = new JSONObject(res);
                        calculateCommission(newAmount);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (dialogPayment != null) {
                        dialogPayment.dismiss();
                    }
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(LunexItemsActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(LunexItemsActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(LunexItemsActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialogPayment != null) {
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
            //float fundamount = Float.parseFloat(edtAmount.getText().toString().trim());
            //float fundamount = (float) mRechargeAmount;//Float.parseFloat(edtAmount.getText().toString().trim());
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
        //String paymentAmount = getString(R.string.recharge_partial_pay1) + " " + finalamount + " " + senderCurrencyCode + " " + getString(R.string.recharge_partial_pay2);
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
        //sentParams.addProperty(AppoConstants.AMOUNT, Float.parseFloat(edtAmount.getText().toString().trim()));

        //sentParams.addProperty(AppoConstants.AMOUNT, getTwoDecimal(newAmountParam));
        sentParams.addProperty(AppoConstants.AMOUNT, String.valueOf(mRechargeAmount));
        sentParams.addProperty(AppoConstants.CARRIER, mListProduct.get(0).getProductName());
        sentParams.addProperty(AppoConstants.CCEXP, (String) null);
        sentParams.addProperty(AppoConstants.CCNUMBER, (String) null);
        sentParams.addProperty(AppoConstants.CHARGES, bankfees);
        sentParams.addProperty(AppoConstants.CVV, (String) null);
        sentParams.addProperty(AppoConstants.FEES, processingfees);
        sentParams.addProperty(AppoConstants.TAXES, chargesAmount);

        sentParams.addProperty(AppoConstants.FROMCURRENCY, currencyId);
        sentParams.addProperty(AppoConstants.FROMCURRENCYCODE, senderCurrencyCode);
        sentParams.addProperty(AppoConstants.FULLNAME, (String) null);
        sentParams.addProperty(AppoConstants.ORIGINALAMOUNT, finalAmount1);
        sentParams.addProperty(AppoConstants.PAYAMOUNT, finalAmount1);
        sentParams.addProperty(AppoConstants.PRODUCTCODE, mListProduct.get(0).getProductCode());
        sentParams.addProperty(AppoConstants.RECEIVERAREACODE, mAraaCode);
        sentParams.addProperty(AppoConstants.RECEIVERMOBILENUMBER, edtphone_number.getText().toString().trim());
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
            ////Log.e(TAG, "makePayment: " + sentParams.toString());
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
                    ////Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
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
                        DataVaultManager.getInstance(LunexItemsActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(LunexItemsActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(LunexItemsActivity.this, SignInActivity.class);
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





    /*public ArrayList<User> getMyUsers() {
            String savedUserPref = sharedPreferenceHelper.getStringPreference(USER_MY_CACHE);
            if (savedUserPref != null) {
                return gson.fromJson(savedUserPref, new TypeToken<ArrayList<User>>() {
                }.getType());
            } else {
                return new ArrayList<User>();
            }
        }*/

    @Override
    protected void onDestroy() {
        closeKeyboard();
        super.onDestroy();
    }
}