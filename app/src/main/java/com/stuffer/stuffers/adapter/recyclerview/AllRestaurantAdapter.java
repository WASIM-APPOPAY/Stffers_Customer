package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.restaurant.RestaurantItemActivity;
import com.stuffer.stuffers.activity.restaurant.RestaurantListActivity;
import com.stuffer.stuffers.models.all_restaurant.Content;
import com.stuffer.stuffers.views.MyTextView;

import java.util.List;

public class AllRestaurantAdapter extends RecyclerView.Adapter<AllRestaurantAdapter.AllRestaurantHolder> {
    private static final String TAG = "AllRestaurantAdapter";
    List<Content> mListContent;
    Context mCtx;

    public AllRestaurantAdapter(List<Content> content, Context ctx) {
        this.mCtx = ctx;
        this.mListContent = content;

    }

    @NonNull
    @Override
    public AllRestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.row_all_restaurant, parent, false);
        return new AllRestaurantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllRestaurantHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mListContent.size();
    }

    public class AllRestaurantHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        MyTextView tvNameRest;
        MyTextView tvEmailRest;
        MyTextView tvContactRest;


        public AllRestaurantHolder(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvNameRest = itemView.findViewById(R.id.tvNameRest);
            tvEmailRest = itemView.findViewById(R.id.tvEmailRest);
            tvContactRest = itemView.findViewById(R.id.tvContactRest);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent=new Intent(mCtx, RestaurantItemActivity.class);
                    String userid= String.valueOf(mListContent.get(getAbsoluteAdapterPosition()).getUserId());
                    String name=mListContent.get(getAbsoluteAdapterPosition()).getBusinessName();
                   // Log.e(TAG, "onClick: "+ );
                    mIntent.putExtra("userid",userid);
                    mIntent.putExtra("name",name);
                    mCtx.startActivity(mIntent);

                }
            });
        }

        public void bind() {
            tvNameRest.setText(mListContent.get(getAdapterPosition()).getBusinessName());
            tvEmailRest.setText(mListContent.get(getAdapterPosition()).getEmail());
            tvContactRest.setText(mListContent.get(getAdapterPosition()).getAreaCode() + " " + mListContent.get(getAdapterPosition()).getBusiPhoneNumber());
            Glide.with(mCtx).load(mListContent.get(getAdapterPosition()).getAddressPic()).into(ivPic);
        }
    }
}
