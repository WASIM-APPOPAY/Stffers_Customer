package com.stuffer.stuffers.activity.loan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.stuffer.stuffers.AppoPayApplication;

import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignupAcitivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.api.MainLoanInterface;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.communicator.CommonListener;
import com.stuffer.stuffers.fragments.bottom.chatmodel.ChatUser;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomLoanDialog;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomRegister;
import com.stuffer.stuffers.models.Country.CountryCodeResponse;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.models.Country.State;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.DeviceUuidFactory;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class L_SignUpActivity extends AppCompatActivity implements CommonListener {
    private static final String TAG = "L_SignUpActivity";

    private MyEditText elname1, elname2, elname3, elmail, elcname, eladdress, elcityname, elstatename, elcountry;
    private ProgressDialog mLoader;
    private MainAPIInterface mainAPIInterface;
    List<Result> mListCountry;
    private List<State> mListState;
    private MainLoanInterface apiServiceLoan;
    private Button btnContinue;

    private User userMe;
    private String mCountryName;
    private String mStatName;
    private BottomLoanDialog mBottomLoanDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsign_up);
        mainAPIInterface = ApiUtils.getAPIService();
        apiServiceLoan = ApiUtils.getApiServiceLoan();

        elname1 = findViewById(R.id.elname1);
        elname2 = findViewById(R.id.elname2);
        elname3 = findViewById(R.id.elname3);
        elmail = findViewById(R.id.elmail);
        elcname = findViewById(R.id.elcname);
        eladdress = findViewById(R.id.eladdress);
        elcityname = findViewById(R.id.elcityname);
        elstatename = findViewById(R.id.elstatename);
        elcountry = findViewById(R.id.elcountry);
        btnContinue = findViewById(R.id.btnContinue);
        ChatHelper mChatHelper = new ChatHelper(this);
        userMe = mChatHelper.getLoggedInUser();

        setValues();
        //getCityList();
        getCountryList();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (elname1.getText().toString().trim().isEmpty()) {
                    elname1.setError("enter first name");
                    elname1.requestFocus();

                    return;
                }
                if (elname3.getText().toString().trim().isEmpty()) {
                    elname3.setError("enter lastname");
                    elname3.requestFocus();
                    return;
                }
                String mEmail = elmail.getText().toString().trim();
                if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                    elmail.setError(getString(R.string.info_valid_email_id));
                    elmail.setFocusable(true);
                    elmail.requestFocus();
                    return;
                }
                if (eladdress.getText().toString().trim().isEmpty()) {
                    eladdress.setError("enter your address");
                    eladdress.setFocusable(true);
                    eladdress.requestFocus();
                    return;
                }
                if (elcityname.getText().toString().trim().isEmpty()) {
                    elcityname.setError("enter your cityName");
                    elcityname.setFocusable(true);
                    elcityname.requestFocus();
                    return;
                }

                loanSignUp();


            }
        });

        mBottomLoanDialog = new BottomLoanDialog();
        mBottomLoanDialog.show(getSupportFragmentManager(), mBottomLoanDialog.getTag());
        mBottomLoanDialog.setCancelable(false);


    }

    private void loanSignUp() {
        Helper.showLoading(getString(R.string.info_please_wait), L_SignUpActivity.this);
        DeviceUuidFactory mDeviceUuidFactory = new DeviceUuidFactory(L_SignUpActivity.this);
        UUID deviceUuid = mDeviceUuidFactory.getDeviceUuid();
        JsonObject mRegistrationRequest = new JsonObject();
        mRegistrationRequest.addProperty("MobileNo", userMe.getId());
        mRegistrationRequest.addProperty("Email", elmail.getText().toString().trim());
        mRegistrationRequest.addProperty("Password", deviceUuid.toString());
        mRegistrationRequest.addProperty("FirstName", elname1.getText().toString().trim());
        mRegistrationRequest.addProperty("MiddleName", "");
        mRegistrationRequest.addProperty("LastName", elname3.getText().toString().trim());
        mRegistrationRequest.addProperty("City", elcityname.getText().toString().trim());
        mRegistrationRequest.addProperty("State", mStatName);
        mRegistrationRequest.addProperty("Country", mCountryName);
        mRegistrationRequest.addProperty("BaseCurrency", Helper.getCurrencySymble());
        mRegistrationRequest.addProperty("NotificationKey", DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.NotificationKey));

        mRegistrationRequest.addProperty("AddressLine1", eladdress.getText().toString().trim());
        mRegistrationRequest.addProperty("AddressLine2", "");
        mRegistrationRequest.addProperty("CompanyName", elcname.getText().toString().trim());
        //Log.e(TAG, "loanSignUp: " + mRegistrationRequest.toString());
        String param1 = String.valueOf(BuildConfig.param1);
        String param2 = BuildConfig.param2;
        String param3 = BuildConfig.param3;
        String base = param1 + "|" + param2 + "|" + param3;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);


        apiServiceLoan.getLoanRegister(mRegistrationRequest, authHeader).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Helper.hideLoading();
                Log.e(TAG, "onResponse: " + response.body());


                //{"message":"Submitted Successfully.","error":false,"WalletID":"202310060001"}
                //{"MobileNo":"919830450542","Email":"mdwasim508@gmail.com","Password":"11a45e1e-f32f-31f3-9a01-166a906d0db6","FirstName":"Md","MiddleName":"","LastName":"Wasim","City":"Kolkata","State":"West Bengal","Country":"INDIA","BaseCurrency":"INR","NotificationKey":"f107eae1-a50e-4aec-8f4a-6ca78af8d9c0","AddressLine1":"bankra howrah 711403","AddressLine2":"","CompanyName":"Software Solutions"}

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Helper.hideLoading();

            }
        });


    }

    private void setValues() {
        String senderName = Helper.getSenderName();
        String firstName = Helper.getFirstName();
        String lastName = Helper.getLastName();
        String email = Helper.getEmail();
        String address = Helper.getAddress();
        elname1.setText(firstName);
        elname3.setText(lastName);
        elmail.setText(email);
        eladdress.setText(address);


    }

    private void getCityList() {
        String param1 = "2017011900003";
        String param2 = "das123";
        String param3 = "en.corecoop.net";

        String base = param1 + "|" + param2 + "|" + param3;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        JsonObject mParam = new JsonObject();
        String phoneCode = Helper.getPhoneCode();
        mParam.addProperty("CountryCode", phoneCode);
        mParam.addProperty("Language", "en");


        apiServiceLoan.getCityList(mParam, authHeader).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse: city " + response.body());


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {


            }

        });


    }

    private void getCountryList() {
        mLoader = new ProgressDialog(L_SignUpActivity.this);
        mLoader.setMessage(getString(R.string.info_getting_country_code));
        mLoader.show();
        mainAPIInterface.getCountryCode().enqueue(new Callback<CountryCodeResponse>() {
            @Override
            public void onResponse(Call<CountryCodeResponse> call, Response<CountryCodeResponse> response) {
                mLoader.dismiss();
                //Log.e(TAG, "onResponse: " + response.body().getResult());
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        mListCountry = new ArrayList<>();
                        mListCountry = response.body().getResult();
                        disableSelectCountry();
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryCodeResponse> call, Throwable t) {
                mLoader.dismiss();
            }
        });
    }


    private void disableSelectCountry() {
        String countryId = Helper.getCountryId();
        //Log.e(TAG, "disableSelectCountry: " + countryId);
        for (int i = 0; i < mListCountry.size(); i++) {
            if (mListCountry.get(i).getId() == Integer.parseInt(countryId)) {
                String country = " ( " + mListCountry.get(i).getCountrycode() + " )  " + mListCountry.get(i).getCountryname();
                mCountryName = mListCountry.get(i).getCountryname();
                elcountry.setText("Selected Country : " + country);
                mListState = new ArrayList<>();
                mListState = mListCountry.get(i).getStates();
                break;
            }

        }

        String stateId = Helper.getStateId();

        for (int i = 0; i < mListState.size(); i++) {
            if (mListState.get(i).getId() == Integer.parseInt(stateId)) {
                mStatName = mListState.get(i).getStatename();
                elstatename.setText("Selected State : " + mStatName);
                break;

            }
        }
    }

    @Override
    public void onCommonConfirm() {
        mBottomLoanDialog.dismiss();
        Intent mIntent=new Intent(L_SignUpActivity.this,L_IdentityProofActivity.class);
        startActivity(mIntent);

    }
}