package com.zjyeshi.dajiujiao.buyer.entity.sellerincome;

/**
 *支出收入详情
 *
 * Created by wuhk on 2015/11/6.
 */
public class DetailIncomeData {
    private String id ;
    private String price;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
