package com.stuffer.stuffers.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.models.output.BindBean;
import com.stuffer.stuffers.utils.DataManager;

import java.util.List;

public class BindAdapter extends RecyclerView.Adapter<BindAdapter.BusinessHolder> {

    private Context context;
    private List<BindBean> data;

    public static ImageView prev;


    public BindAdapter(Context context, List<BindBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public BusinessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BusinessHolder(LayoutInflater.from(context).inflate(R.layout.item_bind_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessHolder holder, int position) {
        BindBean bean = data.get(position);
        holder.itemTitle.setText(bean.accountnumber);
        if (bean.accountnumber.equals(DataManager.ownMerchantAccount)) {
            DataManager.merchantAccount = bean.accountnumber;
            holder.itemCheck.setImageResource(R.drawable.item_study_subject_s);
            prev = holder.itemCheck;
        } else {
            holder.itemCheck.setImageResource(R.drawable.item_study_subject_u);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prev != null) {
                    prev.setImageResource(R.drawable.item_study_subject_u);
                }
                DataManager.merchantAccount = holder.itemTitle.getText().toString();
                holder.itemCheck.setImageResource(R.drawable.item_study_subject_s);
                prev = holder.itemCheck;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BusinessHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        ImageView itemCheck;

        public BusinessHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemCheck = itemView.findViewById(R.id.item_check);
        }
    }
}