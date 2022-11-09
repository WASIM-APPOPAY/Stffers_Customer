package com.stuffer.stuffers.fragments.quick_pay;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.MoneyTransferListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletTransferFragment2 extends Fragment {
    private static final String TAG = "WalletTransferFragment2";
    private MyTextView tvRequiredFilled, tvFromAccount, tvToAccount, tvAmountCredit, tvConversionRates, tvExchange;
    private MainAPIInterface mainAPIInterface;
    private ProgressDialog dialog;
    private ArrayList<AccountModel> mListAccount;
    private ArrayList<String> mListTemp;
    private List<CurrencyResult> resultCurrency;
    private MyEditText edAmount;
    private MyTextView btnTransfer;
    private int mFromPosition;
    private String reciveraccountnumber;
    private Dialog dialogTransfer;
    private String userTransactionPin;
    private JSONObject jsonCommission;
    private float bankfees, processingfees, finaamount, amountaftertax_fees, taxes;
    float conversionRates;
    private String fomrcurrencycode, recaccountnumber, recmobilenumber, recareacode, recname, recuserid, fromcurrency, receiveruser;
    MoneyTransferListener mMoneyTransfer;
    private String mTransferAmount;
    private String receiverEmail;

    private MainAPIInterface mainAPIInterface2;
    private BottotmPinFragment bottotmPinFragment;
    private MyTextViewBold tvName, tvName1;
    private MyTextView tvBalance, tvBalance1;
    private Dialog mDialog;
    private File mFileSSort;
    private int mType = 0;
    private CircleImageView ivSender, ivReceiver;
    private ProgressDialog mProgressDialog;
    private double exchange;
    private String toCurrency;
    private float mCreditAmount = (float) 0.00;


    public WalletTransferFragment2() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = this.getArguments();
        mType = arguments.getInt(AppoConstants.WHERE, 0);
        receiveruser = arguments.getString(AppoConstants.SENTUSER);
        resultCurrency = arguments.getParcelableArrayList(AppoConstants.SENTCURRENCY);
        mTransferAmount = arguments.getString("amount");
        exchange = Double.parseDouble(arguments.getString("exchange"));

        View view = inflater.inflate(R.layout.fragment_wallet_transfer2, container, false);
        mainAPIInterface = ApiUtils.getAPIService();
        ivSender = view.findViewById(R.id.ivSender);
        ivReceiver = view.findViewById(R.id.ivReceiver);
        tvFromAccount = (MyTextView) view.findViewById(R.id.tvFromAccount);
        tvName = (MyTextViewBold) view.findViewById(R.id.tvName);
        tvName1 = (MyTextViewBold) view.findViewById(R.id.tvName1);
        tvBalance = (MyTextView) view.findViewById(R.id.tvBalance);
        tvBalance1 = (MyTextView) view.findViewById(R.id.tvBalance1);
        tvToAccount = (MyTextView) view.findViewById(R.id.tvToAccount);
        edAmount = (MyEditText) view.findViewById(R.id.edAmount);
        tvAmountCredit = (MyTextView) view.findViewById(R.id.tvAmountCredit);
        tvExchange = (MyTextView) view.findViewById(R.id.tvExchange);
        tvConversionRates = (MyTextView) view.findViewById(R.id.tvConversionRates);
        btnTransfer = (MyTextView) view.findViewById(R.id.btnTransfer);
        String senderName = Helper.getSenderName();
        tvName.setText(senderName);
        String currantBalance = Helper.getCurrantBalance();
        DecimalFormat df2 = new DecimalFormat("#.00");
        Double doubleV = Double.parseDouble(currantBalance);
        String format = df2.format(doubleV);
        tvBalance.setText("$" + format);
        tvExchange.setText(String.valueOf(exchange));

        tvRequiredFilled = (MyTextView) view.findViewById(R.id.tvRequiredFilled);
        String required = getString(R.string.required_filled) + "<font color='#00baf2'>" + "*" + "</font>";
        tvRequiredFilled.setText(Html.fromHtml(required));
        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    String inputAmount = edAmount.getText().toString().trim();
                    if (inputAmount.length() > 0) {
                        float tranaferAmount = Float.parseFloat(inputAmount);
                        //float transfer = (float) (tranaferAmount * conversionRates);
                        float transfer = (float) (tranaferAmount / exchange);
                        float twoDecimal = (float) Helper.getTwoDecimal(transfer);
                        mCreditAmount = twoDecimal;
                        tvAmountCredit.setText(String.valueOf(twoDecimal) + " " + toCurrency.toUpperCase());
                        btnTransfer.setEnabled(true);
                        btnTransfer.setClickable(true);
                    } else {
                        float twoDecimal = (float) Helper.getTwoDecimal(0);
                        tvAmountCredit.setText(String.valueOf(twoDecimal));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (edAmount.getText().toString().trim().isEmpty()) {

                    } else {
                        Toast.makeText(getContext(), getString(R.string.info_invalid_format), Toast.LENGTH_SHORT).show();
                        btnTransfer.setEnabled(false);
                        btnTransfer.setClickable(false);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyDetails();
                //getConversion();
            }
        });
        String senderAvatar = Helper.getSenderAvatar();
        if (!StringUtils.isEmpty(senderAvatar)) {
            Glide.with(getActivity()).load(senderAvatar).placeholder(R.drawable.user_chat).centerCrop().into(ivSender);
        }


        setReceiverDetails();
        getCurrentUserDetails();
        //getConversion();


        return view;
    }


    private void showLoading(String message) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void hideLoading() {
        mProgressDialog.dismiss();
    }


    private void setReceiverDetails() {

        try {
            JSONObject obj = new JSONObject(receiveruser);
            reciveraccountnumber = obj.getString(AppoConstants.RECIEVERACCOUNTNUMBER);
            tvToAccount.setText(obj.getString(AppoConstants.RECIEVERACCOUNTNUMBER) + "-" + obj.getString(AppoConstants.FROMCURRENCYCODE));
            recaccountnumber = obj.getString(AppoConstants.RECIEVERACCOUNTNUMBER);
            fromcurrency = obj.getString(AppoConstants.FROMCURRENCY);
            fomrcurrencycode = obj.getString(AppoConstants.FROMCURRENCYCODE);
            recmobilenumber = obj.getString(AppoConstants.RECEIVERMOBILENUMBER);
            recareacode = obj.getString(AppoConstants.RECEIVERAREACODE);
            recname = obj.getString(AppoConstants.RECIEVERNAME);
            recuserid = obj.getString(AppoConstants.RECIEVERUSERID);
            receiverEmail = obj.getString(AppoConstants.EMIAL);
            String avatar = obj.getString(AppoConstants.AVATAR);
            toCurrency = obj.getString(AppoConstants.TOCURRENCY);

            if (!StringUtils.isEmpty(avatar)) {
                Glide.with(getActivity()).load(avatar).placeholder(R.drawable.user_chat).centerCrop().into(ivReceiver);
            }
            tvName1.setText(recname);
            tvBalance1.setText("+" + recareacode + "" + recmobilenumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getCurrentUserDetails() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_getting_profile));
        dialog.show();

        try {
            String userDetails = DataVaultManager.getInstance(getContext()).getVaultValue(KEY_USER_DETIALS);
            JSONObject mIndex = new JSONObject(userDetails);
            String accessToken = DataVaultManager.getInstance(getContext()).getVaultValue(KEY_ACCESSTOKEN);
            JSONObject mResult = mIndex.getJSONObject(AppoConstants.RESULT);
            String ph = mResult.getString(AppoConstants.MOBILENUMBER);
            String area = mResult.getString(AppoConstants.PHONECODE);
            String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
            mainAPIInterface.getProfileDetails(Long.parseLong(ph), Integer.parseInt(area), bearer_).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {


                        JsonObject body = response.body();
                        String res = body.toString();
                        DataVaultManager.getInstance(getContext()).saveUserDetails(res);

                        readUserAccounts();
                    } else {
                        if (response.code() == 401) {
                            DataVaultManager.getInstance(getContext()).saveUserDetails("");
                            DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                            Intent intent = new Intent(getContext(), SignInActivity.class);
                            intent.putExtra(AppoConstants.WHERE, mType);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dialog.dismiss();

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void readUserAccounts() {
        mListAccount = new ArrayList<>();
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
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
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                    model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                    model.setCurrentbalance(index.getString(AppoConstants.CURRENTBALANCE));
                    mListAccount.add(model);
                }

            }
            mListTemp = new ArrayList<String>();

            if (mListAccount.size() > 0) {

                for (int i = 0; i < mListAccount.size(); i++) {
                    mListTemp.add(mListAccount.get(i).getAccountnumber() + "-" + mListAccount.get(i).getCurrencyCode());
                }

                tvFromAccount.setText(mListTemp.get(0));
                mFromPosition = 0;

                if (fomrcurrencycode.equalsIgnoreCase(mListAccount.get(0).getCurrencyCode())) {
                    conversionRates = 1;
                    tvConversionRates.setText(String.valueOf(conversionRates));
                    if (mTransferAmount.equalsIgnoreCase("no")) {

                    } else {
                        edAmount.setText(mTransferAmount);
                    }
                } else {
                    conversionRates = 1;
                    tvConversionRates.setText(String.valueOf(conversionRates));

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


    private void sentParam(JSONObject response, int fromAccountPosition) {
        mFromPosition = fromAccountPosition;
        try {
            JSONObject jsonRates = response.getJSONObject(AppoConstants.RATES);
            conversionRates = (float) Float.parseFloat(jsonRates.getString(fomrcurrencycode.toUpperCase()));
            tvConversionRates.setText(String.valueOf(conversionRates));
            if (mTransferAmount.equalsIgnoreCase("no")) {

            } else {
                edAmount.setText(mTransferAmount);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void verifyDetails() {
        Helper.hideKeyboard(edAmount, getContext());

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
        if (reciveraccountnumber.equalsIgnoreCase(mListAccount.get(mFromPosition).getAccountnumber())) {
            showSameAccountErrors();
            return;
        }

        if (Float.parseFloat(mListAccount.get(mFromPosition).getCurrentbalance()) >= Float.parseFloat(edAmount.getText().toString().trim())) {
            showBottomPinDialog();
        } else {
            showBalanceErrorDailog();
        }


    }

    private void showBottomPinDialog() {
        bottotmPinFragment = new BottotmPinFragment();
        bottotmPinFragment.show(getChildFragmentManager(), bottotmPinFragment.getTag());
        bottotmPinFragment.setCancelable(false);

    }

    private void showSameAccountErrors() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        tvHeader.setText(getString(R.string.wallet_header));
        tvInfo.setText(getString(R.string.merchant_same_account));
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTransfer.dismiss();
            }
        });

        builder.setView(dialogLayout);
        dialogTransfer = builder.create();
        dialogTransfer.setCanceledOnTouchOutside(false);

        dialogTransfer.show();


    }

    private void showBalanceErrorDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        tvHeader.setText(getString(R.string.wallet_header));
        tvInfo.setText(getString(R.string.merchant_balance_error));
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTransfer.dismiss();
            }
        });

        builder.setView(dialogLayout);

        dialogTransfer = builder.create();

        dialogTransfer.setCanceledOnTouchOutside(false);

        dialogTransfer.show();
    }


    private void getCommissions(String transaction) {
        userTransactionPin = transaction;

        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_conversion_rate));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getAllTypeTransferCommissions(bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    if (dialogTransfer != null) {
                        dialogTransfer.dismiss();
                    }
                    String res = new Gson().toJson(response.body());
                    try {
                        jsonCommission = new JSONObject(res);
                        calculateCommission();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (dialogTransfer != null) {
                        dialogTransfer.dismiss();
                    }
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.putExtra(AppoConstants.WHERE, mType);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }

                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialogTransfer != null) {
                    dialogTransfer.dismiss();
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
            float fundamount = Float.parseFloat(edAmount.getText().toString().trim());
            float taxPercentage = Float.parseFloat(jsonResult.getString(AppoConstants.TAXPERCENTAGE));
            String taxon = jsonResult.getString(AppoConstants.TAXON);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_about_topay_common, null);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnNo = dialogLayout.findViewById(R.id.btnNo);
        tvHeader.setText(getString(R.string.wallet_header));
        String boldText = "<font color=''><b>" + amountaftertax_fees + "</b></font>" + " " + "<font color=''><b>" + mListAccount.get(mFromPosition).getCurrencyCode() + "</b></font>";
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
                dialogTransfer.dismiss();
            }
        });
        builder.setView(dialogLayout);
        dialogTransfer = builder.create();
        dialogTransfer.setCanceledOnTouchOutside(false);
        dialogTransfer.show();
    }


    private void makePayment() {

        if (dialogTransfer != null) {
            dialogTransfer.dismiss();
        }
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        JsonObject params = new JsonObject();
        params.addProperty(AppoConstants.AMOUNT, mCreditAmount);
        params.addProperty(AppoConstants.CHARGES, String.valueOf(bankfees));
        params.addProperty(AppoConstants.CONVERSIONRATE, exchange);
        params.addProperty(AppoConstants.ENTEREDAMOUNT, edAmount.getText().toString().trim());
        params.addProperty(AppoConstants.FEES, String.valueOf(processingfees));
        params.addProperty(AppoConstants.FROMCURRENCY, Integer.parseInt(mListAccount.get(mFromPosition).getCurrencyid()));
        params.addProperty(AppoConstants.FROMCURRENCYCODE, mListAccount.get(mFromPosition).getCurrencyCode());


        params.addProperty(AppoConstants.ORIGINALAMOUNT, amountaftertax_fees);
        params.addProperty(AppoConstants.TAXES, taxes);

        params.addProperty(AppoConstants.RECIEVERACCOUNTNUMBER, recaccountnumber);
        params.addProperty(AppoConstants.RECEIVERAREACODE, Integer.parseInt(recareacode));
        params.addProperty(AppoConstants.RECIEVERNAME, recname);
        params.addProperty(AppoConstants.RECIEVERUSERID, Integer.parseInt(recuserid));
        params.addProperty("recivermobilenumber", Long.parseLong(recmobilenumber));
        params.addProperty(AppoConstants.SENDERACCOUNTNUMBER, mListAccount.get(mFromPosition).getAccountnumber());
        try {
            JSONObject jsonUser = new JSONObject(vaultValue);
            JSONObject objResult = jsonUser.getJSONObject(AppoConstants.RESULT);
            params.addProperty(AppoConstants.SENDERAREACODE, Integer.parseInt(objResult.getString(AppoConstants.PHONECODE)));
            params.addProperty(AppoConstants.SENDERMOBILENUMBER, Long.parseLong(objResult.getString(AppoConstants.MOBILENUMBER)));
            String senderName = objResult.getString(AppoConstants.FIRSTNAME) + " " + objResult.getString(AppoConstants.LASTNAME);
            params.addProperty(AppoConstants.SENDERNAME, senderName);
            params.addProperty(AppoConstants.TOCURRENCY, Integer.parseInt(fromcurrency));
            params.addProperty(AppoConstants.TOCURRENCYCODE, fomrcurrencycode);
            params.addProperty(AppoConstants.TRANSACTIONPIN, userTransactionPin);
            params.addProperty(AppoConstants.USERID, Long.parseLong(objResult.getString(AppoConstants.ID)));

            makeTransferMoney(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void makeTransferMoney(JsonObject sentParams) {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait, Sending your request");
        dialog.show();
        String accessToken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);


        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        mainAPIInterface.postTransferFund(sentParams, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    String res = new Gson().toJson(response.body());
                    try {
                        JSONObject responsePayment = new JSONObject(res);
                        if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("-1")) {
                            showCommonError(getString(R.string.error_invalid_transaction_pin));

                        } else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("-2")) {
                            showCommonError(getString(R.string.error_account_balance));

                        } else {

                            showPayDialogLikeUnion(responsePayment.getString(AppoConstants.RESULT));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {


                    if (response.code() == 401) {
                        Toast.makeText(getActivity(), "Session Expired!!!", Toast.LENGTH_SHORT).show();
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.putExtra(AppoConstants.WHERE, mType);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    } else if (response.code() == 500) {
                        Toast.makeText(getActivity(), "Error Code 500", Toast.LENGTH_SHORT).show();


                    } else if (response.code() == 400) {
                        Toast.makeText(getActivity(), "Error Code 400", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();

            }
        });


    }

    public void showCommonError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        ImageView ivCancel = dialogLayout.findViewById(R.id.ivCancel);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.wallet_header));
        tvInfo.setText(message);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        ivCancel.setVisibility(View.VISIBLE);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTransfer.dismiss();
            }
        });

        builder.setView(dialogLayout);

        dialogTransfer = builder.create();

        dialogTransfer.setCanceledOnTouchOutside(false);

        dialogTransfer.show();
    }

    private void showPayDialogLikeUnion(String param) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mCustomLayout = LayoutInflater.from(getActivity()).inflate(R.layout.success_dialog_inner_appopay_transfer, null);
        LinearLayout layoutRoot = mCustomLayout.findViewById(R.id.layoutRoot);
        MyTextView tvInfo = mCustomLayout.findViewById(R.id.tvInfo);
        MyTextView tvHeader = mCustomLayout.findViewById(R.id.tvHeader);
        MyTextViewBold tvAmountPay = mCustomLayout.findViewById(R.id.tvAmountPay);
        MyTextViewBold tvCost = mCustomLayout.findViewById(R.id.tvCost);
        MyTextViewBold tvReceiverAmt = mCustomLayout.findViewById(R.id.tvReceiverAmt);
        MyTextView tvCurrencyPay = mCustomLayout.findViewById(R.id.tvCurrencyPay);
        MyTextView tvTransactionTime = mCustomLayout.findViewById(R.id.tvTransactionTime);
        MyTextView tvVoucherPay = mCustomLayout.findViewById(R.id.tvVoucherPay);
        MyButton btnShare = mCustomLayout.findViewById(R.id.btnShare);
        MyButton btnClose = mCustomLayout.findViewById(R.id.btnClose);
        tvHeader.setText("Transfer Money");
        tvAmountPay.setText(" Amount : " + edAmount.getText().toString().trim());
        tvReceiverAmt.setText("Receiver Amount : " + tvAmountCredit.getText().toString().trim());
        tvReceiverAmt.setTextColor(Color.parseColor("#334CFF"));
        float cost = amountaftertax_fees - Float.parseFloat(edAmount.getText().toString().trim());
        float twoDecimal = Helper.getTwoDecimal(cost);
        tvCost.setText("Transaction Cost : " + twoDecimal);
        tvCost.setTextColor(Color.parseColor("#FE3156"));

        String currencyId = Helper.getCurrencyId();
        String mCurrencyId = "";

        for (int i = 0; i < resultCurrency.size(); i++) {
            if (currencyId.equals(String.valueOf(resultCurrency.get(i).getId()))) {
                mCurrencyId = resultCurrency.get(i).getCurrencyCode();
                //Log.e(TAG, "showPayDialogLikeUnion: "+mCurrencyId );
                break;
            }
        }



        tvCurrencyPay.setText("Currency : " + mCurrencyId);
        tvTransactionTime.setText("Transaction Time : " + getDateTime());
        tvVoucherPay.setText("Transaction No : " + param);
        tvInfo.setText("Paid to " + recname + "" + "\nSUCCESS");

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mDialog.dismiss();
                getActivity().onBackPressed();*/
                redirectHome();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        return format;
    }

    private Bitmap getScreenShot(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;

    }

    private void takeScreenShort(LinearLayout rootLayout) {
        mDialog.dismiss();

        Bitmap bitmap = getScreenShot(rootLayout);
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "/YourDirName";
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/YourDirName";
        }

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        String uniqueFileName = Helper.getUniqueFileName();
        mFileSSort = new File(dir, uniqueFileName);

        try {
            boolean newFile = mFileSSort.createNewFile();
            if (newFile) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
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

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        String absolutePath = imageFile.getAbsolutePath();
        intent.putExtra("link", absolutePath);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
        /*Intent intentShareFile = new Intent();
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
        startActivityForResult(chooser, 198);*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 198) {

            Log.e(TAG, "onActivityResult: called 198 ");

        }
    }


    private void redirectHome() {
        if (mDialog.isShowing() && mDialog != null) {
            mDialog.dismiss();
        }
        mMoneyTransfer.OnMoneyTransferSuccess();


    }

    /*private void getReceiverToken() {
        Query mDatabaseQuery = FirebaseDatabase.getInstance().getReference(AppoConstants.FIREBASE_USERS_NODE)
                .orderByChild(AppoConstants.EMIAL_ID)
                .equalTo(receiverEmail);
        mList = new ArrayList<ChatUser>();
        mDatabaseQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            mList.clear();
            if (snapshot.exists()) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatUser chatUser = dataSnapshot.getValue(ChatUser.class);
                    mList.add(chatUser);
                }
                if (mList.size() > 0) {
                    sendNotification();

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void sendNotification() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        dialog.show();
        mainAPIInterface2 = ApiUtils.getApiServiceForNotification("https:
        Query mDatabaseQuery = FirebaseDatabase.getInstance().getReference(AppoConstants.FIREBASE_USER_TOKENS)
                .orderByKey()
                .equalTo(mList.get(0).getId());
        mDatabaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismiss();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Token token = dataSnapshot.getValue(Token.class);
                        String message = "An amount of (" + tvAmountCredit.getText().toString().trim() + " " + fomrcurrencycode + ")" + " has been credit to your wallet account during the e-wallet transfer";
                        JsonObject jsonParam = new JsonObject();
                        jsonParam.addProperty(AppoConstants.MESSAGE, message);
                        String sentParam = jsonParam.toString();
                        Data data = new Data(AppoConstants.WALLET_TRANSFER, 1, "Wallet Transfer", sentParam,
                                mList.get(0).getId());
                        Sender sender = new Sender(data, token.getToken());

                        mainAPIInterface2.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                                if (response.code() == 200) {
                                    if (response.body().success == 1) {

                                        mMoneyTransfer.OnMoneyTransferSuccess();
                                    } else {
                                        mMoneyTransfer.OnMoneyTransferSuccess();
                                    }
                                } else {
                                    mMoneyTransfer.OnMoneyTransferSuccess();
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                                dialog.dismiss();
                                mMoneyTransfer.OnMoneyTransferSuccess();
                            }
                        });

                    }
                } else {

                    mMoneyTransfer.OnMoneyTransferSuccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                mMoneyTransfer.OnMoneyTransferSuccess();
            }
        });
    }*/

    public void showToast(String msg) {
        Toast.makeText(getContext(), "" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mMoneyTransfer = (MoneyTransferListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent must implement MoneyTransferListener");
        }

    }

    public void getCommission(String pin) {
        if (bottotmPinFragment != null)
            bottotmPinFragment.dismiss();
        getCommissions(pin);
    }


}