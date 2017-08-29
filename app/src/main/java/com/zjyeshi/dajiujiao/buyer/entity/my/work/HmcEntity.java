package com.zjyeshi.dajiujiao.buyer.entity.my.work;

import java.util.List;

/**
 * Created by wuhk on 2016/9/12.
 */
public class HmcEntity {
    private String province;
    private List<Employee> list;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<Employee> getList() {
        return list;
    }

    public void setList(List<Employee> list) {
        this.list = list;
    }
}
