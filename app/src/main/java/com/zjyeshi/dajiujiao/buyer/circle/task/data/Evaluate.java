package com.zjyeshi.dajiujiao.buyer.circle.task.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论
 * Created by wuhk on 2015/11/16.
 */
public class Evaluate implements Serializable {
    private String id;
    private String content;
    private Date creationTime;
    private Member member;//评论的人
    private Member memberup;//评论评论的人

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public Member getMemberup() {
        return memberup;
    }

    public void setMemberup(Member memberup) {
        this.memberup = memberup;
    }
}
