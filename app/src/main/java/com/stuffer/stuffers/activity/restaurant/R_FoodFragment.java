package com.stuffer.stuffers.activity.restaurant;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.life.AllAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainShopAPIInterface;
import com.stuffer.stuffers.models.all_restaurant.RestaurentModels;
import com.stuffer.stuffers.utils.Helper;

import java.util.ArrayList;
import java.util.List;

import im.delight.android.webview.AdvancedWebView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//public class R_FoodFragment extends Fragment implements AdvancedWebView.Listener{
public class R_FoodFragment extends Fragment {

    private RecyclerView viewAllRecycle;
    int mPage = 0;
    private MainShopAPIInterface apiServiceShop;
    private NestedScrollView nestedScrollView;
    private ProgressBar progress_bar;
    private List<RestaurentModels.Array> mListShopItems;
    private AdvancedWebView webView;
    private ProgressBar progressBar;

    public R_FoodFragment() {
        // Required empty public constructor
    }


    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_r__food, container, false);
        webView = (AdvancedWebView) mView.findViewById(R.id.webView);
        progressBar = mView.findViewById(R.id.progressBar);
        webView.setMixedContentAllowed(false);
        webView.setListener(getActivity(),this);
        String url = "https://delicious.appopay.com/h5/#/restaurants";
        webView.loadUrl(url);
        return mView;
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    webView.loadUrl(url);
    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //view.loadUrl(url);
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
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_r__food, container, false);
        apiServiceShop = ApiUtils.getAPIServiceShop();
        viewAllRecycle = mView.findViewById(R.id.viewAllRecycle);
        nestedScrollView = mView.findViewById(R.id.nestedScrollView);
        viewAllRecycle.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        progress_bar = mView.findViewById(R.id.progress_bar);
        mListShopItems = new ArrayList<>();

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {

                    getItems();
                }

            }
        });
        getItems();
        return mView;
    }

    private void getItems() {
        mPage = mPage + 1;
        progress_bar.setVisibility(View.VISIBLE);
        apiServiceShop.getRestaurentAllItemsFood(String.valueOf(mPage), String.valueOf(10)).enqueue(new Callback<RestaurentModels>() {
            @Override
            public void onResponse(Call<RestaurentModels> call, Response<RestaurentModels> response) {
                progress_bar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<RestaurentModels.Array> array = response.body().getArray();

                    if (array.size() > 0) {
                        List<RestaurentModels.Array> items = response.body().getArray();
                        mListShopItems.addAll(items);
                        setAdapter();
                    } else {
                        //Helper.showLongMessage(getActivity(), "no more data");
                    }
                }

            }

            @Override
            public void onFailure(Call<RestaurentModels> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                Helper.showLongMessage(getActivity(), t.getMessage());
            }
        });
    }

    private void setAdapter() {
        AllAdapter mAllAdapter = new AllAdapter(getActivity(), mListShopItems);
        viewAllRecycle.setAdapter(mAllAdapter);
    }
}