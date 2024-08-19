package com.stuffer.stuffers.activity.forgopassword;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.AreaSelectListener;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.fragments.forgot_password.ForgotPasswordFragment;
import com.stuffer.stuffers.fragments.forgot_password.VerifyMobileFragment;
import com.stuffer.stuffers.fragments.transactionpin.TransactionPinUpdateFragment;
import com.stuffer.stuffers.myService.AppSMSBroadcastReceiver;
import com.stuffer.stuffers.utils.AppSignatureHelper;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActvivity extends AppCompatActivity implements FragmentReplaceListener, AreaSelectListener {

    private IntentFilter intentFilter;
    private AppSMSBroadcastReceiver appSMSBroadcastReceiver;
    private static final String TAG = "ForgotPasswordActvivity";
    private TextView toolbarTitle;
    boolean isReset = false;
    private MyTextViewBold common_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_actvivity);
        setupActionBar();
        if (getIntent().hasExtra("expire")) {

            common_toolbar_title.setText("Reset Password");
            isReset = true;
        }

        if (savedInstanceState == null) {
            //  VerifyMobileFragment verifyMobileFragment = new VerifyMobileFragment();
            //initFragments(verifyMobileFragment);
            VerifyForgotFragment mVerifyForgotFragment = new VerifyForgotFragment();
            initFragments(mVerifyForgotFragment);
          /*ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
            Bundle bundle = new Bundle();
            bundle.putString(AppoConstants.PHONECODE, "91");
            bundle.putString(AppoConstants.MOBILENUMBER, "9836683269");
            bundle.putString(AppoConstants.PHWITHCODE, "919836683269");
            forgotPasswordFragment.setArguments(bundle);
            initFragments(forgotPasswordFragment);*/
        }
        /*AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        ArrayList<String> appSignatures = appSignatureHelper.getAppSignatures();
        String s = appSignatures.get(0);
        Log.e(TAG, "onCreate: " + s);*/

        initBroadCast();
        smsListener();
        registerReceiver(appSMSBroadcastReceiver, intentFilter);


    }


    @Override
    public void onAreaSelected(int pos) {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (fragmentById instanceof VerifyMobileFragment) {
            ((VerifyMobileFragment) fragmentById).updateAreaCode(pos);
        }

    }

    private void initBroadCast() {
        intentFilter = new IntentFilter();
        //intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        intentFilter.addAction("com.google.android.gms.auth.api.phone.SMS_RETRIEVED");

        appSMSBroadcastReceiver = new AppSMSBroadcastReceiver();
        appSMSBroadcastReceiver.setOnSmsReceiveListener(new AppSMSBroadcastReceiver.OnSmsReceiveListener() {
            @Override
            public void onReceive(String messageCode) {
                //Log.e(TAG, "onReceive: " + messageCode);
                //Toast.makeText(ForgotPasswordActvivity.this, messageCode, Toast.LENGTH_SHORT).show();
                Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
                Matcher matcher = otpPattern.matcher(messageCode);
                Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
                if (fragmentById instanceof VerifyMobileFragment) {
                    if (matcher.find()) {
                        String group = matcher.group(0);
                        ((VerifyMobileFragment) fragmentById).inputOtp(group);
                    }
                } else if (fragmentById instanceof FragmentOtpForgot) {
                    if (matcher.find()) {
                        String group = matcher.group(0);
                        ((FragmentOtpForgot) fragmentById).inputOtp(group);
                    }
                }
            }
        });

    }

    private void smsListener() {
        SmsRetrieverClient client = SmsRetriever.getClient(ForgotPasswordActvivity.this);
        client.startSmsRetriever();
    }


    private void setupActionBar() {
        common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        common_toolbar_title.setText(getString(R.string.info_forgot_password2));
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(appSMSBroadcastReceiver);
        appSMSBroadcastReceiver = null;
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
        ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
        if (isReset) {
            bundle.putString("reset", "yes");
        } else {
            bundle.putString("reset", "no");
        }
        if (bundle.getBoolean(AppoConstants.OTPSECRREN, false)) {
            FragmentOtpForgot mFragmentOtpForgot = new FragmentOtpForgot();
            mFragmentOtpForgot.setArguments(bundle);
            initFragments(mFragmentOtpForgot);

        } else if (bundle.getBoolean(AppoConstants.OTPSUCCESS, false)) {
            ForgotSuccessFragment mForgotSuccessFragment = new ForgotSuccessFragment();
            mForgotSuccessFragment.setArguments(bundle);
            initFragments(mForgotSuccessFragment);
        } else {
            forgotPasswordFragment.setArguments(bundle);
            initFragments(forgotPasswordFragment);
        }

    }

    public void initFragments(Fragment params) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer, params);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}