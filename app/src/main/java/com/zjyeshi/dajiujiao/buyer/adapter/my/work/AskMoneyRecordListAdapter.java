package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.AskForMoneyDetailActivity;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.CostEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.ReviewStatusEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.ApproveShowParam;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.AskMoneyRecord;

import java.util.Date;
import java.util.List;

/**
 * 费用申请记录列表适配器
 *
 * Created by zhum on 2016/6/16.
 */
public class AskMoneyRecordListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<AskMoneyRecord> dataList;

    public AskMoneyRecordListAdapter(Context context,
                             List<AskMoneyRecord> dataList) {
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
                    R.layout.listitem_ask_money_record, null);
        }
        TextView titleTv = (TextView)convertView.findViewById(R.id.titleTv);
        TextView timeTv = (TextView)convertView.findViewById(R.id.timeTv);
        TextView moneyTv = (TextView)convertView.findViewById(R.id.moneyTv);
        TextView stateTv = (TextView)convertView.findViewById(R.id.stateTv);
        TextView readStatusTv = (TextView)convertView.findViewById(R.id.readStatusTv);

        final AskMoneyRecord askMoneyRecord = dataList.get(position);

        if (askMoneyRecord.getReadStatus() == 2){
            //2是未读
            readStatusTv.setVisibility(View.VISIBLE);
        }else{
            readStatusTv.setVisibility(View.GONE);
        }
        initTextView(titleTv , CostEnum.valueOf(askMoneyRecord.getApplicantType()).toString());
        initTextView(timeTv , DateUtils.date2StringByDay(new Date(askMoneyRecord.getCreationTime())));
        if (Validators.isEmpty(askMoneyRecord.getApplicationMoney())){
            moneyTv.setVisibility(View.GONE);
        }else{
            moneyTv.setVisibility(View.VISIBLE);
            initTextView(moneyTv , "¥" + askMoneyRecord.getApplicationMoney());
        }
        initTextView(stateTv , ReviewStatusEnum.valueOf(askMoneyRecord.getStatus()).toString());
        final ReviewStatusEnum leaveProgressEnum = ReviewStatusEnum.valueOf(askMoneyRecord.getStatus());
        if (leaveProgressEnum.equals(ReviewStatusEnum.PASS)){
            stateTv.setTextColor(Color.parseColor("#20b11d"));
        }else if(leaveProgressEnum.equals(ReviewStatusEnum.REFUSE)){
            stateTv.setTextColor(Color.parseColor("#5d0110"));
        }else if(leaveProgressEnum.equals(ReviewStatusEnum.WAIT)){
            stateTv.setTextColor(Color.parseColor("#f5a623"));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leaveProgressEnum.equals(ReviewStatusEnum.WAIT)){
                    AskForMoneyDetailActivity.startActivity(context  , askMoneyRecord.getId() , ApproveShowParam.SHOW_REMIND_AND_COMMENT , askMoneyRecord.getStatus() , askMoneyRecord.getMainId());
                }else{
                    AskForMoneyDetailActivity.startActivity(context  , askMoneyRecord.getId() , ApproveShowParam.SHOW_ONLY_COMMENT , askMoneyRecord.getStatus() , askMoneyRecord.getMainId());
                }
            }
        });
        return convertView;
    }
}
