package com.xuan.bigdog.lib.update.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络请求判断
 *
 * @author xuan
 */
public class NetWorkUtils {
    private final Context mContext;
    private final ConnectivityManager mConnectivityManager;
    private final NetworkInfo mNetworkInfo;

    public NetWorkUtils(Context context) {
        this.mContext = context;
        mConnectivityManager = (ConnectivityManager) this.mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
    }

    /**
     * 判断网络是否连接
     *
     * @return
     */
    public boolean isConnected() {
        return null != mNetworkInfo && mNetworkInfo.isConnected();
    }

    /**
     * 获取网络连接状态
     * 0: 无网络， 1:WIFI， 2:其他（流量）
     *
     * @return
     */
    public int getNetType() {
        if (!isConnected()) {
            return 0;
        }
        int type = mNetworkInfo.getType();
        if (type == ConnectivityManager.TYPE_WIFI) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * HTTP地址校验
     *
     * @param url
     * @return
     */
    public static boolean isHttpUrl(String url) {
        return (null != url) && (url.length() > 6)
                && url.substring(0, 7).equalsIgnoreCase("http://");
    }

    /**
     * HTTPS地址校验
     *
     * @param url
     * @return
     */
    public static boolean isHttpsUrl(String url) {
        return (null != url) && (url.length() > 7)
                && url.substring(0, 8).equalsIgnoreCase("https://");
    }

    /**
     * HTTPS或者HTTP地址校验
     *
     * @param url
     * @return
     */
    public static boolean isNetworkUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        return isHttpUrl(url) || isHttpsUrl(url);
    }

}