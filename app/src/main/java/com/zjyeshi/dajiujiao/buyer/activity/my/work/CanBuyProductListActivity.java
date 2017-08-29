package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.store.GoodSellListTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.CanBuyProductListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Stock;
import com.zjyeshi.dajiujiao.buyer.receiver.work.CanBuyProductReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CanBuyData;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺可购品项列表
 * Created by wuhk on 2016/12/14.
 */
public class CanBuyProductListActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.avatarIv)
    private ImageView avatartIv;
    @InjectView(R.id.shopNameTv)
    private TextView shopNameTv;
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;
    private CanBuyProductListAdapter adapter;
    private List<Product> dataList = new ArrayList<Product>();

    private Stock stock;
    public static final String SHOP_INFO = "shop_info";

    private CanBuyProductReceiver canBuyProductReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_can_buy_product_list);
        canBuyProductReceiver = new CanBuyProductReceiver() {
            @Override
            public void reload() {
                loadData();
            }
        };
        canBuyProductReceiver.register();
        initWidgets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        canBuyProductReceiver.unRegister();
    }

    private void initWidgets(){

        String jsonStr = getIntent().getStringExtra(SHOP_INFO);
        stock = JSON.parseObject(jsonStr , Stock.class);
        GlideImageUtil.glidImage(avatartIv , stock.getShopPic() , R.drawable.default_img);
        initTextView(shopNameTv , stock.getShopName());

        noDataView.configMessage("没有可购品项");

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("可购品项").configRightText("添加", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCanBuyProductActivity.startActivity(CanBuyProductListActivity.this , dataList , stock.getShopId());
            }
        });

        adapter = new CanBuyProductListAdapter(CanBuyProductListActivity.this , dataList , stock.getShopId());
        listView.setAdapter(adapter);

        loadData();
    }

    private void loadData(){
        GoodSellListTask goodListTask = new GoodSellListTask(CanBuyProductListActivity.this);
        goodListTask.setShowProgressDialog(true);
        goodListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CanBuyData>() {
            @Override
            public void failCallback(Result<CanBuyData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        goodListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CanBuyData>() {
            @Override
            public void successCallback(Result<CanBuyData> result) {
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                adapter.notifyDataSetChanged();
                noDataView.showIfEmpty(dataList);
            }
        });
        goodListTask.execute(stock.getShopId());
    }

    public static void startActivity(Context context , Stock stock){
        Intent intent = new Intent(context , CanBuyProductListActivity.class);
        intent.putExtra(SHOP_INFO , JSON.toJSONString(stock));
        context.startActivity(intent);
    }
}
