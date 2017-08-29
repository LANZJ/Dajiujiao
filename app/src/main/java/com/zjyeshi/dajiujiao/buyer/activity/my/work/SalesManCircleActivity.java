package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.os.Bundle;
import android.view.View;

import com.zjyeshi.dajiujiao.buyer.circle.CircleFragment;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.frame.BaseFrameActivity;

/**
 * 业务员酒友圈
 * Created by wuhk on 2016/12/27.
 */
public class SalesManCircleActivity extends BaseFrameActivity {
    private CircleFragment circleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_salesman_circle);
        circleFragment = (CircleFragment) getSupportFragmentManager().findFragmentById(R.id.circleFragment);
        circleFragment.getTitleLayout().configTitle("酒友圈");
    }
}
