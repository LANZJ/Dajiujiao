package com.xuan.bigdog.lib.location.activity.interfaces;

import java.util.List;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.xuan.bigdog.lib.R;
import com.xuan.bigdog.lib.location.DGPoint;

/**
 * 在地图上标记
 * 
 * @author xuan
 * 
 */
public interface DGMarkLocationInterface {
	/**
	 * 标记默认图标
	 */
	public static BitmapDescriptor DEFAULT_LOCATION_BITMAP = BitmapDescriptorFactory
			.fromResource(R.drawable.dg_location_default_loc_mark);

	/**
	 * 在地图上话标记
	 * 
	 * @param locationPointList
	 * @param locationPointBitmapList
	 */
	void markLocation(List<DGPoint> locationPointList,
					  List<BitmapDescriptor> locationPointBitmapList);

	/**
	 * 在地图上话标记
	 * 
	 * @param locationPointList
	 */
	void markLocation(List<DGPoint> locationPointList);

}
