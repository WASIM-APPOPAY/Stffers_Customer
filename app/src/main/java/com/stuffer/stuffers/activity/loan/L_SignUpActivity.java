package com.stuffer.stuffers.activity.loan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignupAcitivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.api.MainLoanInterface;
import com.stuffer.stuffers.models.Country.CountryCodeResponse;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.models.Country.State;
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

public class L_SignUpActivity extends AppCompatActivity {
    private static final String TAG = "L_SignUpActivity";

    private MyEditText elname1, elname2, elname3, elmail, elcname, eladdress, elcityname, elstatename, elcountry;
    private ProgressDialog mLoader;
    private MainAPIInterface mainAPIInterface;
    List<Result> mListCountry;
    private List<State> mListState;
    private MainLoanInterface apiServiceLoan;

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


        DeviceUuidFactory mDeviceUuidFactory = new DeviceUuidFactory(L_SignUpActivity.this);
        UUID deviceUuid = mDeviceUuidFactory.getDeviceUuid();
        Log.e(TAG, "onCreate: " + deviceUuid);
        //String senderName = Helper.getSenderName();
        //Log.e(TAG, "onCreate: " + Helper.getUserDetails());
        //Log.e(TAG, "onCreate: " + senderName);
        String firstName = Helper.getFirstName();
        String lastName = Helper.getLastName();
        String email = Helper.getEmail();
        String address = Helper.getAddress();
        //getCityList();
        getCountryList();


    }
    /*curl --location 'https://prodapi.appopay.com/api/wallet/topup/v2' \
            --header 'Content-Type: application/json' \
            --data '{
            "amount": "5.0",
            "carrier": "Movistar",
            "charges": 0.0,
            "fees": 0.0,
            "taxes": 0.35,
            "fromcurrency": "1",
            "fromcurrencycode": "INR",
            "originalAmount": 5.35,
            "payamount": 5.35,
            "productcode": "8011",
            "recieverareacode": "507",
            "recievermobilernumber": "63516303",
            "senderaccountnumber": "399905710004896549945",
            "senderareacode": "91",
            "sendermobilenumber": "9836683269",
            "sendername": "MD WASIM",
            "transactionpin": "222222",
            "userid": "684"
}'*/
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
        Log.e(TAG, "disableSelectCountry: " + countryId);
        for (int i = 0; i < mListCountry.size(); i++) {
            if (mListCountry.get(i).getId() == Integer.parseInt(countryId)) {
                String country = " ( " + mListCountry.get(i).getCountrycode() + " )  " + mListCountry.get(i).getCountryname();
                elcountry.setText("Selected Country : " + country);
                //mCountyId = mListCountry.get(i).getId();
                mListState = new ArrayList<>();
                mListState = mListCountry.get(i).getStates();
                break;
            }

        }

        String stateId = Helper.getStateId();

        for (int i = 0; i < mListState.size(); i++) {
            if (mListState.get(i).getId() == Integer.parseInt(stateId)) {
                String statename = mListState.get(i).getStatename();
                elstatename.setText("Selected State : " + statename);
                break;

            }
        }
    }
}