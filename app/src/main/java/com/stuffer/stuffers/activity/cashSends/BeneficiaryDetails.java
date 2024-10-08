package com.stuffer.stuffers.activity.cashSends;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.CalculationListener;
import com.stuffer.stuffers.fragments.dialog.CountryDialogFragment;
import com.stuffer.stuffers.fragments.dialog.DestinationDialog;
import com.stuffer.stuffers.fragments.dialog.ModeDialog;
import com.stuffer.stuffers.models.Country.CountryCodeResponse;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.models.output.DetinationCurrency;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BeneficiaryDetails extends Fragment implements View.OnClickListener {

    private View mView;
    MyTextViewBold cTvTitle;
    private CalculationListener mListener;
    private MyTextView bBtnNext;
    private MyTextViewBold tvTitleTop, tvFetchBank;
    private ArrayList<String> mModeList;
    private ModeDialog mModeDialog;
    private MyTextView tvSendingCurrency, tvPaymentMode, tvDestination, tvDesCurrency;
    private ProgressDialog mLoader;
    List<DetinationCurrency.Result> mListDestination;
    private static final String TAG = "BeneficiaryDetails";
    private DestinationDialog mDestinationDialog;
    private String mPayOut;
    MainAPIInterface apiService;
    private String mCountryNameCode;
    private MyEditText bEdIfsc, bEdFullName, bEdBankName, bEdAcNo, bEdBranch;
    private List<Result> mListCountry;
    private String mPayOutCountry;
    private String mDestinationCountry;

    public BeneficiaryDetails() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCountryNameCode = getArguments().getString(AppoConstants.COUNTRYNAMECODE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_beneficiary_details, container, false);
        apiService = ApiUtils.getAPIService();
        tvTitleTop = mView.findViewById(R.id.tvTitleTop);
        tvFetchBank = mView.findViewById(R.id.tvFetchBank);
        cTvTitle = mView.findViewById(R.id.cTvTitle);
        tvSendingCurrency = mView.findViewById(R.id.tvSendingCurrency);
        tvDestination = mView.findViewById(R.id.tvDestination);
        tvDesCurrency = mView.findViewById(R.id.tvDesCurrency);
        tvPaymentMode = mView.findViewById(R.id.tvPaymentMode);
        bEdIfsc = mView.findViewById(R.id.bEdIfsc);
        bEdFullName = mView.findViewById(R.id.bEdFullName);
        bEdBankName = mView.findViewById(R.id.bEdBankName);
        bEdAcNo = mView.findViewById(R.id.bEdAcNo);
        bEdBranch = mView.findViewById(R.id.bEdBranch);


        tvPaymentMode.setOnClickListener(this);
        bBtnNext = mView.findViewById(R.id.bBtnNext);
        bBtnNext.setOnClickListener(this);
        tvFetchBank.setOnClickListener(this);
        tvDestination.setOnClickListener(this);
        cTvTitle.setText(Html.fromHtml("<u>" + getString(R.string.info_benifi_details) + "</u>"));
        tvTitleTop.setText(Html.fromHtml("<u>" + getString(R.string.info_destination_details) + "</u>"));
        setDetails();
        getDestinationCurrency();
        //getCountryList();
        return mView;
    }


    private void getCountryList() {
        showLoading(getString(R.string.info_getting_country_code));

        apiService.getCountryCode().enqueue(new Callback<CountryCodeResponse>() {
            @Override
            public void onResponse(Call<CountryCodeResponse> call, Response<CountryCodeResponse> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        mListCountry = new ArrayList<>();
                        mListCountry = response.body().getResult();
                        for (int i = 0; i < mListCountry.size(); i++) {
                            if (mDestinationCountry.equalsIgnoreCase(mListCountry.get(i).getCountryname())) {
                                mPayOutCountry = mListCountry.get(i).getCountrycode();
                                break;
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<CountryCodeResponse> call, Throwable t) {
                hideLoading();
            }
        });
    }

    public void showLoading(String message) {
        if (mLoader == null) {
            mLoader = new ProgressDialog(getActivity());
        }
        mLoader.setMessage(message);
        mLoader.show();
    }

    public void hideLoading() {
        if (mLoader != null && mLoader.isShowing()) {
            mLoader.dismiss();
        }
    }

    private void getDestinationCurrency() {
        showLoading(getString(R.string.info_please_wait_dots));
        //AndroidNetworking.get("http://3.140.192.123:8080/api/appopay/country-with-currency")
        AndroidNetworking.get("https://api-prod.cashsends.com:8080/api/appopay/country-with-currency")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        try {
                            JSONArray result = response.getJSONArray("result");
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<DetinationCurrency.Result>>() {
                            }.getType();
                            mListDestination = new ArrayList<>();
                            List<DetinationCurrency.Result> mListTemp = gson.fromJson(String.valueOf(result), type);
                            for (int i = 0; i < mListTemp.size(); i++) {
                                if (mListTemp.get(i).getModalities().equalsIgnoreCase("All Banks")) {
                                    DetinationCurrency.Result result1 = mListTemp.get(i);
                                    mListDestination.add(result1);
                                }
                            }
                            String country = mListDestination.get(0).getCountry();
                            //Log.e(TAG, "onResponse: " + country);
                            //getPaymentMode();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();

                    }
                });

    }

    private void setDetails() {
        String currencySymbol = Helper.getCurrencySymble();
        tvSendingCurrency.setText(currencySymbol);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CalculationListener) context;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bBtnNext) {
            if (tvDestination.getText().toString().trim().isEmpty()) {
                Helper.showLongMessage(getActivity(), "PLEASE select Destination Country");
                return;
            }
            if (bEdFullName.getText().toString().trim().isEmpty()) {
                bEdFullName.setError("enter name as on bank");
                bEdFullName.requestFocus();
                return;
            }
            if (bEdBankName.getText().toString().trim().isEmpty()) {
                bEdBankName.setError("enter bank name");
                bEdBankName.requestFocus();
                return;
            }
            if (bEdAcNo.getText().toString().trim().isEmpty()) {
                bEdAcNo.setError("enter bank account no");
                bEdAcNo.requestFocus();
                return;
            }
            if (bEdBranch.getText().toString().trim().isEmpty()) {
                bEdBranch.setError("enter branch name");
                bEdBranch.requestFocus();
                return;
            }


            mListener.onCalculationRequest(Helper.getCurrencySymble(), mPayOut,
                    bEdFullName.getText().toString().trim(),
                    bEdBankName.getText().toString().trim(),
                    bEdAcNo.getText().toString().trim(), bEdBranch.getText().toString().trim(), bEdIfsc.getText().toString().trim(), mPayOutCountry);

        } else if (view.getId() == R.id.tvPaymentMode) {
            showModeDialog();
        } else if (view.getId() == R.id.tvDestination) {
            showDestinationCountry();
        } else if (view.getId() == R.id.tvFetchBank) {
            if (bEdIfsc.getText().toString().trim().isEmpty()) {
                bEdIfsc.setError("enter ifsc code");
                return;
            }
            JSONObject mRequestIFSCbody = new JSONObject();
            try {
                mRequestIFSCbody.put("bankName", "");
                mRequestIFSCbody.put("branchIfsc", bEdIfsc.getText().toString().trim());
                mRequestIFSCbody.put("branchName", "");
                mRequestIFSCbody.put("city", "");
                mRequestIFSCbody.put("countryCode", mPayOutCountry);
                getBankDetailsByIFSC(mRequestIFSCbody);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void getBankDetailsByIFSC(JSONObject mRequestIFSCbody) {
        showLoading(getString(R.string.info_please_wait_dots));
        //AndroidNetworking.post("http://3.140.192.123:8080/api/transfer/getBankNetworkList")
        AndroidNetworking.post("https://api-prod.cashsends.com:8080/api/transfer/getBankNetworkList")
                .addJSONObjectBody(mRequestIFSCbody)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                Helper.hideKeyboard(bEdBranch, getActivity());
                Log.e(TAG, "onResponse: " + response);
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                    JSONObject mResultObj = result.getJSONObject(0);
                    bEdBankName.setText(mResultObj.getString("bankName"));
                    bEdBranch.setText(mResultObj.getString("branchName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                hideLoading();
                Log.e(TAG, "onError: " + anError.getErrorBody());
                String errorBody = anError.getErrorBody();
                try {
                    JSONObject mBody = new JSONObject(errorBody);
                    if (mBody.has(AppoConstants.MESSAGE)) {
                        Helper.showLongMessage(getActivity(), mBody.getString(AppoConstants.MESSAGE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "onError: " + anError.getErrorDetail());
            }
        });

    }

    private void showDestinationCountry() {
        mDestinationDialog = new DestinationDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppoConstants.COUNTRY, (ArrayList<? extends Parcelable>) mListDestination);
        mDestinationDialog.setArguments(bundle);
        mDestinationDialog.setCancelable(false);
        mDestinationDialog.show(getChildFragmentManager(), mDestinationDialog.getTag());
    }

    public void showModeDialog() {
        mModeList = new ArrayList<String>();
        mModeList.add("Credit to Account");
        mModeList.add("Credit to Account-Any Bank");
        mModeList.add("Real Time Credit");

        mModeDialog = new ModeDialog();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.TITLE, "Select Payment Mode");
        bundle.putStringArrayList(AppoConstants.INFO, mModeList);
        mModeDialog.setArguments(bundle);
        mModeDialog.setCancelable(false);
        mModeDialog.show(getChildFragmentManager(), mModeDialog.getTag());
    }

    public void hideModeDialog(int pos) {
        if (mModeDialog != null) {
            mModeDialog.dismiss();
            mModeDialog = null;
        }
        tvPaymentMode.setText(mModeList.get(pos));
    }

    public void hideDestinationDialog(String region, String country, String payoutCurrency) {
        if (mDestinationDialog != null)
            mDestinationDialog.dismiss();
        Log.e(TAG, "hideDestinationDialog: " + region);
        mDestinationCountry = country;
        mPayOut = payoutCurrency;
        tvDesCurrency.setText("Payout Currency : " + payoutCurrency);
        tvDestination.setText(country);
        if (mListCountry == null) {
            getCountryList();
        } else {
            for (int i = 0; i < mListCountry.size(); i++) {
                if (country.equalsIgnoreCase(mListCountry.get(i).getCountryname())) {
                    mPayOutCountry = mListCountry.get(i).getCountrycode();
                    break;
                }
            }
        }

    }
}