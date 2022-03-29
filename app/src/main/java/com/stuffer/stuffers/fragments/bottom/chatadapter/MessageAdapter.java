package com.stuffer.stuffers.fragments.bottom.chatadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.fragments.bottom.chatmodel.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mListChats;
    private String imageUrl;
    FirebaseUser fuser;

    public MessageAdapter(Context context, List<Chat> list, String imageurl) {
        this.mContext = context;
        this.mListChats = list;
        this.imageUrl = imageurl;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_chat_one_to_one_right, parent, false);
            return new MessageHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_chat_one_to_one_left, parent, false);
            return new MessageHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Chat chat = mListChats.get(position);

        if (chat.getMessage().startsWith("https:")) {
            holder.show_message.setVisibility(View.GONE);
            Glide.with(holder.messageImageView.getContext())
                    .load(chat.getMessage())
                    .into(holder.messageImageView);
            holder.messageImageView.setVisibility(View.VISIBLE);
        } else {
            holder.show_message.setText(chat.getMessage());
            holder.messageImageView.setVisibility(View.GONE);
        }
        if (imageUrl.equals("default")) {
            //holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageUrl).into(holder.profile_image);
        }

        if (position == mListChats.size() - 1) {
            if (chat.isIsseen()) {
                holder.txt_seen_msg.setText("Seen");
            } else {
                holder.txt_seen_msg.setText("Delivered");
            }
        } else {
            holder.txt_seen_msg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mListChats.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image, messageImageView;

        public TextView txt_seen_msg;

        public MessageHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen_msg = itemView.findViewById(R.id.txt_seen);
            messageImageView = itemView.findViewById(R.id.messageImageView);


        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mListChats.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

}
