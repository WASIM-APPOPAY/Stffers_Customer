package com.stuffer.stuffers.activity.wallet;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.AreaSelectListener;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.fragments.forgot_password.VerifyMobileFragment;
import com.stuffer.stuffers.fragments.transactionpin.TransactionPinFragment;
import com.stuffer.stuffers.fragments.transactionpin.TransactionPinUpdateFragment;
import com.stuffer.stuffers.utils.AppoConstants;

public class TransactionPinActivity extends AppCompatActivity implements FragmentReplaceListener , AreaSelectListener {
    private static final String TAG = "TransactionPinActivity";
    private String oldTransactionPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_pin);
        setupActionBar();
        oldTransactionPin = getIntent().getStringExtra(AppoConstants.INFO);
        if (savedInstanceState == null) {
            //Log.e(TAG, "onCreate: called");
            TransactionPinFragment transactionPinFragment = new TransactionPinFragment();
            initFragments(transactionPinFragment);
        } else {
            //Log.e(TAG, "onCreate: not called");
        }
    }

    @Override
    public void onAreaSelected(int pos) {
        Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (fragmentById instanceof TransactionPinFragment){
            ((TransactionPinFragment)fragmentById).updateAreaCode(pos);
        }

    }



    public void initFragments(Fragment params) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer, params);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("Transaction Pin");

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
        TransactionPinUpdateFragment transactionPinUpdateFragment = new TransactionPinUpdateFragment();
        bundle.putString(AppoConstants.INFO, oldTransactionPin);
        transactionPinUpdateFragment.setArguments(bundle);
        initFragments(transactionPinUpdateFragment);
    }
}
