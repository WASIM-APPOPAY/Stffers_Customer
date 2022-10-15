package com.stuffer.stuffers.activity.wallet;

import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.os.BuildCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.JsonObject;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.cashSends.CashSend;
import com.stuffer.stuffers.activity.shop_mall.ShopAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chat.BaseActivity;
import com.stuffer.stuffers.commonChat.chat.BottomChatFragment;
import com.stuffer.stuffers.commonChat.chat.ChatActivity;
import com.stuffer.stuffers.commonChat.chatAdapters.MoreAdapter;
import com.stuffer.stuffers.commonChat.chatModel.AttachmentTypes;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.ChatMore;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.interfaces.ChatItemClickListener;
import com.stuffer.stuffers.commonChat.interfaces.MoreListener;
import com.stuffer.stuffers.commonChat.interfaces.ProceedRequest;
import com.stuffer.stuffers.communicator.ShopListener;
import com.stuffer.stuffers.models.shop_model.ShopModel;
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
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity2 extends BaseActivity implements View.OnClickListener, ChatItemClickListener, ProceedRequest, MoreListener, ShopListener {
    private static final String TAG = "HomeActivity";
    private static final int REQUEST_CODE_CHAT_FORWARD = 99;
    private ImageView ivMenu, menu_icon;
    private LinearLayout llChat, llService, llCall;
    private ChatHelper helper;
    User userMe;
    private MyTextViewBold tvUserName;
    private MyTextView tvMobileNumber, tvDrawername, tvDrawerNo, tvVersion;
    private DatabaseReference myInboxRef;
    private ArrayList<Message> messageForwardList = new ArrayList<>();
    private DrawerLayout drawer_layout;
    private LinearLayout llMyQr;

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
    private LinearLayout layoutAccount, layoutProfile, layoutSetting, layoutLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        llMyQr = (LinearLayout) findViewById(R.id.llMyQr);
        layoutLogout = (LinearLayout) findViewById(R.id.layoutLogout);

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


        llChat.setOnClickListener(this);
        llService.setOnClickListener(this);
        llCall.setOnClickListener(this);
        llMyQr.setOnClickListener(this);
        ivMenuBottom.setOnClickListener(this);
        add_attachment.setOnClickListener(this);
        rvShop = findViewById(R.id.rvShop);
        rvShop.setLayoutManager(new LinearLayoutManager(HomeActivity2.this, LinearLayoutManager.HORIZONTAL, false));
        findViewById(R.id.customlayout_create_merchant).setOnClickListener(this);
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
        tvVersion.setText(" App Version : " + BuildConfig.VERSION_NAME);

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
            }
        });
        OSDeviceState status = OneSignal.getDeviceState();
        if (status != null && status.isSubscribed() && status.getUserId() != null) {
            usersRef.child(userMe.getId()).child("userPlayerId").setValue(status.getUserId());
            helper.setMyPlayerId(status.getUserId());
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
                        Intent mIntentQrCode = new Intent(HomeActivity2.this, AccountActivity.class);
                        mIntentQrCode.putExtra(AppoConstants.WHERE, 7);
                        startActivity(mIntentQrCode);
                    }
                }
            }, 200);
        }
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
        openChat(ChatActivity.newIntent(HomeActivity2.this, messageForwardList, chat), userImage);
    }

    private void openChat(Intent intent, View userImage) {
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity2.this, userImage, "userImage");
        startActivityForResult(intent, REQUEST_CODE_CHAT_FORWARD, activityOptionsCompat.toBundle());
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
                    messageForwardList.addAll(temp);
                    /*userSelectDialogFragment = UserSelectDialogFragment.newUserSelectInstance(myUsers);
                    FragmentManager manager = getSupportFragmentManager();
                    Fragment frag = manager.findFragmentByTag(USER_SELECT_TAG);
                    if (frag != null) {
                        manager.beginTransaction().remove(frag).commit();
                    }
                    userSelectDialogFragment.show(manager, USER_SELECT_TAG);*/
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
        //mReqPayload.addProperty("userId", "userId");
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
                            //Log.e(TAG, "onResponse: avatar::" + avatar);
                            drawer_layout.openDrawer(GravityCompat.START);
                            DataVaultManager.getInstance(HomeActivity2.this).saveIdImagePath(avatar);
                            Toast.makeText(HomeActivity2.this, "avatar updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                /*if (response.code() == 200) {
                    Log.e(TAG, "onResponse: " + response);
                    String res = new Gson().toJson(response.body());
                    JSONObject

                } else {
                    Toast.makeText(HomeActivity2.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                }*/

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

        llCashSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);

                if (TextUtils.isEmpty(userData)) {

                    goToLoginScreen(11);
                } else {
                    Intent mIntent = new Intent(HomeActivity2.this, CashSend.class);
                    mIntent.putExtra(AppoConstants.WHERE, 11);
                    startActivity(mIntent);
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
        Log.e(TAG, "onShopItemClick: pos : " + pos);
        Log.e(TAG, "onShopItemClick: title : " + title);
    }
}