package com.stuffer.stuffers.adapter.recyclerview;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatUtils.GlideUtils;
import com.stuffer.stuffers.widget.CornerImageView;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

public class AddItemAdapter extends BaseBannerAdapter<String> {

    public AddItemAdapter() {
    }

    @Override
    protected void bindData(BaseViewHolder<String> holder, String data, int position,
                            int pageSize) {
        CornerImageView iv = holder.itemView.findViewById(R.id.banner_image);
        RoundedCorners roundedCorners = new RoundedCorners(15);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        iv.setRoundCornet(20);
        GlideUtils.with(holder.itemView.getContext()).load(data).apply(options).placeholder(R.drawable.add_item_default_img).error(R.drawable.add_item_default_img).into(iv);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_slide_mode;
    }
}