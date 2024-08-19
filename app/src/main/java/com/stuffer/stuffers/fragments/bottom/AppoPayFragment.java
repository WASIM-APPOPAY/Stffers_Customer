package com.stuffer.stuffers.fragments.bottom;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.earthling.atminput.ATMEditText;
import com.emv.qrcode.core.model.mpm.TagLengthString;
import com.emv.qrcode.decoder.mpm.DecoderMpm;
import com.emv.qrcode.model.mpm.AdditionalDataField;
import com.emv.qrcode.model.mpm.AdditionalDataFieldTemplate;
import com.emv.qrcode.model.mpm.MerchantAccountInformation;
import com.emv.qrcode.model.mpm.MerchantAccountInformationTemplate;
import com.emv.qrcode.model.mpm.MerchantPresentedMode;
import com.emv.qrcode.model.mpm.UnreservedTemplate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.InnerPayActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chat.TransferChatActivity;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.fragments.dialog.FromAccountDialogFragment;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppoPayFragment extends Fragment {

    private static final String TAG = "qrcode";
    String merchantWalletAccount;
    private View mView;
    MainAPIInterface mainAPIInterface;
    ProgressDialog dialog;
    String resultScan, merchantAreaCode, merchantMobileNumber, merchantEmailId, fromCurrency, userTransactionPin;
    private List<CurrencyResult> resultCurrency;
    private ArrayList<AccountModel> mListAccount;
    private JSONObject indexMerchant, jsonCommission, objResult;
    private MyTextView tvHeader, tvCodeMobile, tvEmialId, tvIndex5, tvAccountNos;
    private MyTextView tvFromAccount, tvConversionRates, tvAmountCredit;
    private CardView tvCardMerchant;
    private String[] splitScan;
    private ArrayList<String> mListTemp;
    private int fromAccountPosition;
    private FromAccountDialogFragment fromAccountDialogFragment;
    private Dialog dialogMerchant;
    private ATMEditText edAmount;
    private float conversionRates = 0;
    MyButton btnPayNow;
    private float finaamount, bankfees, processingfees = 0, amountaftertax_fees, taxes;
    private int mFromPosition;
    private BottotmPinFragment bottotmPinFragment;
    private Dialog mDialog;
    private File mFileSSort;
    private String valueMerchantName;
    private String valuePhone;
    private String valueCountry;
    private MerchantPresentedMode mDecode;
    private int mWhere = 0;
    private String scanText;

    public AppoPayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_appo_pay, container, false);

        mainAPIInterface = ApiUtils.getAPIService();
        tvCardMerchant = (CardView) mView.findViewById(R.id.tvCardMerchant);
        tvHeader = (MyTextView) mView.findViewById(R.id.tvHeader);
        tvCodeMobile = (MyTextView) mView.findViewById(R.id.tvCodeMobile);
        tvEmialId = (MyTextView) mView.findViewById(R.id.tvEmialId);
        tvIndex5 = (MyTextView) mView.findViewById(R.id.tvIndex5);
        tvAccountNos = (MyTextView) mView.findViewById(R.id.tvAccountNos);

        //tvRequiredFilled = (MyTextView) mView.findViewById(R.id.tvRequiredFilled);
        //String required = getString(R.string.required_filled) + "<font color='#00baf2'>" + "*" + "</font>";
        //tvRequiredFilled.setText(Html.fromHtml(required));
        tvFromAccount = (MyTextView) mView.findViewById(R.id.tvFromAccount);

        edAmount = (ATMEditText) mView.findViewById(R.id.edAmount);
        tvAmountCredit = (MyTextView) mView.findViewById(R.id.tvAmountCredit);

        tvConversionRates = (MyTextView) mView.findViewById(R.id.tvConversionRates);
        btnPayNow = (MyButton) mView.findViewById(R.id.btnPayNow);
        Bundle arguments = this.getArguments();
        scanText = arguments.getString(AppoConstants.MERCHANTSCANCODE);

        mWhere = arguments.getInt(AppoConstants.WHERE, 0);
        ////Log.e("TAG", "onCreateView: scanText " + scanText);
        // resultScan = arguments.getString(AppoConstants.MERCHANTSCANCODE);
        mDecode = DecoderMpm.decode(scanText, MerchantPresentedMode.class);
        //String s = new Gson().toJson(decode);
        resultScan = new Gson().toJson(mDecode);

        ////Log.e("TAG", "onCreateView: onGson : " + resultScan);

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
                    Toast.makeText(getActivity(), getString(R.string.info_selecr_from_account_first), Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String inputAmount = edAmount.getText().toString().trim();
                    if (inputAmount.length() > 0) {
                        if (String.valueOf(inputAmount.charAt(inputAmount.length() - 1)).equalsIgnoreCase(".")) {
                            ////Log.e(TAG, "onTextChanged: no need to do anything" );

                        } else {
                            float twoDecimal = (float) Helper.getTwoDecimal(Float.parseFloat(edAmount.getText().toString().trim()) * conversionRates);
                            tvAmountCredit.setText(String.valueOf(twoDecimal));
                        }
                    }


                } catch (Exception e) {
                    if (edAmount.getText().toString().trim().isEmpty()) {
                        ////Log.e(TAG, "onTextChanged: no toast");
                    } else {
                        Toast.makeText(getActivity(), "invalid format", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //showSuccessDialog("param result");

        //showPayDialogLikeUnion("param result");


        //getMerchantProfile();
        //showMerchantDetails();
        getLatestUserDetails();

        return mView;
    }

    private void getMerchantProfile() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.info_getting_merchant_details));
        dialog.show();
        TagLengthString countryCode1 = mDecode.getCountryCode();
        String value = countryCode1.getValue();
        String area = value;
        String ph = "";


        String accessToken = DataVaultManager.getInstance(getActivity()).getVaultValue(KEY_ACCESSTOKEN);
        try {
            JSONObject mRoot = new JSONObject(resultScan);
            JSONObject unreserveds = mRoot.getJSONObject("unreserveds");
            JSONObject unreserved80 = unreserveds.getJSONObject("80");
            JSONObject unreservedvalue = unreserved80.getJSONObject("value");
            JSONObject contextSpecificData = unreservedvalue.getJSONObject("contextSpecificData");
            JSONObject contextSpecificData03 = contextSpecificData.getJSONObject("03");
            String phoneNumber = contextSpecificData03.getString("value");
            ph = phoneNumber;
        } catch (JSONException e) {
            //Log.e("TAG", "getMerchantProfile: " + resultScan);
            e.printStackTrace();
        }


        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        /*mainAPIInterface.getProfileMerchantDetails(ph, area, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String res = new Gson().toJson(response.body());

                dialog.dismiss();
                if (response.isSuccessful()) {
                    res = new Gson().toJson(response.body());
                    try {
                        indexMerchant = new JSONObject(res);
                        if (indexMerchant.isNull("result")) {
                            ////Log.e(TAG, "onResponse: " + true);
                            Toast.makeText(getActivity(), "Merchant Details does not exists", Toast.LENGTH_LONG).show();
                            btnPayNow.setEnabled(false);
                            btnPayNow.setClickable(false);
                            return;
                        }
                        JSONObject jsonObject = indexMerchant.getJSONObject(AppoConstants.RESULT);
                        if (jsonObject.getBoolean(AppoConstants.ENABLE)) {

                            merchantWalletAccount = Helper.getMerchantWalletAccount(jsonObject);
                            showMerchantDetails();
                            getLatestUserDetails();
                        } else {
                            showMerchantErrorDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getActivity()).saveUserDetails("");
                        DataVaultManager.getInstance(getActivity()).saveUserAccessToken("");
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.putExtra(AppoConstants.WHERE, mWhere);

                        startActivity(intent);
                        getActivity().finish();
                    } else if (response.code() == 400) {
                        ////Log.e(TAG, "onResponse: " + response.toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                ////Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });*/

        showMerchantDetails();
        getLatestUserDetails();


    }

    private void showMerchantErrorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
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
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }


    private void showMerchantDetails() {

        conversionRates = 1;
        tvCardMerchant.setVisibility(View.VISIBLE);
        Log.e(TAG, "showMerchantDetails: " + new Gson().toJson(mDecode));
        String mWhole = new Gson().toJson(mDecode);

        TagLengthString countryCode1 = mDecode.getCountryCode();
        valueCountry = countryCode1.getValue();
        TagLengthString merchantNameTL = mDecode.getMerchantName();
        valueMerchantName = merchantNameTL.getValue();
        AdditionalDataFieldTemplate additionalDataField1 = mDecode.getAdditionalDataField();
        AdditionalDataField value2 = additionalDataField1.getValue();
        TagLengthString terminalLabel1 = value2.getTerminalLabel();
        String mTerminalId = terminalLabel1.getValue();
        ////Log.e(TAG, "showMerchantDetails: "+mTerminalId );
        TagLengthString merchantCity = mDecode.getMerchantCity();
        //String mMerchantCity = merchantCity.getValue();
        ////Log.e(TAG, "showMerchantDetails: "+mMerchantCity );


        String s = new Gson().toJson(mDecode.getMerchantAccountInformation());
        ////Log.e(TAG, "showMerchantDetails: "+s );
        try {
            JSONObject mAccountInfo = new JSONObject(s);
            JSONObject jsonObject15 = mAccountInfo.getJSONObject("15");
            JSONObject value = jsonObject15.getJSONObject("value");
            String value1 = value.getString("value");
            String mMerchantId = value1.substring(value1.length() - 15);
            ////Log.e(TAG, "showMerchantDetails: "+mMerchantId );

            tvEmialId.setText("TID : " + mTerminalId);
            tvIndex5.setText("MID : " + mMerchantId);
            tvIndex5.setVisibility(View.VISIBLE);
            tvHeader.setText(valueMerchantName);
            tvCodeMobile.setText("(+" + valueCountry + ")");// + mMerchantCity.toUpperCase());
            JSONObject mJsonWhole = new JSONObject(mWhole);
            if (mJsonWhole.has("transactionAmount")) {
                TagLengthString transactionAmount = mDecode.getTransactionAmount();
                String transactionValue = transactionAmount.getValue();
                edAmount.setText(transactionValue);
            }


            getLatestUserDetails();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        /*try {
            JSONObject mRoot = new JSONObject(resultScan);
            JSONObject unreserveds = mRoot.getJSONObject("unreserveds");
            JSONObject unreserved80 = unreserveds.getJSONObject("80");
            JSONObject unreservedvalue = unreserved80.getJSONObject("value");
            JSONObject contextSpecificData = unreservedvalue.getJSONObject("contextSpecificData");
            JSONObject contextSpecificData03 = contextSpecificData.getJSONObject("03");
            valuePhone = contextSpecificData03.getString("value");


        } catch (JSONException e) {
            //Log.e("TAG", "getMerchantProfile: " + resultScan);
            e.printStackTrace();
        }

        try {
            JSONObject mRoot = new JSONObject(resultScan);

            tvHeader.setText(valueMerchantName);
            tvCodeMobile.setText("(+" + valueCountry + ") " + valuePhone);
            JSONObject additionalDataField = mRoot.getJSONObject("additionalDataField");
            JSONObject additionalDataFieldValue = additionalDataField.getJSONObject("value");
            JSONObject additionalConsumerDataRequest = additionalDataFieldValue.getJSONObject("additionalConsumerDataRequest");
            String valueMID = additionalConsumerDataRequest.getString("value");
            JSONObject terminalLabel = additionalDataFieldValue.getJSONObject("terminalLabel");
            String valueTID = terminalLabel.getString("value");


            tvEmialId.setText("TID : " + valueTID);
            tvIndex5.setText("MID : " + valueMID);
            tvIndex5.setVisibility(View.VISIBLE);
            if (mRoot.has("transactionAmount")) {
                JSONObject transactionAmount = mRoot.getJSONObject("transactionAmount");
                String value = transactionAmount.getString("value");
                edAmount.setText(value);
                edAmount.setEnabled(false);
                edAmount.setClickable(false);
            } else {
                edAmount.setEnabled(true);
                edAmount.setClickable(true);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    private void getLatestUserDetails() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.info_getting_user_details));
        dialog.show();

        String accessToken = DataVaultManager.getInstance(getActivity()).getVaultValue(KEY_ACCESSTOKEN);
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        JSONObject mIndex = null;
        String ph1 = null;
        String area1 = null;
        try {
            mIndex = new JSONObject(vaultValue);
            JSONObject mResult = mIndex.getJSONObject(AppoConstants.RESULT);
            ph1 = mResult.getString(AppoConstants.MOBILENUMBER);
            area1 = mResult.getString(AppoConstants.PHONECODE);
            //Log.e(TAG, "getLatestUserDetails: " + ph1);
            //Log.e(TAG, "getLatestUserDetails: " + area1);
        } catch (JSONException e) {
            e.printStackTrace();
            //Log.e(TAG, "getLatestUserDetails: called");
        }
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);

        mainAPIInterface.getProfileDetails(Long.parseLong(ph1), Integer.parseInt(area1), bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    ////Log.e(TAG, "onResponse: getprofile :" + res);
                    try {
                        JSONObject indexUser = new JSONObject(res);
                        if (indexUser.isNull("result")) {
                            ////Log.e(TAG, "onResponse: " + true);
                            Toast.makeText(getActivity(), "User details not found", Toast.LENGTH_SHORT).show();
                        } else {
                            DataVaultManager.getInstance(getActivity()).saveUserDetails(res);
                            getCurrencyCode();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                       /* DataVaultManager.getInstance(getContext()).saveUserDetails(res);
                        invalidateUserInfo();*/
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getActivity()).saveUserDetails("");
                        DataVaultManager.getInstance(getActivity()).saveUserAccessToken("");
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.putExtra(AppoConstants.WHERE, mWhere);

                        startActivity(intent);
                        getActivity().finish();
                    } else if (response.code() == 400) {
                        Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_SHORT).show();
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

    private void getCurrencyCode() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.info_getting_currency_code));
        dialog.show();

        mainAPIInterface.getCurrencyResponse().enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ////Log.e(TAG, "onResponse: currency :: " + new Gson().toJson(response.body().getResult()));
                    resultCurrency = response.body().getResult();
                    readUserAccounts();
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                dialog.dismiss();
                ////Log.e(TAG, "onFailure: " + t.getMessage().toString());
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
                if (index.has(AppoConstants.ACCOUNTSTATUS)) {
                    ////Log.e(TAG, "readUserAccounts: AccountStatus : " + index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                    model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                    model.setCurrentbalance(index.getString(AppoConstants.CURRENTBALANCE));
                    mListAccount.add(model);
                } /*else {
                    ////Log.e(TAG, "readUserAccounts: AccountStatus : " + "null");
                    model.setAccountstatus("");
                }*/

            }
            mListTemp = new ArrayList<String>();

            if (mListAccount.size() > 0) {
                //resultScan = "MI1000000000031|CERCA24|63516303|507| support@stuffrs.com|undefined|USD";
                //for adapter
                for (int i = 0; i < mListAccount.size(); i++) {
                    mListTemp.add(mListAccount.get(i).getAccountnumber() + "-" + mListAccount.get(i).getCurrencyCode());
                }

                tvFromAccount.setText(mListTemp.get(0));
                mFromPosition = 0;

                //if (splitScan[splitScan.length - 1].equalsIgnoreCase(mListAccount.get(0).getCurrencyCode())) {
                conversionRates = 1;
                tvConversionRates.setText(String.valueOf(conversionRates));
                //} else {
                ////Log.e(TAG, "readUserAccounts: no need");
                //getConversionBaseRate(mFromPosition);
                //}


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getCurrency(String param) {
        String res = null;
        for (int i = 0; i < resultCurrency.size(); i++) {
            String sid = resultCurrency.get(i).getId().toString();
            if (sid.equals(param)) {

                res = resultCurrency.get(i).getCurrencyCode();
                break;
            }
        }
        return res;
    }


    public void verifyDetails() {
        Helper.hideKeyboard(edAmount, getActivity());
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
            Toast.makeText(getActivity(), getString(R.string.info_invalid_format), Toast.LENGTH_SHORT).show();
            return;
        }

        /*if (splitScan[0].equalsIgnoreCase(mListAccount.get(fromAccountPosition).getAccountnumber())) {
            showSameAccountErrors();
            return;
        }*/

        if (Float.parseFloat(mListAccount.get(fromAccountPosition).getCurrentbalance()) >= Float.parseFloat(tvAmountCredit.getText().toString().trim())) {
            showBottomPinDialog();
        } else {
            showBalanceErrorDailog();
        }

    }

    public void showBottomPinDialog() {
        bottotmPinFragment = new BottotmPinFragment();
        bottotmPinFragment.show(getChildFragmentManager(), bottotmPinFragment.getTag());
        bottotmPinFragment.setCancelable(false);
    }


    private void getCommissions(String transaction) {
        userTransactionPin = transaction;
        ////Log.e(TAG, "getCommissions: pin : " + transaction);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.info_conversion_rate));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getAllTypeMerchantCommissions(bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (dialogMerchant != null) {
                        dialogMerchant.dismiss();
                    }
                    String res = new Gson().toJson(response.body());
                    try {
                        jsonCommission = new JSONObject(res);
                        calculateCommission();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (dialogMerchant != null) {
                        dialogMerchant.dismiss();
                    }
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getActivity()).saveUserDetails("");
                        DataVaultManager.getInstance(getActivity()).saveUserAccessToken("");
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.putExtra(AppoConstants.WHERE, mWhere);

                        startActivity(intent);
                        getActivity().finish();
                    }
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialogMerchant != null) {
                    dialogMerchant.dismiss();
                }
                dialog.dismiss();

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
            bankfees = (float) Helper.getTwoDecimal(bankcommission * fundamount);
            float newamount = (float) Helper.getTwoDecimal(fundamount + bankfees);
            processingfees = 0;
            processingfees = (float) Helper.getTwoDecimal(fundamount * processingCommission);
            finaamount = (float) Helper.getTwoDecimal(newamount + processingfees);
            bankfees = (float) Helper.getTwoDecimal(bankfees + flatbankcomission);
            processingfees = (float) Helper.getTwoDecimal(processingfees + flatprocessingcomission);
            float flatfees = (float) Helper.getTwoDecimal(flatbankcomission + flatprocessingcomission);
            finaamount = (float) (finaamount + Helper.getTwoDecimal(flatfees));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_about_topay_common, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView btnYes = dialogLayout.findViewById(R.id.btnYes);

        MyTextView btnNo = dialogLayout.findViewById(R.id.btnNo);
        //String boldText = "<font color=''><b>" + amountaftertax_fees + "</b></font>" + " " + "<font color=''><b>" + mListAccount.get(fromAccountPosition).getCurrencyCode() + "</b></font>";
        String boldText = "<font color=''><b>" + edAmount.getText().toString().trim() + "</b></font>" + " " + "<font color=''><b>" + mListAccount.get(fromAccountPosition).getCurrencyCode() + "</b></font>";
        String paymentAmount = getString(R.string.merchant_partial_pay1) + " " + boldText + " " + getString(R.string.merchant_partial_pay2);
        tvInfo.setText(Html.fromHtml(paymentAmount));
        btnYes.setVisibility(View.VISIBLE);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makePayment();
                //makePaymentUnion();
                showLoad();
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

    private void showLoad() {
        if (dialogMerchant != null) {
            dialogMerchant.dismiss();
        }
        Helper.showLoading(getString(R.string.info_please_wait), getContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //showPayDialogLikeUnion("100202478542755");
                showPayDialogLikeUnion("921059160100025");
            }
        }, 200);
    }

    private void makePaymentUnion() {
        if (dialogMerchant != null) {
            dialogMerchant.dismiss();
        }
        JSONObject mParam = new JSONObject();
        try {
            mParam.put("userType", "CUSTOMER");
            mParam.put("money", edAmount.getText().toString().trim());
            mParam.put("accountNumber", Helper.getWalletAccountNumber());
            mParam.put("qrCode", scanText);
            //Log.e(TAG, "makePaymentUnion: " + mParam.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.info_sending_request));
        dialog.show();
        AndroidNetworking.post("https://prodapi.appopay.com/api/unionpay/transferMoney").addJSONObjectBody(mParam).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ////Log.e(TAG, "onResponse: " + response);
                dialog.dismiss();
                //{"result":{"merchantId":"000000000000096","merchantName":"STUFFRS, S.A."},"message":"success","status":200}
                try {
                    JSONObject result = response.getJSONObject("result");
                    String merchantId = result.getString("merchantId");
                    showPayDialogLikeUnion(merchantId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //showPayDialogLikeUnion();


            }

            @Override
            public void onError(ANError anError) {
                //Log.e(TAG, "onError: " + anError.getErrorBody());
                //Log.e(TAG, "onError: " + anError.getErrorDetail());

                dialog.dismiss();
                Toast.makeText(getActivity(), "" + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();


            }
        });


    }

    private void makePayment() {
        if (dialogMerchant != null) {
            dialogMerchant.dismiss();
        }

        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);

        JsonObject params = new JsonObject();
        params.addProperty(AppoConstants.ACCOUNTNUMBER, mListAccount.get(mFromPosition).getAccountnumber());

        params.addProperty(AppoConstants.CURRENCYCODE, mListAccount.get(mFromPosition).getCurrencyCode());
        params.addProperty(AppoConstants.PAYAMOUNT, String.valueOf(amountaftertax_fees));
        params.addProperty(AppoConstants.CHARGES, bankfees);
        params.addProperty(AppoConstants.FEES, processingfees);
        params.addProperty(AppoConstants.TRANSACTIONPIN, userTransactionPin);
        params.addProperty(AppoConstants.TAXES, taxes);
        params.addProperty(AppoConstants.MERCHANTNAME, valueMerchantName);
        params.addProperty(AppoConstants.MERCHANTACCOUNT, merchantWalletAccount);
        params.addProperty(AppoConstants.MERCHANTNUMBER, valuePhone);
        params.addProperty(AppoConstants.MERCHANTAREACODE, valueCountry);
        params.addProperty(AppoConstants.AMOUNT, tvAmountCredit.getText().toString().trim());

        try {
            JSONObject jsonUser = new JSONObject(vaultValue);
            objResult = jsonUser.getJSONObject(AppoConstants.RESULT);
            params.addProperty(AppoConstants.CUSTOMERMOBILE, objResult.getString(AppoConstants.MOBILENUMBER));
            params.addProperty(AppoConstants.USERID, objResult.getString(AppoConstants.ID));
            String senderName = objResult.getString(AppoConstants.FIRSTNAME) + " " + objResult.getString(AppoConstants.LASTNAME);
            params.addProperty(AppoConstants.CUSTOMERNAME, senderName);
            params.addProperty(AppoConstants.AREACODE, objResult.getString(AppoConstants.PHONECODE));
            ////Log.e(TAG, "makePayment: " + params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        makeMerchantPayment(params);
    }

    private void makeMerchantPayment(JsonObject params) {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.info_sending_request));
        dialog.show();
        String accessToken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        mainAPIInterface.postMerchantPayment(params, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ////Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                    String res = new Gson().toJson(response.body());
                    try {
                        JSONObject responsePayment = new JSONObject(res);
                        if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("-1")) {
                            showCommonError(getString(R.string.info_invalid_transactipon_pin));
                            //showPayDialogLikeUnion("#123467");
                            //showPayDialog();
                        } else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("-2")) {
                            showCommonError(getString(R.string.info_sufficient_fund));
                            //showPayDialog();
                        } else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("0")) {
                            showCommonError(getString(R.string.info_payment_failed_try_after));
                            //showPayDialog();
                        } else {
                            //showPayDialog(responsePayment.getString(AppoConstants.RESULT));
                            showPayDialogLikeUnion(responsePayment.getString(AppoConstants.RESULT));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getActivity()).saveUserDetails("");
                        DataVaultManager.getInstance(getActivity()).saveUserAccessToken("");
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.putExtra(AppoConstants.WHERE, mWhere);

                        startActivity(intent);
                        getActivity().finish();
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

    private void showPayDialogLikeUnion(String param) {
        Helper.hideLoading();
        AlertDialog.Builder mBuilder = new MaterialAlertDialogBuilder(getContext(), R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
        View mCustomLayout = LayoutInflater.from(getActivity()).inflate(R.layout.success_dialog_inner, null);
        LinearLayout layoutRoot = mCustomLayout.findViewById(R.id.layoutRoot);
        MyTextView tvInfo = mCustomLayout.findViewById(R.id.tvInfo);
        MyTextViewBold tvAmountPay = mCustomLayout.findViewById(R.id.tvAmountPay);//edAmount
        MyTextViewBold tvCurrencyPay = mCustomLayout.findViewById(R.id.tvCurrencyPay);
        MyTextViewBold tvTransactionTime = mCustomLayout.findViewById(R.id.tvTransactionTime);
        MyTextViewBold tvVoucherPay = mCustomLayout.findViewById(R.id.tvVoucherPay);
        MyTextViewBold tvSenderName = mCustomLayout.findViewById(R.id.tvSenderName);
        MyTextViewBold tvMerchantId = mCustomLayout.findViewById(R.id.tvMerchantId);

        MyButton btnShare = mCustomLayout.findViewById(R.id.btnShare);
        MyButton btnClose = mCustomLayout.findViewById(R.id.btnClose);
        tvAmountPay.setText(edAmount.getText().toString().trim());
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
        tvCurrencyPay.setText(mCurrencyId);
        tvTransactionTime.setText(getDateTime());
        long currentTimeMillis = System.currentTimeMillis();
        long l = currentTimeMillis / 2;
        tvVoucherPay.setText("" + l);
        tvMerchantId.setText(param);

        //String info = "<font color='#FF0000'>" + "<b>" + "Paid to " + valueMerchantName + "</b></font>" + "<br>" + "SUCCESS";
        String info = "<font color='#FF0000'>" + "<b>" + "Paid to " + "COODE M.L. " + "</b></font>";
        tvInfo.setText(Html.fromHtml(info));

        tvSenderName.setText(Helper.getSenderName());

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectHomePay();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(AddMoneyToWallet.this, "Show Receipt", Toast.LENGTH_SHORT).show();
                takeScreenShort(layoutRoot);
            }
        });
        mBuilder.setView(mCustomLayout);
        mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

    }

    private String getDateTime() {
        String mDateFormat = "dd MMM yyyy HH:mm:ss";
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(mDateFormat);
        Date mDate = new Date();
        String format = mSimpleDateFormat.format(mDate);
        ////Log.e(TAG, "getDate: " + format);
        return format;
    }


    public void redirectHomePay() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }


    private void takeScreenShort(LinearLayout rootLayout) {
        mDialog.dismiss();
        //Bitmap bitmap = screenShot(rootLayout);
        Bitmap bitmap = getScreenShot(rootLayout);
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        mFileSSort = new File(getActivity().getCacheDir(), "screen_short_" + now + ".jpeg");
        try {
            boolean newFile = mFileSSort.createNewFile();
            if (newFile) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                //write the bytes in file
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mFileSSort);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    openScreenshot(mFileSSort);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private Bitmap getScreenShot(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) bgDrawable.draw(canvas);
        else canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;

    }


    private void openScreenshot(File imageFile) {
        Intent intentShareFile = new Intent();
        intentShareFile.setAction(Intent.ACTION_SEND);
        Uri uriForFile = FileProvider.getUriForFile(getActivity().getApplicationContext(), "com.stuffer.stuffers.fileprovider", imageFile);
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentShareFile.setType("image/jpeg");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uriForFile);
        Intent chooser = Intent.createChooser(intentShareFile, "Share File");
        List<ResolveInfo> resInfoList = getActivity().getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            getActivity().grantUriPermission(packageName, uriForFile, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(chooser, 198);
    }


    private void showBalanceErrorDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
    }

    public void showCommonError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        ImageView ivCancel = dialogLayout.findViewById(R.id.ivCancel);
        ivCancel.setVisibility(View.VISIBLE);
        tvInfo.setText(message);
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


    public void onConfirm(String pin) {
        if (bottotmPinFragment != null) bottotmPinFragment.dismiss();
        //getCommissions(pin);
        /*String transactionPin = Helper.getTransactionPin();
        if (transactionPin.equalsIgnoreCase(pin)) {
            showYouAboutToPay();
        } else {
            Toast.makeText(getActivity(), "Invalid Transaction Pin", Toast.LENGTH_SHORT).show();
        }*/
        showYouAboutToPay();
    }

}