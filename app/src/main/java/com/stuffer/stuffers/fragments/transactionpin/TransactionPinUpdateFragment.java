package com.stuffer.stuffers.fragments.transactionpin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
public class TransactionPinUpdateFragment extends Fragment {
    private static final String TAG = "TransactionPinUpdateFra";
    private FragmentReplaceListener mReplaceListener;
    private MyTextView tvHintsTransaction;
    private MyEditText edtOldTransactionPin, edtNewTransactionPin;
    private MyTextView tvSubmit;
    private String phonecode, mobilenumber, phwithcode, info;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private AlertDialog dialogPayment;

    public TransactionPinUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_pin_update, container, false);
        mainAPIInterface = ApiUtils.getAPIService();
        edtOldTransactionPin = view.findViewById(R.id.edtOldTransactionPin);
        edtNewTransactionPin = view.findViewById(R.id.edtNewTransactionPin);
        tvHintsTransaction = view.findViewById(R.id.tvHintsTransaction);
        tvSubmit = view.findViewById(R.id.tvSubmit);
        Bundle arguments = this.getArguments();
        phonecode = arguments.getString(AppoConstants.PHONECODE);
        mobilenumber = arguments.getString(AppoConstants.MOBILENUMBER);
        phwithcode = arguments.getString(AppoConstants.PHWITHCODE);
        info = arguments.getString(AppoConstants.INFO);
        String infoTransaction = "<font color='#00baf2'>" + getString(R.string.trans_note) + "</font>" + " : " + getString(R.string.trans_info);
        tvHintsTransaction.setText(Html.fromHtml(infoTransaction));
        edtOldTransactionPin.setText(info);

        //Log.e(TAG, "onCreateView: phonecode :: " + phonecode);
        //Log.e(TAG, "onCreateView: mobilenumber :: " + mobilenumber);
        //Log.e(TAG, "onCreateView: phwithcode :: " + phwithcode);
        //Log.e(TAG, "onCreateView: info old screen lock : " + info);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtOldTransactionPin.getText().toString().trim().isEmpty()) {
                    edtOldTransactionPin.setError(getString(R.string.info_old_transaction_pin));
                    edtOldTransactionPin.requestFocus();
                    return;
                }

                if (edtOldTransactionPin.getText().toString().trim().length() < 6) {
                    edtOldTransactionPin.setError(getString(R.string.info_transaction_pin_length));
                    edtOldTransactionPin.requestFocus();
                    return;
                }

                if (edtNewTransactionPin.getText().toString().trim().isEmpty()) {
                    edtNewTransactionPin.setError(getString(R.string.info_new_transaction_pin));
                    edtNewTransactionPin.requestFocus();
                    return;
                }

                if (edtNewTransactionPin.getText().toString().trim().length() < 6) {
                    edtNewTransactionPin.setError(getString(R.string.info_transaction_pin_length));
                    edtNewTransactionPin.requestFocus();
                    return;
                }

                if (!edtNewTransactionPin.getText().toString().trim().equalsIgnoreCase(edtOldTransactionPin.getText().toString().trim())) {
                    Toast.makeText(getContext(), "pin mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }
                Helper.hideKeyboard(edtOldTransactionPin, getContext());
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
            //sentIndex.addProperty(AppoConstants.TRANSACTIONPIN, jsonResult.getString(AppoConstants.TRANSACTIONPIN));
            sentIndex.addProperty(AppoConstants.TRANSACTIONPIN, edtNewTransactionPin.getText().toString().trim());
            sentIndex.addProperty(AppoConstants.PHONECODE, jsonResult.getString(AppoConstants.PHONECODE));

            sentIndex.addProperty(AppoConstants.USERTYPE, "CUSTOMER");
            sentIndex.addProperty(AppoConstants.STORENAME, (String) null);
            sentIndex.addProperty(AppoConstants.LATITUDE, 0);
            sentIndex.addProperty(AppoConstants.LONGITUDE, 0);
            sentIndex.addProperty(AppoConstants.SECURITYANSWER, "dollar_sent");
            //sentIndex.addProperty(AppoConstants.SCREENLOCKPIN, edtNewScreenPin.getText().toString().trim());
            sentIndex.addProperty(AppoConstants.SCREENLOCKPIN, (String) null);
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
            sentJsonCustomerDetails.addProperty(AppoConstants.COUNTRYID, jsonCustomerDetails.getString(AppoConstants.COUNTRYID));
            sentJsonCustomerDetails.addProperty(AppoConstants.STATEID, jsonCustomerDetails.getString(AppoConstants.STATEID));
            sentJsonCustomerDetails.addProperty(AppoConstants.ADDRESS, jsonCustomerDetails.getString(AppoConstants.ADDRESS));
            sentJsonCustomerDetails.addProperty(AppoConstants.CITYNAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ZIPCODE2, (String) null);

            sentJsonCustomerDetails.addProperty(AppoConstants.DOB, jsonCustomerDetails.getString(AppoConstants.DOB));
            sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYID, Helper.getCurrencyId());
            sentJsonCustomerDetails.addProperty(AppoConstants.MONTHLYINCOME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.PASSPORTNUMBER, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.EXPIRYDATE, jsonCustomerDetails.getString(AppoConstants.EXPIRYDATE));
            sentJsonCustomerDetails.addProperty(AppoConstants.IDTYPE, jsonCustomerDetails.getString(AppoConstants.IDTYPE));
            sentJsonCustomerDetails.addProperty(AppoConstants.IDNUMBER, jsonCustomerDetails.getString(AppoConstants.IDNUMBER));

            sentJsonCustomerDetails.addProperty(AppoConstants.BANKACCOUNT, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IMAGEURL, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.BANKUSERNAME, (String) null);

            sentJsonCustomerDetails.addProperty(AppoConstants.BANKUSERNAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.MERCHANTQRCODE, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISDEAL, (String) null);
            if (Helper.getCurrencyId().equalsIgnoreCase("1")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "USD");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("2")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "INR");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("3")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "CAD");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("4")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "ERU");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("5")){
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "DOP");
            }
            sentJsonCustomerDetails.addProperty(AppoConstants.IDCUENTA, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IDASOCIADO, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISPLASTICO, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.SOURCEOFINCOME, (String) null);



            JsonArray sentJsonArrayCustomerAccounts = new JsonArray();
            JSONArray jsonArrayCustomerAccount = jsonCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);


            for (int i = 0; i < jsonArrayCustomerAccount.length(); i++) {
                JSONObject jsonObjectIndex = jsonArrayCustomerAccount.getJSONObject(i);
                JsonObject jsonObjectAccount = new JsonParser().parse(jsonObjectIndex.toString()).getAsJsonObject();
                sentJsonArrayCustomerAccounts.add(jsonObjectAccount);
            }
            sentJsonCustomerDetails.add(AppoConstants.CUSTOMERACCOUNTS, sentJsonArrayCustomerAccounts);
            sentIndex.add(AppoConstants.CUSTOMERDETAILS, sentJsonCustomerDetails);
            //Log.e(TAG, "updateUserProfile: " + sentIndex);
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

        mainAPIInterface.postUpdateUserProfile(sentIndex, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Request Successfully updated", Toast.LENGTH_SHORT).show();
                    onUpdateProfile();
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 400) {
                        //Log.e(TAG, "onResponse: bad request");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private void onUpdateProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_success_topup, null);
        MyTextView tvSuccess = dialogLayout.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Thank You!\nYour Transaction pin has been updated Successfully");
        MyTextView tvTitleCommon = dialogLayout.findViewById(R.id.tvTitleCommon);
        tvTitleCommon.setText(getString(R.string.info_transaction_pin));
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //invalidateDetails();
                onOkPress();
            }
        });

        builder.setView(dialogLayout);

        dialogPayment = builder.create();

        dialogPayment.setCanceledOnTouchOutside(false);

        dialogPayment.show();
    }

    public void onOkPress() {
        if (dialogPayment != null)
            dialogPayment.dismiss();
        DataVaultManager.getInstance(getContext()).saveUserDetails("");
        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
        DataVaultManager.getInstance(getContext()).saveCardToken("");
        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mReplaceListener = (FragmentReplaceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent should implement FragmentReplaceListener");
        }

    }
}
