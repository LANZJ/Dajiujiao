package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 支付方式
 * Created by wuhk on 2015/11/26.
 */
public enum PayTypeEnum{
    // 1、微信支付 2、支付宝快捷支付 3、 银联支付 4、线下支付 100、未知
    WEIXIN(1), ZHIFUBAO(2) ,YINLIAN(3) ,XIANXIA(4) , UNKNOW(100);

    private int value;

    private PayTypeEnum(int type) {
        this.value = type;
    }

    public int getValue() {
        return value;
    }

    public static PayTypeEnum valueOf(int selectType) {
        switch (selectType){
            case 1:
                return WEIXIN;
            case 2:
                return ZHIFUBAO;
            case 3:
                return YINLIAN;
            case 4:
                return XIANXIA;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case WEIXIN:
                return "微信支付";
            case ZHIFUBAO:
                return "支付宝支付";
            case YINLIAN:
                return "银联支付";
            case XIANXIA:
                return "线下支付";
            default:
                return "未知";
        }
    };

    public boolean equals(PayTypeEnum payTypeEnum) {
        if (null == payTypeEnum) {
            return false;
        }

        return value == payTypeEnum.value;
    }
}
