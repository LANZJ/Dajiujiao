package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.entity.my.work.LeaveRecord;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/6/20.
 */
public class LeaveRecordListData extends BaseData<LeaveRecordListData> {
    private List<LeaveRecord> list;

    public List<LeaveRecord> getList() {
        return list;
    }

    public void setList(List<LeaveRecord> list) {
        this.list = list;
    }

}
