package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2017/5/16.
 */

public enum RebackManagerStatusEnum {
    //1-退回活动礼品,2-不退回活动礼品
    WAIT_REVIEW(1, "待审核"), PASS_REVIEW(2,"审核通过") , REFUSE_REVIEW(3 , "审核拒绝") , CLOSE_REVIEW(5 , "关闭申请");

    private int value;
    private String name;

    private RebackManagerStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.name();
    }

    public static RebackManagerStatusEnum valueOf(int value) {
        RebackManagerStatusEnum result = WAIT_REVIEW;
        for (RebackManagerStatusEnum e : RebackManagerStatusEnum.values()) {
            if (e.getValue() == value) {
                result = e;
            }
        }
        return result;
    }
}
