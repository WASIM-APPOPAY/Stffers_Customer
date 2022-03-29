package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.adapter.recyclerview.TransactionListAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.models.output.CurrencyResponse;
import com.stuffer.stuffers.models.output.CurrencyResult;
import com.stuffer.stuffers.models.output.TransactionList2;
import com.stuffer.stuffers.models.output.TransactionListResponse;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MySwitchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;

public class TransactionListActivity extends AppCompatActivity implements RecyclerViewRowItemCLickListener {
    private List<TransactionListResponse.Result> mListTransaction;
    private ProgressDialog dialog;
    private MainAPIInterface mainAPIInterface;
    private static final String TAG = "TransactionListActivity";
    private RecyclerView rvTransactionList;
    private String mAccountNumber, mEncryptAccountNumber;
    List<CurrencyResult> result;
    MySwitchView swAccountNumber;
    ArrayList<TransactionList2> mListFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        mainAPIInterface = ApiUtils.getAPIService();
        rvTransactionList = findViewById(R.id.rvTransactionList);
        swAccountNumber = findViewById(R.id.swAccountNumber);
        rvTransactionList.setLayoutManager(new LinearLayoutManager(TransactionListActivity.this));
        mAccountNumber = getIntent().getStringExtra(AppoConstants.ACCOUNTNUMBER);
        mEncryptAccountNumber = getIntent().getStringExtra(AppoConstants.ENCRYPTACCOUNTNUMBER);
        setupActionBar();
        swAccountNumber.setText("Account Number :  " + mEncryptAccountNumber);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTransactionList();
            }
        }, 200);

        swAccountNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swAccountNumber.setText("A/C Number :  " + mAccountNumber);
                } else {
                    swAccountNumber.setText("A/C Number :  " + mEncryptAccountNumber);
                }
            }
        });


    }

    private void getCurrencyCode() {
        dialog = new ProgressDialog(TransactionListActivity.this);
        dialog.setMessage(getString(R.string.info_get_curreny_code));
        dialog.show();

        mainAPIInterface.getCurrencyResponse().enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    result = response.body().getResult();
                    readTranscationDetails();
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                dialog.dismiss();
              //  Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    /*String id;
    String mobilenumber;
    String accountnumber;
    String transactiontype;
    String transactiondescription;
    String transactiondate;
    String transactionamount;
    String pendingbalance;
    String transactionstatus;
    String userid;
    String transactionid;
    String curencyid;
    String sendername;
    String sendermobile;
    String processingfees;
    String bankfees;
    String paymenttype;
    String areacode;
    String taxes;*/

    private void readTranscationDetails() {

        mListFinal = new ArrayList<>();
        for (int i = 0; i < mListTransaction.size(); i++) {
            TransactionList2 model = new TransactionList2();
            model.setId(String.valueOf(mListTransaction.get(i).getId()));
            model.setMobilenumber(mListTransaction.get(i).getMobilenumber());
            model.setAccountnumber(mListTransaction.get(i).getAccountnumber());
            model.setTransactiontype(mListTransaction.get(i).getTransactiontype());

            model.setTransactiondescription(mListTransaction.get(i).getTransactiondescription());
            model.setTransactiondate(mListTransaction.get(i).getTransactiondate());//need change here
            model.setViewdate(getTimeDateOther(mListTransaction.get(i).getTransactiondate()));
            String balance = String.valueOf(mListTransaction.get(i).getTransactionamount());
            DecimalFormat df2 = new DecimalFormat("#.00");
            Double doubleV = Double.parseDouble(balance);
            String format = df2.format(doubleV);
            model.setTransactionamount(format);
            model.setPendingbalance(String.valueOf(mListTransaction.get(i).getPendingbalance()));
            model.setTransactionstatus(mListTransaction.get(i).getTransactionstatus());
            model.setUserid(String.valueOf(mListTransaction.get(i).getUserid()));
            model.setTransactionid(mListTransaction.get(i).getTransactionid());
            model.setCurencyid(String.valueOf(mListTransaction.get(i).getCurencyid()));
            model.setCurrencycode(getCurrency(String.valueOf(mListTransaction.get(i).getCurencyid())));
            model.setSendername(mListTransaction.get(i).getSendername());
            model.setSendermobile(mListTransaction.get(i).getSendermobile());
            model.setProcessingfees(String.valueOf(mListTransaction.get(i).getProcessingfees()));
            model.setBankfees(String.valueOf(mListTransaction.get(i).getBankfees()));
            model.setPaymenttype(String.valueOf(mListTransaction.get(i).getPaymenttype()));
            model.setAreacode(String.valueOf(mListTransaction.get(i).getAreacode()));
            model.setTaxes(String.valueOf(mListTransaction.get(i).getTaxes()));
            mListFinal.add(model);
        }

        TransactionListAdapter adapter = new TransactionListAdapter(TransactionListActivity.this, mListFinal);
        rvTransactionList.setAdapter(adapter);
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

    private void getTransactionList() {
        dialog = new ProgressDialog(TransactionListActivity.this);
        dialog.setMessage(getString(R.string.info_get_transaction));
        dialog.show();
        String accesstoken = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_ACCESSTOKEN);
        JsonObject paramSent = new JsonObject();
        paramSent.addProperty(AppoConstants.ACCOUNTNUMBER, mAccountNumber);
      //  Log.e(TAG, "getTransactionList: " + paramSent.toString());
        String bearer_ = Helper.getAppendAccessToken("bearer ", accesstoken);
        mainAPIInterface.postUserTransactionList(paramSent, bearer_).enqueue(new Callback<TransactionListResponse>() {
            @Override
            public void onResponse(Call<TransactionListResponse> call, Response<TransactionListResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase(AppoConstants.SUCCESS)) {
                      //  Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        mListTransaction = response.body().getResult();
                        getCurrencyCode();
                    } else {
                      //  Log.e(TAG, "onResponse: else called");
                    }
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(TransactionListActivity.this).saveUserDetails("");
                        DataVaultManager.getInstance(TransactionListActivity.this).saveUserAccessToken("");
                        Intent intent = new Intent(TransactionListActivity.this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (response.code() == 405) {
                        Helper.showErrorMessage(TransactionListActivity.this, getString(R.string.error_method_not_alloed));
                    } else if (response.code() == 400) {
                        Helper.showErrorMessage(TransactionListActivity.this, getString(R.string.error_bad_request));
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionListResponse> call, Throwable t) {
                dialog.dismiss();
              //  Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    private String getTimeDateOther(String dateParam){
        // milliseconds
        long milliSec = Long.parseLong(dateParam);

        // Creating date format
        //DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

        // Creating date from milliseconds
        // using Date() constructor
        Date result = new Date(milliSec);

        // Formatting Date according to the
        // given format
        //System.out.println(simple.format(result));
        Log.e(TAG, "getTimeDateOther: "+simple.format(result) );

        return simple.format(result);
    }



    private String getTimeDate(String dateString) {
        //  DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String ourDate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS");
            //TimeZone utc = TimeZone.getTimeZone("UTC");
            TimeZone utc = TimeZone.getDefault();
            formatter.setTimeZone(utc);
            Date value = formatter.parse(dateString);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd,yyyy,HH:mm:ss aa"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
           // crunchifyFormat
            ourDate = dateFormatter.format(value);
            Log.e(TAG, "getTimeDate: :::: " + ourDate);
            return ourDate;
            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            e.printStackTrace();
            ////Log.e(TAG, "getTimeDate: exception called");
        }


        return ourDate;
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(getString(R.string.info_transaction_list));

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
        JSONObject objSent = new JSONObject();
        try {
            objSent.put("id", mListFinal.get(pos).getId());
            objSent.put("mobilenumber", mListFinal.get(pos).getMobilenumber());
            objSent.put("accountnumber", mListFinal.get(pos).getAccountnumber());
            objSent.put("transactiontype", mListFinal.get(pos).getTransactiontype());
            objSent.put("transactiondescription", mListFinal.get(pos).getTransactiondescription());
            objSent.put("transactiondate", mListFinal.get(pos).getTransactiondate());
            objSent.put("viewdate", mListFinal.get(pos).getViewdate());
            objSent.put("transactionamount", mListFinal.get(pos).getTransactionamount());
            objSent.put("pendingbalance", mListFinal.get(pos).getPendingbalance());
            objSent.put("transactionstatus", mListFinal.get(pos).getTransactionstatus());
            objSent.put("userid", mListFinal.get(pos).getUserid());
            objSent.put("transactionid", mListFinal.get(pos).getTransactionid());
            objSent.put("curencyid", mListFinal.get(pos).getCurencyid());
            objSent.put("currencycode", mListFinal.get(pos).getCurrencycode());
            objSent.put("sendername", mListFinal.get(pos).getSendername());

            objSent.put("sendermobile", mListFinal.get(pos).getSendermobile());
            objSent.put("processingfees", mListFinal.get(pos).getProcessingfees());
            objSent.put("bankfees", mListFinal.get(pos).getBankfees());
            objSent.put("paymenttype", mListFinal.get(pos).getPaymenttype());
            objSent.put("areacode", mListFinal.get(pos).getAreacode());
            objSent.put("taxes", mListFinal.get(pos).getTaxes());
            objSent.put("mAccountNumber", mAccountNumber);
            objSent.put("mEncryptAccountNumber", mEncryptAccountNumber);
            //Log.e(TAG, "onRowItemClick: " + objSent);

            Intent intentDetails = new Intent(TransactionListActivity.this, TransactionDetailsActivity.class);
            intentDetails.putExtra(AppoConstants.INFO, objSent.toString());
            startActivity(intentDetails);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
