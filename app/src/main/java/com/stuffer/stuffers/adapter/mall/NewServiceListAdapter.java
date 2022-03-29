package com.stuffer.stuffers.adapter.mall;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.activity.mall.BookAppointmentActivity;
import com.stuffer.stuffers.activity.mall.SellerDetailActivity;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.models.output.NewServiceListOutputModel;
import com.stuffer.stuffers.views.MyTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Sandeep Londhe on 11-02-2019.
 *
 * @Email :  sandeeplondhe54@gmail.com
 * @Author :  https://twitter.com/mesandeeplondhe
 * @Skype :  sandeeplondhe54
 */
public class NewServiceListAdapter extends RecyclerView.Adapter<NewServiceListAdapter.MyViewHolder> {

    ArrayList<NewServiceListOutputModel.Service> serviceArrayList;
    Activity activity;

    public NewServiceListAdapter(ArrayList<NewServiceListOutputModel.Service> serviceArrayList, Activity activity) {
        this.serviceArrayList = serviceArrayList;
        this.activity = activity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView service_image;
        public MyTextView seller_address;
        public MyTextView service_name;
        public MyTextView service_price;
        public MyTextView btnBookNow;
        public MyTextView btnViewSeller;


        public MyViewHolder(View view) {
            super(view);

            service_image = (ImageView) view.findViewById(R.id.service_image);
            seller_address = (MyTextView) view.findViewById(R.id.seller_address);
            service_name = (MyTextView) view.findViewById(R.id.service_name);
            service_price = (MyTextView) view.findViewById(R.id.service_price);
            btnBookNow = (MyTextView) view.findViewById(R.id.btnBookNow);
            btnViewSeller = (MyTextView) view.findViewById(R.id.btnViewSeller);

        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_service_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NewServiceListOutputModel.Service service = serviceArrayList.get(position);

        holder.seller_address.setText(service.getSellerAddress());
        holder.service_name.setText(service.getServiceName());
        holder.service_price.setText(AppoPayApplication.CURRENCY_SYMBOL + service.getPrice());

        Picasso.with(activity)
                .load(Constants.SERVICE_IMAGE_PATH + service.getServiceImage())
                .placeholder(R.mipmap.service_placeholder)
                .into(holder.service_image);


        holder.btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(activity, BookAppointmentActivity.class);
                i.putExtra("serviceId", service.getServiceId());
                i.putExtra("sellerId", service.getSellerId());
                i.putExtra("price", service.getPrice());
                i.putExtra("serviceName", service.getServiceName());

                activity.startActivity(i);

            }
        });


        holder.btnViewSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String strSellerId = service.getSellerId();

                Intent i = new Intent(view.getContext(), SellerDetailActivity.class);
                i.putExtra("sellerId", strSellerId);
                i.putExtra("sellerName", service.getSellerName());
                i.putExtra("sellerLogo", service.getSellerLogo());
                i.putExtra("sellerCity", service.getCity());

                activity.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return serviceArrayList.size();
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
