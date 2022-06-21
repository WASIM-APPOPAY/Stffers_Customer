package com.stuffer.stuffers.activity.wallet;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.MyContextWrapper;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.FianceTab.DominaActivity;
import com.stuffer.stuffers.activity.FianceTab.UnionPayActivity;
import com.stuffer.stuffers.activity.FianceTab.WalletNewBankActivity;
import com.stuffer.stuffers.activity.contact.ContactDemoActivity;
import com.stuffer.stuffers.activity.linkbank.LinkBankAccountActivity;
import com.stuffer.stuffers.activity.lunex_card.LunexGiftActivity;
import com.stuffer.stuffers.activity.payment.GasPaymentActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.asyntask.NotificationHelper;
import com.stuffer.stuffers.asyntask.RegisterDevice;
import com.stuffer.stuffers.communicator.AreaSelectListener;
import com.stuffer.stuffers.communicator.FinanceListener;
import com.stuffer.stuffers.communicator.InnerScanListener;
import com.stuffer.stuffers.communicator.LinkAccountListener;
import com.stuffer.stuffers.communicator.LoginRequestListener;
import com.stuffer.stuffers.communicator.MoneyTransferListener;
import com.stuffer.stuffers.communicator.OnTransactionPinSuccess;
import com.stuffer.stuffers.communicator.ProfileUpdateRequest;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemClickListener2;
import com.stuffer.stuffers.communicator.ScanRequestListener;
import com.stuffer.stuffers.communicator.ScreenTimeoutListener;
import com.stuffer.stuffers.communicator.SideWalletListener;
import com.stuffer.stuffers.communicator.StartActivityListener;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.communicator.UnionPayCardListener;
import com.stuffer.stuffers.communicator.UpdateProfileRequest;
import com.stuffer.stuffers.communicator.UserAccountTransferListener;
import com.stuffer.stuffers.fragments.bottom.BankFragment;
import com.stuffer.stuffers.fragments.bottom.HomeFragment;
import com.stuffer.stuffers.fragments.bottom.MallFragment;
import com.stuffer.stuffers.fragments.bottom.ScanAppopayFragment;
import com.stuffer.stuffers.fragments.bottom.ScanFragment;
import com.stuffer.stuffers.fragments.bottom.chat.ChatFragment;
import com.stuffer.stuffers.fragments.bottom.chat.ChatTabFragment;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomAccountCreated;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomLinkAccount;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomNotCard;
import com.stuffer.stuffers.fragments.dialog.ErrorDialogFragment;
import com.stuffer.stuffers.fragments.quick_pay.WalletTransferFragment2;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.views.MyTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.volcaniccoder.bottomify.BottomifyNavigationView;
import com.volcaniccoder.bottomify.OnNavigationItemChangeListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_FIREBASE_TOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_MOBILE;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_NAME;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_ID1;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_LANGUAGE;


public class HomeActivity extends AppCompatActivity implements OnNavigationItemChangeListener, LoginRequestListener, ProfileUpdateRequest, StartActivityListener, ScanRequestListener, SideWalletListener, ScreenTimeoutListener, UserAccountTransferListener, RecyclerViewRowItemClickListener2, MoneyTransferListener, TransactionPinListener, RecyclerViewRowItemCLickListener, UpdateProfileRequest, OnTransactionPinSuccess, UnionPayCardListener, InnerScanListener, LinkAccountListener, AreaSelectListener, FinanceListener {
    BottomifyNavigationView bottomify_nav;
    private DrawerLayout drawer_layout;
    ExpandableLayout layoutExpand;
    private LinearLayout layoutAccount, layoutProfile, layoutLogout, layoutMall, layoutSetting, layoutOverview, layoutMyCards, layoutTerminal;
    MyTextView tvUserName, tvPhone;
    ImageView notification_video, notification_call, notification_icon, menu_icon;
    private static final String TAG = "HomeActivity";
    private boolean allowParam = false, hasAmount = false, mSlideState = false;
    private MyTextView tvSideBalance;
    FrameLayout frameNotification;
    MyTextView tvTotalNoti;
    private AlertDialog dialogScreenLock;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String userIds, mIndexUser, mBaseConversion, amount = "no", strUserName, strUserMobile;
    private List<CurrencyResult> mCurrencyResponse;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private LinearLayout layoutLinkBank;
    private MyTextView tvVersion;
    private BottomNotCard mBottomNotCard;
    private BottomLinkAccount mBottomLinkAc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainAPIInterface = ApiUtils.getAPIService();
        tvUserName = findViewById(R.id.tvUserName);
        tvPhone = findViewById(R.id.tvPhone);
        tvSideBalance = findViewById(R.id.tvSideBalance);
        frameNotification = findViewById(R.id.frameNotification);
        tvTotalNoti = findViewById(R.id.tvTotalNoti);
        layoutTerminal = (LinearLayout) findViewById(R.id.layoutTerminal);
        layoutExpand = findViewById(R.id.layoutExpand);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        LinearLayout lSend = findViewById(R.id.lSend);
        layoutMyCards = findViewById(R.id.layoutMyCards);
        menu_icon = (ImageView) findViewById(R.id.menu_icon);
        layoutAccount = (LinearLayout) findViewById(R.id.layoutAccount);
        layoutProfile = (LinearLayout) findViewById(R.id.layoutProfile);
        layoutLogout = (LinearLayout) findViewById(R.id.layoutLogout);
        layoutMall = (LinearLayout) findViewById(R.id.layoutMall);
        layoutSetting = (LinearLayout) findViewById(R.id.layoutSetting);
        layoutOverview = (LinearLayout) findViewById(R.id.layoutOverview);
        layoutLinkBank = findViewById(R.id.layoutLinkBank);
        tvVersion = (MyTextView) findViewById(R.id.tvVersion);
        LinearLayout layoutKyc = (LinearLayout) findViewById(R.id.layoutKyc);
        ((AppoPayApplication) getApplication()).registerSessionListener(this);
        layoutKyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.START);
                /*Intent intent = new Intent(HomeActivity.this, KycActivity.class);
                startActivity(intent);*/
            }
        });

        notification_video = findViewById(R.id.notification_video);
        notification_call = findViewById(R.id.notification_call);
        notification_icon = findViewById(R.id.notification_icon);
        strUserName = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_NAME);
        strUserMobile = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_MOBILE);

        hideItem();
        updateUserDetails();

        bottomify_nav = (BottomifyNavigationView) findViewById(R.id.bottomify_nav);
        bottomify_nav.setOnNavigationItemChangedListener(this);
        bottomify_nav.setActiveNavigationIndex(0);


        layoutMyCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intentCards = new Intent(HomeActivity.this, MyCardsActivity.class);
                        startActivity(intentCards);
                    }
                }, 250);

            }
        });

        layoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                        if (StringUtils.isEmpty(vaultValue)) {
                            ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(AppoConstants.INFO, getString(R.string.account_see_error));
                            errorDialogFragment.setArguments(bundle);
                            errorDialogFragment.setCancelable(false);
                            errorDialogFragment.show(getSupportFragmentManager(), errorDialogFragment.getTag());
                        } else {
                            Intent intentAccount = new Intent(HomeActivity.this, AccountActivity.class);
                            startActivity(intentAccount);
                        }
                    }
                }, 250);
            }
        });

        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutClick();
            }
        });

        layoutMall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomify_nav.setActiveNavigationIndex(1);
                        Fragment fragment2 = new MallFragment();
                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                        fragmentTransaction2.addToBackStack(null);
                        fragmentTransaction2.replace(R.id.mainContainer, fragment2);
                        fragmentTransaction2.commit();

                    }
                }, 250);

            }
        });

        layoutSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verifyUser();


                    }
                }, 250);
            }
        });

        layoutOverview.setVisibility(View.GONE);
        layoutOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intentOverview = new Intent(HomeActivity.this, OverviewActivity.class);
                        startActivity(intentOverview);

                    }
                }, 250);
            }
        });


        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
                drawer_layout.requestLayout();
                drawer_layout.bringToFront();
            }
        });

        layoutTerminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutExpand.setExpanded(true);
            }
        });


        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent profileIntent = new Intent(HomeActivity.this, CustomerProfileActivity.class);
                        startActivity(profileIntent);
                    }
                }, 200);
            }
        });

        //readCountNotifications();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {

            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        } else {
            ////Log.e(TAG, "onCreate: user id not exists");

        }


        layoutLinkBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.START);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(HomeActivity.this, LinkBankAccountActivity.class);
                        startActivity(intent);
                    }
                }, 200);
            }
        });

        frameNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNotification = new Intent(HomeActivity.this, notificationActivity.class);
                startActivity(intentNotification);
            }
        });
        try {
            PackageInfo pInfo = HomeActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersion.setText("App Version : " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void verifyUser() {
        if (AppoPayApplication.isNetworkAvailable(HomeActivity.this)) {
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

                    if (!result.getString(AppoConstants.TRANSACTIONPIN).isEmpty() || !result.getString(AppoConstants.TRANSACTIONPIN).equalsIgnoreCase("null")) {
                        Intent intentSetting = new Intent(HomeActivity.this, SettingActvity.class);
                        startActivity(intentSetting);
                    }
                    /*if (customerDetails.getString(AppoConstants.COUNTRYID).isEmpty() || customerDetails.getString(AppoConstants.COUNTRYID).equalsIgnoreCase("0")) {
                        ProfileErrorDialogFragment fragment = new ProfileErrorDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(AppoConstants.INFO, getString(R.string.profile_update_error1));
                        fragment.setArguments(bundle);
                        fragment.setCancelable(false);
                        fragment.show(getSupportFragmentManager(), fragment.getTag());
                    } else {
                        Intent intentSetting = new Intent(HomeActivity.this, SettingActvity.class);
                        startActivity(intentSetting);
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(HomeActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserDetails() {
        String keyUserDetails = DataVaultManager.getInstance(HomeActivity.this).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
        if (StringUtils.isEmpty(keyUserDetails)) {

        } else {
            JSONObject index = null;
            try {
                index = new JSONObject(keyUserDetails);
                JSONObject result = index.getJSONObject(AppoConstants.RESULT);
                tvUserName.setText(result.getString(AppoConstants.FIRSTNAME) + " " + result.getString(AppoConstants.LASTNAME));
                tvPhone.setText(result.getString(AppoConstants.MOBILENUMBER));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void hideItem() {
        notification_call.setVisibility(View.GONE);
        notification_video.setVisibility(View.GONE);
        frameNotification.setVisibility(View.VISIBLE);
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

    private void logoutCalled() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyAlertDialogStyle);
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


    @Override
    public void onResume() {
        super.onResume();
        setupActionBar();
        updateWallet();

    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    public void onBackPressed() {
        try {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
            if (currentFragment instanceof HomeFragment) {
                Log.d("message", "home fragment");
                finish();
            } else {
                Log.d("message", "popping backstack");
                /*getSupportFragmentManager().popBackStack();
                getSupportActionBar().show();*/
                bottomify_nav.setActiveNavigationIndex(0);

            }
        } catch (Exception e) {


        }

    }


    private void logoutUserRequest() {
        try {
            FirebaseAuth.getInstance().signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DataVaultManager.getInstance(HomeActivity.this).saveUserAccessToken("");
        DataVaultManager.getInstance(HomeActivity.this).saveUserDetails("");
        DataVaultManager.getInstance(HomeActivity.this).saveCardToken("");

        Intent i = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(i);
        finish();


    }

    @Override
    public void onNavigationItemChanged(@NotNull BottomifyNavigationView.NavigationItem navigationItem) {

        switch (navigationItem.getPosition()) {

            case 0:
                hideItem();
                Fragment fragment = new HomeFragment();
                if (allowParam) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AppoConstants.INFO, "1");
                    fragment.setArguments(bundle);
                    allowParam = false;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(AppoConstants.INFO, "0");
                    fragment.setArguments(bundle);
                }
                //  Log.e(TAG, "onNavigationItemChanged: called :: " + 1);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.mainContainer, fragment);
                fragmentTransaction.commit();
                break;

            case 1:
                hideItem();
                Fragment fragment2 = new MallFragment();

                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.replace(R.id.mainContainer, fragment2);
                fragmentTransaction2.commit();
                break;

            case 2:
                hideItem();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (isPermissionGranted()) {
                        Fragment fragment3 = new ScanFragment();
                        FragmentManager fragmentManager3 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                        fragmentTransaction3.addToBackStack(null);
                        fragmentTransaction3.replace(R.id.mainContainer, fragment3);
                        fragmentTransaction3.commit();
                    } else {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, AppoConstants.CAMERA_REQUEST_CODE);
                    }

                } else {
                    Fragment fragment3 = new ScanFragment();
                    FragmentManager fragmentManager3 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.replace(R.id.mainContainer, fragment3);
                    fragmentTransaction3.commit();
                }
                break;

           /* case 3:
                hideItem();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (isPermissionGranted()) {
                        Fragment fragment3 = new ScanAppopayFragment();
                        FragmentManager fragmentManager3 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                        fragmentTransaction3.addToBackStack(null);
                        fragmentTransaction3.replace(R.id.mainContainer, fragment3);
                        fragmentTransaction3.commit();
                    } else {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, AppoConstants.INNER_CAMERA_REQUEST_CODE);
                    }

                } else {
                    Fragment fragment3 = new ScanAppopayFragment();
                    FragmentManager fragmentManager3 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.replace(R.id.mainContainer, fragment3);
                    fragmentTransaction3.commit();
                }
                break;*/

            case 3:
                hideItem();
                BankFragment fragment4 = new BankFragment();
                FragmentManager fragmentManager4 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                fragmentTransaction4.addToBackStack(null);
                fragmentTransaction4.replace(R.id.mainContainer, fragment4, "bank");
                fragmentTransaction4.commit();
                break;

            case 4:
                visibleItem();
                Fragment fragment5 = new ChatTabFragment();
                FragmentManager fragmentManager5 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction5 = fragmentManager5.beginTransaction();
                fragmentTransaction5.addToBackStack(null);
                fragmentTransaction5.replace(R.id.mainContainer, fragment5);
                fragmentTransaction5.commit();
                break;
        }
    }

    private void visibleItem() {
        notification_call.setVisibility(View.GONE);
        notification_video.setVisibility(View.GONE);
        frameNotification.setVisibility(View.GONE);
    }

    private boolean isPermissionGranted() {
        boolean cameraPermission = ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        //int writePerPermission = ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //int readPermission = ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //return cameraPermission && writePerPermission && readPermission;
        return cameraPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppoConstants.CAMERA_REQUEST_CODE) {
            if (isPermissionGranted()) {
                Fragment fragment3 = new ScanFragment();
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                fragmentTransaction3.addToBackStack(null);
                fragmentTransaction3.replace(R.id.mainContainer, fragment3);
                fragmentTransaction3.commit();
            } else {
                Toast.makeText(this, "permission denied by user", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AppoConstants.INNER_CAMERA_REQUEST_CODE) {
            if (isPermissionGranted()) {
                Fragment fragment4 = new ScanAppopayFragment();
                FragmentManager fragmentManager4 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                fragmentTransaction4.addToBackStack(null);
                fragmentTransaction4.replace(R.id.mainContainer, fragment4);
                fragmentTransaction4.commit();
            } else {
                Toast.makeText(this, "permission denied by user", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onLoginRequest() {
        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onProfileUpdate() {
        Intent intent = new Intent(HomeActivity.this, CustomerProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e(TAG, "onActivityResult: called");
        if (requestCode == AppoConstants.QR_SCAN_CODE_REQUEST) {
            // Log.e(TAG, "onActivityResult: if called");
            if (resultCode == Activity.RESULT_OK) {
                allowParam = true;
            } else {
                allowParam = false;
            }
            bottomify_nav.setActiveNavigationIndex(0);
        } else if (requestCode == AppoConstants.WALLET_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                updateAccountBalance();
            }
        } else if (requestCode == AppoConstants.RECHARGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                updateAccountBalance();
            }
        } else if (requestCode == ChatFragment.REQUEST_IMAGE) {
            updateChatFragment(requestCode, resultCode, data);
        } else if (requestCode == AppoConstants.PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                String mMobileNumber = data.getStringExtra(AppoConstants.INFO);
                Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("bank");
                if (currentFragment instanceof BankFragment) {
                    ((BankFragment) currentFragment).passPhoneNumber(mMobileNumber);
                }
            }
        } else if (requestCode == AppoConstants.PROFILE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String mUserName = data.getStringExtra(AppoConstants.USERNAME);
                String mEmail = data.getStringExtra(AppoConstants.EMIAL);
                String mMobileNo = data.getStringExtra(AppoConstants.MOBILENUMBER);
                String mDob = data.getStringExtra(AppoConstants.DOB);
                int mCountryId = data.getIntExtra(AppoConstants.COUNTRYID, 0);
                int mStateId = data.getIntExtra(AppoConstants.STATEID, 0);
                String mCityName = data.getStringExtra(AppoConstants.CITYNAME);
                String mTransPin = data.getStringExtra(AppoConstants.TRANSACTIONPIN);
                String mScreenPin = data.getStringExtra(AppoConstants.SCREENLOCKPIN);
                String mAddress = data.getStringExtra(AppoConstants.ADDRESS);
                String mZipCode = data.getStringExtra(AppoConstants.ZIPCODE2);
                Fragment currentFragment = getSupportFragmentManager().getFragments().get(0);
                if (currentFragment instanceof HomeFragment) {
                    // ((HomeFragment) currentFragment).updateUserProfile(mUserName,mEmail,mMobileNo,mDob,mCountryId,mStateId,mCityName,mTransPin,mScreenPin,mAddress,mZipCode);
                } else {
                    //Log.e(TAG, "onFromDateSelected: Something went wrong");
                }

            } else {

            }

        } else if (requestCode == 1003) {

            if (resultCode == Activity.RESULT_OK) {
                showNoCardDialog();
            }

        } else if (requestCode == 1004) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intentTrans = new Intent(HomeActivity.this, AccountActivity.class);
                startActivity(intentTrans);
            }
        } else if (requestCode == 1009) {

            if (resultCode == Activity.RESULT_OK) {
                Intent intentFianceNew = new Intent(HomeActivity.this, WalletNewBankActivity.class);
                startActivityForResult(intentFianceNew, 1003);
            }
        } else {
            // Log.e(TAG, "onActivityResult: else bottom called" );
            bottomify_nav.setActiveNavigationIndex(0);
            allowParam = false;
        }
    }


    private void updateChatFragment(int requestCode, int resultCode, Intent data) {
        Fragment currentFragment = getSupportFragmentManager().getFragments().get(0);
        if (currentFragment instanceof ChatFragment) {
            ((ChatFragment) currentFragment).onActivityResult(requestCode, resultCode, data);
        } else {
            ////Log.e(TAG, "onFromDateSelected: Something went wrong");
        }
    }


    public void updateAccountBalance() {
        Fragment currentFragment = getSupportFragmentManager().getFragments().get(0);
        if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).updateWalletBalance();
        } else {
            //  Log.e(TAG, "onFromDateSelected: Something went wrong");
        }
    }

    @Override
    public void OnStartActivityRequest(int requestCode) {
        if (requestCode == AppoConstants.WALLET_REQUEST_CODE) {
            Intent intent = new Intent(HomeActivity.this, QuickPaymentActivity.class);
            startActivityForResult(intent, requestCode);
        } else if (requestCode == AppoConstants.RECHARGE_REQUEST_CODE) {
            Intent intent = new Intent(HomeActivity.this, MobileRechargeActivity.class);
            startActivityForResult(intent, requestCode);
        } else if (requestCode == AppoConstants.WALLET_CASH_OUT) {
            Intent intent = new Intent(HomeActivity.this, CashOutActivity.class);
            //startActivityForResult(intent,AppoConstants.WALLET_CASH_OUT);;
            startActivity(intent);
        } else if (requestCode == AppoConstants.GAS_PAY_REQUEST) {
            Intent intent = new Intent(HomeActivity.this, GasPaymentActivity.class);
            //startActivityForResult(intent,AppoConstants.GAS_PAY_REQUEST);
            startActivity(intent);
        } else if (requestCode == AppoConstants.PICK_CONTACT) {
            Intent intentContact = new Intent(HomeActivity.this, ContactDemoActivity.class);
            startActivityForResult(intentContact, AppoConstants.PICK_CONTACT);
        }
    }

    @Override
    public void onFinanceRequest(int code){
       if (code==-1) {
           Intent intent = new Intent(HomeActivity.this, MobileRechargeActivity.class);
           startActivityForResult(intent, AppoConstants.RECHARGE_REQUEST_CODE);
       }
       else {
           bottomify_nav.setActiveNavigationIndex(code);
       }



    }




    //for merchant pay
    @Override
    public void onRequestListener(String param) {
        Intent payUserActivity = new Intent(HomeActivity.this, PayUserActivity.class);
        payUserActivity.putExtra(AppoConstants.MERCHANTSCANCODE, param);
        startActivityForResult(payUserActivity, AppoConstants.QR_SCAN_CODE_REQUEST);
    }

    @Override
    public void onInnerRequestListener(String param) {
        Intent payUserActivity = new Intent(HomeActivity.this, InnerPayActivity.class);
        payUserActivity.putExtra(AppoConstants.MERCHANTSCANCODE, param);
        startActivityForResult(payUserActivity, AppoConstants.QR_SCAN_CODE_REQUEST);
    }


    @Override
    public void onSideBalanceRequestUpdate(String param) {
        ////Log.e(TAG, "onSideBalanceRequestUpdate: called");
        try {
            DecimalFormat df2 = new DecimalFormat("#.00");
            Double doubleV = Double.parseDouble(param);
            String format = df2.format(doubleV);
            tvSideBalance.setText("$0");
            tvSideBalance.setText("$" + format);
        } catch (Exception e) {
            tvSideBalance.setText("$0");
            tvSideBalance.setText("$" + param);
        }
        readCountNotifications();


    }

    private void readCountNotifications() {

        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (StringUtils.isEmpty(vaultValue)) {
            ////Log.e(TAG, "readCountNotifications: called");
            DataVaultManager.getInstance(HomeActivity.this).saveUserDetails("");
            DataVaultManager.getInstance(HomeActivity.this).saveUserAccessToken("");
            return;
        }
        JSONObject jsonUser = null;
        try {
            jsonUser = new JSONObject(vaultValue);
            JSONObject objResult = jsonUser.getJSONObject(AppoConstants.RESULT);
            userIds = objResult.getString(AppoConstants.ID);
            String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
            String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
            //String url = Constants.APPOPAY_BASE_URL + "api/wallet/notifications?access_token=" + accesstoken;
            String url = Constants.APPOPAY_BASE_URL + "api/wallet/notifications";
            JSONObject param = new JSONObject();
            param.put(AppoConstants.USERID, userIds);

            NotificationHelper notificationHelper = new NotificationHelper(url, bearer_, param, new NotificationHelper.NoticeReadListener() {
                @Override
                public void noOfUnRead(JSONObject response, String countNotification) {
                    //  Log.e(TAG, "noOfUnRead: COUNT :: " + countNotification);
                    DataVaultManager.getInstance(HomeActivity.this).saveNotificationCount(countNotification);
                    tvTotalNoti.setText(countNotification);
                    updateDevice();
                }

                @Override
                public void onErrorOccur(String error) {
                    //  Log.e(TAG, "onErrorOccur: CODE :: " + error);
                    if (error.equals("401")) {
                        DataVaultManager.getInstance(HomeActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(HomeActivity.this).saveUserAccessToken("");
                        ((AppoPayApplication) getApplication()).cancelTimer();
                        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });
            notificationHelper.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateDevice() {
        String deviceId = TimeUtils.getDeviceId();
        JSONObject mJSON = new JSONObject();
        String mUserId = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_ID1);
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_FIREBASE_TOKEN);
        try {
            mJSON.put("user_id", mUserId);
            mJSON.put("fcm_token", vaultValue);
            mJSON.put("device_id", TimeUtils.getDeviceId());
            String phoneCode = Helper.getPhoneCode();
            Long senderMobileNumber = Helper.getSenderMobileNumber();
            mJSON.put("mobile_number", phoneCode + senderMobileNumber);
            //Log.e(TAG, "updateDevice: called"+mJSON);
            //RegisterDevice mRegister = new RegisterDevice("https://labapi.appopay.com/api/users/registerdevice", mJSON);
            RegisterDevice mRegister = new RegisterDevice("https://prodapi.appopay.com/api/users/registerdevice", mJSON);
            mRegister.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTimeoutConfirm() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlertDialog();
            }
        });

    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogThemeFullScreen);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_screen_lock, null, false);
        builder.setView(view);
        dialogScreenLock = builder.create();
        dialogScreenLock.show();


    }

    @Override
    protected void onDestroy() {
        ((AppoPayApplication) getApplication()).cancelTimer();
        if (dialogScreenLock != null) {
            if (dialogScreenLock.isShowing()) {
                dialogScreenLock.dismiss();
            }

        }
        super.onDestroy();

    }

    public void status(String status) {
        if (firebaseUser != null) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", status);
            reference.updateChildren(hashMap);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    public void onAccountTransfer(JSONObject reciveruser, List<CurrencyResult> currencyResponse, JSONObject baseConversion) {
        mIndexUser = String.valueOf(reciveruser);
        mCurrencyResponse = currencyResponse;
        mBaseConversion = String.valueOf(baseConversion);
        WalletTransferFragment2 walletTransferFragment = new WalletTransferFragment2();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.SENTUSER, mIndexUser);
        bundle.putBoolean("hasAmount", hasAmount);
        bundle.putString("amount", amount);

        bundle.putParcelableArrayList(AppoConstants.SENTCURRENCY, (ArrayList<? extends Parcelable>) mCurrencyResponse);
        bundle.putString(AppoConstants.SENTBASECONVERSION, mBaseConversion);
        walletTransferFragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer, walletTransferFragment);

        transaction.commit();

    }

    @Override
    public void onRowItemClick2(int pos) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (currentFragment instanceof BankFragment) {
            ((BankFragment) currentFragment).getBaseConversion(pos);
        }
    }

    @Override
    public void OnMoneyTransferSuccess() {
        getCurrentUserDetails();
    }

    private void getCurrentUserDetails() {
        dialog = new ProgressDialog(HomeActivity.this);
        dialog.setMessage(getString(R.string.info_getting_profile));
        dialog.show();

        try {
            String userDetails = DataVaultManager.getInstance(HomeActivity.this).getVaultValue(KEY_USER_DETIALS);
            JSONObject mIndex = new JSONObject(userDetails);
            String accessToken = DataVaultManager.getInstance(HomeActivity.this).getVaultValue(KEY_ACCESSTOKEN);

            String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);

            JSONObject mResult = mIndex.getJSONObject(AppoConstants.RESULT);
            String ph = mResult.getString(AppoConstants.MOBILENUMBER);
            String area = mResult.getString(AppoConstants.PHONECODE);
            mainAPIInterface.getProfileDetails(Long.parseLong(ph), Integer.parseInt(area), bearer_).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        //String res = new Gson().toJson(response.body());
                        ////Log.e(TAG, "onResponse: getprofile :" + res);
                        JsonObject body = response.body();
                        String res = body.toString();
                        DataVaultManager.getInstance(HomeActivity.this).saveUserDetails(res);
                        loadHome();
                    } else {
                        if (response.code() == 401) {
                            DataVaultManager.getInstance(HomeActivity.this).saveUserDetails("");
                            DataVaultManager.getInstance(HomeActivity.this).saveUserAccessToken("");
                            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dialog.dismiss();
                    ////Log.e(TAG, "onFailure: " + t.getMessage().toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void loadHome() {
        bottomify_nav.setActiveNavigationIndex(0);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //fetch from shared preference also save to the same when applying. default is English
        //String language = MyPreferenceUtil.getInstance().getString(MyConstants.PARAM_LANGUAGE, "en");
        String userLanguage = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_LANGUAGE);
        if (StringUtils.isEmpty(userLanguage)) {
            userLanguage = "en";
        }
        super.attachBaseContext(MyContextWrapper.wrap(newBase, userLanguage));
    }

    public void updateWallet() {
        if (AppoPayApplication.UPDATE_WALLET) {
            AppoPayApplication.UPDATE_WALLET = false;
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
            if (currentFragment instanceof HomeFragment) {
                Log.d("message", "home fragment");
                ((HomeFragment) currentFragment).updateWalletBalance();

            }
        }

    }

    @Override
    public void onPinConfirm(String pin) {
        //Log.e(TAG, "onPinConfirm: "+pin );
        //String transactionPin = Helper.getTransactionPin();
        //Log.e(TAG, "onPinConfirm: "+transactionPin );
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (currentFragment instanceof WalletTransferFragment2) {
            ((WalletTransferFragment2) currentFragment).getCommission(pin);
        } else if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).dismissBottomPin(pin);
        }
    }

    @Override
    public void onRowItemClick(int pos) {

        /*Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).showBottomSonyPlayStation();
        }*/

        Log.e(TAG, "onRowItemClick: called.....");
        Intent intent = new Intent(HomeActivity.this, LunexGiftActivity.class);
        startActivity(intent);


    }

    @Override
    public void onCompleteProfileRequest() {

        if (AppoPayApplication.isNetworkAvailable(HomeActivity.this)) {
            Intent intentUpdate = new Intent(HomeActivity.this, UpdateProfileActivity.class);
            startActivityForResult(intentUpdate, AppoConstants.PROFILE_REQUEST);
        } else {
            Toast.makeText(HomeActivity.this, "" + getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onPinCreated() {
        //Log.e(TAG, "onPinCreated: called" );
        /*Intent intentFinance = new Intent(HomeActivity.this, FinanceActivity.class);
        startActivityForResult(intentFinance, 1003);*/


        //below temporary disable
        /*Intent intentFianceNew = new Intent(HomeActivity.this, WalletNewBankActivity.class);
        startActivityForResult(intentFianceNew, 1003);*/
        //showLinkAccountDialog();

        BottomAccountCreated mBottomAcCreated = new BottomAccountCreated();
        mBottomAcCreated.show(getSupportFragmentManager(), mBottomAcCreated.getTag());
        mBottomAcCreated.setCancelable(false);


    }

    private void showLinkAccountDialog() {
        mBottomLinkAc = new BottomLinkAccount();
        mBottomLinkAc.show(getSupportFragmentManager(), mBottomLinkAc.getTag());
        mBottomLinkAc.setCancelable(false);
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
        Intent intentUnion = new Intent(HomeActivity.this, UnionPayActivity.class);
        startActivityForResult(intentUnion, 1004);
    }

    @Override
    public void onLinkAccountConfirm() {
        if (mBottomLinkAc != null)
            mBottomLinkAc.dismiss();

        Intent intentDominica = new Intent(HomeActivity.this, DominaActivity.class);
        startActivityForResult(intentDominica, 1009);
    }


    @Override
    public void onAreaSelected(int pos) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (currentFragment instanceof BankFragment){
            ((BankFragment)currentFragment).updateAreaCode(pos);
        }

    }
}
