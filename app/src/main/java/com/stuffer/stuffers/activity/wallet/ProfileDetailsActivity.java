package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stuffer.stuffers.utils.Helper;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.views.MyTextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class ProfileDetailsActivity extends AppCompatActivity {

    private String vaultValue;
    private JSONObject mIndex;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private static final String TAG = "ProfileDetailsActivity";
    MyTextView tvAddress, tvCityName, tvDob, tvScreen, tvTrans, tvPhone, tvUserName, tvEmail,tvState;
    private LinearLayout layoutAfterUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        mainAPIInterface = ApiUtils.getAPIService();
        vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        tvAddress = findViewById(R.id.tvAddress);
        tvCityName = findViewById(R.id.tvCityName);
        tvState = findViewById(R.id.tvState);
        tvEmail = findViewById(R.id.tvEmial);
        tvDob = findViewById(R.id.tvDob);
        tvScreen = findViewById(R.id.tvScreen);
        tvTrans = findViewById(R.id.tvTrans);
        tvPhone = findViewById(R.id.tvPhone);
        tvUserName = findViewById(R.id.tvUserName);
        layoutAfterUpdate = findViewById(R.id.layoutAfterUpdate);

        setupActionBar();
        if (!StringUtils.isEmpty(vaultValue)) {
            try {
                mIndex = new JSONObject(vaultValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            onUpdateProfile();
        }
    }


        private void invalidateUserInfo () {
            vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
            if (!StringUtils.isEmpty(vaultValue)) {
                try {
                    mIndex = new JSONObject(vaultValue);
                    ////Log.e("TAG", "onCreate: " + mIndex.toString());
                    JSONObject result = mIndex.getJSONObject("result");
                    tvUserName.setText("User Name : " + result.getString(AppoConstants.FIRSTNAME) + " " + result.getString(AppoConstants.LASTNAME));
                    tvPhone.setText("Mobile Number : " + result.getString(AppoConstants.MOBILENUMBER));
                    tvEmail.setText("Emial Id : " + result.getString(AppoConstants.EMIAL));
                    JSONObject customerDetails = result.getJSONObject(AppoConstants.CUSTOMERDETAILS);
                    /*if (customerDetails.isNull(AppoConstants.CITYNAME)) {
                        layoutAfterUpdate.setVisibility(View.GONE);
                        //tvScreen.setText("Screen Lock Pin : " + " NA ");
                        //tvTrans.setText("Transaction Pin : " + " NA ");
                        tvCityName.setText("City Name : " + " NA ");
                        tvAddress.setText("Address : " + " NA ");
                        tvDob.setText("Date of birth : " + " NA ");
                    } else*/ {
                        layoutAfterUpdate.setVisibility(View.VISIBLE);
                        //tvScreen.setText("Screen Lock Pin : " + " NA ");
                       // tvTrans.setText("Transaction Pin : " + result.getString(AppoConstants.TRANSACTIONPIN));
                        //tvCityName.setText("City Name : " + customerDetails.getString(AppoConstants.CITYNAME));
                        tvCityName.setVisibility(View.GONE);
                        tvAddress.setText("Address : " + customerDetails.getString(AppoConstants.ADDRESS));
                        //tvDob.setText("Date of birth : " + customerDetails.getString(AppoConstants.DOB));
                        String dob = TimeUtils.parseLongDate(customerDetails.getString(AppoConstants.DOB), TimeUtils.DOBFORMAT);
                        tvDob.setText("Date of birth : " + dob);
                        /*String countryId = customerDetails.getString(AppoConstants.COUNTRYID);
                        String stateId = customerDetails.getString(AppoConstants.STATEID);
                        getState(countryId, stateId);*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    private void getState(String countryId, String stateId) {
        try {
            JSONObject jsonCountryState = new JSONObject(Helper.Country);
            JSONArray jsonCountryStateResult = jsonCountryState.getJSONArray(AppoConstants.RESULT);
            for (int i = 0; i < jsonCountryStateResult.length(); i++) {
                JSONObject index = jsonCountryStateResult.getJSONObject(i);
                if (index.getString(AppoConstants.ID).equals(countryId)) {
//                    Log.e(TAG, "getState: Country Name :: " + index.getString(AppoConstants.COUNTRYNAME));
                    JSONArray jsonStateResult = index.getJSONArray(AppoConstants.STATES);
                    for (int j = 0; j < jsonStateResult.length(); j++) {
                        JSONObject indexState = jsonStateResult.getJSONObject(j);
                        if (indexState.getString(AppoConstants.ID).equals(stateId)) {
//                            Log.e(TAG, "getState: State Name :: " + indexState.getString(AppoConstants.STATENAME));
                            tvState.setText("Province/State : "+indexState.getString(AppoConstants.STATENAME));
                            tvState.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                    break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        private void onUpdateProfile () {
            dialog = new ProgressDialog(ProfileDetailsActivity.this);
            dialog.setMessage("please wait, getting profile");
            dialog.show();

            try {
                String accessToken = DataVaultManager.getInstance(ProfileDetailsActivity.this).getVaultValue(KEY_ACCESSTOKEN);
                JSONObject mResult = mIndex.getJSONObject(AppoConstants.RESULT);
                String ph = mResult.getString(AppoConstants.MOBILENUMBER);
                String area = mResult.getString(AppoConstants.PHONECODE);
                String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
                mainAPIInterface.getProfileDetails(Long.parseLong(ph), Integer.parseInt(area), bearer_).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            //String res = new Gson().toJson(response.body());
                            ////Log.e(TAG, "onResponse: getprofile :" + res);
                            JsonObject body = response.body();
                            String res=body.toString();

                            DataVaultManager.getInstance(ProfileDetailsActivity.this).saveUserDetails(res);
                            invalidateUserInfo();
                        } else {
                            if (response.code() == 401) {
                                DataVaultManager.getInstance(ProfileDetailsActivity.this).saveUserDetails("");
                                DataVaultManager.getInstance(ProfileDetailsActivity.this).saveUserAccessToken("");
                                Intent intent = new Intent(ProfileDetailsActivity.this, SignInActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        dialog.dismiss();
                        ////Log.e(TAG, "onFailure: " + t.getMessage().toString());
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    private void setupActionBar () {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("Profile Details");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // action bar menu behaviour
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;


                default:
                    return super.onOptionsItemSelected(item);
            }
        }

//https://cerca24.com/v1/products/categories/tree
//https://cerca24.com/v1/products/search
    }
