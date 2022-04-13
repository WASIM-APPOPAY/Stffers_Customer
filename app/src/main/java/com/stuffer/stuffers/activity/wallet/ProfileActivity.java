package com.stuffer.stuffers.activity.wallet;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_MOBILE;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_NAME;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.FianceTab.UnionPayActivity;
import com.stuffer.stuffers.activity.profile.DeliveryAddressActivity;
import com.stuffer.stuffers.activity.quick_pass.QRCodeActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.api.MainUAPIInterface;
import com.stuffer.stuffers.communicator.TransactionStatus;
import com.stuffer.stuffers.communicator.UnionPayCardListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomAdditional;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomNotCard;
import com.stuffer.stuffers.fragments.profile_tab.BarCodeFragment;
import com.stuffer.stuffers.fragments.profile_tab.EmvQrFragment;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.myService.NotificationReceiver;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.utils.UnionConstant;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbb20.CountryCodePicker;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

;

public class ProfileActivity extends AppCompatActivity implements TransactionStatus, UnionPayCardListener {
    private static final String TAG = "ProfileActivity";
    ImageView user_qr_image;
    String strUserName, strUserMobile;
    private MyTextView txtUpdateProfile;
    MainAPIInterface mainAPIInterface;
    private JSONObject mIndex;
    private ProgressDialog dialog;
    private String mUserName, mEmail;
    private String mMobileNo, mCityName, mTransPin, mScreenPin, mAddress, vaultValue, mDob;
    private int mCountryId, mStateId;
    private AlertDialog alertDialog;
    MyTextView tvUserName, tvUserMobile, tvProfileDetails, tvWalletAmount;
    CountryCodePicker countryCodePicker;
    List<CurrencyResult> result;
    ArrayList<AccountModel> mListAccount;
    MyTextView tvSacnQrCode;
    private MyTextView tvDeliveryAddress;
    private String mZipCode;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String mBarCode = null, mQrCode = null;
    private JSONObject mRootObject, mMsgInfo, mTrxInfo;
    private ProgressDialog mProgress;
    private ImageView user_qr_image1;
    private MainUAPIInterface apiServiceUNIONPay;
    private OkHttpClient okHttpClient;
    private String mResponse;
    private String mTimeStamp, mOriginalAmount, mSalesAmount, mTrxAmt, mTrxCurrency, mDiscountAmount;
    private Dialog dialogMerchant;
    private BottomAdditional mBottomAdditional;
    private BottomNotCard mBottomNotCard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.profile_activity);
        apiServiceUNIONPay = ApiUtils.getApiServiceUNIONPay();

        /*OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
                .addNetworkInterceptor(new GzipRequestInterceptor())
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);*/

        if (getIntent().getExtras() != null) {
            Intent intent = new Intent(ProfileActivity.this, NotificationReceiver.class);
            intent.putExtra("discount", getIntent().getStringExtra("discount"));
            sendBroadcast(intent);
        }

        registerReceiver(broadcastReceiver, new IntentFilter(AppoConstants.NOTIFY_ACTION));

        okHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .build();
        AndroidNetworking.initialize(getApplicationContext());
        //AndroidNetworking.initialize(getApplicationContext());
        setupActionBar();
        mainAPIInterface = ApiUtils.getAPIService();
        vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        strUserName = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_NAME);
        strUserMobile = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_MOBILE);
        initViews();
        countryCodePicker.setExcludedCountries(getString(R.string.info_exclude_countries));
        if (AppoPayApplication.isNetworkAvailable(this)) {
            if (!StringUtils.isEmpty(vaultValue)) {
                try {
                    mIndex = new JSONObject(vaultValue);
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
        txtUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(vaultValue)) {
                    showErrorDialog();
                } else {
                    if (AppoPayApplication.isNetworkAvailable(ProfileActivity.this)) {
                        Intent intentUpdate = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                        startActivityForResult(intentUpdate, AppoConstants.PROFILE_REQUEST);
                    } else {
                        Toast.makeText(ProfileActivity.this, "" + getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        tvProfileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(vaultValue)) {
                    showErrorDialog();
                } else {
                    Intent intent = new Intent(ProfileActivity.this, ProfileDetailsActivity.class);
                    startActivity(intent);

                    /*Intent intentUpdate = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                    startActivityForResult(intentUpdate, AppoConstants.PROFILE_REQUEST);*/
                }
            }
        });

        tvSacnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted()) {
                    initQuickPass();
                } else {
                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA}, AppoConstants.CAMERA_REQUEST_CODE);
                }

            }
        });

        //txtUpdateProfile.setVisibility(View.VISIBLE);

        tvDeliveryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddress = new Intent(ProfileActivity.this, DeliveryAddressActivity.class);
                startActivity(intentAddress);
            }
        });

        /*String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_TOKEN);
        if (!StringUtils.isEmpty(vaultValue)) {
            getConsumerQrCode(vaultValue);
        } else {
            getSavedCard();
        }*/


        CountryCodePicker.DialogEventsListener mLis = new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {

            }

            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {

            }
        };
        countryCodePicker.setDialogEventsListener(mLis);


        //getSavedCard();
        //showNoCardDialog();


    }


    private void getSavedCard() {
        String walletAccountNumber = Helper.getWalletAccountNumber();
        apiServiceUNIONPay.getSavedCardUnion(walletAccountNumber).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //Log.e(TAG, "onResponse: "+response );
                if (response.isSuccessful()) {
                    String s1 = new Gson().toJson(response.body());
                    try {
                        JSONObject mRoot = new JSONObject(s1);
                        if (mRoot.getInt("status") == 200 && mRoot.getString("message").equalsIgnoreCase("success")) {
                            if (mRoot.has("result")) {
                                String result = mRoot.getString("result");

                                if (result != null && !result.isEmpty()) {
                                    /*JSONObject mJSON = new JSONObject(result);
                                    JSONObject trxInfo = mJSON.getJSONObject("trxInfo");*/
                                    String result1 = mRoot.getString("result");
                                    JSONObject mParent1 = new JSONObject(result1);
                                    JSONObject mParent = mParent1.getJSONObject("card_details");
                                    JSONObject trxInfo = mParent.getJSONObject("trxInfo");
                                    String token = trxInfo.getString("token");
                                    //DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveCardToken(token);
                                    getConsumerQrCode(token);
                                }
                            } else {
                                showNoCardDialog();
                                //Toast.makeText(ProfileActivity.this, getString(R.string.info_request_union_pay_card), Toast.LENGTH_SHORT).show();

                                return;

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(ProfileActivity.this, getString(R.string.info_bad_request), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(ProfileActivity.this, getString(R.string.info_503), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(ProfileActivity.this, getString(R.string.info_not_found), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    private void getConsumerQrCode(String tokenParam) {

        mRootObject = new JSONObject();
        mMsgInfo = new JSONObject();
        mTrxInfo = new JSONObject();

        try {
            mMsgInfo.put("versionNo", "1.0.0");
            String mTimeStamp = TimeUtils.getUniqueTimeStamp();
            mMsgInfo.put("timeStamp", mTimeStamp);
            String uniqueMsgId = TimeUtils.getUniqueMsgId(mTimeStamp);
            mMsgInfo.put("msgID", uniqueMsgId);
            mMsgInfo.put("msgType", "QRC_GENERATION");
            mMsgInfo.put("insID", "39990157");
            Long senderMobileNumber = Helper.getSenderMobileNumber();
            String phoneCode = Helper.getPhoneCode();
            String phWithCode = phoneCode + senderMobileNumber;
            mTrxInfo.put("deviceID", TimeUtils.getDeviceId());
            mTrxInfo.put("userID", phWithCode);

            if (tokenParam != null) {
                mTrxInfo.put("token", tokenParam);
            } else {
                mTrxInfo.put("token", "");
            }

            mTrxInfo.put("trxLimit", "100");
            mTrxInfo.put("cvmLimit", "1");
            mTrxInfo.put("limitCurrency", "840");
            mTrxInfo.put("cpqrcNo", "01");

            mRootObject.put("msgInfo", mMsgInfo);
            mRootObject.put("trxInfo", mTrxInfo);
            JsonObject mRoot = new JsonParser().parse(mRootObject.toString()).getAsJsonObject();
            showLoading();
            apiServiceUNIONPay.getJWSToken(mRoot, UnionConstant.QRCODE_REQUEST_PATH, UnionConstant.CONTENT_TYPE)
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response1) {
                            hide();
                            if (response1.isSuccessful()) {
                                String s = new Gson().toJson(response1.body());
                                try {
                                    JSONObject response = new JSONObject(s);
                                    if (response.getInt("status") == 200) {
                                        if (response.getString("message").equalsIgnoreCase("success")) {
                                            String mResult = response.getString("result");
                                            getUnionQrCode(mResult);
                                        }
                                    } else {
                                        showToast(response.getString("status"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            hide();
                            showToast("Error Code : " + t.getMessage());
                        }
                    });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void showLoading() {
        mProgress = new ProgressDialog(ProfileActivity.this);
        mProgress.setMessage("Please wait.....");
        mProgress.show();
    }

    public void hide() {
        //need to add condition
        if (mProgress != null) {
            mProgress.dismiss();
        }
    }

    private void showToast(String status) {
        Toast.makeText(this, "" + status, Toast.LENGTH_SHORT).show();
    }

    private void getUnionQrCode(String authToken) {
        JSONObject mRootUnion = new JSONObject();
        JSONObject mQrConfig = new JSONObject();
        JSONObject mBarCodeConfig = new JSONObject();
        JSONObject mUninoRequestTemp = new JSONObject();

        try {
            mQrConfig.put("width", 300);
            mQrConfig.put("height", 300);
            mQrConfig.put("ratio", 3);
            mQrConfig.put("logo", false);
            mBarCodeConfig.put("width", 400);
            mBarCodeConfig.put("height", 100);
            mUninoRequestTemp.put("msgInfo", mMsgInfo);
            mUninoRequestTemp.put("trxInfo", mTrxInfo);
            mRootUnion.put("qrConfig", mQrConfig);
            mRootUnion.put("barcodeConfig", mBarCodeConfig);
            mRootUnion.put("unionPayRequest", mUninoRequestTemp);
            JsonObject mRoot = new JsonParser().parse(mRootUnion.toString()).getAsJsonObject();
            showLoading();
           
            apiServiceUNIONPay.getQRandBardCode(mRoot, UnionConstant.QRCODE_REQUEST_PATH, authToken, UnionConstant.CONTENT_TYPE)
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response1) {
                            hide();
                            if (response1.isSuccessful()) {
                                String s = new Gson().toJson(response1.body());
                                try {
                                    JSONObject response = new JSONObject(s);
                                    if (response.has("qrcode") && response.has("barcode")) {
                                        JSONObject qrcode = response.getJSONObject("qrcode");
                                        String resultQRCODE = qrcode.getString("result");
                                        mQrCode = resultQRCODE.substring(resultQRCODE.indexOf(",") + 1);

                                        JSONObject barcode = response.getJSONObject("barcode");
                                        String resultBARCODE = barcode.getString("result");
                                        mBarCode = resultBARCODE.substring(resultBARCODE.indexOf(",") + 1);

                                        if (mBarCode != null && mQrCode != null) {
                                            setupViewPager(viewPager);
                                            tabLayout.setupWithViewPager(viewPager);
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            hide();
                            showToast("Error Code : " + t.getMessage());
                        }
                    });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppoConstants.CAMERA_REQUEST_CODE) {
            if (isPermissionGranted()) {
                initQuickPass();
            } else {
                Toast.makeText(this, "permission denied by user", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean isPermissionGranted() {
        boolean cameraPermission = ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        return cameraPermission;
    }

    public void initQuickPass() {

        /*Intent intent = new Intent(ProfileActivity.this, QuickPassActivity.class);
        startActivityForResult(intent, AppoConstants.QUICK_PASS_REQUEST);*/
        Intent intent = new Intent(ProfileActivity.this, QRCodeActivity.class);
        startActivityForResult(intent, AppoConstants.QUICK_PASS_REQUEST);

    }

    private void uodateUserUi() {
        JSONObject root = null;
        try {
            if (StringUtils.isEmpty(vaultValue)) {
                tvProfileDetails.setVisibility(View.GONE);
                txtUpdateProfile.setVisibility(View.VISIBLE);
            } else {
                root = new JSONObject(vaultValue);
                JSONObject result = root.getJSONObject(AppoConstants.RESULT);
                JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                if (!result.getString(AppoConstants.TRANSACTIONPIN).isEmpty() || !result.getString(AppoConstants.TRANSACTIONPIN).equalsIgnoreCase("null")) {
                    tvProfileDetails.setVisibility(View.VISIBLE);
                    txtUpdateProfile.setVisibility(View.GONE);
                } else {
                    tvProfileDetails.setVisibility(View.VISIBLE);
                    txtUpdateProfile.setVisibility(View.GONE);
                }
                /*if (customerDetails.getString(AppoConstants.COUNTRYID).isEmpty() || customerDetails.getString(AppoConstants.COUNTRYID).equalsIgnoreCase("0")) {//|| zipCode.equalsIgnoreCase("null")) {
                    txtUpdateProfile.setVisibility(View.VISIBLE);
                    tvProfileDetails.setVisibility(View.GONE);
                } else {
                    tvProfileDetails.setVisibility(View.VISIBLE);
                    txtUpdateProfile.setVisibility(View.GONE);
                }*/

            }


        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


    private void initViews() {
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //Log.e(TAG, "initViews: " + android_id);
        txtUpdateProfile = (MyTextView) findViewById(R.id.txtUpdateProfile);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserMobile = findViewById(R.id.tvUserMobile);
        user_qr_image = findViewById(R.id.user_qr_image);
        user_qr_image1 = findViewById(R.id.user_qr_image1);
        tvProfileDetails = findViewById(R.id.tvProfileDetails);
        tvWalletAmount = findViewById(R.id.tvWalletAmount);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        tvSacnQrCode = findViewById(R.id.tvSacnQrCode);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        countryCodePicker.setEnabled(false);
        viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.profile_tabs);

        setupTabIcons();


    }

    private void setupTabIcons() {
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Global QR");
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("China QR");
        tabLayout.addTab(secondTab); // add  the tab at in the TabLayout

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        EmvQrFragment emvQrFragment = EmvQrFragment.newInstance(mQrCode);
        BarCodeFragment barCodeFragment = BarCodeFragment.newInstance(mBarCode);
        adapter.addFragment(emvQrFragment, getString(R.string.info_global_qrcode));
        adapter.addFragment(barCodeFragment, getString(R.string.info_china_qr_code));
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onTransactionComplete(String param) {
        if ("SUCCESS".equalsIgnoreCase(param)) {
            if (mBottomAdditional != null) {
                mBottomAdditional.dismiss();
            }
            getSavedCard();
        } else {
            if (mBottomAdditional != null) {
                mBottomAdditional.dismiss();
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }










        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppoConstants.PROFILE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                mUserName = data.getStringExtra(AppoConstants.USERNAME);
                mEmail = data.getStringExtra(AppoConstants.EMIAL);
                mMobileNo = data.getStringExtra(AppoConstants.MOBILENUMBER);
                mDob = data.getStringExtra(AppoConstants.DOB);
                mCountryId = data.getIntExtra(AppoConstants.COUNTRYID, 0);
                mStateId = data.getIntExtra(AppoConstants.STATEID, 0);
                mCityName = data.getStringExtra(AppoConstants.CITYNAME);
                mTransPin = data.getStringExtra(AppoConstants.TRANSACTIONPIN);
                mScreenPin = data.getStringExtra(AppoConstants.SCREENLOCKPIN);
                mAddress = data.getStringExtra(AppoConstants.ADDRESS);
                mZipCode = data.getStringExtra(AppoConstants.ZIPCODE2);
                updateUserProfile();
            } else {
                //////Log.e(TAG, "onActivityResult: result canceled ");
            }

        } else if (requestCode == AppoConstants.QUICK_PASS_REQUEST) {
            if (resultCode == RESULT_OK) {
                ////  Log.e(TAG, "onActivityResult: success called");
                AppoPayApplication.UPDATE_WALLET = true;
                onUpdateProfile();
            } else {
                ////  Log.e(TAG, "onActivityResult: cancel called");
                AppoPayApplication.UPDATE_WALLET = false;
            }
        }
    }

    //for activity result
    private void updateUserProfile() {
        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject jsonResult = index.getJSONObject(AppoConstants.RESULT);

            JsonObject sentIndex = new JsonObject();
            sentIndex.addProperty(AppoConstants.ID, jsonResult.getString(AppoConstants.ID));
            sentIndex.addProperty(AppoConstants.FIRSTNAME, jsonResult.getString(AppoConstants.FIRSTNAME));
            sentIndex.addProperty(AppoConstants.LASTNAME, jsonResult.getString(AppoConstants.LASTNAME));
            sentIndex.addProperty(AppoConstants.USERNAME, jsonResult.getString(AppoConstants.USERNAME));
            sentIndex.addProperty(AppoConstants.PASSWORD, jsonResult.getString(AppoConstants.PASSWORD));
            sentIndex.addProperty(AppoConstants.EMIAL, jsonResult.getString(AppoConstants.EMIAL));
            sentIndex.addProperty(AppoConstants.ACCOUNTEXPIRED, jsonResult.getString(AppoConstants.ACCOUNTEXPIRED));
            sentIndex.addProperty(AppoConstants.ACCOUNTLOCKED, jsonResult.getString(AppoConstants.ACCOUNTLOCKED));
            sentIndex.addProperty(AppoConstants.CREDENTIALSEXPIRED, jsonResult.getString(AppoConstants.CREDENTIALSEXPIRED));
            sentIndex.addProperty(AppoConstants.ENABLE, jsonResult.getString(AppoConstants.ENABLE));
            sentIndex.addProperty(AppoConstants.MOBILENUMBER, jsonResult.getString(AppoConstants.MOBILENUMBER));
            sentIndex.addProperty(AppoConstants.TRANSACTIONPIN, mTransPin);
            sentIndex.addProperty(AppoConstants.PHONECODE, jsonResult.getString(AppoConstants.PHONECODE));

            sentIndex.addProperty(AppoConstants.USERTYPE, (String) null);
            sentIndex.addProperty(AppoConstants.STORENAME, (String) null);
            sentIndex.addProperty(AppoConstants.LATITUDE, 0);
            sentIndex.addProperty(AppoConstants.LONGITUDE, 0);
            sentIndex.addProperty(AppoConstants.SECURITYANSWER, "dollar_sent");
            sentIndex.addProperty(AppoConstants.SCREENLOCKPIN, mScreenPin);

            JsonArray jsonArrayRole = new JsonArray();
            jsonArrayRole.add("USER");
            sentIndex.add(AppoConstants.ROLE, jsonArrayRole);

            JSONObject jsonCustomerDetails = jsonResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);

            JsonObject sentJsonCustomerDetails = new JsonObject();
            sentJsonCustomerDetails.addProperty(AppoConstants.ID, jsonCustomerDetails.getString(AppoConstants.ID));
            sentJsonCustomerDetails.addProperty(AppoConstants.FIRSTNAME, jsonCustomerDetails.getString(AppoConstants.FIRSTNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.LASTNAME, jsonCustomerDetails.getString(AppoConstants.LASTNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.MIDDLENAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.CARDTOKEN, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.COUNTRYID, mCountryId);
            sentJsonCustomerDetails.addProperty(AppoConstants.STATEID, mStateId);
            sentJsonCustomerDetails.addProperty(AppoConstants.ADDRESS, mAddress);
            sentJsonCustomerDetails.addProperty(AppoConstants.CITYNAME, mCityName);
            sentJsonCustomerDetails.addProperty(AppoConstants.ZIPCODE2, mZipCode);

            sentJsonCustomerDetails.addProperty(AppoConstants.DOB, mDob);
            sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYID, Helper.getCurrencyId());
            sentJsonCustomerDetails.addProperty(AppoConstants.MONTHLYINCOME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.PASSPORTNUMBER, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.EXPIRYDATE, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.BANKACCOUNT, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IMAGEURL, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.BANKUSERNAME, (String) null);

            sentJsonCustomerDetails.addProperty(AppoConstants.BANKUSERNAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.MERCHANTQRCODE, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISDEAL, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, mListAccount.get(0).getCurrencyCode());
            sentJsonCustomerDetails.addProperty(AppoConstants.IDCUENTA, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IDASOCIADO, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISPLASTICO, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.SOURCEOFINCOME, (String) null);
            JsonArray sentJsonArrayCustomerAccounts = new JsonArray();
            JSONArray jsonArrayCustomerAccount = jsonCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);


            for (int i = 0; i < jsonArrayCustomerAccount.length(); i++) {
                JSONObject jsonObjectIndex = jsonArrayCustomerAccount.getJSONObject(i);
                JsonObject jsonObjectAccount = new JsonParser().parse(jsonObjectIndex.toString()).getAsJsonObject();
                sentJsonArrayCustomerAccounts.add(jsonObjectAccount);
            }
            sentJsonCustomerDetails.add(AppoConstants.CUSTOMERACCOUNTS, sentJsonArrayCustomerAccounts);
            sentIndex.add(AppoConstants.CUSTOMERDETAILS, sentJsonCustomerDetails);
            //Log.e(TAG, "updateUserProfile: " + sentIndex);
            /*String s = new Gson().toJson(sentIndex.toString());

            JSONObject jsonObject = new JSONObject(s);
            Log.e(TAG, "updateUserProfile: " + jsonObject);*/
            processUpdateRequest(sentIndex);


        } catch (JSONException e) {
            e.printStackTrace();

        }

    }


    private void showNoCardDialog() {
        mBottomNotCard = new BottomNotCard();
        mBottomNotCard.show(getSupportFragmentManager(), mBottomNotCard.getTag());
        mBottomNotCard.setCancelable(false);
    }

    @Override
    public void onCardRequest() {
        if (mBottomNotCard != null)
            mBottomNotCard.dismiss();
        Intent intentUnion = new Intent(ProfileActivity.this, UnionPayActivity.class);
        startActivity(intentUnion);
    }


    private void processUpdateRequest(JsonObject sentIndex) {
        String accessToken = DataVaultManager.getInstance(ProfileActivity.this).getVaultValue(KEY_ACCESSTOKEN);
        dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setMessage(getString(R.string.info_updaing_profile));
        dialog.show();
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);


        mainAPIInterface.postUpdateUserProfile(sentIndex, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response);
                    Toast.makeText(ProfileActivity.this, getString(R.string.info_profile_successfully_updated), Toast.LENGTH_SHORT).show();
                    tvProfileDetails.setVisibility(View.VISIBLE);
                    txtUpdateProfile.setVisibility(View.GONE);
                    onUpdateProfile();
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(ProfileActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(ProfileActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 400) {

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
            }
        });

    }

    private void invalidateUserInfo() {
        vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(vaultValue)) {
            try {
                mIndex = new JSONObject(vaultValue);
                //////Log.e("TAG", "onCreate: " + mIndex.toString());
                JSONObject result = mIndex.getJSONObject("result");
                tvUserName.setText(result.getString(AppoConstants.FIRSTNAME) + " " + result.getString(AppoConstants.LASTNAME));
                tvUserMobile.setText(result.getString(AppoConstants.MOBILENUMBER));
                JSONObject customerdetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                JSONArray customeraccount = customerdetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
                if (customeraccount.length() > 0) {
                    JSONObject obj = customeraccount.getJSONObject(0);
                    String balance = obj.getString(AppoConstants.CURRENTBALANCE);
                    try {
                        DecimalFormat df2 = new DecimalFormat("#.00");
                        Double doubleV = Double.parseDouble(balance);
                        String format = df2.format(doubleV);
                        tvWalletAmount.setText("$" + format);
                    } catch (Exception e) {
                        tvWalletAmount.setText("$" + balance);

                    }
                } else {
                    tvWalletAmount.setText("$0");
                }
                uodateUserUi();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void onUpdateProfile() {
        dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setMessage("please wait, getting profile");
        dialog.show();
        try {
            String accessToken = DataVaultManager.getInstance(ProfileActivity.this).getVaultValue(KEY_ACCESSTOKEN);
            JSONObject mResult = mIndex.getJSONObject(AppoConstants.RESULT);
            String ph = mResult.getString(AppoConstants.MOBILENUMBER);
            String area = mResult.getString(AppoConstants.PHONECODE);
            String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
            mainAPIInterface.getProfileDetails(Long.parseLong(ph), Integer.parseInt(area), bearer_).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        //String res = new Gson().toJson(response.body());
                        //////Log.e(TAG, "onResponse: getprofile :" + res);
                        JsonObject body = response.body();
                        String res = body.toString();
                        DataVaultManager.getInstance(ProfileActivity.this).saveUserDetails(res);
                        getCurrencyCode();
                    } else {
                        if (response.code() == 401) {
                            DataVaultManager.getInstance(ProfileActivity.this).saveUserDetails("");
                            DataVaultManager.getInstance(ProfileActivity.this).saveUserAccessToken("");
                            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dialog.dismiss();
                    //////Log.e(TAG, "onFailure: " + t.getMessage().toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCurrencyCode() {
        dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setMessage(getString(R.string.info_get_curreny_code));
        dialog.show();

        mainAPIInterface.getCurrencyResponse().enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    result = response.body().getResult();
                    readUserAccounts();
                } else {
                    Toast.makeText(ProfileActivity.this, "Currency  code request failed,please try again", Toast.LENGTH_SHORT).show();
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
                model.setAccountEncrypt(mIncryptAccount);
                //////Log.e(TAG, "readUserAccounts: encrypt ::  " + mIncryptAccount);
                if (index.has(AppoConstants.ACCOUNTSTATUS)) {
                    //////Log.e(TAG, "readUserAccounts: AccountStatus : " + index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                } else {
                    //////Log.e(TAG, "readUserAccounts: AccountStatus : " + "null");
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
                invalidateUserInfo(); //chage here

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


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
        ////////Log.e(TAG, "getAccountNumber: atLast :: " + strTemp);
        StringBuilder builder = new StringBuilder();
        builder.append(strTemp);
        builder = builder.reverse();
        ////////Log.e(TAG, "getAccountNumber: after reverse ::  " + builder.toString());
        return String.valueOf(builder);
    }


    private String getCurrency(String param) {
        return Helper.getCurrency(param, result);
    }


    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.info_profile));
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
        //for login here
        Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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


}
