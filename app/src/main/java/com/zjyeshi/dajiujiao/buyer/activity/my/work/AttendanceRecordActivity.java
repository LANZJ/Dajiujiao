package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.AttendanceRecordAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.AttendanceRecord;
import com.zjyeshi.dajiujiao.buyer.task.work.TimeCardRecordTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.TimeCardRecordListData;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
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
 * 考勤记录
 *
 * Created by zhum on 2016/6/13.
 */
public class AttendanceRecordActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.firstTv)
    private TextView firstTv;//未打卡
    @InjectView(R.id.secondTv)
    private TextView secondTv;//请假
    @InjectView(R.id.thirdTv)
    private TextView thirdTv;//出差
    @InjectView(R.id.fourthTv)
    private TextView fourthTv;//早退

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

    @InjectView(R.id.listView)
    private ListView listView;//考勤列表
    private AttendanceRecordAdapter adapter;
    private List<AttendanceRecord> dataLists;
    private long time;
    private Date curDate;

    public static boolean isReload = false;
    public static String memberId = "";
    private boolean bossView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_attendance_record);
        initWidgets();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isReload){
            initDatas(memberId);
        }
    }

    private void initWidgets(){
        memberId = "";
        bossView = getIntent().getBooleanExtra("bossView" , false);
        if (bossView){
            memberId = getIntent().getStringExtra("memberId");
        }
        titleLayout.configTitle("考勤记录").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //是否显示选择员工
//        if (AuthUtil.isLeaveSelPersonShow()){
//            titleLayout.configRightText("选择员工", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(AttendanceRecordActivity.this,ChooseEmployeeActivity.class);
//                    intent.putExtra("isKq" , true);
//                    startActivity(intent);
//                }
//            });
//        }
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
                initDatas(memberId);
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

                    initDatas(memberId);
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

        dataLists = new ArrayList<>();
        adapter = new AttendanceRecordAdapter(AttendanceRecordActivity.this,dataLists);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AttendanceRecord attendanceRecord = dataLists.get(position);
                Intent intent = new Intent(AttendanceRecordActivity.this,CardDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("record",attendanceRecord);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        initDatas(memberId);
    }

    private void initDatas(String memberId){
        dataLists.clear();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String time = formatter.format(curDate);
        TimeCardRecordTask timeCardRecordTask = new TimeCardRecordTask(AttendanceRecordActivity.this);
        timeCardRecordTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<TimeCardRecordListData>() {
            @Override
            public void failCallback(Result<TimeCardRecordListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        timeCardRecordTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<TimeCardRecordListData>() {
            @Override
            public void successCallback(Result<TimeCardRecordListData> result) {
                isReload = false;
                transformData(result.getValue().getList());
                firstTv.setText("已打卡：" + result.getValue().getPunchDays() + "次");
                secondTv.setText("早退：" + result.getValue().getLeaveEarlyDays() + "次");
                thirdTv.setText("迟到：" + result.getValue().getLateDays() + "次");
                fourthTv.setText("请假：" + result.getValue().getLeaveDays() + "天");

                adapter.notifyDataSetChanged();
            }
        });
        if (Validators.isEmpty(memberId)){
            timeCardRecordTask.execute(time);
        }else{
            timeCardRecordTask.execute(memberId , time);
        }
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
                initDatas(memberId);
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


    //转换成需要的数据
    private void transformData(List<TimeCardRecordListData.CardRecord> recordList){
        for (int i = 31 ;i >= 0  ;i --){
            List<TimeCardRecordListData.CardRecord> tempList = new ArrayList<TimeCardRecordListData.CardRecord>();
            for (int j = 0 ; j < recordList.size(); j++){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(recordList.get(j).getCreationTime()));
                if (calendar.get(Calendar.DAY_OF_MONTH) == i){
                    tempList.add(recordList.get(j));
                }
            }

            if (!Validators.isEmpty(tempList)){
                if (tempList.size() == 1){
                    TimeCardRecordListData.CardRecord cardRecord = tempList.get(0);
                    AttendanceRecord attendanceRecord = new AttendanceRecord();
                    attendanceRecord.setDate(i + "日");
                    attendanceRecord.setUpArea(cardRecord.getAddress());
                    attendanceRecord.setOnList(ExtraUtil.array2List(cardRecord.getPics()));
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    attendanceRecord.setUpTime(formatter.format(cardRecord.getCreationTime()));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    attendanceRecord.setSpeDate(dateFormat.format(cardRecord.getCreationTime()));
                    attendanceRecord.setSingle(false);
                    attendanceRecord.setUpRemark(cardRecord.getRemark());

                    attendanceRecord.setDownArea("未打卡");

                    dataLists.add(attendanceRecord);
                }else if (tempList.size() == 2){
                    TimeCardRecordListData.CardRecord cardRecord1 = new TimeCardRecordListData.CardRecord();//下班
                    TimeCardRecordListData.CardRecord cardRecord2 = new TimeCardRecordListData.CardRecord();//上班
                    if (tempList.get(0).getCreationTime() > tempList.get(1).getCreationTime()){
                        cardRecord1 = tempList.get(0);
                        cardRecord2 = tempList.get(1);
                    }else{
                        cardRecord1 = tempList.get(1);
                        cardRecord2 = tempList.get(0);
                    }

                    AttendanceRecord attendanceRecord = new AttendanceRecord();
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    attendanceRecord.setUpTime(formatter.format(new Date(cardRecord2.getCreationTime())));
                    attendanceRecord.setUpArea(cardRecord2.getAddress());
                    attendanceRecord.setOnList(ExtraUtil.array2List(cardRecord2.getPics()));
                    attendanceRecord.setUpRemark(cardRecord2.getRemark());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    attendanceRecord.setSpeDate(dateFormat.format(cardRecord2.getCreationTime()));
                    attendanceRecord.setDate(i + "日");
                    attendanceRecord.setDownArea(cardRecord1.getAddress());
                    attendanceRecord.setOffList(ExtraUtil.array2List(cardRecord1.getPics()));
                    attendanceRecord.setDownTime(formatter.format(new Date(cardRecord1.getCreationTime())));
                    attendanceRecord.setDownmRemark(cardRecord1.getRemark());
                    attendanceRecord.setSingle(false);
                    attendanceRecord.setLng(cardRecord1.getLngE5());
                    attendanceRecord.setLat(cardRecord1.getLatE5());
                    attendanceRecord.setLngs(cardRecord2.getLngE5());
                    attendanceRecord.setLats(cardRecord2.getLatE5());
                    dataLists.add(attendanceRecord);
                }
            }
        }
    }

}