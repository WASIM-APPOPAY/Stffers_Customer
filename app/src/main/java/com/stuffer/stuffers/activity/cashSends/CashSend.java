package com.stuffer.stuffers.activity.cashSends;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.CalculationListener;
import com.stuffer.stuffers.communicator.CountrySelectListener;
import com.stuffer.stuffers.communicator.BeneficiaryListener;
import com.stuffer.stuffers.communicator.DestinationListener;
import com.stuffer.stuffers.communicator.ModeListener;
import com.stuffer.stuffers.communicator.OnItemSelect;
import com.stuffer.stuffers.utils.AppoConstants;

public class CashSend extends AppCompatActivity implements CountrySelectListener, OnItemSelect, BeneficiaryListener, CalculationListener, ModeListener, DestinationListener {

    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_send);
        setupActionBar();
        if (savedInstanceState == null) {
            CustomerDetails mCustomerDetails = new CustomerDetails();
            initFragment(mCustomerDetails);
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Cash Send");
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

    private void initFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerCashSend, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onCountrySelected(String countryName, String countryId, int countryCode, int pos) {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.containerCashSend);
        if (fragmentById instanceof CustomerDetails) {
            Log.e("onCountrySelected: ", countryName + " : " + countryId);
            ((CustomerDetails) fragmentById).setNationality(countryName, countryId, countryCode, pos);
        }
    }

    @Override
    public void onSelect(int pos) {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.containerCashSend);
        if (fragmentById instanceof CustomerDetails) {
            //((CustomerDetails) fragmentById).setRiskLevel(pos);
        }
    }

    @Override
    public void onBeneficiaryRequest() {
        BeneficiaryDetails mBeneficiaryDetails = new BeneficiaryDetails();

        initFragment(mBeneficiaryDetails);
    }

    @Override
    public void onBackPressed() {
        getBackStack();
    }

    private void getBackStack() {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.containerCashSend);
        if (fragmentById instanceof CustomerDetails) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public void onModeSelected(int pos) {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.containerCashSend);
        if (fragmentById instanceof BeneficiaryDetails) {
            ((BeneficiaryDetails) fragmentById).hideModeDialog(pos);
        }
    }

    @Override
    public void onDestinationSelect(String region, String country, String payoutCurrency) {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.containerCashSend);
        if (fragmentById instanceof BeneficiaryDetails) {
            ((BeneficiaryDetails) fragmentById).hideDestinationDialog(region, country, payoutCurrency);
        }
    }

    @Override
    public void onCalculationRequest(String sendingCurrency, String receiverCurrency) {
        SendMoneyToBank mSendMoneyToBank = new SendMoneyToBank();
        Bundle mBundle = new Bundle();
        mBundle.putString(AppoConstants.SENDERCURRENCY, sendingCurrency);
        mBundle.putString(AppoConstants.RECEIVERCURRENCY, receiverCurrency);
        mSendMoneyToBank.setArguments(mBundle);
        initFragment(mSendMoneyToBank);
    }
}