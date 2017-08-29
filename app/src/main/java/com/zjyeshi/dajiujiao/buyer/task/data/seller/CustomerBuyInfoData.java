package com.zjyeshi.dajiujiao.buyer.task.data.seller;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/4/7.
 */
public class CustomerBuyInfoData extends BaseData<CustomerBuyInfoData> {
    private String id;
    private String pic;
    private String memberName;
    private String allConsumption;
    private List<BuyInfo> products;

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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAllConsumption() {
        return allConsumption;
    }

    public void setAllConsumption(String allConsumption) {
        this.allConsumption = allConsumption;
    }

    public List<BuyInfo> getProducts() {
        return products;
    }

    public void setProducts(List<BuyInfo> products) {
        this.products = products;
    }

    public static class BuyInfo{
        private String id;
        private String pic;
        private String name;
        private String price;
        private String specifications;
        private String total;

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

        public String getSpecifications() {
            return specifications;
        }

        public void setSpecifications(String specifications) {
            this.specifications = specifications;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}
