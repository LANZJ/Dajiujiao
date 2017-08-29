package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 购物车选择时全选和全灭
 *
 * Created by wuhk on 2015/10/21.
 */
public enum ALLEnum {
    // 1、全选 2、全灭 3、未知
    All(1), NONE(2) ,UNKNOW(3);

    private int value;

    private ALLEnum(int type) {
        this.value = type;
    }

    public int getValue() {
        return value;
    }

    public static ALLEnum valueOf(int allType) {
        switch (allType){
            case 1:
                return All;
            case 2:
                return NONE;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case All:
                return "全选";
            case NONE:
                return "全灭";
            default:
                return "未知";
        }
    };

    public boolean equals(ALLEnum allEnum) {
        if (null == allEnum) {
            return false;
        }

        return value == allEnum.value;
    }
}
