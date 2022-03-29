package com.stuffer.stuffers.activity.shop_mall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.ShopItemListener;
import com.stuffer.stuffers.models.shop_model.ItemDetails;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;
import com.google.gson.JsonObject;

import java.util.List;

public class ShopItemDetailAdapter extends RecyclerView.Adapter<ShopItemDetailAdapter.ShopItemDetailHolder> {
    private final List<ItemDetails.Data.Items> mList;
    private final ShopItemListener mListener;
    private Context mCtx;


    public ShopItemDetailAdapter(Context context, List<ItemDetails.Data.Items> items, ShopItemListener listener) {
        this.mCtx = context;
        this.mList = items;
        this.mListener=listener;

    }

    @NonNull
    @Override
    public ShopItemDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.row_shop_item_details, parent, false);
        return new ShopItemDetailHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopItemDetailHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ShopItemDetailHolder extends RecyclerView.ViewHolder {
        ImageView ivItem,ivQr;
        MyTextViewBold tvItemName, tvShopName;
        MyTextView tvDisPrice, tvActualPrice;


        public ShopItemDetailHolder(@NonNull View itemView) {
            super(itemView);
            ivItem = itemView.findViewById(R.id.ivItem);
            ivQr = itemView.findViewById(R.id.ivQr);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvDisPrice = itemView.findViewById(R.id.tvDisPrice);
            tvActualPrice = itemView.findViewById(R.id.tvActualPrice);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JsonObject param = new JsonObject();
                    param.addProperty(AppoConstants.PRODUCT_IMAGE,mList.get(getAdapterPosition()).getMainImage().getMediumUrl());
                    param.addProperty(AppoConstants.PRODUCT_NAME,mList.get(getAdapterPosition()).getName());
                    String urlQr ="https://chart.googleapis.com/chart?chs=75x75&cht=qr&chl="+"https://cerca24.com/"+mList.get(getAdapterPosition()).getAlias()+"&chld=L|1&choe=UTF-8";
                    param.addProperty(AppoConstants.PRODUCT_QR,urlQr);
                    param.addProperty(AppoConstants.PRODUCT_SHOP_NAME,mList.get(getAdapterPosition()).getShop().getBusinessInfo().getName());
                    param.addProperty(AppoConstants.CURRENCYSYMBOL,mList.get(getAdapterPosition()).getCurrency());
                    param.addProperty(AppoConstants.DISCOUNT_PRICE,String.valueOf(mList.get(getAdapterPosition()).getSalePrice()));
                    param.addProperty(AppoConstants.ACTUAL_PRICE,String.valueOf(mList.get(getAdapterPosition()).getPrice()));
                    mListener.onShopItemClick(param.toString());


                }
            });
        }

        public void bind() {
            Glide.with(mCtx).load(mList.get(getAdapterPosition()).getMainImage().getMediumUrl()).into(ivItem);
            tvItemName.setText(mList.get(getAdapterPosition()).getName());
            tvShopName.setText(mList.get(getAdapterPosition()).getShop().getBusinessInfo().getName());
            tvDisPrice.setText(mList.get(getAdapterPosition()).getCurrency() + " " + mList.get(getAdapterPosition()).getSalePrice());
            tvActualPrice.setText(mList.get(getAdapterPosition()).getCurrency() + " " + mList.get(getAdapterPosition()).getPrice());
//             {{env.siteUrl}}products/{{product.alias}}
             String url ="https://chart.googleapis.com/chart?chs=75x75&cht=qr&chl="+"https://cerca24.com/"+mList.get(getAdapterPosition()).getAlias()+"&chld=L|1&choe=UTF-8";
            Glide.with(mCtx).load(url).into(ivQr);
        }
    }
}
