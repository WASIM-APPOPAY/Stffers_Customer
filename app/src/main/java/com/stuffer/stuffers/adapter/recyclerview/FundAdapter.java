package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.models.output.FundModel;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class FundAdapter extends RecyclerView.Adapter<FundAdapter.FundHolder> {
    ArrayList<FundModel> mListFund;
    Context mCtx;

    public FundAdapter(ArrayList<FundModel> mListFund, Context ctx) {
        this.mListFund = mListFund;
        this.mCtx = ctx;
    }

    @NonNull
    @Override
    public FundHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fund, parent, false);
        return new FundHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FundHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mListFund.size();
    }

    public class FundHolder extends RecyclerView.ViewHolder {
        ImageView ivFund;
        MyTextView tvFundName;


        public FundHolder(@NonNull View itemView) {
            super(itemView);
            ivFund = itemView.findViewById(R.id.ivFund);
            tvFundName = itemView.findViewById(R.id.tvFundName);
        }

        public void bind() {
            Glide.with(mCtx).load(mListFund.get(getAdapterPosition()).getUrl()).into(ivFund);
            tvFundName.setText(mListFund.get(getAdapterPosition()).getName().toUpperCase());
        }
    }
}
