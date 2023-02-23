package com.stuffer.stuffers.fragments.wallet_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.recyclerview.FundAdapter;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.models.output.FundModel;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FundsFragment extends Fragment {
    private static final String TAG = "FundsFragment";
    View mView;
    private RecyclerView rvCash, rvCard, rvWallet, rvOnLine;
    private String mCountry;
    private ProgressDialog mProgress;
    private ArrayList<FundModel> mListCash;
    private ArrayList<FundModel> mListCard;
    private ArrayList<FundModel> mListEWallet;
    private ArrayList<FundModel> mListOnline;
    private MyTextViewBold tvCash, tvCard, tvEWallet, tvOnline;
    View vCash, vCard, vWallet;

    public FundsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(TAG, "onCreateView: called");
        mView = inflater.inflate(R.layout.fragment_funds, container, false);

        vCash = mView.findViewById(R.id.vCash);
        vCard = mView.findViewById(R.id.vCard);
        vWallet = mView.findViewById(R.id.vWallet);

        tvCash = mView.findViewById(R.id.tvCash);
        tvCard = mView.findViewById(R.id.tvCard);
        tvEWallet = mView.findViewById(R.id.tvEWallet);
        tvOnline = mView.findViewById(R.id.tvOnLine);

        rvCash = mView.findViewById(R.id.rvCash);
        rvCard = mView.findViewById(R.id.rvCard);
        rvWallet = mView.findViewById(R.id.rvWallet);
        rvOnLine = mView.findViewById(R.id.rvOnLine);

        rvCash.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rvCard.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rvWallet.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rvOnLine.setLayoutManager(new GridLayoutManager(getActivity(),3));

        /*rvCash.setNestedScrollingEnabled(false);
        rvCard.setNestedScrollingEnabled(false);
        rvWallet.setNestedScrollingEnabled(false);
        rvOnLine.setNestedScrollingEnabled(false);*/


        Bundle arguments = this.getArguments();
        mCountry = arguments.getString(AppoConstants.COUNTRY);
        getCash();

        return mView;

    }

    private void getCash() {
        String url = Constants.APPOPAY_BASE_URL + "api/s3/bucket/appopay-mobile-logos?prefix=" + mCountry + "/cash";
        //Log.e(TAG, "getCash: " + url);
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: cash " + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray(AppoConstants.RESULT);
                            mListCash = new ArrayList<FundModel>();
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString(AppoConstants.NAME);
                                    String url = jsonObject.getString(AppoConstants.URL);
                                    FundModel fundModel = new FundModel(name, url);
                                    mListCash.add(fundModel);
                                }
                                if (mListCash.size() > 0) {
                                    rvCash.setAdapter(new FundAdapter(mListCash, getActivity()));
                                }
                            } else {
                                tvCash.setVisibility(View.GONE);
                                rvCash.setVisibility(View.GONE);
                                vCash.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getCard();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getErrorDetail());

                    }
                });

    }

    private void getCard() {

        String url = Constants.APPOPAY_BASE_URL + "api/s3/bucket/appopay-mobile-logos?prefix=" + mCountry + "/card";
        Log.e(TAG, "getCard: " + url);
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: card " + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray(AppoConstants.RESULT);
                            mListCard = new ArrayList<FundModel>();
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString(AppoConstants.NAME);
                                    String url = jsonObject.getString(AppoConstants.URL);
                                    FundModel fundModel = new FundModel(name, url);

                                    mListCard.add(fundModel);


                                }
                                if (mListCard.size() > 0) {
                                    rvCard.setAdapter(new FundAdapter(mListCard, getActivity()));
                                }
                            } else {
                                tvCard.setVisibility(View.GONE);
                                rvCard.setVisibility(View.GONE);
                                vCard.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getWallet();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getErrorDetail());

                    }
                });

    }

    private void getWallet() {
        String url = Constants.APPOPAY_BASE_URL + "api/s3/bucket/appopay-mobile-logos?prefix=" + mCountry + "/e-wallet";
        Log.e(TAG, "getWallet: " + url);
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: wallet " + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray(AppoConstants.RESULT);
                            mListEWallet = new ArrayList<FundModel>();
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString(AppoConstants.NAME);
                                    String url = jsonObject.getString(AppoConstants.URL);
                                    FundModel fundModel = new FundModel(name, url);
                                    mListEWallet.add(fundModel);


                                }
                                if (mListEWallet.size() > 0) {
                                    rvWallet.setAdapter(new FundAdapter(mListEWallet, getActivity()));
                                }

                            } else {
                                tvEWallet.setVisibility(View.GONE);
                                rvWallet.setVisibility(View.GONE);
                                vWallet.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getOnLine();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getErrorDetail());

                    }
                });
    }

    private void getOnLine() {
        String url = Constants.APPOPAY_BASE_URL + "api/s3/bucket/appopay-mobile-logos?prefix=" + mCountry + "/Online";
        Log.e(TAG, "getOnLine: " + url);
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: Online ::" + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray(AppoConstants.RESULT);
                            mListOnline = new ArrayList<FundModel>();
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString(AppoConstants.NAME);
                                    String url = jsonObject.getString(AppoConstants.URL);
                                    FundModel fundModel = new FundModel(name, url);
                                    mListOnline.add(fundModel);


                                }
                                if (mListOnline.size() > 0) {
                                    rvOnLine.setAdapter(new FundAdapter(mListOnline, getActivity()));
                                }

                            } else {
                                tvOnline.setVisibility(View.GONE);
                                rvOnLine.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getErrorDetail());

                    }
                });
    }


    private void showLoading(String message) {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage(message);
        mProgress.show();

    }

    private void hideLoading() {
        mProgress.dismiss();
    }
    //https://prodapi.appopay.com/api/s3/bucket/appopay-mobile-logos?prefix=peru/card

}