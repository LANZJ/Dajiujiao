package com.xuan.bigdog.lib.location;

/**
 * 点对象，可以表示[x,y]点，也可以表示经纬度
 * 
 * @author xuan
 */
public class DGPoint {
	public double lat;
	public double lng;

	public DGPoint() {

	}

	public DGPoint(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

}
