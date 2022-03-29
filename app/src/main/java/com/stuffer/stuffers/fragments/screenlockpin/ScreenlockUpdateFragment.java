package com.stuffer.stuffers.fragments.screenlockpin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;

import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenlockUpdateFragment extends Fragment {
    private static final String TAG = "ScreenlockUpdateFragmen";
    MyTextView tvHintsScreenLock, updateProfile;
    String phonecode, mobilenumber, phwithcode, info;
    MyEditText edtOldScreenPin, edtNewScreenPin;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;

    public ScreenlockUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_screenlock_update, container, false);
        mainAPIInterface = ApiUtils.getAPIService();
        edtOldScreenPin = view.findViewById(R.id.edtOldScreenPin);
        edtNewScreenPin = view.findViewById(R.id.edtNewScreenPin);
        updateProfile = view.findViewById(R.id.updateProfile);
        Bundle arguments = this.getArguments();
        phonecode = arguments.getString(AppoConstants.PHONECODE);
        mobilenumber = arguments.getString(AppoConstants.MOBILENUMBER);
        phwithcode = arguments.getString(AppoConstants.PHWITHCODE);
        info = arguments.getString(AppoConstants.INFO);
        tvHintsScreenLock = view.findViewById(R.id.tvHintsScreenLock);
        String infoScreenLock = "<font color='#00baf2'>" + getString(R.string.screen_note) + "</font>" + " : " + getString(R.string.screen_info);
        tvHintsScreenLock.setText(Html.fromHtml(infoScreenLock));
        Log.e(TAG, "onCreateView: phonecode :: " + phonecode);
        Log.e(TAG, "onCreateView: mobilenumber :: " + mobilenumber);
        Log.e(TAG, "onCreateView: phwithcode :: " + phwithcode);
        Log.e(TAG, "onCreateView: info old screen lock : " + info);
        edtOldScreenPin.setText(info);

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtOldScreenPin.getText().toString().trim().isEmpty()) {
                    edtOldScreenPin.setError(getString(R.string.info_old_screen_pin));
                    edtOldScreenPin.requestFocus();
                    return;
                }

                if (edtOldScreenPin.getText().toString().trim().length() < 4) {
                    edtOldScreenPin.setError(getString(R.string.info_screen_pin_length));
                    edtOldScreenPin.requestFocus();
                    return;
                }

                if (edtNewScreenPin.getText().toString().trim().isEmpty()) {
                    edtNewScreenPin.setError(getString(R.string.info_new_screen_pin));
                    edtNewScreenPin.requestFocus();
                    return;
                }

                if (edtNewScreenPin.getText().toString().trim().length() < 4) {
                    edtNewScreenPin.setError(getString(R.string.info_screen_pin_length));
                    edtNewScreenPin.requestFocus();
                    return;
                }

                if (!edtNewScreenPin.getText().toString().trim().equalsIgnoreCase(edtOldScreenPin.getText().toString().trim())) {
                    Toast.makeText(getContext(), "Screen Lock pin mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }
                Helper.hideKeyboard(edtOldScreenPin, getContext());
                if (AppoPayApplication.isNetworkAvailable(getContext())) {
                    updateUserRequest();
                } else {
                    Toast.makeText(getContext(), "" + getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }

    private void updateUserRequest() {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject jsonResult = index.getJSONObject(AppoConstants.RESULT);

            JsonObject sentIndex = new JsonObject();
            sentIndex.addProperty(AppoConstants.ID, jsonResult.getString(AppoConstants.ID));
            sentIndex.addProperty(AppoConstants.FIRSTNAME, jsonResult.getString(AppoConstants.FIRSTNAME));
            sentIndex.addProperty(AppoConstants.LASTNAME, jsonResult.getString(AppoConstants.LASTNAME));
            sentIndex.addProperty(AppoConstants.USERNAME, jsonResult.getString(AppoConstants.USERNAME));
            sentIndex.addProperty(AppoConstants.PASSWORD, jsonResult.getString(AppoConstants.PASSWORD));
            sentIndex.addProperty(AppoConstants.EMIAL, jsonResult.getString(AppoConstants.EMIAL));
            sentIndex.addProperty(AppoConstants.ACCOUNTEXPIRED, jsonResult.getString(AppoConstants.ACCOUNTEXPIRED));
            sentIndex.addProperty(AppoConstants.ACCOUNTLOCKED, jsonResult.getString(AppoConstants.ACCOUNTLOCKED));
            sentIndex.addProperty(AppoConstants.CREDENTIALSEXPIRED, jsonResult.getString(AppoConstants.CREDENTIALSEXPIRED));
            sentIndex.addProperty(AppoConstants.ENABLE, jsonResult.getString(AppoConstants.ENABLE));
            sentIndex.addProperty(AppoConstants.MOBILENUMBER, jsonResult.getString(AppoConstants.MOBILENUMBER));
            sentIndex.addProperty(AppoConstants.TRANSACTIONPIN, jsonResult.getString(AppoConstants.TRANSACTIONPIN));
            sentIndex.addProperty(AppoConstants.PHONECODE, jsonResult.getString(AppoConstants.PHONECODE));

            sentIndex.addProperty(AppoConstants.USERTYPE, (String) null);
            sentIndex.addProperty(AppoConstants.STORENAME, (String) null);
            sentIndex.addProperty(AppoConstants.LATITUDE, 0);
            sentIndex.addProperty(AppoConstants.LONGITUDE, 0);
            sentIndex.addProperty(AppoConstants.SECURITYANSWER, "dollar_sent");
            sentIndex.addProperty(AppoConstants.SCREENLOCKPIN, edtNewScreenPin.getText().toString().trim());
            JsonArray jsonArrayRole = new JsonArray();
            jsonArrayRole.add("USER");

            sentIndex.add(AppoConstants.ROLE, jsonArrayRole);

            JSONObject jsonCustomerDetails = jsonResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);

            JsonObject sentJsonCustomerDetails = new JsonObject();
            sentJsonCustomerDetails.addProperty(AppoConstants.ID, jsonCustomerDetails.getString(AppoConstants.ID));
            sentJsonCustomerDetails.addProperty(AppoConstants.FIRSTNAME, jsonCustomerDetails.getString(AppoConstants.FIRSTNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.LASTNAME, jsonCustomerDetails.getString(AppoConstants.LASTNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.MIDDLENAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.CARDTOKEN, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.COUNTRYID, jsonCustomerDetails.getInt(AppoConstants.COUNTRYID));
            sentJsonCustomerDetails.addProperty(AppoConstants.STATEID, jsonCustomerDetails.getInt(AppoConstants.STATEID));
            sentJsonCustomerDetails.addProperty(AppoConstants.ADDRESS, jsonCustomerDetails.getString(AppoConstants.ADDRESS));
            sentJsonCustomerDetails.addProperty(AppoConstants.CITYNAME, jsonCustomerDetails.getString(AppoConstants.CITYNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.DOB, jsonCustomerDetails.getString(AppoConstants.DOB));
            sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYID, 1);
            sentJsonCustomerDetails.addProperty(AppoConstants.BANKACCOUNT, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IMAGEURL, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.BANKUSERNAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.MERCHANTQRCODE, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISDEAL, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "USD");
            sentJsonCustomerDetails.addProperty(AppoConstants.IDCUENTA, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IDASOCIADO, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISPLASTICO, (String) null);
            JsonArray sentJsonArrayCustomerAccounts = new JsonArray();
            JSONArray jsonArrayCustomerAccount = jsonCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);


            for (int i = 0; i < jsonArrayCustomerAccount.length(); i++) {
                JSONObject jsonObjectIndex = jsonArrayCustomerAccount.getJSONObject(i);
                JsonObject jsonObjectAccount = new JsonParser().parse(jsonObjectIndex.toString()).getAsJsonObject();
                sentJsonArrayCustomerAccounts.add(jsonObjectAccount);
            }
            sentJsonCustomerDetails.add(AppoConstants.CUSTOMERACCOUNTS, sentJsonArrayCustomerAccounts);
            sentIndex.add(AppoConstants.CUSTOMERDETAILS, sentJsonCustomerDetails);
            Log.e(TAG, "updateUserProfile: " + sentIndex);
            processUpdateRequest(sentIndex);

        } catch (JSONException e) {
            e.printStackTrace();

        }


    }

    private void processUpdateRequest(JsonObject sentIndex) {
        String accessToken = DataVaultManager.getInstance(getContext()).getVaultValue(KEY_ACCESSTOKEN);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait, Sending your request.");
        dialog.show();
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);

        mainAPIInterface.putUpdateUserProfile(sentIndex, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: update : " + new Gson().toJson(response.body()));
                    String res = new Gson().toJson(response);
                    //DataVaultManager.getInstance(getContext()).saveUserDetails(res);
                    Toast.makeText(getContext(), "Request Successfully updated", Toast.LENGTH_SHORT).show();
                    onUpdateProfile();
                    //invalidateUserInfo();
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 400) {
                        Log.e(TAG, "onResponse: bad request");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private void onUpdateProfile() {
        DataVaultManager.getInstance(getContext()).saveUserDetails("");
        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
