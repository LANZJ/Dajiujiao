package com.zjyeshi.dajiujiao.buyer.pay;

import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.pay.wxutils.WxPayConfig;

/**
 * Created by wuhk on 2015/12/21.
 */
public class MyWxPayConfig extends WxPayConfig {
    @Override
    public String getAppId() {
        return "wx1fb4dadd7091346b";
    }

    @Override
    public String getUrl() {
        return UrlConstants.WXPAYPREPARE;
    }
}
