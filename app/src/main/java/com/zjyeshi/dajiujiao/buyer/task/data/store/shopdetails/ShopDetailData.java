package com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;

import java.util.List;

/**
 * 商品详情
 *
 * Created by wuhk on 2015/10/28.
 */
public class ShopDetailData extends BaseData<ShopDetailData> {
    private Shop shop;
    private Member member;
    private List<SalesListData.Sales> activities;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<SalesListData.Sales> getActivities() {
        return activities;
    }

    public void setActivities(List<SalesListData.Sales> activities) {
        this.activities = activities;
    }
}
