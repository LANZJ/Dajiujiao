package com.zjyeshi.dajiujiao.buyer.entity.sales;

/**
 * 添加活动信息
 * Created by wuhk on 2017/4/26.
 */

public class AddSalesRequest {
    private String id;
    private String shopId;
    private String shopIds;
    private String productIds;
    private int satisfyType;
    private int favouredType;
    private String satisfyCondition;
    private String favouredPolicy;
    private String startTime;
    private String endTime;
    private int formType;
    private String giftId;
    private String url;
    private boolean superposition;
    private int priority;
    private String giveProductId;
    private String giveProductNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopIds() {
        return shopIds;
    }

    public void setShopIds(String shopIds) {
        this.shopIds = shopIds;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public int getSatisfyType() {
        return satisfyType;
    }

    public void setSatisfyType(int satisfyType) {
        this.satisfyType = satisfyType;
    }

    public int getFavouredType() {
        return favouredType;
    }

    public void setFavouredType(int favouredType) {
        this.favouredType = favouredType;
    }

    public String getSatisfyCondition() {
        return satisfyCondition;
    }

    public void setSatisfyCondition(String satisfyCondition) {
        this.satisfyCondition = satisfyCondition;
    }

    public String getFavouredPolicy() {
        return favouredPolicy;
    }

    public void setFavouredPolicy(String favouredPolicy) {
        this.favouredPolicy = favouredPolicy;
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

    public int getFormType() {
        return formType;
    }

    public void setFormType(int formType) {
        this.formType = formType;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuperposition() {
        return superposition;
    }

    public void setSuperposition(boolean superposition) {
        this.superposition = superposition;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getGiveProductId() {
        return giveProductId;
    }

    public void setGiveProductId(String giveProductId) {
        this.giveProductId = giveProductId;
    }

    public String getGiveProductNum() {
        return giveProductNum;
    }

    public void setGiveProductNum(String giveProductNum) {
        this.giveProductNum = giveProductNum;
    }
}
