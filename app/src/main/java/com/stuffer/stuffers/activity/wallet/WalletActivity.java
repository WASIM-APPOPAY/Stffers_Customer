package com.stuffer.stuffers.activity.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.adapter.recyclerview.AccountAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.fragments.dialog.CurrencyDialogFragment;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

public class WalletActivity extends AppCompatActivity implements RecyclerViewRowItemCLickListener {
    private static final String TAG = "WalletActivity";
    private RecyclerView rvAccounts;
    private MyButton btnCreate;
    private MainAPIInterface mainAPIInterface;
    private ProgressDialog dialog;
    List<CurrencyResult> result;
    ArrayList<AccountModel> mListAccount;
    private int mCurrencyId = 0;
    private String mCurrencyCode = null;
    private CurrencyDialogFragment dialogFragment;
    private String areacode;
    private String mobileNumber;
    private MyTextView tvAccountNos, tvFullName;
    FrameLayout frameWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        frameWallet = findViewById(R.id.frameWallet);
        mainAPIInterface = ApiUtils.getAPIService();
        tvAccountNos = findViewById(R.id.tvAccountNos);
        tvFullName = findViewById(R.id.tvFullName);
        /*rvAccounts = (RecyclerView) findViewById(R.id.rvAccounts);
        rvAccounts.setLayoutManager(new LinearLayoutManager(this));*/
        setupActionBar();


        getCurrencyCode();
        frameWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(AppoConstants.ACCOUNTNUMBER, mListAccount.get(0).getAccountnumber());
                intent.putExtra(AppoConstants.ACCOUNTSTATUS, mListAccount.get(0).getAccountstatus());
                intent.putExtra(AppoConstants.CURRENCYID, mListAccount.get(0).getCurrencyCode());
                intent.putExtra(AppoConstants.CURRENTBALANCE, mListAccount.get(0).getCurrentbalance());
                intent.putExtra(AppoConstants.ID, mListAccount.get(0).getId());
                intent.putExtra(AppoConstants.RESERVEAMOUNT, mListAccount.get(0).getReserveamount());
                intent.putExtra(AppoConstants.CURRENCYCODE, mListAccount.get(0).getCurrencyCode());
                intent.putExtra(AppoConstants.CURRENTBALANCE,mListAccount.get(0).getCurrentbalance());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    private void getCurrencyCode() {
        dialog = new ProgressDialog(WalletActivity.this);
        dialog.setMessage("Please wait, getting currency code.");
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
                model.setAccountEncrypt(Helper.getAccountNumber(index.getString(AppoConstants.ACCOUNTNUMBER)));
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
                /*String accountEncrypt = mListAccount.get(0).getAccountnumber();
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

                }*/

                tvAccountNos.setText(mListAccount.get(0).getAccountEncrypt());

                //Log.e(TAG, "readUserAccounts: ==============" + finalString);

                tvFullName.setText(Helper.getSenderName());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


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

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText("Wallet Account");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

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
    public void onRowItemClick(int pos) {
        //Log.e(TAG, "onRowItemClick: position " + pos);
        Intent intent = new Intent();
        intent.putExtra(AppoConstants.ACCOUNTNUMBER, mListAccount.get(pos).getAccountnumber());
        intent.putExtra(AppoConstants.ACCOUNTSTATUS, mListAccount.get(pos).getAccountstatus());
        intent.putExtra(AppoConstants.CURRENCYID, mListAccount.get(pos).getCurrencyCode());
        intent.putExtra(AppoConstants.CURRENTBALANCE, mListAccount.get(pos).getCurrentbalance());
        intent.putExtra(AppoConstants.ID, mListAccount.get(pos).getId());
        intent.putExtra(AppoConstants.RESERVEAMOUNT, mListAccount.get(pos).getReserveamount());
        intent.putExtra(AppoConstants.CURRENCYCODE, mListAccount.get(pos).getCurrencyCode());
        setResult(Activity.RESULT_OK, intent);
        finish();


    }
}
