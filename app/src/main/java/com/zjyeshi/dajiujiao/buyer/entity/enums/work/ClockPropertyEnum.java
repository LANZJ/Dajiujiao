package com.zjyeshi.dajiujiao.buyer.entity.enums.work;

/**
 * 打卡性质
 * Created by wuhk on 2016/6/20.
 */
public enum  ClockPropertyEnum {
    GO(1) , OFF(2) , UNKNOW(-1);

    private int value;

    public int getValue() {
        return value;
    }

    ClockPropertyEnum(int value) {
        this.value = value;
    }

    public static ClockPropertyEnum valueOf(int status) {
        switch (status){
            case 1:
                return GO;
            case 2:
                return OFF;
            default:
                return UNKNOW;
        }
    }

    public boolean equals(ClockPropertyEnum clockPropertyEnum) {
        if (null == clockPropertyEnum) {
            return false;
        }

        return value == clockPropertyEnum.value;
    }
}
