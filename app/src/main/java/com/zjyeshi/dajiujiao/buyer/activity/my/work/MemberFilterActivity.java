package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.utils.BDActivityManager;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.DateReportMemberAdapter;
import com.zjyeshi.dajiujiao.buyer.receiver.OrderFilterReceiver;
import com.zjyeshi.dajiujiao.buyer.task.work.SalesmanListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.NewRemindData;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.YwyAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.ChangeRemindShowReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.RefreshDateReportReceiver;
import com.zjyeshi.dajiujiao.buyer.task.work.NewRemindTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.SalemanListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人员筛选
 * Created by wuhk on 2016/8/24.
 */
public class MemberFilterActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private ListView listView;
    @InjectView(R.id.selectLayout)
    private RelativeLayout selectLayout;
    @InjectView(R.id.selectTv)
    private TextView selectTv;

    private List<SalemanListData.Saleman> dataList = new ArrayList<SalemanListData.Saleman>();
    private DateReportMemberAdapter adapter;

    private ChangeRemindShowReceiver changeRemindShowReceiver;
    private RefreshDateReportReceiver refreshDateReportReceiver;

    public static final String TOPROLE = "top_role";
    public static final String TOPNAME = "top_name";
    public static final String TOPID = "top_id";
    public static final String FROMWHERE = "from_where";

    public static final String SELECT_DEALER = "选择经销商";
    public static final String SELECT_TERMINAL = "选择终端";
    //参数
    private int topRole;//当前界面人的级别
    private String topName;//当前界面人的姓名
    private String topId;//当前界面人的ID
    private boolean isOrderFilter;//用来区别是从订单进入的还是日报进入的筛选

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_member_filter);
        initWidgets();

        changeRemindShowReceiver = new ChangeRemindShowReceiver() {
            @Override
            public void change() {
                changeUnreadNum();
                adapter.notifyDataSetChanged();
            }
        };
        changeRemindShowReceiver.register();

        refreshDateReportReceiver = new RefreshDateReportReceiver() {
            @Override
            public void refresh() {
                NewRemindTask newRemindTask = new NewRemindTask(MemberFilterActivity.this);
                newRemindTask.setShowProgressDialog(false);
                newRemindTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NewRemindData>() {
                    @Override
                    public void failCallback(Result<NewRemindData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });

                newRemindTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NewRemindData>() {
                    @Override
                    public void successCallback(Result<NewRemindData> result) {
                        changeUnreadNum();
                        adapter.notifyDataSetChanged();
                    }
                });

                newRemindTask.execute();
            }
        };
        refreshDateReportReceiver.register();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != NewRemind.getNewRemind()){
            changeUnreadNum();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        changeRemindShowReceiver.unregister();
        refreshDateReportReceiver.unregister();
    }

    private void initWidgets(){
        topRole = getIntent().getIntExtra(TOPROLE , -1);
        topName = getIntent().getStringExtra(TOPNAME);
        topId = getIntent().getStringExtra(TOPID);
        if (Validators.isEmpty(topId)){
            topId = "";
        }
        isOrderFilter = getIntent().getBooleanExtra(FROMWHERE , false);

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle(topName);

        UserEnum userEnum = UserEnum.valueOf(topRole);
        if (userEnum.equals(UserEnum.AGENT)){
            //总代级别的，显示选择经销商
            selectTv.setText(SELECT_DEALER);
        }else if(userEnum.equals(UserEnum.DEALER)){
            //经销商级别的，显示选则终端
            selectTv.setText(SELECT_TERMINAL);
        }else if(userEnum.equals(UserEnum.TERMINAL)){
            //终端级别的，直接显示业务员，隐藏选择一栏
            selectLayout.setVisibility(View.GONE);
        }

        selectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击进入选择下级的界面
                SubMemberActivity.startActivity(MemberFilterActivity.this , topName , topId , selectTv.getText().toString() , isOrderFilter);
            }
        });

        adapter = new DateReportMemberAdapter(MemberFilterActivity.this , dataList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SalemanListData.Saleman saleman = dataList.get(position);
                if (isOrderFilter){
                    //从订单筛选进入，发起刷新订单列表广播 ， 关掉相应界面
                    OrderFilterReceiver.notifyReceiver(saleman.getMemberId());
                    BDActivityManager.removeAndFinishIncludes(MemberFilterActivity.class.getSimpleName() , SubMemberActivity.class.getSimpleName());
                }else{
                    //从日报筛选进入,这是直接进入筛选的，点击业务员之后别关闭之前的相关页面，跳转到相应的界面
                    Intent dateIntent= new Intent();
                    dateIntent.setClass(MemberFilterActivity.this , DateReportActivity.class);
                    dateIntent.putExtra(DateReportActivity.PERSONID , saleman.getMemberId());
                    startActivity(dateIntent);
//                    BDActivityManager.removeAndFinishIncludes(MemberFilterActivity.class.getSimpleName() , SubMemberActivity.class.getSimpleName());
                }
            }
        });
        loadData();
    }

    private void loadData(){
        SalesmanListTask salesmanListTask = new SalesmanListTask(MemberFilterActivity.this);
        salesmanListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<SalemanListData>() {
            @Override
            public void failCallback(Result<SalemanListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        salesmanListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<SalemanListData>() {
            @Override
            public void successCallback(Result<SalemanListData> result) {
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                if (!isOrderFilter){
                    changeUnreadNum();
                }
                adapter.notifyDataSetChanged();
            }
        });

        salesmanListTask.execute(topId);
    }

    private void changeUnreadNum(){
        NewRemind newRemind = NewRemind.getNewRemind();
        if (!Validators.isEmpty(newRemind.getUnReadDailyList())){
            Map<String,Integer> memberId2CountMap = new HashMap<String,Integer>();
            for (NewRemindData.UnReadDaily unReadDaily : newRemind.getUnReadDailyList()){
                memberId2CountMap.put(unReadDaily.getMemberId(), unReadDaily.getUnreadCount());
            }
            for(SalemanListData.Saleman saleman : dataList){
                Integer integer = memberId2CountMap.get(saleman.getMemberId());
                if(null != integer){
                    saleman.setUnreadNum(integer);
                }else{
                    saleman.setUnreadNum(0);
                }
            }
        }else{
            for (SalemanListData.Saleman saleman : dataList){
                saleman.setUnreadNum(0);
            }
        }
    }

    public static void startActivity(Context context , int role , String name , String id , boolean isOrderFilter){
        Intent intent = new Intent();
        intent.putExtra(TOPROLE , role);
        intent.putExtra(TOPNAME , name);
        intent.putExtra(TOPID , id);
        intent.putExtra(FROMWHERE , isOrderFilter);
        intent.setClass(context , MemberFilterActivity.class);
        context.startActivity(intent);
    }
}
