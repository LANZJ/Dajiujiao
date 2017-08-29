package com.zjyeshi.dajiujiao.buyer.task.data.order;

import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 订单商品信息
 *
 * Created by wuhk on 2015/11/3.
 */
public class OrderProduct implements Serializable{
    private String id;
    private String orderProductId;
    private String name ;
    private String pic;
    private String price;
    private String count;
    private String countUnit;
    private String unit;
    private String marketCost;
    private String bottlesPerBox;
    private String specifications;
    private boolean evaluated;
    private String boxType;
    private List<SalesListData.Sales> preferentialActivities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(String orderProductId) {
        this.orderProductId = orderProductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCountUnit() {
        return countUnit;
    }

    public void setCountUnit(String countUnit) {
        this.countUnit = countUnit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMarketCost() {
        return marketCost;
    }

    public void setMarketCost(String marketCost) {
        this.marketCost = marketCost;
    }

    public String getBottlesPerBox() {
        return bottlesPerBox;
    }

    public void setBottlesPerBox(String bottlesPerBox) {
        this.bottlesPerBox = bottlesPerBox;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public List<SalesListData.Sales> getPreferentialActivities() {
        return preferentialActivities;
    }

    public void setPreferentialActivities(List<SalesListData.Sales> preferentialActivities) {
        this.preferentialActivities = preferentialActivities;
    }
}
