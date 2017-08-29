package com.zjyeshi.dajiujiao.buyer.task.data.order;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 订单列表
 *
 * Created by wuhk on 2015/11/3.
 */
public class GetOrderListData extends BaseData<GetOrderListData> {
    private List<PerOrder> list;

    public List<PerOrder> getList() {
        return list;
    }

    public void setList(List<PerOrder> list) {
        this.list = list;
    }
}
