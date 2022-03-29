package com.stuffer.stuffers.adapter.mall;

/**
 * Created by Sandeep Londhe on 25-09-2018.
 *
 * @Email :  sandeeplondhe54@gmail.com
 * @Author :  https://twitter.com/mesandeeplondhe
 * @Skype :  sandeeplondhe54
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.models.output.CategoryListModel;
import com.stuffer.stuffers.views.MyTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FrontCategoryListAdapter extends RecyclerView.Adapter<FrontCategoryListAdapter.MyViewHolder> {

    private List<CategoryListModel.Cat> deliveryResponseList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyTextView category_name;
        public ImageView category_image;

        public MyViewHolder(View view) {
            super(view);
            category_name = (MyTextView) view.findViewById(R.id.category_name);
            category_image = (ImageView) view.findViewById(R.id.category_image);

        }
    }


    public FrontCategoryListAdapter(List<CategoryListModel.Cat> deliveryResponseList, Activity activity) {
        this.deliveryResponseList = deliveryResponseList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_list_item2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryListModel.Cat deliveryResponse = deliveryResponseList.get(position);
        holder.category_name.setText(deliveryResponse.getServiceName());

        Picasso.with(activity)
                .load(Constants.UPLOAD_IMAGE_FOLDER + deliveryResponse.getServiceImage())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.category_image);

    }

    @Override
    public int getItemCount() {
        return deliveryResponseList.size();
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

