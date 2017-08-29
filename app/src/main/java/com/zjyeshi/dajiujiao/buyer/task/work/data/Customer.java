package com.zjyeshi.dajiujiao.buyer.task.work.data;

/**
 * 客户
 *
 * Created by zhum on 2016/6/20.
 */
public class Customer {
    private String id;
    private String pic;//头像
    private String name;//名字
    private String shopName;//店名

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
