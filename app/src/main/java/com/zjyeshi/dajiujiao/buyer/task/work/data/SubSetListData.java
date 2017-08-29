package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/6/24.
 */
public class SubSetListData extends BaseData<SubSetListData> {
    private List<SubSet> list;

    public List<SubSet> getList() {
        return list;
    }

    public void setList(List<SubSet> list) {
        this.list = list;
    }

    public static class SubSet{
        private String id;
        private String pic;
        private String phone;
        private String name;
        private String shopName;


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

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }
    }
}
