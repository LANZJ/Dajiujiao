package com.zjyeshi.dajiujiao.buyer.pay.aliutils;

/**
 * 支付配置
 * Created by wuhk on 2015/12/18.
 */
public abstract class AliPayConfig {

    /**
     * 商户PID
     *
     * @return
     */
    public abstract String getPartner();

    /**
     * 商户收款账号
     *
     * @return
     */
    public abstract String getSellerId();

    /**
     * 服务器异步通知页面路径
     * @return
     */
    public abstract String getNotifyUrl();
}
