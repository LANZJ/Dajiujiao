package com.zjyeshi.dajiujiao.buyer.task.data.my;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/8/16.
 */
public class FriendApplyListData extends BaseData<FriendApplyListData> {

    private List<FriendApply> list;

    public List<FriendApply> getList() {
        return list;
    }

    public void setList(List<FriendApply> list) {
        this.list = list;
    }

    public static class FriendApply{
        private String id;
        private String avatar;
        private String name;
        private String remark;
        private boolean applicant;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isApplicant() {
            return applicant;
        }

        public void setApplicant(boolean applicant) {
            this.applicant = applicant;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
