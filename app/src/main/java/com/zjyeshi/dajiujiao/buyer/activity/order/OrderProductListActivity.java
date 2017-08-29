package com.zjyeshi.dajiujiao.buyer.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.ProductCommentAdapter;
import com.zjyeshi.dajiujiao.buyer.receiver.order.OrderCommentReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 订单评价商品的列表
 * Created by wuhk on 2016/1/12.
 */
public class OrderProductListActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;

    public static String PRODUCTLIST = "orderProductList";
    public static String MARKETLIST = "marketList";
    public static String ORDERID = "orderId";

    private ProductCommentAdapter productCommentAdapter;
    private List<OrderProduct> dataList = new ArrayList<OrderProduct>();
    private String orderId;

    private OrderCommentReceiver orderCommentReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comment_product_list);
        initWidgets();

        orderCommentReceiver = new OrderCommentReceiver() {
            @Override
            public void changeStatus(int position) {
                OrderProduct orderProduct = dataList.get(position);
                orderProduct.setEvaluated(true);
                productCommentAdapter.notifyDataSetChanged();
            }
        };
        orderCommentReceiver.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderCommentReceiver.unRegister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        productCommentAdapter.notifyDataSetChanged();
    }

    private void initWidgets(){
        titleLayout.configTitle("商品评价").configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<OrderProduct> productList = (List<OrderProduct>) getIntent().getSerializableExtra(PRODUCTLIST);
        HashMap<String , OrderProduct> productHashMap = new HashMap<String, OrderProduct>();
        List<OrderProduct> marketList = (List<OrderProduct>) getIntent().getSerializableExtra(MARKETLIST);

        dataList.clear();
        productHashMap.clear();
        for(OrderProduct product : productList){
            dataList.add(product);
            productHashMap.put(product.getId() , product);
        }

        for(OrderProduct product : marketList){
            OrderProduct temp = productHashMap.get(product.getId());
            if (null == temp){
                dataList.add(product);
            }
        }

        orderId = getIntent().getStringExtra(ORDERID);

        productCommentAdapter = new ProductCommentAdapter(OrderProductListActivity.this , dataList , orderId);
        listView.setAdapter(productCommentAdapter);

    }


    public static void startActivity(Context context , List<OrderProduct> productList , List<OrderProduct> marketList , String id){
        Intent intent = new Intent();
        intent.setClass(context , OrderProductListActivity.class);
        intent.putExtra(ORDERID , id);
        intent.putExtra(PRODUCTLIST, (Serializable) productList);
        intent.putExtra(MARKETLIST , (Serializable) marketList);
        context.startActivity(intent);
    }
}
