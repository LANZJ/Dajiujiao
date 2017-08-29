package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2017/4/26.
 */

public enum SalesTypeEnum {
//    1-实减金额,2-赠送金额,3-赠送商品
    CUT_MONEY(1, "实减金额"), GIVE_MONEY(2,"赠送金额") , GIVE_PRODUCT(3,"赠送商品") ;

    private int value;
    private String name;

    private SalesTypeEnum(int value, String name) {
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

    public static SalesTypeEnum valueOf(int value) {
        SalesTypeEnum result = CUT_MONEY;
        for (SalesTypeEnum e : SalesTypeEnum.values()) {
            if (e.getValue() == value) {
                result = e;
            }
        }
        return result;
    }
}
