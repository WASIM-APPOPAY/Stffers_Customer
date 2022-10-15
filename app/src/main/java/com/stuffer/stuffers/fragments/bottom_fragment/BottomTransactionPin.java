package com.stuffer.stuffers.fragments.bottom_fragment;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.OnTransactionPinSuccess;
import com.stuffer.stuffers.communicator.TransactionPinListener;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BottomTransactionPin extends BottomSheetDialogFragment implements View.OnClickListener {

    private MyButton btnKey1, btnKey2, btnKey3, btnKey4, btnKey5, btnKey6,
            btnKey7, btnKey8, btnKey9, btnKey10, btnClear, btnConfirm;
    ArrayList<MyButton> mListBtn;


    private String codeString = "";
    List<ImageView> dots;
    private BottomSheetBehavior mBehaviour;
    private MyButton btnCloseDialog;
    private TransactionPinListener mPinListenr;
    private MyEditText edCnfmTransPin, edTransPin;
    private ProgressDialog dialog;
    private MainAPIInterface<Retrofit> mainAPIInterface;
    private OnTransactionPinSuccess mTransactionSuccess;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog fBtmShtDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View mView = View.inflate(getActivity(), R.layout.fragment_trans_pin, null);
        fBtmShtDialog.setContentView(mView);
        mainAPIInterface = ApiUtils.getAPIService();
        mBehaviour = BottomSheetBehavior.from((View) mView.getParent());
        mListBtn = new ArrayList<>();
        dots = new ArrayList<>();
        findIds(mView);
        getRandomKeyFromRange();
        return fBtmShtDialog;
    }

    private void findIds(View mView) {
        edCnfmTransPin = (MyEditText) mView.findViewById(R.id.edCnfmTransPin);
        edTransPin = (MyEditText) mView.findViewById(R.id.edTransPin);


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
        btnClear = (MyButton) mView.findViewById(R.id.btnClear);
        btnConfirm = (MyButton) mView.findViewById(R.id.btnConfirm);
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
        btnConfirm.setOnClickListener(this);
        btnClear.setOnClickListener(this);
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

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
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

            case R.id.btnConfirm:
                //getRandomKeyFromRange();
                if (edTransPin.getText().toString().trim().isEmpty()) {
                    edTransPin.setError("Please enter transaction pin");
                    edTransPin.requestFocus();

                    return;
                }
                if (edTransPin.getText().toString().length() < 6) {
                    edTransPin.setError("Transaction pin should be six digit");
                    edTransPin.requestFocus();

                    return;
                }
                if (edCnfmTransPin.getText().toString().trim().isEmpty()) {
                    edCnfmTransPin.setError("Please Confirm transaction pin");
                    edCnfmTransPin.requestFocus();
                    return;
                }

                if (edCnfmTransPin.getText().toString().trim().length() < 6) {
                    //Toast.makeText(getActivity(), "Transaction pin should be six digit", Toast.LENGTH_SHORT).show();
                    edCnfmTransPin.setError("Confirm pin should be six digit");
                    edCnfmTransPin.requestFocus();
                    return;
                }

                if (edTransPin.getText().toString().trim().equalsIgnoreCase(edCnfmTransPin.getText().toString().trim())) {
                    mPinListenr.onPinConfirm(edTransPin.getText().toString().trim());
                } else {
                    Toast.makeText(getActivity(), "pin mismatch", Toast.LENGTH_SHORT).show();
                }


                //Log.e(TAG, "onClick: " + codeString);
                break;
            case R.id.btnClear:
                setDotDisable();
                getRandomKeyFromRange();

                break;
            case R.id.btnCloseDialog:
                dismiss();
                break;


        }
    }

    private void setDotEnable() {
        for (int i = 0; i < codeString.length(); i++) {
            dots.get(i).setImageResource(R.drawable.dot_enable);
        }
    }

    private void setDotDisable() {
        edCnfmTransPin.setText("");
        edTransPin.setText("");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPinListenr = (TransactionPinListener) context;
        mTransactionSuccess = (OnTransactionPinSuccess) context;
    }

    public void updatePin(String pin) {
        updateUserProfile(pin);
    }

    //for activity result
    public void updateUserProfile(String mTransPin) {
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        //Log.e("TAG", "updateUserProfile: " + vaultValue);
        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject jsonResult = index.getJSONObject(AppoConstants.RESULT);

            JsonObject sentIndex = new JsonObject();
            sentIndex.addProperty(AppoConstants.ID, jsonResult.getString(AppoConstants.ID));
            sentIndex.addProperty(AppoConstants.FIRSTNAME, jsonResult.getString(AppoConstants.FIRSTNAME));
            sentIndex.addProperty(AppoConstants.LASTNAME, jsonResult.getString(AppoConstants.LASTNAME));
            sentIndex.addProperty(AppoConstants.USERNAME, jsonResult.getString(AppoConstants.USERNAME));
            sentIndex.addProperty(AppoConstants.PASSWORD, jsonResult.getString(AppoConstants.PASSWORD));
            sentIndex.addProperty(AppoConstants.EMIAL, jsonResult.getString(AppoConstants.EMIAL));
            sentIndex.addProperty(AppoConstants.ACCOUNTEXPIRED, jsonResult.getString(AppoConstants.ACCOUNTEXPIRED));
            sentIndex.addProperty(AppoConstants.ACCOUNTLOCKED, jsonResult.getString(AppoConstants.ACCOUNTLOCKED));
            sentIndex.addProperty(AppoConstants.CREDENTIALSEXPIRED, jsonResult.getString(AppoConstants.CREDENTIALSEXPIRED));
            sentIndex.addProperty(AppoConstants.ENABLE, jsonResult.getString(AppoConstants.ENABLE));
            sentIndex.addProperty(AppoConstants.MOBILENUMBER, jsonResult.getString(AppoConstants.MOBILENUMBER));
            sentIndex.addProperty(AppoConstants.TRANSACTIONPIN, mTransPin);
            sentIndex.addProperty(AppoConstants.USERTYPE, "CUSTOMER");
            sentIndex.addProperty(AppoConstants.PHONECODE, jsonResult.getString(AppoConstants.PHONECODE));
            sentIndex.addProperty(AppoConstants.STORENAME, (String) null);
            sentIndex.addProperty(AppoConstants.LATITUDE, 0);
            sentIndex.addProperty(AppoConstants.LONGITUDE, 0);
            sentIndex.addProperty(AppoConstants.SECURITYANSWER, "dollar_sent");
            sentIndex.addProperty(AppoConstants.SCREENLOCKPIN, (String) null);

            JsonArray jsonArrayRole = new JsonArray();
            jsonArrayRole.add("USER");
            sentIndex.add(AppoConstants.ROLE, jsonArrayRole);

            JSONObject jsonCustomerDetails = jsonResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);

            JsonObject sentJsonCustomerDetails = new JsonObject();
            sentJsonCustomerDetails.addProperty(AppoConstants.ID, jsonCustomerDetails.getString(AppoConstants.ID));
            sentJsonCustomerDetails.addProperty(AppoConstants.FIRSTNAME, jsonCustomerDetails.getString(AppoConstants.FIRSTNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.LASTNAME, jsonCustomerDetails.getString(AppoConstants.LASTNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.MIDDLENAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.CARDTOKEN, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.COUNTRYID, jsonCustomerDetails.getString(AppoConstants.COUNTRYID));
            sentJsonCustomerDetails.addProperty(AppoConstants.STATEID, jsonCustomerDetails.getString(AppoConstants.STATEID));
            sentJsonCustomerDetails.addProperty(AppoConstants.ADDRESS, jsonCustomerDetails.getString(AppoConstants.ADDRESS));
            sentJsonCustomerDetails.addProperty(AppoConstants.CITYNAME, jsonCustomerDetails.getString(AppoConstants.CITYNAME));
            sentJsonCustomerDetails.addProperty(AppoConstants.ZIPCODE2, jsonCustomerDetails.getString(AppoConstants.ZIPCODE2));

            sentJsonCustomerDetails.addProperty(AppoConstants.DOB, jsonCustomerDetails.getString(AppoConstants.DOB));
            sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYID, jsonCustomerDetails.getString(AppoConstants.CURRENCYID));
            sentJsonCustomerDetails.addProperty(AppoConstants.MONTHLYINCOME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.PASSPORTNUMBER, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.EXPIRYDATE, jsonCustomerDetails.getString(AppoConstants.EXPIRYDATE));
            sentJsonCustomerDetails.addProperty(AppoConstants.IDTYPE, jsonCustomerDetails.getString(AppoConstants.IDTYPE));
            sentJsonCustomerDetails.addProperty(AppoConstants.IDNUMBER, jsonCustomerDetails.getString(AppoConstants.IDNUMBER));

            sentJsonCustomerDetails.addProperty(AppoConstants.BANKACCOUNT, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IMAGEURL, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.BANKUSERNAME, (String) null);

            sentJsonCustomerDetails.addProperty(AppoConstants.BANKUSERNAME, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.MERCHANTQRCODE, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISDEAL, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, jsonCustomerDetails.getString(AppoConstants.CURRENCYSYMBOL));
            /*if (Helper.getCurrencyId().equalsIgnoreCase("1")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "USD");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("2")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "INR");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("3")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "CAD");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("4")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "ERU");
            } else if (Helper.getCurrencyId().equalsIgnoreCase("5")) {
                sentJsonCustomerDetails.addProperty(AppoConstants.CURRENCYSYMBOL, "DOP");
            }*/
            sentJsonCustomerDetails.addProperty(AppoConstants.IDCUENTA, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.IDASOCIADO, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.ISPLASTICO, (String) null);
            sentJsonCustomerDetails.addProperty(AppoConstants.SOURCEOFINCOME, (String) null);


            JsonArray sentJsonArrayCustomerAccounts = new JsonArray();
            JSONArray jsonArrayCustomerAccount = jsonCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);


            for (int i = 0; i < jsonArrayCustomerAccount.length(); i++) {
                JSONObject jsonObjectIndex = jsonArrayCustomerAccount.getJSONObject(i);
                JsonObject jsonObjectAccount = new JsonParser().parse(jsonObjectIndex.toString()).getAsJsonObject();
                sentJsonArrayCustomerAccounts.add(jsonObjectAccount);
            }
            sentJsonCustomerDetails.add(AppoConstants.CUSTOMERACCOUNTS, sentJsonArrayCustomerAccounts);
            sentIndex.add(AppoConstants.CUSTOMERDETAILS, sentJsonCustomerDetails);
            //Log.e("TAG", "updateUserProfile: " + sentIndex);
            processUpdateTransactionPin(sentIndex);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void processUpdateTransactionPin(JsonObject sentIndex) {
        String accessToken = DataVaultManager.getInstance(getActivity()).getVaultValue(KEY_ACCESSTOKEN);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.info_updaing_profile));
        dialog.show();
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);

        mainAPIInterface.postUpdateUserProfile(sentIndex, bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Your have created your transaction pin Successfully", Toast.LENGTH_SHORT).show();
                    mTransactionSuccess.onPinCreated();
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getActivity()).saveUserDetails("");
                        DataVaultManager.getInstance(getActivity()).saveUserAccessToken("");
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 400) {
                        Toast.makeText(getActivity(), "Bad Request", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        Toast.makeText(getActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 503) {
                        Toast.makeText(getActivity(), "Service Unavailable", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Log.e(TAG, "onFailure: profile update" + t.getMessage());
                dialog.dismiss();
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
