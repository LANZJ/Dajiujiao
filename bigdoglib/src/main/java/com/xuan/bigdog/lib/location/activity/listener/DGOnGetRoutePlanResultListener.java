package com.xuan.bigdog.lib.location.activity.listener;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xuan.bigapple.lib.utils.ToastUtils;
import com.xuan.bigdog.lib.location.activity.overlay.DGDrivingRouteOverlay;
import com.xuan.bigdog.lib.location.activity.overlay.DGTransitRouteOverlay;
import com.xuan.bigdog.lib.location.activity.overlay.DGWalkingRouteOverlay;

/**
 * 路线规划回调
 * 
 * @author xuan
 */
public class DGOnGetRoutePlanResultListener implements
		OnGetRoutePlanResultListener {
	private final Context context;
	private final BaiduMap baiduMap;

	public DGOnGetRoutePlanResultListener(Context context, BaiduMap baiduMap) {
		this.context = context;
		this.baiduMap = baiduMap;
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (null == result || SearchResult.ERRORNO.NO_ERROR != result.error) {
			ToastUtils.displayTextShort("抱歉，未找到结果");
		}
		if (SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR == result.error) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			ToastUtils.displayTextShort("起终点或途经点地址有岐义");
			return;
		}
		if (SearchResult.ERRORNO.NO_ERROR == result.error) {
			WalkingRouteOverlay overlay = new DGWalkingRouteOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if (null == result || SearchResult.ERRORNO.NO_ERROR != result.error) {
			ToastUtils.displayTextShort("抱歉，未找到结果");
		}
		if (SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR == result.error) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			ToastUtils.displayTextShort("起终点或途经点地址有岐义");
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			TransitRouteOverlay overlay = new DGTransitRouteOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (null == result || SearchResult.ERRORNO.NO_ERROR != result.error) {
			ToastUtils.displayTextShort("抱歉，未找到结果");
		}
		if (SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR == result.error) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			ToastUtils.displayTextShort("起终点或途经点地址有岐义");
			return;
		}

		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			DrivingRouteOverlay overlay = new DGDrivingRouteOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

}
