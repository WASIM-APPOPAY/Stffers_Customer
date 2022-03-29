package com.stuffer.stuffers.adapter.mall;

/**
 * Created by Sandeep Londhe on 11-12-2018.
 *
 * @Email :  sandeeplondhe54@gmail.com
 * @Author :  https://twitter.com/mesandeeplondhe
 * @Skype :  sandeeplondhe54
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.mall.SellerDetailActivity;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.models.output.GetSellerModel;
import com.stuffer.stuffers.views.MyTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class TopSellerAdapter extends RecyclerView.Adapter<TopSellerAdapter.SimpleViewHolder> {

    private Context context;
    private List<GetSellerModel.Seller> elements;

    public TopSellerAdapter(Context context, List<GetSellerModel.Seller> elements) {
        this.context = context;
        this.elements = elements;
        // Fill dummy list


    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        //      public final Button button;

        MyTextView sellerName;
        ImageView imageView;

        MyTextView sellerCity;

        public SimpleViewHolder(View view) {
            super(view);
//            button = (Button) view.findViewById(R.id.button);
            sellerName = (MyTextView) view.findViewById(R.id.sellerName);
            imageView = (CircleImageView) view.findViewById(R.id.sellerLogo);

            sellerCity = (MyTextView) view.findViewById(R.id.sellerCity);


        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.top_seller_item_layout, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        // holder.button.setText(elements.get(position));

        final GetSellerModel.Seller jobModel = elements.get(position);

        holder.sellerName.setText(jobModel.getSellerName());

        holder.sellerCity.setText(jobModel.getCity());

        Picasso.with(context)
                .load(Constants.SELLER_IMAGE_PATH + jobModel.getSellerLogo())
                .placeholder(R.drawable.profile)
                .into(holder.imageView);

        try {

//            int newImage = Integer.valueOf(Constant.Media_URL + jobModel.getJobimage());
            //         holder.imageView.setImageResource(newImage);


//            Glide.with(context)
//                    .load(Constant.Media_URL + jobModel.getJobimage())
//                    .placeholder(R.drawable.jobplaceholder)
//                    .into(holder.imageView);


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String strSellerId = jobModel.getSellerId();

                    Intent i = new Intent(view.getContext(), SellerDetailActivity.class);
                    i.putExtra("sellerId", strSellerId);
                    i.putExtra("sellerName", jobModel.getSellerName());
                    i.putExtra("sellerLogo", jobModel.getSellerLogo());
                    i.putExtra("sellerCity", jobModel.getCity());

                    view.getContext().startActivity(i);


                }
            });


        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.elements.size();
    }


}

