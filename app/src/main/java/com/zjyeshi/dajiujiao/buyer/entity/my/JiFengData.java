package com.zjyeshi.dajiujiao.buyer.entity.my;

import java.io.Serializable;

/**
 * 积分商城数据
 *
 * Created by wuhk on 2015/10/19.
 */
public class JiFengData implements Serializable {
    private String photo;
    private String name ;
    private String oldPrice;
    private String score;
    private boolean isChanged;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setIsChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }
}
