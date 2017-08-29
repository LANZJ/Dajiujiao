package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.receiver.ChangeRemindShowReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.views.UnReadNumView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;

/**
 * 审批
 * <p>
 * Created by zhum on 2016/6/16.
 */
public class ApproveActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.askLayout)
    private RelativeLayout askLayout;//费用申请
    @InjectView(R.id.bxLayout)
    private RelativeLayout bxLayout;//费用报销
    @InjectView(R.id.approveLayout)
    private RelativeLayout approveLayout;//待我审批
    @InjectView(R.id.feeSqUnread)
    private UnReadNumView feeSqUnread;//费用申请View
    @InjectView(R.id.feeBxUnread)
    private UnReadNumView feeBxUnread;//费用报销View
    @InjectView(R.id.waitApproveUnreadView)
    private UnReadNumView waitApproveUnreadView;//数量

    private ChangeRemindShowReceiver changeRemindShowReceiver;//改变提醒显示广播

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_approve);
        initWidgets();

        //改变提醒显示广播
        changeRemindShowReceiver = new ChangeRemindShowReceiver() {
            @Override
            public void change() {
                changeRemindShow();
            }
        };
        changeRemindShowReceiver.register();

        changeRemindShow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != NewRemind.getNewRemind()) {
            changeRemindShow();
        }
//        LoadNewRemindReceiver.notifyReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        changeRemindShowReceiver.unregister();
    }

    private void initWidgets() {
        titleLayout.configTitle("审批").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //最高权限取消费用申请和费用报销
        if (AuthUtil.isApplyAndBxMoneyShow()){
            askLayout.setVisibility(View.VISIBLE);
            bxLayout.setVisibility(View.VISIBLE);
        }else{
            askLayout.setVisibility(View.GONE);
            bxLayout.setVisibility(View.GONE);
        }
        //费用申请
        askLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApproveActivity.this, AskForMoneyActivity.class);
                startActivity(intent);
                //点击之后改变状态
                NewRemind data = NewRemind.getNewRemind();
                data.setFeeApplyChange(false);
                NewRemind.saveToFile();
                changeRemindShow();
            }
        });
        //费用报销
        bxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApproveActivity.this, BxMoneyActivity.class);
                startActivity(intent);
                //点击之后改变状态
                NewRemind data = NewRemind.getNewRemind();
                data.setFeeReimbursement(false);
                NewRemind.saveToFile();
                changeRemindShow();
            }
        });
        //审批
        approveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApproveActivity.this,MyApproveActivity.class);
                startActivity(intent);
            }
        });

        //费用申请和报销的权限都没有才隐藏待我审批，其余都显示
        if (AuthUtil.isMoneyApproveshow()){
            approveLayout.setVisibility(View.VISIBLE);
        }else{
            approveLayout.setVisibility(View.GONE);
        }
    }

    //改变提醒显示
    private void changeRemindShow(){
        NewRemind newRemind = NewRemind.getNewRemind();
        //费用申请变化
        feeSqUnread.setNum(newRemind.getFeeApplyChangeCount());

        //费用报销变化
        feeBxUnread.setNum(newRemind.getFeeReimbursementCount());

        //待审批数量
        waitApproveUnreadView.setNum(newRemind.getFeeUnAuditCount());
    }
}