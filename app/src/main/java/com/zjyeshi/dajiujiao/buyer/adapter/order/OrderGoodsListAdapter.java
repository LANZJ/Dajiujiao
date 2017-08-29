package com.zjyeshi.dajiujiao.buyer.adapter.order;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.views.SalesView;
import com.zjyeshi.dajiujiao.buyer.views.order.OrderView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 订单商品列表适配器
 *
 * Created by wuhk on 2015/10/14.
 */
public class OrderGoodsListAdapter extends MBaseAdapter {
    private Context context;
    private List<OrderProduct> dataList;
    private boolean showMoney;
    private int type;
    private String amount;
    private DecimalFormat decimalFormat=new DecimalFormat("0.00");
    private int bigPosition;
    private View.OnClickListener onClickListener;

    public OrderGoodsListAdapter(Context context, List<OrderProduct> dataList , boolean showMoney , int type , String amount , int position , View.OnClickListener onClickListener) {
        this.context = context;
        this.dataList = dataList;
        this.showMoney = showMoney;
        this.type = type;
        this.amount = amount;
        this.bigPosition = position;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.listitem_goods_order , null) ;
        final OrderProduct orderProduct = dataList.get(position);
        View divider = (View)view.findViewById(R.id.divider);
        LinearLayout backLayout = (LinearLayout)view.findViewById(R.id.backLayout);
        ImageView photoIv = (ImageView)view.findViewById(R.id.photoIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView contentTv = (TextView)view.findViewById(R.id.contentTv);
        TextView priceTv = (TextView)view.findViewById(R.id.priceTv);
        TextView numTv = (TextView)view.findViewById(R.id.numTv);
        RelativeLayout goodTypeLayout = (RelativeLayout)view.findViewById(R.id.goodTypeLayout);
        TextView goodTypeTV = (TextView)view.findViewById(R.id.goodTypeTv);
        TextView goodNumAndPriceTv = (TextView)view.findViewById(R.id.goodNumAndPriceTv);
        SalesView salesView = (SalesView)view.findViewById(R.id.salesView);
        final RelativeLayout contentLayout = (RelativeLayout)view.findViewById(R.id.contentLayout);

        int backgroundColor = Color.parseColor("#ffffff");
        if (bigPosition % 2 == 0){
            backgroundColor = Color.parseColor("#f7f7f7");

        }else{
            backgroundColor = Color.parseColor("#ffffff");
        }
        backLayout.setBackgroundColor(backgroundColor);

        GlideImageUtil.glidImage(photoIv , ExtraUtil.getResizePic(orderProduct.getPic() , 130 , 130) , R.drawable.default_img);
        initTextView(nameTv ,orderProduct.getName());
        initTextView(contentTv , orderProduct.getSpecifications());
        initTextView(priceTv , "¥"+ decimalFormat.format(Float.parseFloat(orderProduct.getPrice())/100));
        initTextView(numTv , "x" + orderProduct.getCount());

        if (showMoney){
            priceTv.setVisibility(View.VISIBLE);
        }else{
            priceTv.setVisibility(View.GONE);
        }

        //活动
        if (type == GoodTypeEnum.NORMAL_BUY.getValue()){
            salesView.setVisibility(View.VISIBLE);
            salesView.bindDataWithBackground(orderProduct.getPreferentialActivities(), backgroundColor);
        }else{
            salesView.setVisibility(View.GONE);
        }

        if(position == 0){
            if (AuthUtil.showMarketCostTab()){
                goodTypeLayout.setVisibility(View.VISIBLE);
                goodTypeTV.setText(GoodTypeEnum.valueOf(type).toString());
            }else {
                goodTypeLayout.setVisibility(View.GONE);
            }
            if (showMoney){
                goodNumAndPriceTv.setText("共" + String.valueOf(dataList.size()) + "件商品,共计" + ExtraUtil.format(ExtraUtil.getShowPrice(amount)));
            }else{
                goodNumAndPriceTv.setText("共" + String.valueOf(dataList.size()) + "件商品");
            }
        }else{
            goodTypeLayout.setVisibility(View.GONE);
        }

        contentLayout.setOnClickListener(onClickListener);
        salesView.configItemClick(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                contentLayout.performClick();
            }
        });
//        if (position == dataList.size()-1){
//            divider.setVisibility(View.GONE);
//        }else{
//            divider.setVisibility(View.VISIBLE);
//        }
        return view;
    }
}
