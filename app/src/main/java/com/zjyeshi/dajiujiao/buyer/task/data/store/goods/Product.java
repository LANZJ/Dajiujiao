package com.zjyeshi.dajiujiao.buyer.task.data.store.goods;

import com.xuan.bigapple.lib.utils.Validators;

/**
 * Created by wuhk on 2015/10/28.
 */
public class Product {
    private String id ;
    private String pic ;
    private String name;
    private String price;
    private String inventory;
    private String bottlesPerBox;
    private String description;
    private String specifications;
    private String unit;
    private String sellerInventory;
    private String marketCost;//市场支持费用
    private boolean selected;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getBottlesPerBox() {
        return bottlesPerBox;
    }

    public void setBottlesPerBox(String bottlesPerBox) {
        if (bottlesPerBox.equals("0")){
            this.bottlesPerBox = "1";
        }else{
            this.bottlesPerBox = bottlesPerBox;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
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

    public String getMarketCost() {
        return marketCost;
    }

    public void setMarketCost(String marketCost) {
        if (Validators.isEmpty(marketCost)){
            this.marketCost = "0";
        }else{
            this.marketCost = marketCost;
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
