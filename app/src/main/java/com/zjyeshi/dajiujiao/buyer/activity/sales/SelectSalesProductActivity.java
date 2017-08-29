package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.AllCanBuyProductActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.AllCanbuyAdapter;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.SelectSalesProductAdapter;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.SelectSalesJoinReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.task.work.GetSellProductBySalesmanTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CanBuyData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wuhk on 2017/4/26.
 */

public class SelectSalesProductActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.allSelectIv)
    private IVCheckBox allSelectIv;
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;

    private boolean allSelect;

    private List<Product> dataList = new ArrayList<Product>();
    private SelectSalesProductAdapter selectSalesProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_sales_things);
        initWidgets();
    }


    private void initWidgets(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).configTitle("选择商品").configRightText("完成", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Product> selectList = new ArrayList<Product>();
                if (Validators.isEmpty(dataList)){
                    ToastUtil.toast("请选择商品");
                }else{
                    for(Product product : dataList){
                        if (product.isSelected()){
                            selectList.add(product);
                        }
                    }
                    SelectSalesJoinReceiver.notifyReceiver(SelectSalesJoinReceiver.JOIN_PRODUCT , JSONArray.toJSONString(selectList));
                }
                finish();
            }
        });

        allSelectIv.setChecked(false);
        allSelectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allSelect = allSelectIv.isChecked();
                for (Product product : dataList){
                    product.setSelected(allSelectIv.isChecked());
                }
                selectSalesProductAdapter.notifyData(false);
            }
        });

        selectSalesProductAdapter = new SelectSalesProductAdapter(SelectSalesProductActivity.this , dataList , false);
        listView.setAdapter(selectSalesProductAdapter);

        loadData();
    }

    private void loadData(){
        GetSellProductBySalesmanTask task = new GetSellProductBySalesmanTask(SelectSalesProductActivity.this);
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
                selectSalesProductAdapter.notifyData(false);
                noDataView.showIfEmpty(dataList);
            }
        });

        task.execute();
    }


    public void refreshSelect(){
        boolean listAllSelect = true;
        for (Product product :dataList){
            if (!product.isSelected()){
                listAllSelect = false;
                break;
            }
        }
        allSelect = listAllSelect;
        allSelectIv.setChecked(allSelect);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent();
        intent.setClass(context , SelectSalesProductActivity.class);
        context.startActivity(intent);
    }
}
