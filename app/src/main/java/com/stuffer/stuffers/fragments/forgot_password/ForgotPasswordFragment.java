package com.stuffer.stuffers.fragments.forgot_password;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.FragmentReplaceListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.PasswordUtil;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {
    String mMobileNumber;
    String mAreaCode;
    private static final String TAG = "ForgotPasswordFragment";
    private MainAPIInterface mainAPIInterface;
    private ProgressDialog dialog;
    private MyEditText edtNewPassword, edtConfirmPassword;
    private MyButton btnSubmit;
    private LinearLayout ll1, ll2;
    private boolean mShowUser = false;
    private Dialog dialogError;
    private MyTextView tvPasswordPolicy;
    private MyTextView tvForgot, tvReset;
    private String mReset;
    private MyTextView tvNote;
    private FragmentReplaceListener mReplaceListener;


    public ForgotPasswordFragment() {
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
        View mView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        tvForgot = mView.findViewById(R.id.tvForgot);
        tvReset = mView.findViewById(R.id.tvReset);
        edtNewPassword = (MyEditText) mView.findViewById(R.id.edtNewPassword);
        edtConfirmPassword = (MyEditText) mView.findViewById(R.id.edtConfirmPassword);
        btnSubmit = (MyButton) mView.findViewById(R.id.btnSubmit);
        ll1 = (LinearLayout) mView.findViewById(R.id.ll1);
        tvPasswordPolicy = (MyTextView) mView.findViewById(R.id.tvPasswordPolicy);
        tvNote = (MyTextView) mView.findViewById(R.id.tvNote);
        if (mReset.equalsIgnoreCase("yes")) {
            tvReset.setVisibility(View.VISIBLE);
        } else {
            tvForgot.setVisibility(View.VISIBLE);
        }


        mainAPIInterface = ApiUtils.getAPIService();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppoPayApplication.isNetworkAvailable(getContext())) {
                    verify();
                } else {
                    Toast.makeText(getContext(), getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvPasswordPolicy.setText(Html.fromHtml(PasswordUtil.REGEX_PASSWORD_POLICY));
        tvNote.setText(Html.fromHtml(PasswordUtil.NOTE_PASSWORD));

        return mView;
    }


    private void gotoLogin() {
        if (dialogError != null) {
            dialogError.dismiss();
            Intent intent = new Intent(getContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }


    public void verify() {
        /*if (edtNewPassword.getText().toString().trim().isEmpty()) {
            edtNewPassword.setError(getString(R.string.info_new_password));
            edtNewPassword.setFocusable(true);
            edtNewPassword.requestFocus();
            return;
        }

        if (!PasswordUtil.PASSWORD_PATTERN.matcher(edtNewPassword.getText().toString().trim()).matches()) {
            edtNewPassword.setError("please follow the pattern below");
            return;
        }

        if (edtNewPassword.getText().toString().length() < 6) {
            edtNewPassword.setError(getString(R.string.info_password_length));
            edtNewPassword.setFocusable(true);
            edtNewPassword.requestFocus();
        }

        if (edtConfirmPassword.getText().toString().trim().isEmpty()) {
            edtConfirmPassword.setError(getString(R.string.info_confirm_your_password));
            edtConfirmPassword.setFocusable(true);
            edtConfirmPassword.requestFocus();
            return;
        }

        if (!PasswordUtil.PASSWORD_PATTERN.matcher(edtConfirmPassword.getText().toString().trim()).matches()) {
            edtConfirmPassword.setError("please follow the pattern below");
            return;
        }


        if (!edtNewPassword.getText().toString().trim().equals(edtConfirmPassword.getText().toString().trim())) {
            edtNewPassword.setError(getString(R.string.info_mismatch));
            edtNewPassword.requestFocus();
            edtConfirmPassword.setError(getString(R.string.info_mismatch));
            edtConfirmPassword.requestFocus();
            return;
        }


        if (mReset.equalsIgnoreCase("yes")) {
            Log.e(TAG, "verify: reset called");
            processRequest2();
        } else {
            Log.e(TAG, "verify: forgot called");
            processRequest1();
        }*/
        Bundle bundle = new Bundle();

        bundle.putBoolean(AppoConstants.OTPSUCCESS, true);

        if (mReplaceListener != null) {
            mReplaceListener.onFragmentReplaceClick(bundle);
        }


    }

    private void processRequest2() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_please_wait));
        dialog.show();
        mainAPIInterface.getReset_Password(mAreaCode, mMobileNumber, edtConfirmPassword.getText().toString().trim()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                Log.e(TAG, "onResponse: " + response.body());

                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    try {
                        JSONObject object = new JSONObject(res);
                        if (object.getString(AppoConstants.MESSAGE).equalsIgnoreCase(AppoConstants.SUCCESS)) {
                            Toast.makeText(getContext(), object.getString(AppoConstants.RESULT), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), object.getString(AppoConstants.MESSAGE), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void processRequest1() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_please_wait));
        dialog.show();
        JsonObject param = new JsonObject();
        param.addProperty("areacode", mAreaCode);
        param.addProperty("mobilenumber", mMobileNumber);
        param.addProperty("password", edtConfirmPassword.getText().toString().trim());
        param.addProperty("usertype", "CUSTOMER");
        //Log.e(TAG, "processRequest1: " + param.toString());


        mainAPIInterface.postForgotPassword(param).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                dialog.dismiss();
                //Log.e(TAG, "onResponse: "+response );
                if (response.isSuccessful()) {
                    String res = new Gson().toJson(response.body());
                    try {
                        JSONObject object = new JSONObject(res);
                        if (object.getString(AppoConstants.RESULT).equalsIgnoreCase("1")) {
                            /*Toast.makeText(getContext(), getString(R.string.info_forgot_password_success), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);*/

                            /*Bundle bundle = new Bundle();

                            bundle.putBoolean(AppoConstants.OTPSECRREN, true);

                            if (mReplaceListener != null) {
                                mReplaceListener.onFragmentReplaceClick(bundle);
                            }*/

                        } else {
                            Toast.makeText(getContext(), "change password request failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
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