package com.zjyeshi.dajiujiao.buyer.views.coupon;

/**
 * Created by wuhk on 2016/9/22.
 */
public class CouponEntity implements Comparable<CouponEntity> {
    private String id;
    private String fullMoney;
    private String disCountMoney;
    private String description;
    private String startTime;
    private String endTime;
    private boolean effective;

    public String getDisCountMoney() {
        return disCountMoney;
    }

    public void setDisCountMoney(String disCountMoney) {
        this.disCountMoney = disCountMoney;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullMoney() {
        return fullMoney;
    }

    public void setFullMoney(String fullMoney) {
        this.fullMoney = fullMoney;
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

    public boolean isEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    @Override
    public int compareTo(CouponEntity another) {
        return 0;
    }
}
