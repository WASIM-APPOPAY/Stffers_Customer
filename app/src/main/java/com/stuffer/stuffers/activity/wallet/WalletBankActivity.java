package com.stuffer.stuffers.activity.wallet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.AccountSelectedListener;
import com.stuffer.stuffers.communicator.ConfirmSelectListener;
import com.stuffer.stuffers.communicator.CountrySelectListener;
import com.stuffer.stuffers.fragments.dialog.AccountTypesDialogFragment;
import com.stuffer.stuffers.fragments.dialog.CountryDialogFragment;
import com.stuffer.stuffers.models.Country.CountryCodeResponse;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.models.bank.BankCurrencyResponse;
import com.stuffer.stuffers.models.bank.BankNameResponse;
import com.stuffer.stuffers.models.bank.account.BankAccResponse;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyTextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class WalletBankActivity extends AppCompatActivity implements CountrySelectListener, ConfirmSelectListener, AccountSelectedListener {
    String areacode, mobileNumber;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private static final String TAG = "WalletBankActivity";
    private List<CurrencyResult> result;
    ArrayList<AccountModel> mListAccount;
    MyTextView tvFromAccount, tvSelectCountry, tvSelectBank, tvSelectBankCurrency, tvAccountType, tvDebitDate;
    MyTextView tvConversionRates, tvAmountCredit;
    MyEditText edAccountHolderName, edBankAccountNumber;
    List<Result> mListCountry;
    LinearLayout layoutCountry, layoutBankName, layoutAccountType, layoutDebitDate, layoutHolderName, layoutAccountNumber;
    private int mSelectedCountryId;
    private String mSelectedCountryName;
    List<com.stuffer.stuffers.models.bank.Result> mListBank;
    List<BankCurrencyResponse.Result> resultBanckCurrency;
    private int idFromBank;
    private String bankName;
    private String bankCode;
    private MyTextView tvBankCode;
    List<com.stuffer.stuffers.models.bank.account.Result> resultAccTypes;
    private AccountTypesDialogFragment dialogFragment;
    private int mAccountTypePosition;
    private String mAccountTypeName;
    private String mDob;
    private Calendar newCalendar;
    private MyEditText edAmount;
    private float conversionRates = 0;
    private MyButton btnTransfer;
    private AlertDialog dialogFund;
    private String userTransactionPin;
    private JSONObject jsonCommission;
    private float bankfees;
    private float processingfees;
    private float finaamount;
    private float amountaftertax_fees;
    private float taxes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_bank);
        mainAPIInterface = ApiUtils.getAPIService();
        mListCountry = new ArrayList<>();
        newCalendar = Calendar.getInstance();
        findViews();
        edAmount.setEnabled(false);
        setupActionBar();
        getUserDetailsForProfile();
        layoutCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryDialogFragment countryDialogFragment = new CountryDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppoConstants.COUNTRY, (ArrayList<? extends Parcelable>) mListCountry);
                countryDialogFragment.setArguments(bundle);
                countryDialogFragment.setCancelable(false);
                countryDialogFragment.show(getSupportFragmentManager(), countryDialogFragment.getTag());
            }
        });

        layoutBankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListBank == null) {
                    showToast(getString(R.string.info_select_county));
                } else {
                    Intent intentBank = new Intent(WalletBankActivity.this, BankNameActivity.class);
                    intentBank.putParcelableArrayListExtra(AppoConstants.INFO, (ArrayList<? extends Parcelable>) mListBank);
                    startActivityForResult(intentBank, AppoConstants.BANK_NAME_REQUEST);
                }
            }
        });

        layoutAccountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment = new AccountTypesDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppoConstants.INFO, (ArrayList<? extends Parcelable>) resultAccTypes);
                dialogFragment.setArguments(bundle);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());

            }
        });

        layoutDebitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvSelectCountry.getText().toString().trim().isEmpty()) {
                    showToast(getString(R.string.info_select_county));
                    return;
                }


                if (tvSelectBankCurrency.getText().toString().trim().isEmpty()) {
                    showToast(getString(R.string.info_select_bank));
                    return;
                }
                Calendar minCal = Calendar.getInstance();
                minCal.set(1950, 0, 1);
                DatePickerDialog StartTime = new DatePickerDialog(WalletBankActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM");
                        mDob = dateFormatter.format(newDate.getTime());
                        tvDebitDate.setText("Date : " + dateFormatter.format(newDate.getTime()));
                        //Log.e(TAG, "onDateSet: " + dateFormatter.format(newDate.getTime()));
                        edAmount.setEnabled(true);
                        getBaseRate();
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                StartTime.getDatePicker().setMaxDate(new Date().getTime());
                //StartTime.getDatePicker().setMinDate(minCal.getTimeInMillis());
                StartTime.getDatePicker().setMinDate(new Date().getTime());
                StartTime.setCanceledOnTouchOutside(false);
                StartTime.show();
            }
        });

        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (conversionRates == 0) {
                    showToast(getString(R.string.info_select_bank));
                    return;
                }
                try {
                    float twoDecimal =(float) Helper.getTwoDecimal(Float.parseFloat(edAmount.getText().toString().trim()) * conversionRates);
                    tvAmountCredit.setText(String.valueOf(twoDecimal));
                } catch (Exception e) {
                    if (edAmount.getText().toString().trim().isEmpty()) {
                        //do nothing user is using back press for inserting new digits
                    } else {
                        Toast.makeText(WalletBankActivity.this, "invalid format", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    public void getBaseRate() {
        //String url = "https://api.exchangeratesapi.io/latest?base=" + mListAccount.get(0).getCurrencyCode();
        String url = "https://api.exchangeratesapi.io/latest?base=" + tvSelectBankCurrency.getText().toString().trim();
        //Log.e(TAG, "getBaseRate: " + url);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.show();
        AndroidNetworking.get(url)
                .setOkHttpClient(AppoPayApplication.getOkHttpClient2(10))
                .setPriority(Priority.IMMEDIATE)
                .setTag("base conversion")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        //Log.e(TAG, "onResponse: base :: " + response);
                        if (response.has("base")) {
                            //Log.e(TAG, "onResponse: true");
                            sentParam(response);
                        } else {
                            //Log.e(TAG, "onResponse: false");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                    }
                });

    }

    private void sentParam(JSONObject response) {

        try {
            //JSONObject response = new JSONObject(sentBaseConversion);
            JSONObject jsonRates = response.getJSONObject(AppoConstants.RATES);
            conversionRates = Float.parseFloat(jsonRates.getString(mListAccount.get(0).getCurrencyCode().toUpperCase()));
            tvConversionRates.setText(String.valueOf(conversionRates));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void findViews() {
        tvFromAccount = findViewById(R.id.tvFromAccount);
        tvSelectCountry = findViewById(R.id.tvSelectCountry);
        tvSelectBank = findViewById(R.id.tvSelectBank);
        tvBankCode = findViewById(R.id.tvBankCode);
        tvSelectBankCurrency = findViewById(R.id.tvSelectBankCurrency);
        tvAccountType = findViewById(R.id.tvAccountType);
        tvDebitDate = findViewById(R.id.tvDebitDate);
        layoutDebitDate = findViewById(R.id.layoutDebitDate);
        edAmount = findViewById(R.id.edAmount);
        layoutBankName = findViewById(R.id.layoutBankName);
        layoutCountry = findViewById(R.id.layoutCountry);
        layoutAccountType = findViewById(R.id.layoutAccountType);

        layoutHolderName = findViewById(R.id.layoutHolderName);
        edAccountHolderName = findViewById(R.id.edAccountHolderName);

        layoutAccountNumber = findViewById(R.id.layoutAccountNumber);
        edBankAccountNumber = findViewById(R.id.edBankAccountNumber);

        tvConversionRates = findViewById(R.id.tvConversionRates);
        tvAmountCredit = findViewById(R.id.tvAmountCredit);
        btnTransfer = findViewById(R.id.btnTransfer);

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvSelectCountry.getText().toString().trim().isEmpty()) {
                    showToast(getString(R.string.info_select_county));
                    return;
                }

                if (tvSelectBank.getText().toString().trim().isEmpty()) {
                    showToast(getString(R.string.info_select_bank));
                    return;
                }

                if (tvAccountType.getText().toString().trim().isEmpty()) {
                    showToast(getString(R.string.info_select_account_type));
                    return;
                }

                if (edAccountHolderName.getText().toString().trim().isEmpty()) {
                    showToast(getString(R.string.info_account_holder_name));
                    return;
                }

                if (edBankAccountNumber.getText().toString().trim().isEmpty()) {
                    showToast(getString(R.string.info_bank_account_number));
                    return;
                }

                if (edAmount.getText().toString().trim().isEmpty()) {
                    showToast(getString(R.string.info_enter_transfer_ammount));
                    return;
                }
                showTransactionDialog();


            }
        });
    }

    public void showTransactionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_transaction_pin_common, null);

        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        MyButton btnConfirm = dialogLayout.findViewById(R.id.btnConfirm);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.info_fund_wallet));

        final MyEditText edtTransactionPin = dialogLayout.findViewById(R.id.edtTransactionPin);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFund.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTransactionPin.getText().toString().trim().isEmpty()) {
                    edtTransactionPin.setError("Enter transaction pin");
                    edtTransactionPin.requestFocus();
                    return;
                }
                //Log.e(TAG, "onClick: length :  " + edtTransactionPin.getText().toString().trim().length());

                if (edtTransactionPin.getText().toString().trim().length() < 6) {
                    edtTransactionPin.setError("Transaction pin should be in 6 digit");
                    edtTransactionPin.requestFocus();
                    return;
                }

                Helper.hideKeyboard(edtTransactionPin, WalletBankActivity.this);
                getFundCommissions(edtTransactionPin.getText().toString().trim());
            }
        });


        builder.setView(dialogLayout);

        dialogFund = builder.create();

        dialogFund.setCanceledOnTouchOutside(false);

        dialogFund.show();
    }


    private void getFundCommissions(String transaction) {
        userTransactionPin = transaction;
        //Log.e(TAG, "getCommissions: pin : " + transaction);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait, Getting conversion rate");
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getBankFundCommission(bearer_).enqueue(new Callback<JsonObject>() {
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
                        DataVaultManager.getInstance(WalletBankActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(WalletBankActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(WalletBankActivity.this, SignInActivity.class);
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
        //Log.e(TAG, "calculateCommission: " + jsonCommission.toString());
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
            bankfees = (float)Helper.getTwoDecimal(bankcommission * fundamount);
            float newamount =(float) Helper.getTwoDecimal(fundamount + bankfees);
            processingfees = 0;
            processingfees = (float)Helper.getTwoDecimal(fundamount * processingCommission);
            finaamount = (float)Helper.getTwoDecimal(newamount + processingfees);
            bankfees =(float) Helper.getTwoDecimal(bankfees + flatbankcomission);
            processingfees = (float)Helper.getTwoDecimal(processingfees + flatprocessingcomission);
            float flatfees = (float)Helper.getTwoDecimal(flatbankcomission + flatprocessingcomission);
            finaamount = (float)(finaamount + Helper.getTwoDecimal(flatfees));
            amountaftertax_fees = 0;
            taxes = 0;
            if (taxon.equalsIgnoreCase(AppoConstants.FEES)) {
                taxes = (float)Helper.getTwoDecimal(((processingfees * taxPercentage) / 100));
                amountaftertax_fees =(float) Helper.getTwoDecimal(fundamount + ((processingfees * taxPercentage) / 100) + processingfees);
                //Log.e(TAG, "calculateCommission: if called");
            } else {
                taxes =(float) Helper.getTwoDecimal((fundamount * taxPercentage) / 100);
                amountaftertax_fees = (float)Helper.getTwoDecimal(fundamount + ((fundamount * taxPercentage) / 100) + processingfees);
                //Log.e(TAG, "calculateCommission: else caleed");
            }
            showYouAboutToPay();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showYouAboutToPay() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_about_topay_common, null);

        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnNo = dialogLayout.findViewById(R.id.btnNo);
        tvInfo.setText(getString(R.string.info_fund_wallet));
        //String boldText = "<font color=''><b>" + finaamount + "</b></font>" + " " + "<font color=''><b>" + mListAccount.get(fromAccountPosition).getCurrencyCode() + "</b></font>";
        //String boldText = "<font color=''><b>" + tvAmountCredit.getText().toString().trim() + "</b></font>" + " " + "<font color=''><b>" + mListAccount.get(fromAccountPosition).getCurrencyCode() + "</b></font>";
        //String paymentAmount = getString(R.string.recharge_partial_pay1) + " " + finalamount + " " + senderCurrencyCode + " " + getString(R.string.recharge_partial_pay2);
        String boldText = "<font color=''><b>" + amountaftertax_fees + "</b></font>" + " " + "<font color=''><b>" + mListAccount.get(0).getCurrencyCode() + "</b></font>";

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
                dialogFund.dismiss();
            }
        });

        builder.setView(dialogLayout);
        dialogFund = builder.create();
        dialogFund.setCanceledOnTouchOutside(false);
        dialogFund.show();
    }

    /**
     * {
     * amount: "515.75",..
     * bankaccountnumber: "12445544",..
     * bankname: "BANISTMO S.A.",..
     * charges: "1.40",..
     * conversionrate: 73.6784424961,..
     * countryname: "PANAMA",..
     * depositdate: "Fri Sep 25 2020",
     * deposittype: "CREDIT",
     * email: "mdwasim508@gmail.com",
     * enteredAmount: "7",
     * fees: "0.70",
     * fromcurrency: 2,
     * fromcurrencycode: "INR",
     * holdername: "moe",
     * originalAmount: 9.1,
     * routingnumber: "26",
     * senderaccountnumber: "6367820004900292466",
     * senderareacode: 91,
     * sendermobilenumber: 9836683269,
     * sendername: "MD WASIM",
     * tocurrencycode: "USD",
     * transactionpin: "898988",
     * typeofAccount: "SAVINGS",
     * userid: 89
     * }
     */
    private void makePayment() {
        JsonObject paramSend = new JsonObject();
        paramSend.addProperty(AppoConstants.AMOUNT, tvAmountCredit.getText().toString().trim());
        paramSend.addProperty(AppoConstants.BANKACCOUNTNUMBER, edBankAccountNumber.getText().toString().trim());
        paramSend.addProperty(AppoConstants.BANKNAME, bankName);
        paramSend.addProperty(AppoConstants.CHARGES, bankfees);
        paramSend.addProperty(AppoConstants.CONVERSIONRATE, conversionRates);
        paramSend.addProperty(AppoConstants.COUNTRYNAME, tvSelectCountry.getText().toString().trim());
        paramSend.addProperty(AppoConstants.DEPOSITEDATE, Helper.getDepositDateFormat(mDob));
        paramSend.addProperty(AppoConstants.DEPOSITETYPE, "CREDIT");
        paramSend.addProperty(AppoConstants.EMIAL, Helper.getEmail());
        paramSend.addProperty(AppoConstants.ENTEREDAMOUNT, Objects.requireNonNull(edAmount.getText()).toString().trim());
        paramSend.addProperty(AppoConstants.FEES, String.valueOf(processingfees));
        paramSend.addProperty(AppoConstants.TAXES, taxes);
        paramSend.addProperty(AppoConstants.FROMCURRENCY, Integer.parseInt(mListAccount.get(0).getCurrencyid()));
        paramSend.addProperty(AppoConstants.FROMCURRENCYCODE, mListAccount.get(0).getCurrencyCode().toUpperCase());
        paramSend.addProperty(AppoConstants.HOLDERNAME, Objects.requireNonNull(edAccountHolderName.getText()).toString().trim());
        //fees,charges,enternedamount
        float originalAmount = processingfees + bankfees + Float.parseFloat(edAmount.getText().toString().trim()) + taxes;
        paramSend.addProperty(AppoConstants.ORIGINALAMOUNT, amountaftertax_fees);
        paramSend.addProperty(AppoConstants.ROUTINGNUMBER, tvBankCode.getText().toString().trim());
        paramSend.addProperty(AppoConstants.SENDERACCOUNTNUMBER, mListAccount.get(0).getAccountnumber());
        paramSend.addProperty(AppoConstants.SENDERAREACODE, Helper.getSenderAreaCode());
        paramSend.addProperty(AppoConstants.SENDERMOBILENUMBER, Helper.getSenderMobileNumber());
        paramSend.addProperty(AppoConstants.SENDERNAME, Helper.getSenderName());
        paramSend.addProperty(AppoConstants.TOCURRENCYCODE, tvSelectBankCurrency.getText().toString().trim());
        paramSend.addProperty(AppoConstants.TRANSACTIONPIN, userTransactionPin);
        paramSend.addProperty(AppoConstants.TYPESOFACCOUNT, resultAccTypes.get(mAccountTypePosition).getCode());
        paramSend.addProperty(AppoConstants.USERID, Helper.getUserId());

        //Log.e(TAG, "makePayment: " + paramSend.toString());

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

    private void getProfileDetails() {
        dialog = new ProgressDialog(WalletBankActivity.this);
        dialog.setMessage(getString(R.string.info_read_user_details));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getProfileDetails(Long.parseLong(mobileNumber), Integer.parseInt(areacode), bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //String res = new Gson().toJson(response.body());
                    //Log.e(TAG, "onResponse: getprofile :" + res);
                    JsonObject body = response.body();
                    String res=body.toString();

                    DataVaultManager.getInstance(WalletBankActivity.this).saveUserDetails(res);
                    getCurrencyCode();
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(WalletBankActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(WalletBankActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(WalletBankActivity.this, SignInActivity.class);
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
        dialog = new ProgressDialog(WalletBankActivity.this);
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
                getCountry();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


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

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("Add Fund By Bank");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Add Money");
        toolbar.setTitleTextColor(Color.WHITE);


    }

    private void getCountry() {
        dialog = new ProgressDialog(WalletBankActivity.this);
        dialog.setMessage(getString(R.string.info_get_country_code));
        dialog.show();
        mainAPIInterface.getCountryCode().enqueue(new Callback<CountryCodeResponse>() {
            @Override
            public void onResponse(Call<CountryCodeResponse> call, Response<CountryCodeResponse> response) {
                dialog.dismiss();
                //Log.e(TAG, "onResponse: " + new Gson().toJson(response));
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        // mListMain = new ArrayList<>();
                        //mListMain=response.body().getResult();
                        mListCountry = response.body().getResult();
                        //intiCountyCode();
                        getAccountTypes();
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

    private void getAccountTypes() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.info_get_account_types));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getBankAccountsTypes(bearer_).enqueue(new Callback<BankAccResponse>() {
            @Override
            public void onResponse(Call<BankAccResponse> call, Response<BankAccResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    resultAccTypes = response.body().getResult();
                }
            }

            @Override
            public void onFailure(Call<BankAccResponse> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage());
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

    @Override
    public void onCountrySelected(String countryName, String countrycode, int countryId, int pos) {
        //Log.e(TAG, "onCountrySelected: country name :: " + countryName);
        //Log.e(TAG, "onCountrySelected: country id :: " + countryId);
        //Log.e(TAG, "onCountrySelected: country Code :: " + countrycode);
        //Log.e(TAG, "onCountrySelected: position :: " + pos);
        mSelectedCountryId = countryId;
        mSelectedCountryName = countryName;
        tvSelectCountry.setText("Selected Country : " + mSelectedCountryName);

        if (AppoPayApplication.isNetworkAvailable(WalletBankActivity.this)) {
            getBankNames(mSelectedCountryId);
        } else {
            showToast(getString(R.string.no_inteenet_connection));
        }


    }

    private void showToast(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    private void getBankNames(int mSelectedCountryId) {
        dialog = new ProgressDialog(WalletBankActivity.this);
        dialog.setMessage(getString(R.string.info_get_bank_name));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getBankNameById(mSelectedCountryId, bearer_).enqueue(new Callback<BankNameResponse>() {
            @Override
            public void onResponse(Call<BankNameResponse> call, Response<BankNameResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    mListBank = response.body().getResult();
                    if (mListBank.size() > 0) {
                        //Log.e(TAG, "onResponse: true");
                    } else {
                        //Log.e(TAG, "onResponse: false");
                        mListBank = null;
                        showToast(getString(R.string.no_data_found));
                    }
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(WalletBankActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(WalletBankActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(WalletBankActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onFailure(Call<BankNameResponse> call, Throwable t) {
                mListBank = null;
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppoConstants.BANK_NAME_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                idFromBank = data.getIntExtra(AppoConstants.ID, 0);
                bankCode = data.getStringExtra(AppoConstants.BANKCODE);
                bankName = data.getStringExtra(AppoConstants.BANKNAME);
                tvSelectBank.setText(bankName);
                tvBankCode.setText(bankCode);

                getBankCurrency(idFromBank);
            }
        }
    }

    private void getBankCurrency(int idFromBank) {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.info_get_bank_currency));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getBankCurrency(idFromBank, bearer_).enqueue(new Callback<BankCurrencyResponse>() {
            @Override
            public void onResponse(Call<BankCurrencyResponse> call, Response<BankCurrencyResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase(AppoConstants.SUCCESS)) {
                        resultBanckCurrency = response.body().getResult();
                        if (resultBanckCurrency.size() > 0) {
                            tvSelectBankCurrency.setText(resultBanckCurrency.get(0).getCurrencycode());
                        }
                    }
                }

            }

            @Override
            public void onFailure(@NotNull Call<BankCurrencyResponse> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public void onAccountSelect(String accountName, int pos) {
        mAccountTypePosition = pos;
        mAccountTypeName = accountName;
    }

    @Override
    public void onConfirmSelect() {
        dialogFragment.dismiss();
        tvAccountType.setText(mAccountTypeName);

    }


}
