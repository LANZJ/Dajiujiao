package com.zjyeshi.dajiujiao.buyer.views.pay;

import com.zjyeshi.dajiujiao.buyer.entity.enums.PayTypeEnum;

/**
 * 支付类型实体类
 * Created by wuhk on 2016/9/22.
 */
public class PayTypeEntity {
    private PayTypeEnum payEnum;
    private String payType;
    private String payDes;
    private int payIconId;

    private boolean selected;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayDes() {
        return payDes;
    }

    public void setPayDes(String payDes) {
        this.payDes = payDes;
    }

    public int getPayIconId() {
        return payIconId;
    }


    public void setPayEnum(PayTypeEnum payEnum) {
        this.payEnum = payEnum;
    }

    public void setPayIconId(int payIconId) {
        this.payIconId = payIconId;
    }

    public PayTypeEnum getPayEnum() {
        return payEnum;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
