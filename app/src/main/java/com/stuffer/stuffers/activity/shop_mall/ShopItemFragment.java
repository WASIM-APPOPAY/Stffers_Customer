package com.stuffer.stuffers.activity.shop_mall;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainShopAPIInterface;
import com.stuffer.stuffers.communicator.ShopItemListener;
import com.stuffer.stuffers.communicator.TitleListener;
import com.stuffer.stuffers.models.shop_model.ItemDetails;
import com.stuffer.stuffers.models.shop_model.ShopItem;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShopItemFragment extends Fragment implements ShopItemListener {

    private static final String TAG = "ShopItemFragment";
    private MainShopAPIInterface apiServiceShop;
    private RecyclerView rvCategory;
    private MyTextViewBold tvAlert;
    private ProgressDialog mLoading;
    private List<ShopItem.Datum> mListShopItems;
    private List<ShopItem.Datum.Child> mListchildren;
    private String extra;
    private TitleListener mListenerTitle;
    private ShopItemListener mShopItemListener;

    public ShopItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_shop_item, container, false);
        Bundle arguments = this.getArguments();
        extra = arguments.getString(AppoConstants.TITLE);
        apiServiceShop = ApiUtils.getAPIServiceShop();
        rvCategory = mView.findViewById(R.id.rvCategory);
        tvAlert = mView.findViewById(R.id.tvAlert);
        rvCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        getListItems();
        return mView;
    }

    private void getListItems() {
        showLoading(getString(R.string.info_please_wait));
        apiServiceShop.getShopCategoryItems().enqueue(new Callback<ShopItem>() {
            @Override
            public void onResponse(Call<ShopItem> call, Response<ShopItem> response) {
                dismissDialog();
                String res = new Gson().toJson(response.body());
                //Log.e(TAG, "onResponse: " + res);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (!response.body().error) {
                        mListShopItems = response.body().getData();
                        //Log.e(TAG, "onResponse: size :: " + mListShopItems.size());
                        showListItems();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShopItem> call, Throwable t) {
                dismissDialog();
            }
        });
    }

    private void showListItems() {
        mListchildren = null;
        for (int i = 0; i < mListShopItems.size(); i++) {
            if (mListShopItems.get(i).name.contains(extra)) {
                mListchildren = mListShopItems.get(i).children;
                break;
            }
        }
        if (mListchildren != null) {
            if (mListchildren.size() > 0) {
                ShopItemAdapter adapter = new ShopItemAdapter(getActivity(), mListchildren, this);
                rvCategory.setAdapter(adapter);
                rvCategory.setVisibility(View.VISIBLE);
                tvAlert.setVisibility(View.GONE);
            } else {
                tvAlert.setVisibility(View.VISIBLE);
                rvCategory.setVisibility(View.GONE);
            }
        } else {
            tvAlert.setVisibility(View.VISIBLE);
            rvCategory.setVisibility(View.GONE);
        }
    }


    private void showLoading(String message) {
        if (mLoading == null) {
            mLoading = new ProgressDialog(getActivity());
        }
        mLoading.setMessage(message);
        mLoading.show();
    }

    private void dismissDialog() {
        mLoading.dismiss();
    }

    public void setNewItem(String title) {
        if (mListShopItems == null) {
            extra = title;
            mListenerTitle.onTitleUpdate(extra);
            getListItems();
        } else {


            for (int i = 0; i < mListShopItems.size(); i++) {
                if (mListShopItems.get(i).name.contains(title)) {
                    mListchildren = mListShopItems.get(i).children;
                    break;
                }
            }
            if (mListchildren != null) {
                if (mListchildren.size() > 0) {
                    extra = title;
                    mListenerTitle.onTitleUpdate(extra);
                    ShopItemAdapter adapter = new ShopItemAdapter(getActivity(), mListchildren, this);
                    rvCategory.setAdapter(adapter);
                    rvCategory.setVisibility(View.VISIBLE);
                    tvAlert.setVisibility(View.GONE);
                } else {
                    tvAlert.setVisibility(View.VISIBLE);
                    rvCategory.setVisibility(View.GONE);
                }
            } else {
                tvAlert.setVisibility(View.VISIBLE);
                rvCategory.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListenerTitle = (TitleListener) context;
        mShopItemListener = (ShopItemListener) context;

    }

    @Override
    public void onShopItemClick(String categoryId) {
        //Log.e(TAG, "onShopItemClick: Category Id :: " + categoryId);
        mShopItemListener.onShopItemClick(categoryId);

    }




}