package com.zjyeshi.dajiujiao.buyer.entity.contact;

/**
 * Created by wuhk on 2016/9/10.
 */
public class PhoneUser {
    private String ownerUserId;
    private String phoneNumber;
    private String phoneName;
    private int phoneUsed;//1-账户不存在,2-账户存在,非好友,3-账户存在,好友

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public int getPhoneUsed() {
        return phoneUsed;
    }

    public void setPhoneUsed(int phoneUsed) {
        this.phoneUsed = phoneUsed;
    }
}
