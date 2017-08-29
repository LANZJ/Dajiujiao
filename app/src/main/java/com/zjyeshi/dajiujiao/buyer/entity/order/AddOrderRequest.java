package com.zjyeshi.dajiujiao.buyer.entity.order;

import com.xuan.bigapple.lib.utils.Validators;

/**
 * Created by wuhk on 2016/12/27.
 */
public class AddOrderRequest {

    public static final String ORDERED_TYPE_NORMAL = "1";//正常下单
    public static final String ORDERED_TYPE_HELP = "2";//待下单

    public static final String BOX_TYPE_UNIT = "1";//单位
    public static final String BOX_TYPE_XIANG = "2";//箱

    private String memberId;
    private String orderId;
    private String productIds;
    private String prices;
    private String counts;
    private String boxType;
    private String markCostProductIds;
    private String markCostPrices;
    private String markCostCounts;
    private String markCostBoxType;
    private String sellerIds;
    private String addressId;
    private String orderType;
    private String remark;
    private String totalPrice;
    private boolean useMarketCost;
    private boolean editProduct;

    /**
     * 下单
     *
     * @param memberId
     * @param productIds
     * @param prices
     * @param counts
     * @param boxType
     * @param markCostProductIds
     * @param markCostPrices
     * @param markCostCounts
     * @param markCostBoxType
     * @param sellerIds
     * @param addressId
     * @param remark
     */
    public AddOrderRequest(String memberId, String productIds, String prices, String counts, String boxType, String markCostProductIds, String markCostPrices, String markCostCounts, String markCostBoxType, String sellerIds, String addressId, String remark) {
        if (!Validators.isEmpty(memberId)) {
            this.memberId = memberId;
            this.orderType = ORDERED_TYPE_HELP;
        } else {
            this.memberId = "";
            this.orderType = ORDERED_TYPE_NORMAL;
        }

        if (!Validators.isEmpty(productIds)) {
            this.productIds = productIds;
        } else {
            this.productIds = "";
        }

        if (!Validators.isEmpty(prices)) {
            this.prices = prices;
        } else {
            this.prices = "";
        }

        if (!Validators.isEmpty(counts)) {
            this.counts = counts;
        } else {
            this.counts = "";
        }

        if (!Validators.isEmpty(boxType)) {
            this.boxType = boxType;
        } else {
            this.boxType = "";
        }

        if (!Validators.isEmpty(markCostProductIds)) {
            this.markCostProductIds = markCostProductIds;
        } else {
            this.markCostProductIds = "";
        }

        if (!Validators.isEmpty(markCostPrices)) {
            this.markCostPrices = markCostPrices;
        } else {
            this.markCostPrices = "";
        }

        if (!Validators.isEmpty(markCostCounts)) {
            this.markCostCounts = markCostCounts;
        } else {
            this.markCostCounts = "";
        }

        if (!Validators.isEmpty(markCostBoxType)) {
            this.markCostBoxType = markCostBoxType;

        } else {
            this.markCostBoxType = "";

        }

        if (!Validators.isEmpty(sellerIds)) {
            this.sellerIds = sellerIds;
        } else {
            this.sellerIds = "";
        }

        if (!Validators.isEmpty(addressId)) {
            this.addressId = addressId;

        } else {
            this.addressId = "";

        }

        if (!Validators.isEmpty(remark)) {
            this.remark = remark;
        } else {
            this.remark = "";
        }

    }

    /**
     * 修改订单
     *
     * @param orderId
     * @param productIds
     * @param prices
     * @param counts
     * @param boxType
     * @param markCostProductIds
     * @param markCostPrices
     * @param markCostCounts
     * @param markCostBoxType
     * @param sellerIds
     * @param totalPrice
     */
    public AddOrderRequest(String orderId, String productIds, String prices, String counts, String boxType, String markCostProductIds, String markCostPrices, String markCostCounts, String markCostBoxType
            , String sellerIds, String totalPrice , boolean useMarketCost , boolean editProduct) {
        if (!Validators.isEmpty(orderId)) {
            this.orderId = orderId;
        } else {
            this.orderId = "";
        }

        if (!Validators.isEmpty(productIds)) {
            this.productIds = productIds;
        } else {
            this.productIds = "";
        }

        if (!Validators.isEmpty(prices)) {
            this.prices = prices;
        } else {
            this.prices = "";
        }

        if (!Validators.isEmpty(counts)) {
            this.counts = counts;
        } else {
            this.counts = "";
        }

        if (!Validators.isEmpty(boxType)) {
            this.boxType = boxType;
        } else {
            this.boxType = "";
        }

        if (!Validators.isEmpty(markCostProductIds)) {
            this.markCostProductIds = markCostProductIds;
        } else {
            this.markCostProductIds = "";
        }

        if (!Validators.isEmpty(markCostPrices)) {
            this.markCostPrices = markCostPrices;
        } else {
            this.markCostPrices = "";
        }

        if (!Validators.isEmpty(markCostCounts)) {
            this.markCostCounts = markCostCounts;
        } else {
            this.markCostCounts = "";
        }

        if (!Validators.isEmpty(markCostBoxType)) {
            this.markCostBoxType = markCostBoxType;

        } else {
            this.markCostBoxType = "";

        }

        if (!Validators.isEmpty(sellerIds)) {
            this.sellerIds = sellerIds;
        } else {
            this.sellerIds = "";
        }

        if (!Validators.isEmpty(totalPrice)) {
            this.totalPrice = totalPrice;

        } else {
            this.totalPrice = "";
        }

        this.useMarketCost = useMarketCost;

        this.editProduct = editProduct;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public String getMarkCostProductIds() {
        return markCostProductIds;
    }

    public void setMarkCostProductIds(String markCostProductIds) {
        this.markCostProductIds = markCostProductIds;
    }

    public String getMarkCostPrices() {
        return markCostPrices;
    }

    public void setMarkCostPrices(String markCostPrices) {
        this.markCostPrices = markCostPrices;
    }

    public String getMarkCostCounts() {
        return markCostCounts;
    }

    public void setMarkCostCounts(String markCostCounts) {
        this.markCostCounts = markCostCounts;
    }

    public String getMarkCostBoxType() {
        return markCostBoxType;
    }

    public void setMarkCostBoxType(String markCostBoxType) {
        this.markCostBoxType = markCostBoxType;
    }

    public String getSellerIds() {
        return sellerIds;
    }

    public void setSellerIds(String sellerIds) {
        this.sellerIds = sellerIds;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }


    public boolean isUseMarketCost() {
        return useMarketCost;
    }

    public void setUseMarketCost(boolean useMarketCost) {
        this.useMarketCost = useMarketCost;
    }

    public boolean isEditProduct() {
        return editProduct;
    }

    public void setEditProduct(boolean editProduct) {
        this.editProduct = editProduct;
    }
}
