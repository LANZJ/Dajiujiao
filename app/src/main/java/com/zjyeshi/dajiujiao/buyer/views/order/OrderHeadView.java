package com.zjyeshi.dajiujiao.buyer.views.order;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.zjyeshi.dajiujiao.buyer.entity.order.OrderHeadEntity;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;

/**
 * 历史订单头部布局
 * Created by wuhk on 2016/9/12.
 */
public class OrderHeadView extends BaseView {
    @InjectView(R.id.sortTv)
    private TextView sortTv;
    @InjectView(R.id.amountTv)
    private TextView amountTv;

    public OrderHeadView(Context context) {
        super(context);
    }

    public OrderHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.view_order_head, this);
        ViewUtils.inject(this, this);
    }

    public void bindData(OrderHeadEntity orderHeadEntity) {
        initTextView(sortTv, orderHeadEntity.getSort());
        initTextView(amountTv, "总金额:¥" + orderHeadEntity.getAmount());
    }
}
