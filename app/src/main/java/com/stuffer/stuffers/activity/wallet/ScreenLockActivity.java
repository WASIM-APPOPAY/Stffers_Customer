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
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.fragments.screenlockpin.ScreenLockFragment;
import com.stuffer.stuffers.fragments.screenlockpin.ScreenlockUpdateFragment;
import com.stuffer.stuffers.utils.AppoConstants;

public class ScreenLockActivity extends AppCompatActivity implements FragmentReplaceListener {

    private String oldScreenLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_lock);
        setupActionBar();
        oldScreenLock = getIntent().getStringExtra(AppoConstants.INFO);
        ScreenLockFragment screenLockFragment = new ScreenLockFragment();
        if (savedInstanceState == null) {
            initFragments(screenLockFragment);
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

        toolbarTitle.setText(R.string.toolbar_title_screen_lock_pin);

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
        ScreenlockUpdateFragment screenlockUpdateFragment = new ScreenlockUpdateFragment();
        bundle.putString(AppoConstants.INFO, oldScreenLock);
        screenlockUpdateFragment.setArguments(bundle);
        initFragments(screenlockUpdateFragment);
    }
}
