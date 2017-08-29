package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2016/6/22.
 */
public class AskMoneyDetailData extends BaseData<AskMoneyDetailData> {
    private String id;
    private String approver;
    private int applicantType;
    private String applicationMoney;
    private String remark;
    private String pics;
    private List<Comment> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
