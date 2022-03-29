package com.stuffer.stuffers.activity.FianceTab;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainUAPIInterface;
import com.stuffer.stuffers.fragments.finance_fragment.RequestAccountFragment;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.material.textfield.TextInputEditText;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class TarjetaActivity extends AppCompatActivity {

    private TextView toolbarTitle;
    private MyButton btnCardEnrollment;
    private MainUAPIInterface apiServiceUNIONPay;
    private TextInputEditText mTiedAccNo;
    private TextInputEditText mTiedFname;
    private TextInputEditText mTiedLname;
    private MyTextView tvCountryCodeU;
    private MyTextView tvMobNumU;
    private Long senderMobileNumber;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneCode;
    private String phWithCode;
    private String walletAccountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta);
        setupActionBar();
        /*if (savedInstanceState == null) {
            RequestAccountFragment requestAccountFragment = new RequestAccountFragment();
            //toolbarTitle.setText("Request Card");
            toolbarTitle.setText(getString(R.string.info_card1));
            initFragments(requestAccountFragment, "tarjeta");

        }*/

        apiServiceUNIONPay = ApiUtils.getApiServiceUNIONPay();
        btnCardEnrollment = (MyButton) findViewById(R.id.btnCardEnrollment);
        mTiedAccNo = (TextInputEditText) findViewById(R.id.mTiedAccNo);
        mTiedFname = (TextInputEditText) findViewById(R.id.mTiedFname);
        mTiedLname = (TextInputEditText) findViewById(R.id.mTiedLname);
        tvCountryCodeU = (MyTextView) findViewById(R.id.tvCountryCodeU);
        tvMobNumU = (MyTextView) findViewById(R.id.tvMobNumU);


        //btnCardEnrollment.setOnClickListener(this);
        String mUserDetails = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        firstName = Helper.getFirstName();
        lastName = Helper.getLastName();
        fullName = firstName + " " + lastName;
        senderMobileNumber = Helper.getSenderMobileNumber();
        phoneCode = Helper.getPhoneCode();
        phWithCode = phoneCode + senderMobileNumber;
        walletAccountNumber = Helper.getWalletAccountNumber();

        mTiedAccNo.setText(walletAccountNumber);
        mTiedFname.setText(firstName);
        mTiedLname.setText(lastName);
        tvCountryCodeU.setText(phoneCode);
        tvMobNumU.setText("" + senderMobileNumber);


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

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Money Express");
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
    public void onBackPressed() {
        finish();
    }
}