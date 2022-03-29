package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BottomTransactionPin extends BottomSheetDialogFragment implements View.OnClickListener {

    private MyButton btnKey1, btnKey2, btnKey3, btnKey4, btnKey5, btnKey6,
            btnKey7, btnKey8, btnKey9, btnKey10, btnClear, btnConfirm;
    ArrayList<MyButton> mListBtn;



    private String codeString = "";
    List<ImageView> dots;
    private BottomSheetBehavior mBehaviour;
    private MyButton btnCloseDialog;
    private TransactionPinListener mPinListenr;
    private MyEditText edCnfmTransPin,edTransPin;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.fragment_trans_pin, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());
        mListBtn = new ArrayList<>();
        dots = new ArrayList<>();
        findIds(mView);
        getRandomKeyFromRange();
        return fBtmShtDialog;
    }

    private void findIds(View mView) {
        edCnfmTransPin=(MyEditText)mView.findViewById(R.id.edCnfmTransPin);
        edTransPin=(MyEditText)mView.findViewById(R.id.edTransPin);


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
        btnClear = (MyButton) mView.findViewById(R.id.btnClear);
        btnConfirm = (MyButton) mView.findViewById(R.id.btnConfirm);
        btnCloseDialog = (MyButton) mView.findViewById(R.id.btnCloseDialog);

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
        if (dialog != null)
        {
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

            case R.id.btnConfirm:
                //getRandomKeyFromRange();
                if (edTransPin.getText().toString().trim().isEmpty()){
                    edTransPin.setError("Please enter transaction pin");
                    edTransPin.requestFocus();

                    return;
                }
                if (edTransPin.getText().toString().length()<6){
                    edTransPin.setError("Transaction pin should be six digit");
                    edTransPin.requestFocus();

                    return;
                }
                if (edCnfmTransPin.getText().toString().trim().isEmpty()){
                    edCnfmTransPin.setError("Please Confirm transaction pin");
                    edCnfmTransPin.requestFocus();
                    return;
                }

                if (edCnfmTransPin.getText().toString().trim().length()<6){
                    //Toast.makeText(getActivity(), "Transaction pin should be six digit", Toast.LENGTH_SHORT).show();
                    edCnfmTransPin.setError("Confirm pin should be six digit");
                    edCnfmTransPin.requestFocus();
                    return;
                }

                if (edTransPin.getText().toString().trim().equalsIgnoreCase(edCnfmTransPin.getText().toString().trim())){
                    mPinListenr.onPinConfirm(edTransPin.getText().toString().trim());
                }else {
                    Toast.makeText(getActivity(), "pin mismatch", Toast.LENGTH_SHORT).show();
                }




                //Log.e(TAG, "onClick: " + codeString);
                break;
            case R.id.btnClear:
                setDotDisable();
                getRandomKeyFromRange();

                break;
            case R.id.btnCloseDialog:
                dismiss();
                break;


        }
    }

    private void setDotEnable() {
        for (int i = 0; i < codeString.length(); i++) {
            dots.get(i).setImageResource(R.drawable.dot_enable);
        }
    }

    private void setDotDisable() {
        edCnfmTransPin.setText("");
        edTransPin.setText("");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPinListenr=(TransactionPinListener)context;
    }

}
