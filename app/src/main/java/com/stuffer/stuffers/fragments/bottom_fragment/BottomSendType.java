package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.CashTransferListener;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;

public class BottomSendType extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetBehavior mBehaviour;
    private MyRadioButton radioBank, radioCash;
    private MyButton btnNextType;
    private CashTransferListener mCashTransferListener;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnNextType) {
            if (!radioBank.isChecked() && !radioCash.isChecked()) {
                Helper.showErrorMessage(getActivity(), "Select Transfer Type");
                return;
            }
            if (radioBank.isChecked()) {
                mCashTransferListener.onTransferSelect(0);
            } else {
                mCashTransferListener.onTransferSelect(1);
            }
            dismiss();

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.bottom_send_type, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());

        findIds(mView);

        return fBtmShtDialog;

    }

    private void findIds(View mView) {
        radioBank = (MyRadioButton) mView.findViewById(R.id.radioBank);
        radioCash = (MyRadioButton) mView.findViewById(R.id.radioCash);
        btnNextType = (MyButton) mView.findViewById(R.id.btnNextType);
        btnNextType.setOnClickListener(this);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCashTransferListener = (CashTransferListener) context;
    }
}
