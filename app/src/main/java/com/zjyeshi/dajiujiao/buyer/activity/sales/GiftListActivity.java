package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.GiftListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.SelectSalesGiftReceiver;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetGiftListTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.GiftListData;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼品列表
 * Created by wuhk on 2017/4/26.
 */

public class GiftListActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;

    private List<GiftListData.Gift> dataList = new ArrayList<GiftListData.Gift>();
    private GiftListAdapter giftListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gift_list);
        initWidget();
    }

    private void initWidget() {
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).configTitle("礼品列表");

        giftListAdapter = new GiftListAdapter(GiftListActivity.this, dataList);
        listView.setAdapter(giftListAdapter);

        loadData();

    }

    private void loadData() {
        GetGiftListTask.getGiftList(GiftListActivity.this, LoginedUser.getLoginedUser().getShopId(), new AsyncTaskSuccessCallback<GiftListData>() {
            @Override
            public void successCallback(Result<GiftListData> result) {
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                giftListAdapter.notifyDataSetChanged();
                noDataView.showIfEmpty(dataList);
            }
        });
    }


    /**
     * 启动该页面
     *
     * @param context
     */
    public static void startGiftListActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, GiftListActivity.class);
        context.startActivity(intent);
    }
}
