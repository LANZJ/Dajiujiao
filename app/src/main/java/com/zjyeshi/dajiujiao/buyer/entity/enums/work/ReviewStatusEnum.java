package com.zjyeshi.dajiujiao.buyer.entity.enums.work;

/**
 * 审批状态
 * Created by wuhk on 2016/6/20.
 */
public enum ReviewStatusEnum {
    //1、待审核 ， 2、审核通过 3、审核拒绝 ， -1、未知
    WAIT(1) , PASS(2) ,REFUSE(3) , UNKNOW(-1);

    private int value;

    public int getValue() {
        return value;
    }

    ReviewStatusEnum(int value) {
        this.value = value;
    }


    public static ReviewStatusEnum valueOf(int status) {
        switch (status){
            case 1:
                return WAIT;
            case 2:
                return PASS;
            case 3:
                return REFUSE;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case WAIT:
                return "待审核";
            case PASS:
                return "审核通过";
            case REFUSE:
                return "审核拒绝";
            default:
                return "未知";
        }
    };

    public boolean equals(ReviewStatusEnum leaveProgressEnum) {
        if (null == leaveProgressEnum) {
            return false;
        }

        return value == leaveProgressEnum.value;
    }
}
