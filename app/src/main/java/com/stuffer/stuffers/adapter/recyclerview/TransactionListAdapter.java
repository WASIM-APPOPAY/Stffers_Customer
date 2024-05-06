package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.communicator.SeeListener;
import com.stuffer.stuffers.models.output.TransactionList2;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionListHolder> {
    private SeeListener mSeeListener;
    Context mContext;
    ArrayList<TransactionList2> mListItems;
    RecyclerViewRowItemCLickListener mItemClickListener;

    public TransactionListAdapter(Context mContext, ArrayList<TransactionList2> items) {
        this.mContext = mContext;
        this.mListItems = items;
        try {
            this.mItemClickListener = (RecyclerViewRowItemCLickListener) mContext;
            this.mSeeListener = (SeeListener) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent must implement RecyclerViewRowItemCLickListener");
        }
    }

    @NonNull
    @Override
    public TransactionListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_transaction_list_items, parent, false);
        return new TransactionListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionListHolder holder, int position) {
        holder.bindProcess();
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public class TransactionListHolder extends RecyclerView.ViewHolder {
        ImageView ivType, ivView;
        MyTextView tvDescription, tvDateTime, tvAmountCurrency, tvTransactionInfo;
        CardView cardTransaction;

        public TransactionListHolder(@NonNull View itemView) {
            super(itemView);
            cardTransaction = itemView.findViewById(R.id.cardTransaction);
            ivType = itemView.findViewById(R.id.ivType);
            ivView = itemView.findViewById(R.id.ivView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvAmountCurrency = itemView.findViewById(R.id.tvAmountCurrency);
            tvTransactionInfo = itemView.findViewById(R.id.tvTransactionInfo);
            ivView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mSeeListener.onSeeRequest(getAdapterPosition());
                }
            });
            cardTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mItemClickListener.onRowItemClick(getAdapterPosition());

                }
            });
        }

        public void bindProcess() {
            if (mListItems.get(getAdapterPosition()).getPaymenttype().equalsIgnoreCase("DEPOSITE") ||
                    mListItems.get(getAdapterPosition()).getPaymenttype().equalsIgnoreCase("null")) {
                ivType.setImageResource(R.mipmap.ic_recive_money);
                ivView.setVisibility(View.GONE);
            }
            if (mListItems.get(getAdapterPosition()).getPaymenttype().equalsIgnoreCase("PAID") ||
                    mListItems.get(getAdapterPosition()).getPaymenttype().equalsIgnoreCase("BILL") ||
                    mListItems.get(getAdapterPosition()).getPaymenttype().equalsIgnoreCase("TRANSFER")) {
                ivType.setImageResource(R.mipmap.ic_send_money);
                ivView.setVisibility(View.VISIBLE);

            }
            tvDescription.setText(mListItems.get(getAdapterPosition()).getTransactiondescription());
            tvTransactionInfo.setText(mListItems.get(getAdapterPosition()).getTransactionstatus());
            //tvTransactionInfo.setTextColor(Color.parseColor("#3CB371"));
            tvTransactionInfo.setTextColor(Color.parseColor("#000000"));
            tvDateTime.setText(mListItems.get(getAdapterPosition()).getViewdate());
            tvAmountCurrency.setText(mListItems.get(getAdapterPosition()).getCurrencycode() + " " + mListItems.get(getAdapterPosition()).getTransactionamount());

        }
    }
    //yyyy-MM-dd'T'HH:mm:ss.SSS
}
