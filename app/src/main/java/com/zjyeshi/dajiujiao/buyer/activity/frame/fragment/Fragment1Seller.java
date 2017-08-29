package com.zjyeshi.dajiujiao.buyer.activity.frame.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.MemberListActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.MyOrderNewActivity;
import com.zjyeshi.dajiujiao.buyer.activity.seller.MyIncomeActivity;
import com.zjyeshi.dajiujiao.buyer.activity.seller.ProductBuyActivity;
import com.zjyeshi.dajiujiao.buyer.activity.seller.ProductListActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.ShopDetailActivity;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;

/**
 * 卖家的主界面
 * Created by xuan on 15/10/29.
 */
public class Fragment1Seller extends BaseFragment {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.buyProductLayout)
    private View buyProductLayout;//进货

    @InjectView(R.id.orderManageLayout)
    private View orderManageLayout;//出货

    @InjectView(R.id.customerLayout)
    private View customerLayout;//我的客户

    @InjectView(R.id.productManageLayout)
    private View productManageLayout;//酒库

    @InjectView(R.id.myIncomeLayout)
    private View myIncomeLayout;//钱包

    @Override
    protected int initFragmentView() {
        return R.layout.seller_layout_frame_above;
    }

    @Override
    protected void initFragmentWidgets(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configTitle(LoginedUser.getLastLoginedUserInfo().getName());

        //进货
        buyProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginedUser.checkLogined()) {
                    //如果上级只有一家就直接店铺详情
                    if (DaoFactory.getShopsDao().findAll().size() == 1) {
                        Intent intent = new Intent();
                        intent.putExtra(ShopDetailActivity.PARAM_SHOPID, DaoFactory.getShopsDao().findAll().get(0).getShop().getId());
                        intent.setClass(getActivity(), ShopDetailActivity.class);
                        getActivity().startActivity(intent);
                    } else {
                        //多家店铺进入店铺列表
                        startActivity(new Intent(getActivity(), ProductBuyActivity.class));
                    }
                }
            }
        });

        //出货
        orderManageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginedUser.checkLogined()) {
                    MyOrderNewActivity.startOrderActivity(getActivity(), LoginEnum.SELLER.toString());
                }
            }
        });

        //我的客户
        customerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginedUser.checkLogined()) {
                    Intent intent = new Intent(getActivity(), MemberListActivity.class);
                    startActivity(intent);
                }
            }
        });

        //酒库
        productManageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginedUser.checkLogined()) {
                    startActivity(new Intent(getActivity(), ProductListActivity.class));
                }
            }
        });

        //钱包
        myIncomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginedUser.checkLogined()) {
                    startActivity(new Intent(getActivity(), MyIncomeActivity.class));
                }
            }
        });

    }

}
