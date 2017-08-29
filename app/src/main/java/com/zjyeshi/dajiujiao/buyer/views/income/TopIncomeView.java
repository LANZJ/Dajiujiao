package com.zjyeshi.dajiujiao.buyer.views.income;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.buyer.entity.sellerincome.TopIncomeEntity;
import com.zjyeshi.dajiujiao.R;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;

/**
 * Created by wuhk on 2015/11/6.
 */
public class TopIncomeView extends BaseView {
    @InjectView(R.id.timeTv)
    private TextView timeTv;

    @InjectView(R.id.amountTv)
    private TextView amountTv;

    @InjectView(R.id.incomeTv)
    private TextView incomeTv;

    @InjectView(R.id.outTv)
    private TextView outTv;

    public TopIncomeView(Context context) {
        super(context);
    }

    public TopIncomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.seller_listitem_income_top, this);
        ViewUtils.inject(this, this);
    }
    //初始化
    public void bindData(TopIncomeEntity topIncomeEntity){
        initTextView(timeTv  , topIncomeEntity.getTime());
        initTextView(amountTv , topIncomeEntity.getAmount());
        initTextView(incomeTv , topIncomeEntity.getIncome());
        initTextView(outTv , topIncomeEntity.getOut());
    }
}
