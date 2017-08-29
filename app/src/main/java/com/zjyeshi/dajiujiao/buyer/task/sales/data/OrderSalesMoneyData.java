package com.zjyeshi.dajiujiao.buyer.task.sales.data;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;

/**
 * Created by wuhk on 2017/5/19.
 */

public class OrderSalesMoneyData extends BaseData<OrderSalesMoneyData> {
    private String receivedPrice;
    private String subPrice;
    private String productName;
    private String giftName;

    public String getReceivedPrice() {
        return receivedPrice;
    }

    public void setReceivedPrice(String receivedPrice) {
        if (!Validators.isEmpty(receivedPrice)){
            receivedPrice = ExtraUtil.format(ExtraUtil.getShowPrice(receivedPrice));
        }
        this.receivedPrice = receivedPrice;
    }

    public String getSubPrice() {
        return subPrice;
    }

    public void setSubPrice(String subPrice) {
        if (!Validators.isEmpty(subPrice)){
            subPrice = ExtraUtil.format(ExtraUtil.getShowPrice(subPrice));
        }
        this.subPrice = subPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }
}
