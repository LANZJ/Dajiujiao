package com.zjyeshi.dajiujiao.buyer.task.data.my;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 *
 * Created by wuhk on 2016/8/16.
 */
public class FriendListData extends BaseData<FriendListData> {
    private List<Friend> list;

    public List<Friend> getList() {
        return list;
    }

    public void setList(List<Friend> list) {
        this.list = list;
    }

    public static class Friend{
        private String ownerUserId;
        private String id;
        private String memberId;
        private String avatar;
        private String name;

        public String getOwnerUserId() {
            return ownerUserId;
        }

        public void setOwnerUserId(String ownerUserId) {
            this.ownerUserId = ownerUserId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
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
    }
}
