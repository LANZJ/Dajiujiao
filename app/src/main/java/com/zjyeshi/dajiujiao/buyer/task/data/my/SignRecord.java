package com.zjyeshi.dajiujiao.buyer.task.data.my;

import java.util.Date;

/**
 * Created by wuhk on 2016/3/2.
 */
public class SignRecord {
    private String id;
    private String lngE5;
    private String latE5;
    private String address;
    private String status;
    private Date creationTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLngE5() {
        return lngE5;
    }

    public void setLngE5(String lngE5) {
        this.lngE5 = lngE5;
    }

    public String getLatE5() {
        return latE5;
    }

    public void setLatE5(String latE5) {
        this.latE5 = latE5;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
