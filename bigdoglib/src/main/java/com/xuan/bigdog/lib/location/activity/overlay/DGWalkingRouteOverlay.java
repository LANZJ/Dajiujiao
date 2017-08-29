package com.xuan.bigdog.lib.location.activity.overlay;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;

/**
 * 步行
 * 
 * @author xuan
 */
public class DGWalkingRouteOverlay extends WalkingRouteOverlay {
	private BitmapDescriptor startMarker;
	private BitmapDescriptor terminalMarker;

	public DGWalkingRouteOverlay(BaiduMap baiduMap) {
		super(baiduMap);
	}

	public DGWalkingRouteOverlay(BaiduMap baiduMap,
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
