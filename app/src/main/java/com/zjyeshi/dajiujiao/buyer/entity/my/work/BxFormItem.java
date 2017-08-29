package com.zjyeshi.dajiujiao.buyer.entity.my.work;

import java.util.List;

/**
 * 报销明细项
 *
 * Created by zhum on 2016/6/16.
 */
public class BxFormItem {
    private int applicantType;
    private String applcationMoney;
    private String remark;
    private String pics;
    private List<String> imageList;

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public int getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(int applicantType) {
        this.applicantType = applicantType;
    }

    public String getApplcationMoney() {
        return applcationMoney;
    }

    public void setApplcationMoney(String applcationMoney) {
        this.applcationMoney = applcationMoney;
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
