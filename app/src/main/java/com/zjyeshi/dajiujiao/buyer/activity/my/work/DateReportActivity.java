package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListView;
import com.xuan.bigappleui.lib.view.listview.BUPullToRefreshListViewListener;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;
import com.zjyeshi.dajiujiao.buyer.task.work.data.DateReportData;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.DateReportListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LtGtEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.DateReport;
import com.zjyeshi.dajiujiao.buyer.receiver.work.UpdateReceiver;
import com.zjyeshi.dajiujiao.buyer.task.work.DailyListTask;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.OnWheelScrollListener;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.WheelView;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.adapter.NumericWheelAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 工作日报
 *
 * Created by zhum on 2016/6/17.
 */
public class DateReportActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.listView)
    private BUPullToRefreshListView listView;//列表

    @InjectView(R.id.upTv)
    private TextView upTv;//上一月
    @InjectView(R.id.currentMonthTv)
    private TextView currentMonthTv;//当前月份
    @InjectView(R.id.nextTv)
    private TextView nextTv;//下一月
    @InjectView(R.id.nextIv)
    private ImageView nextIv;


    @InjectView(R.id.yearWl)
    private WheelView yearWl;
    @InjectView(R.id.monthWl)
    private WheelView monthWl;
    @InjectView(R.id.sureBtn)
    private TextView sureBtn;
    @InjectView(R.id.timePickerLayout)
    private RelativeLayout timePickerLayout;
    @InjectView(R.id.remainLayout)
    private View remainLayout;

    private long time;
    private Date curDate;

    private List<DateReport> dataLists;
    private DateReportListAdapter adapter;

    private boolean isPullDownRefresh = true;
    private boolean canPullDown = true;
    private boolean canScrollUp = true;
    private String lastTime = "0";
    private LtGtEnum ltGtEnum = LtGtEnum.LT;
    private boolean showDialog = true;

    public static final String PERSONID  = "personId";
    private String personId = "";

    private UpdateReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_date_report);
        registerReceiver();
        initWidgets();
    }

    private void initWidgets(){
        personId = getIntent().getStringExtra(PERSONID);

        if (Validators.isEmpty(personId)){
            personId = "";
        }

        if (!AuthUtil.dateGoFilterDirect()){
            titleLayout.configRightText("写日报", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DateReportActivity.this,WriteDateReportActivity.class);
                    intent.putExtra("type",WriteDateReportActivity.WRITE);
                    startActivity(intent);
                }
            });
        }

        titleLayout.configTitle("工作日报").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        time = System.currentTimeMillis();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
        curDate = new Date(time);//获取当前时间
        String str = formatter.format(curDate);
        currentMonthTv.setText(str);
        nextIv.setVisibility(View.GONE);
        nextTv.setVisibility(View.GONE);
        upTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextIv.setVisibility(View.VISIBLE);
                nextTv.setVisibility(View.VISIBLE);
                curDate = DateUtils.addDay(curDate,-30);
                String str = formatter.format(curDate);
                currentMonthTv.setText(str);
                refreshData();
            }
        });

        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(time);
                if (!(date.getMonth()==curDate.getMonth() && date.getYear()== curDate.getYear())){
                    curDate = DateUtils.addDay(curDate,30);
                    Date judgeDate = new Date();
                    if ((curDate.getMonth()==judgeDate.getMonth() && curDate.getYear()== judgeDate.getYear())){
                        nextTv.setVisibility(View.GONE);
                        nextIv.setVisibility(View.GONE);
                    }else{
                        nextIv.setVisibility(View.VISIBLE);
                        nextIv.setVisibility(View.VISIBLE);
                    }
                    String str = formatter.format(curDate);
                    currentMonthTv.setText(str);

                    refreshData();
                }
            }
        });


        currentMonthTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSelecWidget();
                timePickerLayout.setVisibility(View.VISIBLE);
            }
        });
        remainLayout.setClickable(true);
        remainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerLayout.setVisibility(View.GONE);
            }
        });

        listView.setCanPullDown(true);
        listView.setCanScrollUp(true);
        listView.setPullToRefreshListViewListener(new BUPullToRefreshListViewListener() {
            @Override
            public void onPullDownRefresh() {
                //下拉刷新
                if (canPullDown){
                    if(!Validators.isEmpty(dataLists)){
                        showDialog = false;
                        isPullDownRefresh = true;
                        canPullDown = false;
                        lastTime = String.valueOf(dataLists.get(0).getCreationTime());
                        ltGtEnum = LtGtEnum.GT;
                        initDatas();
                    }
                }
            }

            @Override
            public void onScrollUpRefresh() {
                //上滑更多
                if (canScrollUp){
                    if (dataLists.size() > 1){
                        showDialog = false;
                        isPullDownRefresh = false;
                        canScrollUp = false;
                        lastTime = String.valueOf(dataLists.get(dataLists.size()-1).getCreationTime());
                        ltGtEnum = LtGtEnum.LT;
                        initDatas();
                    }
                }
            }
        });

        dataLists = new ArrayList<>();
        adapter = new DateReportListAdapter(DateReportActivity.this,dataLists);
        listView.setAdapter(adapter);

        initDatas();
    }

    private void initDatas(){
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String filterTime = formatter.format(curDate);
        DailyListTask task = new DailyListTask(DateReportActivity.this);
        task.setShowProgressDialog(showDialog);
        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<DateReportData>() {
            @Override
            public void successCallback(Result<DateReportData> result) {
                if (isPullDownRefresh){
                    listView.onPullDownRefreshComplete("最新更新:" + DateUtils.date2StringBySecond(new Date()));
                    dataLists.addAll(0 , result.getValue().getList());
                    canPullDown = true;
                    //重新开启上拉更多
                    listView.setCanScrollUp(false);
                    listView.setCanScrollUp(true);
                }else{
                    dataLists.addAll(result.getValue().getList());
                    canScrollUp = true;
                    if (Validators.isEmpty(result.getValue().getList())) {
                        listView.onScrollUpRefreshComplete("");
                        listView.onScrollUpNoMoreData("没有更多数据了");
                    }else{
                        listView.onScrollUpRefreshComplete("上滑更多");
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<DateReportData>() {
            @Override
            public void failCallback(Result<DateReportData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        task.execute(personId , lastTime , ltGtEnum.getValueStr() , filterTime);
    }


    //初始化选择时间器
    private void initSelecWidget(){
        //年
        NumericWheelAdapter yearAdapter = new NumericWheelAdapter(this , 1993 , 2600 , "%02d");
        yearAdapter.setLabel(" ");
        yearAdapter.setTextSize(22);
        yearWl.setViewAdapter(yearAdapter);
        yearWl.setCyclic(false);
        //设置监听
        yearWl.addScrollingListener(onWheelScrollListener);

        //月
        NumericWheelAdapter monthAdapter = new NumericWheelAdapter(this , 1 , 12 ,"%02d");
        monthAdapter.setLabel(" ");
        monthAdapter.setTextSize(22);
        monthWl.setViewAdapter(monthAdapter);
        monthWl.setCyclic(true);
        //设置监听
        monthWl.addScrollingListener(onWheelScrollListener);

        //设置显示行数
        yearWl.setVisibleItems(7);
        monthWl.setVisibleItems(7);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        yearWl.setCurrentItem(calendar.get(Calendar.YEAR) - 1993);
        monthWl.setCurrentItem(calendar.get(Calendar.MONTH));

        //确定
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
                String str = formatter.format(curDate);
                currentMonthTv.setText(str);
                timePickerLayout.setVisibility(View.GONE);
                refreshData();
            }
        });
    }

    //滚轮监听
    OnWheelScrollListener onWheelScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR , yearWl.getCurrentItem() + 1993 );
            calendar.set(Calendar.MONTH , monthWl.getCurrentItem());
            curDate = calendar.getTime();

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.unRegister();
    }

    private void registerReceiver(){
        receiver = new UpdateReceiver() {
            @Override
            public void aliResult() {
                refreshData();
            }
        };
        receiver.register();
    }

    private void refreshData(){
        dataLists.clear();
        adapter.notifyDataSetChanged();

        isPullDownRefresh = true;
        canPullDown = true;
        canScrollUp = true;
        lastTime = "0";
        ltGtEnum = LtGtEnum.LT;
        showDialog = true;
        initDatas();
    }
}