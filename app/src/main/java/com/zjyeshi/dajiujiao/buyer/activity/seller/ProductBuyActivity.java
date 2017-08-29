package com.zjyeshi.dajiujiao.buyer.activity.seller;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.adapter.seller.BuyProductAdapter;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.ALLStoreData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.GetNearbyShopList;
import com.zjyeshi.dajiujiao.buyer.task.seller.GetProductBuyTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品采购
 * Created by wuhk on 2015/11/5.
 */
public class ProductBuyActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.listView)
    private ListView listView;

    private List<ALLStoreData> dataList = new ArrayList<ALLStoreData>();
    private BuyProductAdapter buyProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_layout_product_buylist);
        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configTitle("进货").configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buyProductAdapter = new BuyProductAdapter(ProductBuyActivity.this, dataList);
        listView.setAdapter(buyProductAdapter);
        loadData();
    }

    //加载数据
    private void loadData(){
        List<ALLStoreData> allStoreDataList = DaoFactory.getShopsDao().findAll();
        if (Validators.isEmpty(allStoreDataList)){
            doBackground(true);
        }else{
            refreshData();
            //从数据库取出显示之后，在后台请求数据，成功之后刷新
            doBackground(false);
        }
    }

    //首先显示数据库中的数据
    private void refreshData(){
        dataList.clear();
        List<ALLStoreData> tempList = DaoFactory.getShopsDao().findAll();
        dataList.addAll(tempList);
        buyProductAdapter.notifyDataSetChanged();
    }

    //后台熟悉数据并更新界面上的数据
    private void doBackground(boolean show) {
        GetProductBuyTask getProductBuyTask = new GetProductBuyTask(ProductBuyActivity.this);
        getProductBuyTask.setShowProgressDialog(show);
        getProductBuyTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetNearbyShopList>() {
            @Override
            public void failCallback(Result<GetNearbyShopList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getProductBuyTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetNearbyShopList>() {
            @Override
            public void successCallback(Result<GetNearbyShopList> result) {
                refreshData();
            }
        });

        getProductBuyTask.execute();
    }
}
