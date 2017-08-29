package com.zjyeshi.dajiujiao.buyer.views.coupon;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.R;

/**
 * 优惠券View
 * Created by wuhk on 2016/9/22.
 */
public class CouponView extends BaseView {
    @InjectView(R.id.priceTv)
    private TextView priceTv;
    @InjectView(R.id.desTv)
    private TextView desTv;
    @InjectView(R.id.timeTv)
    private TextView timeTv;
    @InjectView(R.id.couponLayout)
    private CouponViewLayout couponLayout;

    public CouponView(Context context) {
        super(context);
    }

    public CouponView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.view_coupon , this);
        ViewUtils.inject(this , this);
    }

    public void bindData(CouponEntity couponEntity){
        initTextView(priceTv ,"满" + couponEntity.getFullMoney() +"元" +  "减" + couponEntity.getDisCountMoney() + "元");
        initTextView(desTv , couponEntity.getDescription() + "可用");
        initTextView(timeTv , couponEntity.getStartTime() + "至" + couponEntity.getEndTime());

        if (couponEntity.isEffective()){
            couponLayout.setBackgroundColor(getResources().getColor(R.color.color_theme));
        }else{
            couponLayout.setBackgroundColor(getResources().getColor(R.color.color_aaaaaa));
        }

    }
}
