package com.xuan.bigdog.lib.location.activity.interfaces;

import com.xuan.bigdog.lib.location.DGPoint;
import com.xuan.bigdog.lib.location.activity.entity.RoutePlanTypeEnum;

/**
 * 路线规划接口
 * 
 * @author xuan
 */
public interface DGRoutePlanInterface {
	/**
	 * 路线规划接口
	 * 
	 * @param fromPoint
	 *            开始点
	 * @param toPoint
	 *            结束点
	 * @param routePlanTypeEnum
	 *            交通工具类型
	 * @param city
	 *            在做公交的时候需要设置当地城市
	 * @param reset
	 *            true会清理之前的点
	 */
	void doRoutePlan(DGPoint fromPoint, DGPoint toPoint,
					 RoutePlanTypeEnum routePlanTypeEnum, String city);

	/**
	 * 步行
	 * 
	 * @param fromPoint
	 * @param toPoint
	 */
	void doRoutePlanWalking(DGPoint fromPoint, DGPoint toPoint);

	/**
	 * 开车
	 * 
	 * @param fromPoint
	 * @param toPoint
	 */
	void doRoutePlanDriving(DGPoint fromPoint, DGPoint toPoint);

	/**
	 * 公交
	 * 
	 * @param fromPoint
	 * @param toPoint
	 * @param city
	 */
	void doRoutePlanTransit(DGPoint fromPoint, DGPoint toPoint, String city);

}
