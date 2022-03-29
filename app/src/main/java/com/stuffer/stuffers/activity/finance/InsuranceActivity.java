package com.stuffer.stuffers.activity.finance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.CarrierSelectListener;
import com.stuffer.stuffers.fragments.dialog.CarrierDialogFragment;
import com.stuffer.stuffers.fragments.dialog.InsuranceDialog;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyTextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class InsuranceActivity extends AppCompatActivity implements CarrierSelectListener {

    private LinearLayout llInsuranceType;
    MyTextView tvAddress, tvCityName, tvEmail, tvDob, tvPhone, tvUserName, tvInsuranceType;
    ImageView ivExpand;
    private int one = 0;
    private ExpandableLayout layoutExpand;
    private ArrayList<String> mListType;
    private InsuranceDialog carrierDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        llInsuranceType = (LinearLayout) findViewById(R.id.llInsuranceType);
        ivExpand = findViewById(R.id.ivExpand);
        layoutExpand = (ExpandableLayout) findViewById(R.id.layoutExpand);
        tvInsuranceType = findViewById(R.id.tvInsuranceType);
        tvAddress = findViewById(R.id.tvAddress);
        tvCityName = findViewById(R.id.tvCityName);
        tvEmail = findViewById(R.id.tvEmial);
        tvDob = findViewById(R.id.tvDob);
        tvPhone = findViewById(R.id.tvPhone);
        tvUserName = findViewById(R.id.tvUserName);
        setupActionBar();
        invalidateUserInfo();
        llInsuranceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSelectedInsuranceType();
            }
        });

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

    private void getSelectedInsuranceType() {
        mListType = new ArrayList<>();
        mListType.add("Life Insurance");
        mListType.add("Home Insurance");
        mListType.add("Car Insurance");
        mListType.add("Funeral");
        mListType.add("Travel Insurance");
        mListType.add("One Day Insurance");

        carrierDialogFragment = new InsuranceDialog();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.TITLE, "Please Select Insurance Type");
        bundle.putStringArrayList(AppoConstants.INFO, mListType);
        carrierDialogFragment.setArguments(bundle);
        carrierDialogFragment.setCancelable(false);
        carrierDialogFragment.show(getSupportFragmentManager(), carrierDialogFragment.getTag());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String stringExtra = data.getStringExtra(AppoConstants.INSURANCE_TYPE);
                tvInsuranceType.setText(stringExtra);
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
        toolbarTitle.setText("Insurance Request");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

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
                tvEmail.setText("Email Id : " + result.getString(AppoConstants.EMIAL));
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
    public void onCarrierSelect(int pos) {
        tvInsuranceType.setText(mListType.get(pos));
        carrierDialogFragment.dismiss();
    }




}
