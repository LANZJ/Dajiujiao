package com.zjyeshi.dajiujiao.buyer.circle.task.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 赞
 * Created by wuhk on 2015/11/16.
 */
public class Praise implements Serializable {
    private String id;
    private Member member;
    private Date creationTime;
    private String deleted;

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
