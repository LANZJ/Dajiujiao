package com.zjyeshi.dajiujiao.buyer.task.data.my;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * 输入酒友码返回的商铺信息
 *
 * Created by xuan on 15/11/27.
 */
public class ShopData extends BaseData<ShopData> {
    private ShopData.Shop shop;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public class Shop{
        private String Id;
        private String pic;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }

}
