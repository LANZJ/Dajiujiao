package com.zjyeshi.dajiujiao.buyer.adapter.my;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.views.coupon.CouponEntity;
import com.zjyeshi.dajiujiao.buyer.views.coupon.CouponView;

import java.util.List;

/**
 * 优惠券
 * Created by wuhk on 2016/9/22.
 */
public class CouponAdapter extends MBaseAdapter {
    private Context context;
    private List<CouponEntity> dataList;

    public CouponAdapter(Context context, List<CouponEntity> dataList) {
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
            view = new CouponView(context);
        }
        CouponEntity couponEntity = dataList.get(position);
        ((CouponView)view).bindData(couponEntity);

        return view;
    }
}
