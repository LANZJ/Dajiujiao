package com.zjyeshi.dajiujiao.buyer.views.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.DGBaseLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.CouponActivity;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.PayTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.coupon.RecommendCouponTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.RecommendCouponData;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.coupon.CouponEntity;
import com.zjyeshi.dajiujiao.buyer.views.other.IVWalletCheck;

/**
 * 支付方式选择View
 * Created by wuhk on 2016/9/22.
 */
public class PaySelectTypeView extends DGBaseLayout {
    @InjectView(R.id.opLayout)
    private LinearLayout opLayout;
    @InjectView(R.id.selectCouponLayout)
    private RelativeLayout selectCouponLayout;//优惠券选择
    @InjectView(R.id.couponDesTv)
    private TextView couponDesTv;
    @InjectView(R.id.walletLayout)
    private RelativeLayout walletLayout;//钱包
    @InjectView(R.id.walletCheckIv)
    private IVWalletCheck walletCheckIv;
    @InjectView(R.id.walletAccountTv)
    private TextView walletAccountTv;
    @InjectView(R.id.marketLayout)
    private RelativeLayout marketLayout;//市场支持费用
    @InjectView(R.id.marketCostTv)
    private TextView marketCostTv;
    @InjectView(R.id.marketCheckIv)
    private IVWalletCheck marketCheckIv;
    @InjectView(R.id.listView)
    private ListView listView;
    public static final int SELECT_COUPON_REQUEST_CODE = 1;
    public static final String SELECT_COUPON = "selet_cpupon";
    public static final String ORDERIDS = "orderIds";
    private PayTypeAdapter payTypeAdapter;
    private PaySelCallback paySelCallback;

    //参数返回
    private PayTypeEnum payTypeEnum = PayTypeEnum.XIANXIA;
    private boolean isWalletFirst = true;//是否钱包优先,默认使用
    private boolean isMarketCost = true;//是否使用市场支持费用,默认使用
    private CouponEntity couponEntity = null;
    private CouponEntity recommentCouPon = new CouponEntity();
    private boolean isFirstLoadCoupon = true;
    private  boolean ip=true;

    public PaySelectTypeView(Context context) {
        super(context);
    }

    public PaySelectTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.view_select_pay_type , this);
        ViewUtils.inject(this , this);
    }

    public void bindData(final String orderIds , final PaySelCallback paySelCallback , boolean onlyXianxia){
        this.paySelCallback = paySelCallback;
        //默认是线下支付 , 隐藏掉优惠券、钱包和市场支持费用功能
        opLayout.setVisibility(GONE);

        //初始化钱包选择
        walletCheckIv.setChecked(true);
        walletCheckIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walletCheckIv.isChecked()){
                    isWalletFirst = true;
                }else{
                    isWalletFirst = false;
                }
                paySelCallback.selParam(payTypeEnum , isWalletFirst , isMarketCost , couponEntity);
            }
        });
        walletLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                walletCheckIv.performClick();
            }
        });


        //初始化市场支持费用
        if(AuthUtil.showMarketCostTab()||ip){
            marketLayout.setVisibility(VISIBLE);
            marketCheckIv.setChecked(true);
            marketCheckIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (marketCheckIv.isChecked()){
                        isMarketCost = true;
                    }else{
                        isMarketCost = false;
                    }
                    paySelCallback.selParam(payTypeEnum , isWalletFirst , isMarketCost , couponEntity);
                }
            });
            marketLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    marketCheckIv.performClick();
                }
            });
        }else{
            marketLayout.setVisibility(GONE);
            isMarketCost = false;
        }

        //支付方式
        payTypeAdapter = new PayTypeAdapter(getContext(), new PayTypeAdapter.SelectCallback() {
            @Override
            public void select(PayTypeEntity payTypeEntity) {
                payTypeEnum = payTypeEntity.getPayEnum();

                if (payTypeEntity.getPayEnum().equals(PayTypeEnum.XIANXIA)){
                    //线下支付没有钱包和优惠券
                    opLayout.setVisibility(GONE);
                    isWalletFirst = false;
                    couponEntity = null;
                    paySelCallback.selParam(payTypeEnum , isWalletFirst ,isMarketCost , couponEntity);
                }else{
                    //如果是卖家,能使用钱包,买家没有,优惠券都能使用
                    getRecommendCoupon(orderIds);
                }
            }
        } , onlyXianxia);
        listView.setAdapter(payTypeAdapter);

        //选择优惠券
        selectCouponLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext() , CouponActivity.class);
                intent.putExtra(SELECT_COUPON , true);
                intent.putExtra(ORDERIDS , orderIds);
                ((Activity)getContext()).startActivityForResult(intent , SELECT_COUPON_REQUEST_CODE);
            }
        });

    }

    /**优惠券选择回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityForResult(int requestCode, int resultCode, Intent data){
        if (Activity.RESULT_OK != resultCode){
            return;
        }

        if (requestCode == SELECT_COUPON_REQUEST_CODE){

            String couponJSon = data.getStringExtra(CouponActivity.COUPON);

            if (couponJSon.equals(CouponActivity.NO_USE_COUPON)){
               couponDesTv.setText("未使用优惠券");
                couponEntity = null;
            }else{
                couponEntity = JSON.parseObject(couponJSon , CouponEntity.class);
                couponDesTv.setText("满" + couponEntity.getFullMoney() + "减" + couponEntity.getDisCountMoney());
            }
            paySelCallback.selParam(payTypeEnum , isWalletFirst , isMarketCost , couponEntity);
        }
    }

    /**设置余额
     *
     * @param money
     */
    public void setWalletAccount(String money){
        if (Validators.isEmpty(money)){
            walletAccountTv.setVisibility(GONE);
        }else{
            walletAccountTv.setVisibility(VISIBLE);
            walletAccountTv.setText("余额:¥" + money);
        }
    }

    /**设置市场支持费用
     *
     * @param marketCost
     */
    public void setMarketAccount(String marketCost){
        if (Validators.isEmpty(marketCost)){
            marketCostTv.setVisibility(GONE);
        }else{
            marketCostTv.setVisibility(VISIBLE);
            marketCostTv.setText("市场支持费用:¥" + marketCost);
        }
    }

    /**获取推荐优惠券
     *
     */
    private void getRecommendCoupon(String orderIds){
        if (isFirstLoadCoupon){
            RecommendCouponTask recommendCouponTask = new RecommendCouponTask(getContext());
            recommendCouponTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<RecommendCouponData>() {
                @Override
                public void failCallback(Result<RecommendCouponData> result) {
                    ToastUtil.toast(result.getMessage());
                }
            });

            recommendCouponTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<RecommendCouponData>() {
                @Override
                public void successCallback(Result<RecommendCouponData> result) {
                    isFirstLoadCoupon = false;
                    RecommendCouponData coupon = result.getValue();
                    if (!Validators.isEmpty(coupon.getId())){
                        recommentCouPon.setId(coupon.getId());
                        recommentCouPon.setFullMoney(coupon.getFulfilMoney());
                        recommentCouPon.setDisCountMoney(coupon.getMinusMoney());
                        recommentCouPon.setStartTime(coupon.getStartTime());
                        recommentCouPon.setEndTime(coupon.getEndTime());
                        recommentCouPon.setEffective(coupon.isWhetherUse());
                        recommentCouPon.setDescription(coupon.getEnvironmentName());
                        couponEntity = recommentCouPon;
                        couponDesTv.setText("满" + couponEntity.getFullMoney() + "减" + couponEntity.getDisCountMoney());
                    }else{
                        couponEntity = null;
                        couponDesTv.setText("没有可用的优惠券");
                    }


                    opLayout.setVisibility(VISIBLE);
                    if (Constants.loginEnum.equals(LoginEnum.SELLER)){
                        walletLayout.setVisibility(VISIBLE);
                        isWalletFirst = walletCheckIv.isChecked();
                    }else{
                        walletLayout.setVisibility(GONE);
                        isWalletFirst = false;
                    }
                    paySelCallback.selParam(payTypeEnum , isWalletFirst , isMarketCost , couponEntity);
                }
            });

            recommendCouponTask.execute(orderIds);
        }else{
            if (!Validators.isEmpty(recommentCouPon.getId())){
                couponEntity = recommentCouPon;
                couponDesTv.setText("满" + couponEntity.getFullMoney() + "减" + couponEntity.getDisCountMoney());
            }else{
                couponEntity = null;
                couponDesTv.setText("没有可用的优惠券");
            }

            opLayout.setVisibility(VISIBLE);
            if (Constants.loginEnum.equals(LoginEnum.SELLER)){
                walletLayout.setVisibility(VISIBLE);
                isWalletFirst = walletCheckIv.isChecked();
            }else{
                walletLayout.setVisibility(GONE);
                isWalletFirst = false;
            }
            paySelCallback.selParam(payTypeEnum , isWalletFirst , isMarketCost , couponEntity);
        }
    }
    /**选择回调接口
     *
     */
    public interface PaySelCallback{
        void selParam(PayTypeEnum payTypeEnum , boolean isWalletFirst , boolean isMarketCost , CouponEntity couponEntity);
    }
}
