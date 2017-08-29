package com.zjyeshi.dajiujiao.buyer.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.ShopSalesFoldAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2017/5/2.
 */

public class SalesView extends BaseView {
    private BUHighHeightListView productSalesListView;
    private ImageView foldIv;

    private List<SalesListData.Sales> dataList = new ArrayList<SalesListData.Sales>();
    private ShopSalesFoldAdapter shopSalesFoldAdapter;

    public SalesView(Context context) {
        super(context);
    }

    public SalesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.view_product_sales , this);
        productSalesListView = (BUHighHeightListView)findViewById(R.id.productSalesListView);
        foldIv = (ImageView)findViewById(R.id.foldIv);

        foldIv.setVisibility(GONE);
    }


    public void bindData(List<SalesListData.Sales> tempList){
        bindDataWithBackground(tempList , Color.parseColor("#ffffff"));
    }

    public void bindDataWithBackground(List<SalesListData.Sales> tempList , int backgroundColor){
        dataList.clear();
        dataList.addAll(tempList);

        shopSalesFoldAdapter = new ShopSalesFoldAdapter(getContext() , dataList , backgroundColor);
        productSalesListView.setAdapter(shopSalesFoldAdapter);
    }

    public void configItemClick(AdapterView.OnItemClickListener onItemClickListener){
        productSalesListView.setOnItemClickListener(onItemClickListener);
    }
}
