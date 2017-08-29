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
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.AskMoneyRecordListAdapter;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CostApplyRecordListData;
import com.zjyeshi.dajiujiao.buyer.views.other.BUPullToRefreshVerticalListView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.AskMoneyRecord;
import com.zjyeshi.dajiujiao.buyer.task.work.CostApplyRecordListTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 费用申请
 *
 * Created by zhum on 2016/6/16.
 */
public class AskForMoneyActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.nameTv)
    private TextView nameTv;//员工姓名
    @InjectView(R.id.listView)
    private BUPullToRefreshVerticalListView listView;//费用申请记录
    private List<AskMoneyRecord> dataLists = new ArrayList<AskMoneyRecord>();
    private AskMoneyRecordListAdapter adapter;

    public static boolean isReload = false;
    private boolean showDialog = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ask_for_money);

        initWidgets();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(isReload){
            initDatas();
        }

        for(AskMoneyRecord askMoneyRecord : dataLists){
            askMoneyRecord.setReadStatus(1);
        }
        adapter.notifyDataSetChanged();
    }

    private void initWidgets() {
        titleLayout.configTitle("特殊费用").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configRightText("填写申请单", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskForMoneyActivity.this,AskMoneyFormActivity.class);
                startActivity(intent);
            }
        });
        nameTv.setVisibility(View.GONE);
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
                //NO SUPPORT
            }
        });
        adapter = new AskMoneyRecordListAdapter(AskForMoneyActivity.this,dataLists);
        listView.setAdapter(adapter);

        initDatas();
    }

    private void initDatas(){
        CostApplyRecordListTask costApplyRecordListTask = new CostApplyRecordListTask(AskForMoneyActivity.this);
        costApplyRecordListTask.setShowProgressDialog(showDialog);
        costApplyRecordListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CostApplyRecordListData>() {
            @Override
            public void failCallback(Result<CostApplyRecordListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        costApplyRecordListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CostApplyRecordListData>() {
            @Override
            public void successCallback(Result<CostApplyRecordListData> result) {
                isReload = false;
                listView.onPullDownRefreshComplete("最新更新：" + DateUtils.date2StringBySecond(new Date()));
                dataLists.clear();
                dataLists.addAll(result.getValue().getList());
                adapter.notifyDataSetChanged();
            }
        });

        costApplyRecordListTask.execute();
    }

}