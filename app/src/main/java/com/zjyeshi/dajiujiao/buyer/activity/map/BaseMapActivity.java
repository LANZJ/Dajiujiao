package com.zjyeshi.dajiujiao.buyer.activity.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.xuan.bigapple.lib.ioc.app.BPActivity;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.location.DGPoint;
import com.xuan.bigdog.lib.location.activity.entity.RoutePlanTypeEnum;
import com.xuan.bigdog.lib.location.activity.interfaces.DGLinkPointInterface;
import com.xuan.bigdog.lib.location.activity.interfaces.DGMarkLocationInterface;
import com.xuan.bigdog.lib.location.activity.interfaces.DGRoutePlanInterface;
import com.xuan.bigdog.lib.location.activity.listener.DGOnGetRoutePlanResultListener;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.Shop;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.store.ShopDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图基类
 * Created by wuhk on 2015/11/20.
 */
public class BaseMapActivity  extends BPActivity implements DGMarkLocationInterface , DGRoutePlanInterface ,DGLinkPointInterface,GotoShopInterface{
    private MapView mMapView;// 地图View
    private BaiduMap mBaiduMap;// 地图map

    private InfoWindow mInfoWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置展示中心点
        initBaiduMapAndCenterPoint();
        // 设置View到界面
        initContentView();
        //删除百度地图Logo
        deleteLogo();
    }

    /**初始化view
     *
     */
    private void initContentView() {
        View titleView = configTitleView();
        View bottomView = configBottomView();
        if (null != titleView && null == bottomView) {
            setViewOnlyTitle(titleView);
        }else if(null == titleView && null != bottomView){
            setViewOnlyBottom(bottomView);
        }else if(null != titleView && null != bottomView){
            setViewBoth(titleView , bottomView);
        } else {
            setContentView(mMapView);
        }
    }
    /** 设置展示中心点 */
    private void initBaiduMapAndCenterPoint() {
        DGPoint centerPoint = configCenterPoint();
        if (null != centerPoint) {
            LatLng p = new LatLng(centerPoint.lat, centerPoint.lng);
            mMapView = new MapView(this,
                    new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                            .target(p).zoom(17).build()));
        } else {
            mMapView = new MapView(this, new BaiduMapOptions());
        }
        mBaiduMap = mMapView.getMap();
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
        UiSettings mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);
        // 删除百度地图logo
        mMapView.removeViewAt(1);
    }

    /**只设置头布局
     *
     * @param titleView
     */
    private void setViewOnlyTitle(View titleView){
        RelativeLayout ll = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.layout_base_map, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        titleView.setLayoutParams(lp);
        ll.addView(mMapView);
        ll.addView(titleView);
        setContentView(ll);
    }

    /**只设置底部布局
     *
     * @param bottomView
     */
    private void setViewOnlyBottom(View bottomView){
        RelativeLayout ll = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.layout_base_map, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomView.setLayoutParams(lp);
        ll.addView(mMapView);
        ll.addView(bottomView);
        setContentView(ll);
    }

    /**上下都设置布局
     *
     * @param titleView
     * @param bottomView
     */
    private void setViewBoth(View titleView , View bottomView){
        RelativeLayout ll = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.layout_base_map, null);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomView.setLayoutParams(lp1);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        titleView.setLayoutParams(lp);
        titleView.setLayoutParams(lp);
        ll.addView(mMapView);
        ll.addView(titleView);
        ll.addView(bottomView);
        setContentView(ll);
    }



    // //////////////////START MarkLocationInterface START/////////
    @Override
    public void markLocation(List<DGPoint> locationPointList,
                             List<BitmapDescriptor> locationPointBitmapList) {
        if (Validators.isEmpty(locationPointBitmapList)) {
            // 如果标记图片是空，设置默认图片
            locationPointBitmapList = new ArrayList<BitmapDescriptor>();
            for (int i = 0, n = locationPointList.size(); i < n; i++) {
                locationPointBitmapList.add(DEFAULT_LOCATION_BITMAP);
            }
        }

        if (!Validators.isEmpty(locationPointList)) {
            for (int i = 0, n = locationPointList.size(); i < n; i++) {
                // 构建MarkerOption，用于在地图上添加Marker
                DGPoint p = locationPointList.get(i);

                final MarkerOptions option = new MarkerOptions();
                option.position(new LatLng(p.lat, p.lng));
                option.icon(locationPointBitmapList.get(i));
                // 在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);
            }
        }
    }

    @Override
    public void markLocation(List<DGPoint> locationPointList) {
        markLocation(locationPointList, null);
    }

    // //////////////////END MarkLocationInterface END/////////

    // //////////////////START GotoShopInterface START/////////

    @Override
    public void markLocationToShop(List<DGPoint> locationPointList, List<BitmapDescriptor> locationPointBitmapList, List<Shop> shopList) {
        if (!Validators.isEmpty(locationPointList)) {
            for (int i = 0, n = locationPointList.size(); i < n; i++) {
                // 构建MarkerOption，用于在地图上添加Marker
                DGPoint p = locationPointList.get(i);
                final Shop shop = shopList.get(i);
                final MarkerOptions option = new MarkerOptions();
                option.position(new LatLng(p.lat, p.lng));
                option.icon(locationPointBitmapList.get(i));
                // 在地图上添加Marker，并显示商店信息,跳转
                final Marker clickMarker = (Marker) (mBaiduMap.addOverlay(option));
                mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        if (marker == clickMarker){
                            Button button = new Button(getApplicationContext());
                            button.setText(shop.getName() + "\n" + shop.getDistance() + "m" );

                            button.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setClass(BaseMapActivity.this , ShopDetailActivity.class);
                                    intent.putExtra(ShopDetailActivity.PARAM_SHOPID, shop.getId());
                                    startActivity(intent);
                                    mBaiduMap.hideInfoWindow();
                                }
                            });
                            LatLng ll = marker.getPosition();
                            mInfoWindow = new InfoWindow(button, ll, -47);
                            mBaiduMap.showInfoWindow(mInfoWindow);
                        }
                        return true;
                    }
                });
                //点击地图取消店铺提示
                mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mBaiduMap.hideInfoWindow();
                    }

                    @Override
                    public boolean onMapPoiClick(MapPoi mapPoi) {
                        return false;
                    }
                });
            }
        }
    }
    // //////////////////END GotoShopInterface END/////////

    // //////////////////START DGRoutePlanInterface START/////////
    @Override
    public void doRoutePlan(DGPoint fromPoint, DGPoint toPoint,
                            RoutePlanTypeEnum routePlanTypeEnum, String city) {
        RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(new DGOnGetRoutePlanResultListener(
                this, mBaiduMap));

        PlanNode stNode = PlanNode.withLocation(new LatLng(fromPoint.lat,
                fromPoint.lng));
        PlanNode enNode = PlanNode.withLocation(new LatLng(toPoint.lat,
                toPoint.lng));

        // 实际使用中请对起点终点城市进行正确的设定
        if (RoutePlanTypeEnum.DRIVING.equals(routePlanTypeEnum)) {
            mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
                    .to(enNode));
        } else if (RoutePlanTypeEnum.TRANSIT.equals(routePlanTypeEnum)) {
            mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
                    .city(city).to(enNode));
        } else {
            mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode)
                    .to(enNode));
        }
    }

    @Override
    public void doRoutePlanDriving(DGPoint fromPoint, DGPoint toPoint) {
        doRoutePlan(fromPoint, toPoint, RoutePlanTypeEnum.DRIVING, null);
    }

    @Override
    public void doRoutePlanTransit(DGPoint fromPoint, DGPoint toPoint,
                                   String city) {
        doRoutePlan(fromPoint, toPoint, RoutePlanTypeEnum.TRANSIT, city);
    }

    @Override
    public void doRoutePlanWalking(DGPoint fromPoint, DGPoint toPoint) {
        doRoutePlan(fromPoint, toPoint, RoutePlanTypeEnum.WALKING, null);
    }

    // //////////////////END DGRoutePlanInterface END///////////////////////////

    // //////////////////START DGLinkPointInterface START/////////
    @Override
    public void doLinkPoint(List<DGPoint> pointList, int strokeWidth,
                            int strokeColor, int fillColor) {
        List<LatLng> latLngList = new ArrayList<LatLng>();
        for (DGPoint p : pointList) {
            latLngList.add(new LatLng(p.lat, p.lng));
        }

//        // 构建用户绘制多边形的Option对象
//        OverlayOptions polygonOption = new PolygonOptions().points(latLngList)
//                .stroke(new Stroke(strokeWidth, strokeColor))
//                .fillColor(fillColor);
//        // 在地图上添加多边形Option，用于显示
//        mBaiduMap.addOverlay(polygonOption);

        //绘制两点之间的连线
        OverlayOptions ooPolyline = new PolylineOptions().width(strokeWidth)
                .color(strokeColor).points(latLngList);
        mBaiduMap.addOverlay(ooPolyline);
    }

    @Override
    public void doLinkPoint(List<DGPoint> pointList) {
        doLinkPoint(pointList, DEFAULT_STROKE_WIDTH, DEFAULT_STROKE_COLOR,
                DEFAULT_FILL_COLOR);
    }

    // //////////////////END DGLinkPointInterface END/////////

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * 子类复写，设置标题栏
     *
     * @return
     */
    protected View configTitleView() {
        return null;
    }

    /**
     * 子类复写，设置底部栏
     *
     * @return
     */
    protected View configBottomView() {
        return null;
    }

    /**
     * 子类复写，显示地图中心点
     *
     * @return
     */
    protected DGPoint configCenterPoint() {
        return null;
    }
}
