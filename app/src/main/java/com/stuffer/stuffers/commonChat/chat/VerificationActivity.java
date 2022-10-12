package com.stuffer.stuffers.commonChat.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.HomeActivity;
import com.stuffer.stuffers.activity.wallet.HomeActivity2;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.chatUtils.Constants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    private AlertDialog dialogVerify;
    private MyTextView tvAgree;
    private String mMobileNumber, mAreaCode;
    private Context mContext;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private boolean authInProgress;
    private DatabaseReference userRef;
    private MyTextViewBold retryTimer;
    private CountDownTimer countDownTimer;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    ArrayList<EditText> mList;
    private String mVerificationId;
    private ChatHelper chatHelper;
    private EditText otp_edit_text1, otp_edit_text2, otp_edit_text3, otp_edit_text4, otp_edit_text5, otp_edit_text6;
    private static final String TAG = "VerificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        progressDialog = new ProgressDialog(this);
        mMobileNumber = getIntent().getStringExtra(Constants.MOBILENUMBER);
        mAreaCode = getIntent().getStringExtra(Constants.AREACODE);
        chatHelper = new ChatHelper(this);
        setContentView(R.layout.activity_verification);
        otp_edit_text1 = findViewById(R.id.otp_edit_text1);
        otp_edit_text2 = findViewById(R.id.otp_edit_text2);
        otp_edit_text3 = findViewById(R.id.otp_edit_text3);
        otp_edit_text4 = findViewById(R.id.otp_edit_text4);
        otp_edit_text5 = findViewById(R.id.otp_edit_text5);
        otp_edit_text6 = findViewById(R.id.otp_edit_text6);
        retryTimer = findViewById(R.id.retryTimer);
        mList = new ArrayList<>();
        mList.add(otp_edit_text1);
        mList.add(otp_edit_text2);
        mList.add(otp_edit_text3);
        mList.add(otp_edit_text4);
        mList.add(otp_edit_text5);
        mList.add(otp_edit_text6);
        mAuth = FirebaseAuth.getInstance();
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


        tvAgree = (MyTextView) findViewById(R.id.tvAgree);
        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showVerify();
                String otp = "";
                for (int i = 0; i < mList.size(); i++) {
                    otp = otp + mList.get(i).getText().toString().trim();
                }
                signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId, otp));
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: mobile : " + mAreaCode + mMobileNumber);
                initAuth("+" + mAreaCode + mMobileNumber);
            }
        }, 500);
    }

    //com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The format of the phone number provided is incorrect. Please enter the phone number in a format that can be parsed into E.164 format. E.164 phone numbers are written in the format [+][country code][subscriber number including area code]. [ Invalid format. ]
    private void initAuth(String phone) {
        if (mContext != null) {
            showProgress(1);

            PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onCodeAutoRetrievalTimeOut(String s) {
                            super.onCodeAutoRetrievalTimeOut(s);
                            Log.e(TAG, "onCodeAutoRetrievalTimeOut: " + s);
                            onCodeSent(mVerificationId, mResendToken);
                        }

                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            progressDialog.dismiss();
                            String smsCode = phoneAuthCredential.getSmsCode();
                            Log.e(TAG, "onVerificationCompleted: " + smsCode);
                            for (int i = 0; i < smsCode.length(); i++) {
                                mList.get(i).setText("" + smsCode.charAt(i));
                            }

                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {

                            e.printStackTrace();
                            authInProgress = false;
                            progressDialog.dismiss();
                            countDownTimer.cancel();
                            retryTimer.setVisibility(View.VISIBLE);
                            retryTimer.setText(getString(R.string.resend));
                            retryTimer.setOnClickListener(view -> initAuth("+" + mAreaCode + mMobileNumber));
                            Toast.makeText(VerificationActivity.this, getString(R.string.error_detail) + (e.getMessage() != null ? "\n" + e.getMessage() : ""), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            //super.onCodeSent(verificationId, forceResendingToken);

                            authInProgress = true;
                            progressDialog.dismiss();
                            mVerificationId = verificationId;
                            Log.e(TAG, "onCodeSent: " + verificationId);
                            mResendToken = forceResendingToken;
                            //verificationMessage.setText(getString(R.string.otp_sent) + " " + phoneNumberInPrefs);
                            retryTimer.setVisibility(View.GONE);

                        }
                    });
            startCountdown();
        }
    }

    private void startCountdown() {
        retryTimer.setOnClickListener(null);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                if (retryTimer != null) {
                    retryTimer.setText(String.valueOf(l / 1000));
                }
            }

            @Override
            public void onFinish() {
                if (retryTimer != null) {
                    retryTimer.setText(getText(R.string.resend));
                    retryTimer.setOnClickListener(view -> initAuth("+" + mAreaCode + mMobileNumber));
                }
            }
        }.start();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        showProgress(2);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            progressDialog.setMessage(getString(R.string.logging_in));
            login();
        }).addOnFailureListener(e -> {
            Toast.makeText(VerificationActivity.this, getString(R.string.error_detail) + (e.getMessage() != null ? "\n" + e.getMessage() : ""), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            authInProgress = false;
        });
    }

    private void login() {
        authInProgress = true;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference(ChatHelper.REF_USERS).child(mAreaCode + mMobileNumber);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    try {
                        User user = dataSnapshot.getValue(User.class);
                        if (User.validate(user)) {
                            chatHelper.setLoggedInUser(user);
                            done(false);
                        } else {
                            createUser(new User(mAreaCode + mMobileNumber, "", ""));
                        }
                    } catch (Exception ex) {
                        createUser(new User(mAreaCode + mMobileNumber, "", ""));
                    }
                } else {
                    createUser(new User(mAreaCode + mMobileNumber, "", ""));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void createUser(final User newUser) {
        userRef.setValue(newUser).addOnSuccessListener(aVoid -> {
            chatHelper.setLoggedInUser(newUser);
            done(true);
        }).addOnFailureListener(e -> Toast.makeText(VerificationActivity.this, R.string.error_create_user, Toast.LENGTH_LONG).show());
    }

    //Go to main activity
    private void done(boolean newUser) {
        if (newUser) {
            Intent intent = new Intent(mContext, BasicInfoActivity.class);
            startActivity(intent);
            VerificationActivity.this.finish();


        } else {


            /*String keyUserDetails = DataVaultManager.getInstance(this).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
            if (!StringUtils.isEmpty(keyUserDetails)) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                VerificationActivity.this.finish();
            } else {
                Intent intent = new Intent(this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                VerificationActivity.this.finish();
            }*/
            Intent intent = new Intent(this, HomeActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            VerificationActivity.this.finish();
        }

    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        mContext = null;
        super.onDestroy();
    }

    private void showProgress(int i) {
        String title = (i == 1) ? getString(R.string.otp_sending) : getString(R.string.otp_verifying);
        String message = (i == 1) ? (getString(R.string.otp_sending_msg) + " " + mAreaCode + mMobileNumber) : getString(R.string.otp_verifying_msg);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }


    private void nextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogVerify.dismiss();
                Intent intentHome = new Intent(VerificationActivity.this, HomeActivity.class);
                startActivity(intentHome);
            }
        }, 500);
    }
}