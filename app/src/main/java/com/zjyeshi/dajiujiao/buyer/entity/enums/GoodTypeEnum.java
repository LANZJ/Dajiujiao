package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2016/12/28.
 */
public enum GoodTypeEnum {
    // 1、常规购买 2、市场支持 100、未知
    NORMAL_BUY(1), MARKET_SUPPORT(2), UNKNOW(100);

    private int value;

    private GoodTypeEnum(int type) {
        this.value = type;
    }

    public int getValue() {
        return value;
    }

    public static GoodTypeEnum valueOf(int type) {
        switch (type) {
            case 1:
                return NORMAL_BUY;
            case 2:
                return MARKET_SUPPORT;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case NORMAL_BUY:
                return "常规购买";
            case MARKET_SUPPORT:
                return "市场支持";
            default:
                return "未知";
        }
    }

    ;

    public boolean equals(GoodTypeEnum goodTypeEnum) {
        if (null == goodTypeEnum) {
            return false;
        }

        return value == goodTypeEnum.value;
    }
}
