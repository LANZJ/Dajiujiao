package com.zjyeshi.dajiujiao.buyer.activity.my.work;

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
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.AllCanbuyAdapter;
import com.zjyeshi.dajiujiao.buyer.receiver.work.CanBuyProductReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.task.work.GetSellProductBySalesmanTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CanBuyData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.work.AddShopCanBuyProductTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wuhk on 2016/12/14.
 */
public class AllCanBuyProductActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;

    public static final String PARAM_PRODUCT_LIST = "param.product.list";
    public static final String PARAM_SHOP_ID = "param_shop_id";

    private AllCanbuyAdapter allCanbuyAdapter;
    private List<Product> dataList = new ArrayList<Product>();
    private List<Product> productList = new ArrayList<Product>();
    private String shopId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_all_can_buy);
        initWidgets();
    }

    private void initWidgets(){

        String jsonStr = getIntent().getStringExtra(PARAM_PRODUCT_LIST);
        shopId = getIntent().getStringExtra(PARAM_SHOP_ID);

        noDataView.configMessage("没有数据哦");

        productList = JSONArray.parseArray(jsonStr , Product.class);
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("选择可购").configRightText("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                if (Validators.isEmpty(dataList)){
                    ToastUtil.toast("请选择要添加的可购品项");
                }else{
                    for(Product product : dataList){
                        if (product.isSelected()){
                            sb.append(product.getId());
                            sb.append(",");
                        }
                    }
                    if (!Validators.isEmpty(sb.toString())){
                        sb.deleteCharAt(sb.length() - 1);
                    }

                    addCanBuy(sb.toString() , shopId);
                }

            }
        });

        allCanbuyAdapter = new AllCanbuyAdapter(AllCanBuyProductActivity.this , dataList);
        listView.setAdapter(allCanbuyAdapter);

        loadData();
    }

    private void loadData(){
        GetSellProductBySalesmanTask task = new GetSellProductBySalesmanTask(AllCanBuyProductActivity.this);
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
                HashMap<String , Product> hashMap = new HashMap<String, Product>();
                for(Product product : productList){
                    hashMap.put(product.getId() , product);
                }
                for(Product product : result.getValue().getList()){
                    Product exist = hashMap.get(product.getId());
                    if (exist != null){
                        product.setSelected(true);
                    }else{
                        product.setSelected(false);
                    }
                    dataList.add(product);
                }

                noDataView.showIfEmpty(dataList);
                allCanbuyAdapter.notifyDataSetChanged();
            }
        });

        task.execute();
    }

    private void addCanBuy(String productIds , String shopId){
        AddShopCanBuyProductTask task = new AddShopCanBuyProductTask(AllCanBuyProductActivity.this);
        task.setShowProgressDialog(true);
        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("添加成功");
                CanBuyProductReceiver.notifyReceiver();
                finish();
            }
        });

        task.execute(productIds , shopId);
    }

    public static void startActivity(Context context , List<Product> productList , String shopId){
        Intent intent = new Intent();
        intent.setClass(context , AllCanBuyProductActivity.class);
        intent.putExtra(PARAM_PRODUCT_LIST , JSONArray.toJSONString(productList));
        intent.putExtra(PARAM_SHOP_ID , shopId);
        context.startActivity(intent);
    }
}
