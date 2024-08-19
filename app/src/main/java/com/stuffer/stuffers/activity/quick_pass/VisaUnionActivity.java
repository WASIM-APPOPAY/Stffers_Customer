package com.stuffer.stuffers.activity.quick_pass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.FianceTab.UnionPayActivity;
import com.stuffer.stuffers.activity.wallet.AccountActivity;
import com.stuffer.stuffers.activity.wallet.HomeActivity3;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.communicator.OnBankSubmit;
import com.stuffer.stuffers.fragments.union_fragments.CardEnrollMentFragment;
import com.stuffer.stuffers.fragments.union_fragments.VisaFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextViewBold;

public class VisaUnionActivity extends AppCompatActivity implements OnBankSubmit {

    private MyTextViewBold toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_union);
        setupActionBar();
        VisaUnionFragment cardEnrollMentFragment = new VisaUnionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppoConstants.PRDNUMBER, 1003);
        bundle.putInt(AppoConstants.CARDTYPE, 2);
        /*String walletAccountNumber = Helper.getWalletAccountNumber();
        bundle.putString("newNumber", walletAccountNumber)*/;
        bundle.putString("newNumber", "2233445566778899");
        cardEnrollMentFragment.setArguments(bundle);
        toolbarTitle.setText("VISA Card Enrollment Overview");
        initFragments(cardEnrollMentFragment, "Visa Card Enrollment Overview");

    }


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

    @Override
    public void onBackPressed() {
        //finish();
        verifyStack();
    }


    private void setupActionBar() {
        toolbarTitle = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        toolbarTitle.setText(getString(R.string.info_union_pay));

        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initFragments(Fragment fragment, String param) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.unionContainer, fragment, param);
        transaction.addToBackStack(param);
        /*transaction.replace(R.id.mainContainer, fragment, tag);
         transaction.addToBackStack(tag);*/
        //transaction.commitAllowingStateLoss();
        transaction.commit();
    }

    private void verifyStack() {
        try {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.unionContainer);
            if (currentFragment instanceof CardEnrollMentFragment) {
                //Log.d("message", "home fragment");
                getSupportFragmentManager().popBackStack();
                toolbarTitle.setText(getString(R.string.info_card_enrollment));
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfirm(int param) {
        DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveDemoValue("yes");
        //Intent intent = new Intent(VisaUnionActivity.this, HomeActivity3.class);
        Intent intent = new Intent(VisaUnionActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
        /*if (param == 2) {
            UnionVisaFragment cardEnrollMentFragment = new UnionVisaFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(AppoConstants.PRDNUMBER, 1003);
            bundle.putInt(AppoConstants.CARDTYPE, 2);
            *//*String walletAccountNumber = Helper.getWalletAccountNumber();
            bundle.putString("newNumber", walletAccountNumber);*//*
            bundle.putString("newNumber", "2233445566778899");
            cardEnrollMentFragment.setArguments(bundle);
            toolbarTitle.setText(getString(R.string.info_wallet_card_enrollment));
            initFragments(cardEnrollMentFragment, getString(R.string.info_wallet_card_enrollment));
        } else {
            DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveDemoValue("yes");
            Intent intent = new Intent(VisaUnionActivity.this, HomeActivity3.class);
            startActivity(intent);
            finish();
        }*/
    }
}