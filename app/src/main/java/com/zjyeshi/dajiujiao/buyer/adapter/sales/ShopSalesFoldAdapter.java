package com.zjyeshi.dajiujiao.buyer.adapter.sales;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFillTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFormTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesGiveTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺活动可折叠适配器
 * Created by wuhk on 2017/4/26.
 */

public class ShopSalesFoldAdapter extends MBaseAdapter {
    private Context context;
    private List<SalesListData.Sales> dataList = new ArrayList<SalesListData.Sales>();
    private int backgroundColor = Color.parseColor("#841528");

    public ShopSalesFoldAdapter(Context context, List<SalesListData.Sales> dataList, int backgroundColor) {
        this.context = context;
        this.dataList = dataList;
        this.backgroundColor = Color.parseColor("#841528");
    }

    @Override
    public int getCount() {
        if (Validators.isEmpty(dataList)){
            return 0;
        }else{
            return dataList.size();
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_sales_fold , null);
        }

        LinearLayout backLayout = (LinearLayout)view.findViewById(R.id.backLayout);
        ImageView flagIv = (ImageView)view.findViewById(R.id.flagIv);
        TextView fillDesTv = (TextView)view.findViewById(R.id.fillDesTv);
        TextView fillTv = (TextView)view.findViewById(R.id.fillTv);
        TextView cutDesTv = (TextView)view.findViewById(R.id.cutDesTv);
        TextView cutTv = (TextView)view.findViewById(R.id.cutTv);

        SalesListData.Sales sales = dataList.get(position);

        backLayout.setBackgroundColor(backgroundColor);
        //满足条件
        SalesFillTypeEnum salesFillTypeEnum = SalesFillTypeEnum.valueOf(sales.getSatisfyType());
        String fillDes = SalesFormTypeEnum.valueOf(sales.getFormType()).getName();
        if (salesFillTypeEnum.equals(SalesFillTypeEnum.FILL_MONEY)){
            fillDesTv.setText(fillDes);
            fillTv.setText(sales.getSatisfyCondition());
        }else if (salesFillTypeEnum.equals(SalesFillTypeEnum.FILL_BOX)){
            fillDesTv.setText(fillDes);
            fillTv.setText(sales.getSatisfyCondition() + "箱");
        }else if (salesFillTypeEnum.equals(SalesFillTypeEnum.MIX_BOX)){
            fillDesTv.setText("合购" + fillDes);
            fillTv.setText(sales.getSatisfyCondition() + "箱");
        }


        //优惠政策
        SalesGiveTypeEnum salesGiveTypeEnum = SalesGiveTypeEnum.valueOf(sales.getFavouredType());
        if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.GIVE_GIFT)){
            cutDesTv.setText("送");
            flagIv.setImageResource(R.drawable.icon_send);
            cutTv.setText(sales.getGiftName());
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.BACK_MONEY)){
            cutDesTv.setText("返");
            flagIv.setImageResource(R.drawable.icon_back);
            cutTv.setText(sales.getFavouredPolicy());
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.CUT_MONEY)){
            cutDesTv.setText("减");
            flagIv.setImageResource(R.drawable.icon_cut);
            cutTv.setText(sales.getFavouredPolicy());
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.GIVE_WINE)){
            cutDesTv.setText("送");
            flagIv.setImageResource(R.drawable.icon_send);
            cutTv.setText(sales.getGiveProductName() + sales.getFavouredPolicy() + "箱");
        }

        return view;
    }
}
