package com.zjyeshi.dajiujiao.buyer.entity.store;

import java.io.Serializable;

/**
 * 店铺经纬度
 * Created by wuhk on 2015/11/20.
 */
public class Point implements Serializable {
    private Double lat ;
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
