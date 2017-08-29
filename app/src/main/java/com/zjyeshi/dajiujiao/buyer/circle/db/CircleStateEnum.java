package com.zjyeshi.dajiujiao.buyer.circle.db;

/**
 * 圈子枚举
 *
 * Created by xuan on 15/11/5.
 */
public enum CircleStateEnum {
    // 1、发送中 2、发送成功 3、发送失败
    ING(1), SUCCESS(2) ,FAIL(3);

    private int value;

    private CircleStateEnum(int type) {
        this.value = type;
    }

    public int getValue() {
        return value;
    }

    public String getValueStr(){
        return String.valueOf(value);
    }

    public static CircleStateEnum valueOf(int value) {
        switch (value){
            case 1:
                return ING;
            case 2:
                return SUCCESS;
            case 3:
                return FAIL;
            default:
                return SUCCESS;
        }
    }

    public boolean equals(CircleStateEnum circleStateEnum) {
        if (null == circleStateEnum) {
            return false;
        }

        return value == circleStateEnum.value;
    }

}
