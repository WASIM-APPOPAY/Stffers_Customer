package com.stuffer.stuffers.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.recyclerview.CartProductsAdapter;
import com.stuffer.stuffers.commonChat.chatUtils.ToastUtil;
import com.stuffer.stuffers.utils.MerchantInfoBean;

import java.util.ArrayList;
import java.util.List;


public class ProductCartDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private RecyclerView cartProductRv;
    private TextView productCartNums;
    private TextView productTotalPrice;
    private int totalPrice;
    private TextView productPay;

    public ProductCartDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ProductCartDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        initDialog();
    }

    /**
     * 初始化Dialog
     */
    public void initDialog() {
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setGravity(Gravity.RELATIVE_LAYOUT_DIRECTION | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        setCanceledOnTouchOutside(false);
        show();
    }

    /**
     * 隐藏Dialog
     */
    private void hideDialog() {
        cancel();
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_product_cart);

        initView();
        initListener();
    }

    private void initView() {
        findViewById(R.id.other_product_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        cartProductRv = findViewById(R.id.cart_product_rv);
        productCartNums = findViewById(R.id.product_cart_nums);
        productTotalPrice = findViewById(R.id.product_cart_total_price);
        productPay = findViewById(R.id.product_cart_pay);
        cartProductRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

    }

    public void setData(List<MerchantInfoBean.CategoryBean> categorys) {
        if (categorys != null && categorys.size() != 0) {
            List<MerchantInfoBean.CategoryBean> newList = filterData(categorys);
            productCartNums.setText(newList.size() + " Items");
            productTotalPrice.setText(totalPrice + " $");
            CartProductsAdapter adapter = new CartProductsAdapter(mContext, filterData(categorys), null);
            cartProductRv.setAdapter(adapter);
        }
    }

    public List<MerchantInfoBean.CategoryBean> filterData(List<MerchantInfoBean.CategoryBean> categorys) {
        List<MerchantInfoBean.CategoryBean> result = new ArrayList<>();
        for (int i = 0; i < categorys.size(); i++) {
            if (categorys.get(i).count == 0) {
                continue;
            }
            totalPrice += categorys.get(i).count * categorys.get(i).price;
            result.add(categorys.get(i));
        }
        return result;
    }

    private void initListener() {
        productPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showTextShort("click product pay");
            }
        });
    }


    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

    @Override
    public void onClick(View v) {

    }

    OnItemClickListener mListener;

    public interface OnItemClickListener {
        /**
         * 点击条目
         */
        void onItemClick(int type);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}
