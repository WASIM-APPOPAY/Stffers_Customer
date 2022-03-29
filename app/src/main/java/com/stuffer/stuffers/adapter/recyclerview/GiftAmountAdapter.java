package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.GiftRequestListener;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemClickListener2;
import com.stuffer.stuffers.models.lunex_giftcard.GiftProductList;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.List;

public class GiftAmountAdapter extends RecyclerView.Adapter<GiftAmountAdapter.GiftAmountHolder> {
    Context mCtx;
    List<GiftProductList.Amount> mListAmout;
    RecyclerViewRowItemClickListener2 mClickListener;
    GiftRequestListener mGiftRequestListener;
    private static SparseBooleanArray sSelectedItems;
    private static final int MULTIPLE = 0;
    private static final int SINGLE = 1;
    private static int sModo = 0;
    private static int sPosition;

    public GiftAmountAdapter(Context mCtx, List<GiftProductList.Amount> mListAmout, int modo) {
        this.mCtx = mCtx;
        this.mListAmout = mListAmout;
        this.mClickListener = (RecyclerViewRowItemClickListener2) mCtx;
        this.mGiftRequestListener= (GiftRequestListener) mCtx;
        sSelectedItems = new SparseBooleanArray();
        sModo = modo;
    }

    @NonNull
    @Override
    public GiftAmountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.row_gift_amout_item, parent, false);
        return new GiftAmountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftAmountHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mListAmout.size();
    }

    public class GiftAmountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyTextViewBold tvAmountGift;
        LinearLayout mBackground;
        ImageView ivBuy;

        public GiftAmountHolder(@NonNull View itemView) {
            super(itemView);
            tvAmountGift = itemView.findViewById(R.id.tvAmountGift);
            mBackground = (LinearLayout) itemView.findViewById(R.id.vertical_list_item_background);
            ivBuy = (ImageView)itemView.findViewById(R.id.ivBuy);
            itemView.setOnClickListener(this);
            ivBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGiftRequestListener.onGiftRequest(getAdapterPosition());
                }
            });
        }

        public void bind() {
            tvAmountGift.setText("" + mListAmout.get(getAdapterPosition()).getAmt());
            mBackground.setSelected(sSelectedItems.get(getAdapterPosition(), false));
        }

        @Override
        public void onClick(View view) {
            if (sSelectedItems.get(getAdapterPosition(), false)) {
                sSelectedItems.delete(getAdapterPosition());
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
                sSelectedItems.put(getAdapterPosition(), true);
                mBackground.setSelected(true);
                mClickListener.onRowItemClick2(getAdapterPosition());
            }
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

