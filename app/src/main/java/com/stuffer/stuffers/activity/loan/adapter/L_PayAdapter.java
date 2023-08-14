package com.stuffer.stuffers.activity.loan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;

public class L_PayAdapter extends RecyclerView.Adapter<L_PayAdapter.L_PayHolder> {
    @NonNull
    @Override
    public L_PayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loan_list_items, parent,false);

        return new L_PayHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull L_PayHolder holder, int position) {
        holder.bind();
    }


    @Override
    public int getItemCount() {
        return 8;
    }

    public class L_PayHolder extends RecyclerView.ViewHolder {

        public L_PayHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void bind() {

        }
    }
}
