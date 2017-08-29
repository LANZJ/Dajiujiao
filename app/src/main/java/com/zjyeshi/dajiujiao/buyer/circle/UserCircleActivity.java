package com.zjyeshi.dajiujiao.buyer.circle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.StringUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.UserInfoActivity;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyNotifyReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.SetListViewReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.CircleContentEntity;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.TopHeadEntity;
import com.zjyeshi.dajiujiao.buyer.circle.task.UserCircleTask;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LtGtEnum;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户圈子
 * Created by wuhk on 2016/8/23.
 */
public class UserCircleActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private BUPullToRefreshListView listView;

    private CircleAdapter circleAdapter;
    private List<BaseEntity> dataList = new ArrayList<BaseEntity>();

    private String memberId;
    private String memberName;
    private String memberPic;
    private String memberBgPic;

    private String lastTime = "0";
    private LtGtEnum ltGtEnum = LtGtEnum.LT;

    private boolean isRefreshing;
    //广播接收器
    private SetListViewReceiver setListViewReceiver;
    private OnlyNotifyReceiver onlyNotifyReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_circle_user);
        setListViewReceiver = new SetListViewReceiver() {
            @Override
            public void setListView(int position) {
                listView.setSelection(position);
            }
        };
        setListViewReceiver.register();

        onlyNotifyReceiver = new OnlyNotifyReceiver() {
            @Override
            public void onlyNotify() {
                refreshData();
            }
        };
        onlyNotifyReceiver.register();
        initWidgets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setListViewReceiver.unRegister();
        onlyNotifyReceiver.unRegister();
    }

    private void initWidgets(){
        memberId = getIntent().getStringExtra(UserInfoActivity.MEMBERID);
        memberName = getIntent().getStringExtra(UserInfoActivity.MEMBERNAME);
        memberPic = getIntent().getStringExtra(UserInfoActivity.MEMBERPIC);
        memberBgPic = getIntent().getStringExtra(UserInfoActivity.MEMBERBGPIC);

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle(memberName);

        listView.setCanPullDown(false);
        listView.setCanScrollUp(true);

        listView.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                //NOT SUPPORT
            }

            @Override
            public void onScrollUpRefresh() {
                if (!isRefreshing){
                    if (dataList.size() > 1) {
                        CircleContentEntity circleContentEntity = (CircleContentEntity) (dataList.get(dataList.size() - 1));
                        lastTime = String.valueOf(circleContentEntity.getTime().getTime());
                        ltGtEnum = LtGtEnum.LT;
                    } else {
                        //本地没有数据,就加载最新
                        lastTime = "0";
                        ltGtEnum = LtGtEnum.LT;
                    }
                    loadData();
                }
            }
        });

        circleAdapter = new CircleAdapter(UserCircleActivity.this , dataList);
        listView.setAdapter(circleAdapter);

        refreshData();

        loadData();
    }


    /**刷新数据
     *
     */
    private void refreshData(){
        dataList.clear();
        //头像
        TopHeadEntity topHeadEntity = new TopHeadEntity();
        topHeadEntity.setId(memberId);
        topHeadEntity.setHeadUrl(memberPic);
        topHeadEntity.setUserName(memberName);
        if (Validators.isEmpty(memberBgPic)){
            topHeadEntity.setHeadBgUrl(String.valueOf(R.drawable.test_touxiang_bg));
        }else{
            topHeadEntity.setHeadBgUrl(memberBgPic);
        }
        dataList.add(topHeadEntity);

        List<CircleData.Circle> circleList = DaoFactory.getUserCircleDao().findAll(memberId);
        for (CircleData.Circle circle : circleList) {
            CircleContentEntity circleContentEntity = new CircleContentEntity();
            circleContentEntity.setId(circle.getId());
            circleContentEntity.setContent(circle.getContent());
            circleContentEntity.setTime(circle.getCreationTime());
            circleContentEntity.setPraiseList(circle.getPraises());
            circleContentEntity.setEvaluateList(circle.getEvaluates());
            circleContentEntity.setMember(circle.getMember());

            String urlsStr = circle.getPics();
            if (!Validators.isEmpty(urlsStr)) {
                String[] urls = StringUtils.split(urlsStr, ",");
                circleContentEntity.setImageUrls(urls);
            }

            circleContentEntity.setPageType(circle.getLinkType());
            circleContentEntity.setPageContent(circle.getLinkTitle());
            circleContentEntity.setPageImage(circle.getLinkPic());
            circleContentEntity.setPageUrl(circle.getLinkContent());
            dataList.add(circleContentEntity);
        }
        circleAdapter.notifyDataSetChanged();
    }

    private void loadData(){
        isRefreshing = true;
        UserCircleTask userCircleTask = new UserCircleTask(UserCircleActivity.this);
        userCircleTask.setShowProgressDialog(false);
        userCircleTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CircleData>() {
            @Override
            public void failCallback(Result<CircleData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        userCircleTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CircleData>() {
            @Override
            public void successCallback(Result<CircleData> result) {
                isRefreshing = false;
                listView.onScrollUpRefreshComplete("上滑更多");
                if (Validators.isEmpty(result.getValue().getList())) {
                    listView.onScrollUpRefreshComplete("");
                    listView.onScrollUpNoMoreData("没有更多数据了");
                }
                refreshData();
            }
        });

        userCircleTask.execute(memberId , lastTime , ltGtEnum.getValueStr());
    }


    /**启动该Activity
     *
     * @param context
     * @param id
     * @param name
     * @param pic
     */
    public static void startActivity(Context context , String id, String name , String pic , String bgPic){
        Intent intent = new Intent();
        intent.setClass(context , UserCircleActivity.class);
        intent.putExtra(UserInfoActivity.MEMBERID , id);
        intent.putExtra(UserInfoActivity.MEMBERNAME , name);
        intent.putExtra(UserInfoActivity.MEMBERPIC , pic);
        intent.putExtra(UserInfoActivity.MEMBERBGPIC , bgPic);
        context.startActivity(intent);
    }
}
