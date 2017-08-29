package com.zjyeshi.dajiujiao.buyer.task.data.pay;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * Created by wuhk on 2016/1/13.
 */
public class WxPayData extends BaseData<WxPayData> {
    private String tradeNo;
    private AppReqData appReqData;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public AppReqData getAppReqData() {
        return appReqData;
    }

    public void setAppReqData(AppReqData appReqData) {
        this.appReqData = appReqData;
    }
}
