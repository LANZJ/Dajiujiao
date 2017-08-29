package com.zjyeshi.dajiujiao.buyer.circle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zjyeshi.dajiujiao.buyer.circle.itemview.TopHeadView;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.CircleContentEntity;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.TopHeadEntity;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.CircleContentView;

import java.util.List;

/**
 * 圈子适配器
 * Created by xuan on 15/10/18.
 */
public class CircleAdapter extends MBaseAdapter {
    private Context mContext;
    private List<BaseEntity> mDataList;

    final int TYPE_TOP = 0;
    final int TYPE_CONTENT = 1;

    public CircleAdapter(Context context, List<BaseEntity> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_TOP;
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
        return mDataList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        BaseEntity baseEntity = mDataList.get(position);
        //view为空，设置view
        if (null == view){
            if (baseEntity instanceof TopHeadEntity) {
                TopHeadView topHeadView = new TopHeadView(mContext);
                view = topHeadView;
            } else if (baseEntity instanceof CircleContentEntity) {
                //图文＋网页合用了一个
                CircleContentView circleContentView = new CircleContentView(mContext);
                view = circleContentView;
            }
        }
        //配置view的内容
        if (baseEntity instanceof TopHeadEntity) {
            TopHeadEntity topHeadEntity = (TopHeadEntity) baseEntity;
            ((TopHeadView) view).bindData(topHeadEntity);

        } else if (baseEntity instanceof CircleContentEntity) {
            final CircleContentEntity circleContentEntity = (CircleContentEntity) baseEntity;
            ((CircleContentView) view).bindData(circleContentEntity , position);
        }
        return view;
    }
}
