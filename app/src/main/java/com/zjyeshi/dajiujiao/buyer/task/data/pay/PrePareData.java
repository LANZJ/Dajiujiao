package com.zjyeshi.dajiujiao.buyer.task.data.pay;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * Created by wuhk on 2016/1/6.
 */
public class PrePareData extends BaseData<PrePareData> {
    private String tradeNo;
    private String sign;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
