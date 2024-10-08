package com.stuffer.stuffers.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.LoginRequestListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;

public class ErrorDialogFragment extends DialogFragment {
    LoginRequestListener mLoginRequest;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_error_dialog, container, false);
        MyTextView tvInfo = view.findViewById(R.id.tvInfo);
        MyButton btnYes = view.findViewById(R.id.btnYes);
        MyButton btnClose = view.findViewById(R.id.btnClose);
        Bundle arguments = this.getArguments();
        tvInfo.setText(arguments.getString(AppoConstants.INFO));
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginRequest.onLoginRequest();
                dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mLoginRequest = (LoginRequestListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException("parent must implement LoginRequestListener");
        }
    }
}
