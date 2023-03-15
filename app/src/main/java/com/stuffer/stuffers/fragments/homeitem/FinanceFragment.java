package com.stuffer.stuffers.fragments.homeitem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.FianceTab.DominaActivity;
import com.stuffer.stuffers.activity.FianceTab.FinanceActivity;
import com.stuffer.stuffers.activity.FianceTab.UnionPayActivity;
import com.stuffer.stuffers.activity.finance.GoBankActivity;
import com.stuffer.stuffers.activity.finance.InsuranceActivity;
import com.stuffer.stuffers.activity.finance.LoanActivity;
import com.stuffer.stuffers.activity.wallet.InnerPayActivity;
import com.stuffer.stuffers.communicator.FinanceListener;

public class FinanceFragment extends Fragment {

    View mView;
    LinearLayout llGoBank, llLoan, llInsurance;
    LinearLayout layoutOpen, layoutAppopay, layoutRequest, layoutExists, layoutUnion;
    LinearLayout layoutChat, layoutTransfer, layoutUnionPay, llRecharge;
    FinanceListener mFinanceListener;

    public FinanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_finance, container, false);
        // llGoBank = (LinearLayout) mView.findViewById(R.id.llGoBank);
        layoutOpen = (LinearLayout) mView.findViewById(R.id.layoutOpen);
        layoutRequest = (LinearLayout) mView.findViewById(R.id.layoutRequest);
        llLoan = (LinearLayout) mView.findViewById(R.id.llLoan);
        llInsurance = (LinearLayout) mView.findViewById(R.id.llInsurance);
        layoutExists = (LinearLayout) mView.findViewById(R.id.layoutExists);
        layoutUnion = (LinearLayout) mView.findViewById(R.id.layoutUnion);
        layoutAppopay = (LinearLayout) mView.findViewById(R.id.layoutAppopay);
        layoutChat = (LinearLayout) mView.findViewById(R.id.layoutChat);
        llRecharge = (LinearLayout) mView.findViewById(R.id.llRecharge);
        layoutTransfer = (LinearLayout) mView.findViewById(R.id.layoutTransfer);
        layoutUnionPay = (LinearLayout) mView.findViewById(R.id.layoutUnionPay);


        addClickListener();

        return mView;
    }

    private void addClickListener() {
        /*llGoBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppoPayApplication.isNetworkAvailable(getContext())) {
                    sentToTheirParentActivity(0);
                } else {
                    showToast(getString(R.string.no_inteenet_connection));
                }
            }
        });*/

        llLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppoPayApplication.isNetworkAvailable(getContext())) {
                    sentToTheirParentActivity(1);
                } else {
                    showToast(getString(R.string.no_inteenet_connection));
                }
            }
        });

        llInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppoPayApplication.isNetworkAvailable(getContext())) {
                    sentToTheirParentActivity(2);
                } else {
                    showToast(getString(R.string.no_inteenet_connection));
                }
            }
        });

        layoutOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOpen = new Intent(getActivity(), FinanceActivity.class);
                startActivity(intentOpen);

            }
        });

        layoutRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent mIntent = new Intent(getActivity(), CashSend.class);
//                startActivity(mIntent);

            }
        });

        layoutExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intentExisting = new Intent(getActivity(), ClientsActivity.class);
                Intent intentExisting = new Intent(getActivity(), DominaActivity.class);
                //startActivity(intentExisting);
                getActivity().startActivityForResult(intentExisting, 1009);
            }
        });

        layoutUnion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUnion = new Intent(getActivity(), UnionPayActivity.class);
                startActivity(intentUnion);
            }
        });

        layoutAppopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentScan = new Intent(getActivity(), InnerPayActivity.class);
                startActivityForResult(intentScan, 190);
            }
        });
        layoutChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFinanceListener.onFinanceRequest(4);

            }
        });
        layoutTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFinanceListener.onFinanceRequest(3);
            }
        });
        layoutUnionPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFinanceListener.onFinanceRequest(2);

            }
        });
        llRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFinanceListener.onFinanceRequest(-1);
            }
        });

    }


    public void sentToTheirParentActivity(int type) {

        switch (type) {
            case 0:
                Intent intent = new Intent(getContext(), GoBankActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(getContext(), LoanActivity.class);
                startActivity(intent1);

                break;
            case 2:
                Intent intent2 = new Intent(getContext(), InsuranceActivity.class);
                startActivity(intent2);

                break;
        }


    }

    private void showToast(String message) {
        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFinanceListener = (FinanceListener) context;

    }
}