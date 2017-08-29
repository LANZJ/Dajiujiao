package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Context;
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
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigappleui.lib.view.tab.sw.BUDefaultTabHost;
import com.xuan.bigappleui.lib.view.tab.sw.BUSwTabHost;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveRecordAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LtGtEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.LeaveRecord;
import com.zjyeshi.dajiujiao.buyer.task.work.WaitApproveLeaveTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.LeaveRecordListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuhk on 2016/6/21.
 */
public class LeaveApproveActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.tabLayout)
    private BUSwTabHost tabLayout;

    private BUPullToRefreshListView listView1;
    private BUPullToRefreshListView listView2;
    private LeaveRecordAdapter adapter1;
    private LeaveRecordAdapter adapter2;
    private List<LeaveRecord> dataLists1;
    private List<LeaveRecord> dataLists2;
    private TextView textView1;
    private TextView textView2;

    //请求参数
    private String status = "1";
    private String lastTime = "0";
    private LtGtEnum ltGtEnum = LtGtEnum.LT;
    private boolean isPullDownRefresh = true;
    private boolean isWait = true;
    private boolean canPullDown = true;
    private boolean canScrollUp = true;

    public static boolean isReload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_approve);

        initWidgets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReload){
            initRequest(true);
            dataLists1.clear();
            dataLists2.clear();
            loadData(true);
        }
    }

    private void initWidgets() {
        titleLayout.configTitle("待我审批").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initTabHost();
        //先将两种状态数据请求下来
        initRequest(true);
        loadData(true);

    }

    private void initTabHost() {
        textView1 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView1.setText("待审批");
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(0);
            }
        });
        textView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView2.setText("已审批");
        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(1);
            }
        });
        //待审批
        listView1 = (BUPullToRefreshListView) LayoutInflater.from(this).inflate(R.layout.listview_approve, null);

        listView1.setCanPullDown(true);
        listView1.setCanScrollUp(true);

        listView1.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                if (canPullDown){
                    status = "1";
                    isWait = true;
                    canPullDown = false;
                    canScrollUp = false;
                    isPullDownRefresh = true;
                    if (!Validators.isEmpty(dataLists1)){
                        lastTime = String.valueOf(dataLists1.get(0).getCreationTime());
                    }
                    ltGtEnum = LtGtEnum.GT;
                    loadData(false);
                }

            }

            @Override
            public void onScrollUpRefresh() {
                if (canScrollUp){
                    status = "1";
                    isWait = true;
                    canScrollUp = false;
                    canPullDown = false;
                    isPullDownRefresh = false;
                    if (!Validators.isEmpty(dataLists1)){
                        lastTime = String.valueOf(dataLists1.get(dataLists1.size() -1).getCreationTime());
                    }
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }
            }
        });

        dataLists1 = new ArrayList<>();
        adapter1 = new LeaveRecordAdapter(LeaveApproveActivity.this,dataLists1 , true , true , false);
        listView1.setAdapter(adapter1);

        //已审批
        listView2 = (BUPullToRefreshListView) LayoutInflater.from(this).inflate(R.layout.listview_approve, null);

        listView2.setCanPullDown(true);
        listView2.setCanScrollUp(true);

        listView2.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                if (canPullDown){
                    status = "2";
                    isWait = false;
                    isPullDownRefresh = true;
                    canPullDown = false;
                    canScrollUp = false;
                    if (!Validators.isEmpty(dataLists2)){
                        lastTime = String.valueOf(dataLists2.get(0).getCreationTime());
                    }
                    ltGtEnum = LtGtEnum.GT;
                    loadData(false);
                }
            }

            @Override
            public void onScrollUpRefresh() {
                if(canScrollUp){
                    status = "2";
                    isWait = false;
                    isPullDownRefresh = false;
                    canPullDown = false;
                    canScrollUp = false;
                    if (!Validators.isEmpty(dataLists2)){
                        lastTime = String.valueOf(dataLists2.get(dataLists2.size() -1).getCreationTime());
                    }
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }
            }
        });
        dataLists2 = new ArrayList<>();
        adapter2 = new LeaveRecordAdapter(LeaveApproveActivity.this,dataLists2 , false , true , false);
        listView2.setAdapter(adapter2);

        tabLayout.addTabAndContent(textView1, listView1);
        tabLayout.addTabAndContent(textView2, listView2);


        tabLayout.setTabHost(new BUDefaultTabHost() {
            @Override
            public View getIndicator(Context context) {
                // 设置一个自定义的指示器
                FrameLayout vLayout = new FrameLayout(LeaveApproveActivity.this);
                vLayout.setPadding(40, 5, 40, 0);
                View v = new View(LeaveApproveActivity.this);
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
                switch (i) {
                    case 0:
                        textView1.setTextColor(getResources().getColor(R.color.color_theme));
                        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
                        break;
                    case 1:
                        textView2.setTextColor(getResources().getColor(R.color.color_theme));
                        textView1.setTextColor(getResources().getColor(R.color.color_9c9893));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.setup();
        tabLayout.initPosition(0);
    }

    private void loadData(final boolean isFirst){
        WaitApproveLeaveTask waitApproveLeaveTask = new WaitApproveLeaveTask(LeaveApproveActivity.this);
        waitApproveLeaveTask.setShowProgressDialog(isFirst);
        waitApproveLeaveTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<LeaveRecordListData>() {
            @Override
            public void failCallback(Result<LeaveRecordListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        waitApproveLeaveTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<LeaveRecordListData>() {
            @Override
            public void successCallback(Result<LeaveRecordListData> result) {
                isReload = false;
                if (isPullDownRefresh){
                    canPullDown = true;
                    canScrollUp = true;
                    if (isWait){
                        listView1.onPullDownRefreshComplete("最新更新:" + DateUtils.date2StringBySecond(new Date()));
                        dataLists1.addAll(0 , result.getValue().getList());
                        textView1.setText("待审批(" + dataLists1.size() + ")");
                        //重新开启上拉更多
                        listView1.setCanScrollUp(false);
                        listView1.setCanScrollUp(true);
                    }else{
                        listView2.onPullDownRefreshComplete("最新更新:" + DateUtils.date2StringBySecond(new Date()));
                        dataLists2.addAll(0 , result.getValue().getList());
                        textView2.setText("已审批(" + dataLists2.size() + ")");
                        //重新开启上拉更多
                        listView2.setCanScrollUp(false);
                        listView2.setCanScrollUp(true);
                    }
                }else{
                    canScrollUp = true;
                    canPullDown = true;
                    if (isWait){
                        dataLists1.addAll(result.getValue().getList());
                        textView1.setText("待审批(" + dataLists1.size() + ")");
                        if (Validators.isEmpty(result.getValue().getList())){
                            listView1.onScrollUpRefreshComplete("");
                            listView1.onScrollUpNoMoreData("没有更多数据了");
                        }else{
                            listView1.onScrollUpRefreshComplete("上滑更多");
                        }
                    }else{
                        dataLists2.addAll(result.getValue().getList());
                        textView2.setText("已审批(" + dataLists2.size() + ")");
                        if(Validators.isEmpty(result.getValue().getList())){
                            listView2.onScrollUpNoMoreData("");
                            listView2.onScrollUpNoMoreData("没有更多数据了");
                        }else{
                            listView2.onScrollUpRefreshComplete("上滑更多");
                        }
                    }
                }
                if (isWait){
                    adapter1.notifyDataSetChanged();
                }else{
                    adapter2.notifyDataSetChanged();
                }
                if (isFirst){
                    initRequest(false);
                    loadData(false);
                }
            }
        });

        waitApproveLeaveTask.execute(status , lastTime , ltGtEnum.getValueStr());
    }

    private void initRequest(boolean isWait){
        this.isWait = isWait;
        if (isWait){
            status = "1";
        }else{
            status = "2";
        }
        lastTime = "0";
        ltGtEnum = LtGtEnum.GT;
    }
}
