package com.zjyeshi.dajiujiao.buyer.task.data.order;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/12/15.
 */
public class PathResData extends BaseData<PathResData> {

    private List<Path> list;


    public List<Path> getList() {
        return list;
    }

    public void setList(List<Path> list) {
        this.list = list;
    }

    public static class Path{
        private String id;
        private String applicate;
        private long creationTime;
        private String pic;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApplicate() {
            return applicate;
        }

        public void setApplicate(String applicate) {
            this.applicate = applicate;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(long creationTime) {
            this.creationTime = creationTime;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getStatus(){return  status;}
        public  void setStatus(String status){this.status=status;}
    }
}
