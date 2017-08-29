package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 省市区选择
 * Created by wuhk on 2015/11/22.
 */
public enum  ProvinceEnum {
    // 1、省 2、市 3、区 100、未知
    PROVINCE(1), DISTRICT(2) ,CITY(3) , UNKNOW(100);

    private int value;

    private ProvinceEnum(int type) {
        this.value = type;
    }

    public int getValue() {
        return value;
    }

    public static ProvinceEnum valueOf(int selectType) {
        switch (selectType){
            case 1:
                return PROVINCE;
            case 2:
                return DISTRICT;
            case 3:
                return CITY;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case PROVINCE:
                return "province";
            case DISTRICT:
                return "district";
            case CITY:
                return "city";
            default:
                return "未知";
        }
    };

    public boolean equals(ProvinceEnum provinceEnum) {
        if (null == provinceEnum) {
            return false;
        }

        return value == provinceEnum.value;
    }
}
