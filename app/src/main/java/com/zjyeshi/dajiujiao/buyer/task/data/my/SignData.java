package com.zjyeshi.dajiujiao.buyer.task.data.my;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/3/2.
 */
public class SignData extends BaseData<SignData> {
    private List<SignRecord> list;

    public List<SignRecord> getList() {
        return list;
    }

    public void setList(List<SignRecord> list) {
        this.list = list;
    }
}
