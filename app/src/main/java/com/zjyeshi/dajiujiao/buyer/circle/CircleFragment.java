package com.zjyeshi.dajiujiao.buyer.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zjyeshi.dajiujiao.buyer.activity.frame.fragment.BaseFragment;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyNotifyReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.SetListViewReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.task.CircleDataTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LtGtEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.ChangeRemindShowReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ChangeAvatorReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.FriendlyTimeUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.buyer.model.UploadCircleBgModel;
import com.zjyeshi.dajiujiao.buyer.model.UploadHeadModel;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyRefrshReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.circle.view.CircleMenuView;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.CircleContentEntity;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.TopHeadEntity;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.StringUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 酒友圈子
 * <p/>
 * Created by xuan on 15/10/18.
 */
public class CircleFragment extends BaseFragment {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    public DGTitleLayout getTitleLayout() {
        return titleLayout;
    }

    @InjectView(R.id.listView)
    private BUPullToRefreshListView listView;
    @InjectView(R.id.fabuLayout)
    private RelativeLayout fabuLayout;
    @InjectView(R.id.addCircleLayout)
    private RelativeLayout addCircleLayout;
    @InjectView(R.id.showView)
    private View showView;
    @InjectView(R.id.newApplyIv)
    private ImageView newApplyIv;

    private CircleAdapter circleAdapter;
    private List<BaseEntity> dataList = new ArrayList<BaseEntity>();

    private String lastTime;
    private LtGtEnum ltGtEnum;
    private boolean isPullDownRefresh;

    private UploadHeadModel uploadHeadModel = new UploadHeadModel();
    private UploadCircleBgModel uploadCircleBgModel = new UploadCircleBgModel();
    public static boolean isBgChange;

    private long firstTime = (new Date()).getTime() ;//上一次刷新的时间
    private long secondTime ;//第二次进入这个页面的时间

    private SetListViewReceiver setListViewReceiver;//设置listView广播
    private OnlyRefrshReceiver onlyRefrshReceiver;//发表新动态后刷新一次
    private OnlyNotifyReceiver onlyNotifyReceiver;//不请求网络刷新列表
    private ChangeAvatorReceiver changeAvatorReceiver;//修改头像
    private ChangeRemindShowReceiver changeRemindShowReceiver;//显示好友请求数量广播

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        register();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    @Override
    public void onResume() {
        super.onResume();
        secondTime = (new Date()).getTime();

        //比较两个时间相差是否超过五分钟
        boolean isRefresh = FriendlyTimeUtil.isMoreThanFive(firstTime, secondTime);
        if (isRefresh){
            doPullDownRefresh();
        }
    }


    @Override
    protected int initFragmentView() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initFragmentWidgets(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        titleLayout.configTitle("酒友");
        changeApplyRemind();
        //发布动态
        fabuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleMenuView circleMenuView = new CircleMenuView(getActivity() , ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
                //设置默认获取焦点
                circleMenuView.setFocusable(true);
                //以某个控件的x和y的偏移量位置开始显示窗口
                circleMenuView.showAsDropDown(showView , 0 , 0);
                //如果窗口存在，则更新
                circleMenuView.update();
            }
        });

        addCircleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CircleAddActivity.class));
            }
        });

        //初始化listView
        listView.setCanScrollUp(true);
        listView.setCanPullDown(true);

        listView.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                //下拉刷新
                doPullDownRefresh();
            }

            @Override
            public void onScrollUpRefresh() {
                //上滑更多
                doScrollUpRefresh();
            }
        });

        circleAdapter = new CircleAdapter(getActivity(), dataList);
        listView.setAdapter(circleAdapter);

        refreshData();//先加载本地数据库中的数据

        //进行一次下来刷新
        doPullDownRefresh();
    }

    //重服务器上加载并刷新
    private void loadDataAndRefresh() {
        CircleDataTask circleDataTask = new CircleDataTask(getActivity());
        circleDataTask.setShowProgressDialog(false);
        circleDataTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CircleData>() {
            @Override
            public void failCallback(Result<CircleData> result) {
                LogUtil.e(result.getMessage());
                if (isPullDownRefresh) {
                    listView.onPullDownRefreshComplete("下拉刷新");
                } else {
                    listView.onScrollUpRefreshComplete("上滑更多");
                }
            }
        });
        circleDataTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CircleData>() {
            @Override
            public void successCallback(Result<CircleData> result) {
                if (isPullDownRefresh) {
                    listView.onPullDownRefreshComplete("最新更新:" + DateUtils.date2StringBySecond(new Date()));
                    firstTime = (new Date()).getTime();
//                    //重新开启上拉更多
                    listView.setCanScrollUp(false);
                    listView.setCanScrollUp(true);
                } else {
                    listView.onScrollUpRefreshComplete("上滑更多");
                    if (Validators.isEmpty(result.getValue().getList())) {
                        listView.onScrollUpRefreshComplete("");
                        listView.onScrollUpNoMoreData("没有更多数据了");
                    }
                }
                refreshData();
            }
        });
        circleDataTask.execute(lastTime, ltGtEnum.getValueStr());
    }

    //从本地数据库加载刷新
    private void refreshData() {
        dataList.clear();
        //头像
        TopHeadEntity topHeadEntity = new TopHeadEntity();
        topHeadEntity.setId(LoginedUser.getLoginedUser().getId());
        topHeadEntity.setHeadUrl(LoginedUser.getLoginedUser().getPic());
        topHeadEntity.setUserName(LoginedUser.getLoginedUser().getName());
        String bg = LoginedUser.getLoginedUser().getCircleBackgroundPic();
        if (Validators.isEmpty(bg)){
            topHeadEntity.setHeadBgUrl(String.valueOf(R.drawable.test_touxiang_bg));
        }else{
            topHeadEntity.setHeadBgUrl(LoginedUser.getLoginedUser().getCircleBackgroundPic());
        }
        topHeadEntity.setUploadHeadModel(uploadHeadModel);
        topHeadEntity.setUploadCircleBgModel(uploadCircleBgModel);
        topHeadEntity.setFragment(this);
        dataList.add(topHeadEntity);

        List<CircleData.Circle> circleList = DaoFactory.getCircleDao().findAll();
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

    //下拉刷新 : 下拉刷新全都改为取最新的一段数据
    private void doPullDownRefresh() {
        isPullDownRefresh = true;
        lastTime = "0";
        ltGtEnum = LtGtEnum.LT;
        loadDataAndRefresh();
    }

    //上滑更多
    private void doScrollUpRefresh() {
        isPullDownRefresh = false;
        if (dataList.size() > 1) {
            CircleContentEntity circleContentEntity = (CircleContentEntity) (dataList.get(dataList.size() - 1));
            lastTime = String.valueOf(circleContentEntity.getTime().getTime());
            ltGtEnum = LtGtEnum.LT;
        } else {
            //本地没有数据,就加载最新
            lastTime = "0";
            ltGtEnum = LtGtEnum.LT;
        }
        loadDataAndRefresh();
    }
    //注册广播
    private void register(){
        //刷新第一行显示位置
        setListViewReceiver = new SetListViewReceiver() {
            @Override
            public void setListView(int position) {
                listView.setSelection(position);
            }
        };
        setListViewReceiver.register();
        //发表新动态后刷新
        onlyRefrshReceiver = new OnlyRefrshReceiver() {
            @Override
            public void refreshList() {
                doPullDownRefresh();
            }
        };
        onlyRefrshReceiver.register();
        //不请求网络刷新列表 : 修改酒友圈全都改为此广播刷新
        onlyNotifyReceiver = new OnlyNotifyReceiver() {
            @Override
            public void onlyNotify() {
                refreshData();
            }
        };
        onlyNotifyReceiver.register();
        //修改头像
        changeAvatorReceiver = new ChangeAvatorReceiver() {
            @Override
            public void changeAvator() {
                refreshData();
            }
        };
        changeAvatorReceiver.register();
        //显示好友数量
        changeRemindShowReceiver = new ChangeRemindShowReceiver() {
            @Override
            public void change() {
                changeApplyRemind();
            }
        };
        changeRemindShowReceiver.register();
    }

    //解绑广播
    private void unRegister(){
        setListViewReceiver.unRegister();
        onlyRefrshReceiver.unRegister();
        onlyNotifyReceiver.unRegister();
        changeAvatorReceiver.unRegister();
        changeRemindShowReceiver.unregister();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode){
            return;
        }

        if (isBgChange){
            uploadCircleBgModel.onActivityResult(requestCode, resultCode, data);
        }else{
            uploadHeadModel.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void changeApplyRemind(){
        NewRemind data = NewRemind.getNewRemind();
        int apply = data.getFriendApplyCount();
        if (apply == 0){
            newApplyIv.setVisibility(View.GONE);
        }else{
            newApplyIv.setVisibility(View.VISIBLE);
        }
    }
}
