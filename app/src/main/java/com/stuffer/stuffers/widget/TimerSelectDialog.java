package com.stuffer.stuffers.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatUtils.Constant;

public class TimerSelectDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView timeUp;
    private TextView allTimeUp;
    private TextView reserveTimeUp;

    public TimerSelectDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public TimerSelectDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        initDialog();
    }

    /**
     * 初始化Dialog
     */
    public void initDialog() {
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.setGravity(Gravity.RELATIVE_LAYOUT_DIRECTION | Gravity.CENTER);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        setCanceledOnTouchOutside(false);
        show();
    }

    /**
     * 隐藏Dialog
     */
    private void hideDialog() {
        cancel();
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.schedule_timer_select);

        initView();
        initListener();
    }

    private void initView() {
        timeUp = (TextView) findViewById(R.id.schedule_select_time_up);
        allTimeUp = (TextView) findViewById(R.id.schedule_all_time_up);
        reserveTimeUp = (TextView) findViewById(R.id.schedule_reserve_time_up);
    }

    private void initListener() {
        timeUp.setOnClickListener(this);
        allTimeUp.setOnClickListener(this);
        reserveTimeUp.setOnClickListener(this);

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {  //返回键监听
                    hideDialog();
                    mListener.onItemClick(Constant.CANCEL);//返回键关闭dialog
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOutOfBounds(getContext(), event)) {
            hideDialog();
            mListener.onItemClick(Constant.CANCEL);//触摸dialog外部关闭dialog
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断当前用户触摸是否超出了Dialog的显示区域
     *
     * @param context
     * @param event
     * @return
     */
    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.schedule_select_time_up) {
            mListener.onItemClick(Constant.SELECT_TIME);
            hideDialog();
        } else if (v.getId() == R.id.schedule_all_time_up) {
            mListener.onItemClick(Constant.ALL_TIME);
            hideDialog();
        } else if (v.getId() == R.id.schedule_reserve_time_up) {
            mListener.onItemClick(Constant.RESERVE_TIME);
            hideDialog();
        }
    }

    OnItemClickListener mListener;

    public interface OnItemClickListener {
        /**
         * 点击条目
         *
         * @param type 0取消，1拍照，2相册
         */
        void onItemClick(int type);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}
