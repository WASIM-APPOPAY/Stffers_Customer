package com.stuffer.stuffers.fragments.request_money;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.stuffer.stuffers.utils.Helper;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitRequestFragment extends Fragment {
    private static final String TAG = "SubmitRequestFragment";
    private String receiveruser;
    private List<CurrencyResult> resultCurrency;
    private MainAPIInterface mainAPIInterface;
    private MyTextView tvRequiredFilled;
    private LinearLayout layoutFromAccount;
    private MyTextView tvFromAccount;
    private MyTextView tvToAccount;
    private MyEditText edAmount;
    private String fomrcurrencycode, recaccountnumber, recmobilenumber, recareacode, recname, recuserid, fromcurrency;
    private String reciveraccountnumber;
    private MyButton btnTransfer;
    private ProgressDialog dialog;
    private ArrayList<AccountModel> mListAccount;
    private ArrayList<String> mListTemp;

    public SubmitRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle arguments = this.getArguments();
        receiveruser = arguments.getString(AppoConstants.SENTUSER);
        resultCurrency = arguments.getParcelableArrayList(AppoConstants.SENTCURRENCY);
        View mView = inflater.inflate(R.layout.fragment_submit_request, container, false);
        mainAPIInterface = ApiUtils.getAPIService();
        layoutFromAccount = (LinearLayout) mView.findViewById(R.id.layoutFromAccount);
        tvFromAccount = (MyTextView) mView.findViewById(R.id.tvFromAccount);
        tvToAccount = (MyTextView) mView.findViewById(R.id.tvToAccount);
        edAmount = (MyEditText) mView.findViewById(R.id.edAmount);

        btnTransfer = (MyButton) mView.findViewById(R.id.btnTransfer);

        tvRequiredFilled = (MyTextView) mView.findViewById(R.id.tvRequiredFilled);
        String required = getString(R.string.required_filled) + "<font color='#00baf2'>" + "*" + "</font>";
        tvRequiredFilled.setText(Html.fromHtml(required));
        setReceiverDetails();
        /*tvRequiredFilled = (MyTextView) mView.findViewById(R.id.tvRequiredFilled);
        String required = getString(R.string.required_filled) + "<font color='#00baf2'>" + "*" + "</font>";
        tvRequiredFilled.setText(Html.fromHtml(required));*/
        return mView;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getCurrentUserDetails();
    }

    private void getCurrentUserDetails() {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("please wait, getting profile");
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
                        ////Log.e(TAG, "onResponse: getprofile :" + res);
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

        //Log.e(TAG, "readUserAccounts: " + vaultValue);

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
                    //Log.e(TAG, "readUserAccounts: AccountStatus : " + index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                    model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                    model.setCurrentbalance(index.getString(AppoConstants.CURRENTBALANCE));
                    mListAccount.add(model);
                } /*else {
                    //Log.e(TAG, "readUserAccounts: AccountStatus : " + "null");
                    model.setAccountstatus("");
                }*/

            }
            mListTemp = new ArrayList<String>();

            if (mListAccount.size() > 0) {
                //resultScan = "MI1000000000031|CERCA24|63516303|507| support@stuffrs.com|undefined|USD";
                //for adapter
                for (int i = 0; i < mListAccount.size(); i++) {
                    mListTemp.add(mListAccount.get(i).getAccountnumber() + "-" + mListAccount.get(i).getCurrencyCode());
                }

                tvFromAccount.setText(mListTemp.get(0));

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


}
