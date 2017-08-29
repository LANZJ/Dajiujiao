package com.xuan.bigdog.lib.location.activity.overlay;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;

/**
 * 开车
 * 
 * @author xuan
 */
public class DGDrivingRouteOverlay extends DrivingRouteOverlay {
	private BitmapDescriptor startMarker;
	private BitmapDescriptor terminalMarker;

	public DGDrivingRouteOverlay(BaiduMap baiduMap) {
		super(baiduMap);
	}

	public DGDrivingRouteOverlay(BaiduMap baiduMap,
			BitmapDescriptor startMarker, BitmapDescriptor terminalMarker) {
		super(baiduMap);
		this.startMarker = startMarker;
		this.terminalMarker = terminalMarker;
	}

	@Override
	public BitmapDescriptor getStartMarker() {
		return startMarker;
	}

	@Override
	public BitmapDescriptor getTerminalMarker() {
		return terminalMarker;
	}

}
