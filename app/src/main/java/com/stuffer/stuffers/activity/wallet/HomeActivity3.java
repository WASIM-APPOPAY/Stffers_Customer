package com.stuffer.stuffers.activity.wallet;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_CCODE;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_LANGUAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.MyContextWrapper;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.loan.L_HomeActivity;
import com.stuffer.stuffers.activity.loan.L_IntroActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.api.MainLoanInterface;
import com.stuffer.stuffers.commonChat.chat.BaseActivity;
import com.stuffer.stuffers.commonChat.chat.BottomChatFragment;
import com.stuffer.stuffers.commonChat.chat.ChatActivity;
import com.stuffer.stuffers.commonChat.chat.UserSelectDialogFragment;
import com.stuffer.stuffers.commonChat.chatModel.AttachmentTypes;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.Contact;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.chatUtils.ConfirmationDialogFragment;
import com.stuffer.stuffers.commonChat.interfaces.ChatItemClickListener;
import com.stuffer.stuffers.commonChat.interfaces.ProceedRequest;
import com.stuffer.stuffers.commonChat.interfaces.UserGroupSelectionDismissListener;
import com.stuffer.stuffers.communicator.CashTransferListener;
import com.stuffer.stuffers.communicator.CommonListener;
import com.stuffer.stuffers.communicator.LanguageListener;
import com.stuffer.stuffers.communicator.StartActivityListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomLanguage;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomRegister;
import com.stuffer.stuffers.fragments.landing.LandingFragment;
import com.stuffer.stuffers.fragments.landing.LifeFragment;
import com.stuffer.stuffers.myService.FetchMyUsersService;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity3 extends BaseActivity implements View.OnClickListener, ChatItemClickListener, ProceedRequest, UserGroupSelectionDismissListener, CashTransferListener, CommonListener, StartActivityListener, LanguageListener {
    private static final String TAG = "HomeActivity3";
    private static final int REQUEST_CODE_CHAT_FORWARD = 99;
    private static String CONFIRM_TAG = "confirmtag";
    private static String USER_SELECT_TAG = "userselectdialog";
    private final int CONTACTS_REQUEST_CODE = 321;
    private ArrayList<User> myUsers = new ArrayList<>();
    private ArrayList<Message> messageForwardList = new ArrayList<>();
    private UserSelectDialogFragment userSelectDialogFragment;
    private CountryCodePicker ccWhere;
    private ChatHelper helper;
    private User userMe;
    private PhoneNumberUtil phoneUtil;
    private BottomRegister mBottomRegister;
    private LinearLayout llMsg;
    private MainAPIInterface apiService;
    private String mShare = "";
    private static CircleImageView ivUser;
    private MyTextView tvMobileNumber, tvDrawername, tvDrawerNo, tvVersion, tvSideBalance;
    private MyTextViewBold tvUserName;
    private LinearLayout layoutMyCards;
    public static Activity mCtx;
    private LinearLayout layoutAccount, llHome, llMe, llFinance, llLife;
    private LinearLayout layoutProfile;
    private LinearLayout layoutSetting, layoutLanguage;
    private ImageView menu_icon;
    private DrawerLayout drawer_layout;
    private ProgressDialog mProgress;
    protected Context mContext;
    private DatabaseReference myInboxRef;
    private MainLoanInterface apiServiceLoan;
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
    private ArrayList<MyTextViewBold> mBottomList;
    private BottomLanguage mBottomLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        apiServiceLoan = ApiUtils.getApiServiceLoan();
        setContentView(R.layout.activity_home3);
        mCtx = HomeActivity3.this;
        menu_icon = (ImageView) findViewById(R.id.menu_icon_drawer);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivUser = findViewById(R.id.ivUser);
        tvDrawername = (MyTextView) findViewById(R.id.tvDrawername);
        tvDrawerNo = (MyTextView) findViewById(R.id.tvDrawerNo);
        tvSideBalance = (MyTextView) findViewById(R.id.tvSideBalance);
        tvVersion = (MyTextView) findViewById(R.id.tvVersion);
        layoutMyCards = findViewById(R.id.layoutMyCards);
        layoutAccount = (LinearLayout) findViewById(R.id.layoutAccount);
        layoutProfile = (LinearLayout) findViewById(R.id.layoutProfile);
        layoutSetting = (LinearLayout) findViewById(R.id.layoutSetting);
        layoutLanguage = (LinearLayout) findViewById(R.id.layoutLanguage);
        llHome = (LinearLayout) findViewById(R.id.llHome);
        llLife = (LinearLayout) findViewById(R.id.llLife);
        llMe = (LinearLayout) findViewById(R.id.llMe);
        llFinance = (LinearLayout) findViewById(R.id.llFinance);
        mBottomList = new ArrayList<>();
        mBottomList.add(findViewById(R.id.bottom_title_landing1));
        mBottomList.add(findViewById(R.id.bottom_title_landing2));
        mBottomList.add(findViewById(R.id.bottom_title_landing3));
        mBottomList.add(findViewById(R.id.bottom_title_landing4));
        mBottomList.add(findViewById(R.id.bottom_title_landing5));

        mBottomList.get(0).setTextColor(Color.parseColor("#ED7014"));

        layoutMyCards.setOnClickListener(this);
        layoutAccount.setOnClickListener(this);
        layoutProfile.setOnClickListener(this);
        layoutSetting.setOnClickListener(this);
        llHome.setOnClickListener(this);
        llLife.setOnClickListener(this);
        llMe.setOnClickListener(this);
        llFinance.setOnClickListener(this);

        apiService = ApiUtils.getAPIService();
        ccWhere = (CountryCodePicker) findViewById(R.id.ccWhere);
        llMsg = findViewById(R.id.llMsg);
        helper = new ChatHelper(this);
        userMe = helper.getLoggedInUser();
        ccWhere.setDialogEventsListener(mLis);
        registerChatUpdates();

        reflectCountry();

        LandingFragment mLandingFragment = new LandingFragment();
        initFragment(mLandingFragment);
        /*HomeLandingFragment mLandingFragment = new HomeLandingFragment();
        initFragment(mLandingFragment);*/
        isAppoPayAccountExist(userMe.getId(), userMe.getName());
        llMsg.setOnClickListener(this);

        //tvUserName.setText(userMe.getName());
        //tvMobileNumber.setText("+" + userMe.getId());
        tvDrawername.setText(userMe.getName());
        tvDrawerNo.setText("+" + userMe.getId());

        tvVersion.setText(getString(R.string.info_version) + BuildConfig.VERSION_NAME);

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
                drawer_layout.requestLayout();
                drawer_layout.bringToFront();
            }
        });


        updateFcmToken();
        refreshMyContacts();
        LinearLayout layoutLogout = (LinearLayout) findViewById(R.id.layoutLogout);
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutClick();
            }
        });

        layoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLanDialogue();
            }
        });
    }

    private void showLanDialogue() {
        mBottomLanguage = new BottomLanguage();
        mBottomLanguage.show(getSupportFragmentManager(), mBottomLanguage.getTag());
        mBottomLanguage.setCancelable(false);

    }

    private void onLogoutClick() {
        drawer_layout.closeDrawer(GravityCompat.START);
        ((AppoPayApplication) getApplication()).cancelTimer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logoutCalled();
            }
        }, 200);
    }

    private void logoutUserRequest() {
        tvSideBalance.setText("$" + "00.00");
        DataVaultManager.getInstance(HomeActivity3.this).saveUserAccessToken("");
        DataVaultManager.getInstance(HomeActivity3.this).saveUserDetails("");
        DataVaultManager.getInstance(HomeActivity3.this).saveCardToken("");


    }

    private void logoutCalled() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity3.this, R.style.MyAlertDialogStyle);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.info_want_to_logout));
        builder.setIcon(R.drawable.appopay_gift_card);
        builder.setPositiveButton(getString(R.string.info_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logoutUserRequest();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(getString(R.string.info_no),

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        builder.show();
    }

    private void isAppoPayAccountExist(String id, String name) {
        try {
            if (phoneUtil == null) {
                phoneUtil = PhoneNumberUtil.createInstance(HomeActivity3.this);
            }
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse("+" + id, "");
            isAccountExist(numberProto.getCountryCode(), numberProto.getNationalNumber());
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

    }

    private void isAccountExist(int countryCode, long nationalNumber) {


        /*mBottomRegister = new BottomRegister();
        Bundle mBundle = new Bundle();
        mBundle.putString(BottomRegister.COMMON_CLOSE, "CLOSE");
        mBundle.putString(BottomRegister.COMMON_APPLY, "APPLY");
        mBundle.putString(BottomRegister.COMMON_HEADING, "Register Now");
        mBundle.putString(BottomRegister.COMMON_BODY, "");
        mBundle.putString(BottomRegister.COMMON_FROM, "AppReg");

        mBottomRegister.show(getSupportFragmentManager(), mBottomRegister.getTag());
        mBottomRegister.setCancelable(false);*/


        String url = Constants.APPOPAY_BASE_URL + "api/users/checkUserExist?" + "customerType=CUSTOMER" + "&phoneCode=" + "" + countryCode + "&mobileNumber=" + "" + nationalNumber + "&";

        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("message").equalsIgnoreCase("success")) {
                                JSONObject jsonObject = response.getJSONObject(AppoConstants.RESULT);
                                JSONObject jsonObject1 = jsonObject.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                                JSONArray jsonArray = jsonObject1.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
                                JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                                String currentBalance = jsonObject2.getString(AppoConstants.CURRENTBALANCE);
                                float twoDecimal = Helper.getTwoDecimal(Float.parseFloat(currentBalance));
                                Helper.setUserDetailsNull();
                                DataVaultManager.getInstance(HomeActivity3.this).saveUserDetails(response.toString());
                                tvSideBalance.setText("");
                                tvSideBalance.setText("$" + twoDecimal);



                            } else {
                                mBottomRegister = new BottomRegister();
                                Bundle mBundle = new Bundle();
                                mBundle.putString(BottomRegister.COMMON_CLOSE, "CLOSE");
                                mBundle.putString(BottomRegister.COMMON_APPLY, "APPLY");
                                mBundle.putString(BottomRegister.COMMON_HEADING, "Register Now");
                                mBundle.putString(BottomRegister.COMMON_BODY, "");
                                mBundle.putString(BottomRegister.COMMON_FROM, "AppReg");

                                mBottomRegister.show(getSupportFragmentManager(), mBottomRegister.getTag());
                                mBottomRegister.setCancelable(false);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }

    private void initFragment(Fragment mFrag) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.landingContainer, mFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void reflectCountry() {
        try {
            try {

                ccWhere.setDialogEventsListener(mLis);
                try {

                    String vaultValue1 = DataVaultManager.getInstance(HomeActivity3.this).getVaultValue(KEY_CCODE);
                    if (!StringUtils.isEmpty(vaultValue1)) {
                        ccWhere.setCountryForNameCode(vaultValue1);
                    } else {
                        ccWhere.setCountryForPhoneCode(ccWhere.getDefaultCountryCodeAsInt());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                System.err.println("NumberParseException was thrown: " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCommonConfirm() {
        mBottomRegister.dismiss();
        Intent intent = new Intent(HomeActivity3.this, Registration.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE_CHAT_FORWARD):
                if (resultCode == Activity.RESULT_OK) {

                    messageForwardList.clear();
                    ArrayList<Message> temp = data.getParcelableArrayListExtra("FORWARD_LIST");
                    String s = new Gson().toJson(temp);

                    messageForwardList.addAll(temp);
                    userSelectDialogFragment = UserSelectDialogFragment.newUserSelectInstance(myUsers);
                    FragmentManager manager = getSupportFragmentManager();
                    Fragment frag = manager.findFragmentByTag(USER_SELECT_TAG);
                    if (frag != null) {
                        manager.beginTransaction().remove(frag).commit();
                    }
                    userSelectDialogFragment.show(manager, USER_SELECT_TAG);

                }
                break;
            case 200:
                Intent mIntentCamera = new Intent(HomeActivity3.this, CameraActivity.class);
                mIntentCamera.putExtra("front", true);
                startActivityForResult(mIntentCamera, 201);
                break;
            case 201:
                if (resultCode == RESULT_OK && data.getExtras() != null) {
                    try {

                        String stringExtra = data.getStringExtra(AppoConstants.IMAGE_PATH);

                        File file2 = new File(stringExtra);
                        FileInputStream imageInFile = new FileInputStream(file2);
                        byte[] imgData = new byte[(int) file2.length()];
                        imageInFile.read(imgData);
                        String imageDataString = encodeImage(imgData);

                        Glide.with(HomeActivity3.this).load(new File(stringExtra)).fitCenter().into(ivUser);
                        DataVaultManager.getInstance(HomeActivity3.this).saveIdImagePath(stringExtra);
                        uploadUserAvatar(imageDataString);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            case 100:
                //Log.e(TAG, "onActivityResult: called");
                if (resultCode == Activity.RESULT_OK) {
                    /*mShare = data.getStringExtra("link");

                    userSelectDialogFragment = UserSelectDialogFragment.newUserSelectInstance(myUsers);
                    FragmentManager manager = getSupportFragmentManager();
                    Fragment frag = manager.findFragmentByTag(USER_SELECT_TAG);
                    if (frag != null) {
                        manager.beginTransaction().remove(frag).commit();
                    }
                    userSelectDialogFragment.show(manager, USER_SELECT_TAG);*/

                    try {
                        isAccountExist(Integer.parseInt(Helper.getPhoneCode()), Helper.getSenderMobileNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                break;
        }
    }

    public void uploadUserAvatar(String avatar) {
        showLoading();
        int userId = Helper.getUserId();
        JsonObject mReqPayload = new JsonObject();
        mReqPayload.addProperty("userId", userId);
        mReqPayload.addProperty("avatar", "data:image/jpeg;base64," + avatar);
        apiService.putUserAvatar(mReqPayload).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideLoading();
                JsonObject body = response.body();
                if (response.isSuccessful()) {
                    try {
                        JSONObject mPrev = new JSONObject(body.toString());
                        if (mPrev.getString("message").equalsIgnoreCase("success")) {
                            String jsonUserDetails = mPrev.toString();
                            DataVaultManager.getInstance(HomeActivity3.this).saveUserDetails(jsonUserDetails);
                            JSONObject jsonObject = mPrev.getJSONObject(AppoConstants.RESULT);
                            String avatar = jsonObject.getString("avatar");
                            drawer_layout.openDrawer(GravityCompat.START);
                            DataVaultManager.getInstance(HomeActivity3.this).saveIdImagePath(avatar);
                            Toast.makeText(HomeActivity3.this, "avatar updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoading();
                Toast.makeText(HomeActivity3.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String encodeImage(byte[] imgData) {
        return Base64.encodeToString(imgData, Base64.DEFAULT);
    }

    public static void startResultForAccountActivity() {
        Intent mIntentQrCode = new Intent(mCtx, AccountActivity.class);
        mIntentQrCode.putExtra(AppoConstants.WHERE, 7);
        mCtx.startActivityForResult(mIntentQrCode, 100);
    }

    private void goToLoginScreen(int where) {

        if (where == 10) {
            Intent mIntentQrCode = new Intent(HomeActivity3.this, SignInActivity.class);
            startActivityForResult(mIntentQrCode, 200);
        } else {
            Intent intent = new Intent(HomeActivity3.this, SignInActivity.class);
            intent.putExtra(AppoConstants.WHERE, where);
            startActivity(intent);
        }


    }

    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.llMsg) {
            setActiveInActive(2);
            BottomChatFragment mBottomChatFragment = new BottomChatFragment();
            initFragment(mBottomChatFragment);
        } else if (view.getId() == R.id.llHome) {
            setActiveInActive(0);
            LandingFragment mLandingFragment = new LandingFragment();
            //HomeLandingFragment mLandingFragment = new HomeLandingFragment();
            initFragment(mLandingFragment);
        } else if (view.getId() == R.id.llLife) {
            setActiveInActive(1);
            LifeFragment mFragment = new LifeFragment();
            initFragment(mFragment);

        } else if (view.getId() == R.id.llMe) {
            setActiveInActive(4);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                    if (TextUtils.isEmpty(userData)) {
                        goToLoginScreen(9);
                    } else {
                        Intent mIntent = new Intent(HomeActivity3.this, CustomerProfileActivity.class);
                        mIntent.putExtra(AppoConstants.WHERE, 9);
                        startActivity(mIntent);
                    }
                }
            }, 200);
        } else if (view.getId() == R.id.llFinance) {
            setActiveInActive(3);
            /*Intent intentFinance = new Intent(HomeActivity3.this, L_IntroActivity.class);
            startActivity(intentFinance);*/
            getDetails();
        } else if (view.getId() == R.id.layoutMyCards) {
            drawer_layout.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                    if (TextUtils.isEmpty(userData)) {
                        goToLoginScreen(7);
                    } else {

                        startResultForAccountActivity();
                    }
                }
            }, 200);

        } else if (view.getId() == R.id.layoutAccount) {
            drawer_layout.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                    if (TextUtils.isEmpty(userData)) {
                        goToLoginScreen(7);
                    } else {
                        startResultForAccountActivity();
                    }
                }
            }, 200);
        } else if (view.getId() == R.id.layoutSetting) {
            drawer_layout.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                    if (TextUtils.isEmpty(userData)) {
                        goToLoginScreen(8);
                    } else {
                        Intent mIntentQrCode = new Intent(HomeActivity3.this, SettingActvity.class);
                        mIntentQrCode.putExtra(AppoConstants.WHERE, 8);
                        startActivity(mIntentQrCode);
                    }
                }
            }, 200);
        } else if (view.getId() == R.id.layoutProfile) {
            drawer_layout.closeDrawer(GravityCompat.START);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                    if (TextUtils.isEmpty(userData)) {
                        goToLoginScreen(9);
                    } else {
                        Intent mIntent = new Intent(HomeActivity3.this, CustomerProfileActivity.class);
                        mIntent.putExtra(AppoConstants.WHERE, 9);
                        startActivity(mIntent);
                    }
                }
            }, 200);
        }


    }

    private void setActiveInActive(int i) {
        for (int j = 0; j < mBottomList.size(); j++) {
            if (i == j) {
                mBottomList.get(j).setTextColor(Color.parseColor("#ED7014"));
            } else {
                mBottomList.get(j).setTextColor(Color.parseColor("#000000"));
            }
        }
    }

    private void getDetails() {
        showLoading();
        String param1 = "2017011900003";
        String param2 = "das123";
        String param3 = "en.corecoop.net";
        String base = param1 + "|" + param2 + "|" + param3;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        JsonObject mParam = new JsonObject();
        mParam.addProperty("MobileNo", userMe.getId());


        apiServiceLoan.getIsUserLogin_Or_Profile(mParam, authHeader).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse: " + response.body());


                hideLoading();
                if (response.code() == 200) {
                    if (response.body().getAsJsonPrimitive(AppoConstants.MESSAGE).getAsString().equalsIgnoreCase("fail")) {
                        Intent intentFinance = new Intent(HomeActivity3.this, L_IntroActivity.class);
                        startActivity(intentFinance);
                    } else {
                        Intent intentFinance = new Intent(HomeActivity3.this, L_HomeActivity.class);
                        startActivity(intentFinance);
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Log.e(TAG, "onFailure: called");
                Log.e(TAG, "onFailure: " + t.getMessage());
                hideLoading();


            }
        });
    }


    public void showLoading() {
        mProgress = new ProgressDialog(HomeActivity3.this);
        mProgress.setMessage("Please wait....");
        mProgress.show();
    }

    public void hideLoading() {
        mProgress.dismiss();
        mProgress = null;
    }

    @Override
    public void onChatItemClick(Chat chat, int position, View userImage) {
        //Log.e(TAG, "onChatItemClick: called" );
        openChat(ChatActivity.newIntent(HomeActivity3.this, messageForwardList, chat, mShare), userImage);
    }

    private void openChat(Intent intent, View userImage) {
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity3.this, userImage, "userImage");
        startActivityForResult(intent, REQUEST_CODE_CHAT_FORWARD, activityOptionsCompat.toBundle());
        if (userSelectDialogFragment != null) {
            userSelectDialogFragment.dismiss();
            messageForwardList.clear();
            //mShare = "";
        }

    }

    protected void onDestroy() {
        //markOnline(false);
        if (myInboxRef != null && chatChildEventListener != null)
            myInboxRef.removeEventListener(chatChildEventListener);
        super.onDestroy();
    }

    private Chat mTemp;
    private ChildEventListener chatChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (mContext != null) {
                Message newMessage = dataSnapshot.getValue(Message.class);
                if (newMessage != null && newMessage.getId() != null && newMessage.getChatId() != null) {

                    if (newMessage.getAttachmentType() == AttachmentTypes.NONE_NOTIFICATION) {
                        setNotificationMessageNames(newMessage);
                    }
                    mTemp = null;
                    Chat newChat = new Chat(newMessage, newMessage.getSenderId().equals(userMe.getId()));
                    mTemp = newChat;


                    if (!newChat.isGroup()) {
                        newChat.setChatName(getNameById(newChat.getUserId()));
//                            for (User user : myUsers) {
//                                if (user.getId().equals(newChat.getUserId())) {
//                                    newChat.setChatName(user.getNameToDisplay());
//                                    break;
//                                }
//                            }
                    }
                    //if (adapter != null) {
                    Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.landingContainer);
                    if (mFragment instanceof BottomChatFragment) {
                        ((BottomChatFragment) mFragment).addMessage(newChat);
                    }

                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (mContext != null) {
                Message updatedMessage = dataSnapshot.getValue(Message.class);
                if (updatedMessage != null && updatedMessage.getId() != null && updatedMessage.getChatId() != null) {
                    if (updatedMessage.getAttachmentType() == AttachmentTypes.NONE_NOTIFICATION) {
                        setNotificationMessageNames(updatedMessage);
                    }

                    Chat newChat = new Chat(updatedMessage, updatedMessage.getSenderId().equals(userMe.getId()));
                    if (!newChat.isGroup()) {
                        newChat.setChatName(getNameById(newChat.getUserId()));
                        Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.landingContainer);
                        if (mFragment instanceof BottomChatFragment) {
                            ((BottomChatFragment) mFragment).addMessage(newChat);
                        }
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void registerChatUpdates() {
        if (myInboxRef == null) {
            myInboxRef = inboxRef.child(userMe.getId());
            myInboxRef.addChildEventListener(chatChildEventListener);
        }
    }


    private void refreshMyContacts() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            if (!FetchMyUsersService.STARTED) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    firebaseUser.getIdToken(false).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            FetchMyUsersService.startMyUsersService(HomeActivity3.this, userMe.getId(), idToken);
                        }
                    });
                }
            }
        } else {
            FragmentManager manager = getSupportFragmentManager();
            ConfirmationDialogFragment confirmationDialogFragment = ConfirmationDialogFragment.newConfirmInstance(getString(R.string.permission_contact_title),
                    getString(R.string.permission_contact_message), getString(R.string.okay), getString(R.string.no),
                    view -> {
                        ActivityCompat.requestPermissions(HomeActivity3.this, new String[]{android.Manifest.permission.READ_CONTACTS}, CONTACTS_REQUEST_CODE);
                    },
                    view -> {
                        finish();
                    });
            confirmationDialogFragment.show(manager, CONFIRM_TAG);
        }
    }


    private BroadcastReceiver userReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(ChatHelper.BROADCAST_USER_ME)) {
                //userUpdated();
            }
        }
    };
    private BroadcastReceiver myUsersReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<User> myUsers = intent.getParcelableArrayListExtra("data");
            if (myUsers != null) {
                //myusers includes inviteAble users with separator tag
                chatHelper.setMyUsers(myUsers);
                myUsersResult(myUsers);
            }
        }
    };

    private BroadcastReceiver myContactsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshMyContactsCache((HashMap<String, Contact>) intent.getSerializableExtra("data"));
            /*refreshMyContactsCache((HashMap<String, Contact>) intent.getSerializableExtra("data"));
            MyChatsFragment userChatsFragment = null, groupChatsFragment = null;
            if (adapter != null && adapter.getCount() > 1)
                userChatsFragment = ((MyChatsFragment) adapter.getItem(0));
            if (adapter != null && adapter.getCount() >= 2)
                groupChatsFragment = ((MyChatsFragment) adapter.getItem(1));
            if (userChatsFragment != null) userChatsFragment.resetChatNames(getSavedContacts());
            if (groupChatsFragment != null) groupChatsFragment.resetChatNames(getSavedContacts());*/
        }
    };

    private void myUsersResult(ArrayList<User> myUsers) {
        this.myUsers.clear();
        this.myUsers.addAll(myUsers);
        //refreshUsers(-1);
        //menuUsersRecyclerAdapter.notifyDataSetChanged();
        //swipeMenuRecyclerView.setRefreshing(false);

        registerChatUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CONTACTS_REQUEST_CODE:
                refreshMyContacts();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        localBroadcastManager.registerReceiver(myContactsReceiver, new IntentFilter(ChatHelper.BROADCAST_MY_CONTACTS));
        localBroadcastManager.registerReceiver(myUsersReceiver, new IntentFilter(ChatHelper.BROADCAST_MY_USERS));
        localBroadcastManager.registerReceiver(userReceiver, new IntentFilter(ChatHelper.BROADCAST_USER_ME));
        upDateBalance();
        reflectCountry();
    }

    public static void showProfileAvatar(String avatar) {

        String idPath = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_IDPATH);
        if (!StringUtils.isEmpty(avatar)) {
            if (avatar.startsWith("http"))
                Glide.with(AppoPayApplication.getInstance()).load(avatar).fitCenter().into(ivUser);

        } else {
            if (!StringUtils.isEmpty(idPath)) {
                Glide.with(AppoPayApplication.getInstance()).load(new File(idPath)).fitCenter().into(ivUser);
            }
        }
    }

    public static void showProfileAvatarLogin(String avatar) {

        String idPath = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_IDPATH);
        if (!StringUtils.isEmpty(avatar)) {
            if (avatar.startsWith("http"))
                Glide.with(AppoPayApplication.getInstance()).load(avatar).fitCenter().into(ivUser);

        } else {
            if (!StringUtils.isEmpty(idPath)) {
                Glide.with(AppoPayApplication.getInstance()).load(new File(idPath)).fitCenter().into(ivUser);
            }
        }
        /*Glide.with(AppoPayApplication.getInstance()).load(avatar).fitCenter().placeholder(R.drawable.user_chat).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveIdImagePath(avatar);


                return false;
            }
        }).into(ivUser);*/
    }

    private void updateFcmToken() {
        OneSignal.addSubscriptionObserver(stateChanges -> {

            if (!stateChanges.getFrom().isSubscribed() && stateChanges.getTo().isSubscribed()) {
                usersRef.child(userMe.getId()).child("userPlayerId").setValue(stateChanges.getTo().getUserId());
                helper.setMyPlayerId(stateChanges.getTo().getUserId());
                DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveNotificationKey(stateChanges.getTo().getUserId());

            }
        });
        OSDeviceState status = OneSignal.getDeviceState();
        if (status != null && status.isSubscribed() && status.getUserId() != null) {
            usersRef.child(userMe.getId()).child("userPlayerId").setValue(status.getUserId());
            helper.setMyPlayerId(status.getUserId());
            DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveNotificationKey(status.getUserId());
        }
    }

    public void upDateBalance() {

        try {
            String currantBalance = Helper.getCurrantBalance();
            DecimalFormat df2 = new DecimalFormat("#.00");
            Double doubleV = Double.parseDouble(currantBalance);
            String format = df2.format(doubleV);
            tvSideBalance.setText("$0");
            tvSideBalance.setText("$" + format);

        } catch (Exception e) {
            //tvSideBalance.setText("$0");
            tvSideBalance.setText("$" + "0.00");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        localBroadcastManager.unregisterReceiver(myContactsReceiver);
        localBroadcastManager.unregisterReceiver(myUsersReceiver);
        localBroadcastManager.unregisterReceiver(userReceiver);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void placeCall(boolean callIsVideo, User user) {

    }


    @Override
    public void onProceedRequest(int where) {

    }

    @Override
    public void onUserGroupSelectDialogDismiss(ArrayList<User> selectedUsers) {
        messageForwardList.clear();
    }

    @Override
    public void selectionDismissed() {

    }

    @Override
    public void onTransferSelect(int type) {

    }


    @Override
    public void OnStartActivityRequest(int requestCode) {
        if (requestCode == 3) {
            Intent mIntent = new Intent(HomeActivity3.this, P2PTransferActivity.class);
            mIntent.putExtra(AppoConstants.WHERE, 3);
            startActivityForResult(mIntent, 100);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        String userLanguage = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_LANGUAGE);
        if (StringUtils.isEmpty(userLanguage)) {
            //userLanguage = "es";
            userLanguage = "en";
        }
        super.attachBaseContext(MyContextWrapper.wrap(newBase, userLanguage));
    }

    @Override
    public void onLanguageSelect(String lan) {
        mBottomLanguage.dismiss();
        DataVaultManager.getInstance(HomeActivity3.this).saveLanguage(lan);
        Intent intent = new Intent(HomeActivity3.this, HomeActivity3.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}