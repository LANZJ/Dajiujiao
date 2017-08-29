package com.zjyeshi.dajiujiao.buyer.entity.good;

import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;

/**
 * Created by wuhk on 2016/4/7.
 */
public class GoodCommentEntity extends BaseEntity {

    private String id;
    private String orderNum;
    private String memberName;
    private String pic;
    private int level;
    private String content;
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
