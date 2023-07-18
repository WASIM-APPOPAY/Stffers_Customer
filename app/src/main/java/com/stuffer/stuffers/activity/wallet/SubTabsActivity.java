package com.stuffer.stuffers.activity.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.address.BusinessAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chatUtils.DimenUtils;
import com.stuffer.stuffers.commonChat.chatUtils.ToastUtil;
import com.stuffer.stuffers.models.shop_model.ShopModel;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.MerchantInfoBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubTabsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvShop;
    private LinearLayout tabsContainers;
    private MainAPIInterface mainAPIInterface;
    private ProgressDialog mProgress;
    private TextView tvTitle;
    private View preView = null;
    private ArrayList<ArrayList<ShopModel>> requestList;
    private ArrayList<ShopModel> tabsList;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_tabs);

        requestList = Helper.getSubShopItems(this);
        int pos = getIntent().getIntExtra("pos", 0);
        title = getIntent().getStringExtra("name");
        tabsList = requestList.get(pos);

        rvShop = findViewById(R.id.rvAllShop);

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvShop.setLayoutManager(lm);
        rvShop.addItemDecoration(new MaterialDividerItemDecoration(this, MaterialDividerItemDecoration.VERTICAL));
        tabsContainers = findViewById(R.id.tabs_containers);
        tvTitle = findViewById(R.id.tabs_title);
        tvTitle.setText(title);
        findViewById(R.id.search_back).setOnClickListener(view -> SubTabsActivity.this.finish());
        mainAPIInterface = ApiUtils.getAPIService();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        tabsContainers.removeAllViews();
        for (ShopModel shopModel : tabsList) {
            View view = View.inflate(this, R.layout.row_shop_items, null);
            ImageView iv = view.findViewById(R.id.ivShopItems);
            TextView tv = view.findViewById(R.id.tvItemTitle);
            iv.setImageResource(shopModel.getResIds());
            tv.setText(shopModel.getmTitle());
            view.setTag(shopModel.getmTitle());
            tabsContainers.addView(view);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.rightMargin = DimenUtils.dip2px(this, 10);
            view.setLayoutParams(params);
            view.setBackground(null);
            view.setOnClickListener(this);
        }
        if (tabsContainers.getChildCount() > 0) {
            tabsContainers.getChildAt(0).performClick();
        }
    }

    @Override
    public void onClick(View view) {
        if (preView != null) {
            preView.setBackground(null);
        }
        view.setBackgroundResource(R.drawable.bg_border_address);
        preView = view;
        Object tag = view.getTag();
        if (tag != null) {
            String type = tag.toString();
            fetchMerchantByBusinessTypes(title, type);
        }
    }

    private void fetchMerchantByBusinessTypes(String businessType, String subBusinessType) {
        showLoading();
        mainAPIInterface.getMerchantBusinessTypes(businessType, subBusinessType).enqueue(new Callback<JsonObject>() {
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
            rvShop.setAdapter(new BusinessAdapter(SubTabsActivity.this, new ArrayList<>()));
        } else {
            rvShop.setAdapter(new BusinessAdapter(SubTabsActivity.this, data));
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










