package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.entity.my.work.Employee;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 客户列表
 * <p>
 * Created by zhum on 2016/6/20.
 */
public class CustomerListData extends BaseData<CustomerListData> {

    private List<Employee> list;

    public List<Employee> getList() {
        return list;
    }

    public void setList(List<Employee> list) {
        this.list = list;
    }
}
