package com.xuan.bigdog.lib.update.core;

import com.xuan.bigapple.lib.utils.log.LogUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 从url地址中读取输入流
 *
 * @author xuan
 */
public class HttpRequest {

    /**
     * 请求下载
     *
     * @param url
     * @return
     */
    public static InputStream get(String url) {
        try {
            URL urlPath = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlPath
                    .openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            //httpURLConnection.connect();

            InputStream inputStream = null;
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }else{
                LogUtils.e("请求["+url+"]错误吗["+ httpURLConnection.getResponseCode()+"]");
            }

            return inputStream;
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return null;
        }
    }

}
