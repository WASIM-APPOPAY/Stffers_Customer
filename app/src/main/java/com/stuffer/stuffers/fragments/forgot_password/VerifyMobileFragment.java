package com.stuffer.stuffers.fragments.forgot_password;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.fragments.dialog.AreaCodeDialog;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyMobileFragment extends Fragment {
    private String selectedCountryNameCode;
    private FragmentReplaceListener mReplaceListener;
    private FloatingActionButton send_customer_otp, confirm_otp;
    private Timer newTimer;
    MyEditText edtCustomerMobileNumber, edtOtpNumber;
    CountryCodePicker edtCustomerCountryCode;
    private String strCustomerPhone, strCustomerCountryCode;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private MyTextView txtTimer;
    LinearLayout llVerification, llVerification2;
    int seconds = 60;
    int minutes = 0;
    private MyTextView btnResendOtp;
    private String strOtp;
    private MyTextViewBold tvAreaCodeDo;
    private String mDominicaAreaCode = "";
    private ArrayList<String> mAreaList;
    private AreaCodeDialog mAreaDialog;
    private ProgressBar progress;

    public VerifyMobileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_mobile, container, false);
        send_customer_otp = view.findViewById(R.id.send_customer_otp);
        progress = view.findViewById(R.id.progress);
        confirm_otp = view.findViewById(R.id.confirm_otp);
        tvAreaCodeDo = (MyTextViewBold) view.findViewById(R.id.tvAreaCodeDo);
        edtCustomerMobileNumber = view.findViewById(R.id.edtCustomerMobileNumber);
        edtCustomerCountryCode = view.findViewById(R.id.edtCustomerCountryCode);
        edtOtpNumber = view.findViewById(R.id.edtOtpNumber);
        edtCustomerCountryCode.setExcludedCountries(getString(R.string.info_exclude_countries));
        llVerification = view.findViewById(R.id.llVerification);
        llVerification2 = view.findViewById(R.id.llVerification2);
        btnResendOtp = view.findViewById(R.id.btnResendOtp);
        txtTimer = view.findViewById(R.id.txtTimer);
        mainAPIInterface = ApiUtils.getAPIService();
        newTimer = new Timer();
        send_customer_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCustomerPhone = edtCustomerMobileNumber.getText().toString();
                strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();
                if (strCustomerPhone.length() < 5) {
                    edtCustomerMobileNumber.setFocusable(true);
                    edtCustomerMobileNumber.setError(getString(R.string.info_enter_valid_phone_number));
                } else {
                    Helper.hideKeyboard(edtCustomerMobileNumber, getContext());
                    requestForOtp();
                }
            }
        });

        btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCustomerPhone = edtCustomerMobileNumber.getText().toString();
                strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();
                if (strCustomerPhone.length() < 5) {
                    edtCustomerMobileNumber.setFocusable(true);
                    edtCustomerMobileNumber.setError(getString(R.string.info_enter_valid_phone_number));
                    return;
                }
                //reSendCustomerOtpRequest();
                btnResendOtp.setVisibility(View.GONE);
                reGenerateOtp();
                Helper.hideKeyboard(edtCustomerMobileNumber, getContext());

            }
        });

        confirm_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOtp = edtOtpNumber.getText().toString();
                if (strOtp.length() < 6) {
                    edtOtpNumber.setFocusable(true);
                    edtOtpNumber.setError(getString(R.string.info_enter_otp));
                } else {
                    Helper.hideKeyboard(edtOtpNumber, getContext());
                    //confirmOtpRequest();
                    getConfirmation();
                }
            }
        });


        edtCustomerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selectedCountryNameCode = edtCustomerCountryCode.getSelectedCountryNameCode();
                if (selectedCountryNameCode.equalsIgnoreCase("DO")) {
                    mDominicaAreaCode = "";
                    tvAreaCodeDo.setVisibility(View.GONE);
                } else {
                    mDominicaAreaCode = "";
                    tvAreaCodeDo.setVisibility(View.GONE);
                }
            }
        });
        edtCustomerCountryCode.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                //your code
                TextView title = (TextView) dialog.findViewById(R.id.textView_title);
                title.setText(getString(R.string.info_cc_reg));
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
        tvAreaCodeDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAreaCodes();
            }
        });

        return view;
    }

    private void getAreaCodes() {
        mAreaList = new ArrayList<String>();
        mAreaList.add("809");
        mAreaList.add("829");
        mAreaList.add("849");

        mAreaDialog = new AreaCodeDialog();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.TITLE, "Please Select Area Code");
        bundle.putStringArrayList(AppoConstants.INFO, mAreaList);
        mAreaDialog.setArguments(bundle);
        mAreaDialog.setCancelable(false);
        mAreaDialog.show(getChildFragmentManager(), mAreaDialog.getTag());

    }

    /**
     * request for otp start timer the invisble current screen ==> show otp screen
     */
    private void requestForOtp() {
        strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait sending OTP.");
        dialog.show();
        JsonObject param = new JsonObject();

        String smsHash = Helper.getHashCode();

        param.addProperty("hashKey", smsHash);

        param.addProperty("mobileNumber", edtCustomerMobileNumber.getText().toString().trim());
        param.addProperty("phoneCode", strCustomerCountryCode);

        //Log.e("TAG", "requestForOtp: " + param.toString());

        mainAPIInterface.getOtpforUser(param).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equalsIgnoreCase("200")) {
                        progress.setVisibility(View.VISIBLE);
                        txtTimer.setVisibility(View.VISIBLE);
                        startTimer();
                        llVerification.setVisibility(View.GONE);
                        llVerification2.setVisibility(View.VISIBLE);
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
                dialog.dismiss();
                //Log.e("tag", t.getMessage().toString());
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
                                llVerification.setVisibility(View.VISIBLE);
                                llVerification2.setVisibility(View.GONE);
//                                llReOtp.setVisibility(View.VISIBLE);
//                                llVerificationOtp.setVisibility(View.GONE);


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


    private void reGenerateOtp() {
        strCustomerPhone = edtCustomerMobileNumber.getText().toString();
        strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();

        if (strCustomerPhone.length() < 5) {
            edtCustomerMobileNumber.setFocusable(true);
            edtCustomerMobileNumber.setError(getString(R.string.info_enter_valid_phone_number));
            return;
        }

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.info_generating_otp));
        dialog.show();

        JsonObject param = new JsonObject();
        String smsHash = Helper.getHashCode();

        param.addProperty("hashKey", smsHash);
        param.addProperty("mobileNumber", edtCustomerMobileNumber.getText().toString().trim());
        param.addProperty("phoneCode", strCustomerCountryCode);

        mainAPIInterface.getOtpforUser(param).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equalsIgnoreCase("200")) {
                        progress.setVisibility(View.VISIBLE);
                        txtTimer.setVisibility(View.VISIBLE);
                        startTimer();
                        llVerification.setVisibility(View.GONE);
                        llVerification2.setVisibility(View.VISIBLE);
                    } else {
                        if (response.body().get("result").getAsString().equalsIgnoreCase("failed")) {
                            Helper.showErrorMessage(getActivity(), response.body().get("message").getAsString());
                        }
                    }


                } else {
                    Toast.makeText(getActivity(), getString(R.string.info_request_otp_failed), Toast.LENGTH_LONG).show();
                }

                /*if (response.isSuccessful()) {
                    response.body();
                    txtTimer.setVisibility(View.VISIBLE);
                    startTimer();
                    btnResendOtp.setVisibility(View.GONE);
                    llVerification.setVisibility(View.GONE);
                    llVerification2.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getActivity(), getString(R.string.info_otp_request_failed), Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e("tag", t.getMessage().toString());
            }
        });
    }


    public void inputOtp(String group) {
        edtOtpNumber.setText(group);
        getConfirmation();
    }

    public void getConfirmation() {
        strCustomerPhone = edtCustomerMobileNumber.getText().toString();
        strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Verifying OTP.");
        dialog.show();
        mainAPIInterface.verifiedGivenOtp(edtOtpNumber.getText().toString().trim())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body().get("status").getAsString().equalsIgnoreCase("200")) {
                                //Helper.showLongMessage(getActivity(), "Successfully Verified!...");
                                String phWithCode = strCustomerCountryCode + mDominicaAreaCode + strCustomerPhone;
                                Bundle bundle = new Bundle();
                                bundle.putString(AppoConstants.PHONECODE, strCustomerCountryCode);
                                bundle.putString(AppoConstants.MOBILENUMBER, strCustomerPhone);
                                bundle.putString(AppoConstants.PHWITHCODE, phWithCode);
                                progress.setVisibility(View.GONE);
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

    private void confirmOtpRequest() {
        strCustomerPhone = edtCustomerMobileNumber.getText().toString();
        strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();


        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Verifying OTP.");
        dialog.show();
        JsonObject params = new JsonObject();
        params.addProperty("phone_number", "+" + strCustomerCountryCode + mDominicaAreaCode + edtCustomerMobileNumber.getText().toString().trim());
        params.addProperty("otp_number", edtOtpNumber.getText().toString().trim());


        mainAPIInterface.getVerificationStatus(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().equalsIgnoreCase("false")) {
                        Toast.makeText(getActivity(), getString(R.string.info_verification_failed_given_otp), Toast.LENGTH_SHORT).show();
                    } else {
                        //// Log.e("TAG", "onResponse: " + response.body());
                        String phWithCode = strCustomerCountryCode + mDominicaAreaCode + strCustomerPhone;
                        Bundle bundle = new Bundle();
                        bundle.putString(AppoConstants.PHONECODE, strCustomerCountryCode);
                        bundle.putString(AppoConstants.MOBILENUMBER, strCustomerPhone);
                        bundle.putString(AppoConstants.PHWITHCODE, phWithCode);
                        if (mReplaceListener != null) {
                            mReplaceListener.onFragmentReplaceClick(bundle);
                        }


                    }

                } else {
                    if (newTimer != null) {
                        newTimer.cancel();
                        newTimer = null;
                        txtTimer.setVisibility(View.GONE);
                    }
                    Toast.makeText(getActivity(), getString(R.string.info_verification_failed_given_otp), Toast.LENGTH_SHORT).show();
                    //btnResendOtp.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
//                Log.e("tag", t.getMessage().toString());
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

    @Override
    public void onDetach() {
        if (newTimer != null) {
            newTimer.cancel();
        }
        super.onDetach();

    }

    public void updateAreaCode(int pos) {
        if (mAreaDialog != null) {
            mAreaDialog.dismiss();
        }
        mDominicaAreaCode = mAreaList.get(pos);
        tvAreaCodeDo.setText("Area Code : " + mDominicaAreaCode);
    }
}