package com.stuffer.stuffers.activity.giftcard;

import android.os.Bundle;
import android.os.Parcelable;
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
import com.stuffer.stuffers.communicator.RecyclerViewRowItemClickListener2;
import com.stuffer.stuffers.communicator.UserAccountTransferListener;
import com.stuffer.stuffers.fragments.giftcard.SearchCustomerGiftCardFragment;
import com.stuffer.stuffers.fragments.giftcard.SentGiftCardFragment;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OwnGiftCardActivity extends AppCompatActivity implements UserAccountTransferListener, RecyclerViewRowItemClickListener2 {
    private TextView toolbarTitle;
    private String tag;
    private String mIndexUser;
    private List<CurrencyResult> mCurrencyResponse;
    private String mBaseConversion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_gift_card);
        setupActionBar();
        if (savedInstanceState == null) {
            SearchCustomerGiftCardFragment fragment = new SearchCustomerGiftCardFragment();
            tag = "search_customer_gift_card";
            initFragments(fragment);
        }

    }

    public void initFragments(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainContainer, fragment, tag);
        transaction.addToBackStack(tag);
        //transaction.commitAllowingStateLoss();
        transaction.commit();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(R.string.ingo_gift_card);

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
                verifyStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRowItemClick2(int pos) {
        //Log.e("TAG", "onRowItemClick: " + pos);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        if (currentFragment instanceof SearchCustomerGiftCardFragment) {
            ((SearchCustomerGiftCardFragment) currentFragment).getBaseConversion(pos);
        }
    }

    @Override
    public void onAccountTransfer(JSONObject indexUser, List<CurrencyResult> currencyResults, JSONObject baseConversion) {

        mIndexUser = String.valueOf(indexUser);
        mCurrencyResponse = currencyResults;
        mBaseConversion = String.valueOf(baseConversion);
        tag = "wallet";
        SentGiftCardFragment fragment = new SentGiftCardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.SENTUSER, mIndexUser);
        bundle.putParcelableArrayList(AppoConstants.SENTCURRENCY, (ArrayList<? extends Parcelable>) mCurrencyResponse);
        bundle.putString(AppoConstants.SENTBASECONVERSION, mBaseConversion);
        fragment.setArguments(bundle);
        initFragments(fragment);

    }

    private void verifyStack() {
        try {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);
            if (currentFragment instanceof SearchCustomerGiftCardFragment) {
                //Log.d("message", "home fragment");
                finish();
            } else {
                //Log.d("message", "popping backstack");
                getSupportFragmentManager().popBackStack();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        verifyStack();
    }

}