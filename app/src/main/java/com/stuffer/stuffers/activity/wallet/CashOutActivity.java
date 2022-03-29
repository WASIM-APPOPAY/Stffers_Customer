package com.stuffer.stuffers.activity.wallet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.QRCodeUtil;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
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

public class CashOutActivity extends AppCompatActivity implements View.OnClickListener {

    private MyTextView tvFromAccount, tvTotalAmount;
    private MyEditText edAmount;
    private MyButton btnCreate;
    private ProgressDialog dialog;
    private String mUserDetails;
    private JSONObject mIndex;
    private MainAPIInterface mainAPIInterface;

    List<CurrencyResult> result;
    ArrayList<AccountModel> mListAccount;
    private String finalString;
    private float amountF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_out);
        mainAPIInterface = ApiUtils.getAPIService();
        tvFromAccount = (MyTextView) findViewById(R.id.tvFromAccount);
        edAmount = (MyEditText) findViewById(R.id.edAmount);
        tvTotalAmount = (MyTextView) findViewById(R.id.tvTotalAmount);
        btnCreate = (MyButton) findViewById(R.id.btnCreate);
        mUserDetails = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);

        try {
            mIndex = new JSONObject(mUserDetails);
            JSONObject root = new JSONObject(mUserDetails);
            JSONObject objResult = root.getJSONObject(AppoConstants.RESULT);
            JSONObject objCustomerDetails = objResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            JSONArray arrCustomerAccount = objCustomerDetails.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            JSONObject index = arrCustomerAccount.getJSONObject(0);
            String accountMNumber = index.getString(AppoConstants.ACCOUNTNUMBER);
            tvFromAccount.setText(accountMNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setupActionBar();
        btnCreate.setOnClickListener(this);

        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String amount = edAmount.getText().toString().trim();
                if (!StringUtils.isEmpty(amount)) {
                    amountF = (float) (Float.parseFloat(amount) + 1.0);
                    tvTotalAmount.setText(String.valueOf(amountF));
                } else {
                    tvTotalAmount.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(getString(R.string.info_cash_out));
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(getString(R.string.info_add_money));
        toolbar.setTitleTextColor(Color.WHITE);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreate:
                if (AppoPayApplication.isNetworkAvailable(this)) {
                    if (edAmount.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this, "Please enter request amount", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Helper.hideKeyboard(edAmount, CashOutActivity.this);
                        onUpdateProfile();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.no_inteenet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void onUpdateProfile() {
        dialog = new ProgressDialog(CashOutActivity.this);
        dialog.setMessage(getString(R.string.info_getting_profile));
        dialog.show();
        try {
            String accessToken = DataVaultManager.getInstance(CashOutActivity.this).getVaultValue(KEY_ACCESSTOKEN);
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
                        //DataVaultManager.getInstance(getContext()).saveUserDetails(res);
                        DataVaultManager.getInstance(CashOutActivity.this).saveUserDetails(res);
                        getCurrencyCode();
                    } else {
                        if (response.code() == 401) {
                            DataVaultManager.getInstance(CashOutActivity.this).saveUserDetails("");
                            DataVaultManager.getInstance(CashOutActivity.this).saveUserAccessToken("");
                            Intent intent = new Intent(CashOutActivity.this, SignInActivity.class);
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

    private void getCurrencyCode() {
        dialog = new ProgressDialog(CashOutActivity.this);
        dialog.setMessage(getString(R.string.info_get_curreny_code));
        dialog.show();

        mainAPIInterface.getCurrencyResponse().enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    result = response.body().getResult();
                    readUserAccounts();
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                dialog.dismiss();
                //Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

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
                String mIncryptAccount = getAccountNumber(index.getString(AppoConstants.ACCOUNTNUMBER));
                model.setAccountEncrypt(mIncryptAccount);
                //Log.e(TAG, "readUserAccounts: encrypt ::  " + mIncryptAccount);
                if (index.has(AppoConstants.ACCOUNTSTATUS)) {
                    //Log.e(TAG, "readUserAccounts: AccountStatus : " + index.getString(AppoConstants.ACCOUNTSTATUS));
                    model.setAccountstatus(index.getString(AppoConstants.ACCOUNTSTATUS));
                } else {
                    //Log.e(TAG, "readUserAccounts: AccountStatus : " + "null");
                    model.setAccountstatus("");
                }
                model.setCurrencyid(index.getString(AppoConstants.CURRENCYID));
                model.setCurrencyCode(getCurrency(index.getString(AppoConstants.CURRENCYID)));
                model.setCurrentbalance(index.getString(AppoConstants.CURRENTBALANCE));
                mListAccount.add(model);
            }
            if (mListAccount.size() > 0) {
                String accountEncrypt = mListAccount.get(0).getAccountnumber();
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

                }

            }

            if (mListAccount.size() > 0) {
                invalidateUserInfo(); //chage here

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getAccountNumber(String string) {
        char[] chars = string.toCharArray();
        String strTemp = "";
        int cout = 0;
        for (int i = chars.length - 1; i >= 0; i--) {
            cout = cout + 1;
            if (cout >= 4 && cout < 9) {
                strTemp = strTemp + "X";
            } else {
                String temp = String.valueOf(chars[i]);
                strTemp = strTemp + temp;
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append(strTemp);
        builder = builder.reverse();
        return String.valueOf(builder);
    }

    private String getCurrency(String param) {
        String res = null;
        for (int i = 0; i < result.size(); i++) {
            String sid = result.get(i).getId().toString();
            if (sid.equals(param)) {
                res = result.get(i).getCurrencyCode();
                break;
            }
        }
        return res;
    }

    private void invalidateUserInfo() {
        createUserQrCode();
    }

    private void createUserQrCode() {
        JSONObject root = null;
        try {

            String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
            //6367820101779950083|Cerca24|63516303|507| support@cerca24.com|undefined|USD
            JSONObject obj = new JSONObject(vaultValue);
            JSONObject resultParam = obj.getJSONObject(AppoConstants.RESULT);
            String companyName2 = "Appopay";
            String phoneNumber3 = resultParam.getString(AppoConstants.MOBILENUMBER);
            String areaCode4 = resultParam.getString(AppoConstants.PHONECODE);
            String emailId5 = resultParam.getString(AppoConstants.EMIAL);
            JSONObject objectCustomer = resultParam.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            //String symbol6 = objectCustomer.getString(AppoConstants.CURRENCYSYMBOL);
            String symbol6 = mListAccount.get(0).getCurrencyCode();
            JSONArray arrayParam = objectCustomer.getJSONArray(AppoConstants.CUSTOMERACCOUNT);
            JSONObject index0 = arrayParam.getJSONObject(0);
            String string = index0.getString(AppoConstants.ACCOUNTNUMBER);
            finalString = string + "|" + companyName2 + "|" + phoneNumber3 + "|" + areaCode4 + "|" + emailId5 + "|" + tvTotalAmount.getText().toString().trim() + "|" + symbol6;
            //generatedQrCode(finalString);
/*
            Bitmap bitmap = QRCodeUtil.encodeAsBitmap(finalString, 150, 150);
            user_qr_image.setImageBitmap(bitmap);*/
            //showDialog(finalString);

            String name = resultParam.getString(AppoConstants.FIRSTNAME) + " " + resultParam.getString(AppoConstants.LASTNAME);
            Intent intent = new Intent(CashOutActivity.this, CashOut2Activity.class);
            intent.putExtra(AppoConstants.INFO, finalString);
            intent.putExtra(AppoConstants.MOBILENUMBER, phoneNumber3);
            intent.putExtra(AppoConstants.FULLNAME, name);
            startActivity(intent);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}