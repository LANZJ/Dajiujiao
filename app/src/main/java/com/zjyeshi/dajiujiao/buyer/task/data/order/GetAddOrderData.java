package com.zjyeshi.dajiujiao.buyer.task.data.order;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.io.Serializable;
import java.util.List;

/**
 * 增加订单返回
 *
 * Created by wuhk on 2015/11/3.
 */
public class GetAddOrderData extends BaseData<GetAddOrderData> {
    private List<OrderInfo> list;

    public List<OrderInfo> getList() {
        return list;
    }

    public void setList(List<OrderInfo> list) {
        this.list = list;
    }

    public static class OrderInfo implements Serializable{
        private String id ;
        private String name;

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
    }

}