package com.stuffer.stuffers.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.recyclerview.OtherProductsAdapter;
import com.stuffer.stuffers.commonChat.chatUtils.GlideUtils;
import com.stuffer.stuffers.utils.DataManager;
import com.stuffer.stuffers.utils.MerchantInfoBean;
import com.stuffer.stuffers.utils.SellerHelper;
import com.stuffer.stuffers.widget.ProductCartDialog;


import java.util.ArrayList;
import java.util.List;

public class OtherProductActivity extends BaseBusinessActivity {

    private TextView otherCartNums;
    private ImageView pictureAvator;
    private TextView pictureStatus;
    private TextView tvFindCart;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private List<MerchantInfoBean.CategoryBean> categorys;
    private RecyclerView otherProductRv;
    private ProductCartDialog productCartDialog;
    private OtherProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_products);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        categorys = DataManager.merchantInfoBean.categories;
        if (categorys != null && categorys.size() != 0) {
            otherProductRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            adapter = new OtherProductsAdapter(this, categorys, otherCartNums,tvFindCart,  null);
            otherProductRv.setAdapter(adapter);
            fillCartNums();
        }

        if (!TextUtils.isEmpty(DataManager.getMerchantInfoBean().avatar)) {
            RoundedCorners roundedCorners = new RoundedCorners(15);
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
            GlideUtils.with(this).load(DataManager.getMerchantInfoBean().avatar).apply(options).placeholder(R.drawable.avator_profile).error(R.drawable.avator_profile).circleCrop().into(pictureAvator);
        }
    }

    private void fillCartNums() {
        if (categorys != null) {
            int count = 0;
            for (MerchantInfoBean.CategoryBean bean : categorys) {
                count += bean.count;
            }
            if (count > 0) {
                otherCartNums.setVisibility(View.VISIBLE);
            } else {
                otherCartNums.setVisibility(View.INVISIBLE);
            }
            otherCartNums.setText(count + "");
        }
    }

    private void initView() {
        otherProductRv = findViewById(R.id.other_product_rv);
        otherCartNums = findViewById(R.id.other_product_cart_nums);
        pictureAvator = findViewById(R.id.picture_catalogue_avator);
        tvFindCart = findViewById(R.id.other_product_find_cart);
        pictureStatus = findViewById(R.id.picture_catalogue_current_status);
        findViewById(R.id.other_product_back).setOnClickListener(view -> finish());

        findViewById(R.id.other_product_cart).setOnClickListener(view -> {
            productCartDialog = new ProductCartDialog(OtherProductActivity.this, R.style.ActionSheetDialogStyle);
            productCartDialog.setData(categorys);
        });

        tvFindCart.setOnClickListener(view -> {
            productCartDialog = new ProductCartDialog(OtherProductActivity.this, R.style.ActionSheetDialogStyle);
            productCartDialog.setData(categorys);
        });
        if (DataManager.getMerchantInfoBean().schedule != null && DataManager.getMerchantInfoBean().schedule.timeInfoList != null) {
            SellerHelper.getInstance().showScheduleStatus(DataManager.getMerchantInfoBean().schedule.timeInfoList, pictureStatus);
        }
    }


}