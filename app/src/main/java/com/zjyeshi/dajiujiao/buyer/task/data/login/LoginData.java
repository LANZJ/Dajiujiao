package com.zjyeshi.dajiujiao.buyer.task.data.login;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 *登录数据
 *
 * Created by wuhk on 2015/10/22.
 */
public class LoginData extends BaseData<LoginData> {
    private String token;
    private String id ;//用户id
    private String name;//用户名
    private String pic ;//头像
    private int type;//类型
    private int shopType;//业务员所属店铺类型
    private int marketCostType;//市场支持费用类型，1.无，2.立返，3.月返，季返，年返
    private String shopName;//点名
    private String jur;//员工权限
    private String shopId;//业务员所属店铺Id;
    private String circleBackgroundPic;//朋友圈背景图

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public String getJur() {
        return jur;
    }

    public void setJur(String jur) {
        this.jur = jur;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCircleBackgroundPic() {
        return circleBackgroundPic;
    }

    public void setCircleBackgroundPic(String circleBackgroundPic) {
        this.circleBackgroundPic = circleBackgroundPic;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getMarketCostType() {
        return marketCostType;
    }

    public void setMarketCostType(int marketCostType) {
        this.marketCostType = marketCostType;
    }
}
