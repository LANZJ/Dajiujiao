package com.zjyeshi.dajiujiao.buyer.entity.my.work;

import com.xuan.bigapple.lib.utils.Validators;

/**
 * Created by wuhk on 2016/6/23.
 */
public class CompanyStock implements Comparable<CompanyStock>{
    private String id;
    private String pic;
    private String name;
    private String inventory;
    private String specifications;
    private String bottlesPerBox;
    private String unit;
    private Integer kc;
    private String price;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getBottlesPerBox() {
        return bottlesPerBox;
    }

    public void setBottlesPerBox(String bottlesPerBox) {
        this.bottlesPerBox = bottlesPerBox;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        if (Validators.isEmpty(unit)){
            this.unit = "瓶";
        }else{
            this.unit = unit;
        }
    }

    public Integer getKc() {
        return kc;
    }

    public void setKc(Integer kc) {
        this.kc = kc;
    }

    public int compareTo(CompanyStock arg0) {
        return this.getKc().compareTo(arg0.getKc());
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
