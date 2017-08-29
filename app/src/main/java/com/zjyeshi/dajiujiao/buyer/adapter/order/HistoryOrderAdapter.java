package com.zjyeshi.dajiujiao.buyer.adapter.order;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.order.OrderHeadEntity;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PerOrder;
import com.zjyeshi.dajiujiao.buyer.views.order.OrderContentView;
import com.zjyeshi.dajiujiao.buyer.views.order.OrderHeadView;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;

import java.util.List;

/**
 * 历史订单适配器
 * Created by wuhk on 2016/9/12.
 */
public class HistoryOrderAdapter extends MBaseAdapter {
    private Context context;
    private List<BaseEntity> dataList;
    private String type;

    final int TYPE_HEAD = 0;
    final int TYPE_CONTENT = 1;

    public HistoryOrderAdapter(Context context, List<BaseEntity> dataList , String type) {
        this.context = context;
        this.dataList = dataList;
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        BaseEntity baseEntity = dataList.get(position);
        if (baseEntity instanceof OrderHeadEntity){
            return TYPE_HEAD;
        }else{
            return  TYPE_CONTENT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        BaseEntity baseEntity = dataList.get(position);
        if (null == view){
            if (baseEntity instanceof OrderHeadEntity){
                OrderHeadView orderHeadView = new OrderHeadView(context);
                view = orderHeadView;
            }else if (baseEntity instanceof PerOrder){
                OrderContentView orderContentView = new OrderContentView(context);
                view = orderContentView;
            }
        }

        if(baseEntity instanceof OrderHeadEntity){
            OrderHeadEntity orderHeadEntity = (OrderHeadEntity)baseEntity;
            ((OrderHeadView)view).bindData(orderHeadEntity);
        }else if (baseEntity instanceof PerOrder){
            PerOrder orderContentEntity = (PerOrder)baseEntity;
            ((OrderContentView)view).bindData(orderContentEntity , type , position);
        }

        return view;
    }
}
