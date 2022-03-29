package com.stuffer.stuffers.adapter.mall;

/**
 * Created by Sandeep Londhe on 29-01-2019.
 *
 * @Email :  sandeeplondhe54@gmail.com
 * @Author :  https://twitter.com/mesandeeplondhe
 * @Skype :  sandeeplondhe54
 */

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.mall.SellerDetailActivity;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.models.output.GetSellerModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SellerListViewAdapter extends RecyclerView.Adapter<SellerListViewAdapter.MyViewHolder> {


    private List<GetSellerModel.Seller> sellerList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView seller_image;
        public TextView seller_name;
        public TextView address;


        public MyViewHolder(View view) {
            super(view);

            seller_name = (TextView) view.findViewById(R.id.seller_name);
            address = (TextView) view.findViewById(R.id.address);
            seller_image = (ImageView) view.findViewById(R.id.seller_image);


        }
    }


    public SellerListViewAdapter(List<GetSellerModel.Seller> sellerList, Activity activity) {
        this.sellerList = sellerList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sellers_list_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GetSellerModel.Seller seller = sellerList.get(position);
//        holder.txtCatName.setText(deliveryResponse.getServiceName());

        holder.seller_name.setText(seller.getSellerName());
        holder.address.setText(seller.getSellerAddress());

        Picasso.with(activity)
                .load(Constants.SELLER_IMAGE_PATH + seller.getSellerLogo())
                .placeholder(R.drawable.placeholder)
                .into(holder.seller_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String strSellerId = seller.getSellerId();

                Intent i = new Intent(view.getContext(), SellerDetailActivity.class);
                i.putExtra("sellerId", strSellerId);
                i.putExtra("sellerName", seller.getSellerName());
                i.putExtra("sellerLogo", seller.getSellerLogo());
                i.putExtra("sellerCity", seller.getCity());

                view.getContext().startActivity(i);


            }
        });


    }

    @Override
    public int getItemCount() {
        return sellerList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}


