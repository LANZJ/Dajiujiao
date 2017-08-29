package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.SalesListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetSalesListTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.RemoveSalesTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动列表
 * Created by wuhk on 2017/4/25.
 */

public class SalesActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;
    @InjectView(R.id.addBtn)
    private Button addBtn;
//    @InjectView(R.id.opTv)
//    private TextView opTv;

//    private boolean edit = false;

    public static boolean reload;

    private List<SalesListData.Sales> dataList = new ArrayList<SalesListData.Sales>();
    private SalesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sales_list);
        initWidgets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reload){
            loadData();
        }
    }

    private void initWidgets() {
        titleLayout.configTitle("优惠活动").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        opTv.setText("编辑");
//        opTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (edit){
//                    //如果现在是可编辑的，操作显示完成
//                    opTv.setText("完成");
//                    //TODO：上传
//                }else{
//                    //现在是不可编辑的，操作显示编辑
//                    opTv.setText("编辑");
//                    adapter.notifyData(true);
//                }
//
//                //改变一下标志位
//                edit = !edit;
//
//
//            }
//        });



        //添加活动
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewSalesActivity.startAddNewSalesActivity(SalesActivity.this);
            }
        });

        adapter = new SalesListAdapter(SalesActivity.this , dataList);
        listView.setAdapter(adapter);

        loadData();
    }


    public void loadData(){
        GetSalesListTask.getSalesList(SalesActivity.this, LoginedUser.getLoginedUser().getShopId(), new AsyncTaskSuccessCallback<SalesListData>() {
            @Override
            public void successCallback(Result<SalesListData> result) {
                reload = false;
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                adapter.notifyDataSetChanged();
                noDataView.showIfEmpty(dataList);
            }
        });
    }
    /**
     * 启动该页面
     *
     * @param context
     */
    public static void startSalesActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SalesActivity.class);
        context.startActivity(intent);
    }
}
