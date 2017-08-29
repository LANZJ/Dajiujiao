package com.zjyeshi.dajiujiao.buyer.activity.seller;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.seller.ChangeShopNameActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.seller.CouponSettingActivity;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ShopModifyEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ModifyShopInfoReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.my.CouponListData;
import com.zjyeshi.dajiujiao.buyer.task.my.ModifyShopInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.seller.GetShopInfoTask;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVButtonBox;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.coupon.CouponSwitchTask;
import com.zjyeshi.dajiujiao.buyer.task.coupon.ShopCouponsTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.seller.ShopManageData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.OnWheelScrollListener;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.WheelView;
import com.zjyeshi.dajiujiao.buyer.widgets.wheelview.adapter.NumericWheelAdapter;

/**
 * 商家管理
 * Created by wuhk on 2015/11/6.
 */
public class ShopManageActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.shopNameLayout)
    private RelativeLayout shopNameLayout;
    @InjectView(R.id.shopNameTv)
    private TextView shopNameTv;//点名
    @InjectView(R.id.timeLayout)
    private RelativeLayout timeLayout;//时间
    @InjectView(R.id.deliverIv)
    private IVButtonBox deliverIv;//是否送货
    @InjectView(R.id.buttonIv)
    private IVButtonBox buttonIv;//是否营业
    @InjectView(R.id.couponIv)
    private IVButtonBox couponIv;//是否开启优惠券
    @InjectView(R.id.hour)
    private WheelView hour;//时
    @InjectView(R.id.minute)
    private WheelView minute;//分
    @InjectView(R.id.nextStep)
    private TextView nextStep;
    @InjectView(R.id.timeLabel)
    private TextView timeLabel;
    @InjectView(R.id.openTimeTv)
    private TextView openTimeTv;
    @InjectView(R.id.closeTimeTv)
    private TextView closeTimeTv;
    @InjectView(R.id.zhiTv)
    private TextView zhiTv;
    @InjectView(R.id.timePickerLayout)
    private RelativeLayout timePickerLayout;
    @InjectView(R.id.remainlayout)
    private View remainlayout;
    @InjectView(R.id.moneyLayout)
    private RelativeLayout moneyLayout;
    @InjectView(R.id.marketLayout)
    private RelativeLayout marketLayout;
    @InjectView(R.id.marketTv)
    private TextView marketTv;
    @InjectView(R.id.moneyTv)
    private TextView moneyTv;
    @InjectView(R.id.couponDesTv)
    private TextView couponDesTv;
    @InjectView(R.id.couponSettingLayout)
    private RelativeLayout couponSettinLayout;
    private String openTime;
    private String closeTime;

    private String couponId = "";

    private int mHour = 18;
    private int mMinute=21 ;

    private int openHour = 0 ;
    private int openMin = 0 ;

    private int closeHour = 0 ;
    private int closeMin = 0 ;

    private ShopManageData shopManageData;
    private String marketCost = "0.00";

    private ModifyShopInfoReceiver modifyShopInfoReceiver;//修改店铺信息广播

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modifyShopInfoReceiver = new ModifyShopInfoReceiver() {
            @Override
            public void doModify(String str, int type) {
                if (type == ShopModifyEnum.MODIFYSHOPNAME.getValue()){
                    shopNameTv.setText(str);
                    shopManageData.setName(str);
                }else if (type == ShopModifyEnum.MODIFYDELIVERYPRICE.getValue()){
                    initTextView(moneyTv , str);
                    shopManageData.setDeliverMinCost(str);
                }else if (type == ShopModifyEnum.MODIFYSELLINGTIME.getValue()){
                    String open = str.split(",")[0];
                    String close = str.split(",")[1];
                    shopManageData.setOpenTime(open);
                    shopManageData.setCloseTime(close);
                }else if (type == ShopModifyEnum.MODIFYMARKETCOST.getValue()){
                    initTextView(marketTv , str);
                    shopManageData.setMarketCost(str);
                    marketCost = str;
                }

                modifyShopInfo();
            }
        };
        modifyShopInfoReceiver.register();
        setContentView(R.layout.seller_layout_shop_manage);
        initWidget();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shopCoupon();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        modifyShopInfoReceiver.unRegister();
    }

    private void initWidget() {
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("商家管理");
        //先设为不能点击，有数据时在设置可点击
        shopNameLayout.setEnabled(false);
        timeLayout.setEnabled(false);
        buttonIv.setEnabled(false);
        setShopInfo();
        shopCoupon();
        //修改店铺名称
        shopNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeShopNameActivity.startActivity(ShopManageActivity.this , ShopModifyEnum.MODIFYSHOPNAME , shopManageData.getName());
            }
        });

        //修改营业时间
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerLayout.setVisibility(View.VISIBLE);
            }
        });
        remainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerLayout.setVisibility(View.GONE);
                timeLabel.setText("设置起始时间");
                nextStep.setText("下一步");
            }
        });

        //是否营业
        buttonIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonIv.isChecked()) {
                    shopManageData.setStatus("1");
                    timeLayout.setVisibility(View.VISIBLE);
                } else {
                    shopManageData.setStatus("2");
                    timeLayout.setVisibility(View.GONE);
                }
                modifyShopInfo();
            }
        });

        //是否送货
        deliverIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deliverIv.isChecked()){
//                    ToastUtil.toast("设置为送货");
                    shopManageData.setDelivery(true);
                    moneyLayout.setVisibility(View.VISIBLE);
                }else{
                    shopManageData.setDelivery(false);
//                    ToastUtil.toast("设置为不送货");
                    moneyLayout.setVisibility(View.GONE);
                }
                modifyShopInfo();
            }
        });

        //修改起送价格
        moneyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeShopNameActivity.startActivity(ShopManageActivity.this ,ShopModifyEnum.MODIFYDELIVERYPRICE , shopManageData.getDeliverMinCost());
            }
        });


        //修改市场支持返还费用
        marketLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeShopNameActivity.startActivity(ShopManageActivity.this , ShopModifyEnum.MODIFYMARKETCOST , marketCost);
            }
        });
    }

    /**获取店铺信息
     *
     */
    private void setShopInfo() {
        GetShopInfoTask getShopInfoTask = new GetShopInfoTask(ShopManageActivity.this);
        getShopInfoTask.setShowProgressDialog(false);
        getShopInfoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<ShopManageData>() {
            @Override
            public void failCallback(Result<ShopManageData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getShopInfoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<ShopManageData>() {
            @Override
            public void successCallback(Result<ShopManageData> result) {
                shopManageData = result.getValue();
                shopNameTv.setText(shopManageData.getName());
                marketCost = ExtraUtil.format(Float.parseFloat(shopManageData.getMarketCost())/100);
                marketTv.setText(marketCost);
                if(Validators.isEmpty(shopManageData.getDeliverMinCost())){
                    initTextView(moneyTv , "0");
                }else{
                    initTextView(moneyTv , shopManageData.getDeliverMinCost());
                }
                openTime = shopManageData.getOpenTime();
                closeTime = shopManageData.getCloseTime();
                initTime();
                initSelecWidget();
                if (shopManageData.getStatus().equals("1")) {
                    buttonIv.setChecked(true);
                } else {
                    buttonIv.setChecked(false);
                }
                deliverIv.setChecked(shopManageData.isDelivery());
                //设为可点击
                shopNameLayout.setEnabled(true);
                timeLayout.setEnabled(true);
                buttonIv.setEnabled(true);

                if (buttonIv.isChecked()) {
                    timeLayout.setVisibility(View.VISIBLE);
                } else {
                    timeLayout.setVisibility(View.GONE);
                }

                if (deliverIv.isChecked()){
                    moneyLayout.setVisibility(View.VISIBLE);
                }else{
                    moneyLayout.setVisibility(View.GONE);
                }

            }
        });

        getShopInfoTask.execute();
    }

    /**初始化选择时间器
     *
     */
    private void initSelecWidget(){
        //小时
        NumericWheelAdapter hourAdapter = new NumericWheelAdapter(this , 0 , 23 , "%02d");
        hourAdapter.setLabel(" ");
        hourAdapter.setTextSize(22);
        hour.setViewAdapter(hourAdapter);
        hour.setCyclic(true);
        //设置监听
        hour.addScrollingListener(onWheelScrollListener);

        //分钟
        NumericWheelAdapter minuteAdapter = new NumericWheelAdapter(this , 0 , 59 ,"%02d");
        minuteAdapter.setLabel(" ");
        minuteAdapter.setTextSize(22);
        minute.setViewAdapter(minuteAdapter);
        minute.setCyclic(true);
        //设置监听
        minute.addScrollingListener(onWheelScrollListener);

        //设置显示行数
        hour.setVisibleItems(7);
        minute.setVisibleItems(7);

        mHour =openHour;
        mMinute = openMin;

        hour.setCurrentItem(mHour);
        minute.setCurrentItem(mMinute);

        //下一步点击事件
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextStep.getText().toString().equals("下一步")){
                    zhiTv.setText("至");
                    openTimeTv.setText(String.format("%02d" , mHour)+":"+String.format("%02d" , mMinute));
                    mHour =closeHour;
                    mMinute = closeMin;
                    hour.setCurrentItem(mHour);
                    minute.setCurrentItem(mMinute);
                    timeLabel.setText("设置结束时间");
                    nextStep.setText("完成");
                }else if(nextStep.getText().toString().equals("完成")){
                    closeTimeTv.setText(String.format("%02d", mHour)+":"+String.format("%02d", mMinute));
                    timePickerLayout.setVisibility(View.GONE);
                    timeLabel.setText("设置起始时间");
                    nextStep.setText("下一步");
                    String time  = openTimeTv.getText().toString() + "," + closeTimeTv.getText().toString();
                    ModifyShopInfoReceiver.notifyReceiver(time, ShopModifyEnum.MODIFYSELLINGTIME.getValue());
                }
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
            mHour = hour.getCurrentItem(); //时
            mMinute = minute.getCurrentItem(); //分
        }
    };

    /**初始化时间
     *
     */
    private void initTime(){
        if (Validators.isEmpty(openTime) || Validators.isEmpty(closeTime)){
            openTime = "";
            closeTime = "" ;
            openTimeTv.setText(openTime);
            closeTimeTv.setText(closeTime);

            zhiTv.setText("设置时间");

            openHour = 0;
            openMin = 0 ;

            closeHour = 0;
            closeMin = 0;
        }else{
            openTimeTv.setText(openTime);
            closeTimeTv.setText(closeTime);
            if (openTime.contains(":")){
                openHour = Integer.parseInt(openTime.split(":")[0]);
                openMin = Integer.parseInt(openTime.split(":")[1]);
            }

            if (closeTime.contains(":")){
                closeHour = Integer.parseInt(closeTime.split(":")[0]);
                closeMin = Integer.parseInt(closeTime.split(":")[1]);
            }
        }
    }

    /**修改店铺信息
     *
     */
    private void modifyShopInfo(){
        ModifyShopInfoTask modifyShopInfoTask = new ModifyShopInfoTask(ShopManageActivity.this);
        modifyShopInfoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        modifyShopInfoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("设置成功");
            }
        });

        modifyShopInfoTask.execute(shopManageData);
    }

    /**是否使用优惠券
     *
     */
    private void couponSwitch(final String couponId , boolean use){

        CouponSwitchTask couponSwitchTask = new CouponSwitchTask(ShopManageActivity.this);
        couponSwitchTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        couponSwitchTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                if (Validators.isEmpty(couponId)){
                    shopCoupon();
                }
            }
        });

        couponSwitchTask.execute(couponId , String.valueOf(use));
    }

    /**获取优惠券
     *
     */
    private void shopCoupon(){
        ShopCouponsTask shopCouponsTask = new ShopCouponsTask(ShopManageActivity.this);
        shopCouponsTask.setShowProgressDialog(false);
        shopCouponsTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CouponListData>() {
            @Override
            public void failCallback(Result<CouponListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        shopCouponsTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CouponListData>() {
            @Override
            public void successCallback(Result<CouponListData> result) {
                if (Validators.isEmpty(result.getValue().getList())) {
                    couponIv.setChecked(false);
                    couponSettinLayout.setVisibility(View.GONE);
                    couponDesTv.setText("去设置");
                } else {
                    CouponListData.Coupon coupon = result.getValue().getList().get(0);
                    if (coupon.isStatus()) {
                        couponIv.setChecked(true);
                        couponSettinLayout.setVisibility(View.VISIBLE);
                    } else {
                        couponIv.setChecked(false);
                        couponSettinLayout.setVisibility(View.GONE);
                    }
                    couponId = coupon.getId();
                    if (Validators.isEmpty(coupon.getFulfilMoney()) || Validators.isEmpty(coupon.getMinusMoney())) {
                        couponDesTv.setText("去设置");
                    } else {
                        couponDesTv.setText("满" + coupon.getFulfilMoney() + "元减" + coupon.getMinusMoney() + "元");
                    }
                }
                //是否设置优惠券
                couponIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (couponIv.isChecked()) {
                            couponSettinLayout.setVisibility(View.VISIBLE);
                        } else {
                            couponSettinLayout.setVisibility(View.GONE);
                        }
                        couponSwitch(couponId, couponIv.isChecked());
                    }
                });
                //设置优惠券
                couponSettinLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CouponSettingActivity.startActivity(ShopManageActivity.this);
                    }
                });
            }
        });

        shopCouponsTask.execute();
    }
}
