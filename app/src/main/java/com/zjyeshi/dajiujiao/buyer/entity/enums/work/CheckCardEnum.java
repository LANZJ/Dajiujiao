package com.zjyeshi.dajiujiao.buyer.entity.enums.work;

/**
 * 打卡情况
 * Created by wuhk on 2016/6/20.
 */
public enum CheckCardEnum {
    NOTCARD(1) , UPCARD(2) , DOWNCARD(3) , UNKNOW(-1);

    private int value;

    CheckCardEnum(int value) {
        this.value = value;
    }

    public static CheckCardEnum valueOf(int status) {
        switch (status){
            case 1:
                return NOTCARD;
            case 2:
                return UPCARD;
            case 3:
                return DOWNCARD;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case NOTCARD:
                return "未打卡";
            case UPCARD:
                return "上班已打卡";
            case DOWNCARD:
                return "下班已打卡";
            default:
                return "未知";
        }
    };

    public boolean equals(CheckCardEnum checkCardEnum) {
        if (null == checkCardEnum) {
            return false;
        }

        return value == checkCardEnum.value;
    }
}
