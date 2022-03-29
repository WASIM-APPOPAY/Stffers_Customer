package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemCLickListener;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.views.MyButton;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class GiftCardAdapter extends RecyclerView.Adapter<GiftCardAdapter.GiftCardHolder> {
    private RecyclerViewRowItemCLickListener mItemClickListener;
    private Context mCtx;
    private ArrayList<AccountModel> mListAccount;
    private String mUserName;
    private ArrayList<String> mListNumbers;


    public GiftCardAdapter(Context mCtx, String username, ArrayList<AccountModel> list, ArrayList<String> mListWalletNumber) {
        this.mCtx = mCtx;
        this.mUserName = username;
        this.mListAccount = list;
        this.mListNumbers = mListWalletNumber;
        try {
            this.mItemClickListener = (RecyclerViewRowItemCLickListener) mCtx;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent should implement RecyclerViewRowItemCLickListener");
        }


    }

    @NonNull
    @Override
    public GiftCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mCtx).inflate(R.layout.layout_gift_card, parent, false);
        return new GiftCardHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftCardHolder holder, int position) {
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        if (mListAccount.size() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public class GiftCardHolder extends RecyclerView.ViewHolder {
        FrameLayout fParent;
        MyTextView tvAccountNos, tvFullName;
        ImageView ivWallet;
        LinearLayout layoutCurrency;
        MyTextView tvFromCurrency, tvGiftAmount, tvGiftCard;
        MyButton btnClaim;
        View viewLine;

        public GiftCardHolder(@NonNull View itemView) {
            super(itemView);
            fParent = itemView.findViewById(R.id.fParent);
            tvAccountNos = itemView.findViewById(R.id.tvAccountNos);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            ivWallet = itemView.findViewById(R.id.ivWallet);
            tvFromCurrency = itemView.findViewById(R.id.tvFromCurrency);
            tvGiftAmount = itemView.findViewById(R.id.tvGiftAmount);
            layoutCurrency = itemView.findViewById(R.id.layoutCurrency);
            btnClaim = itemView.findViewById(R.id.btnClaim);
            viewLine = itemView.findViewById(R.id.viewLine);
            tvGiftCard = itemView.findViewById(R.id.tvGiftCard);

            ivWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() == 0) {
                        mItemClickListener.onRowItemClick(getAdapterPosition());
                    }
                }
            });

        }


        public void bindData() {

            if (mListAccount.get(getAdapterPosition()).getAccountstatus().isEmpty() || mListAccount.get(getAdapterPosition()).getAccountstatus().equalsIgnoreCase("null")) {
                fParent.setVisibility(View.GONE);
                ivWallet.setVisibility(View.GONE);
                layoutCurrency.setVisibility(View.GONE);
                btnClaim.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
                tvGiftCard.setVisibility(View.GONE);
            } else {
                if (getAdapterPosition() == 0) {
                    layoutCurrency.setVisibility(View.GONE);
                    btnClaim.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                    tvGiftCard.setVisibility(View.GONE);
                } else {
                    layoutCurrency.setVisibility(View.VISIBLE);
                    btnClaim.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.VISIBLE);
                    tvGiftCard.setVisibility(View.GONE);
                }
                fParent.setVisibility(View.VISIBLE);
                ivWallet.setVisibility(View.VISIBLE);

                tvFullName.setText(mUserName);
                tvAccountNos.setText(mListAccount.get(getAdapterPosition()).getAccountEncrypt());
                tvFromCurrency.setText(mListAccount.get(getAdapterPosition()).getCurrencyCode());
                tvGiftAmount.setText(mListAccount.get(getAdapterPosition()).getCurrentbalance());

            }

        }
    }
}
