package com.zjyeshi.dajiujiao.buyer.entity.my.work;

/**
 * 费用请求记录
 *
 * Created by zhum on 2016/6/16.
 */
public class AskMoneyRecord {
    private String id;
    private String mainId;
    private int applicantType;
    private String applicationMoney;
    private long creationTime;
    private int status;
    private int readStatus; //1、已读， 2、未读

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public int getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(int applicantType) {
        this.applicantType = applicantType;
    }

    public String getApplicationMoney() {
        return applicationMoney;
    }

    public void setApplicationMoney(String applicationMoney) {
        this.applicationMoney = applicationMoney;
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
}
