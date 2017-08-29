package com.zjyeshi.dajiujiao.buyer.widgets.store;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xuan.bigapple.lib.utils.IntentUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.location.DGLocationUtils;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.map.SellerDistanceMapActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails.Member;
import com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails.Shop;
import com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails.ShopDetailData;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.SalesFoldView;

import io.rong.imkit.RongIM;

/**
 * 店铺信息
 * Created by wuhk on 2016/6/29.
 */
public class ShopInfoView extends LinearLayout {
    private TextView storeNameTv;
    private TextView openTimeTv;
    private TextView addressTv;
    private TextView distanceTv;
    private RelativeLayout lookAddLayout;
    private LinearLayout messageLayout;
    private LinearLayout chatLayout;
    private LinearLayout callLayout;
    private SalesFoldView salesFoldView;

    public ShopInfoView(Context context) {
        super(context);
        init();
    }

    public ShopInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        inflate(getContext() , R.layout.view_shop_info , this);
        storeNameTv = (TextView)findViewById(R.id.storeNameTv);
        openTimeTv = (TextView)findViewById(R.id.openTimeTv);
        addressTv = (TextView)findViewById(R.id.addressTv);
        distanceTv = (TextView)findViewById(R.id.distanceTv);
        lookAddLayout = (RelativeLayout)findViewById(R.id.lookAddLayout);
        messageLayout = (LinearLayout)findViewById(R.id.messageLayout);
        chatLayout = (LinearLayout)findViewById(R.id.chatLayout);
        callLayout = (LinearLayout)findViewById(R.id.callLayout);
        salesFoldView = (SalesFoldView)findViewById(R.id.salesFoldView);
    }

    /**绑定数据
     *
     * @param data
     */
    public void bindData(ShopDetailData data){
        if (null != data){
            final Shop shop = data.getShop();
            final Member member = data.getMember();
            lookAddLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Validators.isEmpty(shop.getLatE5()) || Validators.isEmpty(shop.getLngE5())){
                        ToastUtil.toast("当前卖家未设置位置信息");
                    }else{
                        DGLocationUtils.init(getContext(), new BDLocationListener() {
                            @Override
                            public void onReceiveLocation(BDLocation bdLocation) {
                                String lat = String.valueOf((int) (bdLocation.getLatitude() * 100000));
                                String lng = String.valueOf((int) (bdLocation.getLongitude() * 100000));

                                LogUtil.debug("定位结果:" + "lat" + lat + "-----" + "lng" + lng);

                                Intent intent = new Intent();
                                intent.putExtra(SellerDistanceMapActivity.PARAM_MAP_SHOP_NAME, shop.getName());
                                intent.putExtra(SellerDistanceMapActivity.PARAM_MAP_ADDRESS, shop.getAddress());
                                intent.putExtra(SellerDistanceMapActivity.PARAM_MAP_MY_LAT, lat);
                                intent.putExtra(SellerDistanceMapActivity.PARAM_MAP_MY_LNG, lng);
                                intent.putExtra(SellerDistanceMapActivity.PARAM_MAP_LAT, shop.getLatE5());
                                intent.putExtra(SellerDistanceMapActivity.PARAM_MAP_LNG, shop.getLngE5());
                                intent.setClass(getContext(), SellerDistanceMapActivity.class);
                                getContext().startActivity(intent);
                                DGLocationUtils.stop();
                            }
                        });

                        DGLocationUtils.start();

                    }
                }
            });

            if (null != shop){
                storeNameTv.setText(shop.getName());
                distanceTv.setText(shop.getDistance());

                if (Validators.isEmpty(shop.getOpenTime()) || Validators.isEmpty(shop.getCloseTime())){
                    openTimeTv.setText("营业时间:" + "卖家未设置" );
                }else{
                    openTimeTv.setText("营业时间:" + shop.getOpenTime() + "-" + shop.getCloseTime());
                }
                addressTv.setText(shop.getAddress());
            }
            //短信
            messageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != member){
                        IntentUtils.sendSmsByPhone(getContext() , member.getPhone());
                    }else{
                        ToastUtil.toast("请稍后，商家信息正在加载中");
                    }
                }
            });
            //聊天
            chatLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginedUser.checkLogined()){
                        if (null != member){
                          //  ChatManager.getInstance().startConversion(getContext() , member.getId());
                            RongIM.getInstance().startPrivateChat(getContext(), member.getId(),member.getName());
                        }else{
                            ToastUtil.toast("请稍后，商家信息正在加载中");
                        }
                    }
                }
            });
            //电话
            callLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != member){
                        IntentUtils.callByPhone(getContext() , member.getPhone());
                    }else{
                        ToastUtil.toast("请稍后，商家信息正在加载中");
                    }
                }
            });
        }

            salesFoldView.bindData(data.getActivities());

    }
}
