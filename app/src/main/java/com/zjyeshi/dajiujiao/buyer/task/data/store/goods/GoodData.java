package com.zjyeshi.dajiujiao.buyer.task.data.store.goods;

import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;

import java.util.List;

/**
 * 商品信息
 *
 * Created by wuhk on 2015/10/27.
 */
public class GoodData {
    private Product product;
    private List<SalesListData.Sales> preferentialActivities;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<SalesListData.Sales> getPreferentialActivities() {
        return preferentialActivities;
    }

    public void setPreferentialActivities(List<SalesListData.Sales> preferentialActivities) {
        this.preferentialActivities = preferentialActivities;
    }
}
