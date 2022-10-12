package com.stuffer.stuffers.commonChat.chatAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatModel.ChatMore;
import com.stuffer.stuffers.commonChat.interfaces.MoreListener;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MoreHolder> {
    private Context mCtx;
    private ArrayList<ChatMore> mList;
    private MoreListener moreListener;

    public MoreAdapter(ArrayList<ChatMore> moreItems,Context context) {
        this.mList = moreItems;
        moreListener= (MoreListener) context;
    }

    @NonNull
    @Override
    public MoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_more_option, parent, false);
        return new MoreHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MoreHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        MyTextView tvNameMore;

        public MoreHolder(@NonNull View itemView) {
            super(itemView);
            tvNameMore = itemView.findViewById(R.id.tvNameMore);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moreListener.onMoreItemClick(getAdapterPosition());
                }
            });
        }

        public void bind() {
            ivIcon.setImageResource(mList.get(getAdapterPosition()).getId());
            tvNameMore.setText(mList.get(getAdapterPosition()).getName());
        }
    }
}
