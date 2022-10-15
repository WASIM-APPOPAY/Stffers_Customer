package com.stuffer.stuffers.activity.wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.stuffer.stuffers.utils.DataManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.MerchantInfoBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabsActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private RecyclerView rvShop;
    private LinearLayout tabsContainers;
    private MainAPIInterface mainAPIInterface;
    private ProgressDialog mProgress;
    private TextView tvTitle;
    private View preView = null;

    private TextView tabsPosition;
    private ViewGroup tabsPositionLayout;
    private View tabsPositionLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        rvShop = findViewById(R.id.rvAllShop);
        tabsPosition = findViewById(R.id.tabs_location);
        tabsPositionLayout = findViewById(R.id.tabs_location_layout);
        tabsPositionLine = findViewById(R.id.tabs_position_line);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvShop.setLayoutManager(lm);
        rvShop.addItemDecoration(new MaterialDividerItemDecoration(this, MaterialDividerItemDecoration.VERTICAL));
        tabsContainers = findViewById(R.id.tabs_containers);
        tvTitle = findViewById(R.id.tabs_title);
        findViewById(R.id.search_back).setOnClickListener(view -> TabsActivity.this.finish());
        /*tabsPositionLayout.setOnClickListener(view -> {
            Intent intent = new Intent(TabsActivity.this, CurMapActivity.class);
            startActivity(intent);
        });*/
        mainAPIInterface = ApiUtils.getAPIService();
        initView();

        // 判断当前是否拥有使用GPS的权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            openGPSSettings();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(DataManager.newPosition)) {
            tabsPosition.setText(DataManager.newPosition);
        } else {
            tabsPosition.setText("Click to select the current point");
        }
    }

    private void initView() {
        ArrayList<ShopModel> mList = Helper.getShopItems(this);
        tabsContainers.removeAllViews();
        for (ShopModel shopModel : mList) {
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
            tvTitle.setText(type);
            fetchMerchantByShopType(type);
        }
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
        for (int i = 0; i < data.size(); i ++) {
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
            rvShop.setAdapter(new BusinessAdapter(TabsActivity.this, new ArrayList<>()));
        } else {
            rvShop.setAdapter(new BusinessAdapter(TabsActivity.this, data));
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

    private LocationManager locationManager;
    private double curLatitude = 0;
    private double curLongitude = 0;

    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            getLocation();
            return;
        } else {
            Toast.makeText(this, "Please enable GPS permission", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            startActivityForResult(intent, 100); // 此为设置完成后返回到获取界面
        }

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        // 获取当前位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        curLatitude = location.getLatitude();
        curLongitude = location.getLongitude();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(curLatitude, curLongitude, 1);
            if (addresses == null || addresses.size() == 0) {
                return;
            }
            if (!TextUtils.isEmpty(DataManager.curPosition)) {
                return;
            }
            // 得到第一个经纬度位置解析信息
            Address address = addresses.get(0);
            DataManager.curLatLog = address.getLatitude() + "," + address.getLongitude();
            if (TextUtils.isEmpty(DataManager.newLatLog)) {
                DataManager.newLatLog = DataManager.curLatLog;
            }
            if (address != null) {
                StringBuilder sb = new StringBuilder();
                if (!TextUtils.isEmpty(address.getCountryName())) {
                    sb.append(address.getCountryName());
                }
                if (!TextUtils.isEmpty(address.getAdminArea())) {
                    sb.append(", " + address.getAdminArea());
                }
                if (!TextUtils.isEmpty(address.getLocality())) {
                    sb.append(", " + address.getLocality());
                }
                if (!TextUtils.isEmpty(address.getAddressLine(1))) {
                    sb.append(", " + address.getAddressLine(1));
                }
                if (!TextUtils.isEmpty(address.getAddressLine(0))) {
                    sb.append(", " + address.getAddressLine(0));
                }
                tabsPositionLine.setVisibility(View.VISIBLE);
                tabsPositionLayout.setVisibility(View.VISIBLE);
                tabsPosition.setText(sb.toString());
                DataManager.curPosition = sb.toString();
                if (TextUtils.isEmpty(DataManager.newPosition)) {
                    DataManager.newPosition = sb.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 移除位置管理器
        // 需要一直获取位置信息可以去掉这个
        locationManager.removeUpdates(this);
    }

}










