package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 规格
 * Created by wuhk on 2016/6/28.
 */
public enum  FormatEnum {
    //1、单位 ，  2、箱  ，-1未知
    UNIT(1) , BOX(2) , UNKNOW(-1);
    private int value;

    FormatEnum(int value) {
        this.value = value;
    }

    public static FormatEnum valueOf(int type) {
        switch (type){
            case 1:
                return UNIT;
            case 2:
                return BOX;
            default:
                return UNKNOW;
        }
    }

    public boolean equals(FormatEnum formatEnum) {
        if (null == formatEnum) {
            return false;
        }

        return value == formatEnum.value;
    }


}
