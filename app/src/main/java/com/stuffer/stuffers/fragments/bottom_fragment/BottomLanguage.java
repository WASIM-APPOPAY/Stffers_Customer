package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.LanguageListener;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyRadioButton;

public class BottomLanguage extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetBehavior mBehaviour;
    private MyButton btnApplyLan;
    private String lang;
    private MyRadioButton rbSpanish, rbEnglish;
    private LanguageListener mLanguageListener;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnApplyLan) {
            if (!rbEnglish.isChecked() && !rbSpanish.isChecked()) {
                Toast.makeText(getActivity(), "Please Select Language", Toast.LENGTH_SHORT).show();
                return;
            }

            mLanguageListener.onLanguageSelect(lang);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.bottom_language, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());

        findIds(mView);

        return fBtmShtDialog;

    }

    private void findIds(View mView) {
        btnApplyLan = (MyButton) mView.findViewById(R.id.btnApplyLan);
        rbSpanish = (MyRadioButton) mView.findViewById(R.id.rbSpanish);
        rbEnglish = (MyRadioButton) mView.findViewById(R.id.rbEnglish);
        btnApplyLan.setOnClickListener(this);

        rbEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    lang = "en";
                    //setLocal(lang);
                }
            }
        });
        rbSpanish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    lang = "es";
                    //setLocal(lang);
                }
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mLanguageListener = (LanguageListener) context;
    }
}
