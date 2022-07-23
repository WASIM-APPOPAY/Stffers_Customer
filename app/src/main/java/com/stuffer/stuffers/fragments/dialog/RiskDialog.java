package com.stuffer.stuffers.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.OnItemSelect;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;

public class RiskDialog extends DialogFragment {
    private View mView;
    private MyRadioButton rRiskLevel;
    private OnItemSelect mListener;
    private MyButton btnClose, btnConfirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_risk, container, false);
        rRiskLevel = (MyRadioButton) mView.findViewById(R.id.rRiskLevel);
        btnClose = (MyButton) mView.findViewById(R.id.btnClose);
        btnConfirm = (MyButton) mView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rRiskLevel.isChecked()) {
                    mListener.onSelect(0);
                } else {
                    Toast.makeText(getContext(), "Please select Risk Level ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return mView;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnItemSelect) context;
    }
}
