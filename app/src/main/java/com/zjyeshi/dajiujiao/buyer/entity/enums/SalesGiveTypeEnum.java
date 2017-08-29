package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 活动优惠类型
 * Created by wuhk on 2017/5/8.
 */

public enum SalesGiveTypeEnum {
    //1-送礼品,2-返金额,3-减金额,4-送酒品
    GIVE_GIFT(1, "送礼品"), BACK_MONEY(2,"返金额") , CUT_MONEY(3,"减金额") , GIVE_WINE(4,"送酒品");

    private int value;
    private String name;

    private SalesGiveTypeEnum(int value, String name) {
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

    public static SalesGiveTypeEnum valueOf(int value) {
        SalesGiveTypeEnum result = GIVE_GIFT;
        for (SalesGiveTypeEnum e : SalesGiveTypeEnum.values()) {
            if (e.getValue() == value) {
                result = e;
            }
        }
        return result;
    }
}
