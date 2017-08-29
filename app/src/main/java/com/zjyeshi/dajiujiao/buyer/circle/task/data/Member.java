package com.zjyeshi.dajiujiao.buyer.circle.task.data;

import java.io.Serializable;

/**
 * 成员
 * Created by wuhk on 2015/11/16.
 */
public class Member implements Serializable{
    private String id;
    private String name;
    private String pic;//用户头像
    private String circleBackgroundPic;//酒友圈背景图

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCircleBackgroundPic() {
        return circleBackgroundPic;
    }

    public void setCircleBackgroundPic(String circleBackgroundPic) {
        this.circleBackgroundPic = circleBackgroundPic;
    }
}
