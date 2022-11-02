package com.stuffer.stuffers.activity.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.recyclerview.AddItemAdapter;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.chatUtils.GlideUtils;
import com.stuffer.stuffers.commonChat.chatUtils.ToastUtil;
import com.stuffer.stuffers.utils.DataManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.MerchantInfoBean;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.PageStyle;

import java.util.ArrayList;
import java.util.List;

public class ProductItemDetailActivity extends AppCompatActivity {
    private MerchantInfoBean.CategoryBean opBean;
    private ImageView productItemBack;
    private View productItemAddCart;
    private TextView productItemCartNums;
    private TextView productItemName;
    private TextView productItemPrice;
    private TextView productItemDesc;
    private ImageView productItemSellerAvator;
    private TextView productItemSellerName;
    private TextView productItemSellerDesc;
    private ProgressDialog mProgress;
    private ChatHelper helper;
    private PhotoView scaleImg;
    private ViewGroup productItemContainer;
    private ImageView productItemClose;

    private MainAPIInterface mainAPIInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_item);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //状态恢复
        if (DataManager.index != -1) {
            if (DataManager.merchantInfoBean.categories == null) {
                DataManager.merchantInfoBean.categories = new ArrayList<>();
            }
            if (DataManager.index >= DataManager.merchantInfoBean.categories.size()) {
                opBean = new MerchantInfoBean.CategoryBean();
                DataManager.merchantInfoBean.categories.add(opBean);
                DataManager.index = DataManager.merchantInfoBean.categories.size() - 1;
            } else {
                opBean = DataManager.merchantInfoBean.categories.get(DataManager.index);
            }
            fillViewPager();
            if (!TextUtils.isEmpty(opBean.itemName)) {
                productItemName.setText(opBean.itemName);
            }
            if (!TextUtils.isEmpty(opBean.price + "")) {
                productItemPrice.setText(opBean.price + "");
            }
            if (!TextUtils.isEmpty(opBean.description)) {
                productItemDesc.setText(opBean.description);
            }
            GlideUtils.with(this).load(DataManager.getMerchantInfoBean().avatar).placeholder(R.drawable.avator_profile).error(R.drawable.avator_profile).circleCrop().into(productItemSellerAvator);

            if (!TextUtils.isEmpty(DataManager.getMerchantInfoBean().businessName)) {
                productItemSellerName.setText(DataManager.getMerchantInfoBean().businessName);
            }

            if (!TextUtils.isEmpty(DataManager.getMerchantInfoBean().description)) {
                productItemSellerDesc.setText(DataManager.getMerchantInfoBean().description);
            }
            showCartNums();
        }
    }

    private void initView() {
        productItemBack = findViewById(R.id.product_item_back);
        productItemAddCart = findViewById(R.id.product_item_cart);
        productItemCartNums = findViewById(R.id.product_item_nums);
        productItemName = findViewById(R.id.add_item_name);
        productItemPrice = findViewById(R.id.add_item_price);
        productItemDesc = findViewById(R.id.add_item_desc);
        productItemSellerAvator = findViewById(R.id.product_item_avator);
        productItemSellerName = findViewById(R.id.product_item_seller_name);
        productItemSellerDesc = findViewById(R.id.product_item_seller_desc);
        scaleImg = findViewById(R.id.scale_img);
        productItemContainer = findViewById(R.id.product_item_scale_container);
        productItemClose = findViewById(R.id.product_item_close);
        helper = new ChatHelper(this);

        productItemBack.setOnClickListener(view -> ProductItemDetailActivity.this.finish());
        productItemAddCart.setOnClickListener(view -> {
            opBean.count += 1;
            ToastUtil.showTextShort("Added to Cart");
            DataManager.isNeedRefreshForList = true;
            showCartNums();
        });
        productItemClose.setOnClickListener(view -> productItemContainer.setVisibility(View.GONE));
        findViewById(R.id.product_item_avator_container).setOnClickListener(v -> {
            startActivity(new Intent(ProductItemDetailActivity.this, OtherBusinessActivity.class));
        });
        mainAPIInterface = ApiUtils.getAPIService();
        initViewPager();
    }

    private void showCartNums() {
        if (opBean.count == 0) {
            productItemCartNums.setVisibility(View.INVISIBLE);
        } else {
            productItemCartNums.setVisibility(View.VISIBLE);
        }
        productItemCartNums.setText(opBean.count + "");
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

    private BannerViewPager<String> mViewPager;
    private AddItemAdapter adapter;
    private List<String> items = new ArrayList<>();

    private void initViewPager() {
        mViewPager = findViewById(R.id.banner_view);
        mViewPager.setLifecycleRegistry(getLifecycle());
        mViewPager.setIndicatorVisibility(View.GONE);
        mViewPager.setCanLoop(false);
        mViewPager.setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
            @Override
            public void onPageClick(View clickedView, int position) {
                scaleImg.setVisibility(View.VISIBLE);
                productItemContainer.setVisibility(View.VISIBLE);
                scaleImg.setImageDrawable(null);
                Glide.with(ProductItemDetailActivity.this).asDrawable().load(items.get(position)).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        scaleImg.setImageDrawable(resource);
                    }
                });
            }
        });
        adapter = new AddItemAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager
                .setPageMargin(50)
                .setScrollDuration(800)
                .setRevealWidth(100, PageStyle.MULTI_PAGE_SCALE)
                .setPageStyle(PageStyle.MULTI_PAGE_SCALE).create(items);

    }

    private void fillViewPager() {
        items.addAll(opBean.pictureList);
        mViewPager.setCanLoop(false);
        mViewPager.create(items);
    }


}