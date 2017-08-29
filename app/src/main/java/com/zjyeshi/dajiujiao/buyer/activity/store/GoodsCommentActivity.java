package com.zjyeshi.dajiujiao.buyer.activity.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.store.GoodCommentAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LtGtEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodCommentList;
import com.zjyeshi.dajiujiao.buyer.task.store.ListEvaluateTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品评价
 * Created by wuhk on 2016/9/19.
 */
public class GoodsCommentActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private BUPullToRefreshListView listView;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;

    private List<GoodCommentList.GoodComment> dataList = new ArrayList<GoodCommentList.GoodComment>();
    private GoodCommentAdapter goodCommentAdapter;

    public static final String PRODUCTID = "product_id";

    private String productId;
    private String lastTime = "0";
    private LtGtEnum ltGtEnum = LtGtEnum.LT;
    private boolean isPullDownRefresh = true;
    private boolean canRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_good_comment);
        initWidgets();
    }

    private void initWidgets(){
        productId = getIntent().getStringExtra(PRODUCTID);

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("商品评价");

        listView.setCanPullDown(true);
        listView.setCanScrollUp(true);
        listView.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                if (canRefresh){
                    canRefresh = false;
                    isPullDownRefresh = true;
                    if (!Validators.isEmpty(dataList)){
                       lastTime = String.valueOf(dataList.get(0).getCreateTime());
                    }else{
                        lastTime = "0";
                    }
                    ltGtEnum = LtGtEnum.GT;

                    loadData(false);
                }
            }

            @Override
            public void onScrollUpRefresh() {
                if (canRefresh){
                    canRefresh = false;
                    isPullDownRefresh = false;
                    if (!Validators.isEmpty(dataList)){
                        lastTime = String.valueOf(dataList.get(dataList.size() - 1).getCreateTime());
                    }
                    ltGtEnum = LtGtEnum.LT;
                    loadData(false);
                }
            }
        });

        goodCommentAdapter = new GoodCommentAdapter(GoodsCommentActivity.this , dataList);
        listView.setAdapter(goodCommentAdapter);

        loadData(true);
    }

    private void loadData(boolean showDialog){
        ListEvaluateTask listEvaluateTask = new ListEvaluateTask(GoodsCommentActivity.this);
        listEvaluateTask.setShowProgressDialog(showDialog);
        listEvaluateTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GoodCommentList>() {
            @Override
            public void failCallback(Result<GoodCommentList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        listEvaluateTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GoodCommentList>() {
            @Override
            public void successCallback(Result<GoodCommentList> result) {
                canRefresh = true;
                if (isPullDownRefresh){
                    dataList.addAll(0 , result.getValue().getList());
                    listView.onPullDownRefreshComplete("最新更新" + DateUtils.date2StringBySecond(new Date()));
                    //重新开启上拉更多
                    listView.setCanScrollUp(false);
                    listView.setCanScrollUp(true);
                }else {
                    dataList.addAll(result.getValue().getList());
                    listView.onScrollUpRefreshComplete("上滑更多");
                    if (Validators.isEmpty(result.getValue().getList())) {
                        listView.onScrollUpRefreshComplete("");
                        listView.onScrollUpNoMoreData("没有更多数据了");
                    }
                }
                goodCommentAdapter.notifyDataSetChanged();
                noDataView.showIfEmpty(dataList);
            }
        });
        listEvaluateTask.execute(productId, lastTime, ltGtEnum.getValueStr());
    }

    public static void startActivity(Context context , String id){
        Intent intent = new Intent();
        intent.putExtra(PRODUCTID , id);
        intent.setClass(context , GoodsCommentActivity.class);
        context.startActivity(intent);
    }
}
