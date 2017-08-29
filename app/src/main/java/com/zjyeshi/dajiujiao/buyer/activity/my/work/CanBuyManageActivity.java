package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.CanBuyManageAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Stock;
import com.zjyeshi.dajiujiao.buyer.task.work.CustomerInventoryTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.StockListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 可购品项管理
 * Created by wuhk on 2016/12/14.
 */
public class CanBuyManageActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;

    private CanBuyManageAdapter canBuyManageAdapter;
    private List<Stock> dataLists = new ArrayList<Stock>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_customer_stock);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("下级客户");

        canBuyManageAdapter = new CanBuyManageAdapter(CanBuyManageActivity.this , dataLists);
        listView.setAdapter(canBuyManageAdapter);

        loadData();

    }

    private void loadData(){
        CustomerInventoryTask customerInventoryTask = new CustomerInventoryTask(CanBuyManageActivity.this);
        customerInventoryTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<StockListData>() {
            @Override
            public void failCallback(Result<StockListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        customerInventoryTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<StockListData>() {
            @Override
            public void successCallback(Result<StockListData> result) {
                dataLists.clear();
                dataLists.addAll(result.getValue().getList());
                canBuyManageAdapter.notifyDataSetChanged();
            }
        });

        customerInventoryTask.execute();
    }

    public static void startActivity(Context context){
        Intent intent = new Intent();
        intent.setClass(context , CanBuyManageActivity.class);
        context.startActivity(intent);
    }
}
