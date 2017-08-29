package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.RefuseReasonAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.sales.RefuseReason;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.RefreshRebackManagerReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.sales.ReviewRebackApplyTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2017/5/11.
 */

public class RefuseReasonListActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private BUHighHeightListView listView;
    @InjectView(R.id.remarkEt)
    private EditText remarkEt;
    @InjectView(R.id.addBtn)
    private Button addBtn;
    public static final String PARAM_REBACK_ID = "param.reback.id";

    private List<RefuseReason> dataList = new ArrayList<RefuseReason>();
    private RefuseReasonAdapter refuseReasonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_refuse_reason);
        initWidgets();
    }

    private void initWidgets(){

        final String rebackId = getIntent().getStringExtra(PARAM_REBACK_ID);

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).configTitle("拒绝理由");

        dataList.clear();
        dataList.add(new RefuseReason("包装破损"));
        dataList.add(new RefuseReason("货物质量问题"));
        dataList.add(new RefuseReason("货发错了"));
        dataList.add(new RefuseReason("其他"));

        refuseReasonAdapter = new RefuseReasonAdapter(RefuseReasonListActivity.this , dataList);
        listView.setAdapter(refuseReasonAdapter);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder reason = new StringBuilder();
                boolean checked = false;
                for (RefuseReason refuseReason : dataList){
                    if (refuseReason.isChecked()){
                        checked = true;
                        reason.append(refuseReason.getContent());
                        reason.append(",");
                    }
                }
                if (!checked){
                    ToastUtil.toast("至少选择一项拒绝理由");
                    return;
                }

                String checkReason = reason.deleteCharAt(reason.length() - 1).toString();
                String remark = remarkEt.getText().toString();
                if (!Validators.isEmpty(remark)){
                    checkReason = checkReason + "," + remark;
                }

                ReviewRebackApplyTask.reviewReback(RefuseReasonListActivity.this, rebackId, String.valueOf(3), checkReason, new AsyncTaskSuccessCallback<NoResultData>() {
                    @Override
                    public void successCallback(Result<NoResultData> result) {
                        ToastUtil.toast("已拒绝");
                        RebackDetailActivity.reload = true;
                        RefreshRebackManagerReceiver.notifyReceiver();
                        finish();
                    }
                });

            }
        });
    }

    public static void startRefuseReasonActivity(Context context , String rebackId){
        Intent intent = new Intent();
        intent.setClass(context , RefuseReasonListActivity.class);
        intent.putExtra(PARAM_REBACK_ID , rebackId);
        context.startActivity(intent);
    }
}
