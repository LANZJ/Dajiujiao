package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2016/9/11.
 */
public enum  PhoneUsedEnum {
    //1-账户不存在,2-账户存在,非好友,3-账户存在,好友
    NOTEXIT(1) , NOTFRIEND(2) , FRIEND(3) , UNKNOW(-1);

    private int value;

    PhoneUsedEnum(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static PhoneUsedEnum valueOf(int type) {
        switch (type){
            case 1:
                return NOTEXIT;
            case 2:
                return NOTFRIEND;
            case 3:
                return FRIEND;
            default:
                return UNKNOW;
        }
    }

    public boolean equals(PhoneUsedEnum phoneUsedEnum) {
        if (null == phoneUsedEnum) {
            return false;
        }

        return value == phoneUsedEnum.value;
    }
}
