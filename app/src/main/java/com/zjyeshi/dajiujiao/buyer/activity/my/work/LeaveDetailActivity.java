package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveDetailImageAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ApproveEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.LeaveTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.UserData;
import com.zjyeshi.dajiujiao.buyer.task.work.LeaveApproveToNextTask;
import com.zjyeshi.dajiujiao.buyer.task.work.PassLeaveTask;
import com.zjyeshi.dajiujiao.buyer.task.work.RefuseLeaveTask;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.GetUserInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.work.LeaveInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.ApproverListData;
import com.zjyeshi.dajiujiao.buyer.task.work.data.LeaveInfoData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 请假记录详情
 *
 * Created by zhum on 2016/6/14.
 */
public class LeaveDetailActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.typeTv)
    private TextView typeTv;
    @InjectView(R.id.openTimeTv)
    private TextView openTimeTv;
    @InjectView(R.id.endTimeTv)
    private TextView endTimeTv;
    @InjectView(R.id.leaveDaysTv)
    private TextView leaveDaysTv;
    @InjectView(R.id.bzTv)
    private TextView bzTv;
    @InjectView(R.id.gridView)
    private MyGridView gridView;
    @InjectView(R.id.spNameTv)
    private TextView spNameTv;
    @InjectView(R.id.spAvatarIv)
    private ImageView spAvatarIv;
    @InjectView(R.id.opLayout)
    private LinearLayout opLayout;
    @InjectView(R.id.personTv)
    private TextView personTv;
    @InjectView(R.id.passLayout)
    private RelativeLayout passLayout;
    @InjectView(R.id.refuseLayout)
    private RelativeLayout refuseLayout;
    @InjectView(R.id.nextLayout)
    private RelativeLayout nextLayout;

    private LeaveDetailImageAdapter leaveDetailImageAdapter;
    private List<String> dataList = new ArrayList<String>();
    private String leaveId;
    private boolean canApprove;//表示可以进行审批操作
    private boolean showClock;//显示请假人，表示已审批的

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_leave_detail);

        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("请假详情").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        leaveId = getIntent().getStringExtra("leaveId");
        canApprove = getIntent().getBooleanExtra("canApprove" , false);
        showClock = getIntent().getBooleanExtra("showClockPerson" , false);

        if (canApprove){
            opLayout.setVisibility(View.VISIBLE);
        }else{
            opLayout.setVisibility(View.GONE);
        }

        leaveDetailImageAdapter = new LeaveDetailImageAdapter(LeaveDetailActivity.this , dataList);
        gridView.setAdapter(leaveDetailImageAdapter);

        initDatas();

        passLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass();
            }
        });

        refuseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refuse();
            }
        });

        nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LeaveDetailActivity.this , ApproverListActivity.class);
                intent.putExtra(ApproverListActivity.TYPE , ApproveEnum.LEAVE.getValue());
                startActivityForResult(intent , 111);
            }
        });
    }

    private void initDatas(){
        LeaveInfoTask leaveInfoTask = new LeaveInfoTask(LeaveDetailActivity.this);
        leaveInfoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<LeaveInfoData>() {
            @Override
            public void failCallback(Result<LeaveInfoData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        leaveInfoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<LeaveInfoData>() {
            @Override
            public void successCallback(Result<LeaveInfoData> result) {
                LeaveInfoData leaveInfoData = result.getValue();
                typeTv.setText(LeaveTypeEnum.valueOf(leaveInfoData.getType()).toString());
                openTimeTv.setText(leaveInfoData.getStartTime());
                endTimeTv.setText(leaveInfoData.getEndTime());
                String[] leaves = leaveInfoData.getLeaveDays().split(",");
                if (leaves.length == 3){
                    leaveDaysTv.setText( leaves[0] +  "天" + leaves[1] + "时" + leaves[2] + "分");
                }else{
                    leaveDaysTv.setText(leaveInfoData.getLeaveDays());
                }

                if (!Validators.isEmpty(leaveInfoData.getRemark())){
                    bzTv.setText(leaveInfoData.getRemark());
                }else{
                    bzTv.setText("未添加备注");
                }
                if (showClock){
                    personTv.setText("请假人");
                    confidApprover(leaveInfoData.getClockPerson());
                }else{
                    personTv.setText("审批人");
                    confidApprover(leaveInfoData.getApprover());
                }
                if (!Validators.isEmpty(leaveInfoData.getPics())){
                   String[] pics = leaveInfoData.getPics().split(",");
                   for (int i = 0 ; i < pics.length ; i++){
                       dataList.add(pics[i]);
                   }
                    leaveDetailImageAdapter.notifyDataSetChanged();
                }
            }
        });

        leaveInfoTask.execute(leaveId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode){
            return;
        }
        switch (requestCode){
            case 111:
                ApproverListData.Approver approver = JSON.parseObject(data.getStringExtra("approver") , ApproverListData.Approver.class);
                nextApprove(approver.getId());
                break;
        }
    }

    //设置审批人信息
    private void confidApprover(String id){
        GetUserInfoTask getUserInfoTask = new GetUserInfoTask(LeaveDetailActivity.this);
        getUserInfoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<UserData>() {
            @Override
            public void failCallback(Result<UserData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getUserInfoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<UserData>() {
            @Override
            public void successCallback(Result<UserData> result) {
                spNameTv.setText(result.getValue().getName());
                GlideImageUtil.glidImage(spAvatarIv , result.getValue().getPic() , R.drawable.default_img);
            }
        });

        getUserInfoTask.execute(id);
    }

    //通过
    private void pass(){
        PassLeaveTask passLeaveTask = new PassLeaveTask(LeaveDetailActivity.this);
        passLeaveTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        passLeaveTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("通过审批");
                LeaveApproveActivity.isReload = true;
                finish();
            }
        });

        passLeaveTask.execute(leaveId);
    }

    //拒绝
    private void refuse(){
        RefuseLeaveTask refuseLeaveTask = new RefuseLeaveTask(LeaveDetailActivity.this);
        refuseLeaveTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        refuseLeaveTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("你已拒绝请假要求");
                LeaveApproveActivity.isReload = true;
                finish();
            }
        });

        refuseLeaveTask.execute(leaveId);
    }

    private void nextApprove(String toMemberId){
        LeaveApproveToNextTask leaveApproveToNextTask = new LeaveApproveToNextTask(LeaveDetailActivity.this);
        leaveApproveToNextTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        leaveApproveToNextTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("转审成功");
                finish();
                LeaveApproveActivity.isReload = true;
            }
        });

        leaveApproveToNextTask.execute(leaveId , toMemberId);
    }

}