package com.stuffer.stuffers.commonChat.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.interfaces.OnMessageItemClick;
import com.stuffer.stuffers.commonChat.viewHolders.BaseMessageViewHolder;



/**
 * Created by mayank on 11/5/17.
 */

public class MessageAttachmentImageViewHolder extends BaseMessageViewHolder {
    ImageView image;
    LinearLayout ll;

    public MessageAttachmentImageViewHolder(View itemView, OnMessageItemClick itemClickListener) {
        super(itemView, itemClickListener);
        image = itemView.findViewById(R.id.image);
        ll = itemView.findViewById(R.id.container);

        itemView.setOnClickListener(v -> onItemClick(true));
        itemView.setOnLongClickListener(v -> {
            onItemClick(false);
            return true;
        });
    }

    @Override
    public void setData(final Message message, int position) {
        super.setData(message, position);

//        cardView.setCardBackgroundColor(ContextCompat.getColor(context, message.isSelected() ? R.color.colorPrimary : R.color.colorBgLight));
//        ll.setBackgroundColor(message.isSelected() ? ContextCompat.getColor(context, R.color.colorPrimary) : isMine() ? Color.WHITE : ContextCompat.getColor(context, R.color.colorBgLight));

        Glide.with(context).load(message.getAttachment().getUrl())
                .apply(new RequestOptions().placeholder(R.drawable.user1).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(image);
    }
}
