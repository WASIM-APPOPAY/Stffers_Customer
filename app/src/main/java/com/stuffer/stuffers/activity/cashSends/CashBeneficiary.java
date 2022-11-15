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
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
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


public class CashBeneficiary extends Fragment implements View.OnClickListener {

    private View mView;
    MyTextViewBold cTvTitle;
    private CalculationListener mListener;
    private MyTextView bBtnNext;
    private MyTextViewBold tvTitleTop;
    private ArrayList<String> mModeList;
    private ModeDialog mModeDialog;
    private MyTextView tvSendingCurrency, tvPaymentMode, tvDestination, tvDesCurrency, bTvNational;
    private ProgressDialog mLoader;
    List<DetinationCurrency.Result> mListDestination;
    private static final String TAG = "BeneficiaryDetails";
    private DestinationDialog mDestinationDialog;
    private String mPayOut;
    MainAPIInterface apiService;
    private String mCountryNameCode;
    private MyEditText bEdFirstName, bEdlastName, bMobileNumber, bEdAddress;
    private List<Result> mListCountry;
    private String mPayOutCountry;
    private String mDestinationCountry;
    private CountryCodePicker bCountryCode;
    private String mNationalityCode;
    private MainAPIInterface mainAPIInterface;


    public CashBeneficiary() {
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
        mainAPIInterface = ApiUtils.getAPIService();
        mView = inflater.inflate(R.layout.fragment_cash_beneficiary, container, false);
        apiService = ApiUtils.getAPIService();
        tvTitleTop = mView.findViewById(R.id.tvTitleTop);
        bCountryCode=mView.findViewById(R.id.bCountryCode);
        cTvTitle = mView.findViewById(R.id.cTvTitle);
        tvSendingCurrency = mView.findViewById(R.id.tvSendingCurrency);
        tvDestination = mView.findViewById(R.id.tvDestination);
        tvDesCurrency = mView.findViewById(R.id.tvDesCurrency);
        tvPaymentMode = mView.findViewById(R.id.tvPaymentMode);
        bEdFirstName = mView.findViewById(R.id.bEdFirstName);
        bEdlastName = mView.findViewById(R.id.bEdlastName);
        bEdAddress = mView.findViewById(R.id.bEdAddress);
        bTvNational = mView.findViewById(R.id.bTvNational);
        bMobileNumber = mView.findViewById(R.id.bMobileNumber);


        tvPaymentMode.setOnClickListener(this);
        bBtnNext = mView.findViewById(R.id.bBtnNext);

        bBtnNext.setOnClickListener(this);
        bTvNational.setOnClickListener(this);
        tvDestination.setOnClickListener(this);
        cTvTitle.setText(Html.fromHtml("<u>" + getString(R.string.info_benifi_details) + "</u>"));
        tvTitleTop.setText(Html.fromHtml("<u>" + getString(R.string.info_destination_details) + "</u>"));
        setDetails();
        getDestinationCurrency();
        //getCountryList();
        return mView;

    }

    private void setDetails() {
        String currencySymbol = Helper.getCurrencySymble();
        tvSendingCurrency.setText(currencySymbol);
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
                            //getCountryList();

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

    /*private void getCountryList() {
        showLoading(getString(R.string.info_getting_country_code));

        mainAPIInterface.getCountryCode().enqueue(new Callback<CountryCodeResponse>() {
            @Override
            public void onResponse(Call<CountryCodeResponse> call, Response<CountryCodeResponse> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        mListCountry = new ArrayList<>();
                        mListCountry = response.body().getResult();
                    }
                }

            }

            @Override
            public void onFailure(Call<CountryCodeResponse> call, Throwable t) {
                hideLoading();
            }
        });
    }*/


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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bBtnNext) {
            if (tvDestination.getText().toString().trim().isEmpty()) {
                Helper.showLongMessage(getActivity(), "PLEASE select Destination Country");
                return;
            }
            if (bEdFirstName.getText().toString().trim().isEmpty()) {
                bEdFirstName.setError("enter first name");
                bEdFirstName.requestFocus();
                return;
            }
            if (bEdlastName.getText().toString().trim().isEmpty()) {
                bEdlastName.setError("enter last name");
                bEdlastName.requestFocus();
                return;
            }
            if (bTvNational.getText().toString().trim().isEmpty()) {
                Toast.makeText(getActivity(), "Please select Nationality", Toast.LENGTH_SHORT).show();
                return;
            }
            if (bMobileNumber.getText().toString().trim().isEmpty()) {
                bMobileNumber.setError("enter mobile number");
                bMobileNumber.requestFocus();
                return;
            }
            if (bEdAddress.getText().toString().trim().isEmpty()) {
                bEdAddress.setError("enter beneficiary address");
                bEdAddress.requestFocus();
                return;
            }
            String mFName = bEdFirstName.getText().toString().trim();
            String mLName = bEdlastName.getText().toString().trim();
            String mFullName = mFName + "," + mLName;
            String selectedCountryCode = bCountryCode.getSelectedCountryCode();
            String mobile = bMobileNumber.getText().toString().trim();
            String mCCNUmber = selectedCountryCode  + mobile;

            mListener.onCashCalculation(Helper.getCurrencySymble(), mPayOut,
                    mFullName,
                    mNationalityCode,
                    mCCNUmber,
                    bEdAddress.getText().toString().trim(),
                    mPayOutCountry);

        } else if (view.getId() == R.id.tvPaymentMode) {
            showModeDialog();
        } else if (view.getId() == R.id.tvDestination) {
            showDestinationCountry();
        } else if (view.getId() == R.id.bTvNational) {
            if (mListCountry != null && mListCountry.size() > 0) {
                CountryDialogFragment countryDialogFragment = new CountryDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppoConstants.COUNTRY, (ArrayList<? extends Parcelable>) mListCountry);
                countryDialogFragment.setArguments(bundle);
                countryDialogFragment.setCancelable(false);
                countryDialogFragment.show(getChildFragmentManager(), countryDialogFragment.getTag());

            } else {
                Log.e(TAG, "onClick: called");
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CalculationListener) context;

    }

    public void setNationality(String countryName, String countryCode, int code, int pos) {
        for (int i = 0; i < mListCountry.size(); i++) {
            if (countryName.equalsIgnoreCase(mListCountry.get(i).getCountryname())) {
                //mCountyId = mListCountry.get(i).getId();
                break;
            }
        }
        bTvNational.setText(countryName);

        mNationalityCode = countryCode;
        bTvNational.setAllCaps(true);

    }
}