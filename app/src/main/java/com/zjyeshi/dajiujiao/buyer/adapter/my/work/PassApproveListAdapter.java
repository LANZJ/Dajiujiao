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
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.ApproveShowParam;
import com.zjyeshi.dajiujiao.buyer.task.work.data.WaitReview;

import java.util.Date;
import java.util.List;

/**
 * 已审批列表适配器
 *
 * Created by zhum on 2016/6/17.
 */
public class PassApproveListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<WaitReview> dataList;

    public PassApproveListAdapter(Context context,
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
                    R.layout.listitem_pass_approve, null);

            viewHolder.stateTv = (TextView)convertView.findViewById(R.id.stateTv);
            viewHolder.titleTv = (TextView)convertView.findViewById(R.id.titleTv);
            viewHolder.spPersonTv = (TextView)convertView.findViewById(R.id.spPersonTv);
            viewHolder.timeTv = (TextView)convertView.findViewById(R.id.timeTv);
            viewHolder.typeTv = (TextView)convertView.findViewById(R.id.typeTv);
            viewHolder.approveNameTv = (TextView)convertView.findViewById(R.id.approveNameTv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final WaitReview waitReview = dataList.get(position);
        if (waitReview!=null){
            if (waitReview.getStatus().equals("1")){
                initTextView(viewHolder.stateTv,"待审核");
                viewHolder.stateTv.setTextColor(context.getResources().getColor(R.color.color_yellow));
            }else if (waitReview.getStatus().equals("2")){
                initTextView(viewHolder.stateTv,"审核通过");
                viewHolder.stateTv.setTextColor(context.getResources().getColor(R.color.color_time));
            }else if (waitReview.getStatus().equals("3")){
                initTextView(viewHolder.stateTv,"审核拒绝");
                viewHolder.stateTv.setTextColor(context.getResources().getColor(R.color.color_theme));
            }else{
                initTextView(viewHolder.stateTv,"未知");
                viewHolder.stateTv.setTextColor(context.getResources().getColor(R.color.color_more_black));
            }
            initTextView(viewHolder.titleTv,waitReview.getName());
            initTextView(viewHolder.spPersonTv,"申请人:" + waitReview.getApplication());
            initTextView(viewHolder.timeTv, DateUtils.date2StringByDay(new Date(waitReview.getCreationTime())));

            if (waitReview.getType().equals("1")){

                initTextView(viewHolder.typeTv,"特殊费用");
                viewHolder.typeTv.setBackgroundResource(R.drawable.red_border_shape);
                viewHolder.typeTv.setTextColor(context.getResources().getColor(R.color.color_theme));
            }else{

                initTextView(viewHolder.typeTv,"费用报销");
                viewHolder.typeTv.setBackgroundResource(R.drawable.yellow_border_shape);
                viewHolder.typeTv.setTextColor(Color.parseColor("#F5A622"));
            }

            if (!Validators.isEmpty(waitReview.getApproverNow())){
                if (waitReview.getApproverType() == 2){
                    initTextView(viewHolder.approveNameTv , "[已转审]当前审批人:" + waitReview.getApproverNow());

                }else{
                    initTextView(viewHolder.approveNameTv , "当前审批人:" + waitReview.getApproverNow());
                }
            }else{
                initTextView(viewHolder.approveNameTv , "已转审");
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (waitReview.getType().equals("1")){
                        AskForMoneyDetailActivity.startActivity(context , waitReview.getId() , ApproveShowParam.SHOW_ONLY_COMMENT , Integer.parseInt(waitReview.getStatus()) , waitReview.getMainId());
//                        Intent intent = new Intent(context , AskForMoneyDetailActivity.class);
//                        intent.putExtra("id" , waitReview.getId());
//                        intent.putExtra("onlyComment" , true);
//                        context.startActivity(intent);
                    }else {
                        ApproveDetailActivity.startActivity(context , waitReview.getId() , ApproveShowParam.SHOW_ONLY_COMMENT , Integer.parseInt(waitReview.getStatus()) , waitReview.getMainId());
//                        Intent intent = new Intent(context , ApproveDetailActivity.class);
//                        intent.putExtra("id",waitReview.getId());
//                        intent.putExtra("canApprove",false);
//                        context.startActivity(intent);
                    }
                }
            });

        }

        return convertView;
    }

    private static class ViewHolder {
        TextView typeTv;
        TextView approveNameTv;
        TextView stateTv;
        TextView titleTv;
        TextView spPersonTv;
        TextView timeTv;
    }
}