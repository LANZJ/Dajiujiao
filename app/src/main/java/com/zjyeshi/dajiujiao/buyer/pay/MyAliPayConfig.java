package com.zjyeshi.dajiujiao.buyer.pay;

import com.zjyeshi.dajiujiao.buyer.pay.aliutils.AliPayConfig;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;

/**
 * Created by wuhk on 2015/12/18.
 */
public class MyAliPayConfig extends AliPayConfig {
    @Override
    public String getPartner() {
        return "2088121450647747";
    }

    @Override
    public String getSellerId() {
        return "3327588690@qq.com";
    }

    @Override
    public String getNotifyUrl() {
        return UrlConstants.PAY_WEB_SITE + "pay/doAlipay.htm";
    }
}
