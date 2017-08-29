package com.xuan.bigdog.lib.bservice.bbanner;

import com.xuan.bigdog.lib.bservice.bcommon.BServiceBaseData;

import java.util.List;

/**
 * 轮播图返回
 * Created by wuhk on 2016/3/14.
 */
public class BDGetBannerData extends BServiceBaseData<BDGetBannerData> {
    private List<Banner> list;

    public List<Banner> getList() {
        return list;
    }

    public void setList(List<Banner> list) {
        this.list = list;
    }

    private static class Banner{
        private String id;
        private String name;
        private String pic;
        private String url;

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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
