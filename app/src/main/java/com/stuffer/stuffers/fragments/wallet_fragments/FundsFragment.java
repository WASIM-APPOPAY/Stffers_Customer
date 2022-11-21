package com.stuffer.stuffers.fragments.wallet_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FundsFragment extends Fragment {

    View mView;
    private RecyclerView rvCash, rvCard, rvWallet;
    private String mCountry;
    private ProgressDialog mProgress;
    private ArrayList<FundModel> mListCash;
    private ArrayList<FundModel> mListCard;
    private ArrayList<FundModel> mListEWallet;

    public FundsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_funds, container, false);
        rvCash = mView.findViewById(R.id.rvCash);
        rvCard = mView.findViewById(R.id.rvCard);
        rvWallet = mView.findViewById(R.id.rvWallet);
        rvCash.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvCard.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvWallet.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        Bundle arguments = this.getArguments();
        mCountry = arguments.getString(AppoConstants.COUNTRY);
        getCash();

        return mView;

    }

    private void getCash() {
        String url = Constants.APPOPAY_BASE_URL + "api/s3/bucket/appopay-mobile-logos?prefix=" + mCountry + "/cash";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getCard();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    private void getCard() {
        String url = Constants.APPOPAY_BASE_URL + "api/s3/bucket/appopay-mobile-logos?prefix=" + mCountry + "/card";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getWallet();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    private void getWallet() {
        String url = Constants.APPOPAY_BASE_URL + "api/s3/bucket/appopay-mobile-logos?prefix=" + mCountry + "/e-wallet";
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

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