package com.zjyeshi.dajiujiao.buyer.task.sales.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2017/4/27.
 */

public class SalesDetailData extends BaseData<SalesDetailData> {
    private String id;
    private String shopId;
    private int satisfyType;
    private int favouredType;
    private int formType;
    private boolean superposition;
    private String satisfyCondition;
    private String favouredPolicy;
    private String startTime;
    private String endTime;
    private String giftId;
    private String giftName;
    private String giftPrice;
    private SalesProduct giveProduct;
    private String url;
    private int priority;
    private long creationTime;
    private int status;
    private List<SalesProduct> products;
    private List<SalesShop> shops;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getSatisfyType() {
        return satisfyType;
    }

    public void setSatisfyType(int satisfyType) {
        this.satisfyType = satisfyType;
    }

    public boolean isSuperposition() {
        return superposition;
    }

    public void setSuperposition(boolean superposition) {
        this.superposition = superposition;
    }

    public int getFavouredType() {
        return favouredType;
    }

    public void setFavouredType(int favouredType) {
        this.favouredType = favouredType;
    }

    public int getFormType() {
        return formType;
    }

    public void setFormType(int formType) {
        this.formType = formType;
    }

    public String getSatisfyCondition() {
        return satisfyCondition;
    }

    public void setSatisfyCondition(String satisfyCondition) {
        this.satisfyCondition = satisfyCondition;
    }

    public String getFavouredPolicy() {
        return favouredPolicy;
    }

    public void setFavouredPolicy(String favouredPolicy) {
        this.favouredPolicy = favouredPolicy;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(String giftPrice) {
        this.giftPrice = giftPrice;
    }

    public SalesProduct getGiveProduct() {
        return giveProduct;
    }

    public void setGiveProduct(SalesProduct giveProduct) {
        this.giveProduct = giveProduct;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SalesProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SalesProduct> products) {
        this.products = products;
    }

    public List<SalesShop> getShops() {
        return shops;
    }

    public void setShops(List<SalesShop> shops) {
        this.shops = shops;
    }

    public static class SalesProduct{
        private String id;
        private String productId;
        private String pic;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
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
    }


    public static class SalesShop{
        private String id;
        private String shopId;
        private String pic;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
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
    }

    public static class Join{
        private String id;
        private String shopProductId;
        private int type;
        private String pic;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShopProductId() {
            return shopProductId;
        }

        public void setShopProductId(String shopProductId) {
            this.shopProductId = shopProductId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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
    }
}
