package com.stuffer.stuffers.fragments.transactionpin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.fragments.dialog.AreaCodeDialog;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionPinFragment extends Fragment {
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
    private String selectedCountryNameCode = "";
    private String mDominicaAreaCode = "";
    private MyTextViewBold tvAreaCodeDo;
    private ArrayList<String> mAreaList;
    private AreaCodeDialog mAreaDialog;

    public TransactionPinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_pin, container, false);
        send_customer_otp = view.findViewById(R.id.send_customer_otp);
        confirm_otp = view.findViewById(R.id.confirm_otp);
        tvAreaCodeDo = (MyTextViewBold) view.findViewById(R.id.tvAreaCodeDo);
        edtCustomerMobileNumber = view.findViewById(R.id.edtCustomerMobileNumber);
        edtCustomerCountryCode = view.findViewById(R.id.edtCustomerCountryCode);
        edtOtpNumber = view.findViewById(R.id.edtOtpNumber);

        llVerification = view.findViewById(R.id.llVerification);
        llVerification2 = view.findViewById(R.id.llVerification2);
        btnResendOtp = view.findViewById(R.id.btnResendOtp);
        txtTimer = view.findViewById(R.id.txtTimer);
        mainAPIInterface = ApiUtils.getAPIService();
        newTimer = new Timer();
        edtCustomerCountryCode.setExcludedCountries(getString(R.string.info_exclude_countries));
        send_customer_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCustomerPhone = edtCustomerMobileNumber.getText().toString();
                strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();

                if (strCustomerPhone.length() < 5) {
                    edtCustomerMobileNumber.setFocusable(true);
                    edtCustomerMobileNumber.setError(getString(R.string.info_enter_valid_phone_number));
                } else {
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
                    confirmOtpRequest();
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

    public void updateAreaCode(int pos) {
        if (mAreaDialog != null) {
            mAreaDialog.dismiss();
        }
        mDominicaAreaCode = mAreaList.get(pos);
        tvAreaCodeDo.setText("Area Code : " + mDominicaAreaCode);
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

        param.addProperty("phone_number", "+" + strCustomerCountryCode + mDominicaAreaCode + edtCustomerMobileNumber.getText().toString().trim());

        //Log.e("TAG", "requestForOtp: " + param.toString());

        mainAPIInterface.getOtpforUserVerificaiton(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().equalsIgnoreCase("0")) {
                        Toast.makeText(getContext(), getString(R.string.info_verify_your_phone_number2), Toast.LENGTH_SHORT).show();
                    } else {

                        response.body();
                        //Log.e("TAG", "onResponse: " + response.body());
                        //Log.e("TAG", "onResponse: " + new Gson().toJson(response));
                        txtTimer.setVisibility(View.VISIBLE);
                        startTimer();

                        llVerification.setVisibility(View.GONE);
                        llVerification2.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(getContext(), getString(R.string.info_otp_request_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                //Log.e("tag", t.getMessage().toString());
            }
        });

    }


    private void startTimer() {
        if (newTimer == null) {
            newTimer = new Timer();
        }
        newTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtTimer.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
                        seconds -= 1;
                        if (seconds == 0) {
                            //btnResendOtp.setVisibility(View.VISIBLE);
                            txtTimer.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
                            seconds = 59;
                            txtTimer.setVisibility(View.GONE);
                            //minutes = minutes - 1;
                            newTimer.cancel();
                            newTimer = null;
                            btnResendOtp.setVisibility(View.VISIBLE);
                            llVerification.setVisibility(View.VISIBLE);
                            llVerification2.setVisibility(View.GONE);
                        }
                    }

                });
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
        param.addProperty("phone_number", "+" + strCustomerCountryCode + mDominicaAreaCode + edtCustomerMobileNumber.getText().toString().trim());

        mainAPIInterface.getOtpforUserVerificaiton(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    response.body();
                    //Log.e("TAG", "onResponse: " + response.body());
                    //Log.e("TAG", "onResponse: " + new Gson().toJson(response));
                    txtTimer.setVisibility(View.VISIBLE);
                    startTimer();
                    btnResendOtp.setVisibility(View.GONE);
                    llVerification.setVisibility(View.GONE);
                    llVerification2.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getActivity(), getString(R.string.info_otp_request_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                //Log.e("tag", t.getMessage().toString());
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
                        //Log.e("TAG", "onResponse: " + response.body());
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
                //Log.e("tag", t.getMessage().toString());
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

}
