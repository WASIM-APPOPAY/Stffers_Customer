package com.stuffer.stuffers.adapter.address;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.OtherBusinessActivity;
import com.stuffer.stuffers.commonChat.chatUtils.GlideUtils;
import com.stuffer.stuffers.commonChat.chatUtils.ToastUtil;
import com.stuffer.stuffers.utils.DataManager;
import com.stuffer.stuffers.utils.MerchantInfoBean;


import java.util.List;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessHolder> {
    private Context context;
    private List<MerchantInfoBean> data;

    public BusinessAdapter(Context context, List<MerchantInfoBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public BusinessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BusinessHolder(LayoutInflater.from(context).inflate(R.layout.item_business, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessHolder holder, int position) {
        MerchantInfoBean bean = data.get(position);
        if (bean != null) {
            if (TextUtils.isEmpty(bean.avatar)) {
                holder.avator.setImageResource(R.drawable.profile);
            } else {
                GlideUtils.with(context).load(bean.avatar).error(R.drawable.profile).placeholder(R.drawable.profile).circleCrop().into(holder.avator);
            }
            holder.businessName.setText(bean.businessName);
            holder.phone.setText(bean.mobileNumber);
            if (!TextUtils.isEmpty(bean.businessAddress)) {
                holder.position.setText(bean.businessAddress);
            }
            holder.content.setOnClickListener(view -> jumpOtherBusinessPage(bean));

            holder.avator.setOnClickListener(view -> jumpOtherBusinessPage(bean));

            holder.route.setOnClickListener(view -> {
                if (TextUtils.isEmpty(bean.businessAddress)) {
                    ToastUtil.showTextShort("Seller did not upload the address");
                    return;
                }
                if (bean.address == null) {
                    ToastUtil.showTextShort("Seller did not upload the address");
                    return;
                }
                String[] dest = bean.address.split(",");
                if (dest.length != 2) {
                    ToastUtil.showTextShort("Seller did not upload the address");
                    return;
                }
                if (TextUtils.isEmpty(DataManager.newLatLog)) {
                    ToastUtil.showTextShort("The current location failed");
                    return;
                }
                String[] src = DataManager.newLatLog.split(",");
                if (src.length != 2) {
                    ToastUtil.showTextShort("Seller did not upload the address");
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr="
                        + src[0]
                        + ","
                        + src[1]
                        + "&daddr="
                        + dest[0]
                        + ","
                        + dest[1]));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                context.startActivity(i);
            });
        }
    }

    private void jumpOtherBusinessPage(MerchantInfoBean bean) {
        Intent intent = new Intent(context, OtherBusinessActivity.class);
        intent.putExtra("otherMerchant", bean);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BusinessHolder extends RecyclerView.ViewHolder {
        ImageView avator;
        TextView businessName;
        TextView phone;
        TextView position;
        ImageView route;
        ViewGroup content;

        public BusinessHolder(@NonNull View itemView) {
            super(itemView);
            avator = itemView.findViewById(R.id.item_business_avator);
            businessName = itemView.findViewById(R.id.item_business_name);
            phone = itemView.findViewById(R.id.item_business_phone);
            position = itemView.findViewById(R.id.item_business_position);
            route = itemView.findViewById(R.id.item_business_address);
            content = itemView.findViewById(R.id.item_business_content);
        }
    }
}
