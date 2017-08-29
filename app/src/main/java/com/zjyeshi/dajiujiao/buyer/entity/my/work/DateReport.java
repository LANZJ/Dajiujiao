package com.zjyeshi.dajiujiao.buyer.entity.my.work;

/**
 * 工作日报
 *
 * Created by zhum on 2016/6/17.
 */
public class DateReport{

    private String id;
    private String recordTime;
    private String person;
    private String trip;
    private String unread;
    private String unreadComment;
    private long creationTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    public String getUnreadComment() {
        return unreadComment;
    }

    public void setUnreadComment(String unreadComment) {
        this.unreadComment = unreadComment;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
}
