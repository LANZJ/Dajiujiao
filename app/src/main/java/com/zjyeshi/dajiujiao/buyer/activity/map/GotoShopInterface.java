package com.zjyeshi.dajiujiao.buyer.activity.map;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.xuan.bigdog.lib.location.DGPoint;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.Shop;

import java.util.List;

/**
 * Created by wuhk on 2015/11/27.
 */
public interface GotoShopInterface {

    /**在地图上话标记并显示商家信息
     *
     * @param locationPointList
     * @param locationPointBitmapList
     * @param shopList
     */
    void markLocationToShop(List<DGPoint> locationPointList,
                      List<BitmapDescriptor> locationPointBitmapList , List<Shop> shopList);
}
