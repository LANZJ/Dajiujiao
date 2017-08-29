package com.zjyeshi.dajiujiao.buyer.entity.my;

import java.io.Serializable;

/**
 * 兑换记录
 *
 * Created by wuhk on 2015/10/19.
 */
public class ChangeRecordData implements Serializable {
    private String time ;
    private String result;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
