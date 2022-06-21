package com.stuffer.stuffers.activity.quick_pass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.FianceTab.UnionPayActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainUAPIInterface;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomNotCard;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.views.MyButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_TOKEN;

public class QrGenerateFragment extends Fragment implements View.OnClickListener {
    private ImageView ivBottom, ivChild;
    private MyButton btnCreateQr;
    private BottotmPinFragment mBottomPinFragment;
    private JSONObject mRootObject;
    private JSONObject mMsgInfo;
    private JSONObject mTrxInfo;
    private static final String TAG = "QrGenerateFragment";
    private ProgressDialog mProgress;
    private MainUAPIInterface apiServiceUNIONPay;
    private BottomNotCard mBottomNotCard;
    private String mCardToken;

    public QrGenerateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_qr_generate, container, false);
        apiServiceUNIONPay = ApiUtils.getApiServiceUNIONPay();
        ivBottom = (ImageView) mView.findViewById(R.id.ivBottom);
        ivChild = (ImageView) mView.findViewById(R.id.ivChild);
        btnCreateQr = (MyButton) mView.findViewById(R.id.btnCreateQr);
        btnCreateQr.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnCreateQr) {


            mCardToken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_TOKEN);
            if (!StringUtils.isEmpty(mCardToken)) {
                showBottomPin();
            } else {
                getSavedCard();
            }

        }
    }

    private void showBottomPin() {
        mBottomPinFragment = new BottotmPinFragment();
        mBottomPinFragment.show(getChildFragmentManager(), mBottomPinFragment.getTag());
        mBottomPinFragment.setCancelable(false);
    }

    private void getSavedCard() {
        String walletAccountNumber = Helper.getWalletAccountNumber();
        apiServiceUNIONPay.getSavedCardUnion(walletAccountNumber).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    String s1 = new Gson().toJson(response.body());
                    try {
                        //Log.e(TAG, "onResponse: " + s1);
                        JSONObject mRoot = new JSONObject(s1);
                        if (mRoot.getInt("status") == 200 && mRoot.getString("message").equalsIgnoreCase("success")) {
                            if (mRoot.has("result")) {
                                String result = mRoot.getString("result");
                                //Log.e(TAG, "onResponse: saved caed " + result);
                                if (result != null && !result.isEmpty()) {
                                    JSONObject mJSON = new JSONObject(result);
                                    JSONObject trxInfo = mJSON.getJSONObject("trxInfo");
                                    String token = trxInfo.getString("token");
                                    DataVaultManager.getInstance(AppoPayApplication.getInstance()).saveCardToken(token);
                                    //getConsumerQrCode(token);
                                    showBottomPin();
                                }
                            } else {
                                //Log.e(TAG, "onResponse: no result");
                                showNoCardDialog();
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(getActivity(), getString(R.string.info_bad_request), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(getActivity(), getString(R.string.info_503), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(getActivity(), getString(R.string.info_not_found), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    private void showNoCardDialog() {
        mBottomNotCard = new BottomNotCard();
        mBottomNotCard.show(getChildFragmentManager(), mBottomNotCard.getTag());
        mBottomNotCard.setCancelable(false);
    }

    public void closeBottomDialog(String transactionPin) {
        if (mBottomPinFragment != null)
            mBottomPinFragment.dismiss();
        //Toast.makeText(getActivity(), "" + transactionPin, Toast.LENGTH_SHORT).show();
        String mTransactionPin = Helper.getTransactionPin();
        if (mTransactionPin.equalsIgnoreCase(transactionPin)) {
            getConsumerQrCode(mCardToken);
        } else {
            showToast(getString(R.string.info_invalid_transaction_pin));
        }


    }

    private void getConsumerQrCode(String token) {

        mRootObject = new JSONObject();
        mMsgInfo = new JSONObject();
        mTrxInfo = new JSONObject();

        try {
            mMsgInfo.put("versionNo", "1.0.0");
            String mTimeStamp = TimeUtils.getUniqueTimeStamp();
            mMsgInfo.put("timeStamp", mTimeStamp);
            String uniqueMsgId = TimeUtils.getUniqueMsgId(mTimeStamp);
            mMsgInfo.put("msgID", uniqueMsgId);
            mMsgInfo.put("msgType", "QRC_GENERATION");
            mMsgInfo.put("insID", "39990157");

            mTrxInfo.put("deviceID", TimeUtils.getDeviceId());
            Long senderMobileNumber = Helper.getSenderMobileNumber();
            String phoneCode = Helper.getPhoneCode();
            String phWithCode = phoneCode + senderMobileNumber;
            mTrxInfo.put("userID", phWithCode);
            //mTrxInfo.put("token", "6263600715063224");
            mTrxInfo.put("token", token);
            mTrxInfo.put("trxLimit", "100");
            mTrxInfo.put("cvmLimit", "1");
            mTrxInfo.put("limitCurrency", "156");
            mTrxInfo.put("cpqrcNo", "01");

            mRootObject.put("msgInfo", mMsgInfo);
            mRootObject.put("trxInfo", mTrxInfo);
            //Log.e(TAG, "getConsumerQrCode: " + mRootObject);
            showLoading();

            AndroidNetworking.post("https://labapi-union.appopay.com/scis/switch/getJWSToken")
                    .addHeaders("requestPath", "/scis/switch/qrcgeneration")
                    .addHeaders("Content-Type", "application/json")
                    .addJSONObjectBody(mRootObject)
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
                                        getUnionQrCode(mResult);
                                    }
                                } else {
                                    showToast(response.getString("status"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //getUnionQrCode();
                        }

                        @Override
                        public void onError(ANError anError) {
                            hide();
                            ///*Log.e(TAG, "onError: " + anError.getMessage());
                            //Log.e(TAG, "onError: " + anError.getErrorBody());
                            //Log.e(TAG, "onError: " + anError.getErrorCode());
                            //Log.e(TAG, "onError: " + anError.getResponse());
                            //Log.e(TAG, "onError: " + anError.getErrorDetail());*/
                            showToast("Error Code : " + anError.getErrorBody());

                        }
                    });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void getUnionQrCode(String authToken) {
        JSONObject mRootUnion = new JSONObject();
        JSONObject mQrConfig = new JSONObject();
        JSONObject mUninoRequestTemp = new JSONObject();

        try {
            mQrConfig.put("width", 300);
            mQrConfig.put("height", 300);
            mQrConfig.put("ratio", 3);
            mQrConfig.put("logo", false);
            mUninoRequestTemp.put("msgInfo", mMsgInfo);
            mUninoRequestTemp.put("trxInfo", mTrxInfo);
            //mUnionPayRequest.put("unionPayRequest",mUninoRequestTemp);
            mRootUnion.put("qrcConfig", mQrConfig);
            mRootUnion.put("unionPayRequest", mUninoRequestTemp);
            //Log.e(TAG, "getUnionQrCode: " + mRootUnion);
            showLoading();

            AndroidNetworking.post("https://labapi-union.appopay.com/scis/switch/getQRCode")
                    .addHeaders("requestPath", "/scis/switch/qrcgeneration")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("authToken", authToken)
                    .addJSONObjectBody(mRootUnion)
                    .setPriority(Priority.IMMEDIATE)
                    .setTag("token")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hide();
                            //Log.e(TAG, "onResponse: " + response);
                            try {
                                if (response.getInt("status") == 200) {
                                    String mResult = response.getString("result");
                                    // String barCode = response.getString("evmqrcode");

                                    String mQrCode = mResult.substring(mResult.indexOf(",") + 1);
                                    //mQrCode=mResult;
                                    /*final byte[] decodedBytes = Base64.decode(mQrCode, Base64.DEFAULT);
                                    Glide.with(ProfileActivity.this).load(decodedBytes).into(user_qr_image1);*/
                                    /*if (mBarCode != null && mQrCode != null) {
                                        setupViewPager(viewPager);
                                        tabLayout.setupWithViewPager(viewPager);
                                    }*/
                                    /*try {
                                        String barCode = response.getString("barcode");
                                        mBarCode = barCode.substring(barCode.indexOf(",") + 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }*/

                                    final byte[] decodedBytes = Base64.decode(mQrCode, Base64.DEFAULT);
                                    Glide.with(getActivity()).load(decodedBytes).into(ivBottom);
                                    ivChild.setVisibility(View.VISIBLE);


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
                            //Log.e(TAG, "onError: " + anError.getErrorDetail());
                            //Log.e(TAG, "onError: " + anError.getErrorBody());
                            //Log.e(TAG, "onError: " + anError.getMessage());
                            showToast("Error Code : " + anError.getErrorBody());


                        }
                    });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showToast(String status) {
        Toast.makeText(getActivity(), "" + status, Toast.LENGTH_SHORT).show();
    }

    public void showLoading() {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Please wait.....");
        mProgress.show();
    }

    public void hide() {
        mProgress.dismiss();
    }

    public void redirectCardEnrollment() {
        if (mBottomNotCard != null)
            mBottomNotCard.dismiss();
        Intent intentUnion = new Intent(getActivity(), UnionPayActivity.class);
        startActivity(intentUnion);
    }
}