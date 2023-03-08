package com.stuffer.stuffers.widget;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatUtils.DimenUtils;
import com.stuffer.stuffers.utils.ScheduleTimeBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TimerItemView extends FrameLayout implements View.OnClickListener {

    private TextView timerTitle;
    private LinearLayout timerContainer;
    private TextView addTimeIv;
    private ImageView dateSwitch;
    private SimpleDateFormat format;
    private Calendar calendar = Calendar.getInstance(Locale.getDefault());
    private TimePickerDialog timePickerDialog;
    private static View originLine;
    private String defaultContent = "closure";
    private String selectContent = "Open at selected hours";
    private ScheduleTimeBean.ScheduleTimeInfo timeInfo;

    public TimerItemView(@NonNull Context context) {
        this(context, null);
    }

    public TimerItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TimerItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.include_schedule_container, this);
        dateSwitch = findViewById(R.id.schedule_date_switch);
        timerTitle = findViewById(R.id.schedule_date_title);
        timerContainer = findViewById(R.id.schedule_container_timers);
        addTimeIv = findViewById(R.id.schedule_date_add);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimerItemViewStyle);
        String title = ta.getString(R.styleable.TimerItemViewStyle_title);
        timerTitle.setText(title);
        ta.recycle();
        dateSwitch.setTag("off");
        format = new SimpleDateFormat("HH:mm");
        dateSwitch.setOnClickListener(this);
        addTimeIv.setOnClickListener(this);
        timeInfo = new ScheduleTimeBean.ScheduleTimeInfo();
        initTimerContainer(defaultContent);
    }

    private void initTimerContainer(String text) {
        //初始化默认时间容器
        TextView defaultTv = new TextView(getContext());
        defaultTv.setText(text);
        timerContainer.addView(defaultTv);
        MarginLayoutParams params = (MarginLayoutParams) defaultTv.getLayoutParams();
        params.bottomMargin = DimenUtils.dip2px(getContext(), 10);
        defaultTv.setLayoutParams(params);
    }


    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.schedule_date_switch:
                switchBtnStatus();
                break;
            case R.id.schedule_date_add:
                changeTimeRange(1);
                addTimeIv.setVisibility(View.GONE);
                break;
            case R.id.schedule_time_delete:
                deleteTimer(Integer.parseInt((String) view.getTag()));
                addTimeIv.setVisibility(View.VISIBLE);
                break;
            case R.id.schedule_start_time_container:
            case R.id.schedule_end_time_container:
                //获取日期格式器对象
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker v, int hourOfDay, int minute) {
                        //同DatePickerDialog控件
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        try {
                            ((TextView) ((ViewGroup) view).getChildAt(0)).setText(format.format(calendar.getTime()));
                        } catch (Exception e) {

                        }
                    }

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
                changeTimeLineBold(view);
                break;
            default:
                break;
        }
    }

    private void changeTimeLineBold(View timeContent) {
        ViewGroup parent = (ViewGroup) timeContent;
        if (parent.getChildCount() == 2) {
            View child = parent.getChildAt(1);
            if (originLine != null) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) originLine.getLayoutParams();
                params.height = DimenUtils.dip2px(getContext(), 1);
                originLine.setLayoutParams(params);
            }
            originLine = child;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
            //加粗
            params.height = DimenUtils.dip2px(getContext(), 3);
            child.setLayoutParams(params);
        }
    }

    private void switchBtnStatus() {
        if (dateSwitch.getTag().equals("on")) {
            dateSwitch.setImageResource(R.drawable.schedule_container_btn_off);
            dateSwitch.setTag("off");
            changeTimeRange(0);
            initTimerContainer(defaultContent);
            addTimeIv.setVisibility(View.GONE);
        } else {
            dateSwitch.setImageResource(R.drawable.schedule_container_btn_on);
            dateSwitch.setTag("on");
            timerContainer.removeAllViews();
            //添加一行
            if (selectContent.equals(getResources().getString(R.string.schedule_select_time_up))) {
                changeTimeRange(1);
            } else {
                initTimerContainer(selectContent);
            }
        }
    }

    private void changeTimeRange(int op) {
        if (op == 1) {
            //添加
            View addView = View.inflate(getContext(), R.layout.include_schedule_time, null);
            View startContainer = addView.findViewById(R.id.schedule_start_time_container);
            View endContainer = addView.findViewById(R.id.schedule_end_time_container);
            startContainer.setOnClickListener(this);
            endContainer.setOnClickListener(this);
            timerContainer.addView(addView);
            //如果容器的数量为2将所有的叉号显示出来
            if (timerContainer.getChildCount() == 2) {
                for (int i = 0; i < 2; i++) {
                    ImageView timerDelete = timerContainer.getChildAt(i).findViewById(R.id.schedule_time_delete);
                    timerDelete.setVisibility(View.VISIBLE);
                    timerDelete.setTag(String.valueOf(i));
                    timerDelete.setOnClickListener(this);
                }
            } else {
                addTimeIv.setVisibility(View.VISIBLE);
            }
        } else {
            //删除
            if (timerContainer.getChildCount() > 0) {
                timerContainer.removeViewAt(timerContainer.getChildCount() - 1);
            }
        }
    }

    private void deleteTimer(int index) {
        timerContainer.removeViewAt(index);
        //如果容器的数量为1将所有的叉号显示出来
        if (timerContainer.getChildCount() == 1) {
            ImageView timerDelete = timerContainer.getChildAt(0).findViewById(R.id.schedule_time_delete);
            timerDelete.setVisibility(View.GONE);
        }
    }

    public void resetData(String content) {
        selectContent = content;
        if (selectContent.equals(getResources().getString(R.string.schedule_select_time_up))) {
            dateSwitch.setTag("on");
            switchBtnStatus();
            timerContainer.removeAllViews();
            initTimerContainer(defaultContent);
        } else {
            dateSwitch.setTag("off");
            switchBtnStatus();
            timerContainer.removeAllViews();
            initTimerContainer(selectContent);
            addTimeIv.setVisibility(View.GONE);
        }
    }

    public ScheduleTimeBean.ScheduleTimeInfo getScheduleTimeInfo() {
        timeInfo.timerList = new ArrayList<String>();
        timeInfo.dateName = timerTitle.getText().toString();
        if ("on".equals(dateSwitch.getTag())) {
            timeInfo.isOn = true;
            if (selectContent.equals(getResources().getString(R.string.schedule_select_time_up))) {
                int count = timerContainer.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = timerContainer.getChildAt(i);
                    View startView = child.findViewById(R.id.schedule_start_time);
                    View endView = child.findViewById(R.id.schedule_end_time);
                    if (startView instanceof TextView && endView instanceof TextView) {
                        timeInfo.timerList.add(((TextView) startView).getText() + " - " + ((TextView) endView).getText());
                    }
                }
            } else {
                timeInfo.timerList.add(selectContent);
            }
        } else {
            timeInfo.isOn = false;
            timeInfo.timerList.add(defaultContent);
        }
        return timeInfo;
    }
}
