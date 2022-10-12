package com.stuffer.stuffers.commonChat.chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;


import java.io.File;

public class ImageViewerActivity extends AppCompatActivity {
    private static final String MESSAGE = ImageViewerActivity.class.getPackage().getName() + ".MESSAGE";
    private static final String URL = ImageViewerActivity.class.getPackage().getName() + ".URL";


    public static Intent newMessageInstance(Context context, Message message) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putExtra(MESSAGE, message);
        return intent;
    }

    public static Intent newUrlInstance(Context context, String url) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putExtra(URL, url);
        return intent;
    }

    PhotoView photoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        photoView = findViewById(R.id.photo_view);

        Message message = getIntent().getParcelableExtra(MESSAGE);
        String url = getIntent().getStringExtra(URL);
        if (url != null) {
            Glide.with(this).load(url).into(photoView);
        } else {
            User userMe = new ChatHelper(this).getLoggedInUser();
            File file = ChatHelper.getFile(this, message, userMe.getId());
            Glide.with(this).load(file).into(photoView);
        }
    }
}
