package com.stuffer.stuffers.activity.wallet;

import static android.view.View.VISIBLE;

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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fasterxml.jackson.core.Version;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.MainActivity;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.cashSends.CashSend;
import com.stuffer.stuffers.activity.cashSends.SendCashActivity;
import com.stuffer.stuffers.activity.shop_mall.ShopAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chat.BaseActivity;
import com.stuffer.stuffers.commonChat.chat.BottomChatFragment;
import com.stuffer.stuffers.commonChat.chat.ChatActivity;
import com.stuffer.stuffers.commonChat.chat.UserSelectDialogFragment;
import com.stuffer.stuffers.commonChat.chatAdapters.MoreAdapter;
import com.stuffer.stuffers.commonChat.chatModel.AttachmentTypes;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.ChatMore;
import com.stuffer.stuffers.commonChat.chatModel.Contact;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.chatUtils.ConfirmationDialogFragment;
import com.stuffer.stuffers.commonChat.interfaces.ChatItemClickListener;
import com.stuffer.stuffers.commonChat.interfaces.MoreListener;
import com.stuffer.stuffers.commonChat.interfaces.ProceedRequest;
import com.stuffer.stuffers.commonChat.interfaces.UserGroupSelectionDismissListener;
import com.stuffer.stuffers.communicator.CashTransferListener;
import com.stuffer.stuffers.communicator.LinkAccountListener;
import com.stuffer.stuffers.communicator.ShopListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomNotCard;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomRegister;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomSendType;
import com.stuffer.stuffers.models.shop_model.ShopModel;
import com.stuffer.stuffers.myService.FetchMyUsersService;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
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

public class HomeActivity2 extends BaseActivity implements View.OnClickListener, ChatItemClickListener, ProceedRequest, MoreListener, ShopListener, UserGroupSelectionDismissListener, CashTransferListener, LinkAccountListener {
    private static final String TAG = "HomeActivity";
    private static final int REQUEST_CODE_CHAT_FORWARD = 99;
    private static String CONFIRM_TAG = "confirmtag";
    private static String USER_SELECT_TAG = "userselectdialog";
    private final int CONTACTS_REQUEST_CODE = 321;
    private ImageView ivMenu, menu_icon;
    private LinearLayout llChat, llService, llCall;
    private ChatHelper helper;
    User userMe;
    private MyTextViewBold tvUserName;
    private MyTextView tvMobileNumber, tvDrawername, tvDrawerNo, tvVersion, tvSideBalance;
    private DatabaseReference myInboxRef;
    private ArrayList<User> myUsers = new ArrayList<>();
    private ArrayList<Message> messageForwardList = new ArrayList<>();
    private DrawerLayout drawer_layout;
    private LinearLayout llMyQr;
    private UserSelectDialogFragment userSelectDialogFragment;
    private ImageView ivMenuBottom;
    private static CircleImageView ivUser;
    private RecyclerView rvBottomChat;
    private ImageView add_attachment;
    private int mRequestPosition = 0;
    private MoreAdapter mMoreAdapter;
    private ArrayList<ChatMore> moreItems;
    private RecyclerView rvShop;
    private FrameLayout frameLayout;
    private MainAPIInterface apiService;
    private ProgressDialog mProgress;
    private LinearLayout layoutAccount, layoutProfile, layoutSetting, layoutLogout, layoutMyCards;
    private String mShare = "";
    private BottomSendType mBottomSendType;
    //public static Context mCtx;
    public static Activity mCtx;
    private PhoneNumberUtil phoneUtil;
    private BottomRegister mBottomRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = HomeActivity2.this;
        helper = new ChatHelper(this);
        setContentView(R.layout.activity_home2);
        apiService = ApiUtils.getAPIService();
        frameLayout = findViewById(R.id.frameLayout);
        ivUser = findViewById(R.id.ivUser);
        rvBottomChat = (RecyclerView) findViewById(R.id.rvBottomChat);
        rvBottomChat.setLayoutManager(new GridLayoutManager(this, 4));
        ivMenuBottom = (ImageView) findViewById(R.id.ivMenuBottom);
        add_attachment = (ImageView) findViewById(R.id.add_attachment);
        menu_icon = (ImageView) findViewById(R.id.menu_icon_drawer);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        userMe = helper.getLoggedInUser();
        tvDrawername = (MyTextView) findViewById(R.id.tvDrawername);
        tvDrawerNo = (MyTextView) findViewById(R.id.tvDrawerNo);
        tvSideBalance = (MyTextView) findViewById(R.id.tvSideBalance);
        llMyQr = (LinearLayout) findViewById(R.id.llMyQr);
        layoutLogout = (LinearLayout) findViewById(R.id.layoutLogout);
        layoutMyCards = (LinearLayout) findViewById(R.id.layoutMyCards);


        tvUserName = (MyTextViewBold) findViewById(R.id.tvUserName);
        tvMobileNumber = (MyTextView) findViewById(R.id.tvMobileNumber);
        tvVersion = (MyTextView) findViewById(R.id.tvVersion);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        llChat = (LinearLayout) findViewById(R.id.layoutChat);
        llService = (LinearLayout) findViewById(R.id.layoutService);
        llCall = (LinearLayout) findViewById(R.id.layoutCalls);
        layoutAccount = (LinearLayout) findViewById(R.id.layoutAccount);
        layoutProfile = (LinearLayout) findViewById(R.id.layoutProfile);
        layoutSetting = (LinearLayout) findViewById(R.id.layoutSetting);

        layoutAccount.setOnClickListener(this);
        layoutProfile.setOnClickListener(this);
        layoutSetting.setOnClickListener(this);
        layoutMyCards.setOnClickListener(this);


        llChat.setOnClickListener(this);
        llService.setOnClickListener(this);
        llCall.setOnClickListener(this);
        llMyQr.setOnClickListener(this);
        ivMenuBottom.setOnClickListener(this);
        add_attachment.setOnClickListener(this);
        rvShop = findViewById(R.id.rvShop);
        rvShop.setLayoutManager(new LinearLayoutManager(HomeActivity2.this, LinearLayoutManager.HORIZONTAL, false));
        findViewById(R.id.customlayout_create_merchant).setOnClickListener(this);
        isAppoPayAccountExist(userMe.getId(), userMe.getName());
        ArrayList<ShopModel> mList = Helper.getShopItems(HomeActivity2.this);
        ShopAdapter adapter = new ShopAdapter(HomeActivity2.this, mList, this);
        rvShop.setAdapter(adapter);
        //getProfile();

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
                drawer_layout.requestLayout();
                drawer_layout.bringToFront();
            }
        });

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //1.Manage Account
                //2.Profile
                //3.All seller kept
                //4. Create business profile
                //5.Create Seller

                PopupMenu popup = new PopupMenu(HomeActivity2.this, ivMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.home_menu, popup.getMenu());
                popup.setForceShowIcon(true);


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_profile) {
                            /*Intent intent = new Intent(HomeActivity2.this, BusinessInfoActivity.class);
                            startActivity(intent);*/
                        } else if (item.getItemId() == R.id.action_search) {
                            Intent intent = new Intent(HomeActivity2.this, TabsActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.action_manage) {

                        } else if (item.getItemId() == R.id.action_settings) {
                            String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                            if (TextUtils.isEmpty(userData)) {
                                goToLoginScreen(8);
                            } else {
                                Intent mIntentQrCode = new Intent(HomeActivity2.this, SettingActvity.class);
                                mIntentQrCode.putExtra(AppoConstants.WHERE, 8);
                                startActivity(mIntentQrCode);
                            }
                        } else if (item.getItemId() == R.id.action_logout) {
                            DataVaultManager.getInstance(HomeActivity2.this).saveUserAccessToken("");
                            DataVaultManager.getInstance(HomeActivity2.this).saveUserDetails("");

                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        if (savedInstanceState == null) {
            BottomChatFragment mBottomChatFragment = new BottomChatFragment();
            initFragment(mBottomChatFragment);
        }

        tvUserName.setText(userMe.getName());
        tvMobileNumber.setText("+" + userMe.getId());
        tvDrawername.setText(userMe.getName());
        tvDrawerNo.setText("+" + userMe.getId());
        moreItems = ChatHelper.getMoreItems();
        registerChatUpdates();
        updateFcmToken();
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(GravityCompat.START);
                String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                if (TextUtils.isEmpty(userData)) {
                    goToLoginScreen(10);
                } else {
                    Intent mIntentQrCode = new Intent(HomeActivity2.this, CameraActivity.class);
                    mIntentQrCode.putExtra(AppoConstants.WHERE, 10);
                    mIntentQrCode.putExtra("front", true);
                    startActivityForResult(mIntentQrCode, 201);
                }
            }
        });
        String idPath = DataVaultManager.getInstance(HomeActivity2.this).getVaultValue(DataVaultManager.KEY_IDPATH);
        if (!StringUtils.isEmpty(idPath)) {
            if (idPath.startsWith("http"))
                Glide.with(HomeActivity2.this).load(idPath).fitCenter().into(ivUser);
            else
                Glide.with(HomeActivity2.this).load(new File(idPath)).fitCenter().into(ivUser);
        }
        layoutLogout.setOnClickListener(view -> {
            drawer_layout.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    logoutCalled();
                }
            }, 200);
        });
        tvVersion.setText(getString(R.string.info_version) + BuildConfig.VERSION_NAME);

        refreshMyContacts();


    }

    private void isAppoPayAccountExist(String id, String name) {

        try {

            if (phoneUtil == null) {
                phoneUtil = PhoneNumberUtil.createInstance(HomeActivity2.this);
            }
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse("+" + id, "");
            int countryCode = numberProto.getCountryCode();
            //Log.e(TAG, "onActivityResult: " + countryCode);
            long nationalNumber = numberProto.getNationalNumber();
            //Log.e(TAG, "isAppoPayAccountExist: " + nationalNumber);
            isAccountExist(countryCode, nationalNumber);

        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

    }

    /*{
        "rules": {
        // only authenticated users can read or write to my Firebase
        ".read": "auth !== null",
                ".write": "auth !== null"
    }
    }*/

    //Secure Rule
    /*{
        "rules": {
        "Tokens": {
            ".read": true,
                    ".write": true

        },
        "Users": {
            ".read": true,
                    ".write": true

        },
        "chats": {
            ".read": true,
                    ".write": true

        },
        "inbox": {
            ".read": true,
                    ".write": true

        },
        "users": {
            ".read": true,
                    ".write": true

        }




    }
    }*/

    private void isAccountExist(int countryCode, long nationalNumber) {
        //long mRequest = 987455555;

        apiService.getProfileDetails(nationalNumber, countryCode).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse: " + response.body());
                //{"result":"failed","message":"user not exist in system","status":500}
                if (response.code()==401){

                }else {
                JsonObject body = response.body();
                if (body.get("result").isJsonObject()) {
                    Log.e(TAG, "onResponse: yes");

                } else {
                    mBottomRegister = new BottomRegister();
                    mBottomRegister.show(getSupportFragmentManager(), mBottomRegister.getTag());
                    mBottomRegister.setCancelable(false);
                }
                }





            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });

    }

    public static void showProfileAvatar(String avatar) {
        //Log.e(TAG, "showProfileAvatar: called");
        String idPath = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_IDPATH);
        if (!StringUtils.isEmpty(idPath)) {
            if (idPath.startsWith("http"))
                Glide.with(AppoPayApplication.getInstance()).load(idPath).fitCenter().into(ivUser);
            else
                Glide.with(AppoPayApplication.getInstance()).load(new File(idPath)).fitCenter().into(ivUser);
        }
    }

    public void upDateBalance() {
        Log.e(TAG, "upDateBalance: called");
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

    /*private void getProfile() {
        showLoading();
        if (!TextUtils.isEmpty(helper.getLoggedInUser().getId())) {
            ApiUtils.getAPIService().getSellerByMobile(helper.getLoggedInUser().getId()).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        MerchantInfoBean ownInfoBean = new Gson().fromJson(response.body().get("result").toString(), MerchantInfoBean.class);
                        if (ownInfoBean != null) {
                            DataManager.merchantInfoBean = ownInfoBean;
                        }
                    } catch (Exception e) {

                    }
                    hideLoading();
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    hideLoading();
                }
            });
        }
    }*/

    public static void showProfileAvatarLogin(String avatar) {

        String idPath = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_IDPATH);
        if (!StringUtils.isEmpty(idPath)) {
            if (idPath.startsWith("http"))
                Glide.with(AppoPayApplication.getInstance()).load(idPath).fitCenter().into(ivUser);
            else
                Glide.with(AppoPayApplication.getInstance()).load(new File(idPath)).fitCenter().into(ivUser);
        }
        /*Glide.with(AppoPayApplication.getInstance()).load(avatar).fitCenter().placeholder(R.drawable.user_chat).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveIdImagePath(avatar);
                Log.e(TAG, "onResourceReady:called ");

                return false;
            }
        }).into(ivUser);*/
    }

    private void updateFcmToken() {
        OneSignal.addSubscriptionObserver(stateChanges -> {

            if (!stateChanges.getFrom().isSubscribed() && stateChanges.getTo().isSubscribed()) {
                usersRef.child(userMe.getId()).child("userPlayerId").setValue(stateChanges.getTo().getUserId());
                helper.setMyPlayerId(stateChanges.getTo().getUserId());
                //Log.e("TAG", "updateFcmToken: 1"+ stateChanges.getTo().getUserId());
            }
        });
        //{"id":"a8c2cfd1-021c-489b-8e57-e8fb2496036f","recipients":1,"external_id":null}
        //1d60d3334-1cb0-4dce-a7c5-0ee35b727ce0 //vivo

        OSDeviceState status = OneSignal.getDeviceState();
        if (status != null && status.isSubscribed() && status.getUserId() != null) {
            usersRef.child(userMe.getId()).child("userPlayerId").setValue(status.getUserId());
            helper.setMyPlayerId(status.getUserId());
            //Log.e("TAG", "updateFcmToken: 1"+ status.getUserId());
        }
    }

    private void registerChatUpdates() {
        if (myInboxRef == null) {
            myInboxRef = inboxRef.child(userMe.getId());
            myInboxRef.addChildEventListener(chatChildEventListener);
        }
    }

    private void initFragment(Fragment mFragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mHomeContainer, mFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        //markOnline(false);
        if (myInboxRef != null && chatChildEventListener != null)
            myInboxRef.removeEventListener(chatChildEventListener);
        super.onDestroy();
    }

    private ChildEventListener chatChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (mContext != null) {
                Message newMessage = dataSnapshot.getValue(Message.class);
                if (newMessage != null && newMessage.getId() != null && newMessage.getChatId() != null) {

                    if (newMessage.getAttachmentType() == AttachmentTypes.NONE_NOTIFICATION) {
                        setNotificationMessageNames(newMessage);
                    }

                    Chat newChat = new Chat(newMessage, newMessage.getSenderId().equals(userMe.getId()));
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
                    Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.mHomeContainer);
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
                        Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.mHomeContainer);
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

    @Override
    public void onBackPressed() {
        finish();
    }

    public void enable() {
        if (mMoreAdapter == null) {
            mMoreAdapter = new MoreAdapter(moreItems, HomeActivity2.this);
        }
        rvBottomChat.setAdapter(mMoreAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_attachment) {
            plusMinus();

        } else if (view.getId() == R.id.ivMenuBottom) {
            showPictureialog();
        } else if (view.getId() == R.id.layoutChat) {
            BottomChatFragment mBottomChatFragment = new BottomChatFragment();
            initFragment(mBottomChatFragment);
        } else if (view.getId() == R.id.layoutService) {
            /*BottomServiceFragment mBottomServiceFragment = new BottomServiceFragment();
            initFragment(mBottomServiceFragment);*/
        } else if (view.getId() == R.id.layoutCalls) {
            /*BottomCallFragment mBottomCallFragment = new BottomCallFragment();
            initFragment(mBottomCallFragment);*/
        } else if (view.getId() == R.id.llMyQr) {
            /*String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
            if (TextUtils.isEmpty(userData)) {
                mBottomNotAccount = new BottomNotAccount();
                Bundle mBundle = new Bundle();
                mBundle.putInt(AppoConstants.WHERE, 9);
                mBottomNotAccount.setArguments(mBundle);
                mBottomNotAccount.show(getSupportFragmentManager(), mBottomNotAccount.getTag());
                mBottomNotAccount.setCancelable(false);
            } else {
                Intent mIntentQrCode = new Intent(HomeActivity2.this, CustomerProfileActivity.class);
                mIntentQrCode.putExtra(AppoConstants.WHERE, 9);
                startActivity(mIntentQrCode);

            }*/
        } else if (view.getId() == R.id.customlayout_create_merchant) {
            /*Intent mIntentQrCode = new Intent(HomeActivity2.this, CreateProfileActivity.class);
            startActivity(mIntentQrCode);*/
        } else if (view.getId() == R.id.layoutSetting) {
            drawer_layout.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                    if (TextUtils.isEmpty(userData)) {
                        goToLoginScreen(8);
                    } else {
                        Intent mIntentQrCode = new Intent(HomeActivity2.this, SettingActvity.class);
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
                        Intent mIntent = new Intent(HomeActivity2.this, CustomerProfileActivity.class);
                        mIntent.putExtra(AppoConstants.WHERE, 9);
                        startActivity(mIntent);
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
        } else if (view.getId() == R.id.layoutMyCards) {
            drawer_layout.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                    if (TextUtils.isEmpty(userData)) {
                        goToLoginScreen(7);
                    } else {
                        /*Intent mIntentQrCode = new Intent(HomeActivity2.this, AccountActivity.class);
                        mIntentQrCode.putExtra(AppoConstants.WHERE, 7);
                        startActivity(mIntentQrCode);*/
                        startResultForAccountActivity();
                    }
                }
            }, 200);
        }
    }

    public static void startResultForAccountActivity() {
        Intent mIntentQrCode = new Intent(mCtx, AccountActivity.class);
        mIntentQrCode.putExtra(AppoConstants.WHERE, 7);
        mCtx.startActivityForResult(mIntentQrCode, 100);
    }

    public void disable() {

    }

    private void plusMinus() {
        if (rvBottomChat.getVisibility() == VISIBLE) {
            disable();
            rvBottomChat.setVisibility(View.GONE);
            add_attachment.animate().setDuration(400).rotationBy(-45).start();

        } else {
            enable();
            rvBottomChat.setVisibility(VISIBLE);
            add_attachment.animate().setDuration(400).rotationBy(-45).start();

        }
    }

    @Override
    public void onChatItemClick(Chat chat, int position, View userImage) {
        openChat(ChatActivity.newIntent(HomeActivity2.this, messageForwardList, chat, mShare), userImage);
    }

    private void openChat(Intent intent, View userImage) {
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity2.this, userImage, "userImage");
        startActivityForResult(intent, REQUEST_CODE_CHAT_FORWARD, activityOptionsCompat.toBundle());
        if (userSelectDialogFragment != null) {
            userSelectDialogFragment.dismiss();
            messageForwardList.clear();
            //mShare = "";
        }

    }

    @Override
    public void placeCall(boolean callIsVideo, User user) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE_CHAT_FORWARD):
                if (resultCode == Activity.RESULT_OK) {
                    //show forward dialog to choose users
                    messageForwardList.clear();
                    ArrayList<Message> temp = data.getParcelableArrayListExtra("FORWARD_LIST");
                    String s = new Gson().toJson(temp);
                    //Log.e(TAG, "onActivityResult: "+s );
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
                Intent mIntentCamera = new Intent(HomeActivity2.this, CameraActivity.class);
                mIntentCamera.putExtra("front", true);
                startActivityForResult(mIntentCamera, 201);
                break;
            case 201:
                if (resultCode == RESULT_OK && data.getExtras() != null) {
                    try {
                        Log.e(TAG, "onActivityResult: called");
                        String stringExtra = data.getStringExtra(AppoConstants.IMAGE_PATH);
                        //File file=new File(stringExtra);
                        File file2 = new File(stringExtra);
                        FileInputStream imageInFile = new FileInputStream(file2);
                        byte[] imgData = new byte[(int) file2.length()];
                        imageInFile.read(imgData);
                        String imageDataString = encodeImage(imgData);
                        //Log.e(TAG, "onActivityResult: " + imageDataString);
                        Glide.with(HomeActivity2.this).load(new File(stringExtra)).fitCenter().into(ivUser);
                        DataVaultManager.getInstance(HomeActivity2.this).saveIdImagePath(stringExtra);
                        uploadUserAvatar(imageDataString);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            case 100:
                Log.e(TAG, "onActivityResult: 100 called");
                if (resultCode == Activity.RESULT_OK) {
                    mShare = data.getStringExtra("link");
                    Log.e(TAG, "onActivityResult: " + mShare);
                    userSelectDialogFragment = UserSelectDialogFragment.newUserSelectInstance(myUsers);
                    FragmentManager manager = getSupportFragmentManager();
                    Fragment frag = manager.findFragmentByTag(USER_SELECT_TAG);
                    if (frag != null) {
                        manager.beginTransaction().remove(frag).commit();
                    }
                    userSelectDialogFragment.show(manager, USER_SELECT_TAG);
                }


                break;
        }
    }

    public void showLoading() {
        mProgress = new ProgressDialog(HomeActivity2.this);
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
                            DataVaultManager.getInstance(HomeActivity2.this).saveUserDetails(jsonUserDetails);
                            JSONObject jsonObject = mPrev.getJSONObject(AppoConstants.RESULT);
                            String avatar = jsonObject.getString("avatar");
                            drawer_layout.openDrawer(GravityCompat.START);
                            DataVaultManager.getInstance(HomeActivity2.this).saveIdImagePath(avatar);
                            Toast.makeText(HomeActivity2.this, "avatar updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoading();
                Toast.makeText(HomeActivity2.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String encodeImage(byte[] imgData) {
        return Base64.encodeToString(imgData, Base64.DEFAULT);
    }

    @Override
    public void onProceedRequest(int where) {
        //mBottomNotAccount.dismiss();
        if (where == 10) {
            Intent mIntentQrCode = new Intent(HomeActivity2.this, SignInActivity.class);
            startActivityForResult(mIntentQrCode, 200);
        } else {
            Intent mIntentQrCode = new Intent(HomeActivity2.this, SignInActivity.class);
            mIntentQrCode.putExtra(AppoConstants.WHERE, where);
            startActivity(mIntentQrCode);
        }


        ;
    }

    private void showPictureialog() {
        Dialog dialog = new Dialog(HomeActivity2.this,
                android.R.style.Theme_Translucent_NoTitleBar);

        // Setting dialogue
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.setTitle(null);

        dialog.setContentView(R.layout.service_dialog);
        LinearLayout llRecharge = window.findViewById(R.id.llRecharge);
        LinearLayout llPTransfer = window.findViewById(R.id.llPTransfer);
        LinearLayout llScan = window.findViewById(R.id.llScan);
        LinearLayout llBProfile = window.findViewById(R.id.llBProfile);
        LinearLayout llProfile = window.findViewById(R.id.llProfile);
        LinearLayout llCashSend = window.findViewById(R.id.llCashSend);
        LinearLayout layoutScanUnion = window.findViewById(R.id.layoutScanUnion);

        llCashSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);

                if (TextUtils.isEmpty(userData)) {

                    goToLoginScreen(11);
                } else {
                    mBottomSendType = new BottomSendType();
                    mBottomSendType.show(getSupportFragmentManager(), mBottomSendType.getTag());

                }

            }
        });

        llRecharge.setOnClickListener(view -> {
            String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
            if (TextUtils.isEmpty(userData)) {
                goToLoginScreen(2);
            } else {
                Intent mIntent = new Intent(HomeActivity2.this, MobileRechargeActivity.class);
                mIntent.putExtra(AppoConstants.WHERE, 2);
                startActivity(mIntent);
            }


        });

        llPTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                if (TextUtils.isEmpty(userData)) {
                    goToLoginScreen(3);
                } else {
                    Intent mIntent = new Intent(HomeActivity2.this, P2PTransferActivity.class);
                    mIntent.putExtra(AppoConstants.WHERE, 3);
                    startActivityForResult(mIntent, 100);
                }
            }
        });
        llScan.setOnClickListener(view -> {
            String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
            if (TextUtils.isEmpty(userData)) {
                goToLoginScreen(4);
            } else {
                Intent mIntent = new Intent(HomeActivity2.this, ScanPayActivity.class);
                mIntent.putExtra(AppoConstants.WHERE, 4);
                startActivity(mIntent);
            }
        });

        llBProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent mIntent = new Intent(HomeActivity2.this, CreateProfileActivity.class);
                startActivity(mIntent);*/
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
             if (TextUtils.isEmpty(userData)) {
                  goToLoginScreen(9);
              } else {
                Intent mIntent = new Intent(HomeActivity2.this, CustomerProfileActivity.class);
                mIntent.putExtra(AppoConstants.WHERE, 9);
                startActivity(mIntent);
             }
            }
        });

        layoutScanUnion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity2.this, InnerPayActivity.class);
                startActivity(intent);
            }
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void goToLoginScreen(int where) {

        if (where == 10) {
            Intent mIntentQrCode = new Intent(HomeActivity2.this, SignInActivity.class);
            startActivityForResult(mIntentQrCode, 200);
        } else {
            Intent intent = new Intent(HomeActivity2.this, SignInActivity.class);
            intent.putExtra(AppoConstants.WHERE, where);
            startActivity(intent);
        }


    }

    private void logoutCalled() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity2.this, R.style.MyAlertDialogStyle);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.info_want_to_logout));
        builder.setIcon(R.drawable.appopay_gift_card);
        builder.setPositiveButton(getString(R.string.info_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logoutUserRequest();
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

    private void logoutUserRequest() {
        DataVaultManager.getInstance(HomeActivity2.this).saveUserAccessToken("");
        DataVaultManager.getInstance(HomeActivity2.this).saveUserDetails("");
        DataVaultManager.getInstance(HomeActivity2.this).saveCardToken("");

        /*Intent i = new Intent(HomeActivity2.this, SignInActivity.class);
        startActivity(i);
        finish();*/


    }

    @Override
    public void onMoreItemClick(int pos) {
        mRequestPosition = pos;
        String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
        if (mRequestPosition == 5) {
            /*if (TextUtils.isEmpty(userData)) {
                mBottomNotAccount = new BottomNotAccount();
                Bundle mBundle = new Bundle();
                mBundle.putInt(AppoConstants.WHERE, 1);
                mBottomNotAccount.setArguments(mBundle);
                mBottomNotAccount.show(getSupportFragmentManager(), mBottomNotAccount.getTag());
                mBottomNotAccount.setCancelable(false);
            } else {
                plusMinus();
                *//*Intent mIntent = new Intent(HomeActivity2.this, AddMoneyToWallet.class);
                mIntent.putExtra(AppoConstants.WHERE, 1);
                startActivity(mIntent);*//*
                Intent mIntent = new Intent(HomeActivity2.this, FundCountry.class);
                mIntent.putExtra(AppoConstants.WHERE, 1);
                startActivity(mIntent);

            }*/
            plusMinus();
            Intent mIntent = new Intent(HomeActivity2.this, FundCountry.class);
            mIntent.putExtra(AppoConstants.WHERE, 1);
            startActivity(mIntent);
        }
    }

    @Override
    public void onShopItemClick(int pos, String title) {
        Intent intent = new Intent(HomeActivity2.this, TabsActivity.class);
        intent.putExtra(AppoConstants.WHERE, pos);
        startActivity(intent);
    }


    private void refreshMyContacts() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            if (!FetchMyUsersService.STARTED) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    firebaseUser.getIdToken(false).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            FetchMyUsersService.startMyUsersService(HomeActivity2.this, userMe.getId(), idToken);
                        }
                    });
                }
            }
        } else {
            FragmentManager manager = getSupportFragmentManager();
            ConfirmationDialogFragment confirmationDialogFragment = ConfirmationDialogFragment.newConfirmInstance(getString(R.string.permission_contact_title),
                    getString(R.string.permission_contact_message), getString(R.string.okay), getString(R.string.no),
                    view -> {
                        ActivityCompat.requestPermissions(HomeActivity2.this, new String[]{android.Manifest.permission.READ_CONTACTS}, CONTACTS_REQUEST_CODE);
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
    public void onUserGroupSelectDialogDismiss(ArrayList<User> selectedUsers) {
        messageForwardList.clear();
    }

    @Override
    public void selectionDismissed() {

    }

    @Override
    public void onTransferSelect(int type) {
        if (type == 0) {
            Intent mIntent = new Intent(HomeActivity2.this, CashSend.class);
            mIntent.putExtra(AppoConstants.WHERE, 11);
            startActivity(mIntent);
        } else {
            Intent mIntent = new Intent(HomeActivity2.this, SendCashActivity.class);
            mIntent.putExtra(AppoConstants.WHERE, 11);
            startActivity(mIntent);
        }
    }

    @Override
    public void onLinkAccountConfirm() {
        mBottomRegister.dismiss();
        Intent intent = new Intent(HomeActivity2.this, Registration.class);
        startActivity(intent);
    }
}