package com.zjyeshi.dajiujiao.buyer.activity.my.seller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.task.coupon.ModifyCouponsTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.CouponListData;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.coupon.ShopCouponsTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVButtonBox;
import com.zjyeshi.dajiujiao.buyer.widgets.timePicker.DatePickerWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.timePicker.callback.DatePickCallback;

import java.util.Calendar;

/**
 * 优惠券设置
 * Created by wuhk on 2016/9/30.
 */
public class CouponSettingActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.buttonIv)
    private IVButtonBox buttonIv;
    @InjectView(R.id.fullMoneyEt)
    private EditText fullMoneyEt;
    @InjectView(R.id.disMoneyEt)
    private EditText disMoneyEt;
    @InjectView(R.id.startTimeLayout)
    private RelativeLayout startTimeLayout;
    @InjectView(R.id.startTimeTv)
    private TextView startTimeTv;
    @InjectView(R.id.endTimeLayout)
    private RelativeLayout endTimeLayout;
    @InjectView(R.id.endTimeTv)
    private TextView endTimeTv;
    @InjectView(R.id.datePickerWidget)
    private DatePickerWidget datePickerWidget;

    private String couponId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_coupon_setting);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("设置优惠券").configRightText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCoupon();
            }
        });

        loadData();

    }

    /**获取数据
     *
     */
    private void loadData(){
        ShopCouponsTask shopCouponsTask = new ShopCouponsTask(CouponSettingActivity.this);
        shopCouponsTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CouponListData>() {
            @Override
            public void failCallback(Result<CouponListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        shopCouponsTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CouponListData>() {
            @Override
            public void successCallback(Result<CouponListData> result) {
                CouponListData.Coupon coupon = result.getValue().getList().get(0);
                couponId = coupon.getId();
                bindData(coupon);
            }
        });

        shopCouponsTask.execute();
    }

    private void bindData(final CouponListData.Coupon coupon){
        //设置是否直送绑定用户
        if (coupon.getApplicationType() == CouponListData.BIND_CUSTOMER_USE){
            buttonIv.setChecked(true);
        }else {
            buttonIv.setChecked(false);
        }
        //设置满减价格
        if (!Validators.isEmpty(coupon.getFulfilMoney())){
            fullMoneyEt.setText(coupon.getFulfilMoney());
        }

        if (!Validators.isEmpty(coupon.getMinusMoney())){
            disMoneyEt.setText(coupon.getMinusMoney());
        }

        //开始时间
        if (!Validators.isEmpty(coupon.getStartTime())){
            startTimeTv.setText(coupon.getStartTime());

        }else{
            startTimeTv.setText("未设置");
        }


        startTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calender = Calendar.getInstance();
                int year = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH) + 1;
                int day = calender.get(Calendar.DAY_OF_MONTH);

                if (!startTimeTv.getText().equals("未设置")){
                    String[] times = coupon.getStartTime().split("-");
                    year = Integer.parseInt(times[0]);
                    month = Integer.parseInt(times[1]);
                    day = Integer.parseInt(times[2]);
                }
                datePickerWidget.showSelect(year, month , day ,  new DatePickCallback() {
                    @Override
                    public void setTime(String time) {
                        startTimeTv.setText(time);
                    }
                });
            }
        });

        //结束时间
        if (!Validators.isEmpty(coupon.getEndTime())){
            endTimeTv.setText(coupon.getEndTime());
        }else{
            endTimeTv.setText("未设置");
        }


        endTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calender = Calendar.getInstance();
                int year = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH) + 1;
                int day = calender.get(Calendar.DAY_OF_MONTH);
                if (!endTimeTv.getText().equals("未设置")){
                    String[] times = coupon.getEndTime().split("-");
                    year = Integer.parseInt(times[0]);
                    month = Integer.parseInt(times[1]);
                    day = Integer.parseInt(times[2]);
                }
                datePickerWidget.showSelect(year , month , day , new DatePickCallback() {
                    @Override
                    public void setTime(String time) {
                        endTimeTv.setText(time);
                    }
                });
            }
        });
    }

    /**修改优惠券
     *
     */
    private void modifyCoupon(){
        String fulfilMoney = fullMoneyEt.getText().toString();
        String minusMoney = disMoneyEt.getText().toString();
        String startTime = startTimeTv.getText().toString();
        String endTime = endTimeTv.getText().toString();
        String applicationType = "";
        if (buttonIv.isChecked()){
            applicationType = "2";
        }else{
            applicationType = "1";
        }

        if (Validators.isEmpty(fulfilMoney) || Validators.isEmpty(minusMoney)){
            ToastUtil.toast("设置完整满减价格");
            return;
        }else if(Validators.isEmpty(startTime) || Validators.isEmpty(endTime)){
            ToastUtil.toast("请将优惠券有效期设置完整");
            return;
        }

        ModifyCouponsTask modifyCouponsTask = new ModifyCouponsTask(CouponSettingActivity.this);
        modifyCouponsTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        modifyCouponsTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("优惠券设置成功");
                finish();
            }
        });

        modifyCouponsTask.execute(couponId , fulfilMoney , minusMoney , startTime , endTime , applicationType);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent();
        intent.setClass(context , CouponSettingActivity.class);
        context.startActivity(intent);
    }
}