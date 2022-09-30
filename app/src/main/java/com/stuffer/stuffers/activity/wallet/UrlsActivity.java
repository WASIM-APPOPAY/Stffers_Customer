package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;

import org.json.JSONException;
import org.json.JSONObject;

public class UrlsActivity extends AppCompatActivity {
    private static final String TAG = "UrlsActivity";

    private MyTextView tvLongText;
    private String mTitle, mUrlName;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urls);

        mTitle = getIntent().getStringExtra(AppoConstants.TITLE);
        mUrlName = getIntent().getStringExtra(AppoConstants.NAME);
        setupActionBar();
        tvLongText = (MyTextView) findViewById(R.id.tvLongText);


        getContent(Constants.APPOPAY_BASE_URL + mUrlName);

    }

    private void getContent(String urls) {
        showLoading();
        AndroidNetworking.get(urls)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        try {
                            String string = response.getString(AppoConstants.RESULT);
                            tvLongText.setText(Html.fromHtml(string));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.e(TAG, "onResponse: "+response.toString() );
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        Helper.showErrorMessage(UrlsActivity.this, anError.getMessage());
                    }
                });
    }

    public void showLoading() {
        if (mProgress == null) {
            mProgress = new ProgressDialog(UrlsActivity.this);
        }
        mProgress.setMessage(getString(R.string.info_please_wait_dots));
        mProgress.show();
    }

    public void hideLoading() {
        if (mProgress != null) {
            mProgress.dismiss();
            mProgress = null;
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView menu_icon = toolbar.findViewById(R.id.menu_icon);
        menu_icon.setVisibility(View.GONE);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setVisibility(View.VISIBLE);

        toolbarTitle.setText(mTitle);

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