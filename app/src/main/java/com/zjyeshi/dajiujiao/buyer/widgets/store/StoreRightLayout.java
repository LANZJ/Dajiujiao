package com.zjyeshi.dajiujiao.buyer.widgets.store;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zjyeshi.dajiujiao.buyer.activity.store.ShopDetailActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.store.StoreRightPopAdapter;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.SortData;

import java.util.List;

/**
 * 店铺右侧弹出框
 *
 * Created by wuhk on 2015/10/15.
 */
public class StoreRightLayout extends RelativeLayout {
    private LinearLayout rightRemainLayout;
    private ShopDetailActivity act;

    private ListView listView;;
    private StoreRightPopAdapter storeRightPopAdapter;

    public StoreRightLayout(Context context) {
        super(context);
        init();
    }

    public StoreRightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StoreRightLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        act = (ShopDetailActivity) getContext();
        inflate(getContext(), R.layout.view_store_right, this);
        listView = (ListView)findViewById(R.id.listView);
        rightRemainLayout = (LinearLayout)findViewById(R.id.rightRemainLayout);
        rightRemainLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
            }
        });
    }

    //刷新列表
    public void refreshSort(List<SortData> dataList){
        storeRightPopAdapter = new StoreRightPopAdapter(getContext() , dataList);
        listView.setAdapter(storeRightPopAdapter);
    }
}
