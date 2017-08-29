package com.zjyeshi.dajiujiao.buyer.task.data.order;

import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 单个订单
 *
 * Created by wuhk on 2015/11/4.
 */
public class PerOrder extends BaseEntity implements Serializable{
    private String id;
    private String number;
    private int confirmStatus;//修改确认状态
    private String status;
    private int type;
    private boolean dealWith;//能够处理，true可以处理，false不可以处理
    private String amount;
    private String marketCostAmount;
    private long createTime;
    private String shopId;
    private String shopName;
    private String buyer;//消费者姓名
    private String buyerShopName;//消费者店铺名
    private String salesman;//相关业务员
    private boolean replaceOrder;//是否是代为下单
    private boolean canRevoke;//能够撤销转审
    private List<OrderProduct> productResp;
    private List<OrderProduct> marketCostProductResp;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<OrderProduct> getProductResp() {
        return productResp;
    }

    public void setProductResp(List<OrderProduct> productResp) {
        this.productResp = productResp;
    }

    public List<OrderProduct> getMarketCostProductResp() {
        return marketCostProductResp;
    }

    public void setMarketCostProductResp(List<OrderProduct> marketCostProductResp) {
        this.marketCostProductResp = marketCostProductResp;
    }

    public int getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(int confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBuyerShopName() {
        return buyerShopName;
    }

    public void setBuyerShopName(String buyerShopName) {
        this.buyerShopName = buyerShopName;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public boolean isReplaceOrder() {
        return replaceOrder;
    }

    public void setReplaceOrder(boolean replaceOrder) {
        this.replaceOrder = replaceOrder;
    }

    public boolean isCanRevoke() {
        return canRevoke;
    }

    public void setCanRevoke(boolean canRevoke) {
        this.canRevoke = canRevoke;
    }
}
