package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/6/24.
 */
public class SalemanListData extends BaseData<SalemanListData> {

    private List<Saleman> list;

    public List<Saleman> getList() {
        return list;
    }

    public void setList(List<Saleman> list) {
        this.list = list;
    }

    public static class Saleman{
        private String memberId;
        private String id;
        private String phone;
        private String name;
        private String pic;
        private String amount;
        private boolean selected;
        private int unreadNum;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getUnreadNum() {
            return unreadNum;
        }

        public void setUnreadNum(int unreadNum) {
            this.unreadNum = unreadNum;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
