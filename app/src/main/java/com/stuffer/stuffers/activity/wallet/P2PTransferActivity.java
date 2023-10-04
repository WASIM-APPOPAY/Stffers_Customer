package com.stuffer.stuffers.activity.wallet;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
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
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.onesignal.OneSignal;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.contact.ContactDemoActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chat.ChatActivity;
import com.stuffer.stuffers.commonChat.chat.TransferChatActivity;
import com.stuffer.stuffers.commonChat.chat.UserSelectDialogFragment;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.interfaces.ChatItemClickListener;
import com.stuffer.stuffers.commonChat.interfaces.UserGroupSelectionDismissListener;
import com.stuffer.stuffers.communicator.MoneyTransferListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemClickListener2;
import com.stuffer.stuffers.communicator.StartActivityListener;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.communicator.UserAccountTransferListener;
import com.stuffer.stuffers.fragments.bottom.BankFragment;
import com.stuffer.stuffers.fragments.quick_pay.WalletTransferFragment2;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class P2PTransferActivity extends AppCompatActivity implements UserAccountTransferListener, StartActivityListener, RecyclerViewRowItemClickListener2, MoneyTransferListener, TransactionPinListener {

    private static final int REQUEST_CODE_CHAT_FORWARD = 99;
    private int mType = 0;
    private String mIndexUser;
    private List<CurrencyResult> mCurrencyResponse;
    private boolean hasAmount = false;
    private String amount = "no", mBaseConversion;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private JSONObject indexUser;
    private AlertDialog mDialog;
    private File mFileSSort;
    private static String CONFIRM_TAG = "confirmtag";
    private static String USER_SELECT_TAG = "userselectdialog";
    private ArrayList<User> myUsers = new ArrayList<>();
    private UserSelectDialogFragment userSelectDialogFragment;
    private ArrayList<Message> messageForwardList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private static final String TAG = "P2PTransferActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2_ptransfer2);
        mainAPIInterface = ApiUtils.getAPIService();
        if (getIntent().getExtras() != null) {
            mType = getIntent().getIntExtra(AppoConstants.WHERE, 0);
        }

        setupActionBar();

        if (savedInstanceState == null) {
            BankFragment mFragment = new BankFragment();
            Bundle mBundle = new Bundle();
            mBundle.putInt(AppoConstants.WHERE, mType);
            mFragment.setArguments(mBundle);
            initFragment(mFragment);
        }

    }

    private void initFragment(Fragment mFragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.transferContainer, mFragment);
        fragmentTransaction.commit();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("P-2-P Transfer");

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
    public void onAccountTransfer(JSONObject reciveruser, List<CurrencyResult> currencyResponse, JSONObject baseConversion) {
        mIndexUser = String.valueOf(reciveruser);
        mCurrencyResponse = currencyResponse;
        mBaseConversion = String.valueOf(baseConversion);
        String fromSymble = Helper.getCurrencySymble();
        try {
            String toCurrency = reciveruser.getString(AppoConstants.TOCURRENCY);
            getConversion(fromSymble, toCurrency);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppoConstants.PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                String mMobileNumber = data.getStringExtra(AppoConstants.INFO);
                //Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("bank");
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.transferContainer);

                if (currentFragment instanceof BankFragment) {
                    ((BankFragment) currentFragment).passPhoneNumber(mMobileNumber);
                }
            }
        }
    }

    @Override
    public void OnStartActivityRequest(int requestCode) {
        Intent intentContact = new Intent(P2PTransferActivity.this, ContactDemoActivity.class);
        startActivityForResult(intentContact, AppoConstants.PICK_CONTACT);
    }

    @Override
    public void onRowItemClick2(int pos) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.transferContainer);
        if (currentFragment instanceof BankFragment) {
            ((BankFragment) currentFragment).getBaseConversion(pos);
        }
    }

    @Override
    public void OnMoneyTransferSuccess() {
        getCurrentUserDetails();
    }

    private void getCurrentUserDetails() {
        dialog = new ProgressDialog(P2PTransferActivity.this);
        dialog.setMessage(getString(R.string.info_getting_profile));
        dialog.show();

        try {
            String userDetails = DataVaultManager.getInstance(P2PTransferActivity.this).getVaultValue(KEY_USER_DETIALS);
            JSONObject mIndex = new JSONObject(userDetails);
            String accessToken = DataVaultManager.getInstance(P2PTransferActivity.this).getVaultValue(KEY_ACCESSTOKEN);

            String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);

            JSONObject mResult = mIndex.getJSONObject(AppoConstants.RESULT);
            String ph = mResult.getString(AppoConstants.MOBILENUMBER);
            String area = mResult.getString(AppoConstants.PHONECODE);
            mainAPIInterface.getProfileDetails(Long.parseLong(ph), Integer.parseInt(area), bearer_).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        JsonObject body = response.body();
                        String res = body.toString();
                        DataVaultManager.getInstance(P2PTransferActivity.this).saveUserDetails(res);
                        loadHome();
                    } else {
                        if (response.code() == 401) {
                            DataVaultManager.getInstance(P2PTransferActivity.this).saveUserDetails("");
                            DataVaultManager.getInstance(P2PTransferActivity.this).saveUserAccessToken("");
                            Intent intent = new Intent(P2PTransferActivity.this, SignInActivity.class);
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

    private void loadHome() {
        //bottomify_nav.setActiveNavigationIndex(0);
        finish();
    }

    @Override
    public void onPinConfirm(String pin) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.transferContainer);
        if (currentFragment instanceof WalletTransferFragment2) {
            ((WalletTransferFragment2) currentFragment).getCommission(pin);
        }
    }

    private void getConversion(String mFrom, String mTo) {
        showLoading(getString(R.string.info_please_wait_dots));

        String url = "https://admin.corecoop.net/api/iConnectMasters/CurrencyForexRateBuying?" + "FromCurrency=" + mFrom + "&" + "ToCurrency=" + mTo;

        String userName = "+919999591757";
        String password = "iConnect@123!";
        String base = userName + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        AndroidNetworking.get(url)
                .addHeaders("Authorization", authHeader)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        /*
            WalletTransferFragment2 walletTransferFragment = new WalletTransferFragment2();
            Bundle bundle = new Bundle();
            bundle.putInt(AppoConstants.WHERE, mType);
            bundle.putString(AppoConstants.SENTUSER, mIndexUser);
            bundle.putBoolean("hasAmount", hasAmount);
            bundle.putString("amount", amount);
            bundle.putParcelableArrayList(AppoConstants.SENTCURRENCY, (ArrayList<? extends Parcelable>) mCurrencyResponse);
            bundle.putString(AppoConstants.SENTBASECONVERSION, mBaseConversion);
            walletTransferFragment.setArguments(bundle);
            initFragment(walletTransferFragment);
                         */
                        try {
                            if (response.getString(AppoConstants.MESSAGE).equalsIgnoreCase(AppoConstants.SUCCESS)) {
                                if (!response.getBoolean(AppoConstants.ERROR)) {
                                    JSONArray currencyForexRate = response.getJSONArray("CurrencyForexRate");
                                    JSONObject jsonObject = currencyForexRate.getJSONObject(0);
                                    String column1 = jsonObject.getString("Column1");
                                    WalletTransferFragment2 walletTransferFragment = new WalletTransferFragment2();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(AppoConstants.WHERE, mType);
                                    bundle.putString(AppoConstants.SENTUSER, mIndexUser);
                                    bundle.putBoolean("hasAmount", hasAmount);
                                    bundle.putString("amount", amount);
                                    bundle.putString("exchange", column1);
                                    bundle.putParcelableArrayList(AppoConstants.SENTCURRENCY, (ArrayList<? extends Parcelable>) mCurrencyResponse);
                                    bundle.putString(AppoConstants.SENTBASECONVERSION, mBaseConversion);
                                    walletTransferFragment.setArguments(bundle);
                                    initFragment(walletTransferFragment);


                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    private void showLoading(String message) {
        mProgressDialog = new ProgressDialog(P2PTransferActivity.this);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void hideLoading() {
        mProgressDialog.dismiss();
    }

    public void sentNotification() {

        /*OneSignal.postNotification(new JSONObject("{'headings': {'en':'" +headings + "'}, 'contents': {'en':'" + message.getBody() + "'}, 'include_player_ids': " + userPlayerIds.toString() + ",'data': " + new Gson().toJson(message) + ",'android_group':" + message.getChatId() + " }"),
                new OneSignal.PostNotificationResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.i("OneSignalExample", "postNotification Success: " + response.toString());
                    }

                    @Override
                    public void onFailure(JSONObject response) {

                    }
                });*/
    }

    //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
}