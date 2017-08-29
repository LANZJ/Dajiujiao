package com.zjyeshi.dajiujiao.buyer.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.SalesView;

import java.util.List;

/**
 * 订单详情适配器
 *
 * Created by whk on 2015/10/16.
 */
public class OrderDetailGoodListAdapter extends MBaseAdapter {
    private Context context;
    private List<OrderProduct> dataList;
    private boolean showMoney;
    private int type;
    private String amount;

    public OrderDetailGoodListAdapter(Context context, List<OrderProduct> dataList , boolean showMoney , int type , String amount) {
        this.context = context;
        this.dataList = dataList;
        this.showMoney = showMoney;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view  = LayoutInflater.from(context).inflate(R.layout.listitem_order_detail_good, null);
        View divider = (View)view.findViewById(R.id.divider);
        ImageView photoIv = (ImageView)view.findViewById(R.id.photoIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView contentTv = (TextView)view.findViewById(R.id.contentTv);
        TextView priceTv = (TextView)view.findViewById(R.id.priceTv);
        TextView numTv = (TextView)view.findViewById(R.id.numTv);
        RelativeLayout goodTypeLayout = (RelativeLayout)view.findViewById(R.id.goodTypeLayout);
        TextView goodTypeTv = (TextView)view.findViewById(R.id.goodTypeTv);
        TextView goodNumAndPriceTv = (TextView)view.findViewById(R.id.goodNumAndPriceTv);
        SalesView salesView = (SalesView)view.findViewById(R.id.salesView);

        OrderProduct orderProduct = dataList.get(position);
        GlideImageUtil.glidImage(photoIv , ExtraUtil.getResizePic(orderProduct.getPic() , 130 , 130) , R.drawable.default_img);
        initTextView(nameTv , orderProduct.getName());
        initTextView(contentTv , orderProduct.getSpecifications());
        initTextView(priceTv, "¥" + ExtraUtil.format(Float.parseFloat(orderProduct.getPrice())/100));
        initTextView(numTv , "X"+ orderProduct.getCountUnit());

        //显示优惠活动
        if (type == GoodTypeEnum.NORMAL_BUY.getValue()){
            salesView.setVisibility(View.VISIBLE);
            salesView.bindData(orderProduct.getPreferentialActivities());
        }else{
            salesView.setVisibility(View.GONE);
        }

        if (position == 0){
            if (AuthUtil.showMarketCostTab()){
                goodTypeLayout.setVisibility(View.VISIBLE);
                goodTypeTv.setText(GoodTypeEnum.valueOf(type).toString());
            }else{
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

//        if(position == dataList.size() - 1 ){
//            divider.setVisibility(View.GONE);
//        }
        if (showMoney){
            priceTv.setVisibility(View.VISIBLE);
        }else{
            priceTv.setVisibility(View.GONE);
        }
        return view;
    }
}
