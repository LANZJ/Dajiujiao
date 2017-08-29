package com.zjyeshi.dajiujiao.buyer.circle;

import android.os.Bundle;
import android.view.View;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.circle.task.MyCircleCollectionTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleCollectionData;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LtGtEnum;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 圈子收藏
 * Created by wuhk on 2016/8/16.
 */
public class CircleCollectActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private BUPullToRefreshListView listView;
    private List<CircleCollectionData.Collection> dataList = new ArrayList<CircleCollectionData.Collection>();
    private CircleCollectAdapter adapter;

    //上传参数
    private String lastTime = "0";
    private LtGtEnum ltGtEnum = LtGtEnum.LT;

    private boolean showDialog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_circle_collect);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("我的收藏");

        listView.setCanPullDown(false);
        listView.setCanScrollUp(true);

        listView.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                //不用下拉刷新
            }

            @Override
            public void onScrollUpRefresh() {
                //上拉加载
                showDialog = false;
                lastTime = String.valueOf(dataList.get(dataList.size() -1).getCreationTime());
                loadData();
            }
        });
        adapter = new CircleCollectAdapter(CircleCollectActivity.this , dataList);
        listView.setAdapter(adapter);

        loadData();
    }

    private void loadData(){
        MyCircleCollectionTask circleCollectionTask = new MyCircleCollectionTask(CircleCollectActivity.this);
        circleCollectionTask.setShowProgressDialog(showDialog);
        circleCollectionTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CircleCollectionData>() {
            @Override
            public void failCallback(Result<CircleCollectionData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        circleCollectionTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CircleCollectionData>() {
            @Override
            public void successCallback(Result<CircleCollectionData> result) {
                dataList.addAll(result.getValue().getList());
                listView.onScrollUpRefreshComplete("");
                listView.onScrollUpRefreshComplete("上滑更多");
                if (Validators.isEmpty(result.getValue().getList())) {
                    listView.onScrollUpRefreshComplete("");
                    listView.onScrollUpNoMoreData("没有更多数据了");
                }
                adapter.notifyDataSetChanged();
            }
        });

        circleCollectionTask.execute(lastTime , ltGtEnum.getValueStr());
    }
}
