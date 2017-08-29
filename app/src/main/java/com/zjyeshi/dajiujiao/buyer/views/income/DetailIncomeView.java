package com.zjyeshi.dajiujiao.buyer.views.income;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.DateUtils;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.seller.DetailIncomeAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.sellerincome.DetailIncomeEntity;
import com.zjyeshi.dajiujiao.buyer.task.data.seller.IncomeData;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuhk on 2015/11/6.
 */
public class DetailIncomeView extends BaseView {
    @InjectView(R.id.timeTv)
    private TextView timeTv;

    @InjectView(R.id.listView)
    private BUHighHeightListView listView;

    private DetailIncomeAdapter detailIncomeAdapter;
    List<IncomeData.Income> dataList = new ArrayList<IncomeData.Income>();

    public DetailIncomeView(Context context) {
        super(context);
    }

    public DetailIncomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.seller_listitem_income_detail , this);
        ViewUtils.inject(this, this);
    }
    //初始化
    public void bindData(DetailIncomeEntity detailIncomeEntity){
        String timeDate = detailIncomeEntity.getTime();
        String time = DateUtils.date2StringByDay(new Date(DateUtils.string2Date(timeDate).getTime())) + " " +DateUtils.getWeekOfDate(new Date(DateUtils.string2Date(timeDate).getTime()));
        initTextView(timeTv, time);
        dataList.clear();
        dataList.addAll(detailIncomeEntity.getDetailList());
        detailIncomeAdapter = new DetailIncomeAdapter(getContext() , dataList);
        listView.setAdapter(detailIncomeAdapter);

    }
}
