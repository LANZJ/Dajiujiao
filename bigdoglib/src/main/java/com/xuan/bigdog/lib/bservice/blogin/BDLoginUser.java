package com.xuan.bigdog.lib.bservice.blogin;


import com.xuan.bigdog.lib.bservice.bcommon.BServiceBaseData;

/**
 * 登录用户
 *
 * Created by xuan on 16/2/16.
 */
public class BDLoginUser extends BServiceBaseData<BDLoginUser> {
    private String userId;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
