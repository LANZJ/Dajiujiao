package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.entity.my.work.DateReport;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by zhum on 2016/6/23.
 */
public class DateReportData extends BaseData<DateReportData>{

    private List<DateReport> list;

    public List<DateReport> getList() {
        return list;
    }

    public void setList(List<DateReport> list) {
        this.list = list;
    }
}
