package com.zjyeshi.dajiujiao.buyer.views.coupon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.xuan.bigapple.lib.utils.display.DisplayUtils;
import com.zjyeshi.dajiujiao.R;

/**
 * 优惠券波浪布局
 * Created by wuhk on 2016/9/21.
 */
public class CouponViewLayout extends LinearLayout {
    private Paint smallPaint;
    private Paint bigPaint;
    private float radius = DisplayUtils.getPxByDp((Activity) getContext() , 4);
    private int circleNum;
    private float gap = DisplayUtils.getPxByDp((Activity) getContext() , 4);
    private float remainGap;
    private float bigCircleRadius = DisplayUtils.getPxByDp((Activity)getContext() , 15);


    public CouponViewLayout(Context context) {
        super(context);
        init();
    }

    public CouponViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        smallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        smallPaint.setDither(true);
        smallPaint.setColor(getResources().getColor(R.color.color_e9e9e9));
        smallPaint.setStyle(Paint.Style.FILL);

        bigPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigPaint.setDither(true);
        bigPaint.setColor(Color.WHITE);
        bigPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float oneCircleUse = (2 * radius) + gap;
        circleNum  = (int)(w / oneCircleUse);
        remainGap =(w % oneCircleUse) / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0 ; i < circleNum ; i ++){
            float x = remainGap + gap/2 + radius + (gap + 2 * radius) * i;
            canvas.drawCircle(x , getHeight() , radius , smallPaint);
        }

        canvas.drawCircle(0 , getHeight() / 2 , bigCircleRadius , bigPaint);
        canvas.drawCircle(getWidth() , getHeight() / 2 , bigCircleRadius , bigPaint);
    }
}
