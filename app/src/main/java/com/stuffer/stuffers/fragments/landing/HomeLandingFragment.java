package com.stuffer.stuffers.fragments.landing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.MobileRechargeActivity;
import com.stuffer.stuffers.utils.AppoConstants;
import com.tamic.jswebview.browse.CallBackFunction;
import com.tamic.jswebview.browse.JsWeb.CustomWebViewClient;
import com.tamic.jswebview.browse.JsWeb.JavaCallHandler;
import com.tamic.jswebview.browse.JsWeb.JsHandler;
import com.tamic.jswebview.view.ProgressBarWebView;

import java.util.ArrayList;
import java.util.Map;

import im.delight.android.webview.AdvancedWebView;


public class HomeLandingFragment extends Fragment {
    private static final String TAG = "HomeLandingFragment";
    private AdvancedWebView webView;
    private ProgressBar progressBar;
    private LinearLayout includeLayout;

    // UI references.
    private ProgressBarWebView mProgressBarWebView;
    private ArrayList<String> mHandlers = new ArrayList<>();

    ValueCallback<Uri> mUploadMessage;
    private static CallBackFunction mfunction;

    int RESULT_CODE = 0;
    private LinearLayout llRecharge;


    public HomeLandingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_home_landing, container, false);

        webView = mView.findViewById(R.id.webView);
        progressBar = mView.findViewById(R.id.progressBar);
        includeLayout = mView.findViewById(R.id.includeLayout);
        llRecharge = mView.findViewById(R.id.llRecharge);
        llRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getActivity(), MobileRechargeActivity.class);
                mIntent.putExtra(AppoConstants.WHERE, 2);
                startActivity(mIntent);
            }
        });

        // Setting a webViewClient
        // WebViewClient mWebViewClient = new WebViewClient();


        // Loading a URL
        webView.getSettings().setLoadsImagesAutomatically(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.addJavascriptInterface();
        String url = "http://18.220.143.66:83/#/home";
        webView.loadUrl(url);
        initWebView(mView);

        return mView;

    }

    private void initWebView(View mView) {
        mProgressBarWebView = (ProgressBarWebView) mView.findViewById(R.id.login_progress_webview);
        mProgressBarWebView.setWebViewClient(new CustomWebViewClient(mProgressBarWebView.getWebView()) {
            @Override
            public String onPageError(String url) {
                //指定网络加载失败时的错误页面
                return "file:///android_asset/error.html";
            }

            @Override
            public Map<String, String> onPageHeaders(String url) {
                // 可以加入header
                return null;
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                pickFile();
            }
        });

        // 打开页面，也可以支持网络url
        mProgressBarWebView.loadUrl("http://18.220.143.66:83/#/home");
        mHandlers.add("login");
        mHandlers.add("restaurant");
        mHandlers.add("sh_sports");
        mHandlers.add("metro");
        mHandlers.add("city_services");
        mHandlers.add("rights");
        mHandlers.add("loan");
        mHandlers.add("store");
        mHandlers.add("card_repay");
        mHandlers.add("callNative");
        mHandlers.add("callJs");
        mHandlers.add("open");

        //js call native
        mProgressBarWebView.registerHandlers(mHandlers, new JsHandler() {
            @Override
            public void OnHandler(String handlerName, String responseData, CallBackFunction function) {
                if (handlerName.equals("login")) {
                    Toast.makeText(getActivity(), responseData, Toast.LENGTH_SHORT).show();
                } else if (handlerName.equals("callNative")) {
                    Toast.makeText(getActivity(), responseData, Toast.LENGTH_SHORT).show();
                    function.onCallBack("我在上海");
                } else if (handlerName.equals("callJs")) {
                    Toast.makeText(getActivity(), responseData, Toast.LENGTH_SHORT).show();
                    // 想调用你的方法：
                    function.onCallBack("好的 这是图片地址 ：xxxxxxx");
                }
                if (handlerName.equals("open")) {
                    mfunction = function;
                    pickFile();
                }

            }
        });

        // native call js
        mProgressBarWebView.callHandler("callNative", "hello H5, 我是java", new JavaCallHandler() {
            @Override
            public void OnHandler(String handlerName, String jsResponseData) {
                Toast.makeText(getActivity(), "h5返回的数据：" + jsResponseData, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

            mfunction.onCallBack(intent.getData().toString());

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressBarWebView.getWebView() != null) {
            mProgressBarWebView.getWebView().destroy();
        }
    }


    /*public class WebViewClient extends android.webkit.WebViewClient {
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
            includeLayout.setVisibility(View.VISIBLE);
        }


    }*/
}