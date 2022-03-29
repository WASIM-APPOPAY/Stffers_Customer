package com.stuffer.stuffers.activity.shop_mall;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainShopAPIInterface;
import com.stuffer.stuffers.communicator.ItemDetailsListener;
import com.stuffer.stuffers.communicator.ShopItemListener;
import com.stuffer.stuffers.models.shop_model.ItemDetails;
import com.stuffer.stuffers.utils.AppoConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShopItemDetailsFragment extends Fragment implements ShopItemListener {
    private static final String TAG = "ShopItemDetailsFragment";
    RecyclerView rvItemCategory;
    ProgressBar progress_bar;
    private String categoryId;
    private MainShopAPIInterface apiServiceShop;
    private int mPage = 0;
    private List<ItemDetails.Data.Items> mListItem;
    private NestedScrollView nestedScrollView;
    private ItemDetailsListener mItemDetailsListener;

    public ShopItemDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_shop_item_details, container, false);
        apiServiceShop = ApiUtils.getAPIServiceShop();
        Bundle arguments = this.getArguments();
        mListItem = new ArrayList<>();
        categoryId = arguments.getString(AppoConstants.CATEGORY_ID);
        rvItemCategory = mView.findViewById(R.id.rvItemCategory);
        nestedScrollView = mView.findViewById(R.id.nestedScrollView);
        rvItemCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false));
        progress_bar = mView.findViewById(R.id.progress_bar);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    //mPage++;
                    getCategoryItems(categoryId);
                }

            }
        });
        getCategoryItems(categoryId);


        return mView;
    }

    private void getCategoryItems(String categoryId) {
        mPage = mPage + 1;
        apiServiceShop.getShopCategoryItemsDetails(String.valueOf(mPage), "10", "", "", "", categoryId, "", "", "", "", "", "", "").enqueue(new Callback<ItemDetails>() {
            @Override
            public void onResponse(Call<ItemDetails> call, Response<ItemDetails> response) {
                //Log.e(TAG, "onResponse: " + response.body().getData().getCount());
                if (!response.body().getError()) {
                    //mListItem = ;
                    List<ItemDetails.Data.Items> items = response.body().getData().getItems();
                    mListItem.addAll(items);
                    setAdapter();
                }
            }

            @Override
            public void onFailure(Call<ItemDetails> call, Throwable t) {
                //Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    private void setAdapter() {
        ShopItemDetailAdapter adapter = new ShopItemDetailAdapter(getActivity(), mListItem,this);
        rvItemCategory.setAdapter(adapter);
    }

    @Override
    public void onShopItemClick(String param) {
        //Log.e(TAG, "onShopItemClick: " + param);
        mItemDetailsListener.onItemDetailsReceived(param);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mItemDetailsListener=(ItemDetailsListener)context;
    }
}