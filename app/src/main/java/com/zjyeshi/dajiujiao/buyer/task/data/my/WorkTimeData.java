package com.zjyeshi.dajiujiao.buyer.task.data.my;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * 上下班时间
 * Created by wuhk on 2016/7/25.
 */
public class WorkTimeData extends BaseData<WorkTimeData> {
    private String startTime;
    private String amEndTime;
    private String pmStartTime;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = "8:30";
    }

    public String getAmEndTime() {
        return amEndTime;
    }

    public void setAmEndTime(String amEndTime) {
        this.amEndTime = "12:00";
    }

    public String getPmStartTime() {
        return pmStartTime;
    }

    public void setPmStartTime(String pmStartTime) {
        this.pmStartTime = "13:00";
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = "17:30";
    }
}
