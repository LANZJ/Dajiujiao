package com.zjyeshi.dajiujiao.buyer.widgets.timePicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.widgets.timePicker.callback.EndTimeCallbck;
import com.zjyeshi.dajiujiao.buyer.widgets.timePicker.callback.OpenTimeCallback;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.OnWheelScrollListener;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.WheelView;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.adapter.NumericWheelAdapter;

/**
 * 年月日时分时间选择器
 * Created by wuhk on 2016/6/21.
 */
public class TimePickerWidget extends LinearLayout {
    @InjectView(R.id.timePickerLayout)
    private RelativeLayout timePickerLayout;
    @InjectView(R.id.yearWl)
    private WheelView yearWl;
    @InjectView(R.id.monthWl)
    private WheelView monthWl;
    @InjectView(R.id.dayWl)
    private WheelView dayWl;
    @InjectView(R.id.hourWl)
    private WheelView hourWl;
    @InjectView(R.id.minWl)
    private WheelView minWl;
    @InjectView(R.id.sureTv)
    private TextView sureTv;
    @InjectView(R.id.remainLayout)
    private View remainLayout;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mhour;
    private int mMin;
    private boolean isOpen;

    public TimePickerWidget(Context context) {
        super(context);
        init();
    }

    public TimePickerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.widget_timepicker, this);
        ViewUtils.inject(this, this);
        remainLayout.setClickable(true);
        remainLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerLayout.setVisibility(GONE);
            }
        });
    }

    public void showSelect(int year, int month, int day, int hour, int min, boolean isOpen) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mhour = hour;
        mMin = min;
        this.isOpen = isOpen;
        initTimePicker();
        timePickerLayout.setVisibility(VISIBLE);
    }

    public void setTimeClick(final OpenTimeCallback openTimeCallback, final EndTimeCallbck endTimeCallbck) {

        sureTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = mYear + "-" + String.format("%02d", mMonth) + "-" + String.format("%02d", mDay) + " "
                        + String.format("%02d", mhour) + ":" + String.format("%02d", mMin);
                if (isOpen) {
                    openTimeCallback.openTime(time);
                } else {
                    endTimeCallbck.endTime(time);
                }
                timePickerLayout.setVisibility(GONE);
            }
        });
    }

    //初始化时间选择
    private void initTimePicker() {
        //年
        NumericWheelAdapter yearAdapter = new NumericWheelAdapter(getContext(), 1993, 2600, "%02d");
        yearAdapter.setLabel(" ");
        yearAdapter.setTextSize(20);
        yearWl.setViewAdapter(yearAdapter);
        yearWl.setCyclic(false);
        yearWl.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mYear = yearWl.getCurrentItem() + 1993;
                dayWl.setCurrentItem(0);
                setDay();
            }
        });

        //月
        NumericWheelAdapter monthAdapter = new NumericWheelAdapter(getContext(), 1, 12, "%02d");
        monthAdapter.setLabel(" ");
        monthAdapter.setTextSize(20);
        monthWl.setViewAdapter(monthAdapter);
        monthWl.setCyclic(false);
        monthWl.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mMonth = monthWl.getCurrentItem() + 1;
                dayWl.setCurrentItem(0);
                setDay();
            }
        });

        //小时
        NumericWheelAdapter hourAdapter = new NumericWheelAdapter(getContext(), 0, 23, "%02d");
        hourAdapter.setLabel(" ");
        hourAdapter.setTextSize(20);
        hourWl.setViewAdapter(hourAdapter);
        hourWl.setCyclic(true);
        //设置监听
        hourWl.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mhour = hourWl.getCurrentItem();
            }
        });

        //分钟
        NumericWheelAdapter minuteAdapter = new NumericWheelAdapter(getContext(), 0, 59, "%02d");
        minuteAdapter.setLabel(" ");
        minuteAdapter.setTextSize(20);
        minWl.setViewAdapter(minuteAdapter);
        minWl.setCyclic(true);
        //设置监听
        minWl.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mMin = minWl.getCurrentItem();
            }
        });

        //设置显示行数
        yearWl.setVisibleItems(7);
        monthWl.setVisibleItems(7);
        dayWl.setVisibleItems(7);
        hourWl.setVisibleItems(7);
        minWl.setVisibleItems(7);

        yearWl.setCurrentItem(mYear - 1993);
        monthWl.setCurrentItem(mMonth - 1);
        //日
        setDay();
        dayWl.setCurrentItem(mDay - 1);
        hourWl.setCurrentItem(mhour);
        minWl.setCurrentItem(mMin);

    }

    //设置日期
    private void setDay() {
        int maxDay = DateUtils.getMaxDayOfMonth(yearWl.getCurrentItem() + 1993, monthWl.getCurrentItem());
        NumericWheelAdapter dayAdapter = new NumericWheelAdapter(getContext(), 1, maxDay, "%02d");
        dayAdapter.setLabel(" ");
        dayAdapter.setTextSize(20);
        dayWl.setViewAdapter(dayAdapter);
        dayWl.setCyclic(false);
        dayWl.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                mDay = dayWl.getCurrentItem() + 1;
            }
        });
    }


}
