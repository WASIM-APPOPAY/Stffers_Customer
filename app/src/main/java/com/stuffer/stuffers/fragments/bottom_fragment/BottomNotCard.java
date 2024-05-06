package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.FianceTab.UnionPayActivity;
import com.stuffer.stuffers.communicator.UnionPayCardListener;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class BottomNotCard extends BottomSheetDialogFragment implements View.OnClickListener {

    private MyTextViewBold tvCommonHeader;
    MyTextView tvCommonContent;
    private MyButton btnCommonOk;
    private BottomSheetBehavior mBehaviour;
    private MyTextView btnApply;
    UnionPayCardListener mUnionPayCardListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.bottom_not_card, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());

        findIds(mView);

        return fBtmShtDialog;

    }

    private void findIds(View mView) {
        btnApply = (MyTextView) mView.findViewById(R.id.btnApply);
        tvCommonContent = (MyTextView) mView.findViewById(R.id.tvCommonContent);
        btnApply.setOnClickListener(this);
        //Dear customer you have not apply for UnionPay Wallet card,please click on Apply to processed.
        String info = "Dear customer you have not applied for Visa Card and UnionPay Virtual Wallet card,please click on " + "<font color='#FF9201'>" + "APPLY" + "</font>" + " to processed.";
        tvCommonContent.setText(Html.fromHtml(info));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnApply) {
             /*Intent intentUnion = new Intent(getActivity(), UnionPayActivity.class);
             startActivity(intentUnion);*/
            mUnionPayCardListener.onCardRequest();
        }
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
        mUnionPayCardListener = (UnionPayCardListener) context;
    }
}
