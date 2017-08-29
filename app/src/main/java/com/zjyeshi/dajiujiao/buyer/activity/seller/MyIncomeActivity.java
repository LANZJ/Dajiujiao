package com.zjyeshi.dajiujiao.buyer.activity.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseTitleActivity;
import com.zjyeshi.dajiujiao.buyer.activity.account.ApplyAccountActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.seller.MyIncomeAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.sellerincome.DetailIncomeEntity;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.Wallet;
import com.zjyeshi.dajiujiao.buyer.task.data.seller.IncomeData;
import com.zjyeshi.dajiujiao.buyer.task.my.MyAccountTask;
import com.zjyeshi.dajiujiao.buyer.task.seller.GetIncomeTask;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收入
 * Created by xuan on 15/10/29.
 */
public class MyIncomeActivity extends BaseTitleActivity {
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.amountTv)
    private TextView amountTv;
    @InjectView(R.id.incomeTv)
    private TextView incomeTv;
    @InjectView(R.id.marketMoneyTv)
    private TextView marketMoneyTv;
    @InjectView(R.id.outTv)
    private TextView outTv;
    @InjectView(R.id.inLayout)
    private LinearLayout inLayout;
    @InjectView(R.id.outLayout)
    private LinearLayout outLayout;
    @InjectView(R.id.outFlagIv)
    private ImageView outFlagIv;
    @InjectView(R.id.inFlagIv)
    private ImageView inFlagIv;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;
    @InjectView(R.id.marketSupportLayout)
    private LinearLayout marketSupportLayout;

    private MyIncomeAdapter myIncomeAdapter;
    private List<DetailIncomeEntity> dataList = new ArrayList<DetailIncomeEntity>();
    private List<IncomeData.Income> incomeList = new ArrayList<IncomeData.Income>();
    private List<IncomeData.Income> inList = new ArrayList<IncomeData.Income>();
    private List<IncomeData.Income> outList = new ArrayList<IncomeData.Income>();

    private float allIn = 0.00f;//总收入
    private float allOut = 0.00f ;//总支出
    private float profit = 0.00f ;//净收入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_layout_income_my);
        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configTitle("钱包").configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configRightText("提现", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyIncomeActivity.this , ApplyAccountActivity.class);
                startActivity(intent);
            }
        });

        refreshData();

        inLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inFlagIv.setVisibility(View.VISIBLE);
                outFlagIv.setVisibility(View.GONE);
                sort(inList);
            }
        });

        outLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inFlagIv.setVisibility(View.GONE);
                outFlagIv.setVisibility(View.VISIBLE);
                sort(outList);
            }
        });

        if (AuthUtil.showMarketCostTab()){
            marketSupportLayout.setVisibility(View.VISIBLE);
        }else {
            marketSupportLayout.setVisibility(View.GONE);

        }

        myIncomeAdapter = new MyIncomeAdapter(MyIncomeActivity.this, dataList);
        listView.setAdapter(myIncomeAdapter);
    }

    private void refreshData() {
        GetIncomeTask getIncomeTask = new GetIncomeTask(MyIncomeActivity.this);
        getIncomeTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<IncomeData>() {
            @Override
            public void failCallback(Result<IncomeData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getIncomeTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<IncomeData>() {
            @Override
            public void successCallback(Result<IncomeData> result) {
                incomeList.clear();
                incomeList.addAll(result.getValue().getList());
                sortOutData(incomeList);
                myIncomeAdapter.notifyDataSetChanged();
            }
        });
        getIncomeTask.execute();
    }



    //判断是否是同一天，并整合数据
    private void sortOutData(List<IncomeData.Income> disOrderList){
        // 设置收支，根据接受返回正负计算
        getIncomeAndOut(disOrderList);

        getMyAccount();

        //默认显示收入
        sort(inList);
    }

    //计算收入支出总数
    private void getIncomeAndOut(List<IncomeData.Income> list){
        inList.clear();
        outList.clear();
        for (IncomeData.Income income : list){
            String price = income.getAmount().substring(1);
            String flag = income.getAmount().substring(0 , 1);
            if (flag.equals("-")){
                allOut += Float.parseFloat(price)/100;
                income.setIsIn(false);
                outList.add(income);
            }else{
                allIn += Float.parseFloat(price)/100;
                income.setIsIn(true);
                inList.add(income);
            }
        }
    }

    private void getMyAccount(){
        MyAccountTask myAccountTask = new MyAccountTask(MyIncomeActivity.this);
        myAccountTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<Wallet>() {
            @Override
            public void failCallback(Result<Wallet> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        myAccountTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<Wallet>() {
            @Override
            public void successCallback(Result<Wallet> result) {
                profit = Float.parseFloat(result.getValue().getAccount())/100;
                //可提现金额
                amountTv.setText(ExtraUtil.format(profit));
                //市场返还费用
                marketMoneyTv.setText(ExtraUtil.format(Float.parseFloat(result.getValue().getMarketCost())/100));

                incomeTv.setText(ExtraUtil.format(allIn));
                outTv.setText(ExtraUtil.format(allOut));

            }
        });

        myAccountTask.execute();
    }


    private void  sort(final List<IncomeData.Income> disOrderList){
        dataList.clear();
        HashMap<String , List<IncomeData.Income>> sortMap = new HashMap<String, List<IncomeData.Income>>();
        for (IncomeData.Income income : disOrderList){
            String weekTime = DateUtils.date2StringByDay(income.getCreationTime());
            List<IncomeData.Income> tempList = sortMap.get(weekTime);
            if (Validators.isEmpty(tempList)){
                tempList = new ArrayList<IncomeData.Income>();
                tempList.add(income);

                sortMap.put(weekTime , tempList);
            }else{
                sortMap.get(weekTime).add(income);
            }
        }

        for (Map.Entry<String, List<IncomeData.Income>> entry : sortMap
                .entrySet()) {
            String key = entry.getKey();
            List<IncomeData.Income> itemList = entry.getValue();
            DetailIncomeEntity detailIncomeEntity = new DetailIncomeEntity();
            detailIncomeEntity.setTime(key);
            detailIncomeEntity.setDetailList(itemList);
            dataList.add(detailIncomeEntity);
        }

        Collections.sort(dataList, new Comparator<DetailIncomeEntity>() {
            @Override
            public int compare(DetailIncomeEntity lhs, DetailIncomeEntity rhs) {

                return rhs.getTime().compareTo(lhs.getTime());
            }
        });

        noDataView.showIfEmpty(dataList);
        myIncomeAdapter.notifyDataSetChanged();
    }
}
