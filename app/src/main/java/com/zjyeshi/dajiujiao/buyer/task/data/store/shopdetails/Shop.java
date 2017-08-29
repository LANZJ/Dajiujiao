package com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails;

import java.io.Serializable;

/**
 * Created by wuhk on 2015/10/28.
 */
public class Shop implements Serializable{
    private String id;
    private String name ;
    private String level;
    private String distance ;
    private String lngE5;
    private String latE5;
    private String address;
    private String openTime;
    private String closeTime;
    private String pic ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
