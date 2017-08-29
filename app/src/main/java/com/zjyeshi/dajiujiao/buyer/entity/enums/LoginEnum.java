package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 登录类型
 *
 * Created by wuhk on 2015/11/6.
 */
public enum LoginEnum {
    //1、卖家 2、买家 100、未知
    SELLER(1) , BURER(2) , UNKNOW(100);


    private int value;

    LoginEnum(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static LoginEnum valueOf(int userType) {
        switch (userType){
            case 1:
                return SELLER;
            case 2:
                return BURER;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case SELLER:
                return "卖家";
            case BURER:
                return "买家";
            default:
                return "未知";
        }
    };

    public boolean equals(LoginEnum loginEnum) {
        if (null == loginEnum) {
            return false;
        }

        return value == loginEnum.value;
    }
}
