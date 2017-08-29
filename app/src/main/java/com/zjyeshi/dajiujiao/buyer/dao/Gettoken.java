package com.zjyeshi.dajiujiao.buyer.dao;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * Created by lan on 2017/7/6.
 */
public class Gettoken extends BaseData<Gettoken> {
    private String userId;
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }


}
