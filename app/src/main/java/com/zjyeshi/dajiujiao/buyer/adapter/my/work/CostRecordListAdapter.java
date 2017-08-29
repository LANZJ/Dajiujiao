package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.ApproveDetailActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.ReviewStatusEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.ApproveShowParam;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.CostRecord;
import com.zjyeshi.dajiujiao.buyer.task.work.RemindApproverTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.Date;
import java.util.List;

/**
 * 费用申请记录列表适配器
 *
 * Created by zhum on 2016/6/16.
 */
public class CostRecordListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<CostRecord> dataList;

    public CostRecordListAdapter(Context context,
                                   List<CostRecord> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != dataList) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        if (null == convertView) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(
                    R.layout.listitem_cost_record, null);
        }
        TextView titleTv = (TextView)convertView.findViewById(R.id.titleTv);
        TextView timeTv = (TextView)convertView.findViewById(R.id.timeTv);
        TextView statusTv = (TextView)convertView.findViewById(R.id.statusTv);
        TextView noticeTv = (TextView)convertView.findViewById(R.id.noticeTv);
        TextView readStatusTv = (TextView)convertView.findViewById(R.id.readStatusTv);

        final CostRecord costRecord = dataList.get(position);

        initTextView(titleTv , costRecord.getName());
        initTextView(timeTv , DateUtils.date2StringByDay(new Date(costRecord.getCreationTime())));
        initTextView(statusTv , ReviewStatusEnum.valueOf(costRecord.getStatus()).toString());

        if (costRecord.getReadStatus() == 2){
            //2是未读
            readStatusTv.setVisibility(View.VISIBLE);
        }else{
            readStatusTv.setVisibility(View.GONE);
        }

        final ReviewStatusEnum leaveProgressEnum = ReviewStatusEnum.valueOf(costRecord.getStatus());
        if (leaveProgressEnum.equals(ReviewStatusEnum.PASS)){
            statusTv.setTextColor(Color.parseColor("#20b11d"));
        }else if(leaveProgressEnum.equals(ReviewStatusEnum.REFUSE)){
            statusTv.setTextColor(Color.parseColor("#5d0110"));
        }else if(leaveProgressEnum.equals(ReviewStatusEnum.WAIT)){
            statusTv.setTextColor(Color.parseColor("#f5a623"));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leaveProgressEnum.equals(ReviewStatusEnum.WAIT)){
                    ApproveDetailActivity.startActivity(context , costRecord.getId() , ApproveShowParam.SHOW_REMIND_AND_COMMENT , costRecord.getStatus() , costRecord.getMainId());
                }else{
                    ApproveDetailActivity.startActivity(context , costRecord.getId() , ApproveShowParam.SHOW_ONLY_COMMENT , costRecord.getStatus() , costRecord.getMainId());
                }
            }
        });

        noticeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemindApproverTask remindApproverTask = new RemindApproverTask(context);
                remindApproverTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                    @Override
                    public void failCallback(Result<NoResultData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });

                remindApproverTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                    @Override
                    public void successCallback(Result<NoResultData> result) {
                        ToastUtil.toast("提醒成功");
                    }
                });
                remindApproverTask.execute(costRecord.getApprover() , costRecord.getId() , "2");
            }
        });

        return convertView;
    }
}
