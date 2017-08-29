package com.zjyeshi.dajiujiao.buyer.views.pay;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.OrderSalesMoneyData;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.buyer.entity.enums.PayTypeEnum;
import com.zjyeshi.dajiujiao.buyer.views.coupon.CouponEntity;

import java.util.List;

/**
 * 付款参与金额描述
 * Created by wuhk on 2016/12/16.
 */
public class PayDesView extends BaseView {
    @InjectView(R.id.priceDesLayout)
    private LinearLayout priceDesLayout;
    @InjectView(R.id.totalPriceTv)
    private TextView totalPriceTv;
    @InjectView(R.id.walletPriceTv)
    private TextView walletPriceTv;
    @InjectView(R.id.couponPriceTv)
    private TextView couponPriceTv;
    @InjectView(R.id.marketCostTv)
    private TextView marketCostTv;
    @InjectView(R.id.salesBackMoneyTv)
    private TextView salesBackMoneyTv;
    @InjectView(R.id.salesCutMoneyTv)
    private TextView salesCutMoneyTv;
    @InjectView(R.id.salesProductNameTv)
    private TextView salesProductNameTv;
    @InjectView(R.id.salesGiftNameTv)
    private TextView salesGiftNameTv;

    private String moneyToPay = "";

    public PayDesView(Context context) {
        super(context);
    }

    public PayDesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.view_pay_des , this);
        ViewUtils.inject(this , this);
    }


    public void bindPayDesData(List<GoodsCar> selectedList, List<GoodsCar> marketList , String totalMoney , PayTypeEnum payTypeEnum , CouponEntity couponData , boolean isWallet , String walletAccount
            , boolean isMarket , String marketCost , OrderSalesMoneyData orderSalesMoneyData){
        //线下支付, 钱包和优惠券隐藏
        if (payTypeEnum.equals(PayTypeEnum.XIANXIA)){
            isWallet = false;
            couponData = null;
            walletPriceTv.setVisibility(GONE);
            couponPriceTv.setVisibility(GONE);
        }else{
            walletPriceTv.setVisibility(VISIBLE);
            couponPriceTv.setVisibility(VISIBLE);
        }

        float allPrice = 0.00f;

        float normalAllPrice = 0.00f;
        for (GoodsCar goodsCar : selectedList) {
            float price = (Float.parseFloat(goodsCar.getGoodCount())) * (Float.parseFloat(goodsCar.getGoodPrice()));
            normalAllPrice += price;
        }
        float marketAllPrice = 0.00f;
        for (GoodsCar goodsCar : marketList) {
            float price = (Float.parseFloat(goodsCar.getGoodCount())) * (Float.parseFloat(goodsCar.getGoodPrice()));
            marketAllPrice += price;
        }

        //计算两类商品价格和总价
        if (Validators.isEmpty(totalMoney)){
            allPrice = normalAllPrice + marketAllPrice;
        }else{
            allPrice = Float.parseFloat(totalMoney);
        }


        //总计
        final String truePayMoney = ExtraUtil.format(allPrice);
        totalPriceTv.setText("总计: ¥" + truePayMoney);

        //活动立返
        if (!Validators.isEmpty(orderSalesMoneyData.getReceivedPrice())){
            salesBackMoneyTv.setVisibility(VISIBLE);
            salesBackMoneyTv.setText("活动立返: ¥" + orderSalesMoneyData.getReceivedPrice());
        }else{
            salesBackMoneyTv.setVisibility(GONE);
        }
        //活动立减
        if (!Validators.isEmpty(orderSalesMoneyData.getSubPrice())){
            salesCutMoneyTv.setVisibility(VISIBLE);
            salesCutMoneyTv.setText("活动立减: ¥" + orderSalesMoneyData.getSubPrice());
        }else{
            salesCutMoneyTv.setVisibility(GONE);
        }
        //活动酒品
        if (!Validators.isEmpty(orderSalesMoneyData.getProductName())){
            salesProductNameTv.setVisibility(VISIBLE);
            salesProductNameTv.setText("活动赠送酒品: " + orderSalesMoneyData.getProductName());
        }else{
            salesProductNameTv.setVisibility(GONE);
        }
        //活动礼品
        if (!Validators.isEmpty(orderSalesMoneyData.getGiftName())){
            salesGiftNameTv.setVisibility(VISIBLE);
            salesGiftNameTv.setText("活动赠送礼品: " + orderSalesMoneyData.getGiftName());
        }else{
            salesGiftNameTv.setVisibility(GONE);
        }

        //各种判断,市场支持费用只能用户市场支持商品，需要优先计算.isMarket表示默认使用市场支持费用
        if(AuthUtil.showMarketCostTab()){
            if (isMarket){
                marketCostTv.setVisibility(VISIBLE);
                //如果能使用的市场费用大于选择的商品价格,则总价减去选择市场商品总价
                if (marketCost==null)
                    return;
                if (Float.parseFloat(marketCost) > marketAllPrice){
                    allPrice = allPrice - marketAllPrice;
                    marketCostTv.setText("市场费用: -¥" + ExtraUtil.format(marketAllPrice));
                }else{
                    //如果小于等于，就使用
                    allPrice = allPrice - Float.parseFloat(marketCost);
                    marketCostTv.setText("市场费用: -¥" + marketCost);
                }
                if (allPrice < 0){
                    allPrice = 0;
                }
            }else{
                marketCostTv.setVisibility(VISIBLE);
                marketCostTv.setText("市场费用: -¥0.00");
            }
        }else{
            marketCostTv.setVisibility(GONE);
        }

        //先减去立返的价格
        if(!Validators.isEmpty(orderSalesMoneyData.getSubPrice())){
            allPrice = allPrice - Float.parseFloat(orderSalesMoneyData.getSubPrice());
        }

        //先减去优惠券的价格在进行钱包相关的计算
        if (null == couponData){
            couponPriceTv.setText("优惠券: 未使用");
        }else{
            couponPriceTv.setText("优惠券: -¥" + couponData.getDisCountMoney());
            allPrice = allPrice - Float.parseFloat(couponData.getDisCountMoney());
        }

        //使用优惠券之后，总价已经小于等于0，不使用钱包
        if (allPrice <= 0){
            allPrice = 0;
            walletPriceTv.setText("钱包: -¥0.00" );
        }else{
            //减去优惠券后的价格大于0，在进行钱包计算
            if (isWallet){
                if (Float.parseFloat(walletAccount) > allPrice){
                    walletPriceTv.setText("钱包: -¥" + ExtraUtil.format(allPrice));
                }else{
                    walletPriceTv.setText("钱包: -¥" + walletAccount);
                }
                allPrice = allPrice - Float.parseFloat(walletAccount);

                if (allPrice < 0){
                    allPrice = 0;
                }

            }else{
                //不是用钱包显示0
                walletPriceTv.setText("钱包: -¥0.00");
            }
        }
        moneyToPay = ExtraUtil.format(allPrice);
    }


    public String getMoneyToPay(){
        return moneyToPay;
    }

}
