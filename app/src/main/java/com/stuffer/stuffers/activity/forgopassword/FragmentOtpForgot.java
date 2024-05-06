package com.stuffer.stuffers.activity.forgopassword;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentOtpForgot extends Fragment {


    private View mView;
    private MyTextViewBold tvInfoOtp;
    private String mMobileNumber, mAreaCode, mReset;
    private EditText otp_edit_text1, otp_edit_text2, otp_edit_text3, otp_edit_text4, otp_edit_text5, otp_edit_text6;
    private int seconds;
    private int minutes;
    private Timer newTimer;
    private MyTextView txtTimer;
    private MyTextView btnResendOtp;
    private ArrayList<EditText> mListEditText;
    private MainAPIInterface mainAPIInterface;
    private FragmentReplaceListener mReplaceListener;
    private ProgressDialog dialog;
    private MyTextView tvVerifyOtpCommon;

    public FragmentOtpForgot() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMobileNumber = getArguments().getString(AppoConstants.MOBILENUMBER);
            mAreaCode = getArguments().getString(AppoConstants.PHONECODE);
            mReset = getArguments().getString("reset");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_otp_forgot, container, false);
        mainAPIInterface = ApiUtils.getAPIService();
        btnResendOtp = (MyTextView) mView.findViewById(R.id.btnResendOtp);
        tvInfoOtp = (MyTextViewBold) mView.findViewById(R.id.tvInfoOtp);
        tvVerifyOtpCommon = (MyTextView) mView.findViewById(R.id.tvVerifyOtpCommon);
        txtTimer = (MyTextView) mView.findViewById(R.id.txtTimer);
        otp_edit_text1 = mView.findViewById(R.id.otp_edit_text1);
        otp_edit_text2 = mView.findViewById(R.id.otp_edit_text2);
        otp_edit_text3 = mView.findViewById(R.id.otp_edit_text3);
        otp_edit_text4 = mView.findViewById(R.id.otp_edit_text4);
        otp_edit_text5 = mView.findViewById(R.id.otp_edit_text5);
        otp_edit_text6 = mView.findViewById(R.id.otp_edit_text6);
        setInfo();
        setOtpBoxFocus();
        startTimer();
        return mView;
    }

    private void setOtpBoxFocus() {
        /*
        for (int i = 0; i < smsCode.length(); i++) {
                                mList.get(i).setText("" + smsCode.charAt(i));
                            }
         */
        otp_edit_text1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp_edit_text1.getText().toString().trim().length() == 1) {
                    otp_edit_text2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp_edit_text2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp_edit_text2.getText().toString().trim().length() == 1) {
                    otp_edit_text3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp_edit_text3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp_edit_text3.getText().toString().trim().length() == 1) {
                    otp_edit_text4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp_edit_text4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp_edit_text4.getText().toString().trim().length() == 1) {
                    otp_edit_text5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp_edit_text5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp_edit_text5.getText().toString().trim().length() == 1) {
                    otp_edit_text6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void setInfo() {
        mListEditText = new ArrayList<>();
        mListEditText.add(otp_edit_text1);
        mListEditText.add(otp_edit_text2);
        mListEditText.add(otp_edit_text3);
        mListEditText.add(otp_edit_text4);
        mListEditText.add(otp_edit_text5);
        mListEditText.add(otp_edit_text6);
        tvInfoOtp.setText("Please enter the code we just sent to \nmobile number " + "+" + mAreaCode + mMobileNumber);
        tvVerifyOtpCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*boolean allow = true;
                for (int i = 0; i < mListEditText.size(); i++) {
                    if (mListEditText.get(i).getText().toString().trim().isEmpty()) {
                        allow = false;

                    }
                }
                if (allow) {
                    getConfirmation();
                } else {
                    Helper.showErrorMessage(getActivity(), "Please enter otp");
                }*/
                String phWithCode = mAreaCode + mMobileNumber;
                Bundle bundle = new Bundle();
                bundle.putString(AppoConstants.PHONECODE, mAreaCode);
                bundle.putString(AppoConstants.MOBILENUMBER, mMobileNumber);
                bundle.putString(AppoConstants.PHWITHCODE, phWithCode);
                if (mReplaceListener != null) {
                    mReplaceListener.onFragmentReplaceClick(bundle);
                }

            }
        });

    }

    private void startTimer() {
        seconds = 59;
        minutes = 4;
        if (newTimer == null) {
            newTimer = new Timer();
        }
        newTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtTimer.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds) + "s");
                            txtTimer.setVisibility(View.VISIBLE);
                            seconds -= 1;
                            if (seconds == 0 && minutes == 0) {
                                //btnResendOtp.setVisibility(View.VISIBLE);
                                txtTimer.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
                                seconds = 59;
                                minutes = 4;
                                txtTimer.setVisibility(View.GONE);
                                //minutes = minutes - 1;
                                newTimer.cancel();
                                newTimer = null;
                                btnResendOtp.setVisibility(View.VISIBLE);
                                //llVerification.setVisibility(View.VISIBLE);
                                //llVerification2.setVisibility(View.GONE);


                            } else if (seconds == 0 && minutes > 0) {
                                seconds = 59;
                                minutes = minutes - 1;

                            }
                        }

                    });
                }
            }

        }, 0, 1000);
    }


    public void inputOtp(String group) {


        for (int i = 0; i < group.length(); i++) {
            mListEditText.get(i).setText("" + group.charAt(i));
        }


        getConfirmation();
    }

    public void getConfirmation() {
        String otp = "";
        for (int i = 0; i < mListEditText.size(); i++) {
            otp = otp + mListEditText.get(i).getText().toString().trim();
        }
        String strCustomerPhone = mMobileNumber;
        String strCustomerCountryCode = mAreaCode;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Verifying OTP.");
        dialog.show();
        mainAPIInterface.verifiedGivenOtp(otp).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equalsIgnoreCase("200")) {
                        Helper.showLongMessage(getActivity(), "Successfully Verified!...");
                        String phWithCode = strCustomerCountryCode + strCustomerPhone;
                        Bundle bundle = new Bundle();
                        bundle.putString(AppoConstants.PHONECODE, strCustomerCountryCode);
                        bundle.putString(AppoConstants.MOBILENUMBER, strCustomerPhone);
                        bundle.putString(AppoConstants.PHWITHCODE, phWithCode);
                        if (mReplaceListener != null) {
                            mReplaceListener.onFragmentReplaceClick(bundle);
                        }

                    } else {
                        if (response.body().get("result").getAsString().equalsIgnoreCase("failed")) {
                            Helper.showErrorMessage(getActivity(), response.body().get("message").getAsString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mReplaceListener = (FragmentReplaceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent should implement FragmentReplaceListener");
        }

    }
}