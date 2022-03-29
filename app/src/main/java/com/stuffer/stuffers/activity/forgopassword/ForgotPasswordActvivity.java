package com.stuffer.stuffers.activity.forgopassword;

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

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.AreaSelectListener;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.fragments.forgot_password.ForgotPasswordFragment;
import com.stuffer.stuffers.fragments.forgot_password.VerifyMobileFragment;
import com.stuffer.stuffers.fragments.transactionpin.TransactionPinUpdateFragment;
import com.stuffer.stuffers.utils.AppoConstants;

public class ForgotPasswordActvivity extends AppCompatActivity implements FragmentReplaceListener, AreaSelectListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_actvivity);
        setupActionBar();
        if (savedInstanceState == null) {
            VerifyMobileFragment verifyMobileFragment = new VerifyMobileFragment();
            initFragments(verifyMobileFragment);
          /*ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
            Bundle bundle = new Bundle();
            bundle.putString(AppoConstants.PHONECODE, "91");
            bundle.putString(AppoConstants.MOBILENUMBER, "9836683269");
            bundle.putString(AppoConstants.PHWITHCODE, "919836683269");
            forgotPasswordFragment.setArguments(bundle);
            initFragments(forgotPasswordFragment);*/
        }



    }

    @Override
    public void onAreaSelected(int pos) {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (fragmentById instanceof VerifyMobileFragment){
            ((VerifyMobileFragment)fragmentById).updateAreaCode(pos);
        }

    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.info_forgot_password2));
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
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
    public void onFragmentReplaceClick(Bundle bundle) {
        ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
        forgotPasswordFragment.setArguments(bundle);
        initFragments(forgotPasswordFragment);
    }
    public void initFragments(Fragment params) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer, params);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}