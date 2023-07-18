package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.restaurant.RestaurantItemActivity;
import com.stuffer.stuffers.communicator.RestaurantListener;
import com.stuffer.stuffers.models.all_restaurant.RestaurantItems;
import com.stuffer.stuffers.views.MyTextView;

import java.util.List;

public class AlItemsAdapter extends RecyclerView.Adapter<AlItemsAdapter.AllItemHolder> {
    List<RestaurantItems.Result> mItemResult;
    Context mCtx;
    RestaurantListener mRestaurantListener;

    public AlItemsAdapter(List<RestaurantItems.Result> result, Context ctx) {
        this.mItemResult = result;
        this.mCtx = ctx;
        this.mRestaurantListener = (RestaurantListener) ctx;


    }

    @NonNull
    @Override
    public AllItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.row_all_restaurant_items, parent, false);
        return new AllItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllItemHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mItemResult.size();
    }

    public class AllItemHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        MyTextView tvNameItem, tvDescItem, tvRateItem, tvCountItem, tvIncrease,tvDecrease;

        public AllItemHolder(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvNameItem = itemView.findViewById(R.id.tvNameItem);
            tvDescItem = itemView.findViewById(R.id.tvDescItem);
            tvRateItem = itemView.findViewById(R.id.tvRateItem);
            tvCountItem = itemView.findViewById(R.id.tvCountItem);
            tvIncrease = itemView.findViewById(R.id.tvIncrease);
            tvDecrease = itemView.findViewById(R.id.tvDecrease);
            tvIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRestaurantListener.onIncreaseItemRequest(getAdapterPosition(), mItemResult.get(getAdapterPosition()),mItemResult.get(getAdapterPosition()).getId());
                }
            });

            tvDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRestaurantListener.onDeCreaseRequest(getAdapterPosition(),mItemResult.get(getAdapterPosition()).getId());
                }
            });

        }

        public void bind() {
            Glide.with(mCtx).load(mItemResult.get(getAdapterPosition()).getImageUrl()).into(ivPic);
            tvNameItem.setText(mItemResult.get(getAdapterPosition()).getShelvesName());
            tvDescItem.setText(mItemResult.get(getAdapterPosition()).getDescription());
            tvRateItem.setText("$" + mItemResult.get(getAdapterPosition()).getPrice());
            tvCountItem.setText("" + mItemResult.get(getAdapterPosition()).getCount());


        }
    }
}
