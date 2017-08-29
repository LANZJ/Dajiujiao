package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;

import java.util.List;

/**
 * Created by wuhk on 2016/12/14.
 */
public class CanBuyData extends BaseData<CanBuyData> {
    private List<Product> list;

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }
}
