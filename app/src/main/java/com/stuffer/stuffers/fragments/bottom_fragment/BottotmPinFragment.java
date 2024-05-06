package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.views.MyButton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class BottotmPinFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "BottotmPinFragment";
    private MyButton btnKey1, btnKey2, btnKey3, btnKey4, btnKey5, btnKey6,
            btnKey7, btnKey8, btnKey9, btnKey10;
    MyTextView btnClear, btnConfirm;
    ArrayList<MyButton> mListBtn;
    ImageView ivDots1, ivDots2, ivDots3, ivDots4, ivDots5, ivDots6, ivClosePin;


    private String codeString = "";
    List<ImageView> dots;
    private BottomSheetBehavior mBehaviour;
    private MyButton btnCloseDialog;
    private TransactionPinListener mPinListenr;

    public BottotmPinFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.fragment_bottotm_pin, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());
        mListBtn = new ArrayList<>();
        dots = new ArrayList<>();
        findIds(mView);
        getRandomKeyFromRange();
        return fBtmShtDialog;
    }

    private void findIds(View mView) {
        ivDots1 = (ImageView) mView.findViewById(R.id.dot_1);
        ivDots2 = (ImageView) mView.findViewById(R.id.dot_2);
        ivDots3 = (ImageView) mView.findViewById(R.id.dot_3);
        ivDots4 = (ImageView) mView.findViewById(R.id.dot_4);
        ivDots5 = (ImageView) mView.findViewById(R.id.dot_5);
        ivDots6 = (ImageView) mView.findViewById(R.id.dot_6);
        btnKey1 = (MyButton) mView.findViewById(R.id.btnKey1);
        btnKey2 = (MyButton) mView.findViewById(R.id.btnKey2);
        btnKey3 = (MyButton) mView.findViewById(R.id.btnKey3);
        btnKey4 = (MyButton) mView.findViewById(R.id.btnKey4);
        btnKey5 = (MyButton) mView.findViewById(R.id.btnKey5);
        btnKey6 = (MyButton) mView.findViewById(R.id.btnKey6);
        btnKey7 = (MyButton) mView.findViewById(R.id.btnKey7);
        btnKey8 = (MyButton) mView.findViewById(R.id.btnKey8);
        btnKey9 = (MyButton) mView.findViewById(R.id.btnKey9);
        btnKey10 = (MyButton) mView.findViewById(R.id.btnKey10);
        btnClear = (MyTextView) mView.findViewById(R.id.btnClear);
        btnConfirm = (MyTextView) mView.findViewById(R.id.btnConfirm);
        btnCloseDialog = (MyButton) mView.findViewById(R.id.btnCloseDialog);
        ivClosePin = (ImageView) mView.findViewById(R.id.ivClosePin);

        btnKey1.setOnClickListener(this);
        btnKey2.setOnClickListener(this);
        btnKey3.setOnClickListener(this);
        btnKey4.setOnClickListener(this);
        btnKey5.setOnClickListener(this);
        btnKey6.setOnClickListener(this);
        btnKey7.setOnClickListener(this);
        btnKey8.setOnClickListener(this);
        btnKey9.setOnClickListener(this);
        btnKey10.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnCloseDialog.setOnClickListener(this);
        ivClosePin.setOnClickListener(this);

        mListBtn.add(btnKey1);
        mListBtn.add(btnKey2);
        mListBtn.add(btnKey3);
        mListBtn.add(btnKey4);
        mListBtn.add(btnKey5);
        mListBtn.add(btnKey6);
        mListBtn.add(btnKey7);
        mListBtn.add(btnKey8);
        mListBtn.add(btnKey9);
        mListBtn.add(btnKey10);
        dots.add(ivDots1);
        dots.add(ivDots2);
        dots.add(ivDots3);
        dots.add(ivDots4);
        dots.add(ivDots5);
        dots.add(ivDots6);

    }

    private void getRandomKeyFromRange() {
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        List<Integer> list;
        list = Arrays.asList(array);
        Collections.shuffle(list);

        for (int i = 0; i < list.size(); i++) {
            int answer = list.get(i);
            mListBtn.get(i).setText("" + list.get(answer));
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnKey1:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey1.getText().toString().trim();
                //tvInputPin.setText(codeString);

                setDotEnable();
                break;
            case R.id.btnKey2:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey2.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();


                break;
            case R.id.btnKey3:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey3.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;
            case R.id.btnKey4:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey4.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey5:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey5.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;
            case R.id.btnKey6:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey6.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey7:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey7.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey8:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey8.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();
                break;

            case R.id.btnKey9:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey9.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey10:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey10.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;
            case R.id.btnConfirm:
                //getRandomKeyFromRange();
                if (codeString.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter transaction pin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (codeString.length() < 6) {
                    Toast.makeText(getActivity(), "Transaction pin should be six digit", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPinListenr.onPinConfirm(codeString);


                //Log.e(TAG, "onClick: " + codeString);
                break;
            case R.id.btnClear:
                setDotDisable();
                getRandomKeyFromRange();

                break;
            case R.id.btnCloseDialog:
                dismiss();
                break;
            case R.id.ivClosePin:
                dismiss();
                break;


        }
    }

    private void setDotEnable() {
        for (int i = 0; i < codeString.length(); i++) {
            dots.get(i).setImageResource(R.drawable.color_dots);
            //dots.get(i).setColorFilter(ContextCompat.getColor(getContext(), R.color.common_color), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    private void setDotDisable() {
        for (int i = 0; i < 6; i++) {
            dots.get(i).setImageResource(R.drawable.gray_dots);
        }
        codeString = "";
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPinListenr = (TransactionPinListener) context;
    }
}
