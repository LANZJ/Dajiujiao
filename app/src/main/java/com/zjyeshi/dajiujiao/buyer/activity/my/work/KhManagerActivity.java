package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.MyOrderNewActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.OrderManagerActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.ChangeRemindShowReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.views.UnReadNumView;

/**
 * 客户管理
 *
 * Created by zhum on 2016/6/14.
 */
public class KhManagerActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.hmcLayout)
    private RelativeLayout hmcLayout;//花名册
    @InjectView(R.id.orderManageLayout)
    private RelativeLayout orderManageLayout;//订单管理
    @InjectView(R.id.kcManagerLayout)
    private RelativeLayout kcManagerLayout;//库存管理
    @InjectView(R.id.canBuyLayout)
    private RelativeLayout canBuyLayout;//可够品项
    @InjectView(R.id.helpOrderLayout)
    private RelativeLayout helpOrderLayout;//代客户下单
    @InjectView(R.id.orderCountUnRead)
    private UnReadNumView orderCountUnRead;//订单数量

    private ChangeRemindShowReceiver changeRemindShowReceiver;//改变显示广播
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kh_manager);

        initWidgets();

        changeRemindShowReceiver = new ChangeRemindShowReceiver() {
            @Override
            public void change() {
                changeRemindShow();
            }
        };
        changeRemindShowReceiver.register();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != NewRemind.getNewRemind()) {
            changeRemindShow();
        }
        LoadNewRemindReceiver.notifyReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        changeRemindShowReceiver.unregister();
    }

    private void initWidgets(){
        titleLayout.configTitle("客户管理").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //显示订单数量
        changeRemindShow();

        //终端没有花名册
        if(AuthUtil.isHmcShow()){
            hmcLayout.setVisibility(View.VISIBLE);
        }else{
            hmcLayout.setVisibility(View.GONE);
        }

        //花名册
        hmcLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是boss或者超管，先看业务员列表，点击业务员在查看对应花名册
                if (LoginedUser.getLoginedUser().isMaxLeavel()){
                    Intent intent = new Intent(KhManagerActivity.this,MemberListActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(KhManagerActivity.this,HmcActivity.class);
                    startActivity(intent);
                }
            }
        });

        //订单管理
        orderManageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AuthUtil.goOrderDirect()){
                    //终端业务员或者终端角色，直接进入订单
                    MyOrderNewActivity.startOrderActivity(KhManagerActivity.this , LoginEnum.SELLER.toString() , MyOrderNewActivity.USE_SELLER_LIST_URL);
                }else{
                    Intent intent = new Intent(KhManagerActivity.this,OrderManagerActivity.class);
                    startActivity(intent);
                }
            }
        });

        //客户管理
        kcManagerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KhManagerActivity.this,StockActivity.class);
                startActivity(intent);
            }
        });

        //可够品项
        canBuyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CanBuyManageActivity.startActivity(KhManagerActivity.this);
            }
        });
        //终端和终端业务员不显示可购
        if (AuthUtil.isTerminalOrSales()){
            canBuyLayout.setVisibility(View.GONE);
        }else{
            canBuyLayout.setVisibility(View.VISIBLE);
        }

        //代为下单
        helpOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KhManagerActivity.this,HmcActivity.class);
                intent.putExtra(HmcActivity.IS_ORDERED , true);
                startActivity(intent);
            }
        });
        //是否显示代为下单
        if (AuthUtil.showHelpOrdered()){
            helpOrderLayout.setVisibility(View.VISIBLE);
        }else{
            helpOrderLayout.setVisibility(View.GONE);
        }
    }

    private void changeRemindShow(){
        NewRemind data = NewRemind.getNewRemind();
        orderCountUnRead.setNum(data.getOrderCount());
    }
}