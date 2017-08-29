package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.tab.sw.BUDefaultTabHost;
import com.xuan.bigappleui.lib.view.tab.sw.BUSwTabHost;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.FriendAdapter;
import com.zjyeshi.dajiujiao.buyer.adapter.my.FriendApplyAdapter;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.receiver.UpdateFriendReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.my.FriendApplyListData;
import com.zjyeshi.dajiujiao.buyer.task.data.my.FriendListData;
import com.zjyeshi.dajiujiao.buyer.task.my.FriendApplyListTask;
import com.zjyeshi.dajiujiao.buyer.task.my.FriendListTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 酒友通讯录
 * Created by wuhk on 2016/8/16.
 */
public class FriendActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.tabLayout)
    private BUSwTabHost tabLayout;
    private TextView textView1;
    private TextView textView2;
    private ListView listView1;
    private ListView listView2;
    private List<FriendListData.Friend> dataLists1;
    private FriendAdapter adapter1;
    private List<FriendApplyListData.FriendApply> dataLists2;
    private FriendApplyAdapter adapter2;
    private boolean isFirst = true;

    private UpdateFriendReceiver updateFriendReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_friend);
        initWidgets();

        updateFriendReceiver = new UpdateFriendReceiver() {
            @Override
            public void update() {
                loadFriend();
                loadApply();
            }
        };
        updateFriendReceiver.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateFriendReceiver.unregister();
    }

    private void initWidgets(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("通讯录");

        initTabHost();
    }

    private void initTabHost() {
        textView1 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView1.setText("我的酒友");
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(0);
            }
        });
        textView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView2.setText("酒友申请");
        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(1);
            }
        });

        listView1 = (ListView) LayoutInflater.from(this).inflate(R.layout.dg_include_listview, null);
        listView2 = (ListView) LayoutInflater.from(this).inflate(R.layout.dg_include_listview, null);
        dataLists1 = new ArrayList<>();
        adapter1 = new FriendAdapter(FriendActivity.this,dataLists1 , false);
        listView1.setAdapter(adapter1);

        dataLists2 = new ArrayList<>();
        adapter2 = new FriendApplyAdapter(FriendActivity.this,dataLists2);
        listView2.setAdapter(adapter2);
        tabLayout.addTabAndContent(textView1, listView1);
        tabLayout.addTabAndContent(textView2, listView2);

        tabLayout.setTabHost(new BUDefaultTabHost() {
            @Override
            public View getIndicator(Context context) {
                // 设置一个自定义的指示器
                FrameLayout vLayout = new FrameLayout(FriendActivity.this);
                vLayout.setPadding(40, 5, 40, 0);
                View v = new View(FriendActivity.this);
                v.setBackgroundColor(getResources().getColor(R.color.color_theme));
                vLayout.addView(v);
                return vLayout;
            }
        });
        tabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        textView1.setTextColor(getResources().getColor(R.color.color_theme));
                        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
                        loadFriend();
                        break;
                    case 1:
                        textView2.setTextColor(getResources().getColor(R.color.color_theme));
                        textView1.setTextColor(getResources().getColor(R.color.color_9c9893));
                        loadApply();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.setup();
        tabLayout.initPosition(0);

        loadLocalContact();
        loadApply();
    }

    /**获取好友列表
     *
     */
    private void loadFriend(){
        FriendListTask friendListTask = new FriendListTask(FriendActivity.this);
        friendListTask.setShowProgressDialog(isFirst);

        friendListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<FriendListData>() {
            @Override
            public void failCallback(Result<FriendListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        friendListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<FriendListData>() {
            @Override
            public void successCallback(Result<FriendListData> result) {
                dataLists1.clear();
                dataLists1.addAll(result.getValue().getList());
                adapter1.notifyDataSetChanged();
                isFirst = false;
            }
        });

        friendListTask.execute();
    }

    /**显示本地通讯录
     *
     */
    private void loadLocalContact(){
        List<FriendListData.Friend> localList = DaoFactory.getContactDao().findAll();
        if (Validators.isEmpty(localList)){
            loadFriend();
        }else{
            dataLists1.clear();
            dataLists1.addAll(localList);
            adapter1.notifyDataSetChanged();
            isFirst = false;
            loadFriend();
        }
    }

    /**获取请求列表
     *
     */
    private void loadApply(){
        FriendApplyListTask applyListTask = new FriendApplyListTask(FriendActivity.this);
        applyListTask.setShowProgressDialog(false);
        applyListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<FriendApplyListData>() {
            @Override
            public void failCallback(Result<FriendApplyListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        applyListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<FriendApplyListData>() {
            @Override
            public void successCallback(Result<FriendApplyListData> result) {
                dataLists2.clear();
                dataLists2.addAll(result.getValue().getList());
                int num = 0;
                for (FriendApplyListData.FriendApply apply : dataLists2){
                    if (apply.isApplicant()){
                        num ++;
                    }
                }
                if (num == 0){
                    textView2.setText("酒友申请" );
                }else{
                    textView2.setText("酒友申请(" + num + ")" );
                }
                adapter2.notifyDataSetChanged();
            }
        });
        applyListTask.execute();
    }
}
