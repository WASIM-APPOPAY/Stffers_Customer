package com.stuffer.stuffers.activity.wallet;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.AreaSelectListener;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.fragments.forgot_password.VerifyMobileFragment;
import com.stuffer.stuffers.fragments.transactionpin.TransactionPinFragment;
import com.stuffer.stuffers.fragments.transactionpin.TransactionPinUpdateFragment;
import com.stuffer.stuffers.myService.AppSMSBroadcastReceiver;
import com.stuffer.stuffers.utils.AppoConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionPinActivity extends AppCompatActivity implements FragmentReplaceListener, AreaSelectListener {
    private static final String TAG = "TransactionPinActivity";
    private String oldTransactionPin;
    private IntentFilter intentFilter;
    private AppSMSBroadcastReceiver appSMSBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_pin);
        setupActionBar();
        oldTransactionPin = getIntent().getStringExtra(AppoConstants.INFO);
        if (savedInstanceState == null) {
            //Log.e(TAG, "onCreate: called");
            TransactionPinFragment transactionPinFragment = new TransactionPinFragment();
            initFragments(transactionPinFragment);
        } else {
            //Log.e(TAG, "onCreate: not called");
        }

        smsListener();
        initBroadCast();
        registerReceiver(appSMSBroadcastReceiver, intentFilter);
    }

    @Override
    public void onAreaSelected(int pos) {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (fragmentById instanceof TransactionPinFragment) {
            ((TransactionPinFragment) fragmentById).updateAreaCode(pos);
        }

    }


    public void initFragments(Fragment params) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer, params);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("Transaction Pin");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }

    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
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
    public void onFragmentReplaceClick(Bundle bundle) {
        TransactionPinUpdateFragment transactionPinUpdateFragment = new TransactionPinUpdateFragment();
        bundle.putString(AppoConstants.INFO, oldTransactionPin);
        transactionPinUpdateFragment.setArguments(bundle);
        initFragments(transactionPinUpdateFragment);
    }

    private void initBroadCast() {
        intentFilter = new IntentFilter("com.google.android.gms.auth.api.phone.SMS_RETRIEVED");
        appSMSBroadcastReceiver = new AppSMSBroadcastReceiver();
        appSMSBroadcastReceiver.setOnSmsReceiveListener(new AppSMSBroadcastReceiver.OnSmsReceiveListener() {
            @Override
            public void onReceive(String messageCode) {
                Log.e(TAG, "onReceive: " + messageCode);
                Toast.makeText(TransactionPinActivity.this, messageCode, Toast.LENGTH_SHORT).show();

                Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
                Matcher matcher = otpPattern.matcher(messageCode);
                Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
                if (fragmentById instanceof TransactionPinFragment) {
                    if (matcher.find()) {
                        String group = matcher.group(0);
                        ((TransactionPinFragment) fragmentById).inputOtp(group);
                    }
                }
            }
        });

    }

    private void smsListener() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsRetriever();
    }
}
