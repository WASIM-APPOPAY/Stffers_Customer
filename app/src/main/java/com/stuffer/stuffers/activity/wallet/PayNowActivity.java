package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.gson.JsonParser;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.FianceTab.UnionPayActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.api.MainUAPIInterface;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.communicator.UnionPayCardListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomNotCard;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.IsoDepTransceiver;
import com.stuffer.stuffers.utils.QRCodeUtil;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.utils.UnionConstant;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import com.upi.hcesdk.api.service.HceApiService;

import org.apache.commons.lang3.StringUtils;
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

public class PayNowActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback, IsoDepTransceiver.OnMessageReceived, UnionPayCardListener, TransactionPinListener {
    private static final String TAG = "PayNowActivity";
    MainAPIInterface mainAPIInterface;
    MyTextViewBold tvUserName, tvUserMobile;
    MyTextViewBold tvWalletAmount;
    private String mUserDetails;
    private JSONObject mIndex;
    private ProgressDialog dialog;
    private ImageView user_qr_image;
    private AlertDialog alertDialog;
    List<CurrencyResult> result;
    ArrayList<AccountModel> mListAccount;
    private NfcAdapter nfcAdapter;
    private MainUAPIInterface apiServiceUNIONPay;
    private MyTextView tvCvvU;
    private MyTextView tvCardTypeU;
    private ImageView ivUninonPay;
    private MyTextViewBold tvCardHeaderU;
    private MyTextView tvCardNumberU;
    private MyTextView tvExpU;
    private MyTextView tvNameU;
    private FrameLayout fLayout;
    private String mCardInfo;
    private BottomNotCard mBottomNotCard;
    private long mLastClickTime = 0;
    private BottotmPinFragment mBottomPinFragment;
    private ProgressDialog mProgress;
    private JSONObject mRoot;
    private String cardFaceID;
    private String maskedPan;
    private String panExpiry;
    private String firstName;
    private String lastName;
    private JSONObject mRootObject;
    private JSONObject mMsgInfo;
    private JSONObject mTrxInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pay_now_activity);
        setupActionBar();
        mainAPIInterface = ApiUtils.getAPIService();
        tvUserName = findViewById(R.id.tvUserName);
        tvUserMobile = findViewById(R.id.tvUserMobile);
        tvWalletAmount = findViewById(R.id.tvWalletAmount);
        apiServiceUNIONPay = ApiUtils.getApiServiceUNIONPay();
        tvCvvU = (MyTextView) findViewById(R.id.tvCvvU);
        tvCardTypeU = (MyTextView) findViewById(R.id.tvCardTypeU);
        ivUninonPay = (ImageView) findViewById(R.id.ivUninonPay);
        tvCardHeaderU = (MyTextViewBold) findViewById(R.id.tvCardHeaderU);
        tvCardNumberU = (MyTextView) findViewById(R.id.tvCardNumberU);
        tvExpU = (MyTextView) findViewById(R.id.tvExpU);
        tvNameU = (MyTextView) findViewById(R.id.tvNameU);
        fLayout = (FrameLayout) findViewById(R.id.fLayout);


        mUserDetails = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);

        if (AppoPayApplication.isNetworkAvailable(this)) {
            if (!StringUtils.isEmpty(mUserDetails)) {
                try {
                    mIndex = new JSONObject(mUserDetails);
                    onUpdateProfile();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showErrorDialog();
            }
        } else {
            Toast.makeText(this, "" + getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter != null) {
            android.nfc.NfcAdapter mNfcAdapter = android.nfc.NfcAdapter.getDefaultAdapter(PayNowActivity.this);

            if (!mNfcAdapter.isEnabled()) {

                android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(PayNowActivity.this);
                alertbox.setTitle("Info");
                alertbox.setMessage("Please Enable your NFC, select Appopay as a Default Wallet");
                alertbox.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        {
                            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                            startActivity(intent);
                        }
                    }
                });
                alertbox.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertbox.show();

            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSavedCard();
            }
        }, 300);

        ivUninonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showBottomPin();
            }
        });

    }

    private void showBottomPin() {
        mBottomPinFragment = new BottotmPinFragment();
        mBottomPinFragment.show(getSupportFragmentManager(), mBottomPinFragment.getTag());
        mBottomPinFragment.setCancelable(false);
    }

    @Override
    public void onPinConfirm(String transactionPin) {
        String mTransactionPin = Helper.getTransactionPin();
        if (mTransactionPin.equalsIgnoreCase(transactionPin)) {
            makeUnmaskedRequest();
        } else {
            if (mBottomPinFragment != null) {
                mBottomPinFragment.dismiss();
            }
            showToast("Invalid Transaction Pin");
        }
    }

    public void showLoading() {
        mProgress = new ProgressDialog(PayNowActivity.this);
        mProgress.setMessage("Please wait.....");
        mProgress.show();
    }

    public void hideLoading() {
        mProgress.dismiss();
    }

    private void makeUnmaskedRequest() {
        if (mBottomPinFragment != null)
            mBottomPinFragment.dismiss();

        showLoading();
        //Log.e(TAG, "makeUnmaskedRequest: " + mCardInfo);
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), mCardInfo);
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
                    //.setText("Card Type : " + mUnmaskType);
                    //tvCardTypeU.setVisibility(View.VISIBLE);
                    tvCardNumberU.setText(mUnmaskPan);
                    tvCvvU.setText("CVV : " + mUnmaskCvv);
                    tvCvvU.setVisibility(View.VISIBLE);
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

    private void showToast(String param) {
        Toast.makeText(this, "" + param, Toast.LENGTH_SHORT).show();
    }

    private void getSavedCard() {
        showLoading();
        String walletAccountNumber = Helper.getWalletAccountNumber();
        //mAccountNumber = accountnumber;
        apiServiceUNIONPay.getSavedCardUnion(walletAccountNumber).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // Log.e(TAG, "onResponse: " + response);
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
                        e.printStackTrace();

                    }
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(PayNowActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(PayNowActivity.this, "Service Unavailable server error", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(PayNowActivity.this, "Not Found", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                hideLoading();

            }
        });

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
            //Log.e(TAG, "getCardImage: " + mRootObject);
            showLoading();
            JsonObject mRoot = new JsonParser().parse(mRootObject.toString()).getAsJsonObject();

            apiServiceUNIONPay.getJWSTokenImage(mRoot, "/scis/switch/cardfacedownloading", UnionConstant.CONTENT_TYPE)
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            hideLoading();
                            if (response.isSuccessful()) {
                                String responseString = new Gson().toJson(response.body());
                                try {
                                    JSONObject mResponse = new JSONObject(responseString);
                                    if (mResponse.getInt("status") == 200) {
                                        ////Log.e(TAG, "onResponse: called");
                                        if (mResponse.getString("message").equalsIgnoreCase("success")) {
                                            String mResult = mResponse.getString("result");
                                            makeRequestCardDownloadRequest(mResult);
                                        }
                                    } else {
                                        if (response.code() == 400) {
                                            Toast.makeText(PayNowActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                        } else if (response.code() == 503) {
                                            Toast.makeText(PayNowActivity.this, "Service Unavailable server error", Toast.LENGTH_SHORT).show();
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
                            //Log.e(TAG, "onFailure: JWS " + t.getMessage());
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
                //Log.e(TAG, "onResponse: "+response );
                hideLoading();
                String s = new Gson().toJson(response.body());
                try {
                    JSONObject mRES = new JSONObject(s);
                    String result = mRES.getString("result");
                    JSONObject mRESULT = new JSONObject(result);
                    JSONObject trxInfo = mRESULT.getJSONObject("trxInfo");
                    String cardfaceData = trxInfo.getString("cardfaceData");
                    byte[] decodedString = Base64.decode(cardfaceData, Base64.DEFAULT);
                    //Glide.with(PayNowActivity.this).load(decodedString).into(ivUninonPay);
                    Glide.with(PayNowActivity.this).load(R.drawable.wallt_card_blank).into(ivUninonPay);
                    fLayout.setVisibility(View.VISIBLE);
                    tvCardNumberU.setText(maskedPan);
                    tvExpU.setText("Exp:" + panExpiry);
                    tvNameU.setText("   " + firstName + " " + lastName);

                    tvCardHeaderU.setVisibility(View.VISIBLE);

                    fLayout.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Log.e(TAG, "onFailure: CardFace" + t.getMessage());
                hideLoading();
            }
        });
    }

    private void showNoCardDialog() {
        mBottomNotCard = new BottomNotCard();
        mBottomNotCard.show(getSupportFragmentManager(), mBottomNotCard.getTag());
        mBottomNotCard.setCancelable(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                    null);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }

    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_error_dialog, null, false);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyButton btnYes = dialogLayout.findViewById(R.id.btnYes);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        tvInfo.setText(getString(R.string.profile_update_error));
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRedirect();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void closeDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private void loginRedirect() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }

        Intent intent = new Intent(PayNowActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void onUpdateProfile() {
        dialog = new ProgressDialog(PayNowActivity.this);
        dialog.setMessage(getString(R.string.info_getting_profile));
        dialog.show();
        try {
            String accessToken = DataVaultManager.getInstance(PayNowActivity.this).getVaultValue(KEY_ACCESSTOKEN);
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
                        DataVaultManager.getInstance(PayNowActivity.this).saveUserDetails(res);
                        getCurrencyCode();
                    } else {
                        if (response.code() == 401) {
                            DataVaultManager.getInstance(PayNowActivity.this).saveUserDetails("");
                            DataVaultManager.getInstance(PayNowActivity.this).saveUserAccessToken("");
                            Intent intent = new Intent(PayNowActivity.this, SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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

    private void getCurrencyCode() {
        dialog = new ProgressDialog(PayNowActivity.this);
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

            }
        });

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
                String mIncryptAccount = Helper.getAccountNumber(index.getString(AppoConstants.ACCOUNTNUMBER));
                model.setAccountEncrypt(mIncryptAccount);
                if (index.has(AppoConstants.ACCOUNTSTATUS)) {
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                } else {
                    model.setAccountstatus("");
                }
                model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                model.setCurrentbalance(index.getString(AppoConstants.CURRENTBALANCE));
                mListAccount.add(model);
            }
            if (mListAccount.size() > 0) {
                String accountEncrypt = mListAccount.get(0).getAccountnumber();
                char[] charsEncrypt = accountEncrypt.toCharArray();
                int count = -1;
                int count1 = 0;
                String temp = "";
                String finalString = "";
                for (int i = 0; i < charsEncrypt.length; i++) {
                    count = count + 1;
                    temp = temp + String.valueOf(charsEncrypt[i]);
                    if (count == 4) {
                        finalString = finalString + temp + "    ";
                        temp = "";
                        count = -1;
                    }
                    count1 = count1 + 1;
                    if (count1 >= charsEncrypt.length) {
                        finalString = finalString + temp;
                    }
                }
            }
            if (mListAccount.size() > 0) {
                invalidateUserInfo();
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

    private void invalidateUserInfo() {
        mUserDetails = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(mUserDetails)) {
            String senderName = Helper.getSenderName();
            Long senderMobileNumber = Helper.getSenderMobileNumber();
            String currantBalance = Helper.getCurrantBalance();
            tvUserName.setText(senderName);
            tvUserMobile.setText("" + senderMobileNumber);
            try {
                DecimalFormat df2 = new DecimalFormat("#.00");
                Double doubleV = Double.parseDouble(currantBalance);
                String format = df2.format(doubleV);
                tvWalletAmount.setText("Avail Bal : " + "$" + format);
            } catch (Exception e) {
                tvWalletAmount.setText("Avail Bal : " + "$" + currantBalance);
            }
        }

    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(getString(R.string.info_pay_now));

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        IsoDep isoDep = IsoDep.get(tag);
        IsoDepTransceiver transceiver = new IsoDepTransceiver(isoDep, this);
        Thread thread = new Thread(transceiver);
        thread.start();
    }

    @Override
    public void onMessage(byte[] message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                //isoDepAdapter.addMessage(new String(message));
                Toast.makeText(PayNowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onCardRequest() {
        redirectCardEnrollment();
    }

    public void redirectCardEnrollment() {
        if (mBottomNotCard != null)
            mBottomNotCard.dismiss();
        Intent intentUnion = new Intent(PayNowActivity.this, UnionPayActivity.class);
        startActivity(intentUnion);
    }
}
