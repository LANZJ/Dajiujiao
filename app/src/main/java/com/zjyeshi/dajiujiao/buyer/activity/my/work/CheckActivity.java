package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.receiver.ChangeRemindShowReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.buyer.views.UnReadNumView;

/**
 * 考勤主界面
 *
 * Created by zhum on 2016/6/13.
 */
public class CheckActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.cardLayout)
    private RelativeLayout cardLayout;//打卡
    @InjectView(R.id.cardTv)
    private TextView cardTv;
    @InjectView(R.id.approveLayout)
    private RelativeLayout approveLayout;//待我审核
    @InjectView(R.id.leaveLayout)
    private RelativeLayout leaveLayout;//请假
    @InjectView(R.id.unReadNumView)
    private UnReadNumView unReadNumView;//待审批数量
    @InjectView(R.id.leaveUnread)
    private UnReadNumView leaveUnread;

    private ChangeRemindShowReceiver changeRemindShowReceiver;//改变提醒显示广播

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_check);

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

    private void initWidgets(){

        titleLayout.configTitle("考勤").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //boss是查看员工打卡，普通业务员是打卡
        if (AuthUtil.bossViewKq()){
            cardTv.setText("查看员工打卡");
        }else{
            cardTv.setText("打卡");
        }
        //打卡
        cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AuthUtil.bossViewKq()){
                    Intent intent = new Intent(CheckActivity.this,ChooseEmployeeActivity.class);
                    intent.putExtra("bossView" , true);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(CheckActivity.this,CardActivity.class);
                    startActivity(intent);
                }
            }
        });
        //审批
        approveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CheckActivity.this , LeaveApproveActivity.class);
                startActivity(intent);
            }
        });
        //请假
        leaveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckActivity.this,LeaveActivity.class);
                startActivity(intent);
                //点击之后去掉红点
                NewRemind.getNewRemind().setLeaveStatusChange(false);
                NewRemind.saveToFile();
                changeRemindShow();
            }
        });

        //请假显示
        if (AuthUtil.isLeaveShow()){
            leaveLayout.setVisibility(View.VISIBLE);
        }else{
            leaveLayout.setVisibility(View.GONE);
        }

        //待审批显示
        if (AuthUtil.isLeaveApproveShow()){
            approveLayout.setVisibility(View.VISIBLE);
        }else {
            approveLayout.setVisibility(View.GONE);
        }
    }

    //改变提醒显示
    private void changeRemindShow(){
        NewRemind data = NewRemind.getNewRemind();
        //待审批数量
        unReadNumView.setNum(data.getLeaveUnAuditCount());
        //请假状态变动数量
        leaveUnread.setNum(data.getLeaveChangeCount());
    }
}