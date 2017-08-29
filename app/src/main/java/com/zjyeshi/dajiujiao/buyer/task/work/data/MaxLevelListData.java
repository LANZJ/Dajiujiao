package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/9/30.
 */
public class MaxLevelListData extends BaseData<MaxLevelListData> {

    private List<MaxLevelMan> list;

    public List<MaxLevelMan> getList() {
        return list;
    }

    public void setList(List<MaxLevelMan> list) {
        this.list = list;
    }

    public static class MaxLevelMan{
        private String memberId;
        private String phone;
        private String name;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
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
    }
}
