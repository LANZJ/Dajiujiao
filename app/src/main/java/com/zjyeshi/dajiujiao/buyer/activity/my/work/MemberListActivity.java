package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.YwyAdapter;
import com.zjyeshi.dajiujiao.buyer.task.work.MemberListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.SalemanListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/8/25.
 */
public class MemberListActivity extends BaseActivity{
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;
    private List<SalemanListData.Saleman> dataList = new ArrayList<SalemanListData.Saleman>();
    private YwyAdapter adapter;

    private boolean isContact;//从联系进入
    private boolean isOrdered;//从订单进入


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_member_list);

        initWidgets();
    }

    private void initWidgets(){

        isContact = getIntent().getBooleanExtra(HmcActivity.IS_CONTACT , false);
        isOrdered = getIntent().getBooleanExtra(HmcActivity.IS_ORDERED , false);


        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("选择人员");

        adapter = new YwyAdapter(MemberListActivity.this , dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SalemanListData.Saleman saleman = dataList.get(position);
                Intent intent = new Intent();
                intent.putExtra(HmcActivity.MEMBER_ID , saleman.getMemberId());
                intent.putExtra(HmcActivity.IS_CONTACT , isContact);
                intent.putExtra(HmcActivity.IS_ORDERED , isOrdered);
                intent.setClass(MemberListActivity.this , HmcActivity.class);
                startActivity(intent);
            }
        });
        loadData();
    }

    private void loadData(){
        MemberListTask memberListTask = new MemberListTask(MemberListActivity.this);
        memberListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<SalemanListData>() {
            @Override
            public void failCallback(Result<SalemanListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        memberListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<SalemanListData>() {
            @Override
            public void successCallback(Result<SalemanListData> result) {
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                adapter.notifyDataSetChanged();
            }
        });

        memberListTask.execute();

    }
}
