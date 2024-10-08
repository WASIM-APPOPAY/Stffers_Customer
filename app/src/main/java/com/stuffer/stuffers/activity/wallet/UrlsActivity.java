package com.stuffer.stuffers.activity.wallet;

import static com.stuffer.stuffers.utils.DataVaultManager.KEY_USER_LANGUAGE;
import static com.stuffer.stuffers.utils.DataVaultManager.TANDC;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.MyContextWrapper;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.commonChat.chat.NumberActivity;
import com.stuffer.stuffers.commonChat.chat.NumberDemoActivity;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import im.delight.android.webview.AdvancedWebView;

public class UrlsActivity extends AppCompatActivity implements AdvancedWebView.Listener {
    private static final String TAG = "UrlsActivity";
    private static final int SPLASH_TIMEOUT = 100;
    private static final int BEFORE_13 = 2298;
    private static final int AFTER_13 = 2299;
    private MyTextView tvLongText;
    private String mTitle, mUrlName;
    private static ProgressDialog mProgress;
    private CheckBox checkbox;
    private LinearLayout rLayout;
    private ChatHelper chatHelper;
    private AdvancedWebView webView;
    private MyTextViewBold common_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urls);
        rLayout = findViewById(R.id.rLayout);
        webView = findViewById(R.id.webView);


        checkbox = findViewById(R.id.checkbox);
        mTitle = getIntent().getStringExtra(AppoConstants.TITLE);
        mUrlName = getIntent().getStringExtra(AppoConstants.NAME);
        setupActionBar();

        tvLongText = (MyTextView) findViewById(R.id.tvLongText);
        chatHelper = new ChatHelper(this);
        String vaultValue = DataVaultManager.getInstance(UrlsActivity.this).getVaultValue(TANDC);
        if (!StringUtils.isEmpty(vaultValue)) {
            checkbox.setChecked(true);
        }
        boolean checkPermission = checkPermission();
        Button btnNext = findViewById(R.id.btnNextTerms);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox.isChecked()) {
                    if (checkPermission) {
                        /*User loggedInUser = chatHelper.getLoggedInUser();
                        String s = new Gson().toJson(loggedInUser);*/
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setupUI();
                            }
                        }, SPLASH_TIMEOUT);
                    } else {
                        showPermission(getString(R.string.permission_desc_storage));
                    }
                } else {
                    Toast.makeText(UrlsActivity.this, "" + getString(R.string.ifo_terms_conditions), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //getContent(Constants.APPOPAY_BASE_URL + mUrlName);
        //showLoading();
        //webView.setListener(this, this);
        //webView.setMixedContentAllowed(false);
        //webView.loadUrl("https://tool.appopay.com/tac");

        /*MyWebViewClient myWebViewClient = new MyWebViewClient();
        webView.setWebViewClient(myWebViewClient);*/


        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    DataVaultManager.getInstance(UrlsActivity.this).saveTerm("yes");
                } else {
                    DataVaultManager.getInstance(UrlsActivity.this).saveTerm("");
                }


            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BEFORE_13) {
            //Log.e(TAG, "onRequestPermissionsResult: below called" );
            if (grantResults.length > 0) {
                boolean p1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean p2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean p3 = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean p4 = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean p5 = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                if (p1 && p2 && p3 && p4 && p5) {
                    setupUI();
                } else {
                    showPermission(getString(R.string.permission_desc_storage));
                }
            }
        } else if (requestCode == AFTER_13) {
            if (grantResults.length > 0) {

                boolean p1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean p2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean p3 = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean p4 = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean p5 = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                if (p1 && p2 && p3 && p4 && p5) {
                    setupUI();
                } else {
                    showPermission(getString(R.string.permission_desc_storage));
                }
            }
        }
    }

    private boolean checkPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            int result1 = ContextCompat.checkSelfPermission(UrlsActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int result2 = ContextCompat.checkSelfPermission(UrlsActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int result3 = ContextCompat.checkSelfPermission(UrlsActivity.this, android.Manifest.permission.READ_CONTACTS);
            int result4 = ContextCompat.checkSelfPermission(UrlsActivity.this, android.Manifest.permission.RECORD_AUDIO);
            int result5 = ContextCompat.checkSelfPermission(UrlsActivity.this, Manifest.permission.CAMERA);

            return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                    && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED
                    && result5 == PackageManager.PERMISSION_GRANTED;
        } else {
            int result1 = ContextCompat.checkSelfPermission(UrlsActivity.this, Manifest.permission.READ_MEDIA_AUDIO);
            int result2 = ContextCompat.checkSelfPermission(UrlsActivity.this, Manifest.permission.READ_MEDIA_IMAGES);
            int result3 = ContextCompat.checkSelfPermission(UrlsActivity.this, android.Manifest.permission.READ_CONTACTS);
            int result4 = ContextCompat.checkSelfPermission(UrlsActivity.this, android.Manifest.permission.RECORD_AUDIO);
            int result5 = ContextCompat.checkSelfPermission(UrlsActivity.this, Manifest.permission.CAMERA);

            return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                    && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED
                    && result5 == PackageManager.PERMISSION_GRANTED;
        }

    }

    public void showPermission(String permission_desc) {
        Snackbar snackbar = Snackbar.make(rLayout, permission_desc, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                            //Toast.makeText(UrlsActivity.this, "Below Called", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(UrlsActivity.this, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.CAMERA
                            }, BEFORE_13);
                        } else {
                            ActivityCompat.requestPermissions(UrlsActivity.this, new String[]{
                                    Manifest.permission.READ_MEDIA_AUDIO,
                                    Manifest.permission.READ_MEDIA_IMAGES,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.CAMERA
                            }, AFTER_13);
                        }


                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View view = snackbar.getView();
        TextView sbTextView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        sbTextView.setMaxLines(3);
        sbTextView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void setupUI() {

        /*Intent i = new Intent(UrlsActivity.this, NumberDemoActivity.class);
        startActivity(i);
        finish();*/

        /*Intent i = new Intent(UrlsActivity.this, Registration.class);
        startActivity(i);
        finish();*/
        if (chatHelper.getLoggedInUser() != null) {
            Intent i = new Intent(UrlsActivity.this, HomeActivity3.class);
            startActivity(i);
            finish();
        } else {


            Intent i = new Intent(UrlsActivity.this, NumberActivity.class);
            startActivity(i);
            finish();


        }


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

    public static void hideLoading() {
        if (mProgress != null) {
            mProgress.dismiss();
            mProgress = null;
        }
    }

    private void setupActionBar() {
        common_toolbar_title = (MyTextViewBold) findViewById(R.id.common_toolbar_title);
        common_toolbar_title.setText(mTitle);
        ImageView iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        iv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        String userLanguage = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(KEY_USER_LANGUAGE);
        if (StringUtils.isEmpty(userLanguage)) {
            //userLanguage = "es";
            userLanguage = "en";
        }
        super.attachBaseContext(MyContextWrapper.wrap(newBase, userLanguage));
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        showLoading();
    }

    @Override
    public void onPageFinished(String url) {
        hideLoading();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        hideLoading();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideLoading();
        }

    }
}