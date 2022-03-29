package com.stuffer.stuffers.activity.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.ConfirmSelectListener;
import com.stuffer.stuffers.communicator.MoneyTransferListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemClickListener2;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.communicator.UserAccountTransferListener;
import com.stuffer.stuffers.fragments.quick_pay.CustomerPayFragment;
import com.stuffer.stuffers.fragments.quick_pay.WalletTransferFragment;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;

public class QuickPaymentActivity extends AppCompatActivity implements ConfirmSelectListener, UserAccountTransferListener, RecyclerViewRowItemCLickListener, RecyclerViewRowItemClickListener2, MoneyTransferListener, TransactionPinListener {
    private static final String TAG = "QuickPaymentActivity";
    private TextView toolbarTitle;
    private String mIndexUser;
    private List<CurrencyResult> mCurrencyResponse;
    private String mBaseConversion;
    private String tag;
    private int fromPosition;
    private String amount = "";
    private boolean hasAmount = false;
    private PhoneNumberUtil phoneUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_payment);
        Log.e(TAG, "onCreate: called::: ");
        setupActionBar();
        if (getIntent().getExtras() != null) {
            amount = getIntent().getStringExtra(AppoConstants.AMOUNT);
            hasAmount = true;
        } else {
            amount = "no";
        }
        if (savedInstanceState == null) {
            CustomerPayFragment customerPayFragment = new CustomerPayFragment();
            tag = "current";
            intiFragments(customerPayFragment);
        }

    }

    private void intiFragments(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onAccountTransfer(JSONObject reciveruser, List<CurrencyResult> currencyResponse, JSONObject baseConversion) {
        mIndexUser = String.valueOf(reciveruser);
        mCurrencyResponse = currencyResponse;
        mBaseConversion = String.valueOf(baseConversion);
        tag = "wallet";
        WalletTransferFragment walletTransferFragment = new WalletTransferFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.SENTUSER, mIndexUser);
        bundle.putBoolean("hasAmount", hasAmount);
        bundle.putString("amount", amount);


        bundle.putParcelableArrayList(AppoConstants.SENTCURRENCY, (ArrayList<? extends Parcelable>) mCurrencyResponse);
        bundle.putString(AppoConstants.SENTBASECONVERSION, mBaseConversion);
        walletTransferFragment.setArguments(bundle);
        intiFragments(walletTransferFragment);

    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

        finish();
        /*int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            super.onBackPressed();
            finish();
        } else if (backStackEntryCount == 2) {
            String toolbarTitle = "e-TimePayTrack" + "<br>" + "<small><i>" + " OD History" + "</i></small>";
            // tvHeader.setText(Html.fromHtml(toolbarTitle));
            getSupportFragmentManager().popBackStackImmediate();
        }*/

        /*Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
            super.onBackPressed();
        }*/
    }

    @Override
    public void onRowItemClick(int pos) {
        fromPosition = pos;

    }


    @Override
    public void onConfirmSelect() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment instanceof WalletTransferFragment) {
            ((WalletTransferFragment) currentFragment).setFromAccount(fromPosition);
        }

    }

    @Override
    public void onRowItemClick2(int pos) {
        //Log.e("TAG", "onRowItemClick: " + pos);
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment instanceof CustomerPayFragment) {
            ((CustomerPayFragment) currentFragment).getBaseConversion(pos);
        }
    }

    @Override
    public void OnMoneyTransferSuccess() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPinConfirm(String pin) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment instanceof WalletTransferFragment) {
            ((WalletTransferFragment) currentFragment).callCommission(pin);
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppoConstants.PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                //Log.e(TAG, "onActivityResult: Pick Contact NUmber :: " + data.getStringExtra(AppoConstants.INFO));
                String mMobileNumber = data.getStringExtra(AppoConstants.INFO);
                //edtphone_number.setText(mMobileNumber);
                try {
                    // phone must begin with '+'
                    if (phoneUtil == null) {
                        phoneUtil = PhoneNumberUtil.createInstance(QuickPaymentActivity.this);
                    }
                    Phonenumber.PhoneNumber numberProto = phoneUtil.parse(mMobileNumber, "");
                    int countryCode = numberProto.getCountryCode();
                    long nationalNumber = numberProto.getNationalNumber();
                    //edtphone_number.setText(String.valueOf(nationalNumber));
                    //Log.e("code", "code " + countryCode);
                    //Log.e("code", "national number " + nationalNumber);
                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
                }
            }
        }
    }*/
}
