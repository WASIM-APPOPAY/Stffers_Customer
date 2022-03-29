package com.stuffer.stuffers.activity.payment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.QRCodeUtil;
import com.stuffer.stuffers.views.MyTextView;
import com.hbb20.CountryCodePicker;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class GasQrActivity extends AppCompatActivity {
    MyTextView tvUserName, tvUserMobile, tvProfileDetails, tvWalletAmount;
    CountryCodePicker countryCodePicker;
    ImageView user_qr_image;
    private String stringExtra;
    private String userName;
    private String userMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_qr);
        stringExtra = getIntent().getStringExtra(AppoConstants.INFO);
        userName = getIntent().getStringExtra(AppoConstants.FULLNAME);
        userMobile = getIntent().getStringExtra(AppoConstants.MOBILENUMBER);
        setupActionBar();
        initViews();
        countryCodePicker.setExcludedCountries(getString(R.string.info_exclude_countries));
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserMobile = findViewById(R.id.tvUserMobile);
        user_qr_image = findViewById(R.id.user_qr_image);
        tvProfileDetails = findViewById(R.id.tvProfileDetails);
        tvWalletAmount = findViewById(R.id.tvWalletAmount);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        countryCodePicker.setEnabled(false);
        tvUserName.setText(userName);
        tvUserMobile.setText(userMobile);



        invalidateUserInfo();


    }

    private void invalidateUserInfo() {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        if (!StringUtils.isEmpty(vaultValue)) {
            try {
                JSONObject mIndex = new JSONObject(vaultValue);
                //Log.e("TAG", "onCreate: " + mIndex.toString());
                JSONObject result = mIndex.getJSONObject("result");
                JSONObject customerdetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                JSONArray customeraccount = customerdetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
                if (customeraccount.length() > 0) {
                    JSONObject obj = customeraccount.getJSONObject(0);
                    String balance = obj.getString(AppoConstants.CURRENTBALANCE);
                    tvWalletAmount.setText("$" + balance);
                } else {
                    tvWalletAmount.setText("$0");
                }
                Bitmap bitmap = QRCodeUtil.encodeAsBitmap(stringExtra, 150, 150);
                user_qr_image.setImageBitmap(bitmap);
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
        toolbarTitle.setText(getString(R.string.info_gas_pay));
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(getString(R.string.info_add_money));
        toolbar.setTitleTextColor(Color.WHITE);
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