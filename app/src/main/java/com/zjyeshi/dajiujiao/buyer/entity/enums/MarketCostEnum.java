package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2017/5/2.
 */

public enum MarketCostEnum {
    // 1.无，2.立返，3.月返，季返，年返。4.次单返
    NO_SUPPORT(1, "无市场费用"), REBACK_NOW(2,"立返")  , REBACK_OTHER(3 , "其他返"),REBACK_OTE(4,"次单返");

    private int value;
    private String name;

    private MarketCostEnum(int value, String name) {
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

    public static MarketCostEnum valueOf(int value) {
        MarketCostEnum result = NO_SUPPORT;
        for (MarketCostEnum e : MarketCostEnum.values()) {
            if (e.getValue() == value) {
                result = e;
            }
        }
        return result;
    }
}
