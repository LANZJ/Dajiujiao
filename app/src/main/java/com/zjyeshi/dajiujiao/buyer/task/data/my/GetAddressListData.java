package com.zjyeshi.dajiujiao.buyer.task.data.my;

import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 获取地址列表数据
 * Created by wuhk on 2015/11/13.
 */
public class GetAddressListData extends BaseData<GetAddressListData> {
    private List<Address> list;

    public List<Address> getList() {
        return list;
    }

    public void setList(List<Address> list) {
        this.list = list;
    }
}
