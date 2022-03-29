package com.stuffer.stuffers.activity.shop_mall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.ShopListener;
import com.stuffer.stuffers.models.shop_model.ShopModel;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

import static com.stuffer.stuffers.R.color.pharmacy_color;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopHolder> {
    ArrayList<ShopModel> mListShop;
    Context mCtx;
    ShopListener mShopListener;

    public ShopAdapter(Context ctx, ArrayList<ShopModel> list, ShopListener listen) {
        this.mCtx = ctx;
        this.mListShop = list;
        mShopListener = (ShopListener) listen;
    }

    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.row_shop_items, parent, false);
        return new ShopHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHolder holder, int position) {
        holder.bindItems();
    }

    @Override
    public int getItemCount() {
        return mListShop.size();
    }

    public class ShopHolder extends RecyclerView.ViewHolder {
        ImageView ivShopItems;
        MyTextView tvItemTitle;

        public ShopHolder(@NonNull View itemView) {
            super(itemView);
            ivShopItems = itemView.findViewById(R.id.ivShopItems);
            tvItemTitle = itemView.findViewById(R.id.tvItemTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mShopListener.onShopItemClick(getAdapterPosition(), mListShop.get(getAdapterPosition()).getmTitle());
                }
            });
        }

        public void bindItems() {

            tvItemTitle.setText(mListShop.get(getAdapterPosition()).getmTitle());
            if (mListShop.get(getAdapterPosition()).getmTitle().equals("Restaurant")) {
                ivShopItems.setColorFilter(ContextCompat.getColor(mCtx, R.color.green));
                Glide.with(mCtx).load(mListShop.get(getAdapterPosition()).getResIds()).into(ivShopItems);
            } else if (mListShop.get(getAdapterPosition()).getmTitle().equals("Pharmacy")) {
                ivShopItems.setColorFilter(ContextCompat.getColor(mCtx, R.color.pharmacy_color));
                Glide.with(mCtx).load(mListShop.get(getAdapterPosition()).getResIds()).into(ivShopItems);
                //ivShopItems.setImageResource(mListShop.get(getAdapterPosition()).getResIds());
            } else {
                //Glide.with(mCtx).load(mListShop.get(getAdapterPosition()).getResIds()).into(ivShopItems);
                ivShopItems.setImageResource(mListShop.get(getAdapterPosition()).getResIds());
            }
        }
    }
}
