package com.zjyeshi.dajiujiao.buyer.views.order;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetOrderSalesMoneyTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.OrderSalesMoneyData;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderDetailData;

/**
 * Created by wuhk on 2017/1/3.
 */

public class OrderPayDesView extends BaseView {
    @InjectView(R.id.totalPriceLayout)
    private RelativeLayout totalPriceLayout;
    @InjectView(R.id.walletLayout)
    private RelativeLayout walletLayout;
    @InjectView(R.id.couponLayout)
    private RelativeLayout couponLayout;
    @InjectView(R.id.marketLayout)
    private RelativeLayout marketLayout;
    @InjectView(R.id.salesBackLayout)
    private RelativeLayout salesBackLayout;
    @InjectView(R.id.salesCutLayout)
    private RelativeLayout salesCutLayout;
    @InjectView(R.id.salesProductLayout)
    private RelativeLayout salesProductLayout;
    @InjectView(R.id.salesGiftLayout)
    private RelativeLayout salesGiftLayout;

    @InjectView(R.id.totalPriceTv)
    private TextView totalPriceTv;
    @InjectView(R.id.walletPriceTv)
    private TextView walletPriceTv;
    @InjectView(R.id.couponPriceTv)
    private TextView couponPriceTv;
    @InjectView(R.id.marketPriceTv)
    private TextView marketPriceTv;
    @InjectView(R.id.salesBackPriceTv)
    private TextView salesBackPriceTv;
    @InjectView(R.id.salesCutPriceTv)
    private TextView salesCutPriceTv;
    @InjectView(R.id.salesProductTv)
    private TextView salesProductTv;
    @InjectView(R.id.salesGiftTv)
    private TextView salesGiftTv;
    private String s="0.0";


    public OrderPayDesView(Context context) {
        super(context);
    }

    public OrderPayDesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.view_order_pay_des , this);
        ViewUtils.inject(this , this);
    }

    public void bindData(OrderDetailData data){
        if (!Validators.isEmpty(data.getAmount())){
            float amount = Float.parseFloat(data.getAmount()) / 100;
            float marketAmount = Float.parseFloat(data.getMarketCostAmount())/100;
            totalPriceTv.setText("¥" + ExtraUtil.format(amount + marketAmount));
        }else{
            totalPriceTv.setText("¥0.00");
        }

        if (!Validators.isEmpty(data.getWalletCost())){
            walletLayout.setVisibility(VISIBLE);
            walletPriceTv.setText("-¥" + ExtraUtil.format(Float.parseFloat(data.getWalletCost())));
        }else{
            walletLayout.setVisibility(GONE);
        }

        if (!Validators.isEmpty(data.getCouponsCost())){
            couponLayout.setVisibility(VISIBLE);
            couponPriceTv.setText("-¥" + ExtraUtil.format(Float.parseFloat(data.getCouponsCost())));
        }else{
            couponLayout.setVisibility(GONE);
        }

        if (!Validators.isEmpty(data.getMarketCost())){
            if (AuthUtil.showMarketCostTab()){
                String b=data.getMarketCost();
                if(b.equals(s)){
                    marketLayout.setVisibility(GONE);
                }else {
                    marketLayout.setVisibility(VISIBLE);
                }
            }else {

                marketLayout.setVisibility(GONE);
            }
            marketPriceTv.setText("-¥" + ExtraUtil.format(Float.parseFloat(data.getMarketCost())));

        }else{
            marketLayout.setVisibility(GONE);
        }

        GetOrderSalesMoneyTask.getOrderSalesMoney(getContext(), data.getId(), new AsyncTaskSuccessCallback<OrderSalesMoneyData>() {
            @Override
            public void successCallback(Result<OrderSalesMoneyData> result) {
                OrderSalesMoneyData salesMoneyData = result.getValue();
                if (!Validators.isEmpty(salesMoneyData.getReceivedPrice())){
                    salesBackLayout.setVisibility(VISIBLE);
                    salesBackPriceTv.setText("¥" + salesMoneyData.getReceivedPrice());
                }else{
                    salesBackLayout.setVisibility(GONE);
                }

                if (!Validators.isEmpty(salesMoneyData.getSubPrice())){
                    salesCutLayout.setVisibility(VISIBLE);
                    salesCutPriceTv.setText("¥" + salesMoneyData.getSubPrice());
                }else{
                    salesCutLayout.setVisibility(GONE);
                }

                if (!Validators.isEmpty(salesMoneyData.getProductName())){
                    salesProductLayout.setVisibility(VISIBLE);
                    salesProductTv.setText(salesMoneyData.getProductName());
                }else{
                    salesProductLayout.setVisibility(GONE);
                }

                if (!Validators.isEmpty(salesMoneyData.getGiftName())){
                    salesGiftLayout.setVisibility(VISIBLE);
                    salesGiftTv.setText(salesMoneyData.getGiftName());
                }else{
                    salesGiftLayout.setVisibility(GONE);
                }
            }
        });
    }
}
