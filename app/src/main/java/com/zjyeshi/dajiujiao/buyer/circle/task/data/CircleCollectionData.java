package com.zjyeshi.dajiujiao.buyer.circle.task.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 圈子收藏
 * Created by wuhk on 2016/8/16.
 */
public class CircleCollectionData extends BaseData<CircleCollectionData> {

    private List<Collection> list;

    public List<Collection> getList() {
        return list;
    }

    public void setList(List<Collection> list) {
        this.list = list;
    }

    public static class Collection{
        private String id;
        private String fromMember;
        private String fromMemberPic;
        private String pic;
        private String content;
        private int linkType;
        private String linkPic;
        private String linkTitle;
        private String linkContent;
        private long creationTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFromMember() {
            return fromMember;
        }

        public void setFromMember(String fromMember) {
            this.fromMember = fromMember;
        }

        public String getFromMemberPic() {
            return fromMemberPic;
        }

        public void setFromMemberPic(String fromMemberPic) {
            this.fromMemberPic = fromMemberPic;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLinkType() {
            return linkType;
        }

        public void setLinkType(int linkType) {
            this.linkType = linkType;
        }

        public String getLinkPic() {
            return linkPic;
        }

        public void setLinkPic(String linkPic) {
            this.linkPic = linkPic;
        }

        public String getLinkTitle() {
            return linkTitle;
        }

        public void setLinkTitle(String linkTitle) {
            this.linkTitle = linkTitle;
        }

        public String getLinkContent() {
            return linkContent;
        }

        public void setLinkContent(String linkContent) {
            this.linkContent = linkContent;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(long creationTime) {
            this.creationTime = creationTime;
        }
    }
}
