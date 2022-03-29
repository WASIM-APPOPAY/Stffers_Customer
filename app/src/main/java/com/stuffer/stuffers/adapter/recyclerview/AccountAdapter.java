package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {
    private Context mContext;
    private ArrayList<AccountModel> mListAccount;
    private RecyclerViewRowItemCLickListener mItemClickListener;

    public AccountAdapter(Context context, ArrayList<AccountModel> list) {
        this.mContext = context;
        this.mListAccount = list;
        try {
            this.mItemClickListener = (RecyclerViewRowItemCLickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent should implement RecyclerViewRowItemCLickListener");
        }
    }


    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_account_item, parent, false);
        return new AccountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
        holder.processBinding();
    }

    @Override
    public int getItemCount() {
        return mListAccount.size();
    }

    public class AccountHolder extends RecyclerView.ViewHolder {
        MyTextView tvAccNumber, tvAccStatus, tvCurrencyType, tvBalance;
        CardView cardAccount;

        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            cardAccount = itemView.findViewById(R.id.cardAccount);
            tvAccNumber = itemView.findViewById(R.id.tvAccNumber);
            tvAccStatus = itemView.findViewById(R.id.tvAccStatus);
            tvCurrencyType = itemView.findViewById(R.id.tvCurrencyType);
            tvBalance = itemView.findViewById(R.id.tvBalance);
            cardAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onRowItemClick(getAdapterPosition());
                }
            });

        }

        public void processBinding() {
            if (mListAccount.get(getAdapterPosition()).getAccountEncrypt() == null) {
                tvAccNumber.setText(mListAccount.get(getAdapterPosition()).getAccountnumber());
            } else {
                tvAccNumber.setText(mListAccount.get(getAdapterPosition()).getAccountEncrypt());
            }
            tvAccStatus.setText(mListAccount.get(getAdapterPosition()).getAccountstatus());
            tvCurrencyType.setText(mListAccount.get(getAdapterPosition()).getCurrencyCode());
            tvBalance.setText(mListAccount.get(getAdapterPosition()).getCurrentbalance());
        }
    }
}
