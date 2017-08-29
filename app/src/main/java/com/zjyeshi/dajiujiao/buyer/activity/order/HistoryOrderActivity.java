package com.zjyeshi.dajiujiao.buyer.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.order.HistoryOrderAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.order.OrderHeadEntity;
import com.zjyeshi.dajiujiao.buyer.task.data.order.GetOrderListData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PerOrder;
import com.zjyeshi.dajiujiao.buyer.task.order.HistoryOrderTask;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史订单
 * Created by wuhk on 2016/9/12.
 */
public class HistoryOrderActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private BUPullToRefreshListView listView;
    @InjectView(R.id.floatLayout)
    private RelativeLayout floatLayout;
    @InjectView(R.id.sortTv)
    private TextView sortTv;
    @InjectView(R.id.amountTv)
    private TextView amountTv;

    private List<BaseEntity> dataList = new ArrayList<BaseEntity>();
    private HistoryOrderAdapter historyOrderAdapter;

    private Map<String , List<PerOrder>> map = new HashMap<>();
    private Map<String , String> priceMap = new HashMap<>();

    public static final String TYPE = "type";

    private String date;
    private String type;
    private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM");
    private boolean showDialog = true;
    private boolean canScrollUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history_order);
        initWidgets();
    }

    private void initWidgets(){

        type = getIntent().getStringExtra(TYPE);
        date = "";

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("历史订单");

        listView.setCanScrollUp(true);
        listView.setCanPullDown(false);
        listView.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {

            }

            @Override
            public void onScrollUpRefresh() {
                if (canScrollUp){
                    PerOrder temp = (PerOrder)dataList.get(dataList.size() - 1);
                    date = dateFormat.format(new Date(temp.getCreateTime()));
                    showDialog = false;
                    loadData();
                }
            }
        });


        historyOrderAdapter = new HistoryOrderAdapter(HistoryOrderActivity.this ,dataList , type);
        listView.setAdapter(historyOrderAdapter);

        loadData();

    }

    private void loadData(){
        canScrollUp = false;
        HistoryOrderTask historyOrderTask = new HistoryOrderTask(HistoryOrderActivity.this);
        historyOrderTask.setShowProgressDialog(showDialog);
        historyOrderTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetOrderListData>() {
            @Override
            public void failCallback(Result<GetOrderListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        historyOrderTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetOrderListData>() {
            @Override
            public void successCallback(Result<GetOrderListData> result) {
                canScrollUp = true;
                List<PerOrder> list = result.getValue().getList();
                if (Validators.isEmpty(list)){
                    listView.onScrollUpRefreshComplete("");
                    listView.onScrollUpNoMoreData("没有更多数据了");
                }else{
                    sortData(list);
                    listView.onScrollUpRefreshComplete("上拉更多");
                }
                historyOrderAdapter.notifyDataSetChanged();
            }
        });
        if (type.equals(LoginEnum.SELLER.toString())){
            historyOrderTask.execute(date , "1");
        }else{
            historyOrderTask.execute(date , "2");
        }
    }

    private void sortData(List<PerOrder> tempList){

        for (PerOrder perOrder : tempList){
            String month = dateFormat.format(perOrder.getCreateTime());

            List<PerOrder> itemList = map.get(month);
            if (Validators.isEmpty(itemList)){
                itemList = new ArrayList<PerOrder>();
                map.put(month , itemList);
            }
            itemList.add(perOrder);
        }


        for(Map.Entry<String, List<PerOrder>> entry : map.entrySet()){
            OrderHeadEntity headEntity = new OrderHeadEntity();
            headEntity.setSort(entry.getKey());
            int amount = 0;
            for (PerOrder perOrder : entry.getValue()){
                amount += Integer.parseInt(perOrder.getAmount());
                amount += Integer.parseInt(perOrder.getMarketCostAmount());
            }

            headEntity.setAmount(ExtraUtil.format(ExtraUtil.getShowPrice(String.valueOf(amount))));
            priceMap.put(entry.getKey() , ExtraUtil.format(ExtraUtil.getShowPrice(String.valueOf(amount))));
        }
        String key = "";
        for (PerOrder order : tempList){
            OrderHeadEntity head = new OrderHeadEntity();
            if (!key.equals(dateFormat.format(new Date(order.getCreateTime())))){
                key = dateFormat.format(new Date(order.getCreateTime()));
                head.setSort(key);
                head.setAmount(priceMap.get(key));
                dataList.add(head);
                dataList.add(order);
            }else{
                dataList.add(order);
            }
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
                BaseEntity baseEntity = dataList.get(firstVisibleItem);
                if (baseEntity instanceof  PerOrder) {
                    floatLayout.setVisibility(View.VISIBLE);
                    PerOrder perOrder = (PerOrder)baseEntity;
                    String month = dateFormat.format(new Date(perOrder.getCreateTime()));
                    sortTv.setText(month);
                    amountTv.setText("总金额:¥" + priceMap.get(month));
                } else if (baseEntity instanceof  OrderHeadEntity){
                    floatLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public static void startActivity(Context context , String type){
        Intent intent = new Intent();
        intent.putExtra(TYPE , type);
        intent.setClass(context , HistoryOrderActivity.class);
        context.startActivity(intent);
    }
}
