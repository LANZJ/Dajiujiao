package com.zjyeshi.dajiujiao.buyer.entity.store;

import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;

import java.util.List;

/**
 * 购物车
 * Created by whk on 2015/10/16.
 */
public class GoodCar {

    private boolean isSelected;
    private String type;
    private String storeId;
    private String storeName;
    private List<GoodsCar> goodList;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<GoodsCar> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<GoodsCar> goodList) {
        this.goodList = goodList;
    }

}


