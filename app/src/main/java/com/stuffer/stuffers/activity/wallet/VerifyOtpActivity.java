package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stuffer.stuffers.AppoPayApplication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity {
    MyEditText edtOtpNumber;
    MyTextView btnResendOtp;
    MyTextView txtTimer;
    FloatingActionButton fConfirm_Otp;
    ProgressDialog dialog;
    int seconds = 60;
    int minutes = 0;
    //Declare the timer
    Timer newTimer;
    private String sentOtp;
    private String phNoWithCode;
    private MainAPIInterface mainAPIInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        mainAPIInterface = ApiUtils.getAPIService();
        edtOtpNumber = findViewById(R.id.edtOtpNumber);
        btnResendOtp = findViewById(R.id.btnResendOtp);
        txtTimer = findViewById(R.id.txtTimer);
        fConfirm_Otp = findViewById(R.id.confirm_otp);
        phNoWithCode = getIntent().getStringExtra("phone_number");


        fConfirm_Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentOtp = edtOtpNumber.getText().toString();
                if (sentOtp.length() < 6) {
                    edtOtpNumber.setFocusable(true);
                    edtOtpNumber.setError(getString(R.string.info_enter_otp));
                } else {
                    hideKeyboard(fConfirm_Otp);
                    if (AppoPayApplication.isNetworkAvailable(VerifyOtpActivity.this)) {
                        confirmOtpRequest();
                    } else {
                        Toast.makeText(VerifyOtpActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        setTimer();

        btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppoPayApplication.isNetworkAvailable(VerifyOtpActivity.this)) {
                    generateOtp();
                } else {
                    Toast.makeText(VerifyOtpActivity.this, getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void setTimer() {
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
                            btnResendOtp.setVisibility(View.VISIBLE);
                            txtTimer.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
                            seconds = 59;
                            txtTimer.setVisibility(View.GONE);
                            //minutes = minutes - 1;
                            newTimer.cancel();
                            newTimer = null;
                        }
                    }

                });
            }

        }, 0, 1000);
    }

    private void confirmOtpRequest() {
        dialog = new ProgressDialog(VerifyOtpActivity.this);
        dialog.setMessage(getString(R.string.info_verifying_otp));
        dialog.show();
        JsonObject params = new JsonObject();
        params.addProperty("phone_number", phNoWithCode);
        params.addProperty("otp_number", edtOtpNumber.getText().toString().trim());


        mainAPIInterface.getVerificationStatus(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().equalsIgnoreCase("false")){
                        Toast.makeText(VerifyOtpActivity.this, getString(R.string.info_verification_failed_given_otp), Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                } else {
                    if (newTimer != null) {
                        newTimer.cancel();
                        txtTimer.setVisibility(View.GONE);
                    }

                    //Toast.makeText(VerifyOtpActivity.this, "" + "Verification failed with the given otp ", Toast.LENGTH_SHORT).show();
                    Toast.makeText(VerifyOtpActivity.this, getString(R.string.info_verification_failed_given_otp), Toast.LENGTH_SHORT).show();
                    btnResendOtp.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                //Log.e("tag", t.getMessage().toString());
            }
        });


    }

    private void generateOtp() {
        dialog = new ProgressDialog(VerifyOtpActivity.this);
        dialog.setMessage(getString(R.string.info_generating_otp));
        dialog.show();
        JsonObject param = new JsonObject();

        param.addProperty("phone_number", phNoWithCode );

        mainAPIInterface.getOtpforUserVerificaiton(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    response.body();
                    //Log.e("TAG", "onResponse: " + response.body());
                    //Log.e("TAG", "onResponse: " + new Gson().toJson(response));
                    txtTimer.setVisibility(View.VISIBLE);
                    setTimer();
                    btnResendOtp.setVisibility(View.GONE);

                } else {
                    Toast.makeText(VerifyOtpActivity.this, getString(R.string.info_otp_request_failed), Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
        super.onBackPressed();


    }
}
