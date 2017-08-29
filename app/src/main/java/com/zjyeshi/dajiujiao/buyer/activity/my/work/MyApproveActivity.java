package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.tab.sw.BUDefaultTabHost;
import com.xuan.bigappleui.lib.view.tab.sw.BUSwTabHost;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.task.work.WaitReviewListTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.PassApproveListAdapter;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.WaitApproveListAdapter;
import com.zjyeshi.dajiujiao.buyer.receiver.work.UpdateReceiver;
import com.zjyeshi.dajiujiao.buyer.task.work.data.WaitReview;
import com.zjyeshi.dajiujiao.buyer.task.work.data.WaitReviewListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 待我审批
 * <p>
 * Created by zhum on 2016/6/16.
 */
public class MyApproveActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏

    @InjectView(R.id.tabLayout)
    private BUSwTabHost tabLayout;
    private BUPullToRefreshListView listView1;
    private BUPullToRefreshListView listView2;
    private WaitApproveListAdapter adapter1;
    private PassApproveListAdapter adapter2;
    private List<WaitReview> dataLists1;
    private List<WaitReview> dataLists2;
    private TextView textView1;
    private TextView textView2;

    private UpdateReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_approve);

        receiver = new UpdateReceiver() {
            @Override
            public void aliResult() {
                getData("1");
                getData("2");
            }
        };
        receiver.register();

        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configTitle("待我审批").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initTabHost();
    }

    private void initTabHost() {
        textView1 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView1.setText("待审批");
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(0);
            }
        });
        textView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.textview_tab, null);
        textView2.setText("已审批");
        textView2.setTextColor(getResources().getColor(R.color.color_9c9893));
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.gotoPosition(1);
            }
        });

        listView1 = (BUPullToRefreshListView) LayoutInflater.from(this).inflate(R.layout.listview_approve, null);
        listView2 = (BUPullToRefreshListView) LayoutInflater.from(this).inflate(R.layout.listview_approve, null);
        dataLists1 = new ArrayList<>();
        adapter1 = new WaitApproveListAdapter(MyApproveActivity.this,dataLists1);
        listView1.setAdapter(adapter1);

        dataLists2 = new ArrayList<>();
        adapter2 = new PassApproveListAdapter(MyApproveActivity.this,dataLists2);
        listView2.setAdapter(adapter2);
        tabLayout.addTabAndContent(textView1, listView1);
        tabLayout.addTabAndContent(textView2, listView2);

        tabLayout.setTabHost(new BUDefaultTabHost() {
            @Override
            public View getIndicator(Context context) {
                // 设置一个自定义的指示器
                FrameLayout vLayout = new FrameLayout(MyApproveActivity.this);
                vLayout.setPadding(40, 5, 40, 0);
                View v = new View(MyApproveActivity.this);
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
                        if (dataLists1.size()==0){
                            getData("1");
                        }
                        break;
                    case 1:
                        textView2.setTextColor(getResources().getColor(R.color.color_theme));
                        textView1.setTextColor(getResources().getColor(R.color.color_9c9893));
                        if (dataLists2.size()==0){
                            getData("2");
                        }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.setup();
        tabLayout.initPosition(0);

        listView1.setCanScrollUp(false);
        listView1.setCanPullDown(false);
        listView2.setCanPullDown(false);
        listView2.setCanScrollUp(false);
        getData("1");
        getData("2");
    }

    private void getData(final String status){
        WaitReviewListTask waitReviewListTask = new WaitReviewListTask(MyApproveActivity.this);
        waitReviewListTask.setShowProgressDialog(true);
        waitReviewListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<WaitReviewListData>() {
            @Override
            public void successCallback(Result<WaitReviewListData> result) {
                WaitReviewListData data = result.getValue();
                if (status.equals("1")){
                    //待审批
                    dataLists1.clear();
                    dataLists1.addAll(data.getList());
                    textView1.setText("待审批(" + dataLists1.size() + ")");
                    adapter1.notifyDataSetChanged();
                }else {
                    dataLists2.clear();
                    dataLists2.addAll(data.getList());
                    textView2.setText("已审批(" + dataLists2.size() + ")");
                    adapter2.notifyDataSetChanged();
                }
            }
        });

        waitReviewListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<WaitReviewListData>() {
            @Override
            public void failCallback(Result<WaitReviewListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        waitReviewListTask.execute(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.unRegister();
    }
}