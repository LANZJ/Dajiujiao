package com.zjyeshi.dajiujiao.buyer.circle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleCollectionData;
import com.zjyeshi.dajiujiao.buyer.utils.CircleUtil;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.CircleCollectView;

import java.util.List;

/**
 * 圈子收藏列表适配器
 * Created by wuhk on 2016/8/16.
 */
public class CircleCollectAdapter extends MBaseAdapter {
    private Context context;
    private List<CircleCollectionData.Collection> dataList;

    public CircleCollectAdapter(Context context, List<CircleCollectionData.Collection> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (null == view){
            CircleCollectView circleCollectView = new CircleCollectView(context);
            view = circleCollectView;
        }

        final CircleCollectionData.Collection collection = dataList.get(position);
        ((CircleCollectView)view).bindData(collection);

        CircleUtil.cancelCollect(view, collection, new CircleCollectView.CancelCallback() {
            @Override
            public void callback() {
                dataList.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
