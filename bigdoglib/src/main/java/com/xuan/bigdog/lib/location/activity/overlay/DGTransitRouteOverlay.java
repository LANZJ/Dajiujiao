package com.xuan.bigdog.lib.location.activity.overlay;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;

/**
 * 做公交
 * 
 * @author xuan
 */
public class DGTransitRouteOverlay extends TransitRouteOverlay {
	private BitmapDescriptor startMarker;
	private BitmapDescriptor terminalMarker;

	public DGTransitRouteOverlay(BaiduMap baiduMap) {
		super(baiduMap);
	}

	public DGTransitRouteOverlay(BaiduMap baiduMap,
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
