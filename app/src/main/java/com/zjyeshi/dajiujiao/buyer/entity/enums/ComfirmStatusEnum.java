package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2016/7/20.
 */
public enum  ComfirmStatusEnum {
    //1、正常状态 ， 3、Boss确认
    NORMAL(1) , BOSS(3) , UNKNOW(-1);

    private int value;

    ComfirmStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
