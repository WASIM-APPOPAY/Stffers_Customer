package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.UpdateProfileRequest;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class BottomUpdateProfile extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetBehavior mBehaviour;
    private MyButton btnUpdate;
    private MyTextView tvProfileInfo;
    private UpdateProfileRequest updateListenr;

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnUpdate) {
            updateListenr.onCompleteProfileRequest();
            dismiss();

        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog mBottomDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.profile_bottom_dialog, null);
        mBottomDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());
        findIds(mView);
        return mBottomDialog;


    }

    @Override
    public void onStart() {
        super.onStart();
        View touchOutsideView = Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow())
                .getDecorView()
                .findViewById(com.google.android.material.R.id.touch_outside);
        touchOutsideView.setClickable(false);
        touchOutsideView.setFocusable(false);

        mBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    private void findIds(View mView) {
        btnUpdate = (MyButton) mView.findViewById(R.id.btnUpdate);
        tvProfileInfo = (MyTextView) mView.findViewById(R.id.tvProfileInfo);
        String param = "Dear customer Thank You for SignUp with us, please click on " + "<font color='#FF0000'>" + "UPDATE" + "</font>" + " to complete your profile.";
        tvProfileInfo.setText(Html.fromHtml(param));
        btnUpdate.setOnClickListener(this);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        updateListenr = (UpdateProfileRequest) context;
    }
}
