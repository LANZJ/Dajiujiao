package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.AllCanbuyAdapter;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.SelelctSalesShopAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Employee;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.SelectSalesJoinReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.task.work.CustomerListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.GetSellProductBySalesmanTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CanBuyData;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CustomerListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2017/4/26.
 */

public class SelectSalesShopActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.allSelectIv)
    private IVCheckBox allSelectIv;
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;

    private boolean allSelect;

    private List<Employee> dataList = new ArrayList<Employee>();
    private SelelctSalesShopAdapter salesShopAdapter;


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
        }).configTitle("店铺").configRightText("完成", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (allSelect){
//                    SelectSalesJoinReceiver.notifyReceiver(SelectSalesJoinReceiver.ALL_SELECT , JSONArray.toJSONString(dataList));
//                }else{
//                    StringBuilder sb = new StringBuilder();
//                    List<Employee> selectList = new ArrayList<Employee>();
//                    if (Validators.isEmpty(dataList)){
//                        ToastUtil.toast("请选择店铺");
//                    }else{
//                        for(Employee employee : dataList){
//                            if (employee.isSelected()){
//                                sb.append(employee.getId());
//                                sb.append(",");
//                                selectList.add(employee);
//                            }
//                        }
//                        if (!Validators.isEmpty(sb.toString())){
//                            sb.deleteCharAt(sb.length() - 1);
//                        }
//
//                        String ids  = sb.toString();
//                        SelectSalesJoinReceiver.notifyReceiver(ids , JSONArray.toJSONString(selectList));
//                    }
//                }
                StringBuilder sb = new StringBuilder();
                List<Employee> selectList = new ArrayList<Employee>();
                if (Validators.isEmpty(dataList)){
                    ToastUtil.toast("请选择店铺");
                }else{
                    for(Employee employee : dataList){
                        if (employee.isSelected()){
//                            sb.append(employee.getId());
//                            sb.append(",");
                            selectList.add(employee);
                        }
                    }
//                    if (!Validators.isEmpty(sb.toString())){
//                        sb.deleteCharAt(sb.length() - 1);
//                    }
//
//                    String ids  = sb.toString();
                    SelectSalesJoinReceiver.notifyReceiver(SelectSalesJoinReceiver.JOIN_SHOP , JSONArray.toJSONString(selectList));
                }
                finish();
            }
        });

        allSelectIv.setChecked(false);
        allSelectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allSelect = allSelectIv.isChecked();
                for (Employee employee : dataList){
                    employee.setSelected(allSelectIv.isChecked());
                }
                salesShopAdapter.notifyData(false);
            }
        });

        salesShopAdapter = new SelelctSalesShopAdapter(SelectSalesShopActivity.this , dataList , false);
        listView.setAdapter(salesShopAdapter);

        loadData();

    }

    private void loadData(){
        CustomerListTask.getCustomer(SelectSalesShopActivity.this, LoginedUser.getLoginedUser().getId(), new AsyncTaskSuccessCallback<CustomerListData>() {
            @Override
            public void successCallback(Result<CustomerListData> result) {
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                salesShopAdapter.notifyData(false);
                noDataView.showIfEmpty(dataList);

            }
        });
    }

    public void refreshSelect(){
        boolean listAllSelect = true;
        for (Employee employee : dataList){
            if (!employee.isSelected()){
                listAllSelect = false;
                break;
            }
        }
        allSelect = listAllSelect;
        allSelectIv.setChecked(allSelect);
    }

    public static void startActivity(Context context ){
        Intent intent = new Intent();
        intent.setClass(context , SelectSalesShopActivity.class);
        context.startActivity(intent);
    }
}
