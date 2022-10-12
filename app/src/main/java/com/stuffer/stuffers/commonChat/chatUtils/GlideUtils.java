package com.stuffer.stuffers.commonChat.chatUtils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


import java.io.File;

public class GlideUtils {

    private static GlideUtils glideUtils = new GlideUtils();
    static RequestManager requestManager;
    static RequestBuilder requestBuilder;

    public static GlideUtils with(Context context) {
        requestManager = Glide.with(context);
        return glideUtils;
    }

    public GlideUtils load(String url) {
        requestBuilder = requestManager.load(url);
        RequestOptions options = new RequestOptions();
        options = options.skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL);
        requestBuilder.apply(options);
        return glideUtils;
    }

    public GlideUtils apply(RequestOptions options) {
        requestBuilder.apply(options);
        return glideUtils;
    }

    public GlideUtils load(File file) {
        requestBuilder = requestManager.load(file);
        RequestOptions options = new RequestOptions();
        options = options.skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL);
        requestBuilder.apply(options);
        return glideUtils;
    }

    public GlideUtils placeholder(int res) {
        requestBuilder.placeholder(res);
        return glideUtils;
    }

    public GlideUtils circleCrop() {
        requestBuilder.circleCrop();
        return glideUtils;
    }

    public GlideUtils fitCenter() {
        requestBuilder.fitCenter();
        return glideUtils;
    }

    public void into(ImageView iv) {
        requestBuilder.into(iv);
    }

    public GlideUtils error(int res) {
        requestBuilder.error(res);
        return glideUtils;
    }
}
