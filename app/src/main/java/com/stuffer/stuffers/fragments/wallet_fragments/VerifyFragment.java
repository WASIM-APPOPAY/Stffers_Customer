package com.stuffer.stuffers.fragments.wallet_fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.MobileNumberRegistrationActivity;
import com.stuffer.stuffers.activity.wallet.SignupAcitivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.VerifiedListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyFragment extends Fragment {
    private static final String TAG = "VerifyFragment";
    String mParamNameCode, mParamCountryCode, mParamMobile, mParamEmail, mParamAdd,mParamZip,mParamCity,mParamStateId;
    View mView;
    private Timer newTimer;
    private MyTextView txtTimer, tvHeadingVerify, tvOtpHeading;
    int seconds = 12;
    int minutes = 0;
    LinearLayout llReOtp, llVerificationOtp;
    private CountryCodePicker edtCustomerCountryCode;
    MyEditText edtOtpNumber, edtCustomerMobileNumber;
    private VerifiedListener mVerifiedListener;
    private FloatingActionButton floatingConfirm, floatingReOtp;
    private ProgressDialog mProgress;
    MainAPIInterface mainAPIInterface;
    private boolean mVerify = true;
    private String strOtp;

    public VerifyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamNameCode = getArguments().getString(AppoConstants.COUNTRYNAMECODE);
            mParamCountryCode = getArguments().getString(AppoConstants.COUNTRYCODE);
            mParamMobile = getArguments().getString(AppoConstants.MOBILENUMBER);
            mParamEmail = getArguments().getString(AppoConstants.EMIAL_ID);
            mParamAdd = getArguments().getString(AppoConstants.ADDRESS);
            mParamStateId=getArguments().getString(AppoConstants.STATEID);
            mParamZip=getArguments().getString(AppoConstants.ZIPCODE2);
            mParamCity=getArguments().getString(AppoConstants.CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mainAPIInterface = ApiUtils.getAPIService();
        mView = inflater.inflate(R.layout.fragment_verify, container, false);
        txtTimer = mView.findViewById(R.id.txtTimer);
        llReOtp = mView.findViewById(R.id.llReOtp);
        llVerificationOtp = mView.findViewById(R.id.llVerificationOtp);
        tvHeadingVerify = mView.findViewById(R.id.tvHeadingVerify);
        tvOtpHeading = mView.findViewById(R.id.tvOtpHeading);
        edtCustomerCountryCode = mView.findViewById(R.id.edtCustomerCountryCode);
        edtOtpNumber = mView.findViewById(R.id.edtOtpNumber);
        edtCustomerMobileNumber = mView.findViewById(R.id.edtCustomerMobileNumber);
        floatingConfirm = mView.findViewById(R.id.floatingConfirm);
        floatingReOtp = mView.findViewById(R.id.floatingReOtp);

        String heading1 = "<font color='#00baf2'>" + "Verify your phone number, we have sent an OTP to this " + "</font>";
        String heading2 = "<font color='#FF0000'>" + "+" + mParamCountryCode + " " + mParamMobile + "</font>";
        String heading3 = "<font color='#00baf2'>" + " number." + "</font>";
        tvHeadingVerify.setText(Html.fromHtml(heading1 + heading2 + heading3));
        //Tap above, to Re-Send Otp to verify your number
        String headingOtp1 = "<font color='#00baf2'>" + "Tap above, to " + "</font>";
        String headingOtp2 = "<font color='#FF0000'>" + "Re-Sent Otp " + "</font>";
        String headingOtp3 = "<font color='#00baf2'>" + "to verify your number." + "</font>";
        tvOtpHeading.setText(Html.fromHtml(headingOtp1 + headingOtp2 + headingOtp3));
        edtCustomerCountryCode.setCountryForPhoneCode(Integer.parseInt(mParamCountryCode));


        edtCustomerCountryCode.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {

                //your code
                TextView title =(TextView)  dialog.findViewById(R.id.textView_title);
                title.setText(getString(R.string.info_cc_reg));
                dialog.dismiss();
            }

            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
                //your code
            }

            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {
                //your code
            }
        });

        edtCustomerMobileNumber.setText(mParamMobile);
        edtCustomerMobileNumber.setEnabled(false);
        edtCustomerMobileNumber.setClickable(false);

        startTimer();
        floatingConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strOtp = edtOtpNumber.getText().toString();
                if (strOtp.length() < 6) {
                    edtOtpNumber.setFocusable(true);
                    edtOtpNumber.setError(getString(R.string.info_enter_otp));
                } else {
                    getConfirmation();
                }
                //mVerifiedListener.onVerified(mParamNameCode, mParamCountryCode, mParamMobile);
            }
        });

        floatingReOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llVerificationOtp.setVisibility(View.VISIBLE);
                llReOtp.setVisibility(View.GONE);
                requestForOtp();
            }
        });
        return mView;
    }


    public void getConfirmation() {
        showProgress(getString(R.string.info_verifying_otp));
        mainAPIInterface.verifiedGivenOtp(edtOtpNumber.getText().toString().trim())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        hideProgress();
                        if (response.isSuccessful()) {
                            if (response.body().get("status").getAsString().equalsIgnoreCase("200")) {
                                Helper.showLongMessage(getActivity(), "Successfully Verified!...");
                                mVerifiedListener.onVerified(mParamNameCode, mParamCountryCode, mParamMobile);
                            } else {
                                if (response.body().get("result").getAsString().equalsIgnoreCase("failed")) {
                                    Helper.showErrorMessage(getActivity(), response.body().get("message").getAsString());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        hideProgress();

                    }
                });
    }

    public void showProgress(String msg) {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(msg);
        mProgress.show();
    }

    public void hideProgress() {
        if (mProgress != null) {
            mProgress.dismiss();
            mProgress = null;
        }
    }


    private void requestForOtp() {
        showProgress(getString(R.string.info_sending_otp));
        JsonObject param = new JsonObject();
        param.addProperty("mobileNumber", mParamMobile);
        if (BuildConfig.DEBUG){
            param.addProperty("hashKey", "Ovjaes9qCGm");
        }else {
            param.addProperty("hashKey", "Xhf2+h0fqyI");
        }
        param.addProperty("phoneCode", mParamCountryCode);


        mainAPIInterface.getOtpforUser(param).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideProgress();
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equalsIgnoreCase("200")) {
                        startTimer();
                    } else {
                        if (response.body().get("result").getAsString().equalsIgnoreCase("failed")) {
                            Helper.showErrorMessage(getActivity(), response.body().get("message").getAsString());
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.info_request_otp_failed), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                hideProgress();

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
                                llReOtp.setVisibility(View.VISIBLE);
                                llVerificationOtp.setVisibility(View.GONE);


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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mVerifiedListener = (VerifiedListener) context;
    }

    public void inputOtp(String group) {
        edtOtpNumber.setText(group);
        getConfirmation();

    }
}