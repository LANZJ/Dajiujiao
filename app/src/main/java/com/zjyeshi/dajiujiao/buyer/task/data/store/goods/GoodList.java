package com.zjyeshi.dajiujiao.buyer.task.data.store.goods;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 商品列表
 * Created by wuhk on 2015/10/28.
 */
public class GoodList extends BaseData<GoodList> {
    private List<GoodData> list;

    public List<GoodData> getList() {
        return list;
    }

    public void setList(List<GoodData> list) {
        this.list = list;
    }
}
