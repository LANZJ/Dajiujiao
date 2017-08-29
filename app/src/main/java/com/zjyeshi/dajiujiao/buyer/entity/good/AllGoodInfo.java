package com.zjyeshi.dajiujiao.buyer.entity.good;

import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;

import java.io.Serializable;
import java.util.List;

/**
 * 服务器上的数据加上本地的数据
 *
 * Created by wuhk on 2015/10/29.
 */
public class AllGoodInfo implements Serializable {
    private String goodId ;//商品id
    private String goodIcon ;//商品图标
    private String goodName;//商品名字
    private String goodPrice;//商品单价
    private String marketCost;//市场支持费用
    private String invertory;//卖家进货自己的商品库存
    private String bottlesPerBox;//每箱瓶数
    private String description;//描述
    private String goodCount;//数量
    private String goodType;//规格（箱/瓶）
    private String shopId;//店铺id
    private String shopName;//店铺名字
    private String format;//规格
    private String upPrice;//上传时价格
    private String unit;//单位
    private String priceWithUnit;//选择单位价格
    private String sellerInventory;//商品库存
    private List<SalesListData.Sales> salesList;


    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getGoodIcon() {
        return goodIcon;
    }

    public void setGoodIcon(String goodIcon) {
        this.goodIcon = goodIcon;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(String goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getMarketCost() {
        return marketCost;
    }

    public void setMarketCost(String marketCost) {
        this.marketCost = marketCost;
    }

    public String getInvertory() {
        return invertory;
    }

    public void setInvertory(String invertory) {
        this.invertory = invertory;
    }

    public String getBottlesPerBox() {
        return bottlesPerBox;
    }

    public void setBottlesPerBox(String bottlesPerBox) {
        this.bottlesPerBox = bottlesPerBox;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getUpPrice() {
        return upPrice;
    }

    public void setUpPrice(String upPrice) {
        this.upPrice = upPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSellerInventory() {
        return sellerInventory;
    }

    public void setSellerInventory(String sellerInventory) {
        this.sellerInventory = sellerInventory;
    }

    public String getPriceWithUnit() {
        return priceWithUnit;
    }

    public void setPriceWithUnit(String priceWithUnit) {
        this.priceWithUnit = priceWithUnit;
    }


    public List<SalesListData.Sales> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<SalesListData.Sales> salesList) {
        this.salesList = salesList;
    }
}
