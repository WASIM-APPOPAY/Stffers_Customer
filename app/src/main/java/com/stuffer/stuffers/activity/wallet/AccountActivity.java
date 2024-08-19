package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonParser;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.recyclerview.GiftCardAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.api.MainUAPIInterface;
import com.stuffer.stuffers.communicator.ConfirmSelectListener;
import com.stuffer.stuffers.communicator.CurrencySelectListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.fragments.dialog.CurrencyDialogFragment;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.utils.UnionConstant;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class AccountActivity extends AppCompatActivity implements CurrencySelectListener, ConfirmSelectListener, RecyclerViewRowItemCLickListener, TransactionPinListener {
    private static final String TAG = "AccountActivity";
    private RecyclerView rvGiftCards;
    boolean allow = true;
    private MyButton btnCreate;
    private MainAPIInterface mainAPIInterface;
    private ProgressDialog dialog;
    List<CurrencyResult> result;
    ArrayList<AccountModel> mListAccount;
    private int mCurrencyId = 0;
    private String mCurrencyCode = null;
    private CurrencyDialogFragment dialogFragment;
    private String areacode;
    private String mobileNumber;
    private MyTextView tvAccountNos, tvFullName;
    ImageView ivWallet;
    private String mUserName;
    private ImageView ivUninonPay;
    private MyTextView tvCardNumberU, tvExpU, tvNameU;
    private MainUAPIInterface apiServiceUNIONPay;
    private FrameLayout fLayout, fBottomCvv;
    private long mLastClickTime = 0;
    private BottotmPinFragment mBottomPinFragment;
    private String mCardInfo;
    private ProgressDialog mProgress;
    private MyTextView tvCardTypeU;
    private MyTextViewBold tvCardHeaderU;
    private int mCount = 0;
    private int mTotal;


    private int mCurrentCard;
    private MyTextView tvCardNumber1;
    private MyTextView tvName1;
    ImageView ivWallet1;
    FrameLayout ivFrameTop;
    private String mAccountNumber;
    private MyTextView tvCvvBack;
    private JSONObject mRootObject;
    private JSONObject mMsgInfo;
    private JSONObject mTrxInfo;
    private JSONObject mRoot;
    private String maskedPan;
    private String panExpiry;
    private String firstName;
    private String lastName;
    private String cardFaceID;
    private MyTextView tvCvvU;
    private MyTextViewBold tvTopTap;
    private int mType = 0;
    private AppCompatTextView tvDemoName;
    private FrameLayout demoFrame, walletFrame;
    private ImageView demoBlank;
    private AppCompatTextView tvDemonameCard;
    private MyTextViewBold tvmaskunmask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (getIntent().getExtras() != null) {

            mType = getIntent().getIntExtra(AppoConstants.WHERE, 0);
        }
        demoFrame = findViewById(R.id.demoFrame);
        walletFrame = findViewById(R.id.walletFrame);

        tvmaskunmask = findViewById(R.id.tvmaskunmask);
        tvDemonameCard = findViewById(R.id.tvDemonameCard);
        demoBlank = findViewById(R.id.demoBlank);
        tvDemoName = findViewById(R.id.tvDemoName);
        mainAPIInterface = ApiUtils.getAPIService();
        apiServiceUNIONPay = ApiUtils.getApiServiceUNIONPay();
        btnCreate = (MyButton) findViewById(R.id.btnCreate);
        tvAccountNos = findViewById(R.id.tvAccountNos);
        tvFullName = findViewById(R.id.tvFullName);
        ivWallet = findViewById(R.id.ivWallet);

        ivWallet1 = (ImageView) findViewById(R.id.ivWallet1);
        ivFrameTop = findViewById(R.id.ivFrameTop);
        tvCvvU = (MyTextView) findViewById(R.id.tvCvvU);

        //String transactionPin = Helper.getTransactionPin();
        ////Log.e(TAG, "onCreate: "+transactionPin );
        tvCardTypeU = (MyTextView) findViewById(R.id.tvCardTypeU);
        ivUninonPay = (ImageView) findViewById(R.id.ivUninonPay);
        tvCardHeaderU = (MyTextViewBold) findViewById(R.id.tvCardHeaderU);
        tvCardNumberU = (MyTextView) findViewById(R.id.tvCardNumberU);
        tvExpU = (MyTextView) findViewById(R.id.tvExpU);
        tvNameU = (MyTextView) findViewById(R.id.tvNameU);
        fLayout = (FrameLayout) findViewById(R.id.fLayout);
        fBottomCvv = (FrameLayout) findViewById(R.id.fBottomCvv);
        tvCvvBack = (MyTextView) findViewById(R.id.tvCvvBack);
        tvCardNumber1 = (MyTextView) findViewById(R.id.tvCardNumber1);
        tvName1 = (MyTextView) findViewById(R.id.tvName1);
        rvGiftCards = findViewById(R.id.rvGiftCards);
        tvTopTap = (MyTextViewBold) findViewById(R.id.tvTopTap);
        setupActionBar();
        if (AppoPayApplication.isNetworkAvailable(AccountActivity.this)) {
            String vaultValue = DataVaultManager.getInstance(AccountActivity.this).getVaultValue(KEY_USER_DETIALS);
            if (StringUtils.isEmpty(vaultValue)) {
                Log.e(TAG, "onCreate: empty");
                demoBlank.setVisibility(View.VISIBLE);
            } else {
                demoBlank.setVisibility(View.VISIBLE);
                getUserDetailsForProfile();
            }
        } else {
            showToast(getString(R.string.no_inteenet_connection));
        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment = new CurrencyDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppoConstants.INFO, (ArrayList<? extends Parcelable>) result);
                dialogFragment.setArguments(bundle);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());
            }
        });

        ivWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mListAccount != null && mListAccount.size() > 0) {
                    Intent intentTransactionList = new Intent(AccountActivity.this, TransactionListActivity.class);
                    intentTransactionList.putExtra(AppoConstants.ACCOUNTNUMBER, mListAccount.get(0).getAccountnumber());
                    intentTransactionList.putExtra(AppoConstants.ENCRYPTACCOUNTNUMBER, mListAccount.get(0).getAccountEncrypt());
                    startActivity(intentTransactionList);
                } else {
                    DataVaultManager.getInstance(AccountActivity.this).saveUserDetails("");
                    DataVaultManager.getInstance(AccountActivity.this).saveUserAccessToken("");
                    Intent intent = new Intent(AccountActivity.this, SignInActivity.class);
                    intent.putExtra(AppoConstants.WHERE, mType);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ivWallet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vaultValue = DataVaultManager.getInstance(AccountActivity.this).getVaultValue(KEY_USER_DETIALS);
                if (StringUtils.isEmpty(vaultValue)) {
                    DataVaultManager.getInstance(AccountActivity.this).saveUserDetails("");
                    DataVaultManager.getInstance(AccountActivity.this).saveUserAccessToken("");
                    Intent intent = new Intent(AccountActivity.this, SignInActivity.class);
                    intent.putExtra(AppoConstants.WHERE, mType);
                    startActivity(intent);
                    finish();
                } else {


                    if (AppoPayApplication.isNetworkAvailable(AccountActivity.this)) {
                        if (mListAccount != null && mListAccount.size() > 0) {
                            Intent intentTransactionList = new Intent(AccountActivity.this, TransactionListActivity.class);
                            intentTransactionList.putExtra(AppoConstants.ACCOUNTNUMBER, mListAccount.get(0).getAccountnumber());
                            intentTransactionList.putExtra(AppoConstants.ENCRYPTACCOUNTNUMBER, mListAccount.get(0).getAccountEncrypt());
                            startActivity(intentTransactionList);
                        }
                    } else {
                        showToast(getString(R.string.no_inteenet_connection));
                    }
                }
            }
        });


        ivUninonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                mCurrentCard = 0;
                showBottomPin();
            }
        });

        demoBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Helper.showLoading(getString(R.string.info_please_wait), AccountActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (allow) {
                            tvmaskunmask.setText("Tap on UnionPay Card to MASK");
                            allow = false;
                            tvDemonameCard.setText("5288 1234 5678 9010");
                        } else {
                            tvmaskunmask.setText("Tap on UnionPay Card to UNMASK");
                            tvDemonameCard.setText("5288 **** **** 9010");
                            allow = true;
                        }

                        Helper.hideLoading();
                    }
                }, 2000);
            }
        });

        //getCardImage(cardFaceID);

    }

    private void showBottomPin() {
        mBottomPinFragment = new BottotmPinFragment();
        mBottomPinFragment.show(getSupportFragmentManager(), mBottomPinFragment.getTag());
        mBottomPinFragment.setCancelable(false);
    }


    private void showToast(String param) {
        Toast.makeText(this, "" + param, Toast.LENGTH_SHORT).show();
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

    private void getCurrencyCode() {
        dialog = new ProgressDialog(AccountActivity.this);
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
                //////Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }


    private void readUserAccounts() {
        mListAccount = new ArrayList<>();
        ArrayList<String> mListWalletNumber = new ArrayList<String>();
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
                String mIncryptAccount = getAccountNumber(index.getString(AppoConstants.ACCOUNTNUMBER));
                //Log.e(TAG, "readUserAccounts: " + mIncryptAccount);
                model.setAccountEncrypt(mIncryptAccount);
                if (index.has(AppoConstants.ACCOUNTSTATUS)) {
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                } else {
                    model.setAccountstatus("");
                }
                model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                String balance = index.getString(AppoConstants.CURRENTBALANCE);
                DecimalFormat df2 = new DecimalFormat("#.00");
                Double doubleV = Double.parseDouble(balance);
                String format = df2.format(doubleV);
                model.setCurrentbalance(format);
                mListWalletNumber.add(getNumberInStyle(index.getString(AppoConstants.ACCOUNTNUMBER)));
                mListAccount.add(model);
            }
            //5288 1234 5678 9010
            if (mListAccount.size() > 0) {
                tvTopTap.setVisibility(View.VISIBLE);
                ivFrameTop.setVisibility(View.VISIBLE);
                String accountEncrypt = mListAccount.get(0).getAccountEncrypt();
                ////Log.e(TAG, "readUserAccounts: " + accountEncrypt);
                //tvAccountNos.setText(accountEncrypt);
                mUserName = Helper.getSenderName();
                tvFullName.setText(mUserName);
                tvCardNumber1.setText("" + mListAccount.get(0).getAccountEncrypt());
                tvName1.setText(mUserName);
                demoFrame.setVisibility(View.VISIBLE);
                tvDemoName.setText(mUserName);

            }
            //need to unCooment Below
            if (mListAccount.size() > 0) {
                GiftCardAdapter giftCardAdapter = new GiftCardAdapter(AccountActivity.this, mUserName, mListAccount, mListWalletNumber);
                rvGiftCards.setAdapter(giftCardAdapter);
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTotal = mListAccount.size();
                        getAllCard();

                    }
                }, 300);*/
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void getCardImage(String cardFaceID) {
        //100268
        mRootObject = new JSONObject();
        mMsgInfo = new JSONObject();
        mTrxInfo = new JSONObject();

        try {
            mMsgInfo.put("versionNo", "1.0.0");
            String mTimeStamp = TimeUtils.getUniqueTimeStamp();
            mMsgInfo.put("timeStamp", mTimeStamp);
            String uniqueMsgId = TimeUtils.getUniqueMsgId(mTimeStamp);
            mMsgInfo.put("msgID", uniqueMsgId);
            mMsgInfo.put("msgType", "CARDFACE_DOWNLOADING");
            mMsgInfo.put("insID", "39990157");


            //mTrxInfo.put("cardfaceID", "100268");
            //mTrxInfo.put("cardfaceID", "100259");
            //mTrxInfo.put("cardfaceID","100268");

            //mTrxInfo.put("cardfaceID","100278");
            //mTrxInfo.put("cardfaceID","100276");
            // mTrxInfo.put("cardfaceID","100274");
            //mTrxInfo.put("cardfaceID","100272");
            mTrxInfo.put("cardfaceID", cardFaceID);


            mRootObject.put("msgInfo", mMsgInfo);
            mRootObject.put("trxInfo", mTrxInfo);
            showLoading();
            ////Log.e(TAG, "getCardImage: " + mRootObject);
            JsonObject mRoot = new JsonParser().parse(mRootObject.toString()).getAsJsonObject();

            apiServiceUNIONPay.getJWSTokenImage(mRoot, "/scis/switch/cardfacedownloading", UnionConstant.CONTENT_TYPE).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    hideLoading();
                    if (response.isSuccessful()) {
                        String responseString = new Gson().toJson(response.body());
                        try {
                            JSONObject mResponse = new JSONObject(responseString);
                            if (mResponse.getInt("status") == 200) {
                                //////Log.e(TAG, "onResponse: called");
                                if (mResponse.getString("message").equalsIgnoreCase("success")) {
                                    String mResult = mResponse.getString("result");
                                    makeRequestCardDownloadRequest(mResult);
                                }
                            } else {
                                if (response.code() == 400) {
                                    Toast.makeText(AccountActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 503) {
                                    Toast.makeText(AccountActivity.this, "Service Unavailable server error", Toast.LENGTH_SHORT).show();
                                } else {
                                    showToast(mResponse.getString("status"));
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    ////Log.e(TAG, "onFailure: JWS " + t.getMessage());
                    hideLoading();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void makeRequestCardDownloadRequest(String mResult) {
        showLoading();
        JsonObject mRoot = new JsonParser().parse(mRootObject.toString()).getAsJsonObject();
        apiServiceUNIONPay.getCardImage(mRoot, "/scis/switch/cardfacedownloading", mResult, UnionConstant.CONTENT_TYPE).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ////Log.e(TAG, "onResponse: "+response );
                hideLoading();
                String s = new Gson().toJson(response.body());
                try {
                    JSONObject mRES = new JSONObject(s);
                    String result = mRES.getString("result");
                    JSONObject mRESULT = new JSONObject(result);
                    JSONObject trxInfo = mRESULT.getJSONObject("trxInfo");
                    String cardfaceData = trxInfo.getString("cardfaceData");
                    byte[] decodedString = Base64.decode(cardfaceData, Base64.DEFAULT);
                    //Glide.with(AccountActivity.this).load(decodedString).into(ivUninonPay);
                    Glide.with(AccountActivity.this).load(R.drawable.wallt_card_blank).into(ivUninonPay);
                    //tvCardHeaderU.setVisibility(View.VISIBLE);
                    //tvCardNumber1.setText("" + encrypt);
                    fLayout.setVisibility(View.VISIBLE);
                    ivFrameTop.setVisibility(View.VISIBLE);
                    tvName1.setText(mUserName);
                    tvCardNumberU.setText(maskedPan);
                    tvExpU.setText("Exp:" + panExpiry);
                    tvNameU.setText("   " + firstName + " " + lastName);

                    tvCardHeaderU.setVisibility(View.VISIBLE);

                    fLayout.setVisibility(View.VISIBLE);
                    ivFrameTop.setVisibility(View.VISIBLE);
                    tvName1.setText(mUserName);
                    mCount = mCount + 1;

                    //String accountnumber = mListAccount.get(mCount).getAccountnumber();
                    tvCardNumber1.setText("" + mListAccount.get(0).getAccountEncrypt());
                    tvName1.setText(mUserName);
                    //Glide.with(AccountActivity.this).load(cardfaceData).into(ivUninonPay);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ////Log.e(TAG, "onFailure: CardFace" + t.getMessage());
                hideLoading();
            }
        });
    }


    private void getAllCard() {
        if (mCount >= mTotal) {
            return;
        } else {
            if (mCount == 0) {
                String accountnumber = mListAccount.get(mCount).getAccountnumber();
                getSavedCard1(accountnumber, mListAccount.get(mCount).getAccountEncrypt());
            } else {
                return;
            }
        }

    }

    private void getSavedCard1(String accountnumber, String encrypt) {
        showLoading();
        mAccountNumber = accountnumber;
        apiServiceUNIONPay.getSavedCardUnion(accountnumber).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //// Log.e(TAG, "onResponse: " + response);
                hideLoading();
                if (response.isSuccessful()) {
                    String s1 = new Gson().toJson(response.body());
                    try {
                        mRoot = new JSONObject(s1);
                        if (mRoot.getInt("status") == 200 && mRoot.getString("message").equalsIgnoreCase("success")) {
                            String result = mRoot.getString("result");
                            JSONObject mParent1 = new JSONObject(result);
                            JSONObject mParent = mParent1.getJSONObject("card_details");
                            JSONObject trxInfo = mParent.getJSONObject("trxInfo");
                            cardFaceID = trxInfo.getString("cardFaceID");
                            mCardInfo = trxInfo.getString(Constants.CARDINFO);
                            maskedPan = trxInfo.getString("maskedPan");
                            panExpiry = trxInfo.getString("panExpiry");
                            firstName = Helper.getFirstName();
                            lastName = Helper.getLastName();


                            //fBottomCvv.setVisibility(View.VISIBLE);
                            //getAllCard();
                            getCardImage(cardFaceID);
                        }
                    } catch (JSONException e) {
                        //e.printStackTrace();
                        fBottomCvv.setVisibility(View.GONE);
                        ivFrameTop.setVisibility(View.VISIBLE);
                        tvCardNumber1.setText("" + encrypt);
                        tvName1.setText(mUserName);
                        mCount = mCount + 1;
                        //getAllCard();
                    }
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(AccountActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(AccountActivity.this, "Service Unavailable server error", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(AccountActivity.this, "Not Found", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoading();
                ////Log.e(TAG, "onFailure: " + t.getMessage());
                mCount = mCount + 1;
                getAllCard();
            }
        });


    }

    public String getNumberInStyle(String param) {
        String accountEncrypt = param;
        char[] charsEncrypt = accountEncrypt.toCharArray();
        int count = -1;
        int count1 = 0;
        String temp = "";
        String temp1 = "";
        String finalString = "";
        for (int i = 0; i < charsEncrypt.length; i++) {
            count = count + 1;
            if (count >= 10) {
                        /*finalString = finalString + temp;
                        temp = "";
                        count = -1;*/
                temp = temp + "X";
                temp1 = temp;
            } else {
                temp = temp + String.valueOf(charsEncrypt[i]);
            }
            //count1 = count1 + 1;
        }

        return temp1;

    }

    private String getAccountNumber(String param) {
        return Helper.getAccountNumber(param);
    }

    private String getCurrency(String param) {
        return Helper.getCurrency(param, result);
    }


    private void setupActionBar() {
        MyTextViewBold common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        common_toolbar_title.setText(getString(R.string.info_account_destails));
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

    @Override
    public void onCurrencySelected(int id, String currencyCode) {
        mCurrencyId = id;
        mCurrencyCode = currencyCode;
    }

    @Override
    public void onConfirmSelect() {
        dialogFragment.dismiss();
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject result = index.getJSONObject(AppoConstants.RESULT);
            String userId = result.getString(AppoConstants.ID);
            areacode = result.getString(AppoConstants.PHONECODE);
            mobileNumber = result.getString(AppoConstants.MOBILENUMBER);
            JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            String customerId = customerDetails.getString(AppoConstants.ID);
            JsonObject param = new JsonObject();
            param.addProperty(AppoConstants.AREACODE, areacode);
            param.addProperty(AppoConstants.MOBILENUMBER, mobileNumber);
            param.addProperty(AppoConstants.CURRENCYCODE, mCurrencyCode);
            param.addProperty(AppoConstants.CURRENCYID, mCurrencyId);
            param.addProperty(AppoConstants.CUSTOMERID, customerId);
            param.addProperty(AppoConstants.USERID, userId);
            postCreateWalletAccount(param, accesstoken);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void postCreateWalletAccount(JsonObject param, String token) {
        dialog = new ProgressDialog(AccountActivity.this);
        dialog.setMessage("Please wait, creating account.");
        dialog.show();

        mainAPIInterface.postCreateAccount(param, token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    try {
                        JSONObject resJson = new JSONObject(res);
                        if (resJson.getString(AppoConstants.MESSAGE).equalsIgnoreCase(AppoConstants.SUCCESS)) {
                            Toast.makeText(AccountActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
                            getProfileDetails();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ///*////Log.e(TAG, "onResponse: response 1 : " + new Gson().toJson(response.body()));
                    //////Log.e(TAG, "onResponse: response 2 : " + new Gson().toJson(response));*/
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(AccountActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(AccountActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(AccountActivity.this, SignInActivity.class);
                        intent.putExtra(AppoConstants.WHERE, mType);
                        startActivity(intent);
                        finish();
                    } else if (response.code() == 400) {
                        //////Log.e(TAG, "onResponse: bad request");
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //////Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });


    }

    private void getProfileDetails() {
        dialog = new ProgressDialog(AccountActivity.this);
        dialog.setMessage(getString(R.string.info_read_user_details));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.getProfileDetails(Long.parseLong(mobileNumber), Integer.parseInt(areacode), bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    demoBlank.setVisibility(View.VISIBLE);
                    //String res = new Gson().toJson(response.body());
                    ////////Log.e(TAG, "onResponse: getprofile :" + res);
                    JsonObject body = response.body();
                    String res = body.toString();
                    DataVaultManager.getInstance(AccountActivity.this).saveUserDetails(res);
                    getCurrencyCode();
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(AccountActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(AccountActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(AccountActivity.this, SignInActivity.class);
                        intent.putExtra(AppoConstants.WHERE, mType);
                        startActivity(intent);
                        finish();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                demoBlank.setVisibility(View.GONE);
                //////Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }

    @Override
    public void onRowItemClick(int pos) {
        if (AppoPayApplication.isNetworkAvailable(AccountActivity.this)) {
            Intent intentTransactionList = new Intent(AccountActivity.this, TransactionListActivity.class);
            intentTransactionList.putExtra(AppoConstants.ACCOUNTNUMBER, mListAccount.get(0).getAccountnumber());
            intentTransactionList.putExtra(AppoConstants.ENCRYPTACCOUNTNUMBER, mListAccount.get(0).getAccountEncrypt());
            startActivity(intentTransactionList);
        } else {
            showToast(getString(R.string.no_inteenet_connection));
        }
    }

    @Override
    public void onPinConfirm(String transactionPin) {
        String mTransactionPin = Helper.getTransactionPin();
        if (mTransactionPin.equalsIgnoreCase(transactionPin)) {
            if (mCurrentCard == 0) {
                makeUnmaskedRequest1(0);
            }


        } else {
            showToast("Invalid Transaction Pin");
        }
    }

    public void showLoading() {
        mProgress = new ProgressDialog(AccountActivity.this);
        mProgress.setMessage("Please wait.....");
        mProgress.show();
    }

    public void hideLoading() {
        mProgress.dismiss();
    }


    private void makeUnmaskedRequest1(int pos) {
        if (mBottomPinFragment != null) mBottomPinFragment.dismiss();

        showLoading();
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), mCardInfo);
        apiServiceUNIONPay.getUnmaskRequestBody(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    JsonObject body1 = response.body();
                    String result = body1.get(AppoConstants.RESULT).getAsString();
                    JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
                    String mUnmaskPan = jsonObject.get(AppoConstants.PAN).getAsString();
                    String mUnmaskCvv = jsonObject.get(AppoConstants.CVV2).getAsString();
                    String mUnmaskType = jsonObject.get(AppoConstants.CARDTYPEU).getAsString();
                    //tvCardTypeU.setText("Card Type : " + mUnmaskType);
                    //tvCardTypeU.setVisibility(View.VISIBLE);
                    tvCardNumberU.setText(mUnmaskPan);
                    tvCvvU.setText("CVV : " + mUnmaskCvv);
                    tvCvvU.setVisibility(View.VISIBLE);
                    //tvCvvBack.setText(mUnmaskCvv);
                    tvCardNumber1.setText("" + mListAccount.get(pos).getAccountnumber());
                    showToast("Card UnMask Successfully");

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoading();
                showToast(t.getMessage());
            }
        });

    }


}




