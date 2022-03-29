package com.stuffer.stuffers.activity.FianceTab;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.OnBankSubmit;
import com.stuffer.stuffers.communicator.OptionSelectListener;
import com.stuffer.stuffers.communicator.PictureListener;
import com.stuffer.stuffers.communicator.SignatureCallback;
import com.stuffer.stuffers.fragments.finance_fragment.BankChildFragment;
import com.stuffer.stuffers.fragments.finance_fragment.ExistingCustomerFragment;
import com.stuffer.stuffers.fragments.finance_fragment.IdSignatureFragment;
import com.stuffer.stuffers.fragments.finance_fragment.OpenAccountFragment;
import com.stuffer.stuffers.fragments.finance_fragment.RequestAccountFragment;
import com.stuffer.stuffers.my_camera.CameraActivity;
import com.stuffer.stuffers.utils.AppoConstants;

import org.json.JSONObject;

public class FinanceActivity extends AppCompatActivity implements OptionSelectListener, SignatureCallback, PictureListener, OnBankSubmit {

    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        if (savedInstanceState == null) {
            BankChildFragment bankChildFragment = new BankChildFragment();
            initFragments(bankChildFragment, "nueva cuenta");
        }

        setupActionBar();

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
        toolbarTitle.setText(getString(R.string.info_nueva_cuenta));
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

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
            toolbarTitle.setText(getString(R.string.info_nueva_cuenta));
            initFragments(openAccountFragment, param);
        } else if (param.equals(AppoConstants.NEXT_SCREEN)) {
            IdSignatureFragment idSignatureFragment = new IdSignatureFragment();
            toolbarTitle.setText(getString(R.string.info_nueva_cuenta));
            Bundle bundle = new Bundle();
            bundle.putString(AppoConstants.INFO, userData.toString());
            idSignatureFragment.setArguments(bundle);
            initFragments(idSignatureFragment, param);
        }
    }

    @Override
    public void onBackPressed() {
        verifyStack();
    }

    @Override
    public void onSignatureConfirm(Bitmap bitmap, boolean status) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (currentFragment instanceof IdSignatureFragment) {
            ((IdSignatureFragment) currentFragment).setSignture(bitmap, status);
        }
    }

    @Override
    public void onPictureSelect(String path) {
        //Log.e(TAG, "onPictureSelect: at last called" + path);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (currentFragment instanceof IdSignatureFragment) {
            ((IdSignatureFragment) currentFragment).setImage(path);
        }

    }

    private void verifyStack() {
        try {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
            if (currentFragment instanceof OpenAccountFragment) {
                Log.d("message", "home fragment");
                getSupportFragmentManager().popBackStack();
                toolbarTitle.setText(getString(R.string.info_nueva_cuenta));
            } else if (currentFragment instanceof IdSignatureFragment) {
                getSupportFragmentManager().popBackStack();
                toolbarTitle.setText(getString(R.string.info_nueva_cuenta));
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConfirm() {

        Intent intentOk = new Intent();
        setResult(RESULT_OK, intentOk);
        finish();
    }


}