package com.zjyeshi.dajiujiao.buyer.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.MemberFilterActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.order.OrderManagerListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Order;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.OrderFilterReceiver;
import com.zjyeshi.dajiujiao.buyer.task.work.MyOrderListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.MyOrderListData;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单管理
 *
 * Created by zhum on 2016/6/15.
 */
public class OrderManagerActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.totalMoneyTv)
    private TextView totalMoneyTv;//总金额
    @InjectView(R.id.listView)
    private ListView listView;
    private OrderManagerListAdapter adapter;
    private List<Order> dataLists = new ArrayList<Order>();

    private float totalMoney = 0.00f;
    private boolean isFirst = true;
    private String memberId = "";

    private OrderFilterReceiver orderFilterReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_manager);

        initWidgets();

        orderFilterReceiver = new OrderFilterReceiver() {
            @Override
            public void filter(String id) {
                memberId = id;
                initDatas(true);
            }
        };
        orderFilterReceiver.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderFilterReceiver.unRegister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst){
            initDatas(false);
        }
    }

    private void initWidgets(){
        titleLayout.configTitle("订单管理").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (AuthUtil.isOrderManagerFilterShow()){
            titleLayout.configRightText("筛选", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name;
                    if (!Validators.isEmpty(LoginedUser.getLoginedUser().getShopName())){
                        name = LoginedUser.getLoginedUser().getShopName();
                    }else{
                        name = LoginedUser.getLoginedUser().getName();
                    }
                    MemberFilterActivity.startActivity(OrderManagerActivity.this , AuthUtil.getFilterRole() , name
                            , "" , true);
                }
            });
        }

        adapter = new OrderManagerListAdapter(OrderManagerActivity.this,dataLists);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = dataLists.get(position);
                MyOrderNewActivity.startOrderActivity(OrderManagerActivity.this, LoginEnum.SELLER.toString() ,
                        order.getId() , memberId , "new");
            }
        });

        initDatas(true);
    }

    private void initDatas(boolean show){
        MyOrderListTask myOrderListTask = new MyOrderListTask(OrderManagerActivity.this);
        myOrderListTask.setShowProgressDialog(show);
        myOrderListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<MyOrderListData>() {
            @Override
            public void successCallback(Result<MyOrderListData> result) {
                isFirst = false;
                dataLists.clear();
                dataLists.addAll(result.getValue().getList());
                adapter.notifyDataSetChanged();
                if (!Validators.isEmpty(dataLists)){
                    totalMoney = 0.00f;
                    for (Order order : dataLists){
                        totalMoney = totalMoney + Float.parseFloat(order.getAmount());
                    }
                }
                totalMoneyTv.setText(ExtraUtil.format(totalMoney) + "元");
            }
        });

        myOrderListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<MyOrderListData>() {
            @Override
            public void failCallback(Result<MyOrderListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        myOrderListTask.execute(memberId);
    }
}