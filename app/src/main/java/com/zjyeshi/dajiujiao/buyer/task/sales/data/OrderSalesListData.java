package com.zjyeshi.dajiujiao.buyer.task.sales.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * Created by wuhk on 2017/5/18.
 */

public class OrderSalesListData extends BaseData<OrderSalesListData> {

    private List<OrderSales> list;

    public List<OrderSales> getList() {
        return list;
    }

    public void setList(List<OrderSales> list) {
        this.list = list;
    }

    public static class OrderSales extends SalesListData.Sales{
        private String orderId;
        private String orderProductId;
        private String productId;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderProductId() {
            return orderProductId;
        }

        public void setOrderProductId(String orderProductId) {
            this.orderProductId = orderProductId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
    }
}
