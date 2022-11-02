package com.stuffer.stuffers.utils;

import android.graphics.Color;
import android.text.Html;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SellerHelper {

    private static SellerHelper helper = new SellerHelper();

    private SellerHelper() {

    }

    public static SellerHelper getInstance() {
        return helper;
    }

    public void showScheduleStatus(List<ScheduleTimeBean.ScheduleTimeInfo> list, TextView scheduleStatus) {
        String currentDate = getCurrentWeek();
        ScheduleTimeBean.ScheduleTimeInfo info = null;
        for (int i = 0; i < list.size(); i++) {
            info = list.get(i);
            if (currentDate.equals(info.dateName)) {
                showScheduleStatus(info, scheduleStatus);
                break;
            }
        }
    }

    /**
     * 处理当前日期的状态（是否在服务期内 ）
     *
     * @param timeInfo
     */
    public void showScheduleStatus(ScheduleTimeBean.ScheduleTimeInfo timeInfo, TextView scheduleStatus) {
        if (timeInfo == null) {
            return;
        }
        try {
            if (timeInfo.timerList.size() > 0) {
                String time1 = timeInfo.timerList.get(0);
                if (time1.equals("By appointment only")) {
                    scheduleStatus.setTextColor(Color.BLUE);
                    scheduleStatus.setText("Need an appointment today");
                    return;
                }
                if (time1.equals("closure")) {
                    scheduleStatus.setTextColor(Color.BLUE);
                    scheduleStatus.setText("Rest today");
                    return;
                } else if (time1.equals("Open all day")) {
                    scheduleStatus.setTextColor(Color.BLUE);
                    scheduleStatus.setText("Open all day");
                    return;
                } else {
                    SimpleDateFormat dateF = new SimpleDateFormat("HH:mm");
                    String curStr = dateF.format(new Date());
                    Date curDate = dateF.parse(curStr);
                    if (timeInfo.timerList.size() == 1) {
                        String[] timeRanges1 = time1.split(" - ");
                        String startTime1 = timeRanges1[0];
                        String endTime1 = timeRanges1[1];
                        Date startDate1 = dateF.parse(startTime1);
                        Date endDate1 = dateF.parse(endTime1);
                        if (curDate.compareTo(startDate1) >= 0 && curDate.compareTo(endDate1) <= 0) {
                            scheduleStatus.setText(Html.fromHtml("<font color='#0000ff'>In operation，</font>" + startTime1 + "~" + endTime1 + " (current time)"));
                            return;
                        } else if (curDate.compareTo(startDate1) >= 0) {
                            scheduleStatus.setText(Html.fromHtml("<font color='#0000ff'>In rest，</font>" + startTime1 + " (current time) open for business"));
                            return;
                        }
                    } else {
                        String time2 = timeInfo.timerList.get(1);
                        String[] timeRanges1 = time1.split(" - ");
                        String[] timeRanges2 = time2.split(" - ");
                        String startTime1 = timeRanges1[0];
                        String endTime1 = timeRanges1[1];
                        String startTime2 = timeRanges2[0];
                        String endTime2 = timeRanges2[1];
                        Date startDate1 = dateF.parse(startTime1);
                        Date endDate1 = dateF.parse(endTime1);

                        Date startDate2 = dateF.parse(startTime2);
                        Date endDate2 = dateF.parse(endTime2);
                        if (curDate.compareTo(startDate1) < 0) {
                            scheduleStatus.setText(Html.fromHtml("<font color='#0000ff'>In rest，</font>" + startTime1 + " (current time) open for business"));
                            return;
                        }
                        if (curDate.compareTo(startDate1) >= 0 && curDate.compareTo(endDate1) <= 0) {
                            scheduleStatus.setText(Html.fromHtml("<font color='#0000ff'>In operation，</font>" + startTime1 + "~" + endTime1 + " (current time)"));
                            return;
                        }
                        if (curDate.compareTo(endDate1) > 0 && curDate.compareTo(startDate2) < 0) {
                            scheduleStatus.setText(Html.fromHtml("<font color='#0000ff'>In rest，</font>" + startTime2 + " (current time) open for business"));
                            return;
                        }
                        if (curDate.compareTo(startDate2) >= 0 && curDate.compareTo(endDate2) <= 0) {
                            scheduleStatus.setText(Html.fromHtml("<font color='#0000ff'>In operation，</font>" + startTime2 + "~" + endTime2 + " (current time)"));
                            return;
                        }
                        if (curDate.compareTo(endDate2) > 0) {
                            scheduleStatus.setText(Html.fromHtml("<font color='#0000ff'>In rest</font>"));
                            return;
                        }
                    }

                }
            }
        } catch (Exception e) {

        }

    }

    public String getCurrentWeek() {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        int day = instance.get(Calendar.DAY_OF_WEEK);
        int offDay = day;
        switch (offDay % 7) {
            case 0:
                return "Saturday";
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
        }
        return "";
    }

}
