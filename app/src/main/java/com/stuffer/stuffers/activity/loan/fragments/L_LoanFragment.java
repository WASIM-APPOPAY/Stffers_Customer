package com.stuffer.stuffers.activity.loan.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.hamzaahmedkhan.spinnerdialog.enums.SpinnerSelectionType;
import com.github.hamzaahmedkhan.spinnerdialog.ui.SpinnerDialogFragment;
import com.stuffer.stuffers.R;


public class L_LoanFragment extends Fragment {
AppCompatSpinner spinnerLoan;

    public L_LoanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView= inflater.inflate(R.layout.fragment_l__settings, container, false);
        spinnerLoan=mView.findViewById(R.id.spinnerLoan);


        return mView;
    }
}