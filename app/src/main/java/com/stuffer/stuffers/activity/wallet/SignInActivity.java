package com.stuffer.stuffers.activity.wallet;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_BASE_64;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_CCODE;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_UNIQUE_NUMBER;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_LANGUAGE;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.MyContextWrapper;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.FianceTab.UnionPayActivity;
import com.stuffer.stuffers.activity.forgopassword.ForgotPasswordActvivity;
import com.stuffer.stuffers.activity.quick_pass.VisaUnionActivity;
import com.stuffer.stuffers.activity.restaurant.E_ShopActivity;
import com.stuffer.stuffers.activity.restaurant.E_StoreDiscountActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chat.TransferChatActivity;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.communicator.AreaSelectListener;
import com.stuffer.stuffers.communicator.OnTransactionPinSuccess;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.communicator.UnionPayCardListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomNotCard;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomPasswordPolicy;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomRegister;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomTransactionPin;
import com.stuffer.stuffers.fragments.dialog.AreaCodeDialog;
import com.stuffer.stuffers.models.output.MappingResponse2;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements AreaSelectListener, OnTransactionPinSuccess, TransactionPinListener, UnionPayCardListener {
    String mPinTag = "TransactionTag";
    MyTextView signup;
    MyTextView signin1, signin11;

    MyEditText edtMobile, edtPassword;
    String strMobile, strPassword;
    ProgressDialog dialog;
    MainAPIInterface mainAPIInterface;
    private String TAG = "TAG";
    private AppCompatSpinner spCountry;
    private ImageView ivRefresh;


    private CountryCodePicker edtCustomerCountryCode;
    private String selectedCountryCode;

    private FirebaseAuth mAuth;
    private long mLastClickTime = 0;
    MyTextView tvForgotPassword;
    private MyTextView tvPwdPolicy;
    Button tvEmailTest;
    private String mUserId;
    private DatabaseReference reference;
    private MyTextViewBold tvAreaCodeDo;
    private String selectedCountryNameCode = "";
    private String mDominicaAreaCode = "";
    private ArrayList<String> mAreaList;
    private AreaCodeDialog mAreaDialog;
    private int mType = 0;
    private String mAmount;
    private String mCCode;
    private String mMNumber;
    private static String EXTRA_DATA_CHAT = "extradatachat";
    private Chat chat;
    private BottomTransactionPin mBottomTransDialog;
    private ProgressDialog mProgress;
    private ChatHelper helper;
    private User userMe;
    private PhoneNumberUtil phoneUtil;
    private BottomNotCard mBottomNotCard;
    private ImageView ivFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        helper = new ChatHelper(this);
        userMe = helper.getLoggedInUser();
        if (getIntent().getExtras() != null) {
            mType = getIntent().getIntExtra(AppoConstants.WHERE, 0);
            if (mType == 5) {
                mAmount = getIntent().getStringExtra(AppoConstants.AMOUNT);
                mCCode = getIntent().getStringExtra(AppoConstants.AREACODE);
                mMNumber = getIntent().getStringExtra(AppoConstants.PHWITHCODE);
                chat = getIntent().getParcelableExtra(EXTRA_DATA_CHAT);

            }

            Bundle extras = getIntent().getExtras();
            if (extras.containsKey("open")) {
                mBottomTransDialog = new BottomTransactionPin();
                mBottomTransDialog.show(getSupportFragmentManager(), mPinTag);
                mBottomTransDialog.setCancelable(false);
            }

        } else {
            mType = 0;
        }
        tvAreaCodeDo = (MyTextViewBold) findViewById(R.id.tvAreaCodeDo);
        mAuth = FirebaseAuth.getInstance();
        mainAPIInterface = ApiUtils.getAPIService();
        edtCustomerCountryCode = findViewById(R.id.edtCustomerCountryCode);
        signup = (MyTextView) findViewById(R.id.signup);
        signin1 = (MyTextView) findViewById(R.id.signin1);

        signin11 = findViewById(R.id.signin11);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvPwdPolicy = findViewById(R.id.tvPwdPolicy);


        edtMobile = (MyEditText) findViewById(R.id.edtMobile);
        edtPassword = (MyEditText) findViewById(R.id.edtPassword);

        spCountry = (AppCompatSpinner) findViewById(R.id.spCountry);
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);

        edtCustomerCountryCode.setExcludedCountries(getString(R.string.info_exclude_countries));
        selectedCountryNameCode = edtCustomerCountryCode.getSelectedCountryNameCode();

        edtCustomerCountryCode.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                selectedCountryNameCode = edtCustomerCountryCode.getSelectedCountryNameCode();
                //your code
                TextView title = (TextView) dialog.findViewById(R.id.textView_title);
                title.setText(getString(R.string.info_cc_reg));
            }

            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
                //your code
            }

            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {
                //your code
            }
        });

        ivFlag = findViewById(R.id.ivFlag);
        String ccode = DataVaultManager.getInstance(this).getVaultValue(KEY_CCODE);
        edtCustomerCountryCode.setCountryForNameCode(ccode);
        ImageView imageViewFlag = edtCustomerCountryCode.getImageViewFlag();
        Bitmap bitmap = ((BitmapDrawable) imageViewFlag.getDrawable()).getBitmap();
        ivFlag.setImageBitmap(bitmap);


        signin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strMobile = edtMobile.getText().toString();
                strPassword = edtPassword.getText().toString();
                if (strMobile.length() < 5) {
                    edtMobile.setFocusable(true);
                    edtMobile.setError(getString(R.string.info_enter_mobile_number));
                } else if (strPassword.length() < 5) {
                    edtPassword.setFocusable(true);
                    edtPassword.setError(getString(R.string.info_enter_password));
                } else {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {

                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (AppoPayApplication.isNetworkAvailable(SignInActivity.this)) {
                        userMapping();
                    } else {
                        Toast.makeText(SignInActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
                //237,465

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //isAppoPayAccountExist(userMe.getId(), userMe.getName());
                Intent mIntent = new Intent(SignInActivity.this, Registration.class);
                startActivity(mIntent);
                finish();


            }
        });


        signin11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throw new RuntimeException();
            }
        });
        signin11.setVisibility(View.GONE);

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppoPayApplication.isNetworkAvailable(SignInActivity.this)) {
                    Intent intentForgot = new Intent(SignInActivity.this, ForgotPasswordActvivity.class);
                    startActivity(intentForgot);
                } else {
                    Toast.makeText(SignInActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvPwdPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomPasswordPolicy bottomPasswordPolicy = new BottomPasswordPolicy();
                bottomPasswordPolicy.show(getSupportFragmentManager(), bottomPasswordPolicy.getTag());


            }
        });


        edtCustomerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selectedCountryNameCode = edtCustomerCountryCode.getSelectedCountryNameCode();
                if (selectedCountryNameCode.equalsIgnoreCase("DO")) {
                    mDominicaAreaCode = "";
                    tvAreaCodeDo.setVisibility(View.GONE);
                } else {
                    mDominicaAreaCode = "";
                    tvAreaCodeDo.setVisibility(View.GONE);
                }
                ImageView imageViewFlag = edtCustomerCountryCode.getImageViewFlag();
                Bitmap bitmap = ((BitmapDrawable) imageViewFlag.getDrawable()).getBitmap();
                ivFlag.setImageBitmap(bitmap);
            }
        });

        tvAreaCodeDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAreaCodes();
            }
        });
//showNoCardDialog();

    }

    private void isAppoPayAccountExist(String id, String name) {
        try {
            if (phoneUtil == null) {
                phoneUtil = PhoneNumberUtil.createInstance(SignInActivity.this);
            }
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse("+" + id, "");
            isAccountExist(numberProto.getCountryCode(), numberProto.getNationalNumber());
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

    }


    private void isAccountExist(int countryCode, long nationalNumber) {
        showLoading();

        String url = Constants.APPOPAY_BASE_URL + "api/users/checkUserExist?" + "customerType=CUSTOMER" + "&phoneCode=" + "" + countryCode + "&mobileNumber=" + "" + nationalNumber + "&";

        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        try {
                            if (response.getString("message").equalsIgnoreCase("success")) {
                                //Log.e(TAG, "onResponse: " + response);
                                //Toast.makeText(SignInActivity.this, "Already account exist!", Toast.LENGTH_SHORT).show();
                                Intent mIntent = new Intent(SignInActivity.this, Registration.class);
                                startActivity(mIntent);
                                finish();
                            } else {
                                Intent mIntent = new Intent(SignInActivity.this, Registration.class);
                                startActivity(mIntent);
                                finish();

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        Toast.makeText(SignInActivity.this, "" + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void getAreaCodes() {
        mAreaList = new ArrayList<String>();
        mAreaList.add("809");
        mAreaList.add("829");
        mAreaList.add("849");

        mAreaDialog = new AreaCodeDialog();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.TITLE, getString(R.string.info_select_area_code));
        bundle.putStringArrayList(AppoConstants.INFO, mAreaList);
        mAreaDialog.setArguments(bundle);
        mAreaDialog.setCancelable(false);
        mAreaDialog.show(getSupportFragmentManager(), mAreaDialog.getTag());


    }


    private void userMapping() {
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage(getString(R.string.info_mapping_user));
        dialog.show();
        selectedCountryCode = edtCustomerCountryCode.getSelectedCountryCode();

        //JsonObject mJsonObject = new JsonObject();
        //mJsonObject.addProperty("phoneCode", selectedCountryCode);
        //mJsonObject.addProperty("mobile", edtMobile.getText().toString().trim());
        //mJsonObject.addProperty("userType", "CUSTOMER");
        //mainAPIInterface.getMapping("+" + selectedCountryCode + mDominicaAreaCode + edtMobile.getText().toString().trim()).enqueue(new Callback<MappingResponse>() {

        mainAPIInterface.getMapping3(selectedCountryCode, edtMobile.getText().toString().trim(), "CUSTOMER").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    JsonObject body = response.body();
                    if (body.has("result")) {
                        try {
                            if (body.get("result").getAsString().equalsIgnoreCase("failed")) {
                                Helper.showLongMessage(SignInActivity.this, body.get("message").getAsString());
                            }

                        } catch (Exception e) {
                            JsonObject result = body.getAsJsonObject("result");
                            MappingResponse2.Result mResult = new Gson().fromJson(result, new TypeToken<MappingResponse2.Result>() {
                            }.getType());

                            DataVaultManager.getInstance(SignInActivity.this).saveUniqueNumber(mResult.getUniqueNumber());
                            getAccessToken();
                        }
                    } else {

                    }

                } else {
                    if (response.code() == 502) {
                        Toast.makeText(SignInActivity.this, getString(R.string.info_bad_request), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(SignInActivity.this, getString(R.string.info_503), Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(SignInActivity.this, getString(R.string.info_user_not_exist), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();

                RequestBody body = call.request().body();
                String s = new Gson().toJson(body);


            }
        });


    }

    private void getAccessToken() {
        dialog = new ProgressDialog(SignInActivity.this);
        //dialog.setMessage("Please wait, getting access token.");
        dialog.setMessage(getString(R.string.info_please_wait_dots));
        dialog.show();
        String strUniqueNumber = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_UNIQUE_NUMBER);

        String userName = "devglan-client";
        String password = "devglan-secret";
        String base = userName + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        String loginPassword = edtPassword.getText().toString().trim();
        mainAPIInterface.getAuthorization2(authHeader, strUniqueNumber, loginPassword, "password").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {


                    if (response.body().has("access_token")) {
                        String access_token = response.body().get("access_token").getAsString();
                        DataVaultManager.getInstance(SignInActivity.this).saveUserAccessToken(access_token);
                        getSignInDetails();
                    } else {
                        if (response.body().has("result")) {
                            if (response.body().get("result").getAsString().equalsIgnoreCase("failed")) {
                                Toast.makeText(SignInActivity.this, "" + response.body().get("message").getAsString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                } else {
                    Toast.makeText(SignInActivity.this, getString(R.string.error_account_verification), Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
            }
        });


    }

    private void generateOtp() {
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage(getString(R.string.info_generating_otp));
        dialog.show();
        JsonObject param = new JsonObject();
        param.addProperty("phone_number", "+" + selectedCountryCode + edtMobile.getText().toString().trim());
        mainAPIInterface.getOtpforUserVerificaiton(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    response.body();
                    verifyUserOtp();
                } else {
                    Toast.makeText(SignInActivity.this, getString(R.string.info_otp_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void verifyUserOtp() {
        String phNoWithCode = "+" + selectedCountryCode + edtMobile.getText().toString().trim();
        Intent intent = new Intent(SignInActivity.this, VerifyOtpActivity.class);
        intent.putExtra("phone_number", phNoWithCode);
        startActivityForResult(intent, 121);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 121) {
            if (resultCode == Activity.RESULT_OK) {
                getSignInDetails();
            }
        }
    }

    private void getSignInDetails() {

        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setMessage(getString(R.string.info_verifying_user));
        dialog.show();
        String phNumber = edtMobile.getText().toString().trim();

        String vaultValue = "bearer " + DataVaultManager.getInstance(SignInActivity.this).getVaultValue(KEY_ACCESSTOKEN);
        String phWithDominica = mDominicaAreaCode + phNumber;

        mainAPIInterface.getLoginDetails(Long.parseLong(phWithDominica), Integer.parseInt(selectedCountryCode), vaultValue).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                JsonObject body = response.body();
                if (response.isSuccessful()) {
                    try {
                        JSONObject mPrev = new JSONObject(body.toString());
                        if (mPrev.getString("message").equalsIgnoreCase("success")) {
                            String jsonUserDetails = mPrev.toString();

                            Helper.setUserDetailsNull();
                            DataVaultManager.getInstance(SignInActivity.this).saveUserDetails(jsonUserDetails);
                            DataVaultManager.getInstance(SignInActivity.this).saveCCODE(selectedCountryNameCode);
                            JSONObject result;
                            try {
                                JSONObject obj = new JSONObject(jsonUserDetails);
                                JSONObject jsonObject = obj.getJSONObject(AppoConstants.RESULT);
                                mUserId = jsonObject.getString(AppoConstants.ID);
                                result = obj.getJSONObject(AppoConstants.RESULT);
                                //String keydemo = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_DEMO);
                                String keydemo = "";
                                if (result.getString(AppoConstants.TRANSACTIONPIN).isEmpty() || result.getString(AppoConstants.TRANSACTIONPIN).equalsIgnoreCase("null")) {
                                    mBottomTransDialog = new BottomTransactionPin();
                                    mBottomTransDialog.show(getSupportFragmentManager(), mPinTag);
                                    mBottomTransDialog.setCancelable(false);
                                } /*else if (StringUtils.isEmpty(keydemo)) {
                                    showNoCardDialog();

                                }*/ else {
                                    try {
                                        if (result.getString(AppoConstants.AVATAR).startsWith("http")) {
                                            HomeActivity3.showProfileAvatarLogin(result.getString(AppoConstants.AVATAR));
                                            goToScreen(mType);

                                        } /*else if (result.getBoolean("isPasswordOutdated")) {
                                            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActvivity.class);
                                            intent.putExtra("expire", "data");
                                            startActivity(intent);
                                            finish();
                                        }*/ else {
                                            goToScreen(mType);
                                        }


                                    } catch (Exception e) {
                                        goToScreen(mType);
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    private void showNoCardDialog() {
        mBottomNotCard = new BottomNotCard();
        mBottomNotCard.show(getSupportFragmentManager(), mBottomNotCard.getTag());
        mBottomNotCard.setCancelable(false);
    }

    public void goToScreen(int param) {
        if (mType == 0 || mType == 3 || mType == 11) {
            finish();
            return;
        } else if (mType == 7) {
            HomeActivity3.startResultForAccountActivity();
            finish();
            return;

        }
        Intent mIntent = null;
        switch (mType) {
            case 1:
                mIntent = new Intent(SignInActivity.this, AddMoneyToWallet.class);

                break;
            case 2:
                mIntent = new Intent(SignInActivity.this, MobileRechargeActivity.class);
                break;
            case 3:
                mIntent = new Intent(SignInActivity.this, P2PTransferActivity.class);
                break;
            case 4:
                mIntent = new Intent(SignInActivity.this, ScanPayActivity.class);
                break;
            case 5:
                mIntent = new Intent(SignInActivity.this, TransferChatActivity.class);
                mIntent.putExtra(AppoConstants.AMOUNT, mAmount);
                mIntent.putExtra(AppoConstants.AREACODE, mCCode);
                mIntent.putExtra(EXTRA_DATA_CHAT, chat);
                mIntent.putExtra(AppoConstants.PHWITHCODE, mMNumber);
                break;
            case 6:
                mIntent = new Intent(SignInActivity.this, MyQrCodeActivity.class);

                break;
            case 7:
                mIntent = new Intent(SignInActivity.this, AccountActivity.class);
                break;
            case 8:
                mIntent = new Intent(SignInActivity.this, SettingActvity.class);
                break;
            case 9:
                mIntent = new Intent(SignInActivity.this, CustomerProfileActivity.class);
                break;
            case 10:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case 11:
//                mIntent = new Intent(SignInActivity.this, CashSend.class);
                break;
            case 12:
                mIntent = new Intent(SignInActivity.this, E_ShopActivity.class);
                break;
            case 13:
                mIntent = new Intent(SignInActivity.this, E_StoreDiscountActivity.class);
                break;

        }
        mIntent.putExtra(AppoConstants.WHERE, mType);
        startActivity(mIntent);
        finish();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        String userLanguage = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_LANGUAGE);
        if (StringUtils.isEmpty(userLanguage)) {
            userLanguage = "en";
        }
        super.attachBaseContext(MyContextWrapper.wrap(newBase, userLanguage));
    }

    @Override
    public void onAreaSelected(int pos) {
        if (mAreaDialog != null) {
            mAreaDialog.dismiss();
        }
        mDominicaAreaCode = mAreaList.get(pos);
        tvAreaCodeDo.setText("Area Code : " + mDominicaAreaCode);
    }

    @Override
    public void onPinCreated() {
        if (mBottomTransDialog != null) mBottomTransDialog.dismiss();
        String base64 = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_BASE_64);
        if (!StringUtils.isEmpty(base64)) {
            uploadUserAvatar(base64);
        } else {

            //goToScreen(mType);

            //String keydemo = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_DEMO);
            showNoCardDialog();
        }


    }

    public void showLoading() {
        mProgress = new ProgressDialog(SignInActivity.this);
        mProgress.setMessage("Please wait....");
        mProgress.show();
    }

    public void hideLoading() {
        mProgress.dismiss();
        mProgress = null;
    }

    public void uploadUserAvatar(String avatar) {
        showLoading();
        int userId = Helper.getUserId();
        JsonObject mReqPayload = new JsonObject();
        mReqPayload.addProperty("userId", userId);
        mReqPayload.addProperty("avatar", "data:image/jpeg;base64," + avatar);

        mainAPIInterface.putUserAvatar(mReqPayload).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideLoading();
                JsonObject body = response.body();
                if (response.isSuccessful()) {
                    try {
                        JSONObject mPrev = new JSONObject(body.toString());
                        if (mPrev.getString("message").equalsIgnoreCase("success")) {
                            String jsonUserDetails = mPrev.toString();
                            DataVaultManager.getInstance(SignInActivity.this).saveUserDetails(jsonUserDetails);
                            JSONObject jsonObject = mPrev.getJSONObject(AppoConstants.RESULT);
                            String avatar = jsonObject.getString("avatar");
                            DataVaultManager.getInstance(SignInActivity.this).saveIdImagePath(avatar);
                            //DataVaultManager.getInstance(SignInActivity.this).saveIdImageSignup("");
                            HomeActivity3.showProfileAvatar(avatar);
                            //Toast.makeText(SignInActivity.this, "avatar updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                goToScreen(mType);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoading();
                Toast.makeText(SignInActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                goToScreen(mType);
            }
        });
    }

    @Override
    public void onPinConfirm(String pin) {

        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        int userId = Helper.getUserId();
        showLoading();
        mainAPIInterface.getSetTransactionPin(String.valueOf(userId), pin, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideLoading();

                JsonObject body = response.body();
                if (response.isSuccessful()) {
                    try {
                        JSONObject mPrev = new JSONObject(body.toString());
                        if (mPrev.getString("message").equalsIgnoreCase("success")) {
                            String jsonUserDetails = mPrev.toString();

                            Helper.setUserDetailsNull();
                            DataVaultManager.getInstance(SignInActivity.this).saveUserDetails(jsonUserDetails);
                            JSONObject result;
                            try {
                                JSONObject obj = new JSONObject(jsonUserDetails);
                                JSONObject jsonObject = obj.getJSONObject(AppoConstants.RESULT);
                                mUserId = jsonObject.getString(AppoConstants.ID);
                                //result = obj.getJSONObject(AppoConstants.RESULT);
                                onPinCreated();
                                /*try {
                                    goToScreen(mType);
                                } catch (Exception e) {
                                    goToScreen(mType);
                                }*/

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, getString(R.string.required_filled), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helper.showErrorMessage(SignInActivity.this, t.getMessage());
                hideLoading();
            }
        });


    }

    @Override
    public void onCardRequest(int type) {
        if (mBottomNotCard != null)
            mBottomNotCard.dismiss();
        if (type == 1) {
            Intent intentUnion = new Intent(SignInActivity.this, UnionPayActivity.class);
            intentUnion.putExtra(AppoConstants.CARDTYPE, type);
            startActivity(intentUnion);
            finish();
        } else {
            Intent intentUnion = new Intent(SignInActivity.this, VisaUnionActivity.class);
            intentUnion.putExtra(AppoConstants.CARDTYPE, type);
            startActivity(intentUnion);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SignInActivity.this, HomeActivity3.class);
        startActivity(i);
        finish();
    }
}
