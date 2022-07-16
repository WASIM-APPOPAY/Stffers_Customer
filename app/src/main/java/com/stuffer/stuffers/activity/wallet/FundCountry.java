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

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.CountryListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemClickListener2;
import com.stuffer.stuffers.fragments.wallet_fragments.CountryFragment;
import com.stuffer.stuffers.fragments.wallet_fragments.SpecificFragment;

public class FundCountry extends AppCompatActivity implements RecyclerViewRowItemClickListener2, CountryListener {

    private TextView toolbarTitle;
    private ImageView ivScanCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_country);
        setupActionBar();
        if (savedInstanceState == null) {
            CountryFragment mCountryFragment = new CountryFragment();
            initFragment(mCountryFragment);
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);
        ivScanCard = toolbar.findViewById(R.id.ivScanCard);
        ivScanCard.setVisibility(View.GONE);
        toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("List Of Countries");
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
        fragmentTransaction.replace(R.id.cointainerCountry, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onRowItemClick2(int pos) {
        //Toast.makeText(this, "Position " + pos, Toast.LENGTH_SHORT).show();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.cointainerCountry);
        if (currentFragment instanceof CountryFragment) {
            ((CountryFragment) currentFragment).updatePosition(pos);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.cointainerCountry);
        if (currentFragment instanceof CountryFragment) {
            finish();
        } else {
            toolbarTitle.setText("List Of Countries");
            ivScanCard.setVisibility(View.GONE);
            getSupportFragmentManager().popBackStack();

        }
    }

    @Override
    public void onCountrySelect(int pos, String name, String flag) {
        if (pos == 10) {
            Glide.with(FundCountry.this).load(flag).into(ivScanCard);
            ivScanCard.setVisibility(View.VISIBLE);
            toolbarTitle.setText(name);
            SpecificFragment mSpecificFragment = new SpecificFragment();
            Bundle mBundle = new Bundle();
            mBundle.putString(SpecificFragment.ARG_PARAM1, "1");
            mSpecificFragment.setArguments(mBundle);
            initFragment(mSpecificFragment);

        } else if (pos == 12) {
            Glide.with(FundCountry.this).load(flag).into(ivScanCard);
            ivScanCard.setVisibility(View.VISIBLE);
            toolbarTitle.setText(name);
            SpecificFragment mSpecificFragment = new SpecificFragment();
            Bundle mBundle = new Bundle();
            mBundle.putString(SpecificFragment.ARG_PARAM1, "2");
            mSpecificFragment.setArguments(mBundle);
            initFragment(mSpecificFragment);

        }
    }
}