package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.giftcard.OwnGiftCardActivity;
import com.stuffer.stuffers.activity.lunex_card.LunexItemsActivity;
import com.stuffer.stuffers.activity.wallet.GiftCardActivity;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.models.lunex_giftcard.GiftProductList;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyTextView;

import java.util.List;

public class AllGiftProduct extends RecyclerView.Adapter<AllGiftProduct.AllGiftHolder> {
    public boolean status;
    List<GiftProductList.Product> mListGift;
    int[] mIcons;
    Context mCtx;
    RecyclerViewRowItemCLickListener mClickListener;

    public AllGiftProduct(List<GiftProductList.Product> mListGift, int[] mIcons, Context mCtx, FragmentActivity listen) {
        this.mListGift = mListGift;
        this.mIcons = mIcons;
        this.mCtx = mCtx;
        this.mClickListener = (RecyclerViewRowItemCLickListener) listen;
    }

    public AllGiftProduct( int[] mIcons, Context mCtx, FragmentActivity listen,boolean status) {
        this.status=status;

        this.mIcons = mIcons;
        this.mCtx = mCtx;
        this.mClickListener = (RecyclerViewRowItemCLickListener) listen;
    }



    @NonNull
    @Override
    public AllGiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.row_all_gift_card_lis_item, parent, false);
        return new AllGiftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllGiftHolder holder, int position) {
        holder.bindCards();
    }

    @Override
    public int getItemCount() {
        //return mListGift.size() - 1;
        //return mListGift.size();
        //return 8;
        /*if (status){
            return 8;
        }else {
            return mIcons.length;
        }*/
        return mIcons.length;

    }

    public class AllGiftHolder extends RecyclerView.ViewHolder {
        ImageView ivCards;
        MyTextView tvCardName;

        public AllGiftHolder(@NonNull View itemView) {
            super(itemView);
            ivCards = itemView.findViewById(R.id.ivCards);
            tvCardName = itemView.findViewById(R.id.tvCardName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() == 0) {
                        Intent intent = new Intent(mCtx, OwnGiftCardActivity.class);
                        mCtx.startActivity(intent);
                    } else if (getAdapterPosition() == 1) {
                        mClickListener.onRowItemClick(1);
                    } else if (getAdapterPosition() == 2) {
                        //Toast.makeText(mCtx, "Union Gift Called", Toast.LENGTH_SHORT).show();
                        Intent intentGift=new Intent(mCtx, GiftCardActivity.class);
                        mCtx.startActivity(intentGift);
                    } else {
                        /*GiftProductList.Product product = mListGift.get(getAdapterPosition());
                        String param = new Gson().toJson(product);
                        Intent intent = new Intent(mCtx, LunexItemsActivity.class);
                        intent.putExtra(AppoConstants.PRODUCT,param);
                        ((Activity)mCtx).startActivityForResult(intent,AppoConstants.RECHARGE_REQUEST_CODE);*/
                    }
                }
            });
        }

        public void bindCards() {
           // String text = mListGift.get(getAdapterPosition()).getProductName();
            //United States of America Sony PlayStation Subscription
            /*if (text.equals("United States of America Sony PlayStation Subscription")) {
                tvCardName.setText("USA Sony PlayStation Subs");
            } else {
                tvCardName.setText(text);
            }*/
           // tvCardName.setText(mListGift.get(getAdapterPosition()).getCarrier());


            //ivCards.setImageResource(mIcons[getAdapterPosition()]);
            Glide.with(mCtx).load(mIcons[getAdapterPosition()]).into(ivCards);
        }
    }

}
