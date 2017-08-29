package com.zjyeshi.dajiujiao.buyer.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
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
 * Created by wuhk on 2017/4/25.
 */

public class SalesFoldView extends BaseView {
    private BUHighHeightListView shopSalesListView;
    private ImageView foldIv;
    private List<SalesListData.Sales> dataList = new ArrayList<SalesListData.Sales>();
    private List<SalesListData.Sales> giftList = new ArrayList<SalesListData.Sales>();
    private ShopSalesFoldAdapter shopSalesFoldAdapter;

    private boolean isFold = true;

    public SalesFoldView(Context context) {
        super(context);
    }

    public SalesFoldView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.view_sales_fold , this);
        shopSalesListView = (BUHighHeightListView)findViewById(R.id.shopSalesListView);
        foldIv = (ImageView)findViewById(R.id.foldIv);
    }

    public void bindData(List<SalesListData.Sales> tempList){
        shopSalesFoldAdapter = new ShopSalesFoldAdapter(getContext() , giftList , Color.parseColor("#841528"));
        shopSalesListView.setAdapter(shopSalesFoldAdapter);

        dataList.clear();

        dataList.addAll(tempList);
        if (Validators.isEmpty(dataList)){
            setVisibility(GONE);
        }else{
            setVisibility(VISIBLE);
            if (dataList.size() > 1){
                foldIv.setVisibility(VISIBLE);
                giftList.clear();
                giftList.add(dataList.get(0));
                shopSalesFoldAdapter.notifyDataSetChanged();
            }else{
                foldIv.setVisibility(GONE);
                giftList.clear();
                giftList.addAll(dataList);
                shopSalesFoldAdapter.notifyDataSetChanged();
            }

            foldIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFold){
                        foldIv.setImageResource(R.drawable.icon_fold_up);
                        isFold = false;
                        giftList.clear();
                        giftList.addAll(dataList);
                        shopSalesFoldAdapter.notifyDataSetChanged();
                    }else{
                        foldIv.setImageResource(R.drawable.icon_fold_down);
                        isFold = true;
                        giftList.clear();
                        giftList.add(dataList.get(0));
                        shopSalesFoldAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
