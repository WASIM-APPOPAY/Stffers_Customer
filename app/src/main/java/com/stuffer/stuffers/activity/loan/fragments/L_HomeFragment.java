package com.stuffer.stuffers.activity.loan.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.loan.adapter.L_PayAdapter;


public class L_HomeFragment extends Fragment {


    private RecyclerView recyclerViewLoan;

    public L_HomeFragment() {
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
        View mView = inflater.inflate(R.layout.fragment_l__home, container, false);
        recyclerViewLoan = mView.findViewById(R.id.recyclerViewLoan);
        recyclerViewLoan.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        L_PayAdapter mL_payAdapter = new L_PayAdapter();
        recyclerViewLoan.setAdapter(mL_payAdapter);

        return mView;

    }
}