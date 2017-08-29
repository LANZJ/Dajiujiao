package com.zjyeshi.dajiujiao.buyer.pay.wxutils;

/**
 * 微信配置
 * Created by wuhk on 2015/12/21.
 */
public abstract class WxPayConfig {
    /**
     * appId
     *
     * @return
     */
    public abstract String getAppId();

    /**
     * 服务器调用url
     *
     * @return
     */
    public abstract String getUrl();
}
