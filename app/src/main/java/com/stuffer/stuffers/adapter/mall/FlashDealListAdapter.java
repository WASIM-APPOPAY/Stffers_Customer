package com.stuffer.stuffers.adapter.mall;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.mall.ProductsActivity;
import com.stuffer.stuffers.api.Constants;
import com.stuffer.stuffers.models.output.GetAllFlashDealsModel;
import com.stuffer.stuffers.views.MyTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class FlashDealListAdapter extends BaseAdapter {

    ArrayList<GetAllFlashDealsModel.Deal> bean;
    AppCompatActivity main;


    public FlashDealListAdapter(AppCompatActivity activity, ArrayList<GetAllFlashDealsModel.Deal> bean) {
        this.main = activity;
        this.bean = bean;
    }

    @Override
    public int getCount() {
        return bean.size();
    }

    @Override
    public Object getItem(int position) {
        return bean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) main.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.flash_deal_list_item, null);


            viewHolder = new ViewHolder();

            viewHolder.flash_deal_image = (CircleImageView) convertView.findViewById(R.id.flash_deal_image);
            viewHolder.flash_deal_title = (MyTextView) convertView.findViewById(R.id.flash_deal_title);


            convertView.setTag(viewHolder);


        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }


        final GetAllFlashDealsModel.Deal bean = (GetAllFlashDealsModel.Deal) getItem(position);

//        viewHolder.image.setImageResource(bean.getImage());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(main, ProductsActivity.class);

                i.putExtra("subcat_id", "null");
                i.putExtra("subcat_name", bean.getDealTitle());
                i.putExtra("products", bean.getListOfItemsById());
                main.startActivity(i);


            }
        });


        Picasso.with(main)
                .load(Constants.PRODUCT_IMAGE_PATH + bean.getDealImage())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.flash_deal_image);


        viewHolder.flash_deal_title.setText(bean.getDealTitle());


        return convertView;


    }

    private class ViewHolder {
        CircleImageView flash_deal_image;
        MyTextView flash_deal_title;


    }


}

