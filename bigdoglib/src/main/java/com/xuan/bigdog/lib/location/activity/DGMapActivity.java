package com.xuan.bigdog.lib.location.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.xuan.bigapple.lib.ioc.app.BPActivity;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.location.DGPoint;
import com.xuan.bigdog.lib.location.activity.entity.RoutePlanTypeEnum;
import com.xuan.bigdog.lib.location.activity.interfaces.DGLinkPointInterface;
import com.xuan.bigdog.lib.location.activity.interfaces.DGRoutePlanInterface;
import com.xuan.bigdog.lib.location.activity.interfaces.DGMarkLocationInterface;
import com.xuan.bigdog.lib.location.activity.listener.DGOnGetRoutePlanResultListener;

/**
 * 基础地图
 * 
 * @author xuan
 */
public class DGMapActivity extends BPActivity implements DGLinkPointInterface,
		DGRoutePlanInterface, DGMarkLocationInterface {
	private MapView mMapView;// 地图View
	private BaiduMap mBaiduMap;// 地图map

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置展示中心点
		initBaiduMapAndCenterPoint();
		// 设置View到界面
		initContentView();
	}

	// 初始化view
	private void initContentView() {
		View titleView = configTitleView();
		if (null != titleView) {
			LinearLayout ll = (LinearLayout) LayoutInflater.from(this).inflate(
					R.layout.dg_location_map, null);
			ll.addView(titleView);
			ll.addView(mMapView);
			setContentView(ll);
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
							.target(p).build()));
		} else {
			mMapView = new MapView(this, new BaiduMapOptions());
		}
		mBaiduMap = mMapView.getMap();
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

				MarkerOptions option = new MarkerOptions();
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

		// 构建用户绘制多边形的Option对象
		OverlayOptions polygonOption = new PolygonOptions().points(latLngList)
				.stroke(new Stroke(strokeWidth, strokeColor))
				.fillColor(fillColor);
		// 在地图上添加多边形Option，用于显示
		mBaiduMap.addOverlay(polygonOption);
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
	 * 子类复写，显示地图中心点
	 * 
	 * @return
	 */
	protected DGPoint configCenterPoint() {
		return null;
	}

}
