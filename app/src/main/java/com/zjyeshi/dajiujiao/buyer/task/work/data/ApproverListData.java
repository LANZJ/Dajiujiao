package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/6/21.
 */
public class ApproverListData extends BaseData<ApproverListData> {

    private List<Approver> list;

    public List<Approver> getList() {
        return list;
    }

    public void setList(List<Approver> list) {
        this.list = list;
    }

    public static class Approver{
        private String id;
        private String pic;
        private String name;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
