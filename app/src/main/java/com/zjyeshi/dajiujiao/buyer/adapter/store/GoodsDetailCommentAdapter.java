package com.zjyeshi.dajiujiao.buyer.adapter.store;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.views.good.GoodCommentView;
import com.zjyeshi.dajiujiao.buyer.views.good.GoodInfoView;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;
import com.zjyeshi.dajiujiao.buyer.entity.good.GoodCommentEntity;
import com.zjyeshi.dajiujiao.buyer.entity.good.GoodInfoEntity;

import java.util.List;

/**
 * Created by wuhk on 2016/4/7.
 */
public class GoodsDetailCommentAdapter extends MBaseAdapter {
    private Context context;
    private List<BaseEntity> dataList;

    public GoodsDetailCommentAdapter(Context context, List<BaseEntity> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        BaseEntity baseEntity = dataList.get(position);
        if (null == view){
            if (baseEntity instanceof GoodInfoEntity){
                GoodInfoView goodInfoView = new GoodInfoView(context);
                view = goodInfoView;
            }else if(baseEntity instanceof GoodCommentEntity){
                GoodCommentView goodCommentView = new GoodCommentView(context);
                view = goodCommentView;
            }
        }

        if (baseEntity instanceof GoodInfoEntity){
            GoodInfoEntity goodInfoEntity = (GoodInfoEntity)baseEntity;
            ((GoodInfoView)view).bindData(goodInfoEntity);
        }else if (baseEntity instanceof GoodCommentEntity){
            GoodCommentEntity goodCommentEntity = (GoodCommentEntity)baseEntity;
            ((GoodCommentView)view).bindData(goodCommentEntity);
        }
        return view;
    }


}
