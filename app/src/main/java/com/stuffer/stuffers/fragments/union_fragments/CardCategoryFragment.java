package com.stuffer.stuffers.fragments.union_fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.UnionPayListener;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;

import org.json.JSONException;
import org.json.JSONObject;


public class CardCategoryFragment extends Fragment implements View.OnClickListener {


    private MyButton btnExpressCard, btnCardGift, btnCardWallet, btnCardLoan, btnCardCredit;
    private UnionPayListener mUnionPayListener;
    private int mType;
    private ProgressDialog mDialog;
    private static final String TAG = "CardCategoryFragment";

    public CardCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_card_category, container, false);
        btnExpressCard = (MyButton) mView.findViewById(R.id.btnExpressCard);
        btnCardGift = (MyButton) mView.findViewById(R.id.btnCardGift);
        btnCardWallet = (MyButton) mView.findViewById(R.id.btnCardWallet);
        btnCardLoan = (MyButton) mView.findViewById(R.id.btnCardLoan);
        btnCardCredit = (MyButton) mView.findViewById(R.id.btnCardCredit);

        btnExpressCard.setOnClickListener(this);
        btnCardGift.setOnClickListener(this);
        btnCardWallet.setOnClickListener(this);
        btnCardLoan.setOnClickListener(this);
        btnCardCredit.setOnClickListener(this);


        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mUnionPayListener = (UnionPayListener) context;

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnExpressCard:
                mType = 1000;
                requestNewCard("MONEY");
                break;
            case R.id.btnCardGift:
                mType = 1001;
                requestNewCard("GIFT");
                break;
            case R.id.btnCardWallet:
                mType = 1003;
                //requestNewCard("WALLET");
                mUnionPayListener.onDifferentCardRequest(1003, "WALLET");
                break;
            case R.id.btnCardLoan:
                mType = 1002;
                requestNewCard("LOAN");
                break;
            case R.id.btnCardCredit:
                mType = 1004;
                requestNewCard("CREDIT");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

    }

    public void showDialog() {
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage(getString(R.string.info_please_wait_dots));
        mDialog.show();
    }

    public void hideDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void requestNewCard(String type) {
        showDialog();
        String userId = Helper.getCustomerId();

        String currencyId = Helper.getCurrencyId();
        JSONObject mParam = new JSONObject();
        try {
            mParam.put("customer_id", userId);
            mParam.put("currencyid", currencyId);
            mParam.put("account_type", type);

            AndroidNetworking.post("https://labapi.appopay.com/api/users/createnewaccountnumber")
                    .setPriority(Priority.IMMEDIATE)
                    .setTag("newaccountnumbner")
                    .addJSONObjectBody(mParam)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Log.e(TAG, "onResponse: " + response);
                            hideDialog();
                            try {
                                if (response.getInt("status") == 200 && response.getString("message").equalsIgnoreCase("success")) {
                                    String result = response.getString("result");
                                    if (mType == 1000) {
                                        mUnionPayListener.onDifferentCardRequest(1000, result);
                                    } else if (mType == 1001) {
                                        mUnionPayListener.onDifferentCardRequest(1001, result);
                                    } else if (mType == 1002) {
                                        mUnionPayListener.onDifferentCardRequest(1002, result);
                                    } else if (mType == 1003) {
                                        mUnionPayListener.onDifferentCardRequest(1003, result);
                                    } else if (mType == 1004) {
                                        mUnionPayListener.onDifferentCardRequest(1004, result);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e(TAG, "onError: " + anError.getErrorDetail());
                            hideDialog();
                            Helper.showErrorMessage(getActivity(), anError.getErrorDetail());
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}