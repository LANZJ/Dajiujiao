package com.zjyeshi.dajiujiao.buyer.entity.my.work;

import java.io.Serializable;

/**
 * 库存
 *
 * Created by zhum on 2016/6/15.
 */
public class Stock implements Serializable ,Comparable<Stock>{
    private String id;//memberId
    private String shopId;//店铺Id
    private String shopPic;
    private String shopName;
    private String inventory;
    private Integer kc;

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

    public String getShopPic() {
        return shopPic;
    }

    public void setShopPic(String shopPic) {
        this.shopPic = shopPic;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public Integer getKc() {
        return kc;
    }

    public void setKc(Integer kc) {
        this.kc = kc;
    }

    @Override
    public int compareTo(Stock another) {
        return this.getKc().compareTo(another.getKc());
    }
}
