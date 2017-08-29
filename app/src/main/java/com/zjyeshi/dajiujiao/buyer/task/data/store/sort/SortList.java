package com.zjyeshi.dajiujiao.buyer.task.data.store.sort;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 分类列表
 *
 * Created by wuhk on 2015/10/28.
 */
public class SortList extends BaseData<SortList> {
    private List<SortData> list;

    public List<SortData> getList() {
        return list;
    }

    public void setList(List<SortData> list) {
        this.list = list;
    }
}

