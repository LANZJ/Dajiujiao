package com.zjyeshi.dajiujiao.buyer.utils;

import android.content.Context;

import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.xuan.bigapple.lib.http.BPHttpUtils;
import com.xuan.bigapple.lib.http.BPResponse;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class HttpUtil {
	private static final String TIMESTAMP = "timestamp";
	private static final String SIGNATURE = "signature";
	private static final String USERID = "userId";
	private static final String TOKEN = "token";
	private static final String VER = "ver";
	private static final String VALUE_VER = "2";
	/**
	 * 提交参数(无通用参数)
	 * 
	 * @param context
	 * @param url
	 * @param paramsMap
	 * @return
	 */
	public static BPResponse post(Context context, String url,
			Map<String, String> paramsMap) {
//		setCommonParams(paramsMap, context);
		displayLog(url, paramsMap);
		BPResponse response = null;

		try {
			response = BPHttpUtils.post(url, paramsMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response = new BPResponse();
			response.setStatusCode(-1);
			response.setResultStr(e.getMessage());
		}

		return response;
	}

	/**
	 * 提交参数(包含通用参数)
	 *
	 * @param context
	 * @param url
	 * @param paramsMap
	 * @return
	 */
	public static BPResponse postCommom(Context context, String url,
								  Map<String, String> paramsMap) {
		setCommonParams(paramsMap, context);
		displayLog(url, paramsMap);
		BPResponse response = null;

		try {
			response = BPHttpUtils.post(url, paramsMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response = new BPResponse();
			response.setStatusCode(-1);
			response.setResultStr(e.getMessage());
		}

		return response;
	}

	/** 设置通用参数
	 *
	 * @param paramMap
	 * @param context
	 */
	public static void setCommonParams(Map<String, String> paramMap,
			Context context) {
		paramMap.put(USERID,
				String.valueOf(LoginedUser.getLoginedUser().getId()));
		paramMap.put(VER, VALUE_VER);
		String timestamp = String.valueOf(System.currentTimeMillis());
		paramMap.put(TIMESTAMP, timestamp);
		paramMap.put(SIGNATURE, getSignature(context, paramMap));
	}

	/**
	 * 获取秘钥
	 * 
	 * @param context
	 * @param paramMap
	 * @return
	 */
	public static String getSignature(Context context,
			Map<String, String> paramMap) {
		List<String> valueList = new ArrayList<String>(paramMap.values());
		valueList.add(LoginedUser.getLoginedUser().getToken());
		Collections.sort(valueList);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < valueList.size(); i++) {
			sb.append(valueList.get(i));
		}
		LogUtil.debug(sb.toString());
		LogUtil.debug("访问签名前的内容" + sb.toString());
		return SecurityUtil.encodeByMD5(sb.toString());
	}

	/**
	 * 打印请求日志
	 * 
	 * @param url
	 * @param paramsMap
	 */
	private static void displayLog(String url, Map<String, String> paramsMap) {
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append("?");
		for (Map.Entry<String, String> e : paramsMap.entrySet()) {
			sb.append(e.getKey()).append("=")
					.append(URLEncoder.encode(e.getValue().trim())).append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		LogUtil.debug("请求地址：" + sb.toString());
	}

}
