package com.stuffer.stuffers.activity.quick_pass;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.ScanRequestListener;
import com.stuffer.stuffers.communicator.SuccessResponseListener;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.utils.AppoConstants;

public class QuickPassActivity extends AppCompatActivity implements ScanRequestListener, SuccessResponseListener , TransactionPinListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_pass);
        ScanQuickPassFragment scanQuickPassFragment = new ScanQuickPassFragment();
        initFragments(scanQuickPassFragment);
        setupActionBar();
    }

    public void initFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.scanContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onRequestListener(String param) {
        QuickPassPayFragment fragment = new QuickPassPayFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.MERCHANTSCANCODE, param);
        fragment.setArguments(bundle);
        initFragments(fragment);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Quick Pass");
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
    public void onSuccess(boolean status) {
        //need to pass data to paren then profile screen
        Intent intent = new Intent();
        if (status) {
            setResult(Activity.RESULT_OK, intent);
        } else {
            setResult(Activity.RESULT_CANCELED, intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onPinConfirm(String pin) {
        Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.scanContainer);
        if (mFragment instanceof QuickPassPayFragment) {
            ((QuickPassPayFragment)mFragment).passTransactionPin(pin);
        }
    }
}