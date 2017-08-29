package com.zjyeshi.dajiujiao.buyer.activity.store;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.xuan.bigapple.lib.utils.IntentUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.location.DGLocationUtils;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.activity.map.SellerDistanceMapActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails.Member;
import com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails.Shop;
import com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails.ShopDetailData;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.SalesFoldView;

import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by lan on 2017/8/27.
 */

public class ShopInfoViews extends LinearLayout {
    private TextView storeNameTv;
    private TextView openTimeTv;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
//    private LinearLayout ho;
//    private AutoVerticalScrollTextView label_group_indicator;
//   private LinearLayout ling;
//    private TextView textView5;
//    private boolean isRunning=true;
//    private int number =0;
    private SalesFoldView salesFoldView;

    private List<SalesListData.Sales> tempList;
    public ShopInfoViews(Context context) {
        super(context);
        inif();
    }
    public ShopInfoViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        inif();
    }
    private void inif(){
        inflate(getContext() , R.layout.view_shop_infos , this);
        storeNameTv = (TextView)findViewById(R.id.textView6);
        openTimeTv = (TextView)findViewById(R.id.textView7);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView2 = (ImageView)findViewById(R.id.imageView5);
        imageView3 = (ImageView)findViewById(R.id.imageView4);
        salesFoldView = (SalesFoldView)findViewById(R.id.salesFoldView);
      //  textView5 = (TextView)findViewById(R.id.textView5);
       // ho=(LinearLayout)findViewById(R.id.hudong);
        //label_group_indicator = (AutoVerticalScrollTextView)findViewById(R.id.label_group_indicator);
       // ling= (LinearLayout) findViewById(R.id.ling);
    }
    /**绑定数据
     *
     * @param data
     */
    public void bindData(ShopDetailData data){
        if (null != data){
            final Shop shop = data.getShop();
            final Member member = data.getMember();
            openTimeTv.setOnClickListener(new View.OnClickListener() {
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
                openTimeTv.setText(shop.getAddress());
                BitmapDisplayConfig config = new BitmapDisplayConfig();
                Bitmap temp = BitmapFactory.decodeResource(App.instance.getResources(), R.drawable.default_tx);
                config.setLoadFailedBitmap(temp);
                config.setLoadingBitmap(temp);
                BPBitmapLoader.getInstance().display(imageView, shop.getPic(), config);
//                if (Validators.isEmpty(shop.getOpenTime()) || Validators.isEmpty(shop.getCloseTime())){
//                    openTimeTv.setText("营业时间:" + "卖家未设置" );
//                }else{
//                    openTimeTv.setText("营业时间:" + shop.getOpenTime() + "-" + shop.getCloseTime());
//                }
//                addressTv.setText(shop.getAddress());
            }
            //聊天
            imageView2.setOnClickListener(new View.OnClickListener() {
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
            imageView3.setOnClickListener(new View.OnClickListener() {
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
        //List<SalesListData.Sales> tempList=data.getActivities();
//        textView5.setText(tempList.size()+"个活动");
        //label_group_indicator.setText();
//        new Thread(){
//            @Override
//            public void run() {
//                while (isRunning){
//                    SystemClock.sleep(3000);
//                    handler.sendEmptyMessage(199);
//                }
//            }
//        }.start();

    }
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 199) {
//               label_group_indicator.next();
//                number++;
//              //  label_group_indicator.setText(strings[number%strings.length]);
//            }
//
//        }
//    };

}
