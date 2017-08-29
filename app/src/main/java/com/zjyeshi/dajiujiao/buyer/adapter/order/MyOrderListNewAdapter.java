package com.zjyeshi.dajiujiao.buyer.adapter.order;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PerOrder;
import com.zjyeshi.dajiujiao.buyer.views.order.OrderView;

import java.util.List;

/**
 * 新订单列表适配器
 * Created by wuhk on 2016/9/14.
 */
public class MyOrderListNewAdapter extends MBaseAdapter {
    private Context context;
    private List<PerOrder> dataList;
    private String type;

    public MyOrderListNewAdapter(Context context, List<PerOrder> dataList , String type) {
        this.context = context;
        this.dataList = dataList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
           view = new OrderView(context);
        }
        PerOrder perOrder = dataList.get(position);
        ((OrderView)view).bindData(type , perOrder , position);

        return view;
    }
}
