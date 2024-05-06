package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.ReceiverListener;
import com.stuffer.stuffers.models.Product.Amount;
import com.stuffer.stuffers.views.MyTextView;
import com.stuffer.stuffers.views.MyTextViewBold;

import java.util.List;

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverAdapter.ReceiverHolder> {

    private final Context mContext;
    private final List<Amount> mListAmount;
    private final ReceiverListener mReceiverListener;

    public ReceiverAdapter(Context context, List<Amount> amountList) {
        this.mContext = context;
        this.mListAmount = amountList;
        try {
            this.mReceiverListener = (ReceiverListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent must implement ReceiverListener");
        }
    }

    @NonNull
    @Override
    public ReceiverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_recharge_items, parent, false);
        return new ReceiverHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiverHolder holder, int position) {
        holder.bindProcess();
    }

    @Override
    public int getItemCount() {
        return mListAmount.size();
    }

    public class ReceiverHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        MyTextViewBold tvDestinationAmt;

        public ReceiverHolder(@NonNull View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.cardview);
            tvDestinationAmt = itemView.findViewById(R.id.tvDestinationAmt);
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mReceiverListener.onReceiverClick(getAdapterPosition(), mListAmount.get(getAdapterPosition()).getDestAmt(), mListAmount.get(getAdapterPosition()).getDestCurr());
                }
            });
        }

        public void bindProcess() {
            String amountDestCurrency = mListAmount.get(getAdapterPosition()).getDestCurr()+" " + mListAmount.get(getAdapterPosition()).getDestAmt();
            tvDestinationAmt.setText(amountDestCurrency);
        }
    }
}
