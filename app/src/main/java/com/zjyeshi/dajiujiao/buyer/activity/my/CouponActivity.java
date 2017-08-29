package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.adapter.DGNoDataView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.coupon.GetMyOrderCouponListTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.CouponListData;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;
import com.zjyeshi.dajiujiao.buyer.views.pay.PaySelectTypeView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.my.CouponAdapter;
import com.zjyeshi.dajiujiao.buyer.task.coupon.GetMyCouponListTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.coupon.CouponEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券
 * Created by wuhk on 2016/9/21.
 */
public class CouponActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.couponCheckIv)
    private IVCheckBox couponCheckIv;
    @InjectView(R.id.selectLayout)
    private RelativeLayout selectLayout;
    @InjectView(R.id.noDataView)
    private DGNoDataView noDataView;
    @InjectView(R.id.listView)
    private ListView listView;

    private List<CouponEntity> dataList = new ArrayList<CouponEntity>();
    private CouponAdapter couponAdapter;

    public static final String COUPON = "coupon";
    public static final String NO_USE_COUPON = "no_use_coupon";

    private boolean isSelect;
    private String orderIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_coupon);
        initWidgets();
    }

    private void initWidgets(){
        isSelect = getIntent().getBooleanExtra(PaySelectTypeView.SELECT_COUPON , false);
        orderIds = getIntent().getStringExtra(PaySelectTypeView.ORDERIDS);

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("我的酒券");

        //优惠券使用选择
        couponCheckIv.setChecked(false);
        couponCheckIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (couponCheckIv.isChecked()){
                    Intent intent = getIntent();
                    intent.putExtra(COUPON , NO_USE_COUPON);
                    setResult(Activity.RESULT_OK , intent);
                    finish();
                }else{

                }
            }
        });

        if (isSelect){
            selectLayout.setVisibility(View.VISIBLE);
        }else{
            selectLayout.setVisibility(View.GONE);
        }

        couponAdapter = new CouponAdapter(CouponActivity.this , dataList);
        listView.setAdapter(couponAdapter);
        if (isSelect){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CouponEntity data = dataList.get(position);
                    Intent intent = getIntent();
                    intent.putExtra(COUPON , JSON.toJSONString(data));
                    setResult(Activity.RESULT_OK , intent);
                    finish();
                }
            });
        }

        loadData();
    }

    /**下载数据
     *
     */
    private void loadData(){
        if (isSelect){
            //可用优惠券
            GetMyOrderCouponListTask getMyOrderCouponListTask = new GetMyOrderCouponListTask(CouponActivity.this);
            getMyOrderCouponListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CouponListData>() {
                @Override
                public void failCallback(Result<CouponListData> result) {
                    ToastUtil.toast(result.getMessage());
                }
            });

            getMyOrderCouponListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CouponListData>() {
                @Override
                public void successCallback(Result<CouponListData> result) {
                    noDataView.showIfEmpty(result.getValue().getList());
                    transformData(result.getValue().getList() , false);
                }
            });

            getMyOrderCouponListTask.execute(orderIds);

        }else{
            //我的全部优惠券
            GetMyCouponListTask getMyCouponListTask = new GetMyCouponListTask(CouponActivity.this);
            getMyCouponListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CouponListData>() {
                @Override
                public void failCallback(Result<CouponListData> result) {
                    ToastUtil.toast(result.getMessage());
                }
            });

            getMyCouponListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CouponListData>() {
                @Override
                public void successCallback(Result<CouponListData> result) {
                    transformData(result.getValue().getList() , true);

                }
            });

            getMyCouponListTask.execute();
        }
    }

    private void transformData(List<CouponListData.Coupon> tempList , boolean isAll){
        dataList.clear();
        for(CouponListData.Coupon  coupon : tempList){
            CouponEntity couponEntity = new CouponEntity();
            couponEntity.setId(coupon.getId());
            couponEntity.setFullMoney(coupon.getFulfilMoney());
            couponEntity.setDisCountMoney(coupon.getMinusMoney());
            couponEntity.setStartTime(coupon.getStartTime());
            couponEntity.setEndTime(coupon.getEndTime());
            if (isAll){
                couponEntity.setEffective(true);
            }else{
                couponEntity.setEffective(coupon.isWhetherUse());
            }
            couponEntity.setDescription(coupon.getEnvironmentName());
            dataList.add(couponEntity);
        }

        couponAdapter.notifyDataSetChanged();
    }

    /**启动该Activity
     *
     * @param context
     */
    public static void startActivity(Context context){
        Intent intent = new Intent();
        intent.setClass(context , CouponActivity.class);
        context.startActivity(intent);
    }
}
