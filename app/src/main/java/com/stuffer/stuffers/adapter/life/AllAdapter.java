package com.stuffer.stuffers.adapter.life;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.restaurant.RestaurantWebActivity;
import com.stuffer.stuffers.activity.shop_mall.ItemDetailsActivity;
import com.stuffer.stuffers.adapter.address.BusinessAdapter;
import com.stuffer.stuffers.fragments.landing.LifeFragment;
import com.stuffer.stuffers.models.all_restaurant.RestaurentModels;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.List;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.AllHolder> {
    Context mCtx;
    List<RestaurentModels.Array> mItems;

    public AllAdapter(Context mCtx, List<RestaurentModels.Array> mItems) {
        this.mCtx = mCtx;
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public AllHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new AllHolder(LayoutInflater.from(mCtx).inflate(R.layout.item_all, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class AllHolder extends RecyclerView.ViewHolder {
        ImageView image;
        MyTextViewBold title, description, price;

        public AllHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, ItemDetailsActivity.class);
                    RestaurentModels.Array array = mItems.get(getAdapterPosition());
                    String s = new Gson().toJson(array);
                    intent.putExtra(AppoConstants.DATA, s);
                    mCtx.startActivity(intent);

                }
            });
        }

        public void bind() {
            Glide.with(mCtx).load(mItems.get(getAbsoluteAdapterPosition()).getImageUrl()).autoClone().into(image);
            title.setText(mItems.get(getAbsoluteAdapterPosition()).getName());
            description.setText(mItems.get(getAbsoluteAdapterPosition()).getDescription());
            Double cost = mItems.get(getAbsoluteAdapterPosition()).getPrice();
            price.setText("$" + String.valueOf(cost));
            price.setTextColor(Color.parseColor(LifeFragment.mSelectedColor));
            title.setTextColor(Color.parseColor("#4169E1"));

        }
    }
}
