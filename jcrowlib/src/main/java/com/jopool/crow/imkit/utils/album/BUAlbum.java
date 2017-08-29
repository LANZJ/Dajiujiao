package com.jopool.crow.imkit.utils.album;

import android.app.Activity;
import android.content.Intent;

import com.jopool.crow.imkit.utils.album.activity.BucketActivity;
import com.jopool.crow.imkit.utils.album.entity.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类可以启动相册Activity的工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-12 下午1:35:16 $
 */
public abstract class BUAlbum {
	// ///////////////////////////////常量/////////////////////////////////
	/** 表示是否单选 */
	public static final String PARAM_IF_MULTIPLE_CHOICE = "param.if.multiple.choice";

	/** 设置可选限制参数，如果用户不设置的话，表示是可以选无限个 */
	public static final String PARAM_LIMIT_COUNT = "param.limit.count";

	// ///////////////////////////////图片返回集合/////////////////////////////////
	/**
	 * 点击确定后选中的图片，用户应该调用getSelList方法获取，而不是直接使用selList
	 */
	public static List<ImageItem> selList = new ArrayList<ImageItem>();

	/**
	 * 获取选中的图片，而后进行清理
	 * 
	 * @return
	 */
	public static List<ImageItem> getSelListAndClear() {
		List<ImageItem> temp = new ArrayList<ImageItem>(selList);
		selList.clear();
		return temp;
	}

	// ///////////////////////////////设置相册UI/////////////////////////////////
	private static BUAlbumConfig config = new BUAlbumConfig();

	public static BUAlbumConfig getConfig() {
		return config;
	}

	public static void setConfig(BUAlbumConfig config) {
		BUAlbum.config = config;
	}

	// ///////////////////////////////启动相册/////////////////////////////////
	/**
	 * 启动相册，多选操作，限制选择张数
	 * 
	 * @param activity
	 * @param limitCount
	 *            限制张数
	 * @param requestCode
	 */
	public static void gotoAlbumForMulti(Activity activity, int limitCount,
			int requestCode) {
		Intent intent = new Intent();
		intent.setClass(activity, BucketActivity.class);
		intent.putExtra(BUAlbum.PARAM_LIMIT_COUNT, limitCount);
		intent.putExtra(BUAlbum.PARAM_IF_MULTIPLE_CHOICE, true);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动相册，多选操作，可以无限制的选择张数
	 * 
	 * @param activity
	 * @param requestCode
	 *            返回标识
	 */
	public static void gotoAlbumForMulti(Activity activity, int requestCode) {
		gotoAlbumForMulti(activity, -1, requestCode);
	}

	/**
	 * 启动相册，单选操作
	 * 
	 * @param activity
	 * @param requestCode
	 *            返回标识
	 */
	public static void gotoAlbumForSingle(Activity activity, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(activity, BucketActivity.class);
		intent.putExtra(BUAlbum.PARAM_LIMIT_COUNT, -1);
		intent.putExtra(BUAlbum.PARAM_IF_MULTIPLE_CHOICE, false);
        //intent.putExtra(BUAlbum.PARAM_IF_MULTIPLE_CHOICE, true);
		activity.startActivityForResult(intent, requestCode);
	}

}
