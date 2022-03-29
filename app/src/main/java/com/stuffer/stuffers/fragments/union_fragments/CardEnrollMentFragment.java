package com.stuffer.stuffers.fragments.union_fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainUAPIInterface;
import com.stuffer.stuffers.communicator.OnBankSubmit;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class CardEnrollMentFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CardEnrollMentFragment";
    private MyButton btnCardEnrollment;
    private String enrollMentUrl;
    private TextInputEditText mTiedAccNo;
    private ProgressDialog mProgress;
    private JsonObject mRoot;
    private MainUAPIInterface apiServiceUNIONPay;
    String firstName, lastName, fullName;
    private String phoneCode;
    private Long senderMobileNumber;
    private String walletAccountNumber;
    private String phWithCode;
    private Dialog mDialogCard;
    private TextInputEditText mTiedFname, mTiedLname;
    private MyTextView tvCountryCodeU, tvMobNumU;
    private int mPrdNo;
    private String newNumber;
    private OnBankSubmit mConfirmLsitener;

    public CardEnrollMentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_card_enroll_ment, container, false);
        apiServiceUNIONPay = ApiUtils.getApiServiceUNIONPay();
        btnCardEnrollment = (MyButton) mView.findViewById(R.id.btnCardEnrollment);
        mTiedAccNo = (TextInputEditText) mView.findViewById(R.id.mTiedAccNo);
        mTiedFname = (TextInputEditText) mView.findViewById(R.id.mTiedFname);
        mTiedLname = (TextInputEditText) mView.findViewById(R.id.mTiedLname);
        tvCountryCodeU = (MyTextView) mView.findViewById(R.id.tvCountryCodeU);
        tvMobNumU = (MyTextView) mView.findViewById(R.id.tvMobNumU);


        btnCardEnrollment.setOnClickListener(this);
        String mUserDetails = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        firstName = Helper.getFirstName();
        lastName = Helper.getLastName();
        fullName = firstName + " " + lastName;
        senderMobileNumber = Helper.getSenderMobileNumber();
        phoneCode = Helper.getPhoneCode();
        phWithCode = phoneCode + senderMobileNumber;
        walletAccountNumber = Helper.getWalletAccountNumber();


        mTiedFname.setText(firstName);
        mTiedLname.setText(lastName);
        tvCountryCodeU.setText(phoneCode);
        tvMobNumU.setText("" + senderMobileNumber);
        Bundle arguments = this.getArguments();
        mPrdNo = arguments.getInt(AppoConstants.PRDNUMBER, 0);
        newNumber = arguments.getString("newNumber");
        ////Log.e(TAG, "onCreateView: " + anInt);
        if (mPrdNo == 1003) {
            mTiedAccNo.setText(walletAccountNumber);
        } else {
            mTiedAccNo.setText("" + newNumber);
        }


        return mView;

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnCardEnrollment) {
            getCmvInfo();
            //makeRequest();
        }
    }

    private void getCmvInfo() {


       /* if (mCnedAccNo.getText().toString().trim().isEmpty()) {
            showToast("Enter account no.");
            return;
        }*/


        //JSONObject mRoot = new JSONObject();
        JsonObject mCvmInfoGoogle = new JsonObject();
        if (mPrdNo == 1003) {
            walletAccountNumber = Helper.getWalletAccountNumber();
        } else {
            walletAccountNumber = newNumber;
        }
        mCvmInfoGoogle.addProperty("accountNo", walletAccountNumber);
        mCvmInfoGoogle.addProperty("firstName", firstName);
        mCvmInfoGoogle.addProperty("midName", "");
        mCvmInfoGoogle.addProperty("lastName", lastName);
        mCvmInfoGoogle.addProperty("fullName", fullName);
        mCvmInfoGoogle.addProperty("mobileNo", phWithCode);
        mCvmInfoGoogle.addProperty("idCountry", phoneCode);
        // Log.e(TAG, "getCmvInfo: request for JWE " + mCvmInfoGoogle.toString());
        String s1 = new Gson().toJson(mCvmInfoGoogle);
        //String s2 = JSON.toJSONString(s1);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, s1);

        apiServiceUNIONPay.getJWEToken(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    String responseString = new Gson().toJson(response.body());
                    try {
                        JSONObject mResponse = new JSONObject(responseString);
                        if (mResponse.getInt("status") == 200) {
                            ////Log.e(TAG, "onResponse: called");
                            if (mResponse.getString("message").equalsIgnoreCase("success")) {
                                String mResult = mResponse.getString("result");
                                makeRequest2(mResult);
                            }
                        } else {
                            if (response.code() == 400) {
                                Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 503) {
                                Toast.makeText(getActivity(), "Service Unavailable server error", Toast.LENGTH_SHORT).show();
                            } else {
                                showToast(mResponse.getString("status"));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Helper.showErrorMessage(getActivity(), t.getMessage());
                ////Log.e(TAG, "onFailure: JWE " + t.getMessage());
            }
        });
    }


    private void makeRequest2(String mResult) {
        mRoot = new JsonObject();
        JsonObject mMsgInfo = new JsonObject();
        JsonObject mTrxInfo = new JsonObject();
        mMsgInfo.addProperty("versionNo", "1.0.0");
        String mTimeStamp = TimeUtils.getUniqueTimeStamp();
        mMsgInfo.addProperty("timeStamp", mTimeStamp);
        String uniqueMsgId = TimeUtils.getUniqueMsgId(mTimeStamp);
        mMsgInfo.addProperty("msgID", uniqueMsgId);
        mMsgInfo.addProperty("msgType", "CARD_ENROLLMENT");
        mMsgInfo.addProperty("insID", "39990157");
        /*mTrxInfo.addProperty("deviceID", TimeUtils.getDeviceId());
        mTrxInfo.addProperty("userID", TimeUtils.getDeviceId());*/
        mTrxInfo.addProperty("deviceID", TimeUtils.getDeviceId());
        mTrxInfo.addProperty("userID", phWithCode);
        mTrxInfo.addProperty("prdNo", String.valueOf(mPrdNo));
        mTrxInfo.addProperty("cvmInfo", mResult);
        mRoot.add("msgInfo", mMsgInfo);
        mRoot.add("trxInfo", mTrxInfo);
        // Log.e(TAG, "makeRequest: JWS === " + mRoot.toString());
        sentRequest2(mRoot);


    }

    private void sentRequest2(JsonObject mRoot1) {
        MediaType mediaType = MediaType.parse("application/json");
        String s1 = new Gson().toJson(mRoot1);
        RequestBody body = RequestBody.create(mediaType, s1);
        showLoading();
        apiServiceUNIONPay.getJWSToken(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hide();

                if (response.isSuccessful()) {
                    String responseString = new Gson().toJson(response.body());
                    try {
                        JSONObject mResponse = new JSONObject(responseString);
                        if (mResponse.getInt("status") == 200) {
                            //Log.e(TAG, "onResponse: called");
                            if (mResponse.getString("message").equalsIgnoreCase("success")) {
                                String mResult = mResponse.getString("result");
                                makeFinalRequest2(mResult, mRoot1);
                            }
                        } else {
                            if (response.code() == 400) {
                                Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 503) {
                                Toast.makeText(getActivity(), "Service Unavailable server error", Toast.LENGTH_SHORT).show();
                            } else {
                                showToast(mResponse.getString("status"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hide();
                Log.e(TAG, "onFailure: JWS " + t.getMessage());
            }
        });
    }

    private void makeFinalRequest2(String mResult, JsonObject mRoot1) {
        showLoading();
        String s1 = new Gson().toJson(mRoot1);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, s1);

        apiServiceUNIONPay.getCardEnrollment(body, mResult, walletAccountNumber).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hide();
                ////Log.e(TAG, "onResponse: Final :: " + response.body());
                if (response.isSuccessful()) {
                    ////Log.e(TAG, "onResponse: Final if :: " + response.body());
                    //JsonObject body1 = response.body();
                    String s1 = new Gson().toJson(response.body());
                    try {
                        JSONObject mRoot = new JSONObject(s1);
                        if (mRoot.getInt("status") == 200 && mRoot.getString("message").equalsIgnoreCase("OK")) {
                            showSuccessDialog("Card Enrollment", "Your UnionPay Card has been created Successfully");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (response.code() == 400) {
                        Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(getActivity(), "Service Unavailable server error", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hide();
                ////Log.e(TAG, "onFailure: final error " + t.getMessage());
            }
        });
    }

    public void showLoading() {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Please wait.....");
        mProgress.show();
    }

    public void hide() {
        mProgress.dismiss();
    }


    private void showToast(String message) {
        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
    }

    private void showSuccessDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_success_merchant, null);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        tvHeader.setText(title);
        MyTextView tvSuccess = dialogLayout.findViewById(R.id.tvSuccess);
        tvSuccess.setText(message);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectHome();
            }
        });

        builder.setView(dialogLayout);

        mDialogCard = builder.create();

        mDialogCard.setCanceledOnTouchOutside(false);

        mDialogCard.show();
    }

    private void redirectHome() {
        mDialogCard.dismiss();
        mConfirmLsitener.onConfirm();
        //getActivity().finish();

    }

    public void showCommonError(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_common_merchant_error, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        tvInfo.setText(message);
        MyTextView tvHeader = dialogLayout.findViewById(R.id.tvHeader);
        tvHeader.setText(title);
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogCard.dismiss();
            }
        });

        builder.setView(dialogLayout);

        mDialogCard = builder.create();

        mDialogCard.setCanceledOnTouchOutside(false);

        mDialogCard.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mConfirmLsitener = (OnBankSubmit) context;
    }
}