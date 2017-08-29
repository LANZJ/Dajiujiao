package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.entity.my.work.CompanyStock;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/6/23.
 */
public class CompanyStockListData extends BaseData<CompanyStockListData> {
    private List<CompanyStock> list;

    public List<CompanyStock> getList() {
        return list;
    }

    public void setList(List<CompanyStock> list) {
        this.list = list;
    }
}
