package com.zjyeshi.dajiujiao.buyer.task.data.seller;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * Created by wuhk on 2015/11/13.
 */
public class ShopManageData extends BaseData<ShopManageData> {
    private String name ;
    private String lngE5;
    private String latE5;
    private String address;
    private String openTime;
    private String closeTime;
    private String status;
    private boolean delivery;
    private String deliverMinCost;//起送价
    private String marketCost;//市场支持费用

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLngE5() {
        return lngE5;
    }

    public void setLngE5(String lngE5) {
        this.lngE5 = lngE5;
    }

    public String getLatE5() {
        return latE5;
    }

    public void setLatE5(String latE5) {
        this.latE5 = latE5;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public String getDeliverMinCost() {
        return deliverMinCost;
    }

    public void setDeliverMinCost(String deliverMinCost) {
        this.deliverMinCost = deliverMinCost;
    }

    public String getMarketCost() {
        return marketCost;
    }

    public void setMarketCost(String marketCost) {
        this.marketCost = marketCost;
    }
}
