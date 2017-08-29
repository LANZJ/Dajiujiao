package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2016/7/11.
 */
public enum  ApproveEnum {
    //1、请假审批 ， 2、费用申请审批 ， 3、费用报销审批 ， 4、最高级别 , -1、全部
    LEAVE(1) , APPLYMONEY(2) , BXMONEY(3) , MAXLEVEL(4) ,  ALL(-1) , UNKNOW(100);

    private int value;

    ApproveEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ApproveEnum valueOf(int type) {
        switch (type){
            case 1:
                return LEAVE;
            case 2:
                return APPLYMONEY;
            case 3:
                return BXMONEY;
            case 4:
                return MAXLEVEL;
            case -1:
                return ALL;
            default:
                return UNKNOW;
        }
    }

    public boolean equals(ApproveEnum approveEnum) {
        if (null == approveEnum) {
            return false;
        }

        return value == approveEnum.value;
    }
}
