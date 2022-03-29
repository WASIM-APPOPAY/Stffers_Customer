package com.stuffer.stuffers.activity.shop_mall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.ShopItemListener;
import com.stuffer.stuffers.models.shop_model.ShopItem;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.List;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ShopItemHolder> {
    private final ShopItemListener mListener;
    private List<ShopItem.Datum.Child> mListItems;
    private Context mCtx;

    public ShopItemAdapter(Context context, List<ShopItem.Datum.Child> list, ShopItemListener listener) {
        this.mCtx = context;
        this.mListItems = list;
        this.mListener=listener;
    }

    @NonNull
    @Override
    public ShopItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.row_category_items, parent, false);
        return new ShopItemHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopItemHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public class ShopItemHolder extends RecyclerView.ViewHolder {
        ImageView ivShopItems;
        MyTextViewBold tvItemTitle;

        public ShopItemHolder(@NonNull View itemView) {
            super(itemView);
            ivShopItems = itemView.findViewById(R.id.ivShopItems);
            tvItemTitle = itemView.findViewById(R.id.tvItemTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onShopItemClick(mListItems.get(getAdapterPosition()).getId());
                }
            });
        }

        public void bind() {
            tvItemTitle.setText(mListItems.get(getAdapterPosition()).name);
        }
    }
}
