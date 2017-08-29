package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.work.ApproverListTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.ApproverAdapter;
import com.zjyeshi.dajiujiao.buyer.task.work.data.ApproverListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/6/21.
 */
public class ApproverListActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.searchEt)
    private EditText searchEt;
    @InjectView(R.id.hintLayout)
    private RelativeLayout hintLayout;//搜索默认
    public static final String TYPE = "type";

    @InjectView(R.id.listView)
    private ListView listView;//员工列表
    private List<ApproverListData.Approver> dataList = new ArrayList<ApproverListData.Approver>();
    private ApproverAdapter approverAdapter;

    private int type;//权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_employee);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("审批人").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        type = getIntent().getIntExtra(TYPE , -1);

        approverAdapter = new ApproverAdapter(ApproverListActivity.this , dataList);
        listView.setAdapter(approverAdapter);

        loadData();
    }

    private void loadData(){
        ApproverListTask approverListTask = new ApproverListTask(ApproverListActivity.this);
        approverListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<ApproverListData>() {
            @Override
            public void failCallback(Result<ApproverListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        approverListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<ApproverListData>() {
            @Override
            public void successCallback(Result<ApproverListData> result) {
                dataList.addAll(result.getValue().getList());
                approverAdapter.notifyDataSetChanged();
            }
        });

        approverListTask.execute(String.valueOf(type));
    }

}
