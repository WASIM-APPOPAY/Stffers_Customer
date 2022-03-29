package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.models.output.CardVaultModel;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.MyCardHolder> {
    ArrayList<CardVaultModel> mList;
    Context mContext;
    RecyclerViewRowItemCLickListener mItemsClickListener;

    public MyCardAdapter(Context context, ArrayList<CardVaultModel> list) {
        this.mContext = context;
        this.mList = list;

        try {
            this.mItemsClickListener = (RecyclerViewRowItemCLickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent should implement RecyclerViewRowItemCLickListener");
        }
    }

    @NonNull
    @Override
    public MyCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_my_card_item, parent, false);
        return new MyCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCardHolder holder, int position) {
        holder.bindProcess();
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyCardHolder extends RecyclerView.ViewHolder {

        private ImageView ivCardType;
        private MyTextView tvCardNumber;
        private ImageView ivForward;
        private LinearLayout layoutCard;

        public MyCardHolder(@NonNull View itemView) {
            super(itemView);
            ivCardType = (ImageView) itemView.findViewById(R.id.ivCardType);
            tvCardNumber = (MyTextView) itemView.findViewById(R.id.tvCardNumber);
            ivForward = (ImageView) itemView.findViewById(R.id.ivForward);
            layoutCard = (LinearLayout) itemView.findViewById(R.id.layoutCard);
            layoutCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemsClickListener.onRowItemClick(getAdapterPosition());
                }
            });
        }


        public void bindProcess() {
            if (mList.get(getAdapterPosition()).getCc_type().equalsIgnoreCase(AppoConstants.VISA)) {
                ivCardType.setImageResource(R.drawable.visa_my_card);
            } else if (mList.get(getAdapterPosition()).getCc_type().equalsIgnoreCase(AppoConstants.MASTERCARD)) {
                ivCardType.setImageResource(R.drawable.mastercard_my_card);
            } else if (mList.get(getAdapterPosition()).getCc_type().equalsIgnoreCase(AppoConstants.AMERICA_EXPRESS)) {
                ivCardType.setImageResource(R.drawable.amex_my_card);
            }
            tvCardNumber.setText(mList.get(getAdapterPosition()).getCc_number());
        }
    }
}
