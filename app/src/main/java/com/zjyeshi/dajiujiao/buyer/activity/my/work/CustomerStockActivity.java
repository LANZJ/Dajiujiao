package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.CustomerStockListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Stock;
import com.zjyeshi.dajiujiao.buyer.task.work.CustomerInventoryTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.StockListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 客户库存
 *
 * Created by zhum on 2016/6/15.
 */
public class CustomerStockActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.listView)
    private ListView listView;
    private CustomerStockListAdapter adapter;
    private List<Stock> dataLists = new ArrayList<Stock>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_customer_stock);

        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("客户库存").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configRightText("排序", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(CustomerStockActivity.this)
                        .setItemTextAndOnClickListener(new String[]{"由多至少" , "由少至多"} , new View.OnClickListener[]{new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sortList(true);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sortList(false);

                            }
                        }}).create();
                dialog.show();
            }
        });

        adapter = new CustomerStockListAdapter(CustomerStockActivity.this,dataLists);
        listView.setAdapter(adapter);

        initDatas();
    }

    private void initDatas(){
        CustomerInventoryTask customerInventoryTask = new CustomerInventoryTask(CustomerStockActivity.this);
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
                for(Stock stock : dataLists){
                    stock.setKc(Integer.parseInt(stock.getInventory()));
                }
                sortList(true);
            }
        });

        customerInventoryTask.execute();
    }

    private void sortList(boolean isMost){
        Collections.sort(dataLists,new Comparator<Stock>(){
            public int compare(Stock arg0, Stock arg1) {
                return arg0.getKc().compareTo(arg1.getKc());
            }
        });
        if (isMost){
            List<Stock> tempList = new ArrayList<Stock>();
            for (int i = 0 ; i < dataLists.size() ; i ++){
                tempList.add(0 , dataLists.get(i));
            }
            dataLists.clear();
            dataLists.addAll(tempList);
        }
        adapter.notifyDataSetChanged();
    }
}