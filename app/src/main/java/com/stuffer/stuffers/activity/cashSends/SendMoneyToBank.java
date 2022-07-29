package com.stuffer.stuffers.activity.cashSends;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.fragments.dialog.BankDialog;
import com.stuffer.stuffers.fragments.dialog.ModeDialog;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.ArrayList;


public class SendMoneyToBank extends Fragment {//implements View.OnClickListener {


    private View mView;
    private MyTextViewBold tvTitleBottom, tvTitleTop;
    private MyTextView tvSendingCurrency, tvPaymentMode;
    private ArrayList<String> mModeList;
    private ModeDialog mModeDialog;

    public SendMoneyToBank() {
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
        mView = inflater.inflate(R.layout.fragment_send_money_to_bank, container, false);
        //tvTitleTop = mView.findViewById(R.id.tvTitleTop);
        tvTitleBottom = mView.findViewById(R.id.tvTitleBottom);
        //tvSendingCurrency = mView.findViewById(R.id.tvSendingCurrency);
        //tvPaymentMode = mView.findViewById(R.id.tvPaymentMode);
        //tvPaymentMode.setOnClickListener(this);
        //tvTitleTop.setText(Html.fromHtml("<u>" + getString(R.string.info_destination_details) + "</u>"));
        tvTitleBottom.setText(Html.fromHtml("<u>" + getString(R.string.info_payment_details) + "</u>"));
        //setDetails();
        return mView;
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

    /*private void setDetails() {
        String currencySymbol = Helper.getCurrencySymble();
        tvSendingCurrency.setText(currencySymbol);
    }*/

   /* @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvPaymentMode) {
            showModeDialog();
        }
    }

    public void hideModeDialog(int pos){
        if (mModeDialog!=null){
            mModeDialog.dismiss();
            mModeDialog=null;
        }
        tvPaymentMode.setText(mModeList.get(pos));
    }*/
}