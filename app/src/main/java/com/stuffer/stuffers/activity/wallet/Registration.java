package com.stuffer.stuffers.activity.wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.CarrierSelectListener;
import com.stuffer.stuffers.communicator.OtpRequestListener;
import com.stuffer.stuffers.communicator.StateSelectListener;
import com.stuffer.stuffers.communicator.VerifiedListener;
import com.stuffer.stuffers.fragments.bottom.HomeFragment;
import com.stuffer.stuffers.fragments.wallet_fragments.IdentityFragment;
import com.stuffer.stuffers.fragments.wallet_fragments.NumEmailFragment;
import com.stuffer.stuffers.fragments.wallet_fragments.VerifyFragment;
import com.stuffer.stuffers.myService.AppSMSBroadcastReceiver;
import com.stuffer.stuffers.myService.SMSReceiver;
import com.stuffer.stuffers.utils.AppoConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity implements OtpRequestListener, VerifiedListener, CarrierSelectListener, StateSelectListener {
    String mNameCode, mCountryCode, mMobileNo, mEmailId, mAddress, mCountryId;
    String[] descriptionData = {"Step One", "Step Two", "Step Three"};
    StateProgressBar stateProgressBar;
    private SMSReceiver smsReceiver;
    private static final String TAG = "Registration";
    private AppSMSBroadcastReceiver appSMSBroadcastReceiver;
    private IntentFilter intentFilter;
    private String mStateId, mZipCode, mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        stateProgressBar = (StateProgressBar) findViewById(R.id.my_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        smsListener();
        initBroadCast();
        registerReceiver(appSMSBroadcastReceiver, intentFilter);
        NumEmailFragment mNumEmailFragment = new NumEmailFragment();
        intiFragment(mNumEmailFragment);
        /*IdentityFragment identityFragment = new IdentityFragment();
        intiFragment(identityFragment);*/


    }

    private void intiFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerSignUp, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void state(int position) {
        switch (position) {
            case 1:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                break;
            case 2:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                break;
            case 3:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                break;
            case 4:
                stateProgressBar.setAllStatesCompleted(true);
                break;
        }
    }


    @Override
    public void onOtpRequest(String nameCode, String countryCode, String mobileNumber, String emailId, String address, String countryId, int stateId, String zipCode, String city) {
        state(1);
        mNameCode = nameCode;
        mCountryCode = countryCode;
        mMobileNo = mobileNumber;
        mEmailId = emailId;
        mAddress = address;
        mCountryId = countryId;

        mStateId = String.valueOf(stateId);
        mZipCode = zipCode;
        mCity = city;


        Bundle mBundle = new Bundle();
        mBundle.putString(AppoConstants.COUNTRYNAMECODE, nameCode);
        mBundle.putString(AppoConstants.COUNTRYCODE, countryCode);
        mBundle.putString(AppoConstants.MOBILENUMBER, mobileNumber);
        mBundle.putString(AppoConstants.EMIAL_ID, emailId);
        mBundle.putString(AppoConstants.ADDRESS, address);
        mBundle.putString(AppoConstants.STATEID, String.valueOf(stateId));
        mBundle.putString(AppoConstants.ZIPCODE2, zipCode);
        mBundle.putString(AppoConstants.CITY, city);
        VerifyFragment mVerifyFragment = new VerifyFragment();
        mVerifyFragment.setArguments(mBundle);
        intiFragment(mVerifyFragment);


    }

    @Override
    public void onVerified(String nameCode, String countryCode, String mobileNumber) {
        IdentityFragment mIdentityFragment = new IdentityFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(AppoConstants.COUNTRYNAMECODE, mNameCode);
        mBundle.putString(AppoConstants.COUNTRYCODE, mCountryCode);
        mBundle.putString(AppoConstants.MOBILENUMBER, mMobileNo);
        mBundle.putString(AppoConstants.EMIAL_ID, mEmailId);
        mBundle.putString(AppoConstants.ADDRESS, mAddress);
        mBundle.putString(AppoConstants.COUNTRYID, mCountryId);
        mBundle.putString(AppoConstants.STATEID, mStateId);
        mBundle.putString(AppoConstants.ZIPCODE2, mZipCode);
        mBundle.putString(AppoConstants.CITY, mCity);

        mIdentityFragment.setArguments(mBundle);
        intiFragment(mIdentityFragment);
        state(2);


    }

    @Override
    public void onCarrierSelect(int pos) {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.containerSignUp);
        if (fragmentById instanceof IdentityFragment) {
            ((IdentityFragment) fragmentById).setScanName(pos);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.containerSignUp);
            if (currentFragment instanceof NumEmailFragment) {
                Log.d("message", "home fragment");
                finish();
            } else {
                Log.d("message", "popping backstack");
                getSupportFragmentManager().popBackStack();
                Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.containerSignUp);
                if (fragmentById instanceof VerifyFragment) {
                    state(1);
                    stateProgressBar.checkStateCompleted(false);
                } else if (fragmentById instanceof NumEmailFragment) {
                    state(1);
                    stateProgressBar.checkStateCompleted(false);
                }


            }
        } catch (Exception e) {


        }

    }


    private void showToast(String msg) {
        Toast.makeText(Registration.this, msg, Toast.LENGTH_SHORT).show();
    }

    //#]Your OTP is:845285,3z8jiQN9JSV
    private void initBroadCast() {
        intentFilter = new IntentFilter("com.google.android.gms.auth.api.phone.SMS_RETRIEVED");
        appSMSBroadcastReceiver = new AppSMSBroadcastReceiver();
        appSMSBroadcastReceiver.setOnSmsReceiveListener(new AppSMSBroadcastReceiver.OnSmsReceiveListener() {
            @Override
            public void onReceive(String messageCode) {
                Log.e(TAG, "onReceive: " + messageCode);
                Toast.makeText(Registration.this, messageCode, Toast.LENGTH_SHORT).show();

                Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
                Matcher matcher = otpPattern.matcher(messageCode);
                Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.containerSignUp);
                if (fragmentById instanceof VerifyFragment) {
                    if (matcher.find()) {
                        String group = matcher.group(0);
                        ((VerifyFragment) fragmentById).inputOtp(group);
                    }
                }
            }
        });

    }

    private void smsListener() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsRetriever();
    }



    /*@Override
    protected void onResume() {
        super.onResume();
        registerReceiver(appSMSBroadcastReceiver, intentFilter);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(appSMSBroadcastReceiver);
    }

    @Override
    public void onStateSelected(String statename, int stateid) {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.containerSignUp);
        if (fragmentById instanceof NumEmailFragment) {
            ((NumEmailFragment) fragmentById).setStateName(statename, stateid);
        }
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(appSMSBroadcastReceiver);
    }*/


}