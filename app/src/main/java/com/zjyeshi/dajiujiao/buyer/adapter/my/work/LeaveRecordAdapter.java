package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.LeaveDetailActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.LeaveTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.ReviewStatusEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.LeaveRecord;
import com.zjyeshi.dajiujiao.buyer.utils.FriendlyTimeUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.Date;
import java.util.List;

/**
 * 请假记录适配器
 *
 * Created by zhum on 2016/6/14.
 */
public class LeaveRecordAdapter extends MBaseAdapter {
    private Context context;
    private List<LeaveRecord> dataList;
    private boolean canApprove;
    private boolean showClock;
    private boolean showUnread;

    public LeaveRecordAdapter(Context context, List<LeaveRecord> dataList ,boolean canApprove , boolean showClock , boolean showUnread) {
        this.context = context;
        this.dataList = dataList;
        this.canApprove = canApprove;
        this.showClock = showClock;
        this.showUnread = showUnread;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.listitem_leave_record , null);

        final LeaveRecord leaveRecord = dataList.get(position);

        TextView leaveTypeTv = (TextView) view.findViewById(R.id.leaveTypeTv);
        TextView openTimeTv = (TextView) view.findViewById(R.id.openTimeTv);
        TextView endTimeTv = (TextView) view.findViewById(R.id.endTimeTv);
        final TextView stateTv = (TextView) view.findViewById(R.id.stateTv);
        TextView timeTv = (TextView) view.findViewById(R.id.timeTv);
        TextView readStatusTv = (TextView)view.findViewById(R.id.readStatusTv);
        TextView approveTv = (TextView)view.findViewById(R.id.approveTv);


        String[] leaves = leaveRecord.getLeaveDays().split(",");
        if (leaves.length == 3){
            initTextView(leaveTypeTv , LeaveTypeEnum.valueOf(leaveRecord.getType()).toString() +
                    "(共计" + leaves[0] +  "天" + leaves[1] + "时" + leaves[2] + "分)");
        }else{
            initTextView(leaveTypeTv , LeaveTypeEnum.valueOf(leaveRecord.getType()).toString() +
                    "(共计" + leaveRecord.getLeaveDays() + ")");
        }

        if (!Validators.isEmpty(leaveRecord.getApproverNow())){
            if (leaveRecord.getApproverType() == 2){
                initTextView(approveTv, "当前审批人:" + leaveRecord.getApproverNow() + "(已转审)");

            }else{
                initTextView(approveTv , "当前审批人:" + leaveRecord.getApproverNow());
            }
        }else{
            initTextView(approveTv , "已转审");
        }
        if (showUnread){
            //从请假记录进来的详情
            approveTv.setVisibility(View.GONE);
            if (leaveRecord.getReadStatus() == 2){
                readStatusTv.setVisibility(View.VISIBLE);
            }else{
                readStatusTv.setVisibility(View.GONE);
            }
        }else{
            //审批进来的
            approveTv.setVisibility(View.VISIBLE);
            readStatusTv.setVisibility(View.GONE);
        }

        if (showClock){
            initTextView(timeTv , leaveRecord.getClockPerson());
        }else{
            initTextView(timeTv , FriendlyTimeUtil.friendlyTime(new Date(leaveRecord.getCreationTime())));
        }

        initTextView(openTimeTv , "开始时间" +  leaveRecord.getStartTime());
        initTextView(endTimeTv , "结束时间" + leaveRecord.getEndTime());
        initTextView(stateTv  , ReviewStatusEnum.valueOf(leaveRecord.getStatus()).toString());
        changeStateColor(stateTv , leaveRecord.getStatus());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,LeaveDetailActivity.class);
                intent.putExtra("leaveId" , leaveRecord.getId());
                intent.putExtra("showClockPerson" , showClock);
                intent.putExtra("canApprove" , canApprove);

                context.startActivity(intent);
            }
        });
        return view;
    }

    private void changeStateColor(TextView tv , int state){
        ReviewStatusEnum leaveProgressEnum = ReviewStatusEnum.valueOf(state);
        if (leaveProgressEnum.equals(ReviewStatusEnum.PASS)){
            tv.setTextColor(Color.parseColor("#20b11d"));
        }else if(leaveProgressEnum.equals(ReviewStatusEnum.REFUSE)){
            tv.setTextColor(Color.parseColor("#5d0110"));
        }else if(leaveProgressEnum.equals(ReviewStatusEnum.WAIT)){
            tv.setTextColor(Color.parseColor("#f5a623"));
        }
    }
}
