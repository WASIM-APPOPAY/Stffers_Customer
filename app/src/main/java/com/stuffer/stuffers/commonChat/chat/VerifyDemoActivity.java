package com.stuffer.stuffers.commonChat.chat;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.Registration;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.VerifiedListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import org.aviran.cookiebar2.CookieBar;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyDemoActivity extends AppCompatActivity {
    String mParamNameCode, mParamCountryCode, mParamMobile, mParamEmail, mParamAdd, mParamZip, mParamCity, mParamStateId;
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
    private EditText otp_edit_text1, otp_edit_text2, otp_edit_text3, otp_edit_text4, otp_edit_text5, otp_edit_text6;
    private ArrayList<EditText> mListEditText;
    private ProgressBar progress;
    private MyTextView tvVerifyOtpCommon;

    private
    @ColorRes
    int getThemeColor(Context context, int color, String theme) {
        return context.getResources().getIdentifier(theme, "color", getPackageName());
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    //@SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_demo);
        setupActionBar();
        CookieBar.build(VerifyDemoActivity.this)
                .setTitle("appOpay")
                .setMessage("Your One Time Password is 123456")
                .setIcon(R.drawable.appopay_notification_icon1)
                //.setIconAnimation(R.animator.spin)
                .setEnableAutoDismiss(false)

                .setBackgroundColor(R.color.cookie_bar_color)
                .show();


        mainAPIInterface = ApiUtils.getAPIService();
        mParamCountryCode = getIntent().getStringExtra(AppoConstants.AREACODE);
        mParamMobile = getIntent().getStringExtra(AppoConstants.MOBILENUMBER);

        txtTimer = findViewById(R.id.txtTimer);
        llReOtp = findViewById(R.id.llReOtp);
        progress = findViewById(R.id.progress);
        llVerificationOtp = findViewById(R.id.llVerificationOtp);
        tvHeadingVerify = findViewById(R.id.tvHeadingVerify);
        tvOtpHeading = findViewById(R.id.tvOtpHeading);
        edtCustomerCountryCode = findViewById(R.id.edtCustomerCountryCode);
        edtOtpNumber = findViewById(R.id.edtOtpNumber);
        edtCustomerMobileNumber = findViewById(R.id.edtCustomerMobileNumber);
        otp_edit_text1 = findViewById(R.id.otp_edit_text1);
        otp_edit_text2 = findViewById(R.id.otp_edit_text2);
        otp_edit_text3 = findViewById(R.id.otp_edit_text3);
        otp_edit_text4 = findViewById(R.id.otp_edit_text4);
        otp_edit_text5 = findViewById(R.id.otp_edit_text5);
        otp_edit_text6 = findViewById(R.id.otp_edit_text6);
        tvVerifyOtpCommon = findViewById(R.id.tvVerifyOtpCommon);
        setInfo();
        setOtpBoxFocus();
        //floatingConfirm = findViewById(R.id.floatingConfirm);
        floatingReOtp = findViewById(R.id.floatingReOtp);

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
                TextView title = (TextView) dialog.findViewById(R.id.textView_title);
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


        floatingReOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llVerificationOtp.setVisibility(View.VISIBLE);
                llReOtp.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                requestForOtp();
            }
        });

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

        tvVerifyOtpCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder otp = new StringBuilder();
                boolean allow = true;
                for (int i = 0; i < mListEditText.size(); i++) {
                    if (mListEditText.get(i).getText().toString().trim().isEmpty()) {
                        allow = false;

                    }
                    otp.append(mListEditText.get(i).getText().toString().trim());
                }
                if (allow) {
                    //getConfirmation();

                    if ("123456".equalsIgnoreCase(otp.toString())) {
                        //mVerifiedListener.onVerified(mParamNameCode, mParamCountryCode, mParamMobile);
                        progress.setVisibility(View.GONE);
                        CookieBar.dismiss(VerifyDemoActivity.this);
                        Intent mIntent = new Intent(VerifyDemoActivity.this, Registration.class);
                        mIntent.putExtra(AppoConstants.AREACODE, mParamCountryCode);
                        mIntent.putExtra(AppoConstants.MOBILENUMBER, mParamMobile);
                        startActivity(mIntent);
                        finish();
                    } else {
                        Helper.showErrorMessage(VerifyDemoActivity.this, "Please enter valid otp");
                    }
                } else {
                    Helper.showErrorMessage(VerifyDemoActivity.this, "Please enter otp");
                }


            }
        });

    }

    private void setupActionBar() {
        //MyTextViewBold common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        //common_toolbar_title.setText(mTitle);
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                                //Helper.showLongMessage(VerifyDemoActivity.this, getString(R.string.info_verified));
                                Toast.makeText(VerifyDemoActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                mVerifiedListener.onVerified(mParamNameCode, mParamCountryCode, mParamMobile);
                                progress.setVisibility(View.GONE);
                            } else {
                                if (response.body().get("result").getAsString().equalsIgnoreCase("failed")) {
                                    Helper.showErrorMessage(VerifyDemoActivity.this, response.body().get("message").getAsString());
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
        mProgress = new ProgressDialog(VerifyDemoActivity.this);
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
        String hashCode = Helper.getHashCode();
        param.addProperty("hashKey", hashCode);
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
                            Helper.showErrorMessage(VerifyDemoActivity.this, response.body().get("message").getAsString());
                        }
                    }
                } else {
                    Toast.makeText(VerifyDemoActivity.this, getString(R.string.info_request_otp_failed), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

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
                if (VerifyDemoActivity.this != null) {
                    VerifyDemoActivity.this.runOnUiThread(new Runnable() {
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


}