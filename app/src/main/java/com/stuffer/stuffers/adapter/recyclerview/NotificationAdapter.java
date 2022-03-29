package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.models.output.Notifications;
import com.stuffer.stuffers.views.MyTextView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    ArrayList<Notifications> mListNotification;
    Context mContext;

    public NotificationAdapter(ArrayList<Notifications> mListNotification, Context mContext) {
        this.mListNotification = mListNotification;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_notification_items, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mListNotification.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        MyTextView tvInformation, tvDateTime;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            tvInformation = itemView.findViewById(R.id.tvInformation);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
        }

        public void bind() {
            tvInformation.setText(mListNotification.get(getAdapterPosition()).getNotification_text());
            tvDateTime.setText(mListNotification.get(getAdapterPosition()).getNotification_date());
        }
    }
}
