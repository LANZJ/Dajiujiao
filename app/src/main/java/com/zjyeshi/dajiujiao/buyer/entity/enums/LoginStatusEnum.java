package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2015/11/25.
 */
public enum LoginStatusEnum {
    //1、登录 2、退出 100、未知
    LOGIN(1) , LOGOUT(2) , UNKNOW(100);

    private int value;

    LoginStatusEnum(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static LoginStatusEnum valueOf(int userType) {
        switch (userType){
            case 1:
                return LOGIN;
            case 2:
                return LOGOUT;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case LOGIN:
                return "登录";
            case LOGOUT:
                return " 退出";
            default:
                return "未知";
        }
    };

    public boolean equals(LoginStatusEnum loginStatusEnum) {
        if (null == loginStatusEnum) {
            return false;
        }

        return value == loginStatusEnum.value;
    }
}
