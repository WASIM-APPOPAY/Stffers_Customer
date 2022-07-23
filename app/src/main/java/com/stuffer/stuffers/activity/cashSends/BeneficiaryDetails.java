package com.stuffer.stuffers.activity.cashSends;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.BeneficiaryListener;
import com.stuffer.stuffers.communicator.CalculationListener;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;


public class BeneficiaryDetails extends Fragment implements View.OnClickListener{

    private View mView;
    MyTextViewBold cTvTitle;
    private CalculationListener mListener;
    private MyTextView bBtnNext;

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
        mView=inflater.inflate(R.layout.fragment_beneficiary_details, container, false);
        cTvTitle = mView.findViewById(R.id.cTvTitle);
        bBtnNext = mView.findViewById(R.id.bBtnNext);
        bBtnNext.setOnClickListener(this);
        cTvTitle.setText(Html.fromHtml("<u>" + getString(R.string.info_benifi_details) + "</u>"));
        return mView;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener=(CalculationListener)context;

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.bBtnNext){
            mListener.onCalculationRequest();
        }
    }
}