package com.stuffer.stuffers.activity.cardtopup;

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
import com.stuffer.stuffers.communicator.ConfirmSelectListener;
import com.stuffer.stuffers.communicator.CurrencySelectListener;
import com.stuffer.stuffers.communicator.DefaultCardSelectListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.fragments.topupcard.CardDetailsFragment;
import com.stuffer.stuffers.fragments.topupcard.CardListFragment;
import com.stuffer.stuffers.utils.AppoConstants;

import org.json.JSONObject;

public class TopupCardActivity extends AppCompatActivity implements RecyclerViewRowItemCLickListener, CurrencySelectListener, ConfirmSelectListener, DefaultCardSelectListener {
    String tag;
    private static final String TAG = "TopupCardActivity";
    private int mId;
    private String mCurrencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_card);
        setupActionBar();
        if (savedInstanceState == null) {
            CardListFragment cardListFragment = new CardListFragment();
            tag = "cards";
            intiFragments(cardListFragment);
        }
    }

    private void intiFragments(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }


    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("My Cards");

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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onRowItemClick(int pos) {
        //Log.e(TAG, "onRowItemClick: click position :: " + pos);
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (currentFragment instanceof CardListFragment) {
            ((CardListFragment) currentFragment).getRecyclerViewCardDetailsData(pos);
        }
        /*
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment instanceof CustomerPayFragment) {
            ((CustomerPayFragment) currentFragment).getBaseConversion(pos);
        }

         */
    }

    @Override
    public void onCurrencySelected(int id, String currencyCode) {
        mId = id;
        mCurrencyCode = currencyCode;
    }

    @Override
    public void onConfirmSelect() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment instanceof CardDetailsFragment) {
            ((CardDetailsFragment) currentFragment).setCurrency(mId, mCurrencyCode);
        }
    }

    @Override
    public void onDefaultCardSelect(JSONObject jsonDefault) {
        CardDetailsFragment cardDetailsFragment = new CardDetailsFragment();
        Bundle bundle = new Bundle();
        String json = jsonDefault.toString();
        bundle.putString(AppoConstants.INFO, json);
        cardDetailsFragment.setArguments(bundle);
        tag = "details";
        intiFragments(cardDetailsFragment);
    }
}
