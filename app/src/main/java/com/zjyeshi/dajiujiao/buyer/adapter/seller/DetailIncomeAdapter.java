package com.zjyeshi.dajiujiao.buyer.adapter.seller;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.activity.order.OrderDetailNewActivity;
import com.zjyeshi.dajiujiao.buyer.task.data.seller.IncomeData;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;

import java.util.List;

/**
 * Created by wuhk on 2015/11/6.
 */
public class DetailIncomeAdapter extends MBaseAdapter {
    private Context context;
    private List<IncomeData.Income> dataList;

    public DetailIncomeAdapter(Context context, List<IncomeData.Income> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.seller_listitem_income_last , null);
        ImageView headIv = (ImageView)view.findViewById(R.id.headIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView numTv = (TextView)view.findViewById(R.id.numTv);

        final IncomeData.Income income = dataList.get(position);
        if (income.isIn()){
            if (income.getType() == 0){
                nameTv.setText("线下支付");
            }else{
                nameTv.setText("线上支付");
            }
            headIv.setImageResource(R.drawable.income);
            numTv.setTextColor(Color.rgb(88 , 171 ,63));
        }else{
            if (income.getType() == 0){
                nameTv.setText("线下支付");
            }else{
                nameTv.setText("线上支付");
            }
            headIv.setImageResource(R.drawable.out);
            numTv.setTextColor(Color.rgb(93 , 01 ,16));
        }
        float amount = Float.parseFloat(income.getAmount())/100;
        initTextView(numTv , ExtraUtil.format(amount));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (income.isIn()) {
                    OrderDetailNewActivity.startOrderDetailNewActivity(context , income.getOrderId() , LoginEnum.SELLER.toString());
                } else {
                    OrderDetailNewActivity.startOrderDetailNewActivity(context , income.getOrderId() , LoginEnum.BURER.toString());
                }
            }
        });
        return view;
    }
}
