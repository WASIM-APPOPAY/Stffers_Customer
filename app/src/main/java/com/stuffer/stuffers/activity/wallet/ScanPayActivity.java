package com.stuffer.stuffers.activity.wallet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.InnerScanListener;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.fragments.bottom.AppoPayFragment;
import com.stuffer.stuffers.fragments.bottom.ScanAppopayFragment;
import com.stuffer.stuffers.fragments.merchant_pay.PayToAppopayMerchant;
import com.stuffer.stuffers.fragments.merchant_pay.ScanAppopayMerchant;
import com.stuffer.stuffers.utils.AppoConstants;

public class ScanPayActivity extends AppCompatActivity implements TransactionPinListener, InnerScanListener {

    private int mType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_pay);
        setupActionBar();
        if (getIntent().getExtras() != null) {
            mType = getIntent().getIntExtra(AppoConstants.WHERE, 0);
        }
        if (isPermissionGranted()) {
            if (savedInstanceState == null) {
                ScanAppopayMerchant mScanAppopayFragment = new ScanAppopayMerchant();
                Bundle mBundle = new Bundle();
                mBundle.putInt(AppoConstants.WHERE, mType);
                mScanAppopayFragment.setArguments(mBundle);
                initFragments(mScanAppopayFragment);
            }
        } else {
            ActivityCompat.requestPermissions(ScanPayActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, AppoConstants.CAMERA_REQUEST_CODE);
        }
    }

    private void initFragments(Fragment mFragment) {
        FragmentManager fragmentManager4 = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
        fragmentTransaction4.addToBackStack(null);
        fragmentTransaction4.replace(R.id.scanContainer, mFragment);
        fragmentTransaction4.commit();
    }

    private boolean isPermissionGranted() {
        boolean cameraPermission = ActivityCompat.checkSelfPermission(ScanPayActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        return cameraPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppoConstants.CAMERA_REQUEST_CODE) {
            if (isPermissionGranted()) {
                ScanAppopayMerchant mScanAppopayFragment = new ScanAppopayMerchant();
                initFragments(mScanAppopayFragment);

            } else {
                Toast.makeText(this, "permission denied by user", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(R.string.toolbar_title_merchant_details);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e(TAG, "onActivityResult: result called" );
        redirectHomePay();
    }

    public void redirectHomePay() {

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    public void onPinConfirm(String pin) {

        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.scanContainer);
        if (fragmentById instanceof PayToAppopayMerchant) {
            ((PayToAppopayMerchant) fragmentById).onConfirm(pin);
        }

    }

    @Override
    public void onInnerRequestListener(String param) {
        PayToAppopayMerchant mFragment = new PayToAppopayMerchant();
        Bundle mBundle = new Bundle();
        mBundle.putString(AppoConstants.MERCHANTSCANCODE, param);
        mBundle.putInt(AppoConstants.WHERE, mType);
        mFragment.setArguments(mBundle);
        initFragments(mFragment);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}