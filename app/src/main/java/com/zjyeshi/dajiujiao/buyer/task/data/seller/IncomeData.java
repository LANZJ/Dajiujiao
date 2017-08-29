package com.zjyeshi.dajiujiao.buyer.task.data.seller;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.Date;
import java.util.List;

/**
 * 我的收入
 *
 * Created by wuhk on 2015/11/10.
 */
public class IncomeData extends BaseData<IncomeData>{

    private List<Income> list;

    public List<Income> getList() {
        return list;
    }

    public void setList(List<Income> list) {
        this.list = list;
    }

    public static class Income{
        private String id;
        private String orderId;
        private String amount;
        private String remark;
        private int type;//0，线下支付， 1、线上支付
        private Date creationTime;
        private boolean isSorted;
        private boolean isIn;//是否是收入；true：收入；false：支出


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Date getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Date creationTime) {
            this.creationTime = creationTime;
        }

        public boolean isSorted() {
            return isSorted;
        }

        public void setIsSorted(boolean isSorted) {
            this.isSorted = isSorted;
        }

        public boolean isIn() {
            return isIn;
        }

        public void setIsIn(boolean isIn) {
            this.isIn = isIn;
        }
    }
}
