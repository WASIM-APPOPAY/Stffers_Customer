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
    private MyTextView tvSendingCurrency, tvPaymentMode, cTvExchange, cTvPayOut, cTvCommission, cTvVat, cTvTotalPayable, cTvNow;
    private ArrayList<String> mModeList;
    private ModeDialog mModeDialog;
    private MyEditText edSendAmount;
    private ProgressDialog mLoader;
    List<CalTransfer.Result> mListCal;
    private String mSender, mReceiverCurrency, mRecName, mRecBankName, mRecBankAccount, mRecBranch, mRecBankCode, mNationalityCode;

    public SendMoneyToBank() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSender = getArguments().getString(AppoConstants.SENDERCURRENCY);
            mReceiverCurrency = getArguments().getString(AppoConstants.RECEIVERCURRENCY);
            mRecName = getArguments().getString(AppoConstants.RECEIVERNAME);
            mRecBankName = getArguments().getString(AppoConstants.RECEIVERBANKNAME);
            mRecBankAccount = getArguments().getString(AppoConstants.RECEIVERBANKACCOUNT);
            mRecBranch = getArguments().getString(AppoConstants.RECEIVERBRANCH);
            mRecBankCode = getArguments().getString(AppoConstants.RECEIVERBANKCODE);
            mNationalityCode = getArguments().getString(AppoConstants.SENDERNATIONALITY);
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

        String desc = "Sending Amount From " + "<br>" + "<font color='#FF0000'>" + mSender + "</font>" + " ==>> " + "<font color='#2607B1'>" + mReceiverCurrency + "</font>";
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
                //mSendingObject.put("payInCurrency", "USD");// i have usd
                //mSendingObject.put("transferCurrency", "USD"); // i have usd
                mSendingObject.put("payInCurrency", Helper.getCurrencySymble());// i have usd
                mSendingObject.put("transferCurrency", Helper.getCurrencySymble()); // i have usd
                mSendingObject.put("payoutCurrency", mReceiverCurrency); //on selected rec curr in prev screen
                mSendingObject.put("transferAmount", edSendAmount.getText().toString().trim());
                mSendingObject.put("paymentMode", "BANK");
                calculateTransfer(mSendingObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (view.getId() == R.id.cTvNow) {
            makeRequestBody();
        }
    }

    private void makeRequestBody() {

        JSONObject mSendBody = new JSONObject();
        try {
            mSendBody.put("clientTxnNO", "");
            mSendBody.put("payoutCurrency", mReceiverCurrency);
            mSendBody.put("transferAmount", cTvPayOut.getText().toString().trim());
            mSendBody.put("payoutPartnerUniqueCode", "0");
            mSendBody.put("payoutCountry", "IND");
            mSendBody.put("remitterFirstName", Helper.getFirstName());
            mSendBody.put("remitterLastName", Helper.getLastName());
            mSendBody.put("remitterTelNo", Helper.getNumberWithCountryCode());
            mSendBody.put("remitterMobileNo", Helper.getNumberWithCountryCode());
            mSendBody.put("remitterEmail", Helper.getEmail());
            mSendBody.put("remitterAddress1", Helper.getAddress());
            mSendBody.put("remitterAddress2", "");
            mSendBody.put("remitterAddress3", "");
            mSendBody.put("remitterIDType", "");
            mSendBody.put("remitterIDNumber", "");
            mSendBody.put("remitterIDDesc", "");
            mSendBody.put("remitterIDTypeIssueDate", "");
            mSendBody.put("remitterIDTypeExpiryDate", "");
            mSendBody.put("remitterDOB", "");
            mSendBody.put("remitterGender", "");
            mSendBody.put("remitterNationality", "IND");
            mSendBody.put("customerRelation", "16");
            mSendBody.put("messageToBeneficiary", "");
            String mFirstName = Helper.beneficiaryFirstName(mRecName);
            mSendBody.put("beneficiaryFirstName", mFirstName);
            String mLastName=Helper.beneficiaryLastName(mRecName);
            mSendBody.put("beneficiaryLastName", mLastName);
            mSendBody.put("beneficiaryTelNo", "");//O
            mSendBody.put("beneficiaryMobileNo", "");//M
            mSendBody.put("beneficiaryEmail", "");
            mSendBody.put("beneficiaryAddress1", "");//M
            mSendBody.put("beneficiaryAddress2", "");
            mSendBody.put("beneficiaryAddress3", "");
            mSendBody.put("beneficiaryIDType", "0");
            mSendBody.put("beneficiaryIDDesc", "");
            mSendBody.put("beneficiaryIDNumber", "");
            mSendBody.put("beneficiaryIDTypeIssueDate", "");
            mSendBody.put("beneficiaryIDTypeExpiryDate", "");
            mSendBody.put("beneficiaryDOB", "");
            mSendBody.put("beneficiaryGender", "");
            mSendBody.put("beneficiaryNationality", mNationalityCode);
            mSendBody.put("benficiaryBankCode", mRecBankCode);
            mSendBody.put("paymentMode", "BANK");
            mSendBody.put("benificiaryBankAcNo", mRecBankAccount);
            mSendBody.put("benificiaryBankAcName", mRecBankName);
            mSendBody.put("benificiaryBankAddress1", "");
            mSendBody.put("benificiaryBankAddress2", "");
            mSendBody.put("purposeCode", "20");//purpose code
            mSendBody.put("payinCountry", "GBR");
            mSendBody.put("payinCurrency", Helper.getCurrencySymble());
            mSendBody.put("settlementCurrency", mReceiverCurrency);
            mSendBody.put("remitterPayInAmt", cTvTotalPayable.getText().toString().trim());
            mSendBody.put("msgPayOutBranch", "");
            mSendBody.put("contactBenificiary", "false");
            mSendBody.put("rptno", "0");
            mSendBody.put("sourceofIncome", "");
            mSendBody.put("userID", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                        if (!StringUtils.isEmpty(errorBody)) {
                            try {
                                JSONObject mJson = new JSONObject(errorBody);
                                if (mJson.has("message")) {
                                    Helper.showLongMessage(getActivity(), mJson.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });


    }
}