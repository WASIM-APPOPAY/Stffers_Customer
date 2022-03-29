package com.stuffer.stuffers.fragments.union_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.AccountActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainUAPIInterface;
import com.stuffer.stuffers.fragments.bottom_fragment.BottotmPinFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UnMaskFragment extends Fragment {
    private MainUAPIInterface apiServiceUNIONPay;
    private ImageView ivUninonPay;
    private MyTextView tvCardNumberU, tvExpU, tvNameU;
    private FrameLayout fLayout;
    private static final String TAG = "UnMaskFragment";
    private MyTextViewBold tvCardHeaderU;
    private String maskedPan, panExpiry, fullName;
    private BottotmPinFragment mBottomPinFragment;
    private LinearLayout linearParent;
    private String mCardInfo;
    private MyTextView tvCvvU, tvCardTypeU;
    private long mLastClickTime = 0;
    private ProgressDialog mProgress;

    public UnMaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_un_mask, container, false);
        ivUninonPay = (ImageView) mView.findViewById(R.id.ivUninonPay);
        tvCardNumberU = (MyTextView) mView.findViewById(R.id.tvCardNumberU);
        tvCardHeaderU = (MyTextViewBold) mView.findViewById(R.id.tvCardHeaderU);
        tvCvvU = (MyTextView) mView.findViewById(R.id.tvCvvU);
        tvCardTypeU = (MyTextView) mView.findViewById(R.id.tvCardTypeU);

        tvExpU = (MyTextView) mView.findViewById(R.id.tvExpU);
        tvNameU = (MyTextView) mView.findViewById(R.id.tvNameU);
        fLayout = (FrameLayout) mView.findViewById(R.id.fLayout);
        linearParent = (LinearLayout) mView.findViewById(R.id.linearParent);
        apiServiceUNIONPay = ApiUtils.getApiServiceUNIONPay();
        getSavedCard();

        ivUninonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showBottomPin();
            }
        });


        return mView;
    }

    public void showLoading() {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("Please wait.....");
        mProgress.show();
    }

    public void hideLoading() {
        mProgress.dismiss();
    }

    private void showBottomPin() {
        mBottomPinFragment = new BottotmPinFragment();
        mBottomPinFragment.show(getChildFragmentManager(), mBottomPinFragment.getTag());
        mBottomPinFragment.setCancelable(false);
    }


    private void getSavedCard() {
        showLoading();
        String walletAccountNumber = Helper.getWalletAccountNumber();
        apiServiceUNIONPay.getSavedCardUnion(walletAccountNumber).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    String s1 = new Gson().toJson(response.body());
                    //Log.e(TAG, "onResponse: " + s1);
                    try {
                        JSONObject mRoot = new JSONObject(s1);
                        if (mRoot.getInt("status") == 200 && mRoot.getString("message").equalsIgnoreCase("success")) {
                            String result = mRoot.getString("result");
                            JSONObject mParent = new JSONObject(result);
                            JSONObject trxInfo = mParent.getJSONObject("trxInfo");
                            mCardInfo = trxInfo.getString(Constants.CARDINFO);
                            maskedPan = trxInfo.getString("maskedPan");
                            panExpiry = trxInfo.getString("panExpiry");
                            String firstName = Helper.getFirstName();
                            String lastName = Helper.getLastName();
                            fullName = firstName + " " + lastName;
                            tvCardNumberU.setText(maskedPan);
                            tvExpU.setText("Exp:" + panExpiry);
                            tvNameU.setText(fullName);
                            fLayout.setVisibility(View.VISIBLE);
                            tvCardHeaderU.setVisibility(View.VISIBLE);
                            //Log.e(TAG, "onResponse: saved caed " + result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(getActivity(), "Service Unavailable server error", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(getActivity(), "Not Found", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Log.e(TAG, "onFailure: " + t.getMessage());
                hideLoading();
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void closeBottomDialog(String transactionPin) {
        if (mBottomPinFragment != null)
            mBottomPinFragment.dismiss();
        //Toast.makeText(getActivity(), "" + transactionPin, Toast.LENGTH_SHORT).show();
        String mTransactionPin = Helper.getTransactionPin();
        if (mTransactionPin.equalsIgnoreCase(transactionPin)) {
            makeUnmaskedRequest();
        } else {
            showToast("Invalid Transaction Pin");
        }


    }

    private void showToast(String invalid_transaction_pin) {
        Toast.makeText(getActivity(), "" + invalid_transaction_pin, Toast.LENGTH_SHORT).show();
    }

    private void makeUnmaskedRequest() {
        showLoading();
        //Log.e(TAG, "makeUnmaskedRequest: " + mCardInfo);
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), mCardInfo);
        apiServiceUNIONPay.getUnmaskRequestBody(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //Log.e(TAG, "onResponse: " + response);
                //Log.e(TAG, "onResponse: " + response.body());
                hideLoading();
                if (response.isSuccessful()) {

                    JsonObject body1 = response.body();
                    String result = body1.get(AppoConstants.RESULT).getAsString();
                    JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
                    String mUnmaskPan = jsonObject.get(AppoConstants.PAN).getAsString();
                    String mUnmaskCvv = jsonObject.get(AppoConstants.CVV2).getAsString();
                    String mUnmaskType = jsonObject.get(AppoConstants.CARDTYPEU).getAsString();
                    tvCardTypeU.setText("Card Type : " + mUnmaskType);
                    tvCardTypeU.setVisibility(View.VISIBLE);
                    tvCardNumberU.setText(mUnmaskPan);
                    tvCvvU.setText("CVV : " + mUnmaskCvv);
                    tvCvvU.setVisibility(View.VISIBLE);
                    showToast("Card UnMask Successfully");


                    //Log.e(TAG, "onResponse: result " + result);


                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoading();
                showToast(t.getMessage());


            }

        });

    }
}