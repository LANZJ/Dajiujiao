package com.zjyeshi.dajiujiao.buyer.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigappleui.lib.view.tab.sw.BUDefaultTabHost;
import com.xuan.bigappleui.lib.view.tab.sw.BUSwTabHost;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.order.MyOrderListNewAdapter;
import com.zjyeshi.dajiujiao.buyer.common.OrderRequestParam;
import com.zjyeshi.dajiujiao.buyer.common.PreferenceConstants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LtGtEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.GetOrderListData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PerOrder;
import com.zjyeshi.dajiujiao.buyer.task.order.GetOrderListTask;
import com.zjyeshi.dajiujiao.buyer.task.pay.WxPayOrderQueryTask;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 新的订单界面
 * Created by wuhk on 2016/9/14.
 */
public class MyOrderNewActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.tabLayout)
    private BUSwTabHost tabLayout;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;

    private BUPullToRefreshListView listView1;
    private BUPullToRefreshListView listView2;
    private BUPullToRefreshListView listView3;
    private BUPullToRefreshListView listView4;
    private BUPullToRefreshListView listView5;

    private MyOrderListNewAdapter adapter1;
    private MyOrderListNewAdapter adapter2;
    private MyOrderListNewAdapter adapter3;
    private MyOrderListNewAdapter adapter4;
    private MyOrderListNewAdapter adapter5;

    private List<PerOrder> dataList1 = new ArrayList<PerOrder>();
    private List<PerOrder> dataList2 = new ArrayList<PerOrder>();
    private List<PerOrder> dataList3 = new ArrayList<PerOrder>();
    private List<PerOrder> dataList4 = new ArrayList<PerOrder>();
    private List<PerOrder> dataList5 = new ArrayList<PerOrder>();

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;

    //传入的参数
    public static final String USER_TYPE = "type";
    private String type;//类型，卖家/买家
    //店铺Id
    public static final String SHOP_ID = "shopId";
    private String shopId;
    //业务员Id
    public static final String MEMBER_ID = "memberId";
    private String memberId;
    //微信支付结果码
    public static final String WX_PAY_CODE = "errCode";
    private int code;
    //是否是业务员
    public static final String ROLE = "role";
    public static final String USE_SELLER_LIST_URL = "use_seller_list_url";
    private String role;
    //请求地址url
    private String url;

    //请求参数
    private boolean isPullDownRefresh = true;
    private boolean canPullDown = true;
    private boolean canScrollUp = true;

    private String status;
    private String lastTime = "0";
    private LtGtEnum ltGtEnum = LtGtEnum.LT;

    private ChangeOrderStatusReceiver changeOrderStatusReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_order_new);


        initWidgets();

        changeOrderStatusReceiver = new ChangeOrderStatusReceiver() {
            @Override
            public void changeStatus() {
                isPullDownRefresh = true;
                lastTime = "0";
                ltGtEnum = LtGtEnum.LT;
                loadData(false);
            }
        };
        changeOrderStatusReceiver.register();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        changeOrderStatusReceiver.unRegister();
    }

    private void initWidgets(){
        //获取传入的参数，是卖家还是买家
        type = getIntent().getStringExtra(USER_TYPE);
        code = getIntent().getIntExtra(WX_PAY_CODE , 100);
        shopId = getIntent().getStringExtra(SHOP_ID);
        memberId = getIntent().getStringExtra(MEMBER_ID);
        role = getIntent().getStringExtra(ROLE);
        //设置title
        if (type.equals(LoginEnum.SELLER.toString())){
            titleLayout.configTitle("出货");
        }else{
            titleLayout.configTitle("我的订单");
        }
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //业务员不显示历史订单
//        if (!LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)){
//            titleLayout.configRightText("历史订单", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    HistoryOrderActivity.startActivity(MyOrderNewActivity.this , type);
//                }
//            });
//        }
        //判断微信支付结果
        judgeWxPayResult();

        //初始化tab
        initTabhost();

        //第一次加载tab1
        if (type.equals(LoginEnum.SELLER.toString())){
            status = OrderRequestParam.WAITDELIVE;
        }else{
            status = OrderRequestParam.WAITRECEIVE;
        }
        loadData(true);
    }


    /**初始化第一个Tab页
     *
     */
    private void initTab1(){
        textView1 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        if (type.equals(LoginEnum.SELLER.toString())){
            textView1.setText("待发货");
        }else{
            textView1.setText("待收货");
        }
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(0);
            }
        });

        //待收货/待发货
        listView1 = (BUPullToRefreshListView) LayoutInflater.from(this).inflate(R.layout.listview_approve, null);

        listView1.setCanPullDown(true);
        listView1.setCanScrollUp(true);

        listView1.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
                @Override
            public void onPullDownRefresh() {
                if (canPullDown){
                    if (type.equals(LoginEnum.SELLER.toString())){
                        status = OrderRequestParam.WAITDELIVE.toString();
                    }else{
                        status = OrderRequestParam.WAITRECEIVE.toString();
                    }
                    canPullDown = false;
                    isPullDownRefresh = true;
                    lastTime = "0";
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }

            }

            @Override
            public void onScrollUpRefresh() {
                if (canScrollUp){
                    if (type.equals(LoginEnum.SELLER.toString())){
                        status = OrderRequestParam.WAITDELIVE.toString();
                    }else{
                        status = OrderRequestParam.WAITRECEIVE.toString();
                    }
                    canScrollUp = false;
                    isPullDownRefresh = false;
                    if (!Validators.isEmpty(dataList1)){
                        lastTime = String.valueOf(dataList1.get(dataList1.size() -1).getCreateTime());
                    }
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }
            }
        });
        adapter1 = new MyOrderListNewAdapter(MyOrderNewActivity.this , dataList1 , type);
        listView1.setAdapter(adapter1);
    }

    /**初始化第二个Tab页
     *
     */
    private void initTab2(){
        //tab2
        textView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView2.setText("待付款");
        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(1);
            }
        });

        //待付款
        listView2 = (BUPullToRefreshListView) LayoutInflater.from(this).inflate(R.layout.listview_approve, null);

        listView2.setCanPullDown(true);
        listView2.setCanScrollUp(true);

        listView2.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                if (canPullDown){
                    status = OrderRequestParam.WAITPAY;
                    canPullDown = false;
                    isPullDownRefresh = true;
                    lastTime = "0";
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }

            }

            @Override
            public void onScrollUpRefresh() {
                if (canScrollUp){
                    status = OrderRequestParam.WAITPAY;
                    canScrollUp = false;
                    isPullDownRefresh = false;
                    if (!Validators.isEmpty(dataList2)){
                        lastTime = String.valueOf(dataList2.get(dataList2.size() -1).getCreateTime());
                    }
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }
            }
        });
        adapter2 = new MyOrderListNewAdapter(MyOrderNewActivity.this , dataList2 , type);
        listView2.setAdapter(adapter2);
    }

    /**初始化第三个Tab页
     *
     */
    private void initTab3(){
        //tab3
        textView3 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView3.setText("已收货");
        textView3.setTextColor(getResources().getColor(R.color.color_9c9893));
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(2);
            }
        });

        //已收货
        listView3 = (BUPullToRefreshListView) LayoutInflater.from(this).inflate(R.layout.listview_approve, null);

        listView3.setCanPullDown(true);
        listView3.setCanScrollUp(true);

        listView3.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                if (canPullDown){
                    status = OrderRequestParam.ALREADYGET;
                    canPullDown = false;
                    isPullDownRefresh = true;
                    lastTime = "0";
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }

            }

            @Override
            public void onScrollUpRefresh() {
                if (canScrollUp){
                    status = OrderRequestParam.ALREADYGET;
                    canScrollUp = false;
                    isPullDownRefresh = false;
                    if (!Validators.isEmpty(dataList3)){
                        lastTime = String.valueOf(dataList3.get(dataList3.size() -1).getCreateTime());
                    }
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }
            }
        });
        adapter3 = new MyOrderListNewAdapter(MyOrderNewActivity.this , dataList3 , type);
        listView3.setAdapter(adapter3);
    }

    /**初始化第四个Tab页
     *
     */
    private void initTab4(){
        //tab4
        textView4 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView4.setText("全部");
        textView4.setTextColor(getResources().getColor(R.color.color_9c9893));
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(3);
            }
        });

        //已收货
        listView4 = (BUPullToRefreshListView) LayoutInflater.from(this).inflate(R.layout.listview_approve, null);

        listView4.setCanPullDown(true);
        listView4.setCanScrollUp(true);

        listView4.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                if (canPullDown){
                    status = OrderRequestParam.ALL;
                    canPullDown = false;
                    isPullDownRefresh = true;
                    lastTime = "0";
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }

            }

            @Override
            public void onScrollUpRefresh() {
                if (canScrollUp){
                    status = OrderRequestParam.ALL;
                    canScrollUp = false;
                    isPullDownRefresh = false;
                    if (!Validators.isEmpty(dataList4)){
                        lastTime = String.valueOf(dataList4.get(dataList4.size() -1).getCreateTime());
                    }
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }
            }
        });
        adapter4 = new MyOrderListNewAdapter(MyOrderNewActivity.this , dataList4 , type);
        listView4.setAdapter(adapter4);
    }

    private void initTab5(){
        //tab5
        textView5 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView5.setText("售后退款");
        textView5.setTextColor(getResources().getColor(R.color.color_9c9893));
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(4);
            }
        });

        //待付款
        listView5 = (BUPullToRefreshListView) LayoutInflater.from(this).inflate(R.layout.listview_approve, null);

        listView5.setCanPullDown(true);
        listView5.setCanScrollUp(true);

        listView5.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                if (canPullDown){
                    status = OrderRequestParam.REBACK;
                    canPullDown = false;
                    isPullDownRefresh = true;
                    lastTime = "0";
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }

            }

            @Override
            public void onScrollUpRefresh() {
                if (canScrollUp){
                    status = OrderRequestParam.REBACK;
                    canScrollUp = false;
                    isPullDownRefresh = false;
                    if (!Validators.isEmpty(dataList5)){
                        lastTime = String.valueOf(dataList5.get(dataList5.size() -1).getCreateTime());
                    }
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }
            }
        });
        adapter5 = new MyOrderListNewAdapter(MyOrderNewActivity.this , dataList5 , type);
        listView5.setAdapter(adapter5);
    }

    /**初始化TabHost
     *
     */
    private void initTabhost(){
        initTab1();
        initTab2();
        initTab3();
        initTab4();
        tabLayout.setTabsLayoutHeight(135);
        tabLayout.addTabAndContent(textView1, listView1);
        tabLayout.addTabAndContent(textView2, listView2);
        tabLayout.addTabAndContent(textView3, listView3);
        tabLayout.addTabAndContent(textView4, listView4);
        if(type.equals(LoginEnum.BURER.toString())){
            initTab5();
            tabLayout.addTabAndContent(textView5, listView5);
        }

        tabLayout.setTabHost(new BUDefaultTabHost() {
            @Override
            public View getIndicator(Context context) {
                // 设置一个自定义的指示器
                FrameLayout vLayout = new FrameLayout(MyOrderNewActivity.this);
                vLayout.setPadding(0, 10, 0, 0);
                View v = new View(MyOrderNewActivity.this);
                v.setBackgroundColor(getResources().getColor(R.color.color_theme));
                vLayout.addView(v);
                return vLayout;
            }
        });
        tabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                noDataView.hide();
                switch (i) {
                    case 0:
                        textView1.setTextColor(getResources().getColor(R.color.color_theme));
                        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView3.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView4.setTextColor(getResources().getColor(R.color.color_9c9893));
                        if(type.equals(LoginEnum.BURER.toString())){
                            textView5.setTextColor(getResources().getColor(R.color.color_9c9893));
                        }

                        if (type.equals(LoginEnum.SELLER.toString())){
                            status  = OrderRequestParam.WAITDELIVE;
                        }else{
                            status = OrderRequestParam.WAITRECEIVE;
                        }

                        break;
                    case 1:
                        textView1.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView2.setTextColor(getResources().getColor(R.color.color_theme));
                        textView3.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView4.setTextColor(getResources().getColor(R.color.color_9c9893));
                        if(type.equals(LoginEnum.BURER.toString())){
                            textView5.setTextColor(getResources().getColor(R.color.color_9c9893));
                        }
                        status = OrderRequestParam.WAITPAY;

                        break;
                    case 2:
                        textView1.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView3.setTextColor(getResources().getColor(R.color.color_theme));
                        textView4.setTextColor(getResources().getColor(R.color.color_9c9893));
                        if(type.equals(LoginEnum.BURER.toString())){
                            textView5.setTextColor(getResources().getColor(R.color.color_9c9893));
                        }
                        status = OrderRequestParam.ALREADYGET;

                        break;
                    case 3:
                        textView1.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView3.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView4.setTextColor(getResources().getColor(R.color.color_theme));
                        if(type.equals(LoginEnum.BURER.toString())){
                            textView5.setTextColor(getResources().getColor(R.color.color_9c9893));
                        }
                        status = OrderRequestParam.ALL;

                        break;
                    case 4:
                        textView1.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView3.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView4.setTextColor(getResources().getColor(R.color.color_9c9893));
                        if(type.equals(LoginEnum.BURER.toString())){
                            textView5.setTextColor(getResources().getColor(R.color.color_theme));
                        }
                        status = OrderRequestParam.REBACK;
                        break;
                    default:
                        break;
                }
                lastTime = "0";
                ltGtEnum = LtGtEnum.LT;
                isPullDownRefresh = true;
                loadData(false);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.setup();
        tabLayout.initPosition(0);
    }

    /**加载数据
     *
     */
    private void loadData(boolean showDialog){
        if (type.equals(LoginEnum.BURER.toString())) {
            //买家
            url = UrlConstants.LISTORDERV2;
        } else {
            //卖家
            if (Validators.isEmpty(role) || role.equals(USE_SELLER_LIST_URL)){
                url = UrlConstants.SELLERLISTORDERV2;
            }else{
                url = UrlConstants.FILEDWORKMYORDERLISTV2;
            }

        }
        GetOrderListTask getOrderListTask = new GetOrderListTask(MyOrderNewActivity.this, url);
        getOrderListTask.setShowProgressDialog(showDialog);
        getOrderListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetOrderListData>() {
            @Override
            public void failCallback(Result<GetOrderListData> result) {
                ToastUtil.toast(result.getMessage());
                if (isPullDownRefresh) {
                    if (status.equals(OrderRequestParam.WAITDELIVE) || status.equals(OrderRequestParam.WAITRECEIVE)){
                        listView1.onPullDownRefreshComplete("下拉刷新");
                    }else if(status.equals(OrderRequestParam.WAITPAY)){
                        listView2.onPullDownRefreshComplete("下拉刷新");
                    }else if (status.equals(OrderRequestParam.ALREADYGET)){
                        listView3.onPullDownRefreshComplete("下拉刷新");
                    }else if (status.equals(OrderRequestParam.REBACK)){
                        listView5.onPullDownRefreshComplete("下拉刷新");
                    } else{
                        listView4.onPullDownRefreshComplete("下拉刷新");
                    }
                } else {
                    if (status.equals(OrderRequestParam.WAITDELIVE) || status.equals(OrderRequestParam.WAITRECEIVE)){
                        listView1.onPullDownRefreshComplete("上滑更多");
                    }else if(status.equals(OrderRequestParam.WAITPAY)){
                        listView2.onPullDownRefreshComplete("上滑更多");
                    }else if (status.equals(OrderRequestParam.ALREADYGET)){
                        listView3.onPullDownRefreshComplete("上滑更多");
                    }else if (status.equals(OrderRequestParam.REBACK)){
                        listView5.onPullDownRefreshComplete("上滑更多");
                    }else{
                        listView4.onPullDownRefreshComplete("上滑更多");
                    }
                }
            }
        });

        getOrderListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetOrderListData>() {
            @Override
            public void successCallback(Result<GetOrderListData> result) {
                if (isPullDownRefresh) {
                    canPullDown = true;
                    //下拉的时候删除前面的数据，只加载最新一段数据
                    if (status.equals(OrderRequestParam.WAITDELIVE) || status.equals(OrderRequestParam.WAITRECEIVE)){
                        dataList1.clear();
                        dataList1.addAll(result.getValue().getList());
                        listView1.onPullDownRefreshComplete("最新更新" + DateUtils.date2StringBySecond(new Date()));
                        adapter1.notifyDataSetChanged();
                        noDataView.showIfEmpty(dataList1);
                        //重新开启上拉更多
                        listView1.setCanScrollUp(false);
                        listView1.setCanScrollUp(true);
                    }else if(status.equals(OrderRequestParam.WAITPAY)){
                        dataList2.clear();
                        dataList2.addAll(result.getValue().getList());
                        listView2.onPullDownRefreshComplete("最新更新" + DateUtils.date2StringBySecond(new Date()));
                        adapter2.notifyDataSetChanged();
                        noDataView.showIfEmpty(dataList2);
                        //重新开启上拉更多
                        listView2.setCanScrollUp(false);
                        listView2.setCanScrollUp(true);
                    }else if (status.equals(OrderRequestParam.ALREADYGET)){
                        dataList3.clear();
                        dataList3.addAll(result.getValue().getList());
                        listView3.onPullDownRefreshComplete("最新更新" + DateUtils.date2StringBySecond(new Date()));
                        adapter3.notifyDataSetChanged();
                        noDataView.showIfEmpty(dataList3);
                        //重新开启上拉更多
                        listView3.setCanScrollUp(false);
                        listView3.setCanScrollUp(true);
                    }else if (status.equals(OrderRequestParam.REBACK)){
                        dataList5.clear();
                        dataList5.addAll(result.getValue().getList());
                        listView5.onPullDownRefreshComplete("最新更新" + DateUtils.date2StringBySecond(new Date()));
                        adapter5.notifyDataSetChanged();
                        noDataView.showIfEmpty(dataList5);
                        //重新开启上拉更多
                        listView5.setCanScrollUp(false);
                        listView5.setCanScrollUp(true);
                    }else{
                        dataList4.clear();
                        dataList4.addAll(result.getValue().getList());
                        listView4.onPullDownRefreshComplete("最新更新" + DateUtils.date2StringBySecond(new Date()));
                        adapter4.notifyDataSetChanged();
                        noDataView.showIfEmpty(dataList4);
                        //重新开启上拉更多
                        listView4.setCanScrollUp(false);
                        listView4.setCanScrollUp(true);
                    }
                } else {
                    canScrollUp = true;
                    if (status.equals(OrderRequestParam.WAITDELIVE) || status.equals(OrderRequestParam.WAITRECEIVE)){
                        dataList1.addAll(result.getValue().getList());
                        listView1.onScrollUpRefreshComplete("上滑更多");
                        if (Validators.isEmpty(result.getValue().getList())) {
                            listView1.onScrollUpRefreshComplete("");
                            listView1.onScrollUpNoMoreData("没有更多数据了");
                        }
                        adapter1.notifyDataSetChanged();
                        noDataView.showIfEmpty(dataList1);
                    }else if(status.equals(OrderRequestParam.WAITPAY)){
                        dataList2.addAll(result.getValue().getList());
                        listView2.onScrollUpRefreshComplete("上滑更多");
                        if (Validators.isEmpty(result.getValue().getList())) {
                            listView2.onScrollUpRefreshComplete("");
                            listView2.onScrollUpNoMoreData("没有更多数据了");
                        }
                        adapter2.notifyDataSetChanged();
                        noDataView.showIfEmpty(dataList2);
                    }else if (status.equals(OrderRequestParam.ALREADYGET)){
                        dataList3.addAll(result.getValue().getList());
                        listView3.onScrollUpRefreshComplete("上滑更多");
                        if (Validators.isEmpty(result.getValue().getList())) {
                            listView3.onScrollUpRefreshComplete("");
                            listView3.onScrollUpNoMoreData("没有更多数据了");
                        }
                        adapter3.notifyDataSetChanged();
                        noDataView.showIfEmpty(dataList3);
                    }else if (status.equals(OrderRequestParam.REBACK)){
                        dataList5.addAll(result.getValue().getList());
                        listView5.onScrollUpRefreshComplete("上滑更多");
                        if (Validators.isEmpty(result.getValue().getList())) {
                            listView5.onScrollUpRefreshComplete("");
                            listView5.onScrollUpNoMoreData("没有更多数据了");
                        }
                        adapter5.notifyDataSetChanged();
                        noDataView.showIfEmpty(dataList5);
                    }else{
                        dataList4.addAll(result.getValue().getList());
                        listView4.onScrollUpRefreshComplete("上滑更多");
                        if (Validators.isEmpty(result.getValue().getList())) {
                            listView4.onScrollUpRefreshComplete("");
                            listView4.onScrollUpNoMoreData("没有更多数据了");
                        }
                        adapter4.notifyDataSetChanged();
                        noDataView.showIfEmpty(dataList4);
                    }
                }

            }
        });

        if (Validators.isEmpty(shopId)){
            getOrderListTask.execute(status, lastTime, ltGtEnum.getValueStr());
        }else{
            getOrderListTask.execute(status, lastTime, ltGtEnum.getValueStr() , shopId , memberId);
        }
    }


    /**微信支付结果判断
     *
     */
    private void judgeWxPayResult(){
        switch (code){
            case 0 :
                String tradeNo = BPPreferences.instance().getString("tradeNo" , "noSth");
                WxPayOrderQueryTask wxPayOrderQueryTask = new WxPayOrderQueryTask(MyOrderNewActivity.this);
                wxPayOrderQueryTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                    @Override
                    public void successCallback(Result<NoResultData> result) {
                        //支付成功，清空购物车市场支持费用
                        BPPreferences.instance().putFloat(PreferenceConstants.CAR_MARKET_COST , 0.00f);
                        ToastUtil.toast("支付成功");
                    }
                });

                wxPayOrderQueryTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                    @Override
                    public void failCallback(Result<NoResultData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });

                wxPayOrderQueryTask.execute(tradeNo);
                break;
            case -1:
                DialogUtil.confirm(MyOrderNewActivity.this, "支付失败", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case -2:
                DialogUtil.confirm(MyOrderNewActivity.this, "您取消了支付", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            default:
                break;
        }
    }

    /**启动该Activity
     *
     * @param context
     * @param type
     * @param shopId
     * @param memberId
     * @param role
     */
    public static void startOrderActivity(Context context , String type , String shopId , String memberId , String role){
        Intent intent = new Intent();
        intent.putExtra(USER_TYPE , type);
        intent.putExtra(SHOP_ID , shopId);
        intent.putExtra(MEMBER_ID , memberId);
        intent.putExtra(ROLE , role);
        intent.setClass(context , MyOrderNewActivity.class);
        context.startActivity(intent);
    }

    public static void startOrderActivity(Context context , String type , int code ){
        Intent intent = new Intent();
        intent.putExtra(USER_TYPE , type);
        intent.putExtra(WX_PAY_CODE , code);
        intent.setClass(context , MyOrderNewActivity.class);
        context.startActivity(intent);
    }

    public static void startOrderActivity(Context context , String type , String role){
        Intent intent = new Intent();
        intent.putExtra(USER_TYPE , type);
        intent.putExtra(ROLE , role);
        intent.setClass(context , MyOrderNewActivity.class);
        context.startActivity(intent);
    }

    public static void startOrderActivity(Context context , String type){
        Intent intent = new Intent();
        intent.putExtra(USER_TYPE , type);
        intent.setClass(context , MyOrderNewActivity.class);
        context.startActivity(intent);
    }
}
