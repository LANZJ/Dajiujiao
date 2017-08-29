package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 活动满足条件
 * Created by wuhk on 2017/5/8.
 */

public enum SalesFillTypeEnum {
    //1-满额度,2-满箱数,3-混合满箱
    FILL_MONEY(1, "满额度"), FILL_BOX(2,"单品满箱数") , MIX_BOX(3,"组合满箱数") ;

    private int value;
    private String name;

    private SalesFillTypeEnum(int value, String name) {
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

    public static SalesFillTypeEnum valueOf(int value) {
        SalesFillTypeEnum result = FILL_MONEY;
        for (SalesFillTypeEnum e : SalesFillTypeEnum.values()) {
            if (e.getValue() == value) {
                result = e;
            }
        }
        return result;
    }
}
