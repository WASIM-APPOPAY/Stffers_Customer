package com.stuffer.stuffers.fragments.giftcard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.MobileRechargeActivity;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.MoneyTransferListener;
import com.stuffer.stuffers.fragments.bottom.chatmodel.ChatUser;
import com.stuffer.stuffers.fragments.bottom.chatnotification.Data;
import com.stuffer.stuffers.fragments.bottom.chatnotification.MyResponse;
import com.stuffer.stuffers.fragments.bottom.chatnotification.NotificationData;
import com.stuffer.stuffers.fragments.bottom.chatnotification.Sender;
import com.stuffer.stuffers.fragments.bottom.chatnotification.Token;
import com.stuffer.stuffers.fragments.dialog.FromAccountDialogFragment;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;


public class SentGiftCardFragment extends Fragment {
    private String receiveruser;
    private List<CurrencyResult> resultCurrency;
    private static final String TAG = "SentGiftCardFragment";
    private MyTextView tvRequiredFilled;
    private LinearLayout layoutFromAccount;
    private MyTextView tvFromAccount;
    private MyTextView tvToAccount;
    private MainAPIInterface mainAPIInterface;
    private ProgressDialog dialog;
    private ArrayList<AccountModel> mListAccount;
    private ArrayList<String> mListTemp;
    private FromAccountDialogFragment fromAccountDialogFragment;
    private String fromCurrency;
    private float conversionRates;
    private MyEditText edAmount;
    private MyTextView tvAmountCredit, tvConversionRates;
    private MyButton btnTransfer;
    private int mFromPosition;
    private String reciveraccountnumber;
    private Dialog dialogTransfer;
    private String userTransactionPin;
    private JSONObject jsonCommission;
    private float bankfees, processingfees, finaamount, amountaftertax_fees, taxes;
    private String fomrcurrencycode, recaccountnumber, recmobilenumber, recareacode, recname, recuserid, fromcurrency;
    MoneyTransferListener mMoneyTransfer;


    private View mView;
    private String receiverEmail;
    private ArrayList<ChatUser> mList;

    DatabaseReference dbChatUsers;
    private MainAPIInterface mainAPIInterface2;
    private String countyrId;
    private MyEditText edMessage;
    private String mGiftCardNumber;
    private String mGiftCardNumberFinal;

    public SentGiftCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle arguments = this.getArguments();

        receiveruser = arguments.getString(AppoConstants.SENTUSER);
        resultCurrency = arguments.getParcelableArrayList(AppoConstants.SENTCURRENCY);
        mView = inflater.inflate(R.layout.fragment_sent_gift_card, container, false);
        edMessage = (MyEditText) mView.findViewById(R.id.edMessage);
        mainAPIInterface = ApiUtils.getAPIService();

        layoutFromAccount = (LinearLayout) mView.findViewById(R.id.layoutFromAccount);
        tvFromAccount = (MyTextView) mView.findViewById(R.id.tvFromAccount);
        tvToAccount = (MyTextView) mView.findViewById(R.id.tvToAccount);
        edAmount = (MyEditText) mView.findViewById(R.id.edAmount);
        tvAmountCredit = (MyTextView) mView.findViewById(R.id.tvAmountCredit);
        tvConversionRates = (MyTextView) mView.findViewById(R.id.tvConversionRates);
        btnTransfer = (MyButton) mView.findViewById(R.id.btnTransfer);


        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Helper.hideKeyboard(edAmount, getContext());
                    if (edAmount.getText().toString().trim().isEmpty()) {
                        edAmount.setError("please enter amount");
                        edAmount.requestFocus();
                        return;
                    }

                    try {
                        float twoDecimal = (float) Helper.getTwoDecimal(Float.parseFloat(edAmount.getText().toString().trim()) * conversionRates);
                        sentGiftCard();
                        // sendNotification();
                        //getReceiverToken();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), getString(R.string.info_invalid_format), Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Log.e(TAG, "onClick: exception called ");
                    e.printStackTrace();
                }
            }
        });

        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (conversionRates == 0) {
                    Toast.makeText(getContext(), getString(R.string.info_please_select_from_account), Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                      float twoDecimal = (float) Helper.getTwoDecimal(Float.parseFloat(edAmount.getText().toString().trim()) * conversionRates);
                    tvAmountCredit.setText(edAmount.getText().toString().trim());
                } catch (Exception e) {
                    if (edAmount.getText().toString().trim().isEmpty()) {

                    } else {
                        Toast.makeText(getContext(), getString(R.string.info_invalid_format), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setReceiverDetails();
        getCurrentUserDetails();

        return mView;
    }

    private void sentGiftCard() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        String message = null;
        if (edMessage.getText().toString().trim().isEmpty()) {
            message = "hope you enjoy appopay gift cards";
        } else {
            message = edMessage.getText().toString().trim();
        }
        String amount = edAmount.getText().toString().trim();
        Log.e(TAG, "sentGiftCard: " + amount);
        mainAPIInterface.sentAppopayGiftCards(recmobilenumber, recareacode, amount, countyrId, /*message*/"welcome", accesstoken).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //Log.e(TAG, "onResponse: card :: " + response.body());
                    String resParam = new Gson().toJson(response.body());
                    Log.e(TAG, "onResponse: " + resParam);
                    try {
                        //{"status":200,"message":"success","result":"6367820304817609814"}
                        //No value for message
                        JSONObject res = new JSONObject(resParam);
                        Log.e(TAG, "onResponse: ==== " + res);
                        if (res.getString(AppoConstants.MESSAGE).equalsIgnoreCase(AppoConstants.SUCCESS)) {
                            if (res.has(AppoConstants.RESULT)) {
                                mGiftCardNumber = res.getString(AppoConstants.RESULT);
                                mGiftCardNumberFinal = getGiftCardNumberInFormat();
                                Log.e(TAG, "onResponse: " + mGiftCardNumberFinal);
                                getReceiverToken();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }

    private String getGiftCardNumberInFormat() {

        String accountEncrypt = mGiftCardNumber;
        char[] charsEncrypt = accountEncrypt.toCharArray();
        int count = -1;
        int count1 = 0;
        String temp = "";
        String finalString = "";
        for (int i = 0; i < charsEncrypt.length; i++) {
            count = count + 1;

            temp = temp + String.valueOf(charsEncrypt[i]);
            if (count == 4) {
                finalString = finalString + temp + "    ";
                temp = "";
                count = -1;
            }
            count1 = count1 + 1;
            if (count1 >= charsEncrypt.length) {
                finalString = finalString + temp;
            }
            //tvAccountNos.setText(finalString);
            //Log.e(TAG, "readUserAccounts: ==============" + finalString);
        }

        return finalString;
    }


    private void setReceiverDetails() {
        try {
            JSONObject obj = new JSONObject(receiveruser);
            reciveraccountnumber = obj.getString(AppoConstants.RECIEVERACCOUNTNUMBER);
            tvToAccount.setText(obj.getString(AppoConstants.RECIEVERACCOUNTNUMBER) + "-" + obj.getString(AppoConstants.FROMCURRENCYCODE));
            recaccountnumber = obj.getString(AppoConstants.RECIEVERACCOUNTNUMBER);
            fromcurrency = obj.getString(AppoConstants.FROMCURRENCY);
            fomrcurrencycode = obj.getString(AppoConstants.FROMCURRENCYCODE);
            recmobilenumber = obj.getString(AppoConstants.RECEIVERMOBILENUMBER);
            recareacode = obj.getString(AppoConstants.RECEIVERAREACODE);
            recname = obj.getString(AppoConstants.RECIEVERNAME);
            recuserid = obj.getString(AppoConstants.RECIEVERUSERID);
            receiverEmail = obj.getString(AppoConstants.EMIAL);
            countyrId = obj.getString(AppoConstants.COUNTRYID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCurrentUserDetails() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.info_getting_profile));
        dialog.show();

        try {
            String userDetails = DataVaultManager.getInstance(getContext()).getVaultValue(KEY_USER_DETIALS);
            JSONObject mIndex = new JSONObject(userDetails);
            String accessToken = DataVaultManager.getInstance(getContext()).getVaultValue(KEY_ACCESSTOKEN);
            JSONObject mResult = mIndex.getJSONObject(AppoConstants.RESULT);
            String ph = mResult.getString(AppoConstants.MOBILENUMBER);
            String area = mResult.getString(AppoConstants.PHONECODE);
            String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
            mainAPIInterface.getProfileDetails(Long.parseLong(ph), Integer.parseInt(area), bearer_).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        //String res = new Gson().toJson(response.body());
                        //Log.e(TAG, "onResponse: getprofile :" + res);
                        JsonObject body = response.body();
                        String res=body.toString();

                        DataVaultManager.getInstance(getContext()).saveUserDetails(res);
                        readUserAccounts();
                    } else {
                        if (response.code() == 401) {
                            DataVaultManager.getInstance(getContext()).saveUserDetails("");
                            DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                            Intent intent = new Intent(getContext(), SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dialog.dismiss();
                    //Log.e(TAG, "onFailure: " + t.getMessage().toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void readUserAccounts() {
        mListAccount = new ArrayList<>();
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        try {
            JSONObject root = new JSONObject(vaultValue);
            JSONObject objResult = root.getJSONObject(AppoConstants.RESULT);
            JSONObject objCustomerDetails = objResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            JSONArray arrCustomerAccount = objCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            for (int i = 0; i < arrCustomerAccount.length(); i++) {
                JSONObject index = arrCustomerAccount.getJSONObject(i);
                AccountModel model = new AccountModel();
                model.setAccountnumber(index.getString(AppoConstants.ACCOUNTNUMBER));
                if (index.has(AppoConstants.ACCOUNTSTATUS)) {
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                    model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                    model.setCurrentbalance(index.getString(AppoConstants.CURRENTBALANCE));
                    mListAccount.add(model);
                }

            }
            mListTemp = new ArrayList<String>();

            if (mListAccount.size() > 0) {
                //for adapter
                for (int i = 0; i < mListAccount.size(); i++) {
                    mListTemp.add(mListAccount.get(i).getAccountnumber() + "-" + mListAccount.get(i).getCurrencyCode());
                }
                tvFromAccount.setText(mListTemp.get(0));
                mFromPosition = 0;

                if (fomrcurrencycode.equalsIgnoreCase(mListAccount.get(0).getCurrencyCode())) {
                    conversionRates = 1;
                    tvConversionRates.setText(String.valueOf(conversionRates));
                } else {
                    getConversionBaseRate(mFromPosition);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getCurrency(String param) {
        String res = null;
        for (int i = 0; i < resultCurrency.size(); i++) {
            String sid = resultCurrency.get(i).getId().toString();
            if (sid.equals(param)) {
                res = resultCurrency.get(i).getCurrencyCode();
                break;
            }
        }
        return res;
    }

    private void getConversionBaseRate(final int fromAccountPosition) {

        String url = "https://api.exchangeratesapi.io/latest?base=" + mListAccount.get(fromAccountPosition).getCurrencyCode();
        //Log.e(TAG, "getConversionBaseRate: url :: " + url);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait");
        dialog.show();
        AndroidNetworking.get(url)
                .setOkHttpClient(AppoPayApplication.getOkHttpClient2(10))
                .setPriority(Priority.IMMEDIATE)
                .setTag("base conversion")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        //Log.e(TAG, "onResponse: base :: " + response);
                        if (response.has("base")) {
                            //Log.e(TAG, "onResponse: true");
                            sentParam(response, fromAccountPosition);

                        } else {
                            //Log.e(TAG, "onResponse: false");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                    }
                });
        ////Log.e(TAG, "getConversionBaseRate: " + sentBaseConversion);

        /*JSONObject jsonRates = response.getJSONObject(AppoConstants.RATES);
        conversionRates = Float.parseFloat(jsonRates.getString(fromCurrency));
        tvConversionRates.setText(String.valueOf(conversionRates));*/
    }

    private void sentParam(JSONObject response, int fromAccountPosition) {
        mFromPosition = fromAccountPosition;
        try {
            JSONObject jsonRates = response.getJSONObject(AppoConstants.RATES);
            conversionRates = Float.parseFloat(jsonRates.getString(fomrcurrencycode.toUpperCase()));
            tvConversionRates.setText(String.valueOf(conversionRates));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getReceiverToken() {
        Query mDatabaseQuery = FirebaseDatabase.getInstance().getReference(AppoConstants.FIREBASE_USERS_NODE)
                .orderByChild(AppoConstants.EMIAL_ID)
                .equalTo(receiverEmail);
        mList = new ArrayList<ChatUser>();
        mDatabaseQuery.addListenerForSingleValueEvent(valueEventListener);
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            mList.clear();
            if (snapshot.exists()) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatUser chatUser = dataSnapshot.getValue(ChatUser.class);
                    mList.add(chatUser);
                }
                if (mList.size() > 0) {
                    sendNotification();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void sendNotification() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        dialog.show();
        mainAPIInterface2 = ApiUtils.getApiServiceForNotification("https://fcm.googleapis.com/");
        Query mDatabaseQuery = FirebaseDatabase.getInstance().getReference(AppoConstants.FIREBASE_USER_TOKENS)
                .orderByKey()
                .equalTo(mList.get(0).getId());
        mDatabaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismiss();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Token token = dataSnapshot.getValue(Token.class);
                        String userName = Helper.getSenderName() + " has sent a GIFT CARD of " + "<font color='#00baf2'>" + mListAccount.get(0).getCurrencyCode() + "</font> " + tvAmountCredit.getText().toString().trim() + " to you.";
                        //String userName = Helper.getSenderName() + " has sent a GIFT CARD of " + "<font color='#00baf2'>" + mListAccount.get(0).getCurrencyCode() + "</font> " + tvAmountCredit.getText().toString().trim() + " to you.";
                        float value = Float.parseFloat(edAmount.getText().toString().trim());
                        float twoDecimal = Helper.getTwoDecimal(value);
                        JsonObject jsonParam = new JsonObject();
                        jsonParam.addProperty(AppoConstants.AMOUNT, String.valueOf(twoDecimal));
                        jsonParam.addProperty(AppoConstants.MESSAGE, edMessage.getText().toString().trim());
                        jsonParam.addProperty(AppoConstants.FROMCURRENCY, mListAccount.get(0).getCurrencyCode());
                        jsonParam.addProperty(AppoConstants.SENDERNAME, recname);
                        jsonParam.addProperty(AppoConstants.GIFTCARD_WALLET_NUMBER, mGiftCardNumberFinal);
                        String sentParam = jsonParam.toString();

                        Data data = new Data("Gift Card", 1, "Appopay Gift Card", sentParam,
                                mList.get(0).getId());
                        Sender sender = new Sender(data, token.getToken());

                        mainAPIInterface2.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                                Log.e(TAG, "onResponse: " + response.toString());
                                Log.e(TAG, "onResponse: " + response.message());
                                Log.e(TAG, "onResponse: " + response.errorBody());
                                if (response.code() == 200) {
                                    if (response.body().success == 1) {
                                        // Toast.makeText(MessageActivity.this, "Failes", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(), "Gift Card Sent", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Log.e(TAG, "onFailure: " + t.getMessage().toString());
                                dialog.dismiss();
                            }
                        });

                    }
                } else {
                    Toast.makeText(getContext(), "User token not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: called");
            }
        });
    }
    //mainAPIInterface = ApiUtils.getApiServiceForNotification("https://fcm.googleapis.com/");
}