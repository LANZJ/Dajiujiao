package com.zjyeshi.dajiujiao.buyer.activity.account;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.account.GetApplyLogInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.account.data.CashLogInfo;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.Date;

/**
 * 提现详情
 * Created by wuhk on 2016/5/30.
 */
public class ApplyDetailActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    //提现记录
    @InjectView(R.id.applyLayout)
    private LinearLayout applylayout;
    @InjectView(R.id.applyStatusTv)
    private TextView applyStatusTv;
    @InjectView(R.id.applyMoneyTv)
    private TextView applyMoneyTv;
    @InjectView(R.id.applyTypeTv)
    private TextView applyTypeTv;
    @InjectView(R.id.secondView)
    private View secondView;
    @InjectView(R.id.thirdStatus)
    private View thirdStatus;
    @InjectView(R.id.passTv)
    private TextView passTv;
    @InjectView(R.id.applyToTv)
    private TextView applyToTv;
    @InjectView(R.id.applyOpTv)
    private TextView applyOpTv;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trade_detail);
        initWidgets();
    }

    private void initWidgets() {
        id = getIntent().getStringExtra("id");
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("提现详情");

        GetApplyLogInfoTask getApplyLogInfoTask = new GetApplyLogInfoTask(ApplyDetailActivity.this);
        getApplyLogInfoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CashLogInfo>() {
            @Override
            public void failCallback(Result<CashLogInfo> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getApplyLogInfoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CashLogInfo>() {
            @Override
            public void successCallback(Result<CashLogInfo> result) {
                show(result.getValue());
            }
        });

        getApplyLogInfoTask.execute(id);

    }

    //显示界面
    private void show(CashLogInfo data) {
        //提现
        applylayout.setVisibility(View.VISIBLE);
        applyMoneyTv.setText(ExtraUtil.format(Float.parseFloat(data.getAmount())/100));
        applyTypeTv.setVisibility(View.GONE);
        applyToTv.setText(data.getCashNumber() + "   " + data.getPassportName());
        applyOpTv.setText(DateUtils.date2StringBySecond(new Date(data.getCreationTime())));
        int state = data.getCashStatus();
        switch (state) {
            case 1:
                //未审核
                applyStatusTv.setText("处理中");
                secondView.setBackgroundResource(R.drawable.shape_apply_not);
                thirdStatus.setBackgroundResource(R.drawable.shape_apply_not);
                passTv.setTextColor(Color.parseColor("#cccccc"));
                passTv.setText("审核通过");
                break;
            case 2:
                //通过审核
                applyStatusTv.setText("交易完成");
                secondView.setBackgroundResource(R.drawable.shape_apply_ok);
                thirdStatus.setBackgroundResource(R.drawable.shape_apply_ok);
                passTv.setTextColor(Color.parseColor("#5d0110"));
                passTv.setText("审核通过");
                break;
            case 3:
                //未通过审核
                applyStatusTv.setText("未通过审核");
                secondView.setBackgroundResource(R.drawable.shape_apply_not);
                thirdStatus.setBackgroundResource(R.drawable.shape_apply_not);
                passTv.setTextColor(Color.parseColor("#cccccc"));
                passTv.setText("审核未通过");
                break;
            case 4:
                applyStatusTv.setText("交易完成");
                secondView.setBackgroundResource(R.drawable.shape_apply_ok);
                thirdStatus.setBackgroundResource(R.drawable.shape_apply_ok);
                passTv.setTextColor(Color.parseColor("#5d0110"));
                passTv.setText("审核通过");
                break;
            default:
                break;
        }
    }
}
