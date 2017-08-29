package com.zjyeshi.dajiujiao.buyer.task.data.store.homepage;

/**
 * 首页店铺信息
 *
 * Created by wuhk on 2015/10/22.
 */
public class ALLStoreData {
    private Shop shop;
    private ShopActivity shopActivity;
    private boolean Followed;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public ShopActivity getShopActivity() {
        return shopActivity;
    }

    public void setShopActivity(ShopActivity shopActivity) {
        this.shopActivity = shopActivity;
    }

    public boolean isFollowed() {
        return Followed;
    }

    public void setFollowed(boolean followed) {
        Followed = followed;
    }
}
