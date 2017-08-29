package com.zjyeshi.dajiujiao.buyer.adapter.sales;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.sales.RebackDetailActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.RebackManagerStatusEnum;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.RebackListData;

import java.util.Date;
import java.util.List;

/**
 * Created by wuhk on 2017/5/10.
 */

public class RebackManagerListAdapter extends MBaseAdapter {
    private Context context;
    private List<RebackListData.Reback> dataList;
    private boolean isDealed;

    public RebackManagerListAdapter(Context context, List<RebackListData.Reback> dataList , boolean isDealed) {
        this.context = context;
        this.dataList = dataList;
        this.isDealed = isDealed;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_reback_manager , null);
        }

        TextView titleTv = (TextView)view.findViewById(R.id.titleTv);
        TextView contentTv = (TextView)view.findViewById(R.id.contentTv);
        TextView statusTv = (TextView)view.findViewById(R.id.statusTv);

        final RebackListData.Reback  reback = dataList.get(position);
//        titleTv.setText("订单号:" + reback.getOrderNumber() + " " + "-" + " 退货款" + reback.getRebackOrderTotalAmount());
        titleTv.setText("订单号:" + reback.getOrderNumber());
        if (Validators.isEmpty(reback.getShopName())){
            contentTv.setText(reback.getMemberName() + "|" + DateUtils.date2StringByMinute(new Date(reback.getCreationTime())));
        }else{
            contentTv.setText(reback.getShopName() + " | " + DateUtils.date2StringByMinute(new Date(reback.getCreationTime())));
        }

        int status = reback.getStatus();
        if (status == RebackManagerStatusEnum.WAIT_REVIEW.getValue()){
            if (isDealed){
                statusTv.setText("待审核");
            }else{
                statusTv.setText("待处理");
            }
            statusTv.setTextColor(Color.parseColor("#f5a622"));
        }else if (status == RebackManagerStatusEnum.PASS_REVIEW.getValue()){
            statusTv.setText("审核通过");
            statusTv.setTextColor(Color.parseColor("#56ac3f"));
        }else if (status == RebackManagerStatusEnum.REFUSE_REVIEW.getValue()){
            statusTv.setText("审核拒绝");
            statusTv.setTextColor(Color.parseColor("#5d0110"));
        }else if (status == RebackManagerStatusEnum.CLOSE_REVIEW.getValue()){
            statusTv.setText("关闭");
            statusTv.setTextColor(Color.parseColor("#9b9b9b"));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RebackDetailActivity.startReabckDetailActivityBySeller(context , reback.getId() , isDealed);
            }
        });


        return view;
    }
}
