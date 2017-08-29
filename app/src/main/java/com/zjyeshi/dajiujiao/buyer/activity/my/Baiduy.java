package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * Created by lan on 2017/7/24.
 */
public class Baiduy extends BaseActivity{
   private MapView mMapView = null;
    private BaiduMap baiduMap;
    // 当前经纬度
   // private GeoPoint myPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baiduy);
        init();
       // deleteLogo();
        //initBaiduMapAndCenterPoint();

    }

    private void init() {

        MapView mapView = (MapView) findViewById(R.id.mapView);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String log = bundle.getString("log");
        String lat = bundle.getString("lat");
//        int logs=Integer.parseInt(log);
//        int late=Integer.parseInt(lat);
        float logs=Float.parseFloat(log);
        float late=Float.parseFloat(lat);
        baiduMap = mapView.getMap();
        LatLng cenpt =  new LatLng(logs/10000,late/10000);
        ToastUtil.toast("经纬度已复制粘贴板");
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData=ClipData.newPlainText("Label",log+" "+lat);
        cm.setPrimaryClip(mClipData);

//        MapStatus mMapStatus = new MapStatus.Builder()
//                .target(cenpt)
//                .zoom(17)
//                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(cenpt);
//        baiduMap.animateMapStatus(mMapStatusUpdate);
//        //改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);
        MarkerOptions ooA = new MarkerOptions().position(cenpt).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_address))
                .zIndex(9).draggable(true);
        Marker marker = (Marker) (baiduMap.addOverlay(ooA));


    }

    /**删除百度地图Logo
     *
     */
    private void deleteLogo(){
        // 隐藏缩放控件
        int childCount = mMapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                zoom = child;
                break;
            }
        }
        zoom.setVisibility(View.GONE);
        // 隐藏比例尺控件
        int count = mMapView.getChildCount();
        View scale = null;
        for (int i = 0; i < count; i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                scale = child;
                break;
            }
        }
        scale.setVisibility(View.GONE);
        // 隐藏指南针
        UiSettings mUiSettings = baiduMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);
        // 删除百度地图logo
        mMapView.removeViewAt(1);
    }
}