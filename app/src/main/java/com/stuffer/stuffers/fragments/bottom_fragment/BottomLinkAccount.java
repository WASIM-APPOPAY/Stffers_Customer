package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.LinkAccountListener;
import com.stuffer.stuffers.communicator.UnionPayCardListener;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.Objects;


public class BottomLinkAccount extends BottomSheetDialogFragment implements View.OnClickListener {
    private MyTextViewBold tvCommonHeader;
    MyTextView tvCommonContent;
    private MyButton btnCommonOk;
    private BottomSheetBehavior mBehaviour;
    private MyButton btnApply, btnClose;
    LinkAccountListener mLinkAccountListener;

    public BottomLinkAccount() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.fragment_bottom_link_account, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());

        findIds(mView);

        return fBtmShtDialog;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnApply) {
             /*Intent intentUnion = new Intent(getActivity(), UnionPayActivity.class);
             startActivity(intentUnion);*/
            mLinkAccountListener.onLinkAccountConfirm();
        } else if (view.getId() == R.id.btnClose) {
            dismiss();
        }
    }

    private void findIds(View mView) {
        btnApply = (MyButton) mView.findViewById(R.id.btnApply);

        btnClose = (MyButton) mView.findViewById(R.id.btnClose);
        tvCommonContent = (MyTextView) mView.findViewById(R.id.tvCommonContent);
        btnApply.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        //Dear customer you have not apply for UnionPay Wallet card,please click on Apply to processed.
        //If you have your bank a/c please click on APPLY to link your Bank a/c, other wise click on CLOSE.
        String info = "Dear customer If you have your bank a/c, please click on " + "<font color='#009900'>" + "APPLY" + "</font>" + " to processed . If you don't have please click on " + "<font color='#FF0000'>" + "CLOSE" + "</font>";
        tvCommonContent.setText(Html.fromHtml(info));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mLinkAccountListener = (LinkAccountListener) context;
    }
}