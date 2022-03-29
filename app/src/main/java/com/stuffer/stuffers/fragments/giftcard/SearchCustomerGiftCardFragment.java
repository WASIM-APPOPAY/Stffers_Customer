package com.stuffer.stuffers.fragments.giftcard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.contact.ContactDemoActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.adapter.recyclerview.ActiveAccountAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.UserAccountTransferListener;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class SearchCustomerGiftCardFragment extends Fragment {
    private static final String TAG = "SearchCustomerGiftCardF";
    private View mView;
    MyButton btnSearch;
    private CountryCodePicker edtCustomerCountryCode;
    private MyEditText edtphone_number;
    private ImageView ivContactList;
    private RecyclerView rvActiveAccounts;
    private MainAPIInterface mainAPIInterface;
    private ProgressDialog dialog;
    private String recieverareacode;
    private String recivermobilenumber;
    private JSONObject indexUser;
    private String currencyResponse;
    private List<CurrencyResult> resultCurrency;
    private ArrayList<AccountModel> mListAccount;
    private String recivername;
    private String reciveruserid;
    private UserAccountTransferListener mListener;
    private String reciveremail;

    public SearchCustomerGiftCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_search_customer_gift_card, container, false);
        btnSearch = mView.findViewById(R.id.btnSearch);
        edtCustomerCountryCode = mView.findViewById(R.id.edtCustomerCountryCode);
        edtphone_number = mView.findViewById(R.id.edtphone_number);
        ivContactList = mView.findViewById(R.id.ivContactList);
        rvActiveAccounts = mView.findViewById(R.id.rvActiveAccount);
        mainAPIInterface = ApiUtils.getAPIService();
        String defaultCountryCode = edtCustomerCountryCode.getDefaultCountryCode();
//        Log.e(TAG, "onCreateView: " + defaultCountryCode);
        rvActiveAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
        edtCustomerCountryCode.setExcludedCountries(getString(R.string.info_exclude_countries));
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtphone_number.getText().toString().trim().isEmpty()) {
                    edtphone_number.setError(getString(R.string.info_enter_phone_number));
                    edtphone_number.requestFocus();
                    return;
                }

                if (edtphone_number.getText().toString().trim().length() < 8) {
                    edtphone_number.setError(getString(R.string.info_enter_valid_phone_number));
                    edtphone_number.requestFocus();
                    return;
                }

                String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                try {
                    JSONObject toUserObject = new JSONObject(vaultValue);
                    JSONObject result = toUserObject.getJSONObject(AppoConstants.RESULT);
                    if (result.getString(AppoConstants.MOBILENUMBER).equalsIgnoreCase(edtphone_number.getText().toString().trim())) {
                        Helper.hideKeyboard(btnSearch, getContext());
                        Helper.showCommonErrorDialog(getContext(), getString(R.string.mobile_no_error), getString(R.string.mobile_no_error_info));
                    } else {
//                        Log.e(TAG, "onClick: " + edtCustomerCountryCode.getSelectedCountryCode());
                        Helper.hideKeyboard(btnSearch, getContext());
                        onSearchRequest(edtphone_number.getText().toString().trim(), edtCustomerCountryCode.getSelectedCountryCode());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ivContactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentContact = new Intent(getContext(), ContactDemoActivity.class);
                startActivityForResult(intentContact, 209);
            }
        });

        return mView;
    }

    private void onSearchRequest(String ph, String area) {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_getting_user_account));
        dialog.show();

        recieverareacode = area;
        recivermobilenumber = ph;

        String accessToken = DataVaultManager.getInstance(getContext()).getVaultValue(KEY_ACCESSTOKEN);
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        mainAPIInterface.getProfileDetails(Long.parseLong(ph), Integer.parseInt(area), bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
//                    Log.e(TAG, "onResponse: getprofile :" + res);
                    try {
                        indexUser = new JSONObject(res);
                        if (indexUser.isNull("result")) {
//                            Log.e(TAG, "onResponse: " + true);
                            Toast.makeText(getContext(), getString(R.string.error_user_details_not_exists), Toast.LENGTH_SHORT).show();
                        } else {
                            getCurrency();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 400) {
                        Toast.makeText(getContext(), getString(R.string.error_bad_request), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
//                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private void getCurrency() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_getting_currency_code));
        dialog.show();

        mainAPIInterface.getCurrencyResponse().enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    currencyResponse = new Gson().toJson(response.body().getResult());
//                    Log.e(TAG, "onResponse: =========" + currencyResponse);
                    resultCurrency = response.body().getResult();
                    readUserAccounts();
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                dialog.dismiss();
//                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }


    private void readUserAccounts() {
        mListAccount = new ArrayList<>();
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);

//        Log.e(TAG, "readUserAccounts: " + vaultValue);
        String nameWithMobile = null;
        try {
            JSONObject root = indexUser;
            JSONObject objResult = root.getJSONObject(AppoConstants.RESULT);
            nameWithMobile = objResult.getString(AppoConstants.FIRSTNAME) + " " + objResult.getString(AppoConstants.LASTNAME) + "-" + objResult.getString(AppoConstants.MOBILENUMBER);
            //tvUserMobileName.setText(nameWithMobile);
            recivername = objResult.getString(AppoConstants.FIRSTNAME) + " " + objResult.getString(AppoConstants.LASTNAME);
            reciveruserid = objResult.getString(AppoConstants.ID);
            reciveremail = objResult.getString(AppoConstants.EMIAL);

            JSONObject objCustomerDetails = objResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            JSONArray arrCustomerAccount = objCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            //for (int i = 0; i < arrCustomerAccount.length(); i++) {
            for (int i = 0; i < 1; i++) {
                JSONObject index = arrCustomerAccount.getJSONObject(i);
                AccountModel model = new AccountModel();
                model.setAccountnumber(index.getString(AppoConstants.ACCOUNTNUMBER));
                model.setAccountEncrypt(null);
                if (index.has(AppoConstants.ACCOUNTSTATUS)) {
//                    Log.e(TAG, "readUserAccounts: AccountStatus : " + index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                    model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                    model.setCurrentbalance(index.getString(AppoConstants.CURRENTBALANCE));
                    mListAccount.add(model);
                }

            }

            if (mListAccount.size() > 0) {
                ActiveAccountAdapter activeAccountAdapter = new ActiveAccountAdapter(getContext(), mListAccount, nameWithMobile);
                rvActiveAccounts.setAdapter(activeAccountAdapter);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getCurrency(String param) {
        String res = null;
        for (int i = 0; i < resultCurrency.size(); i++) {
            String sid = resultCurrency.get(i).getId().toString();
            if (sid.equals(param)) {

                res = resultCurrency.get(i).getCurrencyCode();
                break;
            }
        }
        return res;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (UserAccountTransferListener) context;
    }

    public void getBaseConversion(int pos) {
        getConversionBaseRate(pos);
    }

    private void getConversionBaseRate(int pos) {
        sentParam(indexUser, pos);
    }

    private void sentParam(JSONObject response, int pos) {
        JSONObject objReceiver = new JSONObject();
        try {
            objReceiver.put(AppoConstants.RECIEVERACCOUNTNUMBER, mListAccount.get(pos).getAccountnumber());
            objReceiver.put(AppoConstants.FROMCURRENCY, mListAccount.get(pos).getCurrencyid());
            objReceiver.put(AppoConstants.FROMCURRENCYCODE, mListAccount.get(pos).getCurrencyCode());
            objReceiver.put(AppoConstants.RECEIVERMOBILENUMBER, recivermobilenumber);
            objReceiver.put(AppoConstants.RECEIVERAREACODE, recieverareacode);
            objReceiver.put(AppoConstants.RECIEVERNAME, recivername);
            objReceiver.put(AppoConstants.RECIEVERUSERID, reciveruserid);
            objReceiver.put(AppoConstants.EMIAL, reciveremail);
            objReceiver.put(AppoConstants.COUNTRYID,Helper.getCountryId(response));
            mListener.onAccountTransfer(objReceiver, resultCurrency, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}