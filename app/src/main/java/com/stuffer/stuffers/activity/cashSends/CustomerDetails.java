package com.stuffer.stuffers.activity.cashSends;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.BeneficiaryListener;
import com.stuffer.stuffers.fragments.dialog.CountryDialogFragment;
import com.stuffer.stuffers.fragments.dialog.RiskDialog;
import com.stuffer.stuffers.models.Country.CountryCodeResponse;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerDetails extends Fragment implements View.OnClickListener {


    private View mView;
    MyTextView cTvName, cTvDob, cTvAddress, cTvMobile, cTvPhone, cTvNational, cTvRisk, cBtnNext;
    MyTextViewBold cTvTitle;
    private ProgressDialog mProgress;
    private MainAPIInterface mainAPIInterface;
    private List<Result> mListCountry;
    private RiskDialog mRiskLevel;
    private BeneficiaryListener mListener;

    public CustomerDetails() {
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
        mainAPIInterface = ApiUtils.getAPIService();
        mView = inflater.inflate(R.layout.fragment_customer_details, container, false);
        cTvTitle = mView.findViewById(R.id.cTvTitle);
        cTvName = mView.findViewById(R.id.cTvName);
        cTvDob = mView.findViewById(R.id.cTvDob);
        cTvAddress = mView.findViewById(R.id.cTvAddress);
        cTvMobile = mView.findViewById(R.id.cTvMobile);
        cTvNational = mView.findViewById(R.id.cTvNational);
        cTvPhone = mView.findViewById(R.id.cTvPhone);
        cTvRisk = mView.findViewById(R.id.cTvRisk);
        cBtnNext = mView.findViewById(R.id.cBtnNext);
        cTvNational.setOnClickListener(this);
        cTvRisk.setOnClickListener(this);
        cBtnNext.setOnClickListener(this);

        setDetails();
        getCountryList();
        return mView;
    }

    private void setDetails() {
        cTvTitle.setText(Html.fromHtml("<u>" + getString(R.string.info_customer_details) + "</u>"));
        String name = "<font color='#00baf2'>" + "Name" + "<br></font>" + Helper.getSenderName();
        cTvName.setText(Html.fromHtml(name));
        /*String dob = "<font color='#00baf2'>" + "Date Of Birth " + "<br></font>" + Helper.getDob();
        cTvDob.setText(Html.fromHtml(dob));*/
        try {
            String dob = Helper.getDob();
            if (!StringUtils.isEmpty(dob)) {
                String dateOfBirth = Helper.getDateOfBirth(dob);
                dob = "<font color='#00baf2'>" + "Date Of Birth " + "<br></font>" + dateOfBirth;
                cTvDob.setText(Html.fromHtml(dob));
            } else {
                dob = "<font color='#00baf2'>" + "Date Of Birth " + "<br></font>" + Helper.getDob();
                cTvDob.setText(Html.fromHtml(dob));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String address = "<font color='#00baf2'>" + "Address " + "<br></font>" + Helper.getAddress();
        cTvAddress.setText(Html.fromHtml(address));
        String mobile = "<font color='#00baf2'>" + "Mobile No " + "<br></font>" + Helper.getNumberWithCountryCodeSpace();
        cTvMobile.setText(Html.fromHtml(mobile));
        String phone = "<font color='#00baf2'>" + "Phone No " + "<br></font>" + Helper.getNumberWithCountryCodeSpace();
        cTvPhone.setText(Html.fromHtml(phone));


    }

    public void showProgress(String msg) {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(msg);
        mProgress.show();
    }

    public void hideProgress() {
        if (mProgress != null) {
            mProgress.dismiss();
            mProgress = null;
        }
    }

    private void getCountryList() {
        showProgress(getString(R.string.info_getting_country_code));

        mainAPIInterface.getCountryCode().enqueue(new Callback<CountryCodeResponse>() {
            @Override
            public void onResponse(Call<CountryCodeResponse> call, Response<CountryCodeResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        mListCountry = new ArrayList<>();
                        mListCountry = response.body().getResult();
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryCodeResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cTvNational) {
            if (mListCountry != null && mListCountry.size() > 0) {
                CountryDialogFragment countryDialogFragment = new CountryDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppoConstants.COUNTRY, (ArrayList<? extends Parcelable>) mListCountry);
                countryDialogFragment.setArguments(bundle);
                countryDialogFragment.setCancelable(false);
                countryDialogFragment.show(getChildFragmentManager(), countryDialogFragment.getTag());

            }
        } else if (view.getId() == R.id.cTvRisk) {
            mRiskLevel = new RiskDialog();
            mRiskLevel.setCancelable(false);
            mRiskLevel.show(getChildFragmentManager(), mRiskLevel.getTag());
        } else if (view.getId() == R.id.cBtnNext) {

            mListener.onBeneficiaryRequest();
        }
    }

    public void setNationality(String countryName, String countryId, int countryCode, int pos) {
        for (int i = 0; i < mListCountry.size(); i++) {
            if (countryName.equalsIgnoreCase(mListCountry.get(i).getCountryname())) {
                //mCountyId = mListCountry.get(i).getId();
                break;
            }
        }
        cTvNational.setText(countryName);
        cTvNational.setAllCaps(true);
    }


    public void setRiskLevel(int pos) {
        if (mRiskLevel != null) {
            mRiskLevel.dismiss();
        }
        cTvRisk.setText("Natural Person.");

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (BeneficiaryListener) context;

    }
}