package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.communicator.AreaSelectListener;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomAlreadyFragment;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.fragments.dialog.AreaCodeDialog;
import com.stuffer.stuffers.fragments.dialog.BankDialog;
import com.stuffer.stuffers.utils.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileNumberRegistrationActivity extends AppCompatActivity implements AreaSelectListener {
    private static final String TAG = "MobileNumberRegistratio";
    CountryCodePicker edtCustomerCountryCode;
    MyEditText edtCustomerMobileNumber, edtOtpNumber;

    LinearLayout llVerification, llVerification2;

    FloatingActionButton send_customer_otp, confirm_otp;

    MyTextView btnResendOtp;
    MyTextView txtTimer;
    ProgressDialog dialog;
    MainAPIInterface mainAPIInterface;
    int seconds = 60;
    int minutes = 0;

    //Declare the timer
    Timer newTimer;

    MyTextView customer_login;
    String strCustomerPhone;
    String strCustomerCountryCode;

    String strOtp, sentOtp;
    private String selectedCountryNameCode;
    private MyTextViewBold tvAreaCodeDo;
    private String mDominicaAreaCode = "";
    private ArrayList<String> mAreaList;
    private AreaCodeDialog mAreaDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_mobile_verification);

        mainAPIInterface = ApiUtils.getAPIService();
        tvAreaCodeDo = (MyTextViewBold) findViewById(R.id.tvAreaCodeDo);
        edtCustomerCountryCode = (CountryCodePicker) findViewById(R.id.edtCustomerCountryCode);

        send_customer_otp = (FloatingActionButton) findViewById(R.id.send_customer_otp);
        confirm_otp = (FloatingActionButton) findViewById(R.id.confirm_otp);

        edtCustomerMobileNumber = (MyEditText) findViewById(R.id.edtCustomerMobileNumber);
        edtOtpNumber = (MyEditText) findViewById(R.id.edtOtpNumber);

        customer_login = (MyTextView) findViewById(R.id.customer_login);

        btnResendOtp = (MyTextView) findViewById(R.id.btnResendOtp);

        llVerification = (LinearLayout) findViewById(R.id.llVerification);
        llVerification2 = (LinearLayout) findViewById(R.id.llVerification2);

        newTimer = new Timer();
        txtTimer = (MyTextView) findViewById(R.id.txtTimer);

        edtCustomerCountryCode.setExcludedCountries(getString(R.string.info_exclude_countries));

        customer_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(MobileNumberRegistrationActivity.this, SignInActivity.class);
                startActivity(newIntent);
                finish();

            }
        });

        send_customer_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCustomerPhone = edtCustomerMobileNumber.getText().toString();
                strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();
                selectedCountryNameCode = edtCustomerCountryCode.getSelectedCountryNameCode();
                //Log.e(TAG, "onClick: "+strCustomerCountryCode );


                if (strCustomerPhone.length() < 5) {
                    edtCustomerMobileNumber.setFocusable(true);
                    edtCustomerMobileNumber.setError(getString(R.string.info_enter_valid_phone_number));
                } else {
                    try {
                        Helper.hideKeyboard(edtCustomerMobileNumber, MobileNumberRegistrationActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //requestForOtp();

                    if (AppoPayApplication.isNetworkAvailable(MobileNumberRegistrationActivity.this)) {
                        verifyMobileNumber(strCustomerCountryCode + mDominicaAreaCode + edtCustomerMobileNumber.getText().toString().trim());
                    } else {
                        Toast.makeText(MobileNumberRegistrationActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_LONG).show();
                    }


                }

                /*Intent intent = new Intent(MobileNumberRegistrationActivity.this, SignupAcitivity.class);
                String phWithCode = strCustomerCountryCode + strCustomerPhone;
                intent.putExtra(AppoConstants.PHONECODE, strCustomerCountryCode);
                intent.putExtra(AppoConstants.MOBILENUMBER, strCustomerPhone);
                intent.putExtra(AppoConstants.COUNTRYNAMECODE,selectedCountryNameCode);

                intent.putExtra(AppoConstants.PHWITHCODE, phWithCode);
                startActivity(intent);
                finish();*/


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

                if (AppoPayApplication.isNetworkAvailable(MobileNumberRegistrationActivity.this)) {
                    reGenerateOtp();
                    btnResendOtp.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MobileNumberRegistrationActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_LONG).show();
                    btnResendOtp.setVisibility(View.VISIBLE);
                }

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
                    tvAreaCodeDo.setVisibility(View.VISIBLE);
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
        mAreaDialog.show(getSupportFragmentManager(), mAreaDialog.getTag());

    }

    private void verifyMobileNumber(String phoneNumber) {
        //Log.e("TAG", "verifyMobileNumber: " + phoneNumber);
        dialog = new ProgressDialog(MobileNumberRegistrationActivity.this);
        dialog.setMessage(getString(R.string.info_verifying_mobile_number));
        dialog.show();
        mainAPIInterface.getMobileNUmberStatus(phoneNumber).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    String str = new Gson().toJson(response.body());
                    //Log.e("TAG", "onResponse: str" + str);
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.getString("message").equalsIgnoreCase("success") && jsonObject.getBoolean("result")) {
                            if (AppoPayApplication.isNetworkAvailable(MobileNumberRegistrationActivity.this)) {
                                requestForOtp();
                            } else {
                                Toast.makeText(MobileNumberRegistrationActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            BottomAlreadyFragment fragmentBottomAlready = new BottomAlreadyFragment();
                            fragmentBottomAlready.show(getSupportFragmentManager(), fragmentBottomAlready.getTag());
                            fragmentBottomAlready.setCancelable(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Log.e("TAG", "onResponse: failed called");
                    Toast.makeText(MobileNumberRegistrationActivity.this, getString(R.string.error_phone_verification_failed), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e("tag", t.getMessage().toString());
            }
        });


    }

    private void requestForOtp() {
        strCustomerCountryCode = edtCustomerCountryCode.getSelectedCountryCode();
        dialog = new ProgressDialog(MobileNumberRegistrationActivity.this);
        dialog.setMessage(getString(R.string.info_sending_otp));
        dialog.show();
        JsonObject param = new JsonObject();

        param.addProperty("phone_number", "+" + strCustomerCountryCode + mDominicaAreaCode + edtCustomerMobileNumber.getText().toString().trim());
        //{"phone_number":"+919836683269"}

        //Log.e("TAG", "requestForOtp: " + param.toString());

        mainAPIInterface.getOtpforUserVerificaiton(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().equalsIgnoreCase("0")) {
                        //Toast.makeText(MobileNumberRegistrationActivity.this, "Please verify your phone number", Toast.LENGTH_LONG).show();
                        Toast.makeText(MobileNumberRegistrationActivity.this, getString(R.string.info_verify_your_phone_number2), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(MobileNumberRegistrationActivity.this, getString(R.string.info_request_otp_failed), Toast.LENGTH_LONG).show();
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
                runOnUiThread(new Runnable() {
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

        dialog = new ProgressDialog(MobileNumberRegistrationActivity.this);
        //dialog.setMessage("Generating otp for verification.");
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
                    Toast.makeText(MobileNumberRegistrationActivity.this, getString(R.string.info_otp_request_failed), Toast.LENGTH_LONG).show();
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
        dialog = new ProgressDialog(MobileNumberRegistrationActivity.this);
        //dialog.setMessage("Verifying OTP.");
        dialog.setMessage(getString(R.string.info_verifying_otp));
        dialog.show();
        JsonObject params = new JsonObject();
        params.addProperty("phone_number", "+" + strCustomerCountryCode + mDominicaAreaCode + edtCustomerMobileNumber.getText().toString().trim());
        params.addProperty("otp_number", edtOtpNumber.getText().toString().trim());
        //{"phone_number":"+919836683269"}
        //{"otp_number":"986578"}



        mainAPIInterface.getVerificationStatus(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().equalsIgnoreCase("false")) {
                        Toast.makeText(MobileNumberRegistrationActivity.this, getString(R.string.info_verification_failed_given_otp), Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(MobileNumberRegistrationActivity.this, SignupAcitivity.class);
                        String phWithCode = strCustomerCountryCode + mDominicaAreaCode + strCustomerPhone;

                        intent.putExtra(AppoConstants.PHONECODE, strCustomerCountryCode);
                        intent.putExtra(AppoConstants.MOBILENUMBER, strCustomerPhone);
                        intent.putExtra(AppoConstants.COUNTRYNAMECODE, selectedCountryNameCode);

                        intent.putExtra(AppoConstants.PHWITHCODE, phWithCode);
                        startActivity(intent);
                        finish();

                    }

                } else {
                    if (newTimer != null) {
                        newTimer.cancel();
                        txtTimer.setVisibility(View.GONE);
                    }
                    //Toast.makeText(MobileNumberRegistrationActivity.this, "" + "Verification failed with the given otp ", Toast.LENGTH_LONG).show();
                    Toast.makeText(MobileNumberRegistrationActivity.this, getString(R.string.info_verification_failed_given_otp), Toast.LENGTH_LONG).show();
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
    public void onAreaSelected(int pos) {
        if (mAreaDialog != null) {
            mAreaDialog.dismiss();
        }
        mDominicaAreaCode = mAreaList.get(pos);
        tvAreaCodeDo.setText("Area Code : " + mDominicaAreaCode);
    }
}
