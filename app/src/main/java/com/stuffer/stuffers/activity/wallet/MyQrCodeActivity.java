package com.stuffer.stuffers.activity.wallet;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_DETIALS;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyQrCodeActivity extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    ProgressDialog mProgressDialog;
    private static final String TAG = "MyQrCodeActivity";
    MainAPIInterface apiService;

    CountryCodePicker.DialogEventsListener mLis = new CountryCodePicker.DialogEventsListener() {
        @Override
        public void onCcpDialogOpen(Dialog dialog) {
            dialog.dismiss();
        }

        @Override
        public void onCcpDialogDismiss(DialogInterface dialogInterface) {

        }

        @Override
        public void onCcpDialogCancel(DialogInterface dialogInterface) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiUtils.getAPIService();
        setContentView(R.layout.activity_my_qr_code);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        countryCodePicker.setEnabled(false);
        countryCodePicker.setExcludedCountries(getString(R.string.info_exclude_countries));
        countryCodePicker.setDialogEventsListener(mLis);
        setupActionBar();

        String customerId = Helper.getCustomerId();
        //int userId = Helper.getUserId();
        //getCustomerQrCode(String.valueOf(userId));
        String vaultValue = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_DETIALS);
        //Log.e(TAG, "onCreate: "+vaultValue);
        getCustomerQrCode(customerId);
    }
    //{"status":200,"message":"success","result":"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADYAAAA2AQAAAACkPldwAAABu0lEQVR4XgGwAU/+AAGqx3W3nAQAfW/SCtCN9ABF9sMsbm0UAEVfQsnV1RQARai9Bb4dFAB9cN1z0Z30AAFVVVVVVAQA/6SRdS9P/AAEKM0GtsKsACvHGmW+2nQAfTZLe9CtbACy4174Knr8ABXfQ523hRwA9gi89b8aNAA85rKrwMXsAK6K/ygvXuwAMLe6zfTRDABOEsd0t8r0AHWn0irYvSwAZv7DZjYQ7ABV70IJ4+cMAKIovXEj2vQATBFcjdytDACXjZlsK0rsAPBIzQWywAwAV28adC7HdAA1XmpSwLVsAFdxG3AKV3wAIHMDB7PwHAD+7N2Npt80AOwWskPQMywAj8L++GtNbABQD7p98NKMAJM6x4S/TVQA7D+XssCybABK2uHsOw3sAPXvQ+uk+oQAQyi9DSZBdAAoid3S0bLsAEtMkWxvTWwAQXjN7/LSjABXTxocr1t0ACH8S7LQuowAnkMdaBtVfADsQUMD5NAMAP8+vXRn1zQAAQGjU7m1LAB9uv50L0dsAEUBugWU0AwARVLH/D9C7ABFTdLqwLkUAH12xPRrY+wAAW9DbZLcHAD////////8Z87C8A+bY3QAAAAASUVORK5CYII\u003d"}

    public void show() {
        mProgressDialog = new ProgressDialog(MyQrCodeActivity.this);
        mProgressDialog.setMessage(getString(R.string.info_please_wait_dots));
        mProgressDialog.show();
    }

    public void hide() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    private void getCustomerQrCode(String id) {
         show();
        apiService.getCustomerQrCode(id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                 hide();
                Log.e(TAG, "onResponse: "+response );
                Log.e(TAG, "onResponse: "+new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hide();
                Log.e(TAG, "onFailure: "+t.getMessage() );
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
        toolbarTitle.setText("My Qr-Code");
        toolbarTitle.setLetterSpacing((float) 0.1);
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

}