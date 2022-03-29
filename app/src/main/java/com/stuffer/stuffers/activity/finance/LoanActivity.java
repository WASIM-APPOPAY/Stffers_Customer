package com.stuffer.stuffers.activity.finance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyTextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class LoanActivity extends AppCompatActivity {
    MyTextView tvAddress, tvCityName, tvEmail, tvDob, tvPhone, tvUserName;
    ImageView ivExpand;
    private int one = 0;
    private ExpandableLayout layoutExpand;
    private ArrayList<String> mListType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        ivExpand = findViewById(R.id.ivExpand);
        layoutExpand = (ExpandableLayout) findViewById(R.id.layoutExpand);

        tvAddress = findViewById(R.id.tvAddress);
        tvCityName = findViewById(R.id.tvCityName);
        tvEmail = findViewById(R.id.tvEmial);
        tvDob = findViewById(R.id.tvDob);
        tvPhone = findViewById(R.id.tvPhone);
        tvUserName = findViewById(R.id.tvUserName);
        setupActionBar();
        invalidateUserInfo();
        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one == 0) {
                    ivExpand.setImageResource(R.drawable.ic_add_primary);
                    layoutExpand.setExpanded(false);
                    one = 1;
                } else {
                    ivExpand.setImageResource(R.drawable.ic_remove_primary);
                    layoutExpand.setExpanded(true);
                    one = 0;
                }

            }
        });
    }

    private void invalidateUserInfo() {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(vaultValue)) {
            try {
                JSONObject mIndex = new JSONObject(vaultValue);
                //Log.e("TAG", "onCreate: " + mIndex.toString());
                JSONObject result = mIndex.getJSONObject("result");
                tvUserName.setText("User Name : " + result.getString(AppoConstants.FIRSTNAME) + " " + result.getString(AppoConstants.LASTNAME));
                tvPhone.setText("Mobile Number : " + result.getString(AppoConstants.MOBILENUMBER));
                tvEmail.setText("Emial Id : " + result.getString(AppoConstants.EMIAL));
                JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                if (customerDetails.isNull(AppoConstants.CITYNAME)) {
                    tvCityName.setText("City Name : " + " NA ");
                    tvAddress.setText("Address : " + " NA ");
                    tvDob.setText("Date of birth : " + " NA ");
                } else {
                    tvCityName.setText("City Name : " + customerDetails.getString(AppoConstants.CITYNAME));
                    tvAddress.setText("Address : " + customerDetails.getString(AppoConstants.ADDRESS));
                    tvDob.setText("Date of birth : " + customerDetails.getString(AppoConstants.DOB));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Loan Request");
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
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}