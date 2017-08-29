package com.xuan.bigdog.lib.bservice.bregister;


import com.xuan.bigdog.lib.bservice.bcommon.BServiceBaseData;

/**
 * 注册返回
 * Created by wuhk on 2016/3/14.
 */
public class BDRegisterData extends BServiceBaseData<BDRegisterData> {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
