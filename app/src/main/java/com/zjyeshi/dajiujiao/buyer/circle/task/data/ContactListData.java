package com.zjyeshi.dajiujiao.buyer.circle.task.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/9/11.
 */
public class ContactListData extends BaseData<ContactListData> {

    private List<ContactInfo> list;

    public List<ContactInfo> getList() {
        return list;
    }

    public void setList(List<ContactInfo> list) {
        this.list = list;
    }

    public static class ContactInfo{
        private String phone;
        private int type;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
