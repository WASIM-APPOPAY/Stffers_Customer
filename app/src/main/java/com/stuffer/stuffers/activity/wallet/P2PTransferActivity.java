package com.stuffer.stuffers.activity.wallet;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonObject;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.contact.ContactDemoActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class P2PTransferActivity extends AppCompatActivity implements UserAccountTransferListener, StartActivityListener, RecyclerViewRowItemClickListener2, MoneyTransferListener, TransactionPinListener {

    private int mType = 0;
    private String mIndexUser;
    private List<CurrencyResult> mCurrencyResponse;
    private boolean hasAmount = false;
    private String amount = "no", mBaseConversion;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;


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
            ;
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
                        //String res = new Gson().toJson(response.body());
                        ////Log.e(TAG, "onResponse: getprofile :" + res);
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
                    ////Log.e(TAG, "onFailure: " + t.getMessage().toString());
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
    //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
}