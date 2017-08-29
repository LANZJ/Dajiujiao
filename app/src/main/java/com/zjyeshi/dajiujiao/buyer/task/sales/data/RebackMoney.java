package com.zjyeshi.dajiujiao.buyer.task.sales.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * Created by wuhk on 2017/5/14.
 */

public class RebackMoney extends BaseData<RebackMoney> {
    private String rebackOrderTotalAmount;
    private String rebackOrderMarketCostTotalAmount;

    public String getRebackOrderTotalAmount() {
        return rebackOrderTotalAmount;
    }

    public void setRebackOrderTotalAmount(String rebackOrderTotalAmount) {
        this.rebackOrderTotalAmount = rebackOrderTotalAmount;
    }

    public String getRebackOrderMarketCostTotalAmount() {
        return rebackOrderMarketCostTotalAmount;
    }

    public void setRebackOrderMarketCostTotalAmount(String rebackOrderMarketCostTotalAmount) {
        this.rebackOrderMarketCostTotalAmount = rebackOrderMarketCostTotalAmount;
    }
}
