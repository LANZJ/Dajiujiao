package com.zjyeshi.dajiujiao.buyer.entity;

import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车商品实体类
 *
 * Created by wuhk on 2015/10/29.
 */
public class GoodsCar implements Serializable{
    private String shopId;
    private String goodId;
    private String shopName;
    private String goodName;
    private String goodIcon;
    private String goodCount;
    private String goodType;
    private String goodPrice;
    private String goodMarketPrice;
    private String goodBottole;
    private int status;
    private String goodUpPrice;
    private List<SalesListData.Sales> salesList;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodIcon() {
        return goodIcon;
    }

    public void setGoodIcon(String goodIcon) {
        this.goodIcon = goodIcon;
    }

    public String getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(String goodCount) {
        this.goodCount = goodCount;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public String getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(String goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getGoodMarketPrice() {
        return goodMarketPrice;
    }

    public void setGoodMarketPrice(String goodMarketPrice) {
        this.goodMarketPrice = goodMarketPrice;
    }

    public String getGoodBottole() {
        return goodBottole;
    }

    public void setGoodBottole(String goodBottole) {
        this.goodBottole = goodBottole;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGoodUpPrice() {
        return goodUpPrice;
    }

    public void setGoodUpPrice(String goodUpPrice) {
        this.goodUpPrice = goodUpPrice;
    }

    public List<SalesListData.Sales> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<SalesListData.Sales> salesList) {
        this.salesList = salesList;
    }
}
