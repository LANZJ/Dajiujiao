package com.zjyeshi.dajiujiao.buyer.activity.account;

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
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.CashRecordAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LtGtEnum;
import com.zjyeshi.dajiujiao.buyer.task.account.GetCashLogListTask;
import com.zjyeshi.dajiujiao.buyer.task.account.data.CashLogList;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 提现记录
 * Created by wuhk on 2016/7/4.
 */
public class ApplyCashRecordActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private BUPullToRefreshListView listView;

    private List<CashLogList.CashLog> dataList = new ArrayList<CashLogList.CashLog>();
    private CashRecordAdapter cashRecordAdapter;

    private boolean isPullDownRefresh = true;
    private boolean showDialog = true;
    private boolean canPullDown = true;
    private boolean canScrollUp = true;
    //上传参数
    private String lastTime = "0" ;
    private LtGtEnum ltGtEnum = LtGtEnum.LT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_apply_record);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("提现记录");

        listView.setCanPullDown(true);
        listView.setCanScrollUp(true);
        listView.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                if (canPullDown){
                    showDialog = false;
                    canPullDown = false;
                    if (!Validators.isEmpty(dataList)){
                        lastTime = String.valueOf(dataList.get(0).getCreationTime());
                    }
                    ltGtEnum = LtGtEnum.GT;
                    loadData();
                }
            }

            @Override
            public void onScrollUpRefresh() {
                if (canScrollUp){
                    showDialog = false;
                    canScrollUp = false;
                    if (!Validators.isEmpty(dataList)){
                        lastTime = String.valueOf(dataList.get(dataList.size() - 1).getCreationTime());
                    }
                    ltGtEnum = LtGtEnum.LT;
                    loadData();
                }
            }
        });

        cashRecordAdapter = new CashRecordAdapter(ApplyCashRecordActivity.this , dataList);
        listView.setAdapter(cashRecordAdapter);

        loadData();

    }

    private void loadData(){
        GetCashLogListTask getCashLogListTask = new GetCashLogListTask(ApplyCashRecordActivity.this);
        getCashLogListTask.setShowProgressDialog(showDialog);
        getCashLogListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CashLogList>() {
            @Override
            public void failCallback(Result<CashLogList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getCashLogListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CashLogList>() {
            @Override
            public void successCallback(Result<CashLogList> result) {
                if (isPullDownRefresh){
                    listView.onPullDownRefreshComplete("最新更新:" + DateUtils.date2StringBySecond(new Date()));
                    canPullDown = true;
                    dataList.addAll(0 , result.getValue().getList());
                    //重新开启上拉刷新
                    listView.setCanScrollUp(false);
                    listView.setCanScrollUp(true);
                }else{
                    dataList.addAll(result.getValue().getList());
                    canScrollUp = true;
                    if (Validators.isEmpty(result.getValue().getList())){
                        listView.onScrollUpRefreshComplete("");
                        listView.onScrollUpNoMoreData("没有更多数据了");
                    }else{
                        listView.onScrollUpRefreshComplete("");
                        listView.onScrollUpNoMoreData("没有更多数据了");
                    }
                }
                cashRecordAdapter.notifyDataSetChanged();
            }
        });

        getCashLogListTask.execute(lastTime , ltGtEnum.getValueStr());
    }
}
