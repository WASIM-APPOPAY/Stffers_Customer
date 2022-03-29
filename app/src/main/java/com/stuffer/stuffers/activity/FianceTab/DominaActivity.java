package com.stuffer.stuffers.activity.FianceTab;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.R;

import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.communicator.BankSelectListener;
import com.stuffer.stuffers.communicator.TypeSelectListener;
import com.stuffer.stuffers.fragments.dialog.BankDialog;
import com.stuffer.stuffers.fragments.dialog.TypeDialog;
import com.stuffer.stuffers.models.Country.CountryCodeResponse;
import com.stuffer.stuffers.models.Country.Result;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.AccountNumberEditText;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DominaActivity extends AppCompatActivity implements View.OnClickListener, TypeSelectListener, BankSelectListener {

    private ArrayList<String> mBankList;
    private BankDialog mBankDialog;
    private TextView toolbarTitle;
    private LinearLayout llBankType;
    private MyTextView tvBankName;
    private MyEditText edBankFocus;
    private LinearLayout llAccType;
    private MyTextView tvAccType;
    private MyEditText edAccFocus;
    private AccountNumberEditText card_number_field_text;
    private TextInputEditText cardholder_field_text;
    private MyEditText edBankName;
    private ArrayList<String> mBankType;
    private TypeDialog mTypeDialog;
    private MyButton btnSubmitBank;
    private ProgressDialog dialog;
    List<Result> mListCountry;
    private static final String TAG = "DominaActivity";
    private MainAPIInterface mainAPIInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domina);
        mainAPIInterface = ApiUtils.getAPIService();
        setupActionBar();
        btnSubmitBank = (MyButton) findViewById(R.id.btnSubmitBank);
        llBankType = (LinearLayout) findViewById(R.id.llBankType);
        llAccType = (LinearLayout) findViewById(R.id.llAccType);
        tvBankName = (MyTextView) findViewById(R.id.tvBankName);
        tvAccType = (MyTextView) findViewById(R.id.tvAccType);
        edBankFocus = (MyEditText) findViewById(R.id.edBankFocus);
        edAccFocus = (MyEditText) findViewById(R.id.edAccFocus);
        card_number_field_text = (AccountNumberEditText) findViewById(R.id.card_number_field_text);
        cardholder_field_text = (TextInputEditText) findViewById(R.id.cardholder_field_text);
        edBankName = (MyEditText) findViewById(R.id.edBankName);
        llAccType.setOnClickListener(this);
        llBankType.setOnClickListener(this);
        tvBankName.setOnClickListener(this);
        tvAccType.setOnClickListener(this);
        btnSubmitBank.setOnClickListener(this);

        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        try {
            JSONObject index = new JSONObject(vaultValue);
            JSONObject jsonResult = index.getJSONObject(AppoConstants.RESULT);
            JSONObject jsonCustomerDetails = jsonResult.getJSONObject(AppoConstants.CUSTOMERDETAILS);
            String mCustId = jsonCustomerDetails.getString(AppoConstants.COUNTRYID);
            //Log.e(TAG, "onCreate: Country IDs "+mCustId);
            if (mCustId.equalsIgnoreCase("1")) {
                llBankType.setVisibility(View.VISIBLE);
            } else if (mCustId.equalsIgnoreCase("33")) {
                llBankType.setVisibility(View.VISIBLE);
                //getBankNames();
            } else {
              llBankType.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }



    /*private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Link Your Bank");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }*/


    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Link Your Bank");
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

    }

    private void getBankNames() {
        mBankList = new ArrayList<String>();
        mBankList.add("COOPSME");
        mBankList.add("COOPEMERO");
        mBankList.add("COOPETEX");
        mBankList.add("COOP-HERRERA");
        mBankDialog = new BankDialog();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.TITLE, "Please Select Bank Name");
        bundle.putStringArrayList(AppoConstants.INFO, mBankList);
        mBankDialog.setArguments(bundle);
        mBankDialog.setCancelable(false);
        mBankDialog.show(getSupportFragmentManager(), mBankDialog.getTag());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llBankType) {
            getBankNames();
        } else if (view.getId() == R.id.llAccType) {
            getBankType();
        } else if (view.getId() == R.id.tvBankName) {
            getBankNames();
        } else if (view.getId() == R.id.tvAccType) {
            getBankType();
        } else if (view.getId() == R.id.btnSubmitBank) {
            Intent intent = new Intent();
            intent.putExtra("hasAccount", "YES");
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void getBankType() {
        mBankType = new ArrayList<String>();
        mBankType.add("Checking Account");
        mBankType.add("Saving Account");
        mBankType.add("Upi Card");

        mTypeDialog = new TypeDialog();
        Bundle bundle = new Bundle();
        bundle.putString(AppoConstants.TITLE, "Please Select A/c Type ");
        bundle.putStringArrayList(AppoConstants.INFO, mBankType);
        mTypeDialog.setArguments(bundle);
        mTypeDialog.setCancelable(false);
        mTypeDialog.show(getSupportFragmentManager(), mTypeDialog.getTag());
    }

    @Override
    public void onBankSelect(int pos) {
        if (mBankDialog != null)
            mBankDialog.dismiss();
        tvBankName.setText("Name : " + mBankList.get(pos));

    }

    @Override
    public void onTypeSelect(int pos) {
        if (mTypeDialog != null)
            mTypeDialog.dismiss();
        tvAccType.setText("Tyep : " + mBankType.get(pos));
    }



    /*1. Type of Account
            Checking ,Saving, UpiCard
    2.Account Number
    3.Account Name
    4.Branch Name*/


}
