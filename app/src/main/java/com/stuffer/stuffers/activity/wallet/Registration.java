package com.stuffer.stuffers.activity.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.CarrierSelectListener;
import com.stuffer.stuffers.communicator.OtpRequestListener;
import com.stuffer.stuffers.communicator.VerifiedListener;
import com.stuffer.stuffers.fragments.bottom.HomeFragment;
import com.stuffer.stuffers.fragments.wallet_fragments.IdentityFragment;
import com.stuffer.stuffers.fragments.wallet_fragments.NumEmailFragment;
import com.stuffer.stuffers.fragments.wallet_fragments.VerifyFragment;
import com.stuffer.stuffers.utils.AppoConstants;

public class Registration extends AppCompatActivity implements OtpRequestListener, VerifiedListener, CarrierSelectListener {
    String mNameCode, mCountryCode, mMobileNo, mEmailId, mAddress, mCountryId;
    String[] descriptionData = {"Step One", "Step Two", "Step Three"};
    StateProgressBar stateProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        stateProgressBar = (StateProgressBar) findViewById(R.id.my_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        NumEmailFragment mNumEmailFragment = new NumEmailFragment();
        intiFragment(mNumEmailFragment);

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
    public void onOtpRequest(String nameCode, String countryCode, String mobileNumber, String emailId, String address, String countryId) {
         state(1);
        mNameCode = nameCode;
        mCountryCode = countryCode;
        mMobileNo = mobileNumber;
        mEmailId = emailId;
        mAddress = address;
        mCountryId = countryId;


        Bundle mBundle = new Bundle();
        mBundle.putString(AppoConstants.COUNTRYNAMECODE, nameCode);
        mBundle.putString(AppoConstants.COUNTRYCODE, countryCode);
        mBundle.putString(AppoConstants.MOBILENUMBER, mobileNumber);
        mBundle.putString(AppoConstants.EMIAL_ID, emailId);
        mBundle.putString(AppoConstants.ADDRESS, address);
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
                if (fragmentById instanceof VerifyFragment){
                    state(1);
                    stateProgressBar.checkStateCompleted(false);
                }else if (fragmentById instanceof NumEmailFragment){
                    state(1);
                    stateProgressBar.checkStateCompleted(false);
                }


            }
        } catch (Exception e) {


        }

    }
}