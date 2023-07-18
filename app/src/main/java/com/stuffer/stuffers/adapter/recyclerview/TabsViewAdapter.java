package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.SubTabsActivity;
import com.stuffer.stuffers.models.shop_model.ShopModel;

import java.util.ArrayList;

public class TabsViewAdapter extends PagerAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<ShopModel> mList;

    public TabsViewAdapter(Context context, ArrayList<ShopModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        TabsViewHolder holder = new TabsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_tabviews, container, false));
        container.addView(holder.itemView);
        if (position == 0) {
            holder.iv1.setImageResource(mList.get(0).getResIds());
            holder.tv1.setText(mList.get(0).getmTitle());
            holder.iv1.setTag(0);
            holder.iv1.setOnClickListener(this);

            holder.iv2.setImageResource(mList.get(1).getResIds());
            holder.tv2.setText(mList.get(1).getmTitle());
            holder.iv2.setTag(1);
            holder.iv2.setOnClickListener(this);

            holder.iv3.setImageResource(mList.get(2).getResIds());
            holder.tv3.setText(mList.get(2).getmTitle());
            holder.iv3.setTag(2);
            holder.iv3.setOnClickListener(this);

            holder.iv4.setImageResource(mList.get(3).getResIds());
            holder.tv4.setText(mList.get(3).getmTitle());
            holder.iv4.setTag(3);
            holder.iv4.setOnClickListener(this);

            holder.iv11.setImageResource(mList.get(4).getResIds());
            holder.tv11.setText(mList.get(4).getmTitle());
            holder.iv11.setTag(4);
            holder.iv11.setOnClickListener(this);

            holder.iv22.setImageResource(mList.get(5).getResIds());
            holder.tv22.setText(mList.get(5).getmTitle());
            holder.iv22.setTag(5);
            holder.iv22.setOnClickListener(this);

            holder.iv33.setImageResource(mList.get(6).getResIds());
            holder.tv33.setText(mList.get(6).getmTitle());
            holder.iv33.setTag(6);
            holder.iv33.setOnClickListener(this);

            holder.iv44.setImageResource(mList.get(7).getResIds());
            holder.tv44.setText(mList.get(7).getmTitle());
            holder.iv44.setTag(7);
            holder.iv44.setOnClickListener(this);
        } else {
            holder.iv1.setImageResource(mList.get(8).getResIds());
            holder.tv1.setText(mList.get(8).getmTitle());
            holder.iv1.setTag(8);
            holder.iv1.setOnClickListener(this);

            holder.iv2.setImageResource(mList.get(9).getResIds());
            holder.tv2.setText(mList.get(9).getmTitle());
            holder.iv2.setTag(9);
            holder.iv2.setOnClickListener(this);

            holder.iv3.setImageResource(mList.get(10).getResIds());
            holder.tv3.setText(mList.get(10).getmTitle());
            holder.iv3.setTag(10);
            holder.iv3.setOnClickListener(this);

            holder.iv4.setImageResource(mList.get(11).getResIds());
            holder.tv4.setText(mList.get(11).getmTitle());
            holder.iv4.setTag(11);
            holder.iv4.setOnClickListener(this);

            holder.iv11.setImageResource(mList.get(12).getResIds());
            holder.tv11.setText(mList.get(12).getmTitle());
            holder.iv11.setTag(12);
            holder.iv11.setOnClickListener(this);

            holder.iv22.setImageResource(mList.get(13).getResIds());
            holder.tv22.setText(mList.get(13).getmTitle());
            holder.iv22.setTag(13);
            holder.iv22.setOnClickListener(this);

            holder.iv33.setImageResource(mList.get(14).getResIds());
            holder.tv33.setText(mList.get(14).getmTitle());
            holder.iv33.setTag(14);
            holder.iv33.setOnClickListener(this);

            holder.iv44.setBackground(null);
//            holder.tv44.setText(mList.get(14).getmTitle());
        }
        return holder.itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void onClick(View v) {
        try {
            int pos = (Integer) v.getTag();
            if (mList != null && mList.size() > pos) {
                Intent intent = new Intent(context, SubTabsActivity.class);
                intent.putExtra("pos", pos);
                intent.putExtra("name", mList.get(pos).getmTitle());
                context.startActivity(intent);
            }
        } catch (Exception e) {
        }

    }

    class TabsViewHolder extends RecyclerView.ViewHolder {
        TextView tv1;
        ImageView iv1;
        TextView tv2;
        ImageView iv2;
        TextView tv3;
        ImageView iv3;
        TextView tv4;
        ImageView iv4;

        TextView tv11;
        ImageView iv11;
        TextView tv22;
        ImageView iv22;
        TextView tv33;
        ImageView iv33;
        TextView tv44;
        ImageView iv44;

        public TabsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            iv1 = itemView.findViewById(R.id.iv1);
            tv2 = itemView.findViewById(R.id.tv2);
            iv2 = itemView.findViewById(R.id.iv2);
            tv3 = itemView.findViewById(R.id.tv3);
            iv3 = itemView.findViewById(R.id.iv3);
            tv4 = itemView.findViewById(R.id.tv4);
            iv4 = itemView.findViewById(R.id.iv4);

            tv11 = itemView.findViewById(R.id.tv11);
            iv11 = itemView.findViewById(R.id.iv11);
            tv22 = itemView.findViewById(R.id.tv22);
            iv22 = itemView.findViewById(R.id.iv22);
            tv33 = itemView.findViewById(R.id.tv33);
            iv33 = itemView.findViewById(R.id.iv33);
            tv44 = itemView.findViewById(R.id.tv44);
            iv44 = itemView.findViewById(R.id.iv44);
        }
    }
}
