package com.stuffer.stuffers.activity.restaurant;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.life.AllAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainShopAPIInterface;
import com.stuffer.stuffers.models.all_restaurant.RestaurentModels;
import com.stuffer.stuffers.models.shop_model.ItemDetails;
import com.stuffer.stuffers.utils.Helper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class R_AllFragment extends Fragment {

    private RecyclerView viewAllRecycle;
    int mPage = 0;
    private MainShopAPIInterface apiServiceShop;
    private NestedScrollView nestedScrollView;
    private ProgressBar progress_bar;
    private List<RestaurentModels.Array> mListShopItems;

    public R_AllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_r__all, container, false);
        apiServiceShop = ApiUtils.getAPIServiceShop();
        viewAllRecycle = mView.findViewById(R.id.viewAllRecycle);
        nestedScrollView = mView.findViewById(R.id.nestedScrollView);
        viewAllRecycle.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        progress_bar = mView.findViewById(R.id.progress_bar);
        mListShopItems=new ArrayList<>();

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
        apiServiceShop.getRestaurentAllItems(String.valueOf(mPage), String.valueOf(10)).enqueue(new Callback<RestaurentModels>() {
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