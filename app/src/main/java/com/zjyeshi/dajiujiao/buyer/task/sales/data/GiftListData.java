package com.zjyeshi.dajiujiao.buyer.task.sales.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2017/4/26.
 */

public class GiftListData extends BaseData<GiftListData> {

    private List<Gift> list;

    public List<Gift> getList() {
        return list;
    }

    public void setList(List<Gift> list) {
        this.list = list;
    }

    public static class Gift{
        private String id;
        private String name;
        private String price;
        private String pic;
        private boolean selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
