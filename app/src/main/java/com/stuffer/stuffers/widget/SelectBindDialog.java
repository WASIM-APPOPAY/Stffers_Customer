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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.adapter.recyclerview.BindAdapter;
import com.stuffer.stuffers.commonChat.chatUtils.Constant;
import com.stuffer.stuffers.models.output.BindBean;

import java.util.List;


public class SelectBindDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView tvBindYes;
    private TextView tvBindNo;

    private RecyclerView selectBindRv;

    public SelectBindDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public SelectBindDialog(Context context, int theme) {
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
        this.setContentView(R.layout.dialog_select_bind);

        initView();
        initListener();
    }

    private void initView() {
        tvBindYes = (TextView) findViewById(R.id.select_bind_yes);
        tvBindNo = (TextView) findViewById(R.id.select_bind_no);
        selectBindRv = findViewById(R.id.select_bind_rv);
    }

    private void initListener() {
        tvBindYes.setOnClickListener(this);
        tvBindNo.setOnClickListener(this);

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

    public void setData(List<BindBean> data) {
        selectBindRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        selectBindRv.setAdapter(new BindAdapter(mContext, data));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (isOutOfBounds(getContext(), event)) {
//            hideDialog();
//            mListener.onItemClick(Constant.CANCEL);//触摸dialog外部关闭dialog
//        }
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
        if (v.getId() == R.id.select_bind_yes) {
            hideDialog();
            mListener.onItemClick(Constant.BIND_YES);
        } else if (v.getId() == R.id.select_bind_no) {
            hideDialog();
            mListener.onItemClick(Constant.BIND_NO);
        }
    }

    OnItemClickListener mListener;

    public interface OnItemClickListener {
        /**
         * 点击条目
         *
         * @param type 0是no  1是yes
         */
        void onItemClick(int type);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}
