package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.entity.my.work.CostRecord;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/6/22.
 */
public class CostPaidRecordListData extends BaseData<CostPaidRecordListData> {
    private List<CostRecord> list;

    public List<CostRecord> getList() {
        return list;
    }

    public void setList(List<CostRecord> list) {
        this.list = list;
    }
}
