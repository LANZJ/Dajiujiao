package com.zjyeshi.dajiujiao.buyer.task.data.my;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * Created by wuhk on 2016/9/8.
 */
public class MemberMoreData extends BaseData<MemberMoreData> {
    private String name;
    private String pic;
    private String circleBackgroundPic;
    private String phone;
    private boolean friend;
    private boolean customer;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public boolean isCustomer() {
        return customer;
    }

    public void setCustomer(boolean customer) {
        this.customer = customer;
    }
}
