package com.stuffer.stuffers.models.output;


import java.io.Serializable;
import java.util.List;

public class ScheduleTimeBean implements Serializable {
    //选择的类型
    public int type;
    public List<ScheduleTimeInfo> timeInfoList;

    public static class ScheduleTimeInfo implements Serializable {
        public String dateName;
        public boolean isOn;
        public List<String> timerList;
    }
}
