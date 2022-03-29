package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.contact.ContactDemoActivity;
import com.stuffer.stuffers.activity.wallet.MobileRechargeActivity;
import com.stuffer.stuffers.communicator.PhoneNoListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.Objects;

public class BottomPhoneFragments extends BottomSheetDialogFragment implements View.OnClickListener {

    private BottomSheetBehavior mBehaviour;
    private ArrayList<MyButton> mListBtn;
    private MyButton btnKey1, btnKey2, btnKey3, btnKey4, btnKey5, btnKey6,
            btnKey7, btnKey8, btnKey9, btnKey10, btnClear, btnConfirm;
    private MyButton btnCloseDialog;
    private CountryCodePicker edtCustomerCountryCode;
    private MyTextView tvPhoneNumber;
    private ImageView ivContactList;
    private String codeString = "";
    private PhoneNoListener mPhoneNoListener;


    public BottomPhoneFragments() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.fragment_bottotm_phone, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());
        mListBtn = new ArrayList<>();
        findIds(mView);
        return fBtmShtDialog;
    }

    private void findIds(View mView) {
        edtCustomerCountryCode = mView.findViewById(R.id.edtCustomerCountryCode);
        tvPhoneNumber = (MyTextView) mView.findViewById(R.id.tvPhoneNumber);
        ivContactList = (ImageView) mView.findViewById(R.id.ivContactList);
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
        ivContactList.setOnClickListener(this);

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
        edtCustomerCountryCode.setExcludedCountries(getString(R.string.info_exclude_countries));


    }

    public String getSelectedCountryCode() {
        return edtCustomerCountryCode.getSelectedCountryCode();
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
        if (view.getId() == R.id.ivContactList) {
            Intent intentContact = new Intent(getActivity(), ContactDemoActivity.class);
            startActivityForResult(intentContact, AppoConstants.PICK_CONTACT);
        } else if (view.getId() == R.id.btnCloseDialog) {
            dismiss();
        } else if (view.getId() == R.id.btnClear) {
            tvPhoneNumber.setText("receiver phone number");
            codeString = "";
        } else {
            switch (view.getId()) {
                case R.id.btnKey1:
                    /*if (codeString.length() == 6)
                        return;*/
                    codeString += btnKey1.getText().toString().trim();
                    //tvInputPin.setText(codeString);

                    setPhoneNumber(codeString);
                    break;
                case R.id.btnKey2:
                    /*if (codeString.length() == 6)
                        return;*/
                    codeString += btnKey2.getText().toString().trim();
                    //tvInputPin.setText(codeString);
                    setPhoneNumber(codeString);


                    break;
                case R.id.btnKey3:
                    /*if (codeString.length() == 6)
                        return;*/
                    codeString += btnKey3.getText().toString().trim();
                    //tvInputPin.setText(codeString);
                    setPhoneNumber(codeString);

                    break;
                case R.id.btnKey4:
                    /*if (codeString.length() == 6)
                        return;*/
                    codeString += btnKey4.getText().toString().trim();
                    //tvInputPin.setText(codeString);
                    setPhoneNumber(codeString);

                    break;

                case R.id.btnKey5:
                    /*if (codeString.length() == 6)
                        return;*/
                    codeString += btnKey5.getText().toString().trim();
                    //tvInputPin.setText(codeString);
                    setPhoneNumber(codeString);

                    break;
                case R.id.btnKey6:
                    /*if (codeString.length() == 6)
                        return;*/
                    codeString += btnKey6.getText().toString().trim();
                    //tvInputPin.setText(codeString);
                    setPhoneNumber(codeString);

                    break;

                case R.id.btnKey7:
                    /*if (codeString.length() == 6)
                        return;*/
                    codeString += btnKey7.getText().toString().trim();
                    //tvInputPin.setText(codeString);
                    setPhoneNumber(codeString);

                    break;

                case R.id.btnKey8:
                    /*if (codeString.length() == 6)
                        return;*/
                    codeString += btnKey8.getText().toString().trim();
                    //tvInputPin.setText(codeString);
                    setPhoneNumber(codeString);
                    break;

                case R.id.btnKey9:
                    /*if (codeString.length() == 6)
                        return;*/
                    codeString += btnKey9.getText().toString().trim();
                    //tvInputPin.setText(codeString);
                    setPhoneNumber(codeString);

                    break;

                case R.id.btnKey10:
                    /*if (codeString.length() == 6)
                        return;*/
                    codeString += btnKey10.getText().toString().trim();
                    //tvInputPin.setText(codeString);
                    setPhoneNumber(codeString);

                    break;
                case R.id.btnConfirm:
                    //getRandomKeyFromRange();
                    if (codeString.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter receiver phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (codeString.length() < 6) {
                        Toast.makeText(getActivity(), "Please enter valid number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mPhoneNoListener.getPhoneNoWithCountryCode(getSelectedCountryCode(), codeString);


                    //Log.e(TAG, "onClick: " + codeString);
                    break;


            }
        }

    }

    private void setPhoneNumber(String codeString) {
        tvPhoneNumber.setText(codeString);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPhoneNoListener = (PhoneNoListener) context;
    }

    public void setReceiverContact(String mMobileNumber) {
        tvPhoneNumber.setText(mMobileNumber);
    }
}
