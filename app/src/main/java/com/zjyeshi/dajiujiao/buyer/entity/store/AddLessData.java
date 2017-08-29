package com.zjyeshi.dajiujiao.buyer.entity.store;

/**
 * Created by wuhk on 2016/6/29.
 */
public class AddLessData {
    private String unit;
    private String bottolesPerBox;
    private boolean seller;
    private String num;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBottolesPerBox() {
        return bottolesPerBox;
    }

    public void setBottolesPerBox(String bottolesPerBox) {
        this.bottolesPerBox = bottolesPerBox;
    }

    public boolean isSeller() {
        return seller;
    }

    public void setSeller(boolean seller) {
        this.seller = seller;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
