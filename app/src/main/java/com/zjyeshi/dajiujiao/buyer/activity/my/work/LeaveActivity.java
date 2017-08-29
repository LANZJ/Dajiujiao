package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.LeaveRecord;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveRecordAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LtGtEnum;
import com.zjyeshi.dajiujiao.buyer.task.work.LeaveRecordListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.LeaveRecordListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 请假
 *
 * Created by zhum on 2016/6/14.
 */
public class LeaveActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏

    @InjectView(R.id.listView)
    private BUPullToRefreshListView listView;//请假列表

    private List<LeaveRecord> dataLists = new ArrayList<LeaveRecord>();
    private LeaveRecordAdapter adapter;

    private boolean isPullDownRefresh = true;
    private boolean canPullDown = true;
    private boolean canScrollUp = true;
    private String lastTime = "-1";
    private LtGtEnum ltGtEnum = LtGtEnum.LT;
    private boolean showDialog = true;

    public static boolean isReload = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_leave);

        initWidgets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReload){
            dataLists.clear();
            showDialog = true;
            isPullDownRefresh = true;
            lastTime = "-1";
            ltGtEnum = LtGtEnum.LT;
            initDatas();
        }
    }

    private void initWidgets(){
        titleLayout.configTitle("请假").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configRightText("我要请假", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaveActivity.this,AskForLeaveActivity.class);
                startActivity(intent);
            }
        });

        listView.setCanPullDown(true);
        listView.setCanScrollUp(true);
        listView.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                //下拉刷新
                if (canPullDown){
                    if(!Validators.isEmpty(dataLists)){
                        showDialog = false;
                        isPullDownRefresh = true;
                        canPullDown = false;
                        lastTime = String.valueOf(dataLists.get(0).getCreationTime());
                        ltGtEnum = LtGtEnum.GT;
                        initDatas();
                    }
                }
            }

            @Override
            public void onScrollUpRefresh() {
                //上滑更多
                if (canScrollUp){
                    if (dataLists.size() > 1){
                        showDialog = false;
                        isPullDownRefresh = false;
                        canScrollUp = false;
                        lastTime = String.valueOf(dataLists.get(dataLists.size()-1).getCreationTime());
                        ltGtEnum = LtGtEnum.LT;
                        initDatas();
                    }
                }
            }
        });
        adapter = new LeaveRecordAdapter(LeaveActivity.this,dataLists , false , false , true);
        listView.setAdapter(adapter);

        initDatas();
    }

    private void initDatas(){
        LeaveRecordListTask leaveRecordListTask = new LeaveRecordListTask(LeaveActivity.this);
        leaveRecordListTask.setShowProgressDialog(showDialog);
        leaveRecordListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<LeaveRecordListData>() {
            @Override
            public void failCallback(Result<LeaveRecordListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        leaveRecordListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<LeaveRecordListData>() {
            @Override
            public void successCallback(Result<LeaveRecordListData> result) {
                isReload = false;
                if (isPullDownRefresh){
                    listView.onPullDownRefreshComplete("最新更新:" + DateUtils.date2StringBySecond(new Date()));
                    dataLists.addAll(0 , result.getValue().getList());
                    canPullDown = true;
                    //重新开启上拉更多
                    listView.setCanScrollUp(false);
                    listView.setCanScrollUp(true);


                }else{
                    dataLists.addAll(result.getValue().getList());
                    canScrollUp = true;
                    if (Validators.isEmpty(result.getValue().getList())) {
                        listView.onScrollUpRefreshComplete("");
                        listView.onScrollUpNoMoreData("没有更多数据了");
                    }else{
                        listView.onScrollUpRefreshComplete("上滑更多");
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });

        leaveRecordListTask.execute(lastTime , ltGtEnum.getValueStr());
    }


}