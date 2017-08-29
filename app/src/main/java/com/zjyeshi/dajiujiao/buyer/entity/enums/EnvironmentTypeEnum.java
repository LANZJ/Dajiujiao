package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2017/4/26.
 */

public enum EnvironmentTypeEnum {
    //1-店铺活动,2-商品活动
    SHOP_SALES(1, "店铺活动"), PRODUCT_SALES(2,"商品活动");

    private int value;
    private String name;

    private EnvironmentTypeEnum(int value, String name) {
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

    public static EnvironmentTypeEnum valueOf(int value) {
        EnvironmentTypeEnum result = SHOP_SALES;
        for (EnvironmentTypeEnum e : EnvironmentTypeEnum.values()) {
            if (e.getValue() == value) {
                result = e;
            }
        }
        return result;
    }
}
