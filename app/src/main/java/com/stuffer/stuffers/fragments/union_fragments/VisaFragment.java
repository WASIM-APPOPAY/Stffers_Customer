package com.stuffer.stuffers.fragments.union_fragments;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainUAPIInterface;
import com.stuffer.stuffers.communicator.OnBankSubmit;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;


public class VisaFragment extends Fragment implements View.OnClickListener {
    private MyButton btnCardEnrollment;
    private String enrollMentUrl;
    private MyEditText mTiedAccNo;
    private ProgressDialog mProgress;
    private JsonObject mRoot;
    private MainUAPIInterface apiServiceUNIONPay;
    String firstName, lastName, fullName;
    private String phoneCode;
    private Long senderMobileNumber;
    private String walletAccountNumber;
    private String phWithCode;
    private Dialog mDialogCard;
    private MyEditText mTiedFname, mTiedLname;
    private MyTextView tvCountryCodeU, tvMobNumU;
    private int mPrdNo;
    private String newNumber;
    private OnBankSubmit mConfirmLsitener;
    private CheckBox checkbox;


    private View mView;

    public VisaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_visa, container, false);
        apiServiceUNIONPay = ApiUtils.getApiServiceUNIONPay();
        btnCardEnrollment = (MyButton) mView.findViewById(R.id.btnCardEnrollment);
        mTiedAccNo = (MyEditText) mView.findViewById(R.id.mTiedAccNo);
        mTiedFname = (MyEditText) mView.findViewById(R.id.mTiedFname);
        mTiedLname = (MyEditText) mView.findViewById(R.id.mTiedLname);
        tvCountryCodeU = (MyTextView) mView.findViewById(R.id.tvCountryCodeU);
        tvMobNumU = (MyTextView) mView.findViewById(R.id.tvMobNumU);
        checkbox = (CheckBox) mView.findViewById(R.id.checkbox);


        btnCardEnrollment.setOnClickListener(this);
        String mUserDetails = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        firstName = Helper.getFirstName();
        lastName = Helper.getLastName();
        fullName = firstName + " " + lastName;
        senderMobileNumber = Helper.getSenderMobileNumber();
        phoneCode = Helper.getPhoneCode();
        phWithCode = phoneCode + senderMobileNumber;
        walletAccountNumber = Helper.getWalletAccountNumber();


        mTiedFname.setText(firstName);
        mTiedLname.setText(lastName);
        tvCountryCodeU.setText(phoneCode);
        tvMobNumU.setText("" + senderMobileNumber);
        Bundle arguments = this.getArguments();
        mPrdNo = arguments.getInt(AppoConstants.PRDNUMBER, 0);
        newNumber = arguments.getString("newNumber");
        ////Log.e(TAG, "onCreateView: " + anInt);
        //later below will be enable
        /*if (mPrdNo == 1003) {
            mTiedAccNo.setText(walletAccountNumber);
        } else {
            mTiedAccNo.setText("" + newNumber);
        }*/


        return mView;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnCardEnrollment) {
            //getCmvInfo();

            if (checkbox.isChecked()) {
                Helper.showLoading(getString(R.string.info_please_wait), getActivity());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //showSuccessDialog("Card Enrollment", "Your UnionPay Card has been created Successfully");
                        showSuccessDialog();
                    }
                }, 3000);
            } else {
                Toast.makeText(getActivity(), "Please accept term and conditions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_success_unionpay, null);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);


        MyTextView btnClose = dialogLayout.findViewById(R.id.btnClose);
        MyTextView tvSuccess = dialogLayout.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Your VISA Card has been Created Successfully.");
        MyTextView tvNext = dialogLayout.findViewById(R.id.tvNext);
        tvNext.setVisibility(View.GONE);

        MyTextView btnNext = dialogLayout.findViewById(R.id.btnNext);
        btnNext.setVisibility(View.GONE);
          btnClose.setVisibility(View.VISIBLE);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectHome();
                //mDialogCard.dismiss();

            }
        });

        builder.setView(dialogLayout);

        mDialogCard = builder.create();

        mDialogCard.setCanceledOnTouchOutside(false);

        mDialogCard.show();
    }


    private void redirectHome() {
        mDialogCard.dismiss();
        mConfirmLsitener.onConfirm(2);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mConfirmLsitener= (OnBankSubmit) context;
    }



}