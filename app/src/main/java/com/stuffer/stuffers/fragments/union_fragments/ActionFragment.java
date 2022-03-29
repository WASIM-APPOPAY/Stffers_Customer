package com.stuffer.stuffers.fragments.union_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.linkbank.LinkBankAccountActivity;
import com.stuffer.stuffers.communicator.UnionPayListener;
import com.stuffer.stuffers.views.MyButton;


public class ActionFragment extends Fragment implements View.OnClickListener {


    private MyButton btnOpenNewAccount, btnEnrolment,btnUnmask;
    private UnionPayListener mUnionPayListener;
    private MyButton btnLink;

    public ActionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_action, container, false);
        btnOpenNewAccount = (MyButton) mView.findViewById(R.id.btnOpenNewAccount);
        btnEnrolment = (MyButton) mView.findViewById(R.id.btnEnrolment);
        btnLink = (MyButton) mView.findViewById(R.id.btnLink);
        btnUnmask = (MyButton) mView.findViewById(R.id.btnUnmask);
        btnEnrolment.setOnClickListener(this);
        btnOpenNewAccount.setOnClickListener(this);
        btnUnmask.setOnClickListener(this);
        btnLink.setOnClickListener(this);


        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mUnionPayListener = (UnionPayListener) context;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnEnrolment) {
            mUnionPayListener.onEnrollmentClick();
        } else if (view.getId() == R.id.btnOpenNewAccount) {
            mUnionPayListener.onOpenNewAccountRequest();
        } else if (view.getId() == R.id.btnLink) {
            Intent intentLink = new Intent(getActivity(), LinkBankAccountActivity.class);
            startActivity(intentLink);
        } else if (view.getId() == R.id.btnUnmask) {
            mUnionPayListener.onUnMaskRequest();
        }
    }
}