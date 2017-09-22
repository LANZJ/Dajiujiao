package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.GridAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.good.AllGoodInfo;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodList;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.task.store.GoodListTask;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangjian on 2017/9/15.
 */

public class Rceyzs extends BaseActivity {
 private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerview;
    private GridLayoutManager mLayoutManager;
    private GridAdapter gridAdapter;
    private List<GoodData> dataList = new ArrayList<GoodData>();//请求的商品列表
    private List<AllGoodInfo> goodInfoList = new ArrayList<AllGoodInfo>();//显示商品列表信息
    private LinearLayoutManager linearLayoutManager;
    private String shopId;//店铺Id
    public static final String RE = "param.shop.id";//代下业务员ID
    private String categoryId = "";//分类Id
    private HashMap<String, GoodData> productHashMap = new HashMap<String, GoodData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rceyzs);
        initView();//初始化布局
        setListener();//设置监听事件
    }
    private void initView() {
        //接收前一个页面传入的参数
        shopId = getIntent().getStringExtra(RE);
        recyclerview=(RecyclerView)findViewById(R.id.grid_recycler);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.grid_swipe_refresh) ;
        loadProductList();
        //linearLayoutManager=new LinearLayoutManager(LinearLayoutManager.HORIZONTAL  )；
        mLayoutManager=new GridLayoutManager(Rceyzs.this,4 ,GridLayoutManager.VERTICAL,false);//设置为一个3列的纵向网格布局
        recyclerview.setLayoutManager(mLayoutManager);
        //添加分割线
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        //调整SwipeRefreshLayout的位置
        swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }
    private void setListener() {
        //刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    /**
     * 合并成需要显示数据
     */
    private void unionListAndRefresh(final List<GoodData> showList) {
        goodInfoList.clear();
        productHashMap.clear();

        for (int i = 0; i < showList.size(); i++) {
            AllGoodInfo allGoodInfo = new AllGoodInfo();
            GoodData goodData = showList.get(i);

            productHashMap.put(goodData.getProduct().getId(), goodData);
            Product product = goodData.getProduct();

            //库存小于0，显示为0，之前返回出现过小于0的情况
            if (Integer.parseInt(product.getInventory()) < 0) {
                product.setInventory(goodInfoList.size()+"");
            }
            //获取酒品的单位，没有的话默认为瓶
            String unit = product.getUnit();
            if (Validators.isEmpty(unit)) {
                unit = "瓶";
                product.setUnit(unit);
            }
            allGoodInfo.setUnit(unit);
            //商品Id
            allGoodInfo.setGoodId(product.getId());
            //商品名称
            allGoodInfo.setGoodName(product.getName());
            //商品图片
            allGoodInfo.setGoodIcon(product.getPic());
            //商品显示单价,服务器获取的价格除以100
            allGoodInfo.setGoodPrice(ExtraUtil.format(Float.parseFloat(product.getPrice()) / 100));
            //商品上传时的单价,保留服务器上的数据,还是*100
            allGoodInfo.setUpPrice(product.getPrice());
            //一箱多少瓶
            allGoodInfo.setBottlesPerBox(product.getBottlesPerBox());
            //描述
            allGoodInfo.setDescription(product.getDescription());
            //含量 ： ml/单位
            allGoodInfo.setFormat(product.getSpecifications());
            allGoodInfo.setSalesList(goodData.getPreferentialActivities());
            goodInfoList.add(allGoodInfo);
    }
}
    /**
     * 获取商品列表
     */
    private void loadProductList() {
        GoodListTask goodListTask = new GoodListTask(Rceyzs.this);
        goodListTask.setShowProgressDialog(true);
        goodListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GoodList>() {
            @Override
            public void failCallback(Result<GoodList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        goodListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GoodList>() {
            @Override
            public void successCallback(Result<GoodList> result) {

                dataList.clear();
                dataList.addAll(result.getValue().getList());
                unionListAndRefresh(dataList);
                gridAdapter=new GridAdapter(Rceyzs.this,goodInfoList,false);
                recyclerview.setAdapter(gridAdapter);
                gridAdapter.setOnItemClickListener(new GridAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view) {
                   //ToastUtil.toast(recyclerview.getChildAdapterPosition(view)+"");
                    }

                    @Override
                    public void onItemLongClick(View view) {

                    }
                });

            }
        });
        String orderId = "";
        String memberId="";
         String page="1";
        goodListTask.execute(shopId, categoryId, orderId, memberId,page);
    }
}
