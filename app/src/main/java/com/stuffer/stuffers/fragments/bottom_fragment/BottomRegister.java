package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.CommonListener;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.Objects;


public class BottomRegister extends BottomSheetDialogFragment implements View.OnClickListener {
    public static final String COMMON_CLOSE = "common_close";
    public static final String COMMON_APPLY = "common_apply";
    public static final String COMMON_HEADING = "common_heading";
    public static final String COMMON_BODY = "common_body";
    public static final String COMMON_FROM = "common_from";

    private MyTextViewBold tvCommonHeader;
    MyTextView tvCommonContent;
    private MyButton btnCommonOk;
    private BottomSheetBehavior mBehaviour;
    private MyButton btnApply, btnClose;
    CommonListener mLinkAccountListener;
    Bundle arguments;
    private MyTextViewBold tvRegister;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.fragment_bottom_register, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());

        arguments = this.getArguments();

        findIds(mView);

        return fBtmShtDialog;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnApply) {
             /*Intent intentUnion = new Intent(getActivity(), UnionPayActivity.class);
             startActivity(intentUnion);*/
            mLinkAccountListener.onCommonConfirm();
        } else if (view.getId() == R.id.btnClose) {
            dismiss();
        } else if (view.getId() == R.id.tvRegister) {
            mLinkAccountListener.onCommonConfirm();
        }
    }

    private void findIds(View mView) {
        btnApply = (MyButton) mView.findViewById(R.id.btnApply);

        btnClose = (MyButton) mView.findViewById(R.id.btnClose);
        tvCommonContent = (MyTextView) mView.findViewById(R.id.tvCommonContent);
        tvRegister = (MyTextViewBold) mView.findViewById(R.id.tvRegister);
        btnApply.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        //Dear customer you have not apply for UnionPay Wallet card,please click on Apply to processed.
        //If you have your bank a/c please click on APPLY to link your Bank a/c, other wise click on CLOSE.
        //String info = "Dear customer If you have your bank a/c, please click on " + "<font color='#009900'>" + "APPLY" + "</font>" + " to processed . If you don't have please click on " + "<font color='#FF0000'>" + "CLOSE" + "</font>";
        /*btnClose.setText(arguments.getString(COMMON_CLOSE));
        btnApply.setText(arguments.getString(COMMON_APPLY));
        if (arguments.getString(COMMON_FROM).equalsIgnoreCase("App_Req")) {
            String info = "Thank you for registering with appOpay chat. You can now start chatting with other appOpay users.To use all the appOpay feature,please click on " + "<font color='#009900'>" + "APPLY" + "</font>" + " to " + "<font color='#FF0000'>" + "SignUp" + "</font>" + ".";
            tvCommonContent.setText(Html.fromHtml(info));
        }*/
        //String info = "Thank you for registering with appOpay chat. You can now start chatting with other appOpay users.To use all the appOpay feature,please click on " + "<font color='#009900'>" + "APPLY" + "</font>" + " to " + "<font color='#FF0000'>" + "SignUp" + "</font>" + ".";
        //tvCommonContent.setText(Html.fromHtml(info));

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
        mLinkAccountListener = (CommonListener) context;
    }

}
