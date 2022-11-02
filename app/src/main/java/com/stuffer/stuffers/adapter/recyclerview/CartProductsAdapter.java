package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.ShopListener;
import com.stuffer.stuffers.utils.MerchantInfoBean;

import java.util.List;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.OtherProductHolder> {
    List<MerchantInfoBean.CategoryBean> otherProductsList;
    Context mCtx;

    public CartProductsAdapter(Context ctx, List<MerchantInfoBean.CategoryBean> list, ShopListener listen) {
        this.mCtx = ctx;
        this.otherProductsList = list;
    }

    @NonNull
    @Override
    public OtherProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_other_product, parent, false);
        return new OtherProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherProductHolder holder, int position) {
        holder.bindItems(otherProductsList.get(position));
    }

    @Override
    public int getItemCount() {
        return otherProductsList.size();
    }

    public class OtherProductHolder extends RecyclerView.ViewHolder {
        TextView tvItemTitle;
        TextView tvItemDesc;
        TextView tvItemPrice;
        ImageView ivProductImg;
        ImageView ivProductMinus;
        ImageView ivProductAdd;
        TextView tvProductNums;

        public OtherProductHolder(@NonNull View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.other_product_title);
            tvItemDesc = itemView.findViewById(R.id.other_product_desc);
            tvItemPrice = itemView.findViewById(R.id.other_product_price);
            ivProductImg = itemView.findViewById(R.id.other_product_img);
            ivProductMinus = itemView.findViewById(R.id.other_product_minus);
            ivProductAdd = itemView.findViewById(R.id.other_product_plus);
            tvProductNums = itemView.findViewById(R.id.other_product_nums);
        }

        public void bindItems(MerchantInfoBean.CategoryBean bean) {
            if (bean.pictureList != null && bean.pictureList.size() > 0) {
                RoundedCorners roundedCorners = new RoundedCorners(15);
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
                Glide.with(mCtx).load(bean.pictureList.get(0)).apply(options).into(ivProductImg);
            }
            tvItemTitle.setText(bean.itemName);
            tvItemDesc.setText(TextUtils.isEmpty(bean.description) ? "This item has no description" : bean.description);
            tvItemPrice.setText(bean.price + "$");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            if (bean.count == 0) {
                ivProductMinus.setVisibility(View.INVISIBLE);
                tvProductNums.setVisibility(View.INVISIBLE);
            } else {
                ivProductMinus.setVisibility(View.VISIBLE);
                tvProductNums.setVisibility(View.VISIBLE);
                tvProductNums.setText(bean.count + "");
            }

            ivProductAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bean.count += 1;
                    ivProductMinus.setVisibility(View.VISIBLE);
                    tvProductNums.setVisibility(View.VISIBLE);
                    tvProductNums.setText(String.valueOf(bean.count));
                }
            });

            ivProductMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bean.count -= 1;
                    tvProductNums.setText(String.valueOf(bean.count));
                    if (bean.count == 0) {
                        ivProductMinus.setVisibility(View.INVISIBLE);
                        tvProductNums.setVisibility(View.INVISIBLE);
                        notifyDataSetChanged();
                    }
                }
            });

        }
    }
}
