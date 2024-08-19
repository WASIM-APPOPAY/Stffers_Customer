package com.stuffer.stuffers.activity.quick_pass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.FianceTab.UnionPayActivity;
import com.stuffer.stuffers.activity.wallet.HomeActivity3;
import com.stuffer.stuffers.activity.wallet.UrlsActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.json.JSONException;
import org.json.JSONObject;

public class CardTermsActivity extends AppCompatActivity {

    private MyTextViewBold common_toolbar_title;
    private MyTextView tvLongText;
    private CheckBox checkbox;
    private ProgressDialog mProgress;

    public void showLoading() {
        if (mProgress == null) {
            mProgress = new ProgressDialog(CardTermsActivity.this);
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

    int mType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_terms);
        setupActionBar();

        mType = getIntent().getIntExtra(AppoConstants.CARDTYPE, 0);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        tvLongText = (MyTextView) findViewById(R.id.tvLongText);
        Button btnNext = findViewById(R.id.btnNextTerms);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox.isChecked()) {
                    if (mType == 1) {
                        Intent intentUnion = new Intent(CardTermsActivity.this, UnionPayActivity.class);
                        intentUnion.putExtra(AppoConstants.CARDTYPE, mType);
                        startActivity(intentUnion);
                        finish();
                    } else {
                        Intent intentUnion = new Intent(CardTermsActivity.this, VisaUnionActivity.class);
                        intentUnion.putExtra(AppoConstants.CARDTYPE, mType);
                        startActivity(intentUnion);
                        finish();
                    }
                } else {
                    Toast.makeText(CardTermsActivity.this, "" + getString(R.string.ifo_terms_conditions), Toast.LENGTH_SHORT).show();
                }
            }
        });
        getContent("https://prodapi.appopay.com/api/term-condition");
    }

    private void getContent(String urls) {
        showLoading();
        AndroidNetworking.get(urls)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String string = response.getString(AppoConstants.RESULT);
                            tvLongText.setText(Html.fromHtml(string));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.e(TAG, "onResponse: "+response.toString() );
                        hideLoading();
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        Helper.showErrorMessage(CardTermsActivity.this, anError.getMessage());
                    }
                });
    }

    private void setupActionBar() {
        common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        common_toolbar_title.setText("Terms and Conditions");
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}