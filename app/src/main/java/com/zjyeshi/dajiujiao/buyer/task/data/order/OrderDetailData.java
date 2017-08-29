package com.zjyeshi.dajiujiao.buyer.task.data.order;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.Date;
import java.util.List;

/**
 * Created by wuhk on 2015/11/6.
 */
public class OrderDetailData extends BaseData<OrderDetailData> {
    private String id;
    private String number;
    private String buyerId;//购买人Id
    private String buyer;
    private String buyerShop;
    private String salesman;
    private String rebackId;
    private int status;
    private int confirmStatus;
    private String amount;
    private String marketCostAmount;
    private boolean dealWith;//是否能处理
    private int type;//线上线下支付，0线下
    private String walletCost;//钱包支付
    private String couponsCost;//红包支付
    private String otherCost;//第三方支付
    private String marketCost;//市场支持费用
    private Date creationTime;
    private String shopId;
    private String shoName;
    private String remark;
    private String deliveryRemark;//发货备注
    private boolean modifyDeliveryRemark;//发货备注能否修改
    private List<OrderProduct> products;
    private List<OrderProduct> marketCostProducts;
    private Address address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBuyerShop() {
        return buyerShop;
    }

    public void setBuyerShop(String buyerShop) {
        this.buyerShop = buyerShop;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRebackId() {
        return rebackId;
    }

    public void setRebackId(String rebackId) {
        this.rebackId = rebackId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(int confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public boolean isDealWith() {
        return dealWith;
    }

    public void setDealWith(boolean dealWith) {
        this.dealWith = dealWith;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMarketCostAmount() {
        return marketCostAmount;
    }

    public void setMarketCostAmount(String marketCostAmount) {
        this.marketCostAmount = marketCostAmount;
    }

    public String getWalletCost() {
        return walletCost;
    }

    public void setWalletCost(String walletCost) {
        this.walletCost = walletCost;
    }

    public String getCouponsCost() {
        return couponsCost;
    }

    public void setCouponsCost(String couponsCost) {
        this.couponsCost = couponsCost;
    }

    public String getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(String otherCost) {
        this.otherCost = otherCost;
    }

    public String getMarketCost() {
        return marketCost;
    }

    public void setMarketCost(String marketCost) {
        this.marketCost = marketCost;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getShoName() {
        return shoName;
    }

    public void setShoName(String shoName) {
        this.shoName = shoName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeliveryRemark() {
        return deliveryRemark;
    }

    public void setDeliveryRemark(String deliveryRemark) {
        this.deliveryRemark = deliveryRemark;
    }

    public boolean isModifyDeliveryRemark() {
        return modifyDeliveryRemark;
    }

    public void setModifyDeliveryRemark(boolean modifyDeliveryRemark) {
        this.modifyDeliveryRemark = modifyDeliveryRemark;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static class  Address{
        private String name;
        private String phone;
        private String address;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }


}
