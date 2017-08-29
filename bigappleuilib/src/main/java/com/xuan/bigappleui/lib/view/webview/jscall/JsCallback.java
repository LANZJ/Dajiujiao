package com.xuan.bigappleui.lib.view.webview.jscall;

import java.util.HashMap;

/**
 * 全局回调
 * 
 * @author xuan
 * 
 */
public interface JsCallback {
	/**
	 * 处理回调
	 * 
	 * @param method
	 * @param params
	 */
	void call(String method, HashMap<String, String> params);
}