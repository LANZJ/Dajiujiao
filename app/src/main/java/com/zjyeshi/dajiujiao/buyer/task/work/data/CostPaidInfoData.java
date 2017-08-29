package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 费用报销详情
 *
 * Created by zhum on 2016/6/23.
 */
public class CostPaidInfoData extends BaseData<CostPaidInfoData>{

    private String name;
    private String approver;
    private String application;
    private String paidMoney;
    private String status;
    private List<Paid> paids;
    private List<Comment> comments;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(String paidMoney) {
        this.paidMoney = paidMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Paid> getPaids() {
        return paids;
    }

    public void setPaids(List<Paid> paids) {
        this.paids = paids;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public static class Paid{
        private String applicantType;
        private String applicationMoney;
        private String remark;
        private String pics;

        public String getApplicantType() {
            return applicantType;
        }

        public void setApplicantType(String applicantType) {
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
    }
}
