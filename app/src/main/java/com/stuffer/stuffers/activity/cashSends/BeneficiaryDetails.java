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
import com.stuffer.stuffers.communicator.CalculationListener;
import com.stuffer.stuffers.fragments.dialog.CountryDialogFragment;
import com.stuffer.stuffers.fragments.dialog.DestinationDialog;
import com.stuffer.stuffers.fragments.dialog.ModeDialog;
import com.stuffer.stuffers.models.output.DetinationCurrency;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


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

    public BeneficiaryDetails() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_beneficiary_details, container, false);
        tvTitleTop = mView.findViewById(R.id.tvTitleTop);
        tvFetchBank = mView.findViewById(R.id.tvFetchBank);
        cTvTitle = mView.findViewById(R.id.cTvTitle);
        tvSendingCurrency = mView.findViewById(R.id.tvSendingCurrency);
        tvDestination = mView.findViewById(R.id.tvDestination);
        tvDesCurrency = mView.findViewById(R.id.tvDesCurrency);
        tvPaymentMode = mView.findViewById(R.id.tvPaymentMode);
        tvPaymentMode.setOnClickListener(this);
        bBtnNext = mView.findViewById(R.id.bBtnNext);
        bBtnNext.setOnClickListener(this);
        tvFetchBank.setOnClickListener(this);
        tvDestination.setOnClickListener(this);
        cTvTitle.setText(Html.fromHtml("<u>" + getString(R.string.info_benifi_details) + "</u>"));
        tvTitleTop.setText(Html.fromHtml("<u>" + getString(R.string.info_destination_details) + "</u>"));
        setDetails();
        getDestinationCurrency();
        return mView;
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
        AndroidNetworking.get("http://3.140.192.123:8080/api/appopay/country-with-currency")
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
                            Log.e(TAG, "onResponse: " + country);

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
            mListener.onCalculationRequest(Helper.getCurrencySymble(), mPayOut);
        } else if (view.getId() == R.id.tvPaymentMode) {
            showModeDialog();
        } else if (view.getId() == R.id.tvDestination) {
            showDestinationCountry();
        } /*else if (view.getId() == R.id.tvFetchBank) {
            JSONObject mRequestIFSCbody = new JSONObject();
            try {
                mRequestIFSCbody.put("bankName", "string");
                mRequestIFSCbody.put("branchIfsc", "SBIN0008209");
                mRequestIFSCbody.put("branchName", "string");
                mRequestIFSCbody.put("city", "string");
                mRequestIFSCbody.put("countryCode", "IND");

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }*/
    }

    private void getBankDetailsByIFSC() {
        showLoading(getString(R.string.info_please_wait_dots));
        AndroidNetworking.post("http://3.140.192.123:8080/api/transfer/getBankNetworkList")
             //   .addJSONObjectBody()
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

            }

            @Override
            public void onError(ANError anError) {

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
        mPayOut = payoutCurrency;
        tvDesCurrency.setText("Payout Currency : " + payoutCurrency);
        tvDestination.setText(country);

    }
}