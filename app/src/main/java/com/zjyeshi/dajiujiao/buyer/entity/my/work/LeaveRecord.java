package com.zjyeshi.dajiujiao.buyer.entity.my.work;

import java.io.Serializable;

/**
 * 请假记录
 *
 * Created by zhum on 2016/6/14.
 */
public class LeaveRecord implements Serializable{
    private String id;
    private int type;
    private String startTime;
    private String endTime;
    private String leaveDays;
    private long creationTime;
    private int status;
    private String clockPerson;
    private int readStatus;
    private int approverType;//我的审批状态
    private String approverNow;//当前审批人

    public String getClockPerson() {
        return clockPerson;
    }

    public void setClockPerson(String clockPerson) {
        this.clockPerson = clockPerson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public int getApproverType() {
        return approverType;
    }

    public void setApproverType(int approverType) {
        this.approverType = approverType;
    }

    public String getApproverNow() {
        return approverNow;
    }

    public void setApproverNow(String approverNow) {
        this.approverNow = approverNow;
    }
}
