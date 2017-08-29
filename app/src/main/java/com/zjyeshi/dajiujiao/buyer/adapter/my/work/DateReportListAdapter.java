package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.WriteDateReportActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.DateReport;
import com.zjyeshi.dajiujiao.buyer.receiver.RefreshDateReportReceiver;

import java.util.Date;
import java.util.List;

/**
 * 工作日报
 *
 * Created by zhum on 2016/6/17.
 */
public class DateReportListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<DateReport> dataList;

    public DateReportListAdapter(Context context,
                                   List<DateReport> dataList) {
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
    public Object getItem(int arg0) {
        return dataList.get(arg0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder = new ViewHolder();
        if (null == convertView) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(
                    R.layout.listitem_date_report, null);

            viewHolder.nameTv = (TextView)convertView.findViewById(R.id.nameTv);
            viewHolder.timeTv = (TextView)convertView.findViewById(R.id.timeTv);
            viewHolder.unReadTv = (TextView)convertView.findViewById(R.id.unReadTv);
            viewHolder.unReadCommentTv = (TextView)convertView.findViewById(R.id.unReadCommentTv);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DateReport dateReport = dataList.get(position);
        if (dateReport!=null){
            initTextView(viewHolder.nameTv,dateReport.getPerson());
            initTextView(viewHolder.timeTv, DateUtils.date2StringBySecond(new Date(dateReport.getCreationTime())));
            if (Validators.isEmpty(dateReport.getUnread())){
                viewHolder.unReadTv.setVisibility(View.GONE);
            }else{
                viewHolder.unReadTv.setVisibility(View.VISIBLE);
                if (dateReport.getUnread().equals("已读")){
                    viewHolder.unReadTv.setTextColor(Color.parseColor("#999999"));
                    viewHolder.unReadTv.setText("[已读]");
                }else if (dateReport.getUnread().equals("未读")){
                    viewHolder.unReadTv.setTextColor(Color.parseColor("#5d0110"));
                    viewHolder.unReadTv.setText("[未读]");
                }
            }

            if (Validators.isEmpty(dateReport.getUnreadComment())){
                viewHolder.unReadCommentTv.setVisibility(View.GONE);
            }else{
                viewHolder.unReadCommentTv.setVisibility(View.VISIBLE);
                viewHolder.unReadCommentTv.setTextColor(Color.parseColor("#5d0110"));
                viewHolder.unReadCommentTv.setText("["+ dateReport.getUnreadComment() +"]");
            }
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefreshDateReportReceiver.notifyReceiver();
                if (!Validators.isEmpty(dateReport.getUnread())){
                    dateReport.setUnread("已读");
                }
                if(!Validators.isEmpty(dateReport.getUnreadComment())){
                    dateReport.setUnreadComment("");
                }
                Intent intent = new Intent(context,WriteDateReportActivity.class);
                intent.putExtra("id",dateReport.getId());
                intent.putExtra("type", WriteDateReportActivity.READ);
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView nameTv;
        TextView timeTv;
        TextView unReadTv;
        TextView unReadCommentTv;
    }
}
