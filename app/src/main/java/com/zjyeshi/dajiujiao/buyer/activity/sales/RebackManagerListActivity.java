package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigappleui.lib.view.tab.sw.BUDefaultTabHost;
import com.xuan.bigappleui.lib.view.tab.sw.BUSwTabHost;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.RebackManagerListAdapter;
import com.zjyeshi.dajiujiao.buyer.common.OrderRequestParam;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.RebackManagerStatusEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.RefreshRebackManagerReceiver;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetRebackListTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.RebackListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2017/5/10.
 */

public class RebackManagerListActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.tabLayout)
    private BUSwTabHost tabLayout;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;

    private ListView listView1;
    private ListView listView2;

    private TextView textView1;
    private TextView textView2;

    private RebackManagerListAdapter adapter1;
    private RebackManagerListAdapter adapter2;

    private List<RebackListData.Reback> dataList1 = new ArrayList<RebackListData.Reback>();
    private List<RebackListData.Reback> dataList2 = new ArrayList<RebackListData.Reback>();

    private int status = GetRebackListTask.NO_DEAL;

    private RefreshRebackManagerReceiver refreshRebackManagerReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reback_manager);
        initWidgets();

        refreshRebackManagerReceiver = new RefreshRebackManagerReceiver() {
            @Override
            public void refresh() {
                loadData();
            }
        };

        refreshRebackManagerReceiver.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshRebackManagerReceiver.unregister();
    }

    private void initWidgets() {
        initTabhost();

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).configTitle("申请退货");

        //第一次加载tab1
        loadData();
    }


    /**
     * 初始化TabHost
     */
    private void initTabhost() {
        initTab1();
        initTab2();

        tabLayout.setTabsLayoutHeight(135);
        tabLayout.addTabAndContent(textView1, listView1);
        tabLayout.addTabAndContent(textView2, listView2);

        tabLayout.setTabHost(new BUDefaultTabHost() {
            @Override
            public View getIndicator(Context context) {
                // 设置一个自定义的指示器
                FrameLayout vLayout = new FrameLayout(RebackManagerListActivity.this);
                vLayout.setPadding(0, 10, 0, 0);
                View v = new View(RebackManagerListActivity.this);
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
                        status = GetRebackListTask.NO_DEAL;
                        break;
                    case 1:
                        textView1.setTextColor(getResources().getColor(R.color.color_9c9893));
                        textView2.setTextColor(getResources().getColor(R.color.color_theme));
                        status = GetRebackListTask.DEALED;
                        break;
                    default:
                        break;
                }
                loadData();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.setup();
        tabLayout.initPosition(0);
    }


    /**
     * 初始化第一个Tab页
     */
    private void initTab1() {
        textView1 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView1.setText("待处理");
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(0);
            }
        });

        //待收货/待发货
        listView1 = (ListView) LayoutInflater.from(this).inflate(R.layout.dg_include_listview, null);

        adapter1 = new RebackManagerListAdapter(RebackManagerListActivity.this, dataList1, false);
        listView1.setAdapter(adapter1);
    }

    /**
     * 初始化第二个Tab页
     */
    private void initTab2() {
        //tab2
        textView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView2.setText("已处理");
        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(1);
            }
        });

        //待付款
        listView2 = (ListView) LayoutInflater.from(this).inflate(R.layout.dg_include_listview, null);

        adapter2 = new RebackManagerListAdapter(RebackManagerListActivity.this, dataList2, true);
        listView2.setAdapter(adapter2);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        GetRebackListTask.getRebackList(RebackManagerListActivity.this, LoginedUser.getLoginedUser().getShopId(), status, new AsyncTaskSuccessCallback<RebackListData>() {
            @Override
            public void successCallback(Result<RebackListData> result) {
                if (status == RebackManagerStatusEnum.WAIT_REVIEW.getValue()) {
                    dataList1.clear();
                    dataList1.addAll(result.getValue().getList());
                    adapter1.notifyDataSetChanged();
                    noDataView.showIfEmpty(dataList1);
                } else if (status == RebackManagerStatusEnum.PASS_REVIEW.getValue()) {
                    dataList2.clear();
                    dataList2.addAll(result.getValue().getList());
                    adapter2.notifyDataSetChanged();
                    noDataView.showIfEmpty(dataList2);
                }
            }
        });
    }


    public static void startReBackActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RebackManagerListActivity.class);
        context.startActivity(intent);
    }
}
