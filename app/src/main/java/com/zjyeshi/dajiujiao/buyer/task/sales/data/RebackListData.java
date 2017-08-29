package com.zjyeshi.dajiujiao.buyer.task.sales.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2017/5/14.
 */

public class RebackListData extends BaseData<RebackListData> {

    private List<Reback> list;

    public List<Reback> getList() {
        return list;
    }

    public void setList(List<Reback> list) {
        this.list = list;
    }

    public static class Reback{
        private String id;
        private String memberName;
        private String shopName;
        private String orderNumber;
        private String rebackOrderTotalAmount;
        private int status;
        private long creationTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getRebackOrderTotalAmount() {
            return rebackOrderTotalAmount;
        }

        public void setRebackOrderTotalAmount(String rebackOrderTotalAmount) {
            this.rebackOrderTotalAmount = rebackOrderTotalAmount;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(long creationTime) {
            this.creationTime = creationTime;
        }
    }
}
