package com.zjyeshi.dajiujiao.buyer.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.R;

/**
 * Created by wuhk on 2016/12/26.
 */
public class TwoTabClickWidget extends BaseView {
    @InjectView(R.id.tab1Layout)
    private RelativeLayout tab1Layout;
    @InjectView(R.id.tab1Tv)
    private TextView tab1Tv;
    @InjectView(R.id.tab1Divider)
    private View tab1Divider;

    @InjectView(R.id.tab2Layout)
    private RelativeLayout tab2Layout;
    @InjectView(R.id.tab2Tv)
    private TextView tab2Tv;
    @InjectView(R.id.tab2Divider)
    private View tab2Divider;

    @InjectView(R.id.marketSupportPriceTv)
    private TextView marketSupportPriceTv;
    @InjectView(R.id.extLayout)
    private RelativeLayout extLayout;

    private OnTabClickListener onTabClickListener;

    public TwoTabClickWidget(Context context) {
        super(context);
    }

    public TwoTabClickWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.widget_two_tab_click, this);
        ViewUtils.inject(this, this);
    }

    public void configTabTv(final String tab1, final String tab2) {
        tab1Tv.setText(tab1);
        tab2Tv.setText(tab2);

        tab1Tv.setTextColor(getResources().getColor(R.color.color_theme));
        tab2Tv.setTextColor(getResources().getColor(R.color.color_more_black));

        tab1Divider.setVisibility(VISIBLE);
        tab2Divider.setVisibility(GONE);


        tab1Layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tab1Tv.setTextColor(getResources().getColor(R.color.color_theme));
                tab2Tv.setTextColor(getResources().getColor(R.color.color_more_black));

                tab1Divider.setVisibility(VISIBLE);
                tab2Divider.setVisibility(GONE);
                onTabClickListener.onTabClick(tab1);
            }
        });

        tab2Layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tab1Tv.setTextColor(getResources().getColor(R.color.color_more_black));
                tab2Tv.setTextColor(getResources().getColor(R.color.color_theme));

                tab1Divider.setVisibility(GONE);
                tab2Divider.setVisibility(VISIBLE);
                onTabClickListener.onTabClick(tab2);
            }
        });
    }


    /**
     * 设置点击回调
     *
     * @param onTabClickListener
     */
    public void configTabClickListner(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
    }

    /**
     * tab点击回调
     */
    public interface OnTabClickListener {
        void onTabClick(String str);
    }


    /**设置显示价格
     *
     * @param price
     */
    public void configPrice(String price){
        marketSupportPriceTv.setText(price);
    }

    public void hideExtLayout(){
        extLayout.setVisibility(GONE);
    }

}
