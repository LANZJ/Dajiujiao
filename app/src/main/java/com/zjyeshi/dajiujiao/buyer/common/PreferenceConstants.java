package com.zjyeshi.dajiujiao.buyer.common;

import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;

/**
 * 参数常量保存类
 *
 * @author wuhk
 */
public class PreferenceConstants {
    /**
     * 最近登录用户
     */
    public static final String LAST_LOGIN_USER_INFO = "last.login.user.info";

    public static final String CAR_MARKET_COST = LoginedUser.getLoginedUser().getId() + "car_market_cost";

    public static final String MY_ACCOUNT_MARKET = LoginedUser.getLoginedUser().getId() + "my_account_market";

    public static final String SHOP_ACTIVITIES = LoginedUser.getLoginedUser().getId() + "activities";

    public static String REQUEST_MSG = "request_msg";

    public static String getShopActivitiesKey(String shopId){
        return shopId + SHOP_ACTIVITIES;
    }

}
