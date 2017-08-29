package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.AttendanceRecord;

import java.util.List;

/**
 * 考勤记录适配器
 *
 * Created by zhum on 2016/6/13.
 */
public class AttendanceRecordAdapter extends MBaseAdapter {
    private Context context;
    private List<AttendanceRecord> dataList;

    public AttendanceRecordAdapter(Context context, List<AttendanceRecord> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.listitem_attendance_record , null);

        final AttendanceRecord record = dataList.get(position);

        TextView timeTv = (TextView) view.findViewById(R.id.timeTv);
        TextView upTimeTv = (TextView) view.findViewById(R.id.upTimeTv);
        TextView upAreaTv = (TextView) view.findViewById(R.id.upAreaTv);
        TextView downTimeTv = (TextView) view.findViewById(R.id.downTimeTv);
        TextView downAreaTv = (TextView) view.findViewById(R.id.downAreaTv);
        RelativeLayout downLayout = (RelativeLayout)view.findViewById(R.id.downLayout);

        if (record.isSingle()){
            downLayout.setVisibility(View.GONE);
        }else{
            downLayout.setVisibility(View.VISIBLE);
        }
        initTextView(timeTv,record.getDate());
        initTextView(upTimeTv,record.getUpTime());
        initTextView(upAreaTv,record.getUpArea());
        initTextView(downTimeTv,record.getDownTime());
        initTextView(downAreaTv,record.getDownArea());


        return view;
    }
}
