package com.stuffer.stuffers.activity.cashSends;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.fragments.dialog.BankDialog;
import com.stuffer.stuffers.fragments.dialog.ModeDialog;
import com.stuffer.stuffers.models.output.CalTransfer;
import com.stuffer.stuffers.models.output.DetinationCurrency;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SendMoneyToBank extends Fragment implements View.OnClickListener {

    private static final String TAG = "SendMoneyToBank";
    private View mView;
    private MyTextViewBold tvTitleBottom, tvTitleTop, cTvCalculate, tvDescription;
    private MyTextView tvSendingCurrency, tvPaymentMode, cTvExchange, cTvPayOut, cTvCommission, cTvVat, cTvTotalPayable,cTvNow;
    private ArrayList<String> mModeList;
    private ModeDialog mModeDialog;
    private MyEditText edSendAmount;
    private ProgressDialog mLoader;
    List<CalTransfer.Result> mListCal;
    private String mSender,mReceiver;

    public SendMoneyToBank() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSender = getArguments().getString(AppoConstants.SENDERCURRENCY);
            mReceiver = getArguments().getString(AppoConstants.RECEIVERCURRENCY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_send_money_to_bank, container, false);
        tvDescription = mView.findViewById(R.id.tvDescription);
        cTvExchange = mView.findViewById(R.id.cTvExchange);
        cTvPayOut = mView.findViewById(R.id.cTvPayOut);
        cTvCommission = mView.findViewById(R.id.cTvCommission);
        cTvVat = mView.findViewById(R.id.cTvVat);
        cTvTotalPayable = mView.findViewById(R.id.cTvTotalPayable);
        edSendAmount = mView.findViewById(R.id.edSendAmount);
        tvTitleBottom = mView.findViewById(R.id.tvTitleBottom);
        cTvCalculate = mView.findViewById(R.id.cTvCalculate);
        cTvNow = mView.findViewById(R.id.cTvNow);
        cTvCalculate.setOnClickListener(this);

        tvTitleBottom.setText(Html.fromHtml("<u>" + getString(R.string.info_payment_details) + "</u>"));

        String desc = "Sending Amount From " + "<br>" + "<font color='#FF0000'>" + mSender + "</font>" + " ==>> " + "<font color='#2607B1'>" + mReceiver + "</font>";
        tvDescription.setText(Html.fromHtml(desc));
        edSendAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(edSendAmount.getText().toString().trim().length() > 0)) {
                    cTvCalculate.setEnabled(false);
                } else {
                    cTvCalculate.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return mView;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cTvCalculate) {
            try {
                JSONObject mSendingObject = new JSONObject();
                mSendingObject.put("payInCurrency", "USD");// i have usd
                mSendingObject.put("transferCurrency", "USD"); // i have usd
                mSendingObject.put("payoutCurrency", mReceiver); //on selected rec curr in prev screen
                mSendingObject.put("transferAmount", edSendAmount.getText().toString().trim());
                mSendingObject.put("paymentMode", "BANK");
                calculateTransfer(mSendingObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (view.getId()==R.id.cTvNow){
            makeRequestBody();
        }
    }

    private void makeRequestBody() {



    }

    public void showLoading(String message) {
        if (mLoader == null) {
            mLoader = new ProgressDialog(getActivity());
        }
        mLoader.setMessage(message);
        mLoader.show();
    }

    public void hideLoading() {
        if (mLoader != null && mLoader.isShowing()) {
            mLoader.dismiss();
        }
    }

    private void calculateTransfer(JSONObject mSendingObject) {
        showLoading(getString(R.string.info_please_wait_dots));
        AndroidNetworking.post("http://3.140.192.123:8080/api/transfer/calTransfer")
                .addJSONObjectBody(mSendingObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            JSONArray result = response.getJSONArray("result");
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<CalTransfer.Result>>() {
                            }.getType();
                            mListCal = gson.fromJson(String.valueOf(result), type);
                            if (mListCal.size() > 0) {
                                cTvExchange.setText(mListCal.get(0).getExchangeRate());
                                cTvPayOut.setText(mListCal.get(0).getPayoutAmount());
                                cTvCommission.setText(mListCal.get(0).getCommission());
                                cTvVat.setText(mListCal.get(0).getVatValue());
                                cTvTotalPayable.setText(mListCal.get(0).getTotalPayable());


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();

                        //Log.e(TAG, "onError: " + anError.getErrorDetail());
                        //Log.e(TAG, "onError: " + anError.getMessage());
                        //Log.e(TAG, "onError: "+ );
                        String errorBody = anError.getErrorBody();
                        if (!StringUtils.isEmpty(errorBody)){
                            try {
                                JSONObject mJson=new JSONObject(errorBody);
                                if (mJson.has("message")){
                                    Helper.showLongMessage(getActivity(),mJson.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });


    }
}