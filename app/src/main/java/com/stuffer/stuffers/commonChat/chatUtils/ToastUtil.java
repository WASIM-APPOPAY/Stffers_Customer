package com.stuffer.stuffers.commonChat.chatUtils;

import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Toast工具类
 */
public class ToastUtil {
    private static final String TAG = ToastUtil.class.getSimpleName();
    private static Toast toast;
    private static List<Toast> toastList = new ArrayList<>();

    public ToastUtil() {
        throw new UnsupportedOperationException("not init ToastUtils");
    }

    /**
     * 吐司文本内容
     * 单例模式,吐司时间长
     *
     * @param content 吐司内容
     */
    public static void showSingletonLong(String content) {
        if (toast == null) {
            toast = Toast.makeText(ToastApp.getContext(), "", Toast.LENGTH_LONG);
        }
        toast.setText(content);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 吐司文本内容
     * 单例模式，吐司时间短
     *
     * @param content 吐司内容
     */
    public static void showSingletonShort(String content) {
        if (toast == null) {
            toast = Toast.makeText(ToastApp.getContext(), "", Toast.LENGTH_SHORT);
        }
        toast.setText(content);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }


    public static void showTextShort(String content) {
        toast = Toast.makeText(ToastApp.getContext(), "", Toast.LENGTH_LONG);
        toastList.add(toast);
        toast.setText(content);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }


    public static void showTextLong(String content) {
        toast = Toast.makeText(ToastApp.getContext(), "", Toast.LENGTH_LONG);
        toastList.add(toast);
        toast.setText(content);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }



    public static void showSingletonText(String content, int duration, int position) {
        if (toast == null) {
            toast = Toast.makeText(ToastApp.getContext(), "", duration);
        }
        toast.setText(content);
        toast.setDuration(duration);
        toast.setGravity(position, 0, 0);
        toast.show();
    }


    public static void showSingletonImageCenter(int resId, int duration) {
        if (toast == null) {
            toast = Toast.makeText(ToastApp.getContext(), "", duration);
        }
        ImageView imageView = new ImageView(ToastApp.getContext());
        imageView.setImageResource(resId);
        toast.setView(imageView);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void showImageCenter(int resId, int duration) {
        toast = Toast.makeText(ToastApp.getContext(), "", duration);
        toastList.add(toast);
        ImageView imageView = new ImageView(ToastApp.getContext());//创建图片控件
        imageView.setImageResource(resId);//给控件设置图片
        toast.setView(imageView);//把图片绑定到Toast上
        toast.setDuration(duration);//Toast显示的时间;
        //设置图片显示的位置：三个参数
        //第一个：位置，可以用|添加并列位置，第二个：相对于X的偏移量，第三个：相对于Y轴的偏移量
        //注意一点：第二和第三个参数是相对于第一个参数设定的位置偏移的
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();//显示Toast
    }

    /**
     * 重置Toast对象
     */
    public static void resetToast() {
        toast = Toast.makeText(ToastApp.getContext(), "", Toast.LENGTH_LONG);
    }

    /**
     * 取消最近创建的一个Toast
     */
    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * 取消所有的Toast任务
     */
    public static void cancelAll() {
        for (int i = 0; i < toastList.size(); i++) {
            if (toastList.get(i) != null) {
                toastList.get(i).cancel();
            }
        }
    }

}
