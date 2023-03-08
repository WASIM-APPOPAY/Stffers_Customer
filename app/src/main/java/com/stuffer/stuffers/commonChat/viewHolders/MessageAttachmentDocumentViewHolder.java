package com.stuffer.stuffers.commonChat.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import com.stuffer.stuffers.R;

import com.stuffer.stuffers.commonChat.chatModel.Attachment;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatUtils.FileUtils;
import com.stuffer.stuffers.commonChat.interfaces.OnMessageItemClick;


/**
 * Created by mayank on 11/5/17.
 */

public class MessageAttachmentDocumentViewHolder extends BaseMessageViewHolder {
    TextView fileExtention, fileName, fileSize;
    ImageView file_icon;

    public MessageAttachmentDocumentViewHolder(View itemView, OnMessageItemClick itemClickListener) {
        super(itemView, itemClickListener);
        file_icon = itemView.findViewById(R.id.file_icon);
        fileExtention = itemView.findViewById(R.id.file_extention);
        fileName = itemView.findViewById(R.id.file_name);
        fileSize = itemView.findViewById(R.id.file_size);

        itemView.setOnClickListener(v -> {
            onItemClick(true);
        });
        itemView.setOnLongClickListener(v -> {
            onItemClick(false);
            return true;
        });
    }

    @Override
    public void setData(Message message, int position) {
        super.setData(message, position);
        Attachment attachment = message.getAttachment();
        fileName.setText(attachment.getName());
        fileSize.setText(FileUtils.getReadableFileSize(attachment.getBytesCount()));
        fileExtention.setText(FileUtils.getExtension(attachment.getName()));

        Glide.with(context).load(isMine() ? R.drawable.ic_insert_file_white_64dp : R.drawable.ic_insert_file_blue_64dp).into(file_icon);
        fileExtention.setTextColor(ContextCompat.getColor(context, isMine() ? R.color.colorPrimary : android.R.color.white));
        fileSize.setTextColor(ContextCompat.getColor(context, isMine() ? R.color.colorBg : R.color.textColor4));
        fileName.setTextColor(ContextCompat.getColor(context, isMine() ? android.R.color.white : android.R.color.black));
    }

}
