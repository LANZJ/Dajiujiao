package com.zjyeshi.dajiujiao.buyer.task.data.my;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * Created by wuhk on 2016/9/29.
 */
public class RecommendCouponData extends BaseData<RecommendCouponData> {
    private String id;
    private int type;//优惠类型
    private String fulfilMoney;//使用满额度
    private String minusMoney;//减免金额
    private String disCount;//折扣
    private String startTime;//时间期限
    private String endTime;
    private int payType;//支付方式优惠类型，1、线上支付，2、线下支付
    private int environmentType;//应用环境类型，1、全场 ，2、店铺 3、商品
    private String environmentName;//应用环境名称
    private boolean whetherUse;//是否可用

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFulfilMoney() {
        return fulfilMoney;
    }

    public void setFulfilMoney(String fulfilMoney) {
        this.fulfilMoney = fulfilMoney;
    }

    public String getMinusMoney() {
        return minusMoney;
    }

    public void setMinusMoney(String minusMoney) {
        this.minusMoney = minusMoney;
    }

    public String getDisCount() {
        return disCount;
    }

    public void setDisCount(String disCount) {
        this.disCount = disCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(int environmentType) {
        this.environmentType = environmentType;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public boolean isWhetherUse() {
        return whetherUse;
    }

    public void setWhetherUse(boolean whetherUse) {
        this.whetherUse = whetherUse;
    }
}
