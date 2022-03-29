package com.stuffer.stuffers.fragments.wallet_fragments;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.AgreementListen;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;


public class NewAccountFragment extends Fragment implements View.OnClickListener{


    private View mView;
    private MyTextView tvDescription;
    private AgreementListen mListen;
    private MyButton btnNextScreen;

    public NewAccountFragment() {
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
        mView = inflater.inflate(R.layout.fragment_new_account, container, false);
        btnNextScreen=mView.findViewById(R.id.btnNextScreen);
        tvDescription = (MyTextView) mView.findViewById(R.id.tvDescription);
        //with passport Number 123456789, acting in my own name and my own behalf certify under the penalty of perjury that the following is true and correct; I received monthly income of $3,000, which come from:
        //String mString="I the undersigned "+ Helper.getFirstName()+" "+Helper.getLastName()+" with passport Number "+Helper

      btnNextScreen.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListen = (AgreementListen) context;

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnNextScreen){
            mListen.onAgreementConfirm();
        }
    }
}