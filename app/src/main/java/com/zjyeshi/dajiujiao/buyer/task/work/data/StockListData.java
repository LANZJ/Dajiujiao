package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Stock;

import java.util.List;

/**
 * Created by wuhk on 2016/6/23.
 */
public class StockListData extends BaseData<StockListData> {
    private List<Stock> list;

    public List<Stock> getList() {
        return list;
    }

    public void setList(List<Stock> list) {
        this.list = list;
    }
}
