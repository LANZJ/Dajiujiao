package com.zjyeshi.dajiujiao.buyer.task.data.store.homepage;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 *获得附近店铺信息
 *
 * Created by wuhk on 2015/10/26.
 */
public class GetNearbyShopList extends BaseData<GetNearbyShopList> {
    private List<ALLStoreData> list;

    public List<ALLStoreData> getList() {
        return list;
    }

    public void setList(List<ALLStoreData> list) {
        this.list = list;
    }
}
