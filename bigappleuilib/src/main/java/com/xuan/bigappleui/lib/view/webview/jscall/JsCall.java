package com.xuan.bigappleui.lib.view.webview.jscall;

import android.util.Log;
import android.webkit.WebView;

import com.xuan.bigappleui.lib.utils.BUStringUtil;
import com.xuan.bigappleui.lib.utils.BUValidator;

import java.net.URLDecoder;
import java.util.HashMap;

/**
 * js调用本地代码协议
 * 
 * @author xuan
 */
public abstract class JsCall {
	private static final String TAG = "Bigdog.JsCall";

	private static final String LOCAL_PROTOCOL = "local://";

	/**
	 *  处理地址
	 *
	 * @param webView
	 * @param url
	 * @param jsCallback
	 */
	public static void call(WebView webView, String url, JsCallback jsCallback) {
		if (BUValidator.isEmpty(url)) {
			Log.d(TAG, "Url is empty!!!");
			return;
		}

		if (url.startsWith(LOCAL_PROTOCOL)) {
			// 解析地址串和参数串
			String[] urlStrAndParamsStr = BUStringUtil.split(url, "?");
			if (urlStrAndParamsStr.length == 1) {
				// 没有参数
				if(null != jsCallback){
					jsCallback.call(
							urlStrAndParamsStr[0].replaceAll(LOCAL_PROTOCOL, ""),
							new HashMap<String, String>());
				}
			} else if (urlStrAndParamsStr.length == 2) {
				// 带参数
				String urlStr = urlStrAndParamsStr[0].replaceAll(
						LOCAL_PROTOCOL, "");
				String paramsStr = urlStrAndParamsStr[1];

				// 解析参数串
				String[] params = BUStringUtil.split(paramsStr, "&");
				HashMap<String, String> paramMap = new HashMap<String, String>();
				for (String param : params) {
					String[] keyAndValue = BUStringUtil.split(param, "=");
					paramMap.put(keyAndValue[0],
							URLDecoder.decode(keyAndValue[1]));
				}
				if(null != jsCallback){
					jsCallback.call(urlStr, paramMap);
				}
			}
		} else {
			webView.loadUrl(url);
		}
	}

}
