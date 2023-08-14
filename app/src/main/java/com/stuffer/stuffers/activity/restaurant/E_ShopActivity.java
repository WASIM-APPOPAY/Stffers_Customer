
package com.stuffer.stuffers.activity.restaurant;

import static com.stuffer.stuffers.commonChat.chatUtils.ToastApp.getContext;
import static com.stuffer.stuffers.utils.DataVaultManager.KEY_ACCESSTOKEN;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.fragments.bottom_fragment.BottomRegister;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.webview.AdvancedWebView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class E_ShopActivity extends AppCompatActivity {
    private AdvancedWebView webView;
    private ProgressBar progressBar;
    private static final String TAG = "E_ShopActivity";
    private MainAPIInterface mainAPIInterface;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eshop);
        mainAPIInterface = ApiUtils.getAPIService();

        findViewById(R.id.search_back).setOnClickListener(view -> E_ShopActivity.this.onBackPressed());

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);

        // Setting a webViewClient
        WebViewClient mWebViewClient = new WebViewClient();


        // Loading a URL
        webView.getSettings().setLoadsImagesAutomatically(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        url = "http://18.220.143.66:81/h5/#/appopayguide?areacode="+Helper.getSenderAreaCode()+"&"+"phonenumber="+Helper.getSenderMobileNumber()+"&usertype=CUSTOMER";

        webView.setWebViewClient(mWebViewClient);

        isAccountExist();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private void isAccountExist() {
        String accessToken = DataVaultManager.getInstance(getContext()).getVaultValue(KEY_ACCESSTOKEN);
        String bearer_ = Helper.getAppendAccessToken("bearer ", accessToken);
        progressBar.setVisibility(View.VISIBLE);
        mainAPIInterface.getProfileDetails(Helper.getSenderMobileNumber(), Integer.parseInt(Helper.getPhoneCode()), bearer_).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);
                    webView.loadUrl(url);
                } else {
                    if (response.code() == 401) {
                        DataVaultManager.getInstance(getContext()).saveUserDetails("");
                        DataVaultManager.getInstance(getContext()).saveUserAccessToken("");
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        intent.putExtra(AppoConstants.WHERE, "12");
                        startActivity(intent);
                        finish();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //view.loadUrl(url);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Log.e(TAG, "onReceivedHttpError: called");

        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }


    }
}