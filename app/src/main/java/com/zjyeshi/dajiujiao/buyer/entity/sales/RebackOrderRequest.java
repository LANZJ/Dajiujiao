package com.zjyeshi.dajiujiao.buyer.entity.sales;

/**
 * Created by wuhk on 2017/5/14.
 */

public class RebackOrderRequest {
    private String orderId;
    private int type;
    private String returnReason;
    private String pics;

    private String orderProductIds;
    private String nums;
    private String boxType;

    private String markCostOrderProductIds;
    private String markCostNums;
    private String markCostBoxTypes;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getOrderProductIds() {
        return orderProductIds;
    }

    public void setOrderProductIds(String orderProductIds) {
        this.orderProductIds = orderProductIds;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }



    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getMarkCostOrderProductIds() {
        return markCostOrderProductIds;
    }

    public void setMarkCostOrderProductIds(String markCostOrderProductIds) {
        this.markCostOrderProductIds = markCostOrderProductIds;
    }

    public String getMarkCostNums() {
        return markCostNums;
    }

    public void setMarkCostNums(String markCostNums) {
        this.markCostNums = markCostNums;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public String getMarkCostBoxTypes() {
        return markCostBoxTypes;
    }

    public void setMarkCostBoxTypes(String markCostBoxTypes) {
        this.markCostBoxTypes = markCostBoxTypes;
    }
}
