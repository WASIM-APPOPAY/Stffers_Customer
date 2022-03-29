package com.stuffer.stuffers.fragments.union_fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.TitleListener;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.TimeUtils;
import com.stuffer.stuffers.views.MyButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;


public class UnionOpenAccountFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "UnionOpenAccountFragmen";
    MyButton btnKyc, btnSubmit;
    private TitleListener mUpdateListener;
    MainAPIInterface apiService;
    private String mResult;

    public UnionOpenAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_union_open_account, container, false);
        AndroidNetworking.initialize(getActivity());
        apiService = ApiUtils.getAPIService();
        btnKyc = (MyButton) mView.findViewById(R.id.btnKyc);
        btnSubmit = (MyButton) mView.findViewById(R.id.btnSubmit);
        btnKyc.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        // http://labapi.appopay.com/api/configurations/generateJWS


        return mView;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnKyc) {
            mUpdateListener.onTitleUpdate("Union Pay Open Account");
            btnSubmit.setVisibility(View.VISIBLE);
            btnKyc.setVisibility(View.GONE);
            //getJWSToken();
            callOpenAccount(null);
        }
    }

     /*{
            "appId": "39990157",
                "reqPath": "/scis/switch/openaccount",
                "payload": "Hello"
        }*/


    private void getJWSToken() {
        JsonObject param = new JsonObject();
        param.addProperty("appId", "39990157");
        param.addProperty("reqPath", "/scis/switch/openaccount");
        param.addProperty("payload", "Hello");

        apiService.getJWSToken(param).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    String s = new Gson().toJson(response.body());
                    try {
                        JSONObject param = new JSONObject(s);
                        mResult = param.getString("result");
                        callOpenAccount(mResult);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    private void callOpenAccount(String jwsToken) {
        Log.e(TAG, "callOpenAccount: pass jwsToken");

        /*JsonObject param = new JsonObject();
        param.addProperty("appId", "39990157");
        param.addProperty("reqPath", "/scis/switch/openaccount");
        param.addProperty("payload", paramString);*/
        /*apiService.getJWSToken(param,jwsToken).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });*/


        try {
            JSONObject mRoot = new JSONObject();

            JSONObject mMessageInfo = new JSONObject();//msgInfo
            mMessageInfo.put("versionNo", "1.0.0");
            mMessageInfo.put("msgID", "A3999999920190808105201114935");
            //mMessageInfo.put("timeStamp", "20190808105201");

            mMessageInfo.put("timeStamp", TimeUtils.getUniqueTimeStamp());
            mMessageInfo.put("msgType", "OPEN_ACCOUNT");
            mMessageInfo.put("insID", "39990157");

            JSONObject mTransInfo = new JSONObject();//trxInfo
            mTransInfo.put("deviceID", "1b5ddc2562a8de5b4e175d418f5b7edf");
            mTransInfo.put("userID", "50763516303");
            mTransInfo.put("cvmInfo", "eyJhbGciOiJSU0ExXzUiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2Iiwia2lkIjoiMTU2MjAzMjg4NTk2MiJ9.Jr6L-PDZ_2Z9QqDWFAnIvya9iI7e0mz15IPpM1uZPya1Hlk0UuJxSBbrpFUJbx1eg_rgueef76HtEJz-v-LggyjZYxPsmvQQBHHSghyVqgPhMDSmjR9n1HVyWkaNG6sReJL-XFRFjlCGmVfczejNlE1qL9tuoIAQk4DbroFb1s35dQoBOGNO2brxjlYvoSsjurwhYaIlRH_iRQgcCHQe1MHBK0iYYLGitu_I9JxSuHzaxzBj7YMM1Jbi5Pxp99xzyjRp9t-sJAVtvSqDmSOWYJBW7c0B1LETWLyE__pST9KtUN6MLnzBZe3jr07-XfBEh__qdVDBnhdenW2KQXCU9g.ZjU1N2MwODQyMTk5NDg3OQ.oRD8qS3Lz5pGJTu4I84wyF7eo8VhsinNn5Xs3hX0Q-tpcLuEl8-hALHPh7DL_m7o-heCHpyQn4fOCPJ5EqWFfF_Kl8JL2wLgKMzXf29X0yg.qlz04_I7MDYsPyg2dFmwqQ");
            mTransInfo.put("cvmAddtlDocums","Test");
            /*JSONArray mAddDocums = new JSONArray();//cvmAddtlDocums
            JSONObject mDocs1 = new JSONObject();
            mDocs1.put("dataType", "ID card");
            mDocs1.put("dataFormat", "PNG");
            mDocs1.put("dataContent", "ABCDEFG");

            JSONObject mDocs2 = new JSONObject();
            mDocs2.put("dataType", "selfPhoto");
            mDocs2.put("dataFormat", "PNG");
            mDocs2.put("dataContent", "ABCDEFG");
            mAddDocums.put(0, mDocs1);
            mAddDocums.put(1, mDocs2);

            mTransInfo.put("cvmAddtlDocums", mAddDocums);*/

            mRoot.put("msgInfo", mMessageInfo);
            mRoot.put("trxInfo", mTransInfo);

            Log.e(TAG, "callOpenAccount: "+mRoot );



            //AndroidNetworking.post("https://labapi.appopay.com/api/unionpay/openaccount")
            String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
            //String mtype = mListProduct.get(mCarrierPosition).getAmounts().get(mRechargePosition).getDestCurr();
            String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
            //AndroidNetworking.post("https://labapi.appopay.com/api/wallet/openaccount")
            AndroidNetworking.post("https://labapi.appopay.com/api/unionpay/openaccount")
            //AndroidNetworking.post("http://labapi-wallet.appopay.com/api/unionpay/openaccount")
                    //.addHeaders("Authorization", bearer_)
                    .addJSONObjectBody(mRoot)
                    .setPriority(Priority.IMMEDIATE)
                    .build().getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "onResponse: open account response :: " + response);

                }

                @Override
                public void onError(ANError anError) {
                    Log.e(TAG, "onError: " + anError.getResponse());
                    Log.e(TAG, "onError: " + anError.getMessage());
                    Log.e(TAG, "onError: " + anError.getErrorDetail());
                    Log.e(TAG, "onError: " + anError.getErrorBody());
                    Log.e(TAG, "onError: " + anError.getErrorCode());

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mUpdateListener = (TitleListener) context;

    }
}