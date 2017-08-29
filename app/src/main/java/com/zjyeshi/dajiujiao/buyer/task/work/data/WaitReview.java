package com.zjyeshi.dajiujiao.buyer.task.work.data;

/**
 * 待我审批
 *
 * Created by zhum on 2016/6/23.
 */
public class WaitReview {
    private String id;
    private String mainId;
    private String type;
    private String name;
    private String applicationMoney;
    private String application;
    private long creationTime;
    private String status;
    private int approverType;
    private String approverNow;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationMoney() {
        return applicationMoney;
    }

    public void setApplicationMoney(String applicationMoney) {
        this.applicationMoney = applicationMoney;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
