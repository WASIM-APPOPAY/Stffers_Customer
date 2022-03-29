package com.stuffer.stuffers.fragments.bottom_fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.ProfileActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.activity.wallet.VerifyOtpActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.communicator.TransactionStatus;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.utils.UnionConstant;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BottomAdditional extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetBehavior mBehaviour;
    private MyTextView tvInfoProcess;
    private static final String TAG = "BottomAdditional.Class";
    private String mAdditionalData;

    private String mTRXAmt;
    private String mMName;
    private ProgressDialog dialog;
    MainAPIInterface mainAPIInterface;
    private String phoneCode;
    private Long senderMobileNumber;
    private MyButton btnConfirmProgress, btnClearProgress, btnCloseDialog;
    private JSONObject mJSON;
    private MyButton btnKey1, btnKey2, btnKey3, btnKey4, btnKey5, btnKey6,
            btnKey7, btnKey8, btnKey9, btnKey10;
    ArrayList<MyButton> mListBtn;
    ImageView ivDots1, ivDots2, ivDots3, ivDots4, ivDots5, ivDots6;
    List<ImageView> dots;
    private String codeString = "";
    private ProgressDialog mProgress;
    private JSONObject mRequest;
    String responseAdditional = "{\n" +
            "  \"status\": 200,\n" +
            "  \"message\": \"OK\",\n" +
            "  \"result\": \"{\\\"msgInfo\\\":{\\\"versionNo\\\":\\\"1.0.0\\\",\\\"msgID\\\":\\\"A399901572021110919102896731653\\\",\\\"timeStamp\\\":\\\"20211109214006\\\",\\\"msgType\\\":\\\"ADDITIONAL_PROCESSING_RESULT\\\",\\\"insID\\\":\\\"39990157\\\"},\\\"msgResponse\\\":{\\\"responseCode\\\":\\\"00\\\",\\\"responseMsg\\\":\\\"Approved\\\"}}\"\n" +
            "}";
    private AlertDialog dialogPayment;
    TransactionStatus mTransactionStatus;

    public BottomAdditional() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = this.getArguments();
        mAdditionalData = arguments.getString(AppoConstants.DISCOUNT);
        //Log.e(TAG, "onCreate: in Bottom ::  " + mAdditionalData);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.fragment_bottom_additional, null);
        fBtmShtDialog.setContentView(mView);
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());
        mainAPIInterface = ApiUtils.getAPIService();
        mListBtn = new ArrayList<>();
        dots = new ArrayList<>();
        findIds(mView);
        getRandomKeyFromRange();
        setData();
        return fBtmShtDialog;

    }

    private void setData() {
        phoneCode = Helper.getPhoneCode();
        senderMobileNumber = Helper.getSenderMobileNumber();
        /*edtMobile.setText("" + senderMobileNumber);
        edtCustomerCountryCode.setCountryForPhoneCode(Integer.parseInt(phoneCode));*/

        try {
            mJSON = new JSONObject(mAdditionalData);
            JSONObject mTRXINfo = mJSON.getJSONObject(UnionConstant.TRXINFO);
            mTRXAmt = mTRXINfo.getString(UnionConstant.TRXAMT);
            mMName = mTRXINfo.getString(UnionConstant.MERCHANTNAME);
            String infoTransaction = "<font color='#00baf2'>" + getString(R.string.trans_note) + "</font>" +
                    " : " + "Dear User you are about to Pay " + "<b>" + "$" + mTRXAmt + "</b>" + " to " + "<font color='#FF0000'>" + mMName + "</font>" + "<br>" +
                    "Please confirm your transaction pin to complete the Transaction.";
            /*String infoTransaction = "<font color='#00baf2'>" + getString(R.string.trans_note) + "</font>" +
                    " : " + "Dear User you are about to Pay " + "<b>" + "$" + mTRXAmt + "</b>"+ " to "+ "<font color='#FF0000'>" + "<b>" + mMName + "</b> </font>" + "<br>" +
                    "Please Click on Obtain Otp to Authenticate this Transaction.";*/
            tvInfoProcess.setText(Html.fromHtml(infoTransaction));


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void findIds(View mView) {
        tvInfoProcess = (MyTextView) mView.findViewById(R.id.tvInfoProcess);
        //edtMobile = (MyEditText) mView.findViewById(R.id.edtMobile);

        btnConfirmProgress = (MyButton) mView.findViewById(R.id.btnConfirmProgress);
        btnClearProgress = (MyButton) mView.findViewById(R.id.btnClearProgress);
        ivDots1 = (ImageView) mView.findViewById(R.id.dot_1);
        ivDots2 = (ImageView) mView.findViewById(R.id.dot_2);
        ivDots3 = (ImageView) mView.findViewById(R.id.dot_3);
        ivDots4 = (ImageView) mView.findViewById(R.id.dot_4);
        ivDots5 = (ImageView) mView.findViewById(R.id.dot_5);
        ivDots6 = (ImageView) mView.findViewById(R.id.dot_6);
        btnKey1 = (MyButton) mView.findViewById(R.id.btnKey1);
        btnKey2 = (MyButton) mView.findViewById(R.id.btnKey2);
        btnKey3 = (MyButton) mView.findViewById(R.id.btnKey3);
        btnKey4 = (MyButton) mView.findViewById(R.id.btnKey4);
        btnKey5 = (MyButton) mView.findViewById(R.id.btnKey5);
        btnKey6 = (MyButton) mView.findViewById(R.id.btnKey6);
        btnKey7 = (MyButton) mView.findViewById(R.id.btnKey7);
        btnKey8 = (MyButton) mView.findViewById(R.id.btnKey8);
        btnKey9 = (MyButton) mView.findViewById(R.id.btnKey9);
        btnKey10 = (MyButton) mView.findViewById(R.id.btnKey10);

        btnCloseDialog = (MyButton) mView.findViewById(R.id.btnCloseDialog);

        btnKey1.setOnClickListener(this);
        btnKey2.setOnClickListener(this);
        btnKey3.setOnClickListener(this);
        btnKey4.setOnClickListener(this);
        btnKey5.setOnClickListener(this);
        btnKey6.setOnClickListener(this);
        btnKey7.setOnClickListener(this);
        btnKey8.setOnClickListener(this);
        btnKey9.setOnClickListener(this);
        btnKey10.setOnClickListener(this);

        btnCloseDialog.setOnClickListener(this);

        mListBtn.add(btnKey1);
        mListBtn.add(btnKey2);
        mListBtn.add(btnKey3);
        mListBtn.add(btnKey4);
        mListBtn.add(btnKey5);
        mListBtn.add(btnKey6);
        mListBtn.add(btnKey7);
        mListBtn.add(btnKey8);
        mListBtn.add(btnKey9);
        mListBtn.add(btnKey10);
        dots.add(ivDots1);
        dots.add(ivDots2);
        dots.add(ivDots3);
        dots.add(ivDots4);
        dots.add(ivDots5);
        dots.add(ivDots6);

        btnConfirmProgress.setOnClickListener(this);
        btnClearProgress.setOnClickListener(this);


    }


    private void getJWSToken(JSONObject mRoot) {
        showLoading();
        AndroidNetworking.post("https://labapi-union.appopay.com/scis/switch/getJWSToken")
                .addHeaders("requestPath", "/scis/switch/additionalprocessingresult")
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(mRoot)
                .setPriority(Priority.IMMEDIATE)
                .setTag("token")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hide();
                        try {
                            if (response.getInt("status") == 200) {
                                //Log.e(TAG, "onResponse: called");
                                if (response.getString("message").equalsIgnoreCase("success")) {
                                    String mResult = response.getString("result");
                                    requestForPayment(mResult);
                                }
                            } else {
                                showToast(response.getString("status"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hide();
                        Log.e(TAG, "onError: " + anError.getMessage());
                        Log.e(TAG, "onError: " + anError.getErrorBody());
                        Log.e(TAG, "onError: " + anError.getErrorCode());
                        Log.e(TAG, "onError: " + anError.getResponse());
                        Log.e(TAG, "onError: " + anError.getErrorDetail());
                        showToast("Error Code : " + anError.getErrorBody());
                    }
                });
    }

    private void requestForPayment(String mResult) {
        AndroidNetworking.post("https://labapi-union.appopay.com/scis/switch/additionalprocessingresult")
                .addHeaders("requestPath", "/scis/switch/additionalprocessingresult")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("authToken", mResult)
                .addJSONObjectBody(mRequest)
                .setPriority(Priority.IMMEDIATE)
                .setTag("token")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e(TAG, "onResponse: payment response " + response);

                        performNextStep(response);


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getErrorBody());
                        Log.e(TAG, "onError: " + anError.getErrorDetail());
                        showToast("Error Code : " + anError.getErrorBody());

                    }
                });

    }

    private void performNextStep(JSONObject response) {
        try {
            //JSONObject mJson = new JSONObject(responseAdditional);
            JSONObject mJson = response;
            if (mJson.getInt(AppoConstants.STATUS) == 200 && mJson.getString(AppoConstants.MESSAGE).equalsIgnoreCase(AppoConstants.OK)) {
                String mResult = mJson.getString(AppoConstants.RESULT);
                JSONObject mJSonResult = new JSONObject(mResult);
                JSONObject msgResponse = mJSonResult.getJSONObject("msgResponse");
                JSONObject msgInfo = mJSonResult.getJSONObject("msgInfo");
                if (msgResponse.getString("responseCode").equalsIgnoreCase("00")) {
                    showSuccessDialog("ADDITIONAL PROCESSING RESULT");
                } else {
                    showErrorDialog("Failed.");
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        //need to add condition
        if (mProgress != null) {
            mProgress.dismiss();
        }
    }

    public void showLoading() {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Please wait.....");
        mProgress.show();
    }

    private void showToast(String status) {
        Toast.makeText(getActivity(), "" + status, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart() {
        super.onStart();
        View touchOutsideView = Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow())
                .getDecorView()
                .findViewById(com.google.android.material.R.id.touch_outside);
        touchOutsideView.setClickable(false);
        touchOutsideView.setFocusable(false);

        mBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnKey1:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey1.getText().toString().trim();
                //tvInputPin.setText(codeString);

                setDotEnable();
                break;
            case R.id.btnKey2:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey2.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();


                break;
            case R.id.btnKey3:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey3.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;
            case R.id.btnKey4:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey4.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey5:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey5.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;
            case R.id.btnKey6:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey6.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey7:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey7.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey8:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey8.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();
                break;

            case R.id.btnKey9:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey9.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();

                break;

            case R.id.btnKey10:
                if (codeString.length() == 6)
                    return;
                codeString += btnKey10.getText().toString().trim();
                //tvInputPin.setText(codeString);
                setDotEnable();
                break;
            case R.id.btnConfirmProgress:
                String transactionPin = Helper.getTransactionPin();
                //Log.e(TAG, "onClick: " + transactionPin);


                if (codeString.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter transaction pin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (codeString.length() < 6) {
                    Toast.makeText(getActivity(), "Transaction pin should be six digit", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!codeString.equalsIgnoreCase(transactionPin)) {
                    Toast.makeText(getActivity(), "Invalid transaction pin.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject mJSON = new JSONObject(mAdditionalData);
                    mRequest = new JSONObject();

                    JSONObject mMSGInfo = mJSON.getJSONObject(UnionConstant.MSGINFO);

                    JSONObject mTRXInfo = mJSON.getJSONObject(UnionConstant.TRXINFO);

                    JSONObject mMSG = new JSONObject();
                    mMSG.put(UnionConstant.VERSIONNO, mMSGInfo.getString(UnionConstant.VERSIONNO));
                    mMSG.put(UnionConstant.INSID, mMSGInfo.getString(UnionConstant.INSID));
                    String mTimeStamp = TimeUtils.getUniqueTimeStamp();
                    String uniqueMsgId = TimeUtils.getUniqueMsgId(mTimeStamp);
                    mMSG.put(UnionConstant.MSGID, uniqueMsgId);
                    mMSG.put(UnionConstant.TIMESTAMP, mMSGInfo.getString(UnionConstant.TIMESTAMP));
                    mMSG.put(UnionConstant.MSGTYPE, "ADDITIONAL_PROCESSING_RESULT");
                    mRequest.put(UnionConstant.MSGINFO, mMSG);


                    JSONObject mTRX = new JSONObject();
                    mTRX.put(UnionConstant.DEVICEID, mTRXInfo.getString(UnionConstant.DEVICEID));
                    String phoneCode = Helper.getPhoneCode();
                    Long senderMobileNumber = Helper.getSenderMobileNumber();
                    mTRX.put(UnionConstant.USERID, phoneCode + senderMobileNumber);
                    mTRX.put(UnionConstant.TOKEN, mTRXInfo.getString(UnionConstant.TOKEN));
                    JSONArray emvCpqrcPayload = mTRXInfo.getJSONArray("emvCpqrcPayload");

                    String cpqrcPayload = emvCpqrcPayload.getString(0);
                    JSONArray mEmvQrcPayload = new JSONArray();
                    mEmvQrcPayload.put(cpqrcPayload);
                    mTRX.put("emvCpqrcPayload", mEmvQrcPayload);
                    mTRX.put(UnionConstant.ORIGMSGID, mMSGInfo.getString(UnionConstant.MSGID));
                    mTRX.put(UnionConstant.PAYMENTSTATUS, "CONTINUING");
                    mRequest.put(UnionConstant.TRXINFO, mTRX);
                    //Log.e(TAG, "onClick: " + mRequest.toString());
                    getJWSToken(mRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //performNextStep(null);


                break;
            case R.id.btnClearProgress:
                setDotDisable();
                getRandomKeyFromRange();

                break;
            case R.id.btnCloseDialog:
                dismiss();
                break;


        }
    }


    private void showErrorDialog(String params) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_coomon_error, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvTitle = dialogLayout.findViewById(R.id.tvTitle);
        tvTitle.setText("ADDITIONAL PROCESSING RESULT");
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        tvInfo.setText(params);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBalalnceError();
            }
        });

        builder.setView(dialogLayout);

        dialogPayment = builder.create();

        dialogPayment.setCanceledOnTouchOutside(false);

        dialogPayment.show();
    }

    private void updateBalalnceError() {
        dialogPayment.dismiss();

        mTransactionStatus.onTransactionComplete("SUCCESS");
    }

    private void showSuccessDialog(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_success_topup, null);
        MyTextView tvInfo = dialogLayout.findViewById(R.id.tvInfo);
        MyTextView tvTitleCommon = dialogLayout.findViewById(R.id.tvTitleCommon);
        tvTitleCommon.setText(type);
        MyTextView tvSuccess = dialogLayout.findViewById(R.id.tvSuccess);
        tvSuccess.setText("SUCCESS");
        MyButton btnClose = dialogLayout.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //invalidateDetails();
                updateBalalnce();
            }
        });

        builder.setView(dialogLayout);

        dialogPayment = builder.create();

        dialogPayment.setCanceledOnTouchOutside(false);

        dialogPayment.show();
    }

    private void updateBalalnce() {
        dialogPayment.dismiss();

        mTransactionStatus.onTransactionComplete("SUCCESS");
    }

    private void getRandomKeyFromRange() {
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        List<Integer> list;
        list = Arrays.asList(array);
        Collections.shuffle(list);

        for (int i = 0; i < list.size(); i++) {
            int answer = list.get(i);
            mListBtn.get(i).setText("" + list.get(answer));
        }
    }

    private void setDotEnable() {
        for (int i = 0; i < codeString.length(); i++) {
            dots.get(i).setImageResource(R.drawable.dot_enable);
        }
    }

    private void setDotDisable() {
        for (int i = 0; i < 6; i++) {
            dots.get(i).setImageResource(R.drawable.dot_disable);
        }
        codeString = "";
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mTransactionStatus = (TransactionStatus) context;
    }
}
