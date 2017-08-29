package com.zjyeshi.dajiujiao.buyer.entity.order;

import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;

/**
 * Created by wuhk on 2016/9/12.
 */
public class OrderHeadEntity extends BaseEntity {
    private String sort;
    private String amount;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
