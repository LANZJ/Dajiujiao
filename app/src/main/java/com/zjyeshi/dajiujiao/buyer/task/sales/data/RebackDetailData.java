package com.zjyeshi.dajiujiao.buyer.task.sales.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;

import java.util.List;

/**
 * Created by wuhk on 2017/5/17.
 */

public class RebackDetailData extends BaseData<RebackDetailData> {
    private String id;
    private String orderId;
    private String returnAmount;
    private String returnMarketCostAmount;
    private String marketCost;
    private String pics;
    private String returnReason;
    private String giftName;
    private String productName;
    private int orderType;//0、线上 1、线下 2、未选择
    private int status;//退货管理状态
    private int type;//1、默认退钱 , 2、退还礼品
    private List<OrderProduct> products;
    private List<OrderProduct> marketCostProducts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(String returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getReturnMarketCostAmount() {
        return returnMarketCostAmount;
    }

    public void setReturnMarketCostAmount(String returnMarketCostAmount) {
        this.returnMarketCostAmount = returnMarketCostAmount;
    }

    public String getMarketCost() {
        return marketCost;
    }

    public void setMarketCost(String marketCost) {
        this.marketCost = marketCost;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    public List<OrderProduct> getMarketCostProducts() {
        return marketCostProducts;
    }

    public void setMarketCostProducts(List<OrderProduct> marketCostProducts) {
        this.marketCostProducts = marketCostProducts;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
