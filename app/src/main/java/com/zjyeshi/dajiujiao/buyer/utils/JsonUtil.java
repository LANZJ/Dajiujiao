package com.zjyeshi.dajiujiao.buyer.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Json解析工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-26 下午5:54:32 $
 */
public abstract class JsonUtil {

	public static String getString(JSONObject jsonObject, String name)
			throws Exception {
		return jsonObject.has(name) ? jsonObject.getString(name) : "";
	}

	public static int getInt(JSONObject jsonObject, String name)
			throws Exception {
		return jsonObject.has(name) ? jsonObject.getInt(name) : 0;
	}

	public static boolean getBoolean(JSONObject jsonObject, String name)
			throws Exception {
		return jsonObject.has(name) ? jsonObject.getBoolean(name) : false;
	}

	public static double getDouble(JSONObject jsonObject, String name)
			throws Exception {
		return jsonObject.has(name) ? jsonObject.getDouble(name) : 0;
	}

	public static long getLang(JSONObject jsonObject, String name)
			throws Exception {
		return jsonObject.has(name) ? jsonObject.getLong(name) : 0;
	}

	public static JSONObject getJSONObject(JSONObject jsonObject, String name)
			throws Exception {
		return jsonObject.has(name) ? jsonObject.getJSONObject(name) : null;
	}

	public static JSONArray getJSONArray(JSONObject jsonObject, String name)
			throws Exception {
		return jsonObject.has(name) ? jsonObject.getJSONArray(name) : null;
	}

}
