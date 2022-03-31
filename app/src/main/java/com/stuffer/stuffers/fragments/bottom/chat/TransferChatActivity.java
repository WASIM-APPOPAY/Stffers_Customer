package com.stuffer.stuffers.fragments.bottom.chat;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.adapter.recyclerview.ActiveAccountAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferChatActivity extends AppCompatActivity implements TransactionPinListener {
    private static final String TAG = "TransferChatActivity";
    private ProgressDialog dialog;
    private String recieverareacode, recivermobilenumber, recivername, reciveruserid, reciveremail;
    private MainAPIInterface mainAPIInterface;
    private JSONObject indexUser;
    private String currencyResponse;
    private List<CurrencyResult> resultCurrency;
    private ArrayList<AccountModel> mListAccount;
    private MyTextView tvFromAccount;
    private MyTextView tvBalance1;
    private MyTextViewBold tvName;
    private MyTextViewBold tvName1;
    private MyTextView tvBalance;
    private MyTextView tvToAccount;
    private MyEditText edAmount;
    private MyTextView tvAmountCredit;
    private MyTextView tvConversionRates;
    private MyButton btnTransfer;
    private String fomrcurrencycode, recaccountnumber, recmobilenumber, recareacode, recname, recuserid, fromcurrency, receiveruser;
    private String reciveraccountnumber;
    private String receiverEmail;
    private TextView toolbarTitle;
    private String currantBalance;
    private BottotmPinFragment bottotmPinFragment;
    private String userTransactionPin;
    private JSONObject jsonCommission;
    private float bankfees;
    private float processingfees;
    private float finaamount;
    private float amountaftertax_fees;
    private float taxes;
    private AlertDialog dialogTransfer;
    private AlertDialog mDialog;
    private File mFileSSort;
    private String mFromCId, mFromCCode;

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("P-2-P Transfer");

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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

        finish();
        /*int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            super.onBackPressed();
            finish();
        } else if (backStackEntryCount == 2) {
            String toolbarTitle = "e-TimePayTrack" + "<br>" + "<small><i>" + " OD History" + "</i></small>";
            // tvHeader.setText(Html.fromHtml(toolbarTitle));
            getSupportFragmentManager().popBackStackImmediate();
        }*/

        /*Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
            super.onBackPressed();
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_chat);
        setupActionBar();
        mainAPIInterface = ApiUtils.getAPIService();
        String mAmount = getIntent().getStringExtra(AppoConstants.AMOUNT);
        String mAreaCode = getIntent().getStringExtra(AppoConstants.AREACODE);
        String mPhWithCode = getIntent().getStringExtra(AppoConstants.PHWITHCODE);

        tvFromAccount = (MyTextView) findViewById(R.id.tvFromAccount);
        tvName = (MyTextViewBold) findViewById(R.id.tvName);
        tvName1 = (MyTextViewBold) findViewById(R.id.tvName1);
        tvBalance = (MyTextView) findViewById(R.id.tvBalance);
        tvBalance1 = (MyTextView) findViewById(R.id.tvBalance1);
        tvToAccount = (MyTextView) findViewById(R.id.tvToAccount);
        edAmount = (MyEditText) findViewById(R.id.edAmount);
        tvAmountCredit = (MyTextView) findViewById(R.id.tvAmountCredit);
        tvConversionRates = (MyTextView) findViewById(R.id.tvConversionRates);
        btnTransfer = (MyButton) findViewById(R.id.btnTransfer);
        try {
            edAmount.setText(mAmount);
            Helper.hideKeyboard(edAmount, TransferChatActivity.this);
        }catch (Exception e){
            e.printStackTrace();
        }
        tvFromAccount.setText(Helper.getWalletAccountNumber());
        String senderName = Helper.getSenderName();
        tvName.setText(senderName);
        currantBalance = Helper.getCurrantBalance();
        DecimalFormat df2 = new DecimalFormat("#.00");
        Double doubleV = Double.parseDouble(currantBalance);
        String format = df2.format(doubleV);
        tvBalance.setText("$" + format);
        mFromCId = Helper.getCurrencyId();
        if (mFromCId.equalsIgnoreCase("1")) {
            mFromCCode = "USD";
        } else if (mFromCId.equalsIgnoreCase("2")) {

            mFromCCode = "INR";
        } else if (mFromCId.equalsIgnoreCase("3")) {

            mFromCCode = "CAD";
        } else if (mFromCId.equalsIgnoreCase("4")) {

            mFromCCode = "ERU";
        } else if (mFromCId.equalsIgnoreCase("5")) {

            mFromCCode = "DOP";
        }
        String substring = mPhWithCode.substring(mAreaCode.length());
        Log.e(TAG, "onCreate: phone number : "+substring );
        onSearchRequest(substring, mAreaCode);

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
                        float transfer = (float) (tranaferAmount * 1);
                        float twoDecimal = (float) Helper.getTwoDecimal(transfer);
                        tvAmountCredit.setText(String.valueOf(twoDecimal));
                        btnTransfer.setEnabled(true);
                        btnTransfer.setClickable(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (edAmount.getText().toString().trim().isEmpty()) {
                        //no need to show invalid format
                    } else {
                        Toast.makeText(TransferChatActivity.this, getString(R.string.info_invalid_format), Toast.LENGTH_SHORT).show();
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
            }
        });

    }

    public void showDialog() {
        dialog = new ProgressDialog(TransferChatActivity.this);
        dialog.setMessage(getString(R.string.info_getting_user_account));
        dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void onSearchRequest(String ph, String area) {

        showDialog();
        recieverareacode = area;
        recivermobilenumber = ph;

        String accessToken = DataVaultManager.getInstance(TransferChatActivity.this).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        mainAPIInterface.getProfileDetails(Long.parseLong(ph), Integer.parseInt(area), bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dismissDialog();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    //Log.e(TAG, "onResponse: getprofile :" + res);
                    try {
                        indexUser = new JSONObject(res);
                        if (indexUser.isNull("result")) {
                            //Log.e(TAG, "onResponse: " + true);
                            Toast.makeText(TransferChatActivity.this, getString(R.string.error_user_details_not_exists), Toast.LENGTH_SHORT).show();
                        } else {
                            getCurrency();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(TransferChatActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(TransferChatActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(TransferChatActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 400) {
                        Toast.makeText(TransferChatActivity.this, getString(R.string.error_bad_request), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dismissDialog();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private void getCurrency() {
        dialog = new ProgressDialog(TransferChatActivity.this);
        dialog.setMessage(getString(R.string.info_getting_currency_code));
        dialog.show();

        mainAPIInterface.getCurrencyResponse().enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    currencyResponse = new Gson().toJson(response.body().getResult());
                    resultCurrency = response.body().getResult();
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

        //Log.e(TAG, "readUserAccounts: " + vaultValue);
        String nameWithMobile = null;
        try {
            JSONObject root = indexUser;
            JSONObject objResult = root.getJSONObject(AppoConstants.RESULT);
            nameWithMobile = objResult.getString(AppoConstants.FIRSTNAME) + " " + objResult.getString(AppoConstants.LASTNAME) + "-" + objResult.getString(AppoConstants.MOBILENUMBER);
            //tvUserMobileName.setText(nameWithMobile);
            recivername = objResult.getString(AppoConstants.FIRSTNAME) + " " + objResult.getString(AppoConstants.LASTNAME);
            reciveruserid = objResult.getString(AppoConstants.ID);
            reciveremail = objResult.getString(AppoConstants.EMIAL);

            JSONObject objCustomerDetails = objResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            JSONArray arrCustomerAccount = objCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            // for (int i = 0; i < arrCustomerAccount.length(); i++) {
            for (int i = 0; i < 1; i++) {
                JSONObject index = arrCustomerAccount.getJSONObject(i);
                AccountModel model = new AccountModel();
                model.setAccountnumber(index.getString(AppoConstants.ACCOUNTNUMBER));
                model.setAccountEncrypt(null);
                if (index.has(AppoConstants.ACCOUNTSTATUS)) {
                    //Log.e(TAG, "readUserAccounts: AccountStatus : " + index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                    model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                    model.setCurrentbalance(index.getString(AppoConstants.CURRENTBALANCE));
                    mListAccount.add(model);
                } /*else {
                    //Log.e(TAG, "readUserAccounts: AccountStatus : " + "null");
                    model.setAccountstatus("");
                }*/

            }

            if (mListAccount.size() > 0) {
                Log.e(TAG, "readUserAccounts: ");
                JSONObject objReceiver = new JSONObject();
                objReceiver.put(AppoConstants.RECIEVERACCOUNTNUMBER, mListAccount.get(0).getAccountnumber());
                objReceiver.put(AppoConstants.FROMCURRENCY, mListAccount.get(0).getCurrencyid());
                objReceiver.put(AppoConstants.FROMCURRENCYCODE, mListAccount.get(0).getCurrencyCode());
                objReceiver.put(AppoConstants.RECEIVERMOBILENUMBER, recivermobilenumber);
                objReceiver.put(AppoConstants.RECEIVERAREACODE, recieverareacode);
                objReceiver.put(AppoConstants.RECIEVERNAME, recivername);
                objReceiver.put(AppoConstants.RECIEVERUSERID, reciveruserid);
                objReceiver.put(AppoConstants.EMIAL, reciveremail);
                setReceiverDetails(objReceiver.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setReceiverDetails(String objReceiver) {
        try {
            JSONObject obj = new JSONObject(objReceiver);
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

            tvName1.setText(recname);
            tvBalance1.setText("+" + recareacode + "" + recmobilenumber);
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

    public void showToast(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    private void verifyDetails() {
        Helper.hideKeyboard(edAmount, TransferChatActivity.this);

        if (tvFromAccount.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.info_selecr_from_account_first));
            return;
        }

        if (edAmount.getText().toString().trim().isEmpty()) {
            //showToast("please enter transfer amount");
            showToast(getString(R.string.info_enter_transer_amount));
            return;
        }

        try {
            Helper.getTwoDecimal(Float.parseFloat(edAmount.getText().toString().trim()) * 1);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(TransferChatActivity.this, getString(R.string.info_invalid_format), Toast.LENGTH_SHORT).show();
            return;
        }
        /*if (reciveraccountnumber.equalsIgnoreCase(mListAccount.get(0).getAccountnumber())) {
            showSameAccountErrors();
            return;
        }*/

        if (Float.parseFloat(edAmount.getText().toString().trim()) >= Float.parseFloat(currantBalance)) {
            showBalanceErrorDailog();
        } else {
            showBottomPinDialog();
        }


    }

    private void showBottomPinDialog() {
        bottotmPinFragment = new BottotmPinFragment();
        bottotmPinFragment.show(getSupportFragmentManager(), bottotmPinFragment.getTag());
        bottotmPinFragment.setCancelable(false);

    }


    @Override
    public void onPinConfirm(String pin) {
        if (bottotmPinFragment != null)
            bottotmPinFragment.dismiss();
        getCommissions(pin);

    }


    private void getCommissions(String transaction) {
        userTransactionPin = transaction;
        //Log.e(TAG, "getCommissions: pin : " + transaction);
        dialog = new ProgressDialog(TransferChatActivity.this);
        dialog.setMessage(getString(R.string.info_conversion_rate));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getAllTypeTransferCommissions(bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    try {
                        jsonCommission = new JSONObject(res);
                        calculateCommission();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    if (response.code() == 401) {
                        DataVaultManager.getInstance(TransferChatActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(TransferChatActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(TransferChatActivity.this, SignInActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(TransferChatActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_about_topay_common, null);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnNo = dialogLayout.findViewById(R.id.btnNo);
        tvHeader.setText(getString(R.string.wallet_header));
        String boldText = "<font color=''><b>" + amountaftertax_fees + "</b></font>" + " " + "<font color=''><b>" + mFromCCode.toUpperCase() + "</b></font>";
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
        //Log.e(TAG, "makePayment: called");
        if (dialogTransfer != null) {
            dialogTransfer.dismiss();
        }
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        JsonObject params = new JsonObject();
        params.addProperty(AppoConstants.AMOUNT, edAmount.getText().toString().trim());//need to add here
        params.addProperty(AppoConstants.CHARGES, String.valueOf(bankfees));
        params.addProperty(AppoConstants.CONVERSIONRATE, 1);
        params.addProperty(AppoConstants.ENTEREDAMOUNT, edAmount.getText().toString().trim());
        params.addProperty(AppoConstants.FEES, String.valueOf(processingfees));
        params.addProperty(AppoConstants.FROMCURRENCY, Integer.parseInt(fromcurrency));
        params.addProperty(AppoConstants.FROMCURRENCYCODE, fomrcurrencycode);
        params.addProperty(AppoConstants.ORIGINALAMOUNT, amountaftertax_fees);
        params.addProperty(AppoConstants.TAXES, taxes);
        //params.addProperty(AppoConstants.USERTYPE, "CUSTOMER");
        params.addProperty(AppoConstants.RECIEVERACCOUNTNUMBER, recaccountnumber);
        params.addProperty(AppoConstants.RECEIVERAREACODE, Integer.parseInt(recareacode));
        params.addProperty(AppoConstants.RECIEVERNAME, recname);
        params.addProperty(AppoConstants.RECIEVERUSERID, Integer.parseInt(recuserid));
        params.addProperty("recivermobilenumber", Long.parseLong(recmobilenumber));
        //params.addProperty(AppoConstants.SENDERACCOUNTNUMBER, mListAccount.get(mFromPosition).getAccountnumber());
        params.addProperty(AppoConstants.SENDERACCOUNTNUMBER, Helper.getWalletAccountNumber());
        try {
            JSONObject jsonUser = new JSONObject(vaultValue);
            JSONObject objResult = jsonUser.getJSONObject(AppoConstants.RESULT);
            params.addProperty(AppoConstants.SENDERAREACODE, Integer.parseInt(objResult.getString(AppoConstants.PHONECODE)));
            params.addProperty(AppoConstants.SENDERMOBILENUMBER, Long.parseLong(objResult.getString(AppoConstants.MOBILENUMBER)));
            String senderName = objResult.getString(AppoConstants.FIRSTNAME) + " " + objResult.getString(AppoConstants.LASTNAME);
            params.addProperty(AppoConstants.SENDERNAME, senderName);
            params.addProperty(AppoConstants.TOCURRENCY, Integer.parseInt(mFromCId));
            params.addProperty(AppoConstants.TOCURRENCYCODE, mFromCCode);
            params.addProperty(AppoConstants.TRANSACTIONPIN, userTransactionPin);
            params.addProperty(AppoConstants.USERID, Long.parseLong(objResult.getString(AppoConstants.ID)));
            //Log.e(TAG, "makePayment: 1" + params.toString());
            makeTransferMoney(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void makeTransferMoney(JsonObject sentParams) {
        dialog = new ProgressDialog(TransferChatActivity.this);
        dialog.setMessage("Please wait, Sending your request");
        dialog.show();
        String accessToken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        ////Log.e(TAG, "makeTransferMoney: " + accessToken);

        //Log.e(TAG, "makeTransferMoney: ======================");
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        mainAPIInterface.postTransferFund(sentParams, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                    String res = new Gson().toJson(response.body());
                    try {
                        JSONObject responsePayment = new JSONObject(res);
                        if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("-1")) {
                            showCommonError(getString(R.string.error_invalid_transaction_pin));
                            //showPayDialogLikeUnion("#1245454");
                        } else if (responsePayment.getString(AppoConstants.RESULT).equalsIgnoreCase("-2")) {
                            showCommonError(getString(R.string.error_account_balance));
                            //showPayDialogLikeUnion("#1245454");
                        } else {
                            //showSuccessDialog(responsePayment.getString(AppoConstants.RESULT));
                            showPayDialogLikeUnion(responsePayment.getString(AppoConstants.RESULT));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    //showCommonError(getString(R.string.error_invalid_transaction_pin));
                    //showPayDialogLikeUnion("#1245454");
                    if (response.code() == 401) {
                        Toast.makeText(TransferChatActivity.this, "Session Expired!!!", Toast.LENGTH_SHORT).show();
                        DataVaultManager.getInstance(TransferChatActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(TransferChatActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(TransferChatActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 500) {
                        Toast.makeText(TransferChatActivity.this, "Error Code 500", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 400) {
                        Toast.makeText(TransferChatActivity.this, "Error Code 400", Toast.LENGTH_SHORT).show();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(TransferChatActivity.this);
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
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(TransferChatActivity.this);
        View mCustomLayout = LayoutInflater.from(TransferChatActivity.this).inflate(R.layout.success_dialog_inner_appopay, null);
        LinearLayout layoutRoot = mCustomLayout.findViewById(R.id.layoutRoot);
        MyTextView tvInfo = mCustomLayout.findViewById(R.id.tvInfo);
        MyTextView tvHeader = mCustomLayout.findViewById(R.id.tvHeader);
        MyTextViewBold tvAmountPay = mCustomLayout.findViewById(R.id.tvAmountPay);//edAmount
        MyTextView tvCurrencyPay = mCustomLayout.findViewById(R.id.tvCurrencyPay);
        MyTextView tvTransactionTime = mCustomLayout.findViewById(R.id.tvTransactionTime);
        MyTextView tvVoucherPay = mCustomLayout.findViewById(R.id.tvVoucherPay);
        MyButton btnShare = mCustomLayout.findViewById(R.id.btnShare);
        MyButton btnClose = mCustomLayout.findViewById(R.id.btnClose);
        tvHeader.setText("Transfer Money");
        tvAmountPay.setText("Amount : " + edAmount.getText().toString().trim());
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
        tvCurrencyPay.setText("Currency : " + mCurrencyId);
        tvTransactionTime.setText("Transaction Time : " + getDateTime());
        tvVoucherPay.setText("Transaction No : " + param);
        tvInfo.setText("Paid to " + recname + "" + "\nSUCCESS");

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                redirectHome();
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
        //Log.e(TAG, "getDate: " + format);
        return format;
    }

    private void redirectHome() {
        if (mDialog.isShowing() && mDialog != null) {
            mDialog.dismiss();
        }
        //mMoneyTransfer.OnMoneyTransferSuccess();
        //getActivity().onBackPressed();
        //Intent intent = new Intent();
        //getReceiverToken();
        super.onBackPressed();
        finish();


    }

    private void takeScreenShort(LinearLayout rootLayout) {
        mDialog.dismiss();
        //Bitmap bitmap = screenShot(rootLayout);
        Bitmap bitmap = getScreenShot(rootLayout);
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        mFileSSort = new File(getCacheDir(), "screen_short_" + now + ".jpeg");
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

    private void openScreenshot(File imageFile) {
        Intent intentShareFile = new Intent();
        intentShareFile.setAction(Intent.ACTION_SEND);
        Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), "com.stuffer.stuffers.fileprovider", imageFile);
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentShareFile.setType("image/jpeg");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uriForFile);
        Intent chooser = Intent.createChooser(intentShareFile, "Share File");
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uriForFile, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
      startActivityForResult(chooser,198);

    }

    private void showBalanceErrorDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransferChatActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: called 197 ");
        redirectHome();
        /*if (resultCode == 198) {
            Log.e(TAG, "onActivityResult: called 198 ");
*/
        }
    }
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 198) {
            Log.e(TAG, "onActivityResult: called 198 ");
            redirectHome();
        }
    }*/
