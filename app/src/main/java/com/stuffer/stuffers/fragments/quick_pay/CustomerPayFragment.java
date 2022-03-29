package com.stuffer.stuffers.fragments.quick_pay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.contact.ContactDemoActivity;
import com.stuffer.stuffers.activity.wallet.QuickPaymentActivity;
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
import com.stuffer.stuffers.views.MyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerPayFragment extends Fragment {
    private static final String TAG = "CustomerPayFragment";
    private MyEditText edtphone_number;
    private MyButton btnSearch, btnChange;
    private MainAPIInterface mainAPIInterface;
    private ProgressDialog dialog;
    CountryCodePicker edtCustomerCountryCode;
    private JSONObject indexUser;
    private List<CurrencyResult> resultCurrency;
    private ArrayList<AccountModel> mListAccount;
    private UserAccountTransferListener mListener;
    private String currencyResponse;
    private RecyclerView rvActiveAccounts;
    private String recivername;
    String recieverareacode;
    String recivermobilenumber;
    private String reciveruserid;
    private int fromPosition;
    private ImageView ivContactList;
    private String reciveremail;
    private PhoneNumberUtil phoneUtil;

    public CustomerPayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_pay, container, false);
        btnChange = view.findViewById(R.id.btnChange);

        edtCustomerCountryCode = view.findViewById(R.id.edtCustomerCountryCode);
        edtphone_number = view.findViewById(R.id.edtphone_number);
        ivContactList = view.findViewById(R.id.ivContactList);
        rvActiveAccounts = view.findViewById(R.id.rvActiveAccount);


        btnSearch = view.findViewById(R.id.btnSearch);
        mainAPIInterface = ApiUtils.getAPIService();
        String defaultCountryCode = edtCustomerCountryCode.getDefaultCountryCode();
        //Log.e(TAG, "onCreateView: " + defaultCountryCode);
        rvActiveAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
        btnChange.setVisibility(View.GONE);
        edtCustomerCountryCode.setExcludedCountries(getString(R.string.info_exclude_countries));
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAccountTransfer(null, null, null);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtphone_number.getText().toString().trim().isEmpty()) {
                    //edtphone_number.setError("Please enter phone number");
                    edtphone_number.setError(getString(R.string.info_please_enter_phone_number));
                    edtphone_number.requestFocus();
                    return;
                }

                if (edtphone_number.getText().toString().trim().length() < 8) {
                    //edtphone_number.setError("Please enter valid phone number");
                    edtphone_number.setError(getString(R.string.info_enter_valid_phone_number));
                    edtphone_number.requestFocus();
                    return;
                }


                String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
                try {
                    JSONObject toUserObject = new JSONObject(vaultValue);
                    JSONObject result = toUserObject.getJSONObject(AppoConstants.RESULT);
                    if (result.getString(AppoConstants.MOBILENUMBER).equalsIgnoreCase(edtphone_number.getText().toString().trim())) {
                        Helper.hideKeyboard(btnChange, getContext());
                        Helper.showCommonErrorDialog(getContext(), getString(R.string.mobile_no_error), getString(R.string.mobile_no_error_info));
                    } else {
                        //Log.e(TAG, "onClick: " + edtCustomerCountryCode.getSelectedCountryCode());
                        Helper.hideKeyboard(btnChange, getContext());
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
                Intent intentContact = new Intent(getActivity(), ContactDemoActivity.class);
                startActivityForResult(intentContact, AppoConstants.PICK_CONTACT);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppoConstants.PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                //Log.e(TAG, "onActivityResult: Pick Contact NUmber :: " + data.getStringExtra(AppoConstants.INFO));
                String mMobileNumber = data.getStringExtra(AppoConstants.INFO);
                edtphone_number.setText(mMobileNumber);
                try {
                    // phone must begin with '+'
                    if (phoneUtil == null) {
                        phoneUtil = PhoneNumberUtil.createInstance(getActivity());
                    }
                    Phonenumber.PhoneNumber numberProto = phoneUtil.parse(mMobileNumber, "");
                    int countryCode = numberProto.getCountryCode();
                    long nationalNumber = numberProto.getNationalNumber();
                    edtphone_number.setText(String.valueOf(nationalNumber));
                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
                }
            }
        }
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
                    //Log.e(TAG, "onResponse: getprofile :" + res);
                    try {
                        indexUser = new JSONObject(res);
                        if (indexUser.isNull("result")) {
                            //Log.e(TAG, "onResponse: " + true);
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
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
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
                    //Log.e(TAG, "onResponse: =========" + currencyResponse);
                    resultCurrency = response.body().getResult();
                    readUserAccounts();
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }

    private void readUserAccounts() {
        mListAccount = new ArrayList<>();
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);

        //Log.e(TAG, "readUserAccounts: " + vaultValue);
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
           // for (int i = 0; i < arrCustomerAccount.length(); i++) {
            for (int i = 0; i < 1; i++) {
                JSONObject index = arrCustomerAccount.getJSONObject(i);
                AccountModel model = new AccountModel();
                model.setAccountnumber(index.getString(AppoConstants.ACCOUNTNUMBER));
                model.setAccountEncrypt(null);
                if (index.has(AppoConstants.ACCOUNTSTATUS)) {
                    //Log.e(TAG, "readUserAccounts: AccountStatus : " + index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                    model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                    model.setCurrentbalance(index.getString(AppoConstants.CURRENTBALANCE));
                    mListAccount.add(model);
                } /*else {
                    //Log.e(TAG, "readUserAccounts: AccountStatus : " + "null");
                    model.setAccountstatus("");
                }*/

            }

            if (mListAccount.size() > 0) {

                //tvAccounts.setText("(" + mListAccount.get(0).getAccountnumber() + "-" + mListAccount.get(0).getCurrencyCode() + ")");
                //cardUser.setVisibility(View.VISIBLE);
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
        fromPosition = pos;
        ////https://api.exchangeratesapi.io/latest?base=USD
        String url = "https://api.exchangeratesapi.io/latest?base=" + mListAccount.get(pos).getCurrencyCode();
        //Log.e(TAG, "getConversionBaseRate: url :: " + url);
        /*dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait");
        dialog.show();
        AndroidNetworking.get(url)
                .setOkHttpClient(AppoPayApplication.getOkHttpClient(10))
                .setPriority(Priority.IMMEDIATE)
                .setTag("base conversion")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        //Log.e(TAG, "onResponse: base :: " + response);
                        if (response.has("base")) {
                            //Log.e(TAG, "onResponse: true");
                            sentParam(response);

                        } else {
                            //Log.e(TAG, "onResponse: false");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                    }
                });*/

        sentParam(null);


    }

    private void sentParam(JSONObject response) {
        JSONObject objReceiver = new JSONObject();
        try {
            objReceiver.put(AppoConstants.RECIEVERACCOUNTNUMBER, mListAccount.get(fromPosition).getAccountnumber());
            objReceiver.put(AppoConstants.FROMCURRENCY, mListAccount.get(fromPosition).getCurrencyid());
            objReceiver.put(AppoConstants.FROMCURRENCYCODE, mListAccount.get(fromPosition).getCurrencyCode());
            objReceiver.put(AppoConstants.RECEIVERMOBILENUMBER, recivermobilenumber);
            objReceiver.put(AppoConstants.RECEIVERAREACODE, recieverareacode);
            objReceiver.put(AppoConstants.RECIEVERNAME, recivername);
            objReceiver.put(AppoConstants.RECIEVERUSERID, reciveruserid);
            objReceiver.put(AppoConstants.EMIAL,reciveremail);
            //Log.e(TAG, "sentParam: " + objReceiver.toString());
            //mListener.onAccountTransfer(objReceiver, resultCurrency, response);
            mListener.onAccountTransfer(objReceiver, resultCurrency, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
