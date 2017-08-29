package com.zjyeshi.dajiujiao.buyer.widgets.timePicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.zjyeshi.dajiujiao.buyer.widgets.timePicker.callback.DatePickCallback;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.OnWheelScrollListener;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.WheelView;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.adapter.NumericWheelAdapter;
import com.zjyeshi.dajiujiao.R;

/**
 * 年月日时间选择器
 * Created by wuhk on 2016/9/30.
 */
public class DatePickerWidget extends LinearLayout {
    @InjectView(R.id.yearWl)
    private WheelView yearWl;
    @InjectView(R.id.monthWl)
    private WheelView monthWl;
    @InjectView(R.id.dayWl)
    private WheelView dayWl;
    @InjectView(R.id.sureTv)
    private TextView sureTv;
    @InjectView(R.id.remainlayout)
    private View remainLayout;
    private DatePickCallback datePickCallback;

    private int mYear;
    private int mMonth;
    private int mDay;

    public DatePickerWidget(Context context) {
        super(context);
        init();
    }

    public DatePickerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.view_date_pick, this);
        ViewUtils.inject(this, this);
        remainLayout.setClickable(true);
        remainLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
            }
        });

        sureTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = mYear + "-" + String.format("%02d", mMonth) + "-" + String.format("%02d", mDay);
                if (null != datePickCallback) {
                    datePickCallback.setTime(time);
                }
                setVisibility(GONE);
            }
        });
    }

    /**
     * 显示控件
     *
     * @param year
     * @param month
     * @param day
     * @param datePickCallback
     */
    public void showSelect(int year, int month, int day, DatePickCallback datePickCallback) {
        mYear = year;
        mMonth = month;
        mDay = day;
        this.datePickCallback = datePickCallback;
        initTimePicker();
        setVisibility(VISIBLE);
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

        //设置显示行数
        yearWl.setVisibleItems(7);
        monthWl.setVisibleItems(7);
        dayWl.setVisibleItems(7);

        yearWl.setCurrentItem(mYear - 1993);
        monthWl.setCurrentItem(mMonth - 1);
        //日
        setDay();
        dayWl.setCurrentItem(mDay - 1);
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
