package com.zjyeshi.dajiujiao.buyer.adapter.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.activity.account.ApplyDetailActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.task.account.data.CashLogList;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.FriendlyTimeUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.Date;
import java.util.List;

/**
 * 提现记录适配器
 * Created by wuhk on 2016/7/4.
 */
public class CashRecordAdapter extends MBaseAdapter {
    private Context context;
    private List<CashLogList.CashLog> dataList;

    public CashRecordAdapter(Context context, List<CashLogList.CashLog> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_apply_record, null);
        }
        final CashLogList.CashLog cashLog = dataList.get(position);
        TextView timeTv = (TextView) view.findViewById(R.id.timeTv);
        TextView priceTv = (TextView) view.findViewById(R.id.priceTv);
        TextView statusTv = (TextView) view.findViewById(R.id.statusTv);

        initTextView(timeTv, FriendlyTimeUtil.friendlyTime(new Date(cashLog.getCreationTime())));
        initTextView(priceTv, ExtraUtil.format(Float.parseFloat(cashLog.getAmount())/100));
        int status = cashLog.getStatus();
        switch (status) {
            case 1:
                statusTv.setTextColor(Color.rgb(235, 97, 0));
                initTextView(statusTv, "待处理");
                break;
            case 2:
                statusTv.setTextColor(Color.rgb(13, 173, 233));
                initTextView(statusTv, "已处理");
                break;
            default:
                break;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context , ApplyDetailActivity.class);
                intent.putExtra("id" , cashLog.getId());
                context.startActivity(intent);

            }
        });
        return view;
    }
}
