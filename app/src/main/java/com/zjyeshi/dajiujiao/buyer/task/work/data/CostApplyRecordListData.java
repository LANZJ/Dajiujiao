package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.AskMoneyRecord;

import java.util.List;

/**
 * Created by wuhk on 2016/6/22.
 */
public class CostApplyRecordListData extends BaseData<CostApplyRecordListData> {
    private List<AskMoneyRecord> list;

    public List<AskMoneyRecord> getList() {
        return list;
    }

    public void setList(List<AskMoneyRecord> list) {
        this.list = list;
    }
}
