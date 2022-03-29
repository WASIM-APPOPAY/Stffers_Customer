package com.stuffer.stuffers.fragments.finance_fragment;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.views.AccountNumberEditText;
import com.stuffer.stuffers.views.CardNumberEditText;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.material.textfield.TextInputEditText;


public class ExistingCustomerFragment extends Fragment {


    private View mView;
    private MyButton btnSubmit;
    private AlertDialog dialogMerchant;
    private AccountNumberEditText card_number_field_text;
    private TextInputEditText cardholder_field_text;
    private MyEditText edBankName;

    public ExistingCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_existing_customer, container, false);
        btnSubmit = mView.findViewById(R.id.btnSubmit);
        card_number_field_text = (AccountNumberEditText) mView.findViewById(R.id.card_number_field_text);
        cardholder_field_text = (TextInputEditText) mView.findViewById(R.id.cardholder_field_text);
        edBankName = (MyEditText) mView.findViewById(R.id.edBankName);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card_number_field_text.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "please enter card number", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (cardholder_field_text.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "please enter card holder name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edBankName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "please enter bank name", Toast.LENGTH_SHORT).show();
                    return;
                }
                showSuccessDialog("Your request has been successfully sent, we will contact you very shortly " + "\nThank Your");
            }
        });
        return mView;
    }

    private void showSuccessDialog(String param) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_success_merchant, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        MyTextView tvSuccess = dialogLayout.findViewById(R.id.tvSuccess);
        tvSuccess.setText(param);
        tvHeader.setText("Clientes");

        MyTextView tvTransactionId = dialogLayout.findViewById(R.id.tvTransactionId);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        tvTransactionId.setText(param);
        tvTransactionId.setVisibility(View.GONE);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect();
            }
        });

        builder.setView(dialogLayout);

        dialogMerchant = builder.create();

        dialogMerchant.setCanceledOnTouchOutside(false);
        dialogMerchant.setCancelable(false);

        dialogMerchant.show();
    }

    private void redirect() {
        dialogMerchant.dismiss();
        getActivity().onBackPressed();
    }
}