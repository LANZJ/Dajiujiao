package com.zjyeshi.dajiujiao.buyer.entity;

/**
 * 收藏实体类
 * Created by wuhk on 2016/8/15.
 */
public class CircleCollect {
    private String fromMemberId;
    private String pic;
    private String content;
    private int link_type;
    private String link_pic;
    private String link_title;
    private String link_content;

    public String getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(String fromMemberId) {
        this.fromMemberId = fromMemberId;
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

    public int getLink_type() {
        return link_type;
    }

    public void setLink_type(int link_type) {
        this.link_type = link_type;
    }

    public String getLink_pic() {
        return link_pic;
    }

    public void setLink_pic(String link_pic) {
        this.link_pic = link_pic;
    }

    public String getLink_title() {
        return link_title;
    }

    public void setLink_title(String link_title) {
        this.link_title = link_title;
    }

    public String getLink_content() {
        return link_content;
    }

    public void setLink_content(String link_content) {
        this.link_content = link_content;
    }
}
