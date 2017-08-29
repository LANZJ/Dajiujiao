package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.entity.my.work.DateRemark;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 日报详情
 *
 * Created by zhum on 2016/6/23.
 */
public class DailyInfoData extends BaseData<DailyInfoData> {
//    recordTime : '', //记录时间
//    trip : '',//今日行程
//    summary : '',//今日小结
//    todayCost : '',//今日花费
//    tomorrowPlan : '',//明日计划

    private String recordTime;
    private String trip;
    private String summary;
    private String todayCost;
    private String tomorrowPlan;
    private List<DateRemark> supInfos;

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTodayCost() {
        return todayCost;
    }

    public void setTodayCost(String todayCost) {
        this.todayCost = todayCost;
    }

    public String getTomorrowPlan() {
        return tomorrowPlan;
    }

    public void setTomorrowPlan(String tomorrowPlan) {
        this.tomorrowPlan = tomorrowPlan;
    }

    public List<DateRemark> getSupInfos() {
        return supInfos;
    }

    public void setSupInfos(List<DateRemark> supInfos) {
        this.supInfos = supInfos;
    }
}
