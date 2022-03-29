package com.stuffer.stuffers.activity.wallet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.ktx.Firebase;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.LoginRequestListener;
import com.stuffer.stuffers.communicator.ProfileUpdateRequest;
import com.stuffer.stuffers.communicator.TransactionStatus;
import com.stuffer.stuffers.fragments.bottom.chatnotification.MyNotificationManager;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomAdditional;
import com.stuffer.stuffers.fragments.dialog.ErrorDialogFragment;
import com.stuffer.stuffers.fragments.dialog.ProfileErrorDialogFragment;
import com.stuffer.stuffers.myService.NotificationReceiver;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.UnionConstant;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_FIREBASE_TOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_ID1;

public class OverviewActivity extends AppCompatActivity implements LoginRequestListener, ProfileUpdateRequest , TransactionStatus {
    private FrameLayout frameProfileCompleted, frameMobileVerified, frameEmailVerified;
    private FrameLayout frameIdUploaded, framePaymentVerified, frameBankVerified;
    private LinearLayout mainOverview;
    private ImageView ivProfileCompleted, ivMobileVerified, ivEmailVerified, ivIdUploaded, ivPaymentVerified, ivBankVerified;
    private Handler handler;
    private static final String TAG = "OverviewActivity";
    MyTextView tvUserIds, tvFcmToken, tvFcmToken1, tvPlayAvialable;
    private String mResponse;
    private Button btnShow;
    private JSONObject mJson;
    private AlertDialog dialogMerchant;
    private String mTimeStamp, mOriginalAmount, mSalesAmount, mDiscountAmount, mTrxAmt, mTrxCurrency;
    private BottomAdditional mBottomAdditional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        btnShow = (Button) findViewById(R.id.btnShow);
        tvUserIds = (MyTextView) findViewById(R.id.tvUserIds);
        tvFcmToken = (MyTextView) findViewById(R.id.tvFcmToken);
        tvFcmToken1 = (MyTextView) findViewById(R.id.tvFcmToken1);
        tvPlayAvialable = (MyTextView) findViewById(R.id.tvPlayAvialable);
        setupActionBar();
        findIds();
        registerReceiver(broadcastReceiver, new IntentFilter(AppoConstants.NOTIFY_ACTION));
        handler = new Handler();
        verifyUserLogin();

        //mResponse = "{\"msgInfo\":{\"versionNo\":\"1.0.0\",\"msgID\":\"U39990157000000000000963468\",\"timeStamp\":\"20211015115807\",\"msgType\":\"TRX_RESULT_NOTIFICATION\",\"insID\":\"39990157\"},\"trxInfo\":{\"deviceID\":\"919836683269\",\"token\":\"6263600715765554\",\"trxMsgType\":\"CPQRC_PAYMENT\",\"emvCpqrcPayload\":[\"hQVDUFYwMWFWTwigAAADMwEBAlcRYmNgBxV2VVTSMRIBAAABiAFfNAEAYzOfJghWitzXodVnep8nAYCfEBEHAAEDoAAAAQgzOTk5MDE1N582AgAFggIAAJ83BGNSX74\u003d\"],\"trxAmt\":\"9.37\",\"trxCurrency\":\"840\",\"DiscountDetails\":[{\"discountAmt\":\"0.63\",\"discountNote\":\"unionpay promotion\"}],\"originalAmount\":\"10.00\",\"merchantName\":\"UPI QRC test K 840                      \",\"retrivlRefNum\":\"101588094295\",\"settlementKey\":{\"acquirerIIN\":\"47010344\",\"forwardingIIN\":\"00020344\",\"systemTraceAuditNumber\":\"903948\",\"transmissionDateTime\":\"1015115804\"},\"paymentStatus\":\"APPROVED\",\"rejectionReason\":\"APPROVED\"}}";
        mResponse = "{\n" +
                "  \"msgInfo\": {\n" +
                "    \"versionNo\": \"1.0.0\",\n" +
                "    \"msgID\": \"U39990157000000000000975239\",\n" +
                "    \"timeStamp\": \"20211105151258\",\n" +
                "    \"msgType\": \"ADDITIONAL_PROCESSING\",\n" +
                "    \"insID\": \"39990157\"\n" +
                "  },\n" +
                "  \"trxInfo\": {\n" +
                "    \"deviceID\": \"f7946ec3a68d9afb\",\n" +
                "    \"token\": \"6292603450618017\",\n" +
                "    \"trxAmt\": \"11.00\",\n" +
                "    \"trxCurrency\": \"702\",\n" +
                "    \"emvCpqrcPayload\": [\n" +
                "      \"hQVDUFYwMWFWTwigAAADMwEBAVcRYpJgNFBhgBfSQBIBAAABiAFfNAEAYzOfJghWir8H7WFiHJ8nAYCfEBEHAAEDoAAAAQgzOTk5MDE1N582AgBEggIAAJ83BFcS5+Q\\u003d\"\n" +
                "    ],\n" +
                "    \"merchantName\": \"CHN29000CHINA UNIONPAY SIMULATOR        \"\n" +
                "  }\n" +
                "}";


        try {
            mJson = new JSONObject(mResponse);
           // Log.e(TAG, "onCreate: " + mJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showNotification();
            }
        });
        btnShow.setVisibility(View.GONE);
    }

    private void showNotification() {
        String title = mJson.toString();
        String body = mJson.toString();
        MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
        Intent intent = new Intent(this, NotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("discount", body);
        intent.putExtras(bundle);
        int j = 0;
        mNotificationManager.showSmallNotificationDiscount(title, body, intent, j);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        // mGoogleSignInClient.signOut();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean booleanExtra = intent.getBooleanExtra(AppoConstants.HAS_DISCOUNT, false);
            if (booleanExtra) {
                // Log.e(TAG, "onReceive: " + intent.getStringExtra(AppoConstants.DISCOUNT));
                String stringExtra = intent.getStringExtra(AppoConstants.DISCOUNT);
                try {
                    JSONObject mJson = new JSONObject(stringExtra);
                    JSONObject mMsgInfo = mJson.getJSONObject(UnionConstant.MSGINFO);
                    String mMSGType = mMsgInfo.getString(UnionConstant.MSGTYPE);
                    if (mMSGType.equalsIgnoreCase("TRX_RESULT_NOTIFICATION")) {
                        showCommonError(intent.getStringExtra(AppoConstants.DISCOUNT));
                    } else if (mMSGType.equalsIgnoreCase("ADDITIONAL_PROCESSING")) {
                        showAdditionalDialog(intent.getStringExtra(AppoConstants.DISCOUNT));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void showAdditionalDialog(String stringExtra) {
        mBottomAdditional = new BottomAdditional();
        Bundle mBundle = new Bundle();
        mBundle.putString(AppoConstants.DISCOUNT, stringExtra);
        mBottomAdditional.setArguments(mBundle);
        mBottomAdditional.show(getSupportFragmentManager(), mBottomAdditional.getTag());

    }

    public void showCommonError(String message) {
        JSONObject mJSON, mTRXINFO, mMSGINFO;
        JSONArray mDISCDetails;
        try {
            mJSON = new JSONObject(message);
            mTRXINFO = mJSON.getJSONObject("trxInfo");
            mDISCDetails = mTRXINFO.getJSONArray("DiscountDetails");
            mMSGINFO = mJSON.getJSONObject("msgInfo");
            mTimeStamp = mMSGINFO.getString("timeStamp");
            mOriginalAmount = mTRXINFO.getString("originalAmount");
            mSalesAmount = mTRXINFO.getString("originalAmount");
            mTrxAmt = mTRXINFO.getString("trxAmt");
            mTrxCurrency = mTRXINFO.getString("trxCurrency");
            JSONObject jsonObject = mDISCDetails.getJSONObject(0);
            mDiscountAmount = jsonObject.getString("discountAmt");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_notification_discount, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfoD);
        ImageView ivSuccess = dialogLayout.findViewById(R.id.ivSuccess);
        ivSuccess.setVisibility(View.VISIBLE);
        MyTextViewBold tvAmountPayD = dialogLayout.findViewById(R.id.tvAmountPayD);//Amount
        MyTextView tvSalesAmountD = dialogLayout.findViewById(R.id.tvSalesAmountD);//salse Amount
        MyTextView tvDiscAmountD = dialogLayout.findViewById(R.id.tvDiscAmountD);//disc Amount
        MyTextView tvFinalAmountD = dialogLayout.findViewById(R.id.tvFinalAmountD);//final Amount
        MyTextView tvCurrencyPayD = dialogLayout.findViewById(R.id.tvCurrencyPayD);//currency
        MyTextView tvTransactionTimeD = dialogLayout.findViewById(R.id.tvTransactionTimeD);//transaction
        tvAmountPayD.setText(tvAmountPayD.getText().toString().trim() + " " + mOriginalAmount);

        tvSalesAmountD.setText(tvSalesAmountD.getText().toString().trim() + " " + mSalesAmount);

        tvDiscAmountD.setText(tvDiscAmountD.getText().toString().trim() + " " + mDiscountAmount);

        if (mTrxCurrency.equalsIgnoreCase("840")) {
            tvCurrencyPayD.setText(tvCurrencyPayD.getText().toString().trim() + " " + "USD");
        } else {
            tvCurrencyPayD.setText(tvCurrencyPayD.getText().toString().trim() + " " + mTrxCurrency);
        }


        Date value = null;
        String ourDate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            value = formatter.parse(mTimeStamp);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //this format changeable
            ourDate = dateFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvTransactionTimeD.setText(tvTransactionTimeD.getText().toString() + "" + ourDate);

        double mDiscTemp = Double.parseDouble(mDiscountAmount);
        double mAmountTemp = Double.parseDouble(mOriginalAmount);
        double mFinalPay = mAmountTemp - mDiscTemp;
        tvFinalAmountD.setText(tvFinalAmountD.getText().toString().trim() + " " + mFinalPay);
        tvInfo.setText("SUCCESS");
        //tvInfo.setVisibility(View.GONE);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goPrevious();
                dialogMerchant.dismiss();
            }
        });

        builder.setView(dialogLayout);

        dialogMerchant = builder.create();

        dialogMerchant.setCanceledOnTouchOutside(false);

        dialogMerchant.show();
    }

    private void getDetails() {
        String mUserId = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_ID1);
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_FIREBASE_TOKEN);
        tvUserIds.setText("User Id : " + mUserId);
        if (StringUtils.isEmpty(vaultValue)) {
            tvFcmToken.setText("FCM TOKEN 1 EMPTY");
        } else {
            tvFcmToken.setText("FCM TOKEN1 : " + vaultValue);
        }
         //Uncomment Below
        /*String token = FirebaseInstanceId.getInstance().getToken();
        if (StringUtils.isEmpty(token)) {
            tvFcmToken1.setText("FCM TOKEN 2 EMPTY");
        } else {
            tvFcmToken1.setText("FCM TOKEN 2 :" + token);
        }*/
        // Getting status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(OverviewActivity.this);
        if (status == ConnectionResult.SUCCESS) {
            tvPlayAvialable.setText("Google Play Service AVAILABLE");
        } else {
            tvPlayAvialable.setText("Google Play Service NOT available");
        }

    }

    private void verifyUserLogin() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isUserLogin();
            }
        }, 200);
    }

    private void isUserLogin() {
        if (AppoPayApplication.isNetworkAvailable(OverviewActivity.this)) {
            String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
            if (StringUtils.isEmpty(vaultValue)) {
                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
                errorDialogFragment.setArguments(bundle);
                errorDialogFragment.setCancelable(false);
                errorDialogFragment.show(getSupportFragmentManager(), errorDialogFragment.getTag());
            } else {
                try {
                    JSONObject root = new JSONObject(vaultValue);
                    JSONObject result = root.getJSONObject(AppoConstants.RESULT);
                    JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                    if (customerDetails.getString(AppoConstants.COUNTRYID).isEmpty() || customerDetails.getString(AppoConstants.COUNTRYID).equalsIgnoreCase("0")) {
                        ivMobileVerified.setImageResource(R.drawable.ic_verified_details);
                        ivEmailVerified.setImageResource(R.drawable.ic_verified_details);
                        ProfileErrorDialogFragment fragment = new ProfileErrorDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(AppoConstants.INFO, getString(R.string.profile_update_error1));
                        fragment.setArguments(bundle);
                        fragment.setCancelable(false);
                        fragment.show(getSupportFragmentManager(), fragment.getTag());
                    } else {
                        /*ic_verified_details
                        ic_criss_cross*/
                        ivProfileCompleted.setImageResource(R.drawable.ic_verified_details);
                        ivMobileVerified.setImageResource(R.drawable.ic_verified_details);
                        ivEmailVerified.setImageResource(R.drawable.ic_verified_details);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(OverviewActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void findIds() {
        mainOverview = findViewById(R.id.mainOverview);
        frameProfileCompleted = findViewById(R.id.frameProfileCompleted);
        frameMobileVerified = findViewById(R.id.frameMobileVerified);
        frameEmailVerified = findViewById(R.id.frameEmailVerified);
        frameIdUploaded = findViewById(R.id.frameIdUploaded);
        framePaymentVerified = findViewById(R.id.framePaymentVerified);
        frameBankVerified = findViewById(R.id.frameBankVerified);
        ivProfileCompleted = findViewById(R.id.ivProfileCompleted);
        ivMobileVerified = findViewById(R.id.ivMobileVerified);
        ivEmailVerified = findViewById(R.id.ivEmailVerified);
        ivIdUploaded = findViewById(R.id.ivIdUploaded);
        ivPaymentVerified = findViewById(R.id.ivPaymentVerified);
        ivBankVerified = findViewById(R.id.ivBankVerified);


    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("Overview");

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
    public void onLoginRequest() {
        //Log.e(TAG, "onLoginRequest: onLoginRequest");
        Intent intent = new Intent(OverviewActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onProfileUpdate() {
        //Log.e(TAG, "onProfileUpdate: onProfileUpdate");
        Intent intent = new Intent(OverviewActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onTransactionComplete(String param) {
        if ("SUCCESS".equalsIgnoreCase(param)) {
            if (mBottomAdditional != null) {
                mBottomAdditional.dismiss();
            }
            //getSavedCard();
        } else {
            if (mBottomAdditional != null) {
                mBottomAdditional.dismiss();
            }
        }
    }
}
