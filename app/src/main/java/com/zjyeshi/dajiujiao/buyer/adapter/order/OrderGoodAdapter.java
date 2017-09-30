package com.zjyeshi.dajiujiao.buyer.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

import java.util.List;

/**
 * Created by wuhk on 2016/9/8.
 */
public class OrderGoodAdapter extends MBaseAdapter {
    private Context context;
    private List<GoodsCar> dataList;
    private int type;

    public OrderGoodAdapter(Context context, List<GoodsCar> dataList , int type) {
        this.context = context;
        this.dataList = dataList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_goods_order , null);
        }
        GoodsCar goodsCar = dataList.get(position);
        View divider = (View)view.findViewById(R.id.divider);
        ImageView photoIv = (ImageView)view.findViewById(R.id.photoIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView contentTv = (TextView)view.findViewById(R.id.contentTv);
        TextView priceTv = (TextView)view.findViewById(R.id.priceTv);
        TextView numTv = (TextView)view.findViewById(R.id.numTv);
        TextView goodTypeTv = (TextView)view.findViewById(R.id.goodTypeTv);
        RelativeLayout goodTypeLayout = (RelativeLayout)view.findViewById(R.id.goodTypeLayout);
//        SalesView salesView = (SalesView)view.findViewById(R.id.salesView);
//
//        if (type == GoodTypeEnum.NORMAL_BUY.getValue()){
//            salesView.setVisibility(View.GONE);
//            salesView.bindData(goodsCar.getSalesList());
//        }else{
//            salesView.setVisibility(View.GONE);
//       }
        GlideImageUtil.glidImage(photoIv , ExtraUtil.getResizePic(goodsCar.getGoodIcon() , 130 , 130), R.drawable.default_img);
        initTextView(nameTv ,goodsCar.getGoodName());
        contentTv.setVisibility(View.INVISIBLE);
        initTextView(priceTv , "¥"+ goodsCar.getGoodPrice()+"/瓶");
        initTextView(numTv , "x" + goodsCar.getGoodCount());


        if (position == 0){
       // if (dataList.size()!=0){
          //  if (AuthUtil.showMarketCostTab()){
                goodTypeLayout.setVisibility(View.VISIBLE);
                GoodTypeEnum goodTypeEnum = GoodTypeEnum.valueOf(type);
                goodTypeTv.setText(goodTypeEnum.toString());
//            }else{
//                goodTypeLayout.setVisibility(View.GONE);
//            }
        }else{
            goodTypeLayout.setVisibility(View.GONE);
        }

        if (position == dataList.size()-1){
            divider.setVisibility(View.GONE);
        }else{
            divider.setVisibility(View.VISIBLE);
        }
        return view;
    }
}
