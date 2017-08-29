package com.zjyeshi.dajiujiao.buyer.adapter.seller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.views.income.DetailIncomeView;
import com.zjyeshi.dajiujiao.buyer.entity.sellerincome.DetailIncomeEntity;

import java.util.List;

/**
 * 我的收入适配器
 *
 * Created by wuhk on 2015/11/6.
 */
public class MyIncomeAdapter extends MBaseAdapter {
    private Context context;
    private List<DetailIncomeEntity> dataList;

    public MyIncomeAdapter(Context context, List<DetailIncomeEntity> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        DetailIncomeEntity detailIncomeEntity = dataList.get(position);

        if (null == view){
            view = new DetailIncomeView(context);
        }

        ((DetailIncomeView)view).bindData(detailIncomeEntity);
        return view;
    }
}
