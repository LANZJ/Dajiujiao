package com.zjyeshi.dajiujiao.buyer.task.account.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/7/4.
 */
public class CashLogList extends BaseData<CashLogList> {
    private List<CashLog> list;

    public List<CashLog> getList() {
        return list;
    }

    public void setList(List<CashLog> list) {
        this.list = list;
    }

    public static class CashLog{
        private String id;
        private String amount;
        private String remark;
        private int status;
        private long creationTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
