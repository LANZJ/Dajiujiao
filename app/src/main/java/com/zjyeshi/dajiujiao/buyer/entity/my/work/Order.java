package com.zjyeshi.dajiujiao.buyer.entity.my.work;

import java.io.Serializable;

/**
 * 订单管理--订单
 *
 *
 * Created by zhum on 2016/6/15.
 */
public class Order implements Serializable{
    private String id;
    private String pic;
    private String shopName;
    private String amount;
    private String name;
    private String phone;
    private int orderCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
