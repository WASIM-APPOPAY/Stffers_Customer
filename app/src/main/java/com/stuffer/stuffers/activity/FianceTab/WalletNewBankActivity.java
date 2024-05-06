package com.stuffer.stuffers.activity.FianceTab;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.AgreementListen;
import com.stuffer.stuffers.communicator.OnBankSubmit;
import com.stuffer.stuffers.communicator.OptionSelectListener;
import com.stuffer.stuffers.communicator.SignatureCallback;
import com.stuffer.stuffers.fragments.finance_fragment.IdSignatureFragment;
import com.stuffer.stuffers.fragments.wallet_fragments.NewAccountFragment;
import com.stuffer.stuffers.fragments.wallet_fragments.NewSignFragment;
import com.stuffer.stuffers.fragments.wallet_fragments.WalletBankFragment;

import org.json.JSONObject;

public class WalletNewBankActivity extends AppCompatActivity implements OptionSelectListener, AgreementListen, SignatureCallback , OnBankSubmit {

    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_bank2);
        setupActionBar();
        if (savedInstanceState == null) {
            WalletBankFragment mWalletBankFragment = new WalletBankFragment();
            initFragments(mWalletBankFragment);
        }
    }

    private void initFragments(Fragment mFragment) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.walletContainer, mFragment);
        mFragmentTransaction.commit();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Wallet Bank Activity");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }


    @Override
    public void onSelectConfirm(String param, JSONObject userData) {
        NewAccountFragment mNewAccountFragment = new NewAccountFragment();
        initFragments(mNewAccountFragment);
    }

    @Override
    public void onSignatureConfirm(Bitmap bitmap, boolean status) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.walletContainer);
        if (currentFragment instanceof NewSignFragment) {
            ((NewSignFragment) currentFragment).setSignture(bitmap, status);
        }
    }

    @Override
    public void onAgreementConfirm() {
        NewSignFragment mSignFragment = new NewSignFragment();
        initFragments(mSignFragment);
    }

    @Override
    public void onConfirm(int param) {

    }
}