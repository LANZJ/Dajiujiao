package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.FriendAdapter;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.task.data.my.FriendListData;
import com.zjyeshi.dajiujiao.buyer.task.my.FriendListTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/8/16.
 */
public class MyContactActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;
    private List<FriendListData.Friend> dataList = new ArrayList<FriendListData.Friend>();
    private FriendAdapter adapter;

    public static final int SELECTMEMBER = 1;
    public static final String TOMEMBERID = "to_member_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_can_see);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("选择好友").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new FriendAdapter(MyContactActivity.this , dataList , true);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent().putExtra(TOMEMBERID , dataList.get(position).getMemberId());
                setResult(RESULT_OK , intent);
                finish();
            }
        });
        loadData();

    }

    private void loadData(){
        List<FriendListData.Friend> localList = DaoFactory.getContactDao().findAll();
        if (Validators.isEmpty(localList)){
            loadFriend();
        }else{
            dataList.clear();
            dataList.addAll(localList);
            adapter.notifyDataSetChanged();
            loadFriend();
        }
    }

    /**获取好友列表
     *
     */
    private void loadFriend(){
        FriendListTask friendListTask = new FriendListTask(MyContactActivity.this);
        friendListTask.setShowProgressDialog(false);
        friendListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<FriendListData>() {
            @Override
            public void failCallback(Result<FriendListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        friendListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<FriendListData>() {
            @Override
            public void successCallback(Result<FriendListData> result) {
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                adapter.notifyDataSetChanged();
            }
        });

        friendListTask.execute();
    }
}
