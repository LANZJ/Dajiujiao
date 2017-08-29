package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2017/5/9.
 */

public enum SalesFormTypeEnum {
    //1-送礼品,2-返金额,3-减金额,4-送酒品
    FILL(1, "购买满 "), EVERY(2,"每购满");

    private int value;
    private String name;

    private SalesFormTypeEnum(int value, String name) {
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

    public static SalesFormTypeEnum valueOf(int value) {
        SalesFormTypeEnum result = FILL;
        for (SalesFormTypeEnum e : SalesFormTypeEnum.values()) {
            if (e.getValue() == value) {
                result = e;
            }
        }
        return result;
    }
}
