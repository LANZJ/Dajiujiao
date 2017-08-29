package com.zjyeshi.dajiujiao.buyer.views.order;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PerOrder;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.order.OrderGoodsListAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.buyer.activity.order.OrderDetailNewActivity;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 历史订单内容布局
 * Created by wuhk on 2016/9/12.
 */
public class OrderContentView extends BaseView {
    @InjectView(R.id.backLayout)
    private LinearLayout backLayout;
    @InjectView(R.id.dianNameTv)
    private TextView dianNameTV;
    @InjectView(R.id.timeTv)
    private TextView timeTv;
    @InjectView(R.id.goodListView)
    private BUHighHeightListView goodListView;
    @InjectView(R.id.marketGoodListView)
    private BUHighHeightListView marketGoodListView;
    @InjectView(R.id.xianXiaTv)
    private TextView xianXiaTv;
    @InjectView(R.id.finalPriceTv)
    private TextView finalPriceTv;
    @InjectView(R.id.spView)
    private View spView;

    private List<OrderProduct> goodList = new ArrayList<OrderProduct>() ;
    private List<OrderProduct> marketGooodList = new ArrayList<OrderProduct>();

    public OrderContentView(Context context) {
        super(context);
    }

    public OrderContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.view_order_content, this);
        ViewUtils.inject(this, this);
    }

    public void bindData(final PerOrder orderContentEntity , final String type , int position){

        if (position % 2 == 0){
            backLayout.setBackgroundColor(Color.parseColor("#f6f6f6"));
        }else{
            backLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if (Validators.isEmpty(orderContentEntity.getMarketCostProductResp())){
            spView.setVisibility(GONE);
        }else{
            spView.setVisibility(VISIBLE);
        }

        initTextView(dianNameTV , orderContentEntity.getShopName());
        goodList.clear();
        goodList.addAll(orderContentEntity.getProductResp());

        marketGooodList.clear();
        marketGooodList.addAll(orderContentEntity.getMarketCostProductResp());


        OnClickListener salesOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderId = orderContentEntity.getId();
                OrderDetailNewActivity.startOrderDetailNewActivity(getContext() , orderId , type);
            }
        };

        if (!Validators.isEmpty(goodList) || !Validators.isEmpty(marketGooodList)){
            OrderGoodsListAdapter orderGoodsListAdapter = new OrderGoodsListAdapter(getContext() , goodList , true , GoodTypeEnum.NORMAL_BUY.getValue() , orderContentEntity.getAmount() , position , salesOnClickListener);
            goodListView.setAdapter(orderGoodsListAdapter);
            goodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String orderId = orderContentEntity.getId();
                    OrderDetailNewActivity.startOrderDetailNewActivity(getContext() , orderId , type);
                }
            });

            OrderGoodsListAdapter marketOrderGoodsListAdapter = new OrderGoodsListAdapter(getContext() , marketGooodList , true , GoodTypeEnum.MARKET_SUPPORT.getValue() , orderContentEntity.getMarketCostAmount() , position , salesOnClickListener);
            marketGoodListView.setAdapter(marketOrderGoodsListAdapter);
            marketGoodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String orderId = orderContentEntity.getId();
                    OrderDetailNewActivity.startOrderDetailNewActivity(getContext() , orderId , type);
                }
            });

            float finalPrice = Float.parseFloat(orderContentEntity.getAmount()) + Float.parseFloat(orderContentEntity.getMarketCostAmount());
            finalPriceTv.setText("共计:¥" + ExtraUtil.format(finalPrice/100));
        }


        //线下支付
        if (orderContentEntity.getType() == 0){
            xianXiaTv.setText("线下支付");
        }else {
            xianXiaTv.setText("线上支付");
        }

        initTextView(timeTv , DateUtils.date2StringBySecond(new Date(orderContentEntity.getCreateTime())));





    }
}
