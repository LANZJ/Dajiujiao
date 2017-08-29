package com.zjyeshi.dajiujiao.buyer.task.data.store.homepage;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 首页轮播图
 * Created by wuhk on 2015/11/23.
 */
public class CourseImageData extends BaseData<CourseImageData> {
    private List<Course> list;

    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }

    public static class Course{
        private String id;
        private String pic;
        private String link;

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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

}
