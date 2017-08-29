package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CostPaidRecordListData;
import com.zjyeshi.dajiujiao.buyer.views.other.BUPullToRefreshVerticalListView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.CostRecordListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.CostRecord;
import com.zjyeshi.dajiujiao.buyer.task.work.CostPaidRecordListTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 费用报销
 * <p>
 * Created by zhum on 2016/6/16.
 */
public class BxMoneyActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.nameTv)
    private TextView nameTv;//员工姓名
    @InjectView(R.id.listView)
    private BUPullToRefreshVerticalListView listView;//费用申请记录
    private List<CostRecord> dataLists = new ArrayList<CostRecord>();
    private CostRecordListAdapter adapter;

    public static boolean isReload = false;
    private boolean showDialog = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bx_money);

        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configTitle("费用报销").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameTv.setVisibility(View.GONE);
        titleLayout.configRightText("填写报销单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BxMoneyActivity.this,BxFormActivity.class);
                startActivity(intent);
            }
        });

        listView.setCanPullDown(true);
        listView.setCanScrollUp(false);
        listView.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                showDialog = false;
                initDatas();
            }

            @Override
            public void onScrollUpRefresh() {
                //NOT SUPPORT
            }
        });

        adapter = new CostRecordListAdapter(BxMoneyActivity.this,dataLists);
        listView.setAdapter(adapter);

        initDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReload){
            showDialog = false;
            initDatas();
        }

        for (CostRecord costRecord : dataLists){
            costRecord.setReadStatus(1);
        }
        adapter.notifyDataSetChanged();
    }

    private void initDatas(){
        CostPaidRecordListTask costPaidRecordListTask = new CostPaidRecordListTask(BxMoneyActivity.this);
        costPaidRecordListTask.setShowProgressDialog(showDialog);
        costPaidRecordListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CostPaidRecordListData>() {
            @Override
            public void failCallback(Result<CostPaidRecordListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        costPaidRecordListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CostPaidRecordListData>() {
            @Override
            public void successCallback(Result<CostPaidRecordListData> result) {
                listView.onPullDownRefreshComplete("最新更新:" + DateUtils.date2StringBySecond(new Date()));
                dataLists.clear();
                dataLists.addAll(result.getValue().getList());
                adapter.notifyDataSetChanged();
            }
        });

        costPaidRecordListTask.execute();
    }

}