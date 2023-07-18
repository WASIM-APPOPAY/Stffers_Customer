package com.stuffer.stuffers.activity.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.TabsActivity;
import com.stuffer.stuffers.adapter.address.BusinessAdapter;
import com.stuffer.stuffers.adapter.recyclerview.AllRestaurantAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.chatUtils.ToastUtil;
import com.stuffer.stuffers.models.all_restaurant.AllRestaurant;
import com.stuffer.stuffers.models.all_restaurant.Content;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.MerchantInfoBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListActivity extends AppCompatActivity {
    private MainAPIInterface mainAPIInterface;
    private TextView tvTitle;
    private RecyclerView rvAllShop;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restuarnt_list);
        mainAPIInterface = ApiUtils.getAPIService();
        tvTitle = findViewById(R.id.tabs_title);
        rvAllShop = findViewById(R.id.rvAllShop);
        tvTitle.setText("List of Restaurants");
        findViewById(R.id.search_back).setOnClickListener(view -> RestaurantListActivity.this.finish());
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAllShop.setLayoutManager(lm);
        rvAllShop.addItemDecoration(new MaterialDividerItemDecoration(this, MaterialDividerItemDecoration.VERTICAL));
        //fetchMerchantByShopType("Restaurant");
        fetchAllRestaurant(); //open later
        /*Intent mIntent=new Intent(RestaurantListActivity.this, RestaurantItemActivity.class);
        mIntent.putExtra("userid","1");
        mIntent.putExtra("name","test merchant");
        startActivity(mIntent);*/
        
    }

    private void fetchAllRestaurant() {



        showLoading();
        mainAPIInterface.getAllRestaurant(0, 10).enqueue(new Callback<AllRestaurant>() {
            @Override
            public void onResponse(Call<AllRestaurant> call, Response<AllRestaurant> response) {
                hideLoading();

                if (response.body().getMessage().equalsIgnoreCase(AppoConstants.SUCCESS)) {

                    List<Content> content = response.body().getResult().getContent();
                    String s = new Gson().toJson(content);
                    Log.e("TAG", "onResponse: " + s);
                    passToAdapter(content);


                }
            }

            @Override
            public void onFailure(Call<AllRestaurant> call, Throwable t) {
                hideLoading();
            }
        });
    }

    private void passToAdapter(List<Content> content) {
        AllRestaurantAdapter mAllRestaurantAdapter = new AllRestaurantAdapter(content, RestaurantListActivity.this);
        rvAllShop.setAdapter(mAllRestaurantAdapter);
    }


    private void fetchMerchantByShopType(String shopType) {
        showLoading();
        mainAPIInterface.getMerchantShopType(shopType).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideLoading();
                try {
                    if (response.body().get("status").getAsInt() == 200) {
                        List<MerchantInfoBean> data = new Gson().fromJson(response.body().get("result").toString(), new TypeToken<List<MerchantInfoBean>>() {
                        }.getType());
                        fillRecyclerList(getFilterArrayList(data));
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideLoading();
            }
        });
    }

    private List<MerchantInfoBean> getFilterArrayList(List<MerchantInfoBean> data) {
        for (int i = 0; i < data.size(); i++) {
            MerchantInfoBean infoBean = data.get(i);
            if (!TextUtils.isEmpty(infoBean.businessAddress)) {
                String[] splitAdd = infoBean.businessAddress.split("@#");
                if (splitAdd.length > 1) {
                    infoBean.businessAddress = splitAdd[0];
                    infoBean.address = splitAdd[1];
                } else {
                    infoBean.businessAddress = "";
                }
            }
        }
        return data;
    }

    private void fillRecyclerList(List<MerchantInfoBean> data) {
        if (data == null || data.size() == 0) {
            ToastUtil.showTextShort("no data");
            rvAllShop.setAdapter(new BusinessAdapter(RestaurantListActivity.this, new ArrayList<>()));
        } else {
            rvAllShop.setAdapter(new BusinessAdapter(RestaurantListActivity.this, data));
        }
    }

    private void showLoading() {
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
        }
        mProgress.setMessage(getString(R.string.info_please_wait_dots));
        mProgress.show();
    }

    private void hideLoading() {
        mProgress.dismiss();
    }
}