package com.zjyeshi.dajiujiao.buyer.task.data.store.homepage;

import java.io.Serializable;

/**
 * 首页商店信息
 *
 * Created by wuhk on 2015/9/28.
 */
public class Shop implements Serializable{
    private String id;
    private String pic ;
    private String name;
    private String level;
    private String inventory;
    private String distance;
    private String lngE5;
    private String latE5;
    private String productCount;
    private String member;
    private String deliverMinCost;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getDeliverMinCost() {
        return deliverMinCost;
    }

    public void setDeliverMinCost(String deliverMinCost) {
        this.deliverMinCost = deliverMinCost;
    }
}
