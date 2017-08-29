package com.zjyeshi.dajiujiao.buyer.adapter.store;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.views.good.GoodCommentView;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodCommentList;

import java.util.List;

/**
 * Created by wuhk on 2016/9/19.
 */
public class GoodCommentAdapter extends MBaseAdapter {
    private Context context;
    private List<GoodCommentList.GoodComment> dataList;

    public GoodCommentAdapter(Context context, List<GoodCommentList.GoodComment> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
            view = new GoodCommentView(context);
        }

        GoodCommentList.GoodComment goodComment = dataList.get(position);
        ((GoodCommentView)view).setData(goodComment);
        return view;
    }
}
