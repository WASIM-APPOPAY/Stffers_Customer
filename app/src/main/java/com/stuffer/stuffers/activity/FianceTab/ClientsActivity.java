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

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.fragments.finance_fragment.ExistingCustomerFragment;
import com.stuffer.stuffers.utils.FriendlyMessage;

public class ClientsActivity extends AppCompatActivity {

    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        setupActionBar();

        if (savedInstanceState == null) {
            ExistingCustomerFragment existingCustomerFragment = new ExistingCustomerFragment();
            //toolbarTitle.setText("Existing Customer");
            toolbarTitle.setText(getString(R.string.info_clientes));
            initFragments(existingCustomerFragment, "existing customer");
        }
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
        toolbarTitle.setText("Requisitos para tarjeta");
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