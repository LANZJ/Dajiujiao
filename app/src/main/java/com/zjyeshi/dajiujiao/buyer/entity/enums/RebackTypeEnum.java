package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2017/5/15.
 */

public enum  RebackTypeEnum {
    //1-退回活动礼品,2-不退回活动礼品
    REBACK_GIFT(2, "退回活动礼品"), NOT_REBACK_GOFT(1,"不退回活动礼品");

    private int value;
    private String name;

    private RebackTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.name();
    }

    public static RebackTypeEnum valueOf(int value) {
        RebackTypeEnum result = REBACK_GIFT;
        for (RebackTypeEnum e : RebackTypeEnum.values()) {
            if (e.getValue() == value) {
                result = e;
            }
        }
        return result;
    }
}
