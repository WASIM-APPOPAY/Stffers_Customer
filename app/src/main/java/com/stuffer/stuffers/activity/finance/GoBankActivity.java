package com.stuffer.stuffers.activity.finance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.OptionSelectListener;
import com.stuffer.stuffers.communicator.PictureListener;
import com.stuffer.stuffers.communicator.SignatureCallback;
import com.stuffer.stuffers.fragments.finance_fragment.BankChildFragment;
import com.stuffer.stuffers.fragments.finance_fragment.ExistingCustomerFragment;
import com.stuffer.stuffers.fragments.finance_fragment.IdSignatureFragment;
import com.stuffer.stuffers.fragments.finance_fragment.OpenAccountFragment;
import com.stuffer.stuffers.fragments.finance_fragment.RequestAccountFragment;
import com.stuffer.stuffers.utils.AppoConstants;

import org.json.JSONObject;

public class GoBankActivity extends AppCompatActivity implements OptionSelectListener, SignatureCallback, PictureListener {
    private TextView toolbarTitle;
    private static final String TAG = "GoBankActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_bank);
        setupActionBar();
        if (savedInstanceState == null) {
            BankChildFragment bankChildFragment = new BankChildFragment();
            initFragments(bankChildFragment, getString(R.string.info_union_pay));
        }

        getPermission();
    }

    private void getPermission() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                verifyStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Bank Account");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }

    private void verifyStack() {
        try {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
            if (currentFragment instanceof OpenAccountFragment) {
                Log.d("message", "home fragment");
                getSupportFragmentManager().popBackStack();
                toolbarTitle.setText("Bank Account");
            } else if (currentFragment instanceof ExistingCustomerFragment) {
                Log.d("message", " set home fragment");
                getSupportFragmentManager().popBackStack();
                toolbarTitle.setText("Bank Account");
            } else if (currentFragment instanceof IdSignatureFragment) {
                getSupportFragmentManager().popBackStack();
                toolbarTitle.setText("Open Account");
            } else if (currentFragment instanceof RequestAccountFragment) {
                Log.d("message", "popping backstack");
                getSupportFragmentManager().popBackStack();
                toolbarTitle.setText("Bank Account");
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initFragments(Fragment fragment, String param) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer, fragment, param);
        transaction.addToBackStack(param);
        /*transaction.replace(R.id.mainContainer, fragment, tag);
        transaction.addToBackStack(tag);*/
        //transaction.commitAllowingStateLoss();
        transaction.commit();
    }


    @Override
    public void onSelectConfirm(String param, JSONObject userData) {
        if (param.equals(AppoConstants.OPEN_ACCOUNT)) {
            OpenAccountFragment openAccountFragment = new OpenAccountFragment();
            toolbarTitle.setText("Open Account");
            initFragments(openAccountFragment, param);
        } else if (param.equals(AppoConstants.NEXT_SCREEN)) {
            IdSignatureFragment idSignatureFragment = new IdSignatureFragment();
            toolbarTitle.setText("Open Account");
            Bundle bundle = new Bundle();
            bundle.putString(AppoConstants.INFO, userData.toString());
            idSignatureFragment.setArguments(bundle);
            initFragments(idSignatureFragment, param);
        } else if (param.equalsIgnoreCase(AppoConstants.EXISTING_CUSTOMER)) {
            ExistingCustomerFragment existingCustomerFragment = new ExistingCustomerFragment();
            //toolbarTitle.setText("Existing Customer");
            toolbarTitle.setText("Clientes");
            initFragments(existingCustomerFragment, param);
        } else {
            RequestAccountFragment requestAccountFragment = new RequestAccountFragment();
            //toolbarTitle.setText("Request Card");
            toolbarTitle.setText("Requisitos para tarjeta");
            initFragments(requestAccountFragment, param);
        }
    }


    @Override
    public void onBackPressed() {
        verifyStack();
    }

    @Override
    public void onSignatureConfirm(Bitmap bitmap, boolean status) {
        /*Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (currentFragment instanceof IdSignatureFragment) {
            ((IdSignatureFragment) currentFragment).setSignture(bitmap, status);
        }*/
    }

    @Override
    public void onPictureSelect(String path) {
        //Log.e(TAG, "onPictureSelect: at last called" + path);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (currentFragment instanceof IdSignatureFragment) {
            ((IdSignatureFragment) currentFragment).setImage(path);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: bank called");
    }
}