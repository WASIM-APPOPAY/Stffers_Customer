package com.stuffer.stuffers.commonChat.chatUtils;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Created by SiberiaDante on 2017/5/17.
 */

public class ToastApp {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public ToastApp() {
        throw new UnsupportedOperationException("not init ToastUtils");
    }

    public static void initToastUtils(Context context) {
        ToastApp.context = context;
    }

    public static Context getContext() {
        if (context != null) {
            return context;
        } else {
            throw new NullPointerException("ToastUtils NullPointerException,you should init before");
        }
    }
}
