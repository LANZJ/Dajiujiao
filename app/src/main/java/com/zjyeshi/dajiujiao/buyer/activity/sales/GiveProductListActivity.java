package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.GiveProductAdapter;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.task.work.GetSellProductBySalesmanTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CanBuyData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2017/5/9.
 */

public class GiveProductListActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;

    private List<Product> dataList = new ArrayList<Product>();
    private GiveProductAdapter giveProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gift_list);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("选择酒品").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        giveProductAdapter = new GiveProductAdapter(GiveProductListActivity.this , dataList);
        listView.setAdapter(giveProductAdapter);


        loadData();
    }

    private void loadData(){
        GetSellProductBySalesmanTask task = new GetSellProductBySalesmanTask(GiveProductListActivity.this);
        task.setShowProgressDialog(true);
        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CanBuyData>() {
            @Override
            public void failCallback(Result<CanBuyData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CanBuyData>() {
            @Override
            public void successCallback(Result<CanBuyData> result) {
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                giveProductAdapter.notifyDataSetChanged();
                noDataView.showIfEmpty(dataList);
            }
        });

        task.execute();
    }

    public static void startGiveProductListActivty(Context context){
        Intent intent = new Intent();
        intent.setClass(context , GiveProductListActivity.class);
        context.startActivity(intent);
    }
}
