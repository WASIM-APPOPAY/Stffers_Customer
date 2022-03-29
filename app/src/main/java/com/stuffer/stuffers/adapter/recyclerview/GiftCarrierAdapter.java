package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.models.lunex_giftcard.GiftProductList;
import com.stuffer.stuffers.views.MyTextView;

import java.util.List;

public class GiftCarrierAdapter extends RecyclerView.Adapter<GiftCarrierAdapter.GiftCarrierHolder> {
    private static final String TAG = "GiftCarrierAdapter";


    Context mCtx;
    List<GiftProductList.Product> mItems;
    RecyclerViewRowItemCLickListener mItemCLickListener;
    private static SparseBooleanArray sSelectedItems;
    private static final int MULTIPLE = 0;
    private static final int SINGLE = 1;
    private static int sModo = 0;
    private static int sPosition;
    public boolean b=true;

    public GiftCarrierAdapter(Context context, List<GiftProductList.Product> mListGift,int modo) {
        this.mCtx = context;
        this.mItems = mListGift;
        this.mItemCLickListener = (RecyclerViewRowItemCLickListener) context;
        sSelectedItems = new SparseBooleanArray();
        sModo = modo;
    }

    @NonNull
    @Override
    public GiftCarrierHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.row_gift_carrier, parent, false);
        return new GiftCarrierHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftCarrierHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class GiftCarrierHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        ImageView ivGiftIcon;
        MyTextView tvCarrierName;
        LinearLayout mBackground;

        public GiftCarrierHolder(View itemView) {
            super(itemView);
            ivGiftIcon = itemView.findViewById(R.id.ivGiftIcon);
            tvCarrierName = itemView.findViewById(R.id.tvCarrierName);
            mBackground = (LinearLayout) itemView.findViewById(R.id.vertical_list_item_background);
            itemView.setOnClickListener(this);

        }

        public void bind() {
            String path = "https://appopayimages.s3.us-east-2.amazonaws.com/lunex/" + mItems.get(getAdapterPosition()).getProductCode() + ".png";
            //Log.e(TAG, "bind: " + path);
            Glide.with(mCtx).load(path).into(ivGiftIcon);
            tvCarrierName.setText(mItems.get(getAdapterPosition()).getCarrier());
            mBackground.setSelected(sSelectedItems.get(getAdapterPosition(), false));

        }

        @Override
        public void onClick(View view) {
           setClick(getAdapterPosition(),mBackground);
        }

        public void setPositon() {
          setClick(0,mBackground);
        }

    }

    private void setClick(int adapterPosition, LinearLayout mBackground) {
        if (sSelectedItems.get(adapterPosition, false)) {
            sSelectedItems.delete(adapterPosition);
            mBackground.setSelected(false);
            //mLabel.setTextColor(ContextCompat.getColor(sContext, android.R.color.black));
        } else {
            switch (sModo) {
                case SINGLE:
                    sSelectedItems.put(sPosition, false);
                    break;
                case MULTIPLE:
                default:
                    break;
            }
            //mLabel.setTextColor(ContextCompat.getColor(sContext, R.color.colorAccent));
            sSelectedItems.put(adapterPosition, true);
            mBackground.setSelected(true);
            mItemCLickListener.onRowItemClick(adapterPosition);
        }
    }

    public void selected(int position) {
        switch (sModo) {
            case SINGLE:
                sPosition = position;
                notifyDataSetChanged();
                break;
            case MULTIPLE:
            default:
                break;
        }
    }




}
