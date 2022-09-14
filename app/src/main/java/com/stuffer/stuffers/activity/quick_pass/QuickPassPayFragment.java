package com.stuffer.stuffers.activity.quick_pass;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.PayUserActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.SuccessResponseListener;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class QuickPassPayFragment extends Fragment {
    MainAPIInterface mainAPIInterface;
    private static final String TAG = "QuickPassPayFragment";
    ProgressDialog dialog;
    String resultScan, merchantAreaCode, merchantMobileNumber, merchantEmailId, fromCurrency, userTransactionPin;
    private List<CurrencyResult> resultCurrency;
    private ArrayList<AccountModel> mListAccount;
    private JSONObject indexMerchant,jsonCommission,objResult;
    private MyTextView tvHeader, tvCodeMobile, tvEmialId, tvIndex5, tvAccountNos;
    private MyTextView tvRequiredFilled, tvFromAccount, tvConversionRates, tvAmountCredit;
    private CardView tvCardMerchant;
    private String[] splitScan;
    private ArrayList<String> mListTemp;
    private int fromAccountPosition;
    private FromAccountDialogFragment fromAccountDialogFragment;
    private Dialog dialogMerchant;
    private MyEditText edAmount;
    private float conversionRates = 0;
    MyButton btnPayNow;
    private float finaamount;
    private float bankfees;
    private float processingfees = 0;
    private float amountaftertax_fees;
    private float taxes;
    private int mFromPosition;
    private SuccessResponseListener mListnerSucces;
    private String mMerName, mMerNumber, mMerArea;
    private BottotmPinFragment bottotmPinFragment;
    private String merchantAccountNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView =inflater.inflate(R.layout.fragment_quick_pass_pay, container, false);
        mainAPIInterface = ApiUtils.getAPIService();
        tvCardMerchant = (CardView) mView.findViewById(R.id.tvCardMerchant);
        tvHeader = (MyTextView) mView.findViewById(R.id.tvHeader);
        tvCodeMobile = (MyTextView) mView.findViewById(R.id.tvCodeMobile);
        tvEmialId = (MyTextView) mView.findViewById(R.id.tvEmialId);
        tvIndex5 = (MyTextView) mView.findViewById(R.id.tvIndex5);
        tvAccountNos = (MyTextView) mView.findViewById(R.id.tvAccountNos);

        tvRequiredFilled = (MyTextView) mView.findViewById(R.id.tvRequiredFilled);
        String required = getString(R.string.required_filled) + "<font color='#00baf2'>" + "*" + "</font>";
        tvRequiredFilled.setText(Html.fromHtml(required));
        tvFromAccount = (MyTextView) mView.findViewById(R.id.tvFromAccount);

        edAmount = (MyEditText) mView.findViewById(R.id.edAmount);
        tvAmountCredit = (MyTextView) mView.findViewById(R.id.tvAmountCredit);

        tvConversionRates = (MyTextView) mView.findViewById(R.id.tvConversionRates);

        btnPayNow = (MyButton) mView.findViewById(R.id.btnPayNow);

        resultScan = getArguments().getString(AppoConstants.MERCHANTSCANCODE);
//        Log.e(TAG, "onCreateView: resultScan :: "+resultScan);

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
                    float twoDecimal = (float) Helper.getTwoDecimal(Float.parseFloat(edAmount.getText().toString().trim()) * conversionRates);
                    tvAmountCredit.setText(String.valueOf(twoDecimal));

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
        //showSuccessDialog("allow");
        //showMerchantErrorDialog();
        showMerchantDetails();
        //getMerchantProfile();
        return mView;
    }

    private void getMerchantProfile(String pht, String areat) {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_getting_merchant_details));
        dialog.show();

        String accessToken = DataVaultManager.getInstance(getContext()).getVaultValue(KEY_ACCESSTOKEN);
        splitScan = resultScan.split("\\|");

        String ph = pht;
        String area = areat;
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        mainAPIInterface.getProfileMerchantDetails(ph, area, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    ////Log.e(TAG, "onResponse: getprofileMerchant :" + res);
                    try {
                        indexMerchant = new JSONObject(res);
                        if (indexMerchant.isNull("result")) {
                            ////Log.e(TAG, "onResponse: " + true);
                            Toast.makeText(getContext(), "Merchant Details does not exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONObject jsonObject = indexMerchant.getJSONObject(AppoConstants.RESULT);
                        if (jsonObject.getBoolean(AppoConstants.ENABLE)) {
                            //showMerchantDetails();
                            JSONObject result = indexMerchant.getJSONObject("result");
                            JSONObject customerdetails = result.getJSONObject("customerdetails");
                            JSONArray customeraccount = customerdetails.getJSONArray("customeraccount");
                            JSONObject jsonObject1 = customeraccount.getJSONObject(0);
                            merchantAccountNumber = jsonObject1.getString("accountnumber");
                            getLatestUserDetails();

                        } else {
                            showMerchantErrorDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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
        });


    }

    private void showMerchantErrorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_merchant_error, null);

        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectHome(false);
            }
        });


        builder.setView(dialogLayout);

        dialogMerchant = builder.create();

        dialogMerchant.setCanceledOnTouchOutside(false);

        dialogMerchant.show();
    }

    private void showMerchantDetails() {
        tvCardMerchant.setVisibility(View.VISIBLE);
        //resultScan = "MI1000000000031|CERCA24|63516303|507| support@stuffrs.com|undefined|USD";
        //splitScan = resultScan.split("\\|");
        try {
            Log.e(TAG, "showMerchantDetails: ======" + resultScan);
            JSONObject mRoot = new JSONObject(resultScan);
            JSONObject merchatDataWithCodes = mRoot.getJSONObject("merchatDataWithCodes");
            if (merchatDataWithCodes.has("27")) {
                JSONObject jsonObject27 = merchatDataWithCodes.getJSONObject("27");
                String  merchantID= jsonObject27.getString("15");
                tvAccountNos.setText(merchantID + "-USD");
                String  globallyUniqueIdentifier= jsonObject27.getString("00");
                tvEmialId.setText(globallyUniqueIdentifier);
            } else {
                String string15 = merchatDataWithCodes.getString("15");
                tvAccountNos.setText(string15 + "-USD");
                tvEmialId.setText("support@appopay.com");

            }
            String merchantAreaCodeTemp = merchatDataWithCodes.getString("58");
            if (merchantAreaCodeTemp.equalsIgnoreCase("PA")) {
                merchantAreaCode = "507";
            } else {
                merchantAreaCode = "507";
            }
            String name = merchatDataWithCodes.getString("59");

            JSONObject jsonObjectPh = merchatDataWithCodes.getJSONObject("62");
            String mobileNumber;
            if (jsonObjectPh.has("02")) {
                mobileNumber = jsonObjectPh.getString("02");
            } else {
                mobileNumber = "63516303";
            }
            tvHeader.setText(name);

            tvCodeMobile.setText("(+" + merchantAreaCode + ") " + mobileNumber);

            mMerName = name;
            mMerNumber = mobileNumber;
            mMerArea = merchantAreaCode;


            getMerchantProfile(mobileNumber, merchantAreaCode);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*tvCardMerchant.setVisibility(View.VISIBLE);
        tvHeader.setText(splitScan[1]);
        merchantAreaCode = splitScan[3];
        merchantMobileNumber = splitScan[2];
        merchantEmailId = splitScan[4].trim();
        String mobileWithCode = "(+" + merchantAreaCode + ") " + merchantMobileNumber;
        tvCodeMobile.setText(mobileWithCode);
        tvEmialId.setText(merchantEmailId);
        tvIndex5.setText(splitScan[5]);
        String accountWithType = ": " + splitScan[0] + "-" + splitScan[splitScan.length - 1];
        tvAccountNos.setText(accountWithType);
        getLatestUserDetails();*/
    }

    private void getLatestUserDetails() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_getting_user_details));
        dialog.show();

        String accessToken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
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
                    //String res = new Gson().toJson(response.body());
                    JsonObject body = response.body();
                    String res=body.toString();
                    DataVaultManager.getInstance(getContext()).saveUserDetails(res);
                    ////Log.e(TAG, "onResponse: getprofile :" + res);
                    try {
                        JSONObject indexUser = new JSONObject(res);
                        if (indexUser.isNull("result")) {
                            ////Log.e(TAG, "onResponse: " + true);
                            Toast.makeText(getContext(), "User details not found", Toast.LENGTH_SHORT).show();
                        } else {
                            DataVaultManager.getInstance(getContext()).saveUserDetails(res);
                            getCurrencyCode();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                       /* DataVaultManager.getInstance(getContext()).saveUserDetails(res);
                        invalidateUserInfo();*/
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 400) {
                        Toast.makeText(getContext(), "Bad Request", Toast.LENGTH_SHORT).show();
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
        dialog = new ProgressDialog(getContext());
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

                if (splitScan[splitScan.length - 1].equalsIgnoreCase(mListAccount.get(0).getCurrencyCode())) {
                    conversionRates = 1;
                    tvConversionRates.setText(String.valueOf(conversionRates));
                } else {
                    conversionRates = 1;
                    tvConversionRates.setText(String.valueOf(conversionRates));
                    //getConversionBaseRate(mFromPosition);
                }


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

    @Override
    public void onResume() {
        super.onResume();
    }

    public void verifyDetails() {
        Helper.hideKeyboard(edAmount, getContext());
        //if (tvFromAccount.getText().toString().equalsIgnoreCase("Select From Account")) {
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
            Toast.makeText(getContext(), getString(R.string.info_invalid_format), Toast.LENGTH_SHORT).show();
            return;
        }

        if (splitScan[0].equalsIgnoreCase(mListAccount.get(fromAccountPosition).getAccountnumber())) {
            showSameAccountErrors();
            return;
        }

        if (Float.parseFloat(mListAccount.get(fromAccountPosition).getCurrentbalance()) >= Float.parseFloat(tvAmountCredit.getText().toString().trim())) {
            //showToast("show pin");
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
        dialog = new ProgressDialog(getContext());
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
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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

    /**
     * const bankcomission = comdata.result.bankcomission;
     * const processingcomission = comdata.result.processingfees;
     * const flatbankcomission = comdata.result.flatbankcomission;
     * const flatprocessingcomission = comdata.result.flatprocessingfees;
     * const fundamount = this.onwalletForm.get('amounttoCredit').value;
     * let bankfees = (bankcomission * fundamount).toFixed(2);
     * const newamount = parseFloat(fundamount) + parseFloat(bankfees);
     * let processingfees = (fundamount * processingcomission).toFixed(2);
     * let finalamount = newamount + parseFloat(processingfees);
     * bankfees = (parseFloat(bankfees) + flatbankcomission).toFixed(2);
     * processingfees = (parseFloat(processingfees) + flatprocessingcomission).toFixed(2);
     * const flatfees = (flatbankcomission + flatprocessingcomission).toFixed(2);
     * finalamount = finalamount + parseFloat(flatfees);
     */

    private void calculateCommission() {
        ////Log.e(TAG, "calculateCommission: " + jsonCommission.toString());
        try {
            JSONObject jsonResult = jsonCommission.getJSONObject(AppoConstants.RESULT);
            float bankcommission = Float.parseFloat(jsonResult.getString(AppoConstants.BANKCOMMISSION));
            float processingCommission = Float.parseFloat(jsonResult.getString(AppoConstants.PROCESSINGFEES));
            float flatbankcomission = Float.parseFloat(jsonResult.getString(AppoConstants.FLATBANKCOMMISSION));
            float flatprocessingcomission = Float.parseFloat(jsonResult.getString(AppoConstants.FLATPROCESSINGFEES));
            float taxPercentage = Float.parseFloat(jsonResult.getString(AppoConstants.TAXPERCENTAGE));
            String taxon = jsonResult.getString(AppoConstants.TAXON);
            //float fundamount = Float.parseFloat(tvAmountCredit.getText().toString().trim());
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
                ////Log.e(TAG, "calculateCommission: if called");
            } else {
                taxes = (float) Helper.getTwoDecimal((fundamount * taxPercentage) / 100);
                amountaftertax_fees = (float) Helper.getTwoDecimal(fundamount + ((fundamount * taxPercentage) / 100));
                ////Log.e(TAG, "calculateCommission: else caleed");
            }
            showYouAboutToPay();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showYouAboutToPay() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_about_topay_common, null);

        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnNo = dialogLayout.findViewById(R.id.btnNo);
        //String boldText = "<font color=''><b>" + finaamount + "</b></font>" + " " + "<font color=''><b>" + mListAccount.get(fromAccountPosition).getCurrencyCode() + "</b></font>";
        //String boldText = "<font color=''><b>" + tvAmountCredit.getText().toString().trim() + "</b></font>" + " " + "<font color=''><b>" + mListAccount.get(fromAccountPosition).getCurrencyCode() + "</b></font>";
        //String paymentAmount = getString(R.string.recharge_partial_pay1) + " " + finalamount + " " + senderCurrencyCode + " " + getString(R.string.recharge_partial_pay2);
        String boldText = "<font color=''><b>" + amountaftertax_fees + "</b></font>" + " " + "<font color=''><b>" + mListAccount.get(fromAccountPosition).getCurrencyCode() + "</b></font>";

        String paymentAmount = getString(R.string.merchant_partial_pay1) + " " + boldText + " " + getString(R.string.merchant_partial_pay2);

        tvInfo.setText(Html.fromHtml(paymentAmount));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment();
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

    private void makePayment() {
        if (dialogMerchant != null) {
            dialogMerchant.dismiss();
        }
        ////Log.e(TAG, "makePayment: called");

        /*
        * JSONObject jsonUser = new JSONObject(vaultValue);
            JSONObject objResult = jsonUser.getJSONObject(AppoConstants.RESULT);
            sentParams.addProperty(AppoConstants.SENDERAREACODE, objResult.getString(AppoConstants.PHONECODE));
            sentParams.addProperty(AppoConstants.SENDERMOBILENUMBER, objResult.getString(AppoConstants.MOBILENUMBER));
            String senderName = objResult.getString(AppoConstants.FIRSTNAME) + " " + objResult.getString(AppoConstants.LASTNAME);
            sentParams.addProperty(AppoConstants.SENDERNAME, senderName);
            sentParams.addProperty(AppoConstants.TRANSACTIONPIN, userTransactionPin);
            sentParams.addProperty(AppoConstants.USERID, objResult.getString(AppoConstants.ID));
            ////Log.e(TAG, "makePayment: " + sentParams.toString());
         */

        /*
        * accountnumber: this.fromaccountno,
                            customermobile: this.userObj.mobilenumber,
                            currencycode: this.frcurrency,
                            payamount: finalamount,
                            charges: bankfees,
                            fees: processingfees,
                            transactionpin: datatransactionpin,
                            userid: this.userObj.id,
                            customername: this.userObj.firstName + ' ' + this.userObj.lastName,
                            areacode: this.userObj.phonecode,
                            merchantname: this.merchantDetails.merchantname,
                            merchantaccount: this.merchantDetails.merchantaccount,
                            merchantnumber: this.merchantDetails.mobilenumber,
                            merchantareacode: this.merchantDetails.areacode,
                            amount: this.onwalletForm.get('amounttoCredit').value
        */

        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);

        JsonObject params = new JsonObject();
        params.addProperty(AppoConstants.ACCOUNTNUMBER, mListAccount.get(mFromPosition).getAccountnumber());

        params.addProperty(AppoConstants.CURRENCYCODE, mListAccount.get(mFromPosition).getCurrencyCode());
        //params.addProperty(AppoConstants.PAYAMOUNT, finaamount);

        //params.addProperty(AppoConstants.PAYAMOUNT, tvAmountCredit.getText().toString().trim());
        params.addProperty(AppoConstants.PAYAMOUNT, String.valueOf(amountaftertax_fees));
        params.addProperty(AppoConstants.CHARGES, bankfees);
        params.addProperty(AppoConstants.FEES, processingfees);
        params.addProperty(AppoConstants.TRANSACTIONPIN, userTransactionPin);
        params.addProperty(AppoConstants.TAXES, taxes);

/**
 *  merchantaccount: newbarcodetext[0],
 *         merchantname: newbarcodetext[1],
 *         mobilenumber: newbarcodetext[2],
 *         areacode: newbarcodetext[3],
 *         email: newbarcodetext[4],
 *         address: newbarcodetext[5],
 *         currencycode: newbarcodetext[6]
 */

       /* params.addProperty(AppoConstants.MERCHANTNAME, splitScan[1]);
        params.addProperty(AppoConstants.MERCHANTACCOUNT, splitScan[0]);
        params.addProperty(AppoConstants.MERCHANTNUMBER, splitScan[2]);
        params.addProperty(AppoConstants.MERCHANTAREACODE, splitScan[3]);
        params.addProperty(AppoConstants.AMOUNT, tvAmountCredit.getText().toString().trim());*/
        params.addProperty(AppoConstants.MERCHANTNAME, mMerName);
        params.addProperty(AppoConstants.MERCHANTACCOUNT, merchantAccountNumber);
        params.addProperty(AppoConstants.MERCHANTNUMBER, mMerNumber);
        params.addProperty(AppoConstants.MERCHANTAREACODE, mMerArea);
        params.addProperty(AppoConstants.AMOUNT, tvAmountCredit.getText().toString().trim());

        try {
            JSONObject jsonUser = new JSONObject(vaultValue);
            objResult = jsonUser.getJSONObject(AppoConstants.RESULT);
            params.addProperty(AppoConstants.CUSTOMERMOBILE, objResult.getString(AppoConstants.MOBILENUMBER));
            params.addProperty(AppoConstants.USERID, objResult.getString(AppoConstants.ID));
            String senderName = objResult.getString(AppoConstants.FIRSTNAME) + " " + objResult.getString(AppoConstants.LASTNAME);
            params.addProperty(AppoConstants.CUSTOMERNAME, senderName);
            params.addProperty(AppoConstants.AREACODE, objResult.getString(AppoConstants.PHONECODE));
            Log.e(TAG, "makePayment: " + params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        makeMerchantPayment(params);
    }

    private void makeMerchantPayment(JsonObject params) {
        dialog = new ProgressDialog(getContext());
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
                        } else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("-2")) {
                            showCommonError(getString(R.string.info_sufficient_fund));
                        } else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("0")) {
                            showCommonError(getString(R.string.info_payment_failed_try_after));
                        } else {
                            showSuccessDialog(responsePayment.getString(AppoConstants.RESULT));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
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


    private void showSuccessDialog(String param) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        Toast.makeText(getContext(), "" + msg, Toast.LENGTH_SHORT).show();
    }

    public void showCommonError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
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


    private void redirectHome() {
        if (dialogMerchant != null) {
            dialogMerchant.dismiss();
        }

        mListnerSucces.onSuccess(true);

    }
    private void redirectHome(boolean status) {
        if (dialogMerchant != null) {
            dialogMerchant.dismiss();
        }

        mListnerSucces.onSuccess(false);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListnerSucces=(SuccessResponseListener)context;
    }

    public void passTransactionPin(String pin) {
        if (bottotmPinFragment != null)
            bottotmPinFragment.dismiss();
        getCommissions(pin);
    }
}