package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.restaurant.RestaurantItemActivity;
import com.stuffer.stuffers.activity.restaurant.RestaurantWebActivity;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.models.restaurant.Result;
import com.stuffer.stuffers.views.MyTextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ListRestaurent extends RecyclerView.Adapter<ListRestaurent.ListRestaurentHolder> {
    List<Result> mListContent;
    Context mCtx;
    RecyclerViewRowItemCLickListener mListener;

    public ListRestaurent(List<Result> mListContent, Context mCtx) {
        this.mListContent = mListContent;
        this.mCtx = mCtx;
//        this.mListener= (RecyclerViewRowItemCLickListener) mCtx;
    }

    @NonNull
    @Override
    public ListRestaurentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.row_all_restaurant, parent, false);
        return new ListRestaurentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRestaurentHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mListContent.size();
    }

    public class ListRestaurentHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        MyTextView tvNameRest;
        MyTextView tvEmailRest;
        MyTextView tvContactRest;

        public ListRestaurentHolder(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvNameRest = itemView.findViewById(R.id.tvNameRest);
            tvEmailRest = itemView.findViewById(R.id.tvEmailRest);
            tvContactRest = itemView.findViewById(R.id.tvContactRest);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent=new Intent(mCtx, RestaurantWebActivity.class);

                    String merchantId = String.valueOf(mListContent.get(getAdapterPosition()).getId());
                    //String userid= String.valueOf(mListContent.get(getAbsoluteAdapterPosition()));
                  //  String name=mListContent.get(getAbsoluteAdapterPosition()).getBusinessName();
                    mIntent.putExtra("merchantid",merchantId);
                   // mIntent.putExtra("name",name);
                    mCtx.startActivity(mIntent);

                }
            });
        }

        public void bind() {

            tvNameRest.setText(mListContent.get(getAdapterPosition()).getBusinessName());

            if (StringUtils.isEmpty(mListContent.get(getAdapterPosition()).getEmail())) {
                tvEmailRest.setText("NA");

            } else {
                tvEmailRest.setText(mListContent.get(getAdapterPosition()).getEmail().toString());
            }
            tvContactRest.setText(mListContent.get(getAdapterPosition()).getAreaCode() + " " + mListContent.get(getAdapterPosition()).getBusiPhoneNumber());
            Glide.with(mCtx).load(mListContent.get(getAdapterPosition()).getAddressPic()).circleCrop().into(ivPic);

        }
    }
}
