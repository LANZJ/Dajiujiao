package com.zjyeshi.dajiujiao.buyer.entity.my.work;

import java.io.Serializable;
import java.util.List;

/**
 * 请假记录
 *
 * Created by zhum on 2016/6/13.
 */
public class AttendanceRecord implements Serializable{
    private String speDate;
    private String date;
    private String upTime;
    private String upArea;
    private List<String> onList;
    private String upRemark;
    private String downTime;
    private String downArea;
    private String downmRemark;
    private List<String> offList;
    private boolean single;
    private String lng;
    private String lat;
    private String lngs;
    private String lats;

    public String getLngs(){
        return lngs;
    }
    public void setLngs(String lngs){
        this.lngs=lngs;
    }
    public String getLats(){
        return lats;
    }
    public void setLats(String lats){
        this.lats=lats;
    }
    public String getLng(){
        return lng;
    }
    public void setLng(String lng){
        this.lng=lng;
    }
    public String getLat(){
        return lat;
    }
    public void setLat(String lat){
        this.lat=lat;
    }
    public String getSpeDate() {
        return speDate;
    }

    public void setSpeDate(String speDate) {
        this.speDate = speDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getUpArea() {
        return upArea;
    }

    public void setUpArea(String upArea) {
        this.upArea = upArea;
    }

    public String getDownTime() {
        return downTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime;
    }

    public String getDownArea() {
        return downArea;
    }

    public void setDownArea(String downArea) {
        this.downArea = downArea;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public String getUpRemark() {
        return upRemark;
    }

    public void setUpRemark(String upRemark) {
        this.upRemark = upRemark;
    }

    public String getDownmRemark() {
        return downmRemark;
    }

    public void setDownmRemark(String downmRemark) {
        this.downmRemark = downmRemark;
    }

    public List<String> getOnList() {
        return onList;
    }

    public void setOnList(List<String> onList) {
        this.onList = onList;
    }

    public List<String> getOffList() {
        return offList;
    }

    public void setOffList(List<String> offList) {
        this.offList = offList;
    }
}
