package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.ApproveDetailActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.AskForMoneyDetailActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.ApproveShowParam;
import com.zjyeshi.dajiujiao.buyer.task.work.data.WaitReview;
import com.zjyeshi.dajiujiao.R;

import java.util.Date;
import java.util.List;

/**
 * 待审批列表适配器
 *
 * Created by zhum on 2016/6/17.
 */
public class WaitApproveListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<WaitReview> dataList;

    public WaitApproveListAdapter(Context context,
                                 List<WaitReview> dataList) {
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
        ViewHolder viewHolder = new ViewHolder();
        if (null == convertView) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(
                    R.layout.listitem_wait_approve, null);

            viewHolder.typeTv = (TextView)convertView.findViewById(R.id.typeTv);
            viewHolder.titleTv = (TextView)convertView.findViewById(R.id.titleTv);
            viewHolder.noticeTv = (TextView)convertView.findViewById(R.id.noticeTv);
            viewHolder.spPersonTv = (TextView)convertView.findViewById(R.id.spPersonTv);
            viewHolder.timeTv = (TextView)convertView.findViewById(R.id.timeTv);
            viewHolder.approveNameTv = (TextView)convertView.findViewById(R.id.approveNameTv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final WaitReview waitReview = dataList.get(position);
        if (waitReview!=null){
            if (waitReview.getType().equals("1")){
                initTextView(viewHolder.typeTv,"特殊费用");
                viewHolder.typeTv.setBackgroundResource(R.drawable.red_border_shape);
                viewHolder.typeTv.setTextColor(context.getResources().getColor(R.color.color_theme));
                //费用申请
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AskForMoneyDetailActivity.startActivity(context , waitReview.getId() , ApproveShowParam.SHOW_ALL , Integer.parseInt(waitReview.getStatus()) , waitReview.getMainId());
                    }
                });
            }else {
                initTextView(viewHolder.typeTv,"费用报销");
                viewHolder.typeTv.setBackgroundResource(R.drawable.yellow_border_shape);
                viewHolder.typeTv.setTextColor(Color.parseColor("#F5A622"));
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApproveDetailActivity.startActivity(context , waitReview.getId() , ApproveShowParam.SHOW_ALL , Integer.parseInt(waitReview.getStatus()) , waitReview.getMainId());
                    }
                });
            }
            initTextView(viewHolder.titleTv,waitReview.getName());
            initTextView(viewHolder.spPersonTv,"申请人:" + waitReview.getApplication());
            initTextView(viewHolder.timeTv, DateUtils.date2StringByDay(new Date(waitReview.getCreationTime())));
            if (!Validators.isEmpty(waitReview.getApproverNow())){
                initTextView(viewHolder.approveNameTv , "当前审批人:" + waitReview.getApproverNow());
            }else{
                initTextView(viewHolder.approveNameTv , "已转审");
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView typeTv;
        TextView titleTv;
        TextView noticeTv;
        TextView spPersonTv;
        TextView timeTv;
        TextView approveNameTv;
    }
}
