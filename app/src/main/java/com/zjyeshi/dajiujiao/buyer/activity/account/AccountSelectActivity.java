package com.zjyeshi.dajiujiao.buyer.activity.account;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.CashAccountAdapter;
import com.zjyeshi.dajiujiao.buyer.task.account.GetCashAccountListTask;
import com.zjyeshi.dajiujiao.buyer.task.account.data.CashAccountList;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现账号选择
 * Created by wuhk on 2016/7/4.
 */
public class AccountSelectActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;
    private List<CashAccountList.CashAccount> dataList = new ArrayList<CashAccountList.CashAccount>();
    private CashAccountAdapter cashAccountAdapter;

    public static boolean isReload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account_sel);
        initWidgets();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isReload){
            loadData();
        }
    }

    private void initWidgets(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("选择账户");

        cashAccountAdapter = new CashAccountAdapter(AccountSelectActivity.this , dataList);
        listView.setAdapter(cashAccountAdapter);

        loadData();
    }

    public void loadData(){
        GetCashAccountListTask getCashAccountListTask = new GetCashAccountListTask(AccountSelectActivity.this);
        getCashAccountListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CashAccountList>() {
            @Override
            public void failCallback(Result<CashAccountList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getCashAccountListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CashAccountList>() {
            @Override
            public void successCallback(Result<CashAccountList> result) {
                isReload = false;
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                cashAccountAdapter.notifyDataSetChanged();
            }
        });

        getCashAccountListTask.execute();
    }
}
