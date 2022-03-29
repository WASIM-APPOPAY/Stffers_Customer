package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.communicator.RecyclerViewRowItemClickListener2;
import com.stuffer.stuffers.models.output.AccountModel;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class ActiveAccountAdapter extends RecyclerView.Adapter<ActiveAccountAdapter.ActiveAccountHolder> {
    private Context mContext;
    private ArrayList<AccountModel> mListAccount;
    private String mNameMobile;
    private RecyclerViewRowItemClickListener2 mListener;

    public ActiveAccountAdapter(Context mContext, ArrayList<AccountModel> mListAccount, String nameMobile) {
        this.mContext = mContext;
        this.mListAccount = mListAccount;
        this.mNameMobile = nameMobile;
        try {
            mListener = (RecyclerViewRowItemClickListener2) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException("parent must implement RecyclerViewRowItemCLickListener");
        }

    }

    @NonNull
    @Override
    public ActiveAccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_active_account_item, parent, false);
        return new ActiveAccountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveAccountHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mListAccount.size();
    }

    public class ActiveAccountHolder extends RecyclerView.ViewHolder {
        MyTextView tvUserMobileName, tvAccounts;
        LinearLayout layoutUser;

        public ActiveAccountHolder(@NonNull View itemView) {
            super(itemView);
            tvUserMobileName = itemView.findViewById(R.id.tvUserMobileName);
            layoutUser = itemView.findViewById(R.id.layoutUser);
            tvAccounts = itemView.findViewById(R.id.tvAccounts);

            layoutUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRowItemClick2(getAdapterPosition());
                }
            });

        }

        public void bind() {
            tvUserMobileName.setText(mNameMobile);
            tvAccounts.setText(mListAccount.get(getAdapterPosition()).getAccountnumber());
        }
    }
}
