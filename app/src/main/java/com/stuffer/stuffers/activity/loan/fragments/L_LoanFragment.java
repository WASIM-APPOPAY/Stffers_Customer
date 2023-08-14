package com.stuffer.stuffers.activity.loan.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.hamzaahmedkhan.spinnerdialog.enums.SpinnerSelectionType;
import com.github.hamzaahmedkhan.spinnerdialog.ui.SpinnerDialogFragment;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.loan.adapter.L_PayAdapter;


public class L_LoanFragment extends Fragment {
    AppCompatSpinner spinnerLoan;
    private RecyclerView recyclerViewLoan;

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
        View mView = inflater.inflate(R.layout.fragment_l__settings, container, false);




        return mView;
    }
}